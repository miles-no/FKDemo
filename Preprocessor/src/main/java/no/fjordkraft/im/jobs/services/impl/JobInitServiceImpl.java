package no.fjordkraft.im.jobs.services.impl;


import no.fjordkraft.im.jobs.domain.Job;
import no.fjordkraft.im.jobs.domain.JobStatus;
import no.fjordkraft.im.jobs.services.JobInitService;
import no.fjordkraft.im.jobs.services.JobSchedulerService;
import no.fjordkraft.im.jobs.services.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service("JobInitService")
public class JobInitServiceImpl implements JobInitService {
    private static final Logger logger = LoggerFactory.getLogger(JobInitServiceImpl.class);
    @Autowired
    private JobService jobService;

    @Autowired
    private JobSchedulerService jobSchedulerService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void initializeJob(Job job, List<Job> existingJobs) {
        Job existingJob = findJobByJobClass(job, existingJobs);
        if (job.getStatus().equals(JobStatus.deprecated)) {
            initializeJobDeprecate(job, existingJob);
        } /*else if (!checkEnvironment(job.getJobEnvironment())) {
            initializeJobDeprecate(job, existingJob);
        } */else {
            if (existingJob == null) {
                initializeJobCreate(job);
            } else {
                initializeJobUpdate(job, existingJob);
            }
        }
    }

   /* private boolean checkEnvironment(JobEnvEnum jobEnv) {
        String environment = configService.getEnvironmentMessage();

        // Prod: environment is null / blank
        if (environment == null || environment.length() == 0) {
            return JobEnvEnum.all.equals(jobEnv);
        }

        // Other environments: TEST, UTV, LOCAL
        return (JobEnvEnum.all.equals(jobEnv) || environment.equals(jobEnv.toString()));
    }*/

    private Job findJobByJobClass(Job job, List<Job> existingJobs) {
        if (job == null || job.getJobClass() == null) {
            throw new IllegalArgumentException("Job is null or job.jobClass is null");
        }
        if (existingJobs == null) {
            return null;
        }
        for (Job existingJob : existingJobs) {
            if (existingJob.getJobClass().equals(job.getJobClass())) {
                return existingJob;
            }
        }
        return null;
    }

    private void initializeJobCreate(Job job) {
        job.setObid(null);
        Job jobCreated = jobService.createJob(job);
        if (job.getStatus() == JobStatus.enabled) {
            jobCreated = jobService.enableJob(jobCreated.getId());
        }
        logger.info("Job created: " + jobCreated.toString());
    }

    private void initializeJobUpdate(Job job, Job existingJob) {
        boolean update = false;
        if (!existingJob.getName().equals(job.getName())) {
            existingJob.setName(job.getName());
            update = true;
        }
        /*if (!existingJob.getJobCategory().equals(job.getJobCategory())) {
            existingJob.setJobCategory(job.getJobCategory());
            update = true;
        }*/
        if (!existingJob.getManualAllowed().equals(job.getManualAllowed())) {
            existingJob.setManualAllowed(job.getManualAllowed());
            update = true;
        }
        if (existingJob.getEditAllowed() == null || !existingJob.getEditAllowed().equals(job.getEditAllowed())) {
            existingJob.setEditAllowed(job.getEditAllowed());
            update = true;
        }
        if (!existingJob.getSchedule().equals(job.getSchedule())) {
            existingJob.setSchedule(job.getSchedule());
            update = true;
        }
        if (update) {
            boolean updateJob;
            try {
                jobSchedulerService.updateSchedulerOneJob(existingJob);
                updateJob = true;
            } catch (Throwable t) {
                logger.error("Exception updating Obsidian job ", t);
                updateJob = false;
            }
            if (updateJob) {
                jobService.updateJob(existingJob);
                logger.info("Job updated: " + existingJob.toString());
            } else {
                logger.error("Job NOT updated due to exception");
            }
        }
    }

    private void initializeJobDeprecate(Job job, Job existingJob) {
        if (existingJob != null) {
            logger.info("Job deleted: " + existingJob.toString());
            jobService.deleteJob(existingJob);
        }
    }
}