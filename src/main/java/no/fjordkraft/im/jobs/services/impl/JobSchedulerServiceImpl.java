package no.fjordkraft.im.jobs.services.impl;

import com.carfey.ops.api.bean.history.*;
import com.carfey.ops.api.bean.host.Host;
import com.carfey.ops.api.bean.host.HostListing;

import com.carfey.ops.api.bean.job.*;
import com.carfey.ops.api.bean.schedule.ScheduleCreationRequest;
import com.carfey.ops.api.embedded.HostManager;
import com.carfey.ops.api.embedded.JobManager;
import com.carfey.ops.api.embedded.MissingEntityException;
import com.carfey.ops.api.embedded.RuntimeManager;
import com.carfey.ops.api.enums.JobRecoveryType;
import com.carfey.ops.api.enums.JobRuntimeStatus;
import com.carfey.ops.api.enums.JobStatus;
import com.carfey.ops.api.enums.ParameterType;
import com.carfey.suite.action.ValidationException;

import no.fjordkraft.im.jobs.domain.Job;
import no.fjordkraft.im.jobs.domain.JobHost;
import no.fjordkraft.im.jobs.repository.JobRepository;
import no.fjordkraft.im.jobs.services.JobSchedulerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service("jobSchedulerService")
public class JobSchedulerServiceImpl implements JobSchedulerService {

    public static final String JOB_HASH_PARAM = "jobhash";
    public static final String OBSIDIAN_AUDIT_USER = "admin";

    private static Logger logger = LoggerFactory
            .getLogger(JobSchedulerServiceImpl.class);

    @Autowired
    private JobRepository jobRepository;

    @Override
    public List<Job> findAllScheduledJobs() {
        List<JobDetailInternal> jobsInternal = getSchedulerJobListing();
        jobsInternal = getSchedulerJobDetails(jobsInternal);
        List<Job> jobs = new ArrayList<Job>();
        mapJobs(jobsInternal, jobs);
        return jobs;
    }


    @Override
    public void updateSchedulerAllJobs(List<Job> jobs) {
        if (jobs == null) {
            throw new IllegalArgumentException("jobs is null");
        }

        List<JobDetailInternal> jobListing = getSchedulerJobListing();

        // Disable jobs?
        for (JobDetailInternal jdi : jobListing) {
            long obsidianId = jdi.getJobState().getJobId();
            Job jobExists = searchForJobByObsidianId(jobs, obsidianId);
            if (jobExists == null) {
                logger.warn("Job does not exist in job - table.  Will disable. obsidianId = " + obsidianId);
                updateScheduleForObsidianJob(obsidianId, null, JobStatus.DISABLED, OBSIDIAN_AUDIT_USER);
            }
        }

        // Create or update jobs?
        for (Job job : jobs) {
            updateSchedulerOneJob(job);
        }
    }

    @Override
    public void updateSchedulerOneJob(Job job) {
        if (job == null) {
            throw new IllegalArgumentException("job is null");
        }

        logger.info("updateSchedulerOneJob: " + job);

        List<JobDetailInternal> jobListing = getSchedulerJobListing();
        JobDetailInternal jdi = null;
        if (job.getObid() != null) {
            jdi = searchForJobInternalByObsidianId(jobListing, job.getObid());
        }

        boolean jobEnabled = no.fjordkraft.im.jobs.domain.JobStatus.enabled.equals(job.getStatus());

        if (jdi == null && jobEnabled) {
            createObsidianJob(job, OBSIDIAN_AUDIT_USER);
        }

        if (jdi != null) {
            if (jobEnabled) {
                updateObsidianJob(job, OBSIDIAN_AUDIT_USER);
            }
            updateScheduleForObsidianJob(job.getObid(), job.getSchedule(),
                    jobEnabled ? JobStatus.ENABLED : JobStatus.DISABLED,
                    OBSIDIAN_AUDIT_USER
            );
        }
    }

    @Override
    public long runJobAdHoc(long obsidianId) {
        RuntimeSubmissionResult result = null;
        try {
            RuntimeManager runtimeManager = new RuntimeManager();
            RuntimeSubmissionRequest request = new RuntimeSubmissionRequest();
            result = runtimeManager.submitRuntime(obsidianId, request, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (result != null ? result.getJobRuntimeId() : 0);
    }

    @Override
    public int abortRunningJobs(long obsidianId) {
        return abortJobs(getRunningJobs(obsidianId));
    }

    @Override
    public int abortAllRunningJobs() {
        return abortJobs(getRunningJobs(0));
    }

    private RuntimeListing getRunningJobs(long obsidianId) {
        RuntimeListing runtimeListing = null;
        RuntimeManager runtimeManager = new RuntimeManager();
        try {
            RuntimeListingParameters parameters = new RuntimeListingParameters();
            if (obsidianId > 0) {
                List<Long> jobIds = new ArrayList<Long>();
                jobIds.add(obsidianId);
                parameters.setJobIds(jobIds);
            }
            List<JobRuntimeStatus> jobRuntimeStatuses = new ArrayList<JobRuntimeStatus>();
            jobRuntimeStatuses.add(JobRuntimeStatus.RUNNING);
            parameters.setStatuses(jobRuntimeStatuses);
            runtimeListing = runtimeManager.listRuntimes(parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return runtimeListing;
    }

    private int abortJobs(RuntimeListing runtimeListing) {
        int numInterruptedJobs = 0;
        RuntimeManager runtimeManager = new RuntimeManager();
        if (runtimeListing != null && runtimeListing.getRuntimes() != null) {
            for (com.carfey.ops.api.bean.history.Runtime runtime : runtimeListing.getRuntimes()) {
                try {
                    logger.info("Will try to abort running job with obsidianId = " + runtime.getJob().getJobId() +
                            ", jobRuntimeId: " + runtime.getJobRuntimeId());
                    JobInterruptResult jir = runtimeManager.interruptRuntime(runtime.getJobRuntimeId(), OBSIDIAN_AUDIT_USER);
                    numInterruptedJobs++;
                    logger.info("Job interrupted for job with obsidianId = " + runtime.getJob().getJobId()  + (jir != null ? ": " + jir.toString() : ""));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return numInterruptedJobs;
    }

    private List<JobDetailInternal> getSchedulerJobListing() {
        JobManager jobManager = new JobManager();
        JobListing jobs = null;
        try {
            jobs = jobManager.listJobs(new JobListingParameters());
        } catch (ValidationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<JobDetailInternal> jobsInternal = new ArrayList<JobDetailInternal>();
        if (jobs != null && jobs.getJobs() != null) {
            for (JobState js : jobs.getJobs()) {
                jobsInternal.add(new JobDetailInternal(null, null, js));
            }
        }
        return jobsInternal;
    }

    private List<JobDetailInternal> getSchedulerJobDetails(List<JobDetailInternal> jobs) {
        for (JobDetailInternal jdi : jobs) {
            if (jdi.getJobState() == null) {
                throw new IllegalStateException("Missing jobState in JobDetailInternal list");
            }
            JobDetail jobDetail = getJobDetailByObsidianId(jdi.getJobState().getJobId());
            jdi.setJobDetail(jobDetail);
        }
        return jobs;
    }

    private JobDetail getJobDetailByObsidianId(long obsidianJobId) {
        JobManager jobManager = new JobManager();
        JobDetail jobDetail = null;
        try {
            jobDetail = jobManager.getJob(obsidianJobId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jobDetail;
    }

    private Job searchForJobByObsidianId(List<Job> jobs, long obsidianId) {
        if (jobs != null) {
            for (Job job : jobs) {
                if (job.getObid() == obsidianId) {
                    return job;
                }
            }
        }
        return null;
    }

    private JobDetailInternal searchForJobInternalByObsidianId(List<JobDetailInternal> jobsInternal, long obsidianId) {
        for (JobDetailInternal jdi : jobsInternal) {
            if (jdi.getJobState() == null) {
                continue;
            }
            if (jdi.getJobState().getJobId() == obsidianId) {
                return jdi;
            }
        }
        logger.warn("Unable to find Obsidian job with id: " + obsidianId);
        return null;
    }

    private JobDetailInternal searchForJobInternalByJobHash(List<JobDetailInternal> jobsInternal, String jobHash) {
        if (jobHash == null) {
            throw new NullPointerException("jobHash is null");
        }
        for (JobDetailInternal jdi : jobsInternal) {
            if (jdi.getJobDetail() == null) {
                continue;
            }
            List<ParameterDefinition> params = jdi.getJobDetail().getParameters();
            if (params == null) {
                continue;
            }
            for (ParameterDefinition pd : params) {
                if (!JOB_HASH_PARAM.equals(pd.getName())) {
                    continue;
                }
                List<String> values = pd.getValues();
                if (values == null) {
                    continue;
                }
                if (values.size() > 1) {
                    logger.warn("more than one jobhash for job with jobhash = " + jobHash);
                }
                for (String v : values) {
                    if (jobHash.equals(v)) {
                        return jdi;
                    }
                }
            }
        }
        logger.warn("Unable to find Obsidian job with hash: " + jobHash);
        return null;
    }

    private void createObsidianJob(Job job, String auditUser) {
        if (job == null) {
            throw new IllegalArgumentException("job is null");
        }

        logger.info("createObsidianJob: " + job);

        JobManager jobManager = new JobManager();
        JobCreationRequest request = new JobCreationRequest();

        request.setJobClass(job.getJobClass());
        request.setNickname(job.getName());
        List<ConfigurationParameter> parameters = new ArrayList<ConfigurationParameter>();
        ConfigurationParameter jobHashParam = new ConfigurationParameter();
        jobHashParam.setName(JOB_HASH_PARAM);
        jobHashParam.setType(ParameterType.STRING);
        jobHashParam.setValue(job.getId());
        parameters.add(jobHashParam);
        request.setParameters(parameters);
        request.setRecoveryType(JobRecoveryType.NONE);
        request.setSchedule(job.getSchedule());
        request.setState(JobStatus.ENABLED);
        //request.set

        JobDetail jobDetail = null;

        try {
            jobDetail = jobManager.addJob(request, auditUser);
        } catch (Exception e) {
            logger.error("An error occured:", e);
            throw new RuntimeException(e);
        }
        job.setObid(jobDetail.getJob().getJobId());
    }

    private void deleteObsidianJob(Job job, String auditUser) {
        if (job == null) {
            throw new IllegalArgumentException("job is null");
        }
        logger.info("deleteObsidianJob: " + job);

        if (job.getObid() == null) {
            logger.warn("missing job.obid for job.  Unable to delete obsidian job");
            return;
        }

        JobManager jobManager = new JobManager();

        try {
            jobManager.deleteJob(job.getObid(), true, auditUser);
        } catch (MissingEntityException e) {
            e.printStackTrace();
        } catch (ValidationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateObsidianJob(Job job, String auditUser) {
        if (job == null) {
            throw new IllegalArgumentException("job is null");
        }

        logger.info("updateObsidianJob: " + job);

        if (job.getObid() == null) {
            logger.warn("missing job.obid for job.  Unable to update obsidian job");
            return;
        }
        JobManager jobManager = new JobManager();
        JobUpdateRequest request = new JobUpdateRequest();
        request.setNickname(job.getName());

        try {
            jobManager.updateJob(job.getObid(), request, auditUser);
        } catch (MissingEntityException e) {
            e.printStackTrace();
        } catch (ValidationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateScheduleForObsidianJob(long obsidianId, String schedule, JobStatus jobStatus, String auditUser) {
        String msg = "updateScheduleForObsidianJob: " + obsidianId + ", schedule: " + schedule + ", jobStatus: " + jobStatus;
        logger.info(msg);
        System.out.println(msg);
        JobManager jobManager = new JobManager();
        ScheduleCreationRequest request = new ScheduleCreationRequest();
        if (schedule != null && JobStatus.ENABLED.equals(jobStatus)) {
            request.setSchedule(schedule);
        }
        request.setState(jobStatus);

        try {
            jobManager.addJobSchedule(obsidianId, request, auditUser);
        } catch (MissingEntityException e) {
            e.printStackTrace();
        } catch (ValidationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mapJobs(List<JobDetailInternal> jobsInternal, List<Job> jobs) {
        for (JobDetailInternal jdi : jobsInternal) {
            Job job = jdi.getJob();
            if (job == null) {
                job = new Job();
                jdi.setJob(job);
            }
            mapJob(jdi);
            jobs.add(jdi.getJob());
        }
    }

    private void mapJob(JobDetailInternal jdi) {
        Job job = jdi.getJob();
        if (jdi.getJobDetail() != null) {
            job.setObid(jdi.getJobDetail().getJob().getJobId());
            job.setName(jdi.getJobDetail().getJob().getNickname());
            job.setJobClass(jdi.getJobDetail().getJob().getJobClass());
        }
        if (jdi.getJobState() != null) {
            job.setName(jdi.getJobState().getNickname());
            job.setJobClass(jdi.getJobState().getJobClass());
        }
    }

    @Override
    public List<JobHost> getHosts() {
        HostListing hostListing = null;
        try {
            HostManager hostManager = new HostManager();
            hostListing = hostManager.listHosts();
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<JobHost> retList = new ArrayList<JobHost>();
        mapHosts(retList, hostListing);
        return retList;
    }

    private void mapHosts(List<JobHost> retList, HostListing hostListing) {
        if (hostListing != null && hostListing.getHosts() != null) {
            for (Host h : hostListing.getHosts()) {
                retList.add(mapToHost(h));
            }
        }
    }

    private JobHost mapToHost(Host h) {
        JobHost jobHost = new JobHost();
        jobHost.setId(h.getId());
        jobHost.setName(h.getName());
        jobHost.setHeartbeatTime(new Timestamp(h.getHeartbeatTime().getMillis()));
        return jobHost;
    }

    class JobDetailInternal {
        private Job job;
        private JobDetail jobDetail;
        private JobState jobState;

        public JobDetailInternal(Job job, JobDetail jobDetail, JobState jobState) {
            this.job = job;
            this.jobDetail = jobDetail;
            this.jobState = jobState;
        }

        public Job getJob() {
            return job;
        }

        public void setJob(Job job) {
            this.job = job;
        }

        public JobDetail getJobDetail() {
            return jobDetail;
        }

        public void setJobDetail(JobDetail jobDetail) {
            this.jobDetail = jobDetail;
        }

        public JobState getJobState() {
            return jobState;
        }

        public void setJobState(JobState jobState) {
            this.jobState = jobState;
        }
    }

}
