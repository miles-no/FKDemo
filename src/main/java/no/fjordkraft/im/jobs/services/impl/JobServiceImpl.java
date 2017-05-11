package no.fjordkraft.im.jobs.services.impl;


import no.fjordkraft.im.jobs.domain.Job;
import no.fjordkraft.im.jobs.domain.JobFetchRequest;
import no.fjordkraft.im.jobs.domain.JobStatus;
import no.fjordkraft.im.jobs.repository.JobRepository;
import no.fjordkraft.im.jobs.services.JobInitService;
import no.fjordkraft.im.jobs.services.JobSchedulerService;
import no.fjordkraft.im.jobs.services.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service("jobService")
public class JobServiceImpl implements JobService {

    private static final Logger logger = LoggerFactory.getLogger(JobServiceImpl.class);

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobInitService jobInitService;

    @Autowired
    private JobSchedulerService jobSchedulerService;



    @Transactional(readOnly = true)
    @Override
    public List<Job> findAll() {
        return jobRepository.findAll();
    }


    @Transactional(readOnly = true)
    @Override
    public Job getJob(String id) {
        return jobRepository.findOne(id);
    }

    @Transactional
    @Override
    public Job createJob(Job job) {
        //job.setStatus(JobStatus.disabled);
        Job newJob = jobRepository.saveAndFlush(job);
        return newJob;
    }

    @Transactional
    @Override
    public Job updateJob(Job job) {
        return jobRepository.saveAndFlush(job);
    }

    @Transactional
    @Override
    public void deleteJob(Job job) {
        disableJob(job.getId());
        jobRepository.delete(job);
    }

    @Transactional
    @Override
    public Job enableJob(String id) {
        Job job = jobRepository.findOne(id);
        job.setStatus(JobStatus.enabled);
        jobSchedulerService.updateSchedulerOneJob(job);
        return updateJob(job);
    }

    @Transactional
    @Override
    public Job disableJob(String id) {
        Job job = jobRepository.findOne(id);
        job.setStatus(JobStatus.disabled);
        jobSchedulerService.updateSchedulerOneJob(job);
        return updateJob(job);
    }

    @Override
    public long runJob(String id) {
        Job job = getJob(id);
        if (job == null) {
            // TODO throw exception
            return 0;
        }
        Long obid = job.getObid();
        if (obid == null) {
            // TODO throw exception
            return 0;
        }
        if (!job.getManualAllowed()) {
            // TODO throw exception
            return 0;
        }

        return jobSchedulerService.runJobAdHoc(obid);
    }

    @Override
    public boolean abortJob(String id) {
        Job job = getJob(id);
        if (job == null) {
            return false;
        }
        Long obid = job.getObid();
        if (obid == null) {
            return false;
        }
        int numInterruptedJobs = jobSchedulerService.abortRunningJobs(obid);
        return (numInterruptedJobs > 0);
    }

    @Override
    public void abortAllRunningJobs() {
        //log.info("Checking for jobs to be aborted...");
        int numInterruptedJobs = jobSchedulerService.abortAllRunningJobs();
        //log.info("Aborted " + numInterruptedJobs + " jobs");
    }

    @Transactional(readOnly = true)
    @Override
    public List<Job> getJobByClass(String className) {
        return jobRepository.findByJobClass(className);
    }

    @Transactional(readOnly = true)
    @Override
    public String getJobIdByObId(long obId) {
        Job job = jobRepository.findByObid(obId);
        return job != null ? job.getId() : null;
    }

    @Transactional
    @Override
    public void initializeJobs(List<Job> jobSyncRequests) {
        List<Job> existingJobs = findAll();
        for (Job job : jobSyncRequests) {
            jobInitService.initializeJob(job, existingJobs);
        }
    }

}