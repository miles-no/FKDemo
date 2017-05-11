package no.fjordkraft.im.jobs.services;


import no.fjordkraft.im.jobs.domain.Job;
import no.fjordkraft.im.jobs.domain.JobFetchRequest;

import java.util.List;

/**
 *
 */
public interface JobService {

    /**
     * Returns list of all jobs.
     *
     * @return
     */
	List<Job> findAll();

    /**
     * Get a list of jobs, based on filter.
     *
     * @param request
     * @return
     */
   // List<Job> listJobs(JobFetchRequest request);

    /**
     * Returns a job.
     *
     * @param id
     * @return
     */
    Job getJob(String id);

    /**
     * Create job.
     *
     * @param job
     * @return
     */
    Job createJob(Job job);

    /**
     *
     * @param job
     */
    Job updateJob(Job job);

    /**
     * Disables and deletes the job.
     * @param job
     */
    void deleteJob(Job job);

    /**
     * Enable job.
     *
     * @param id
     */
    Job enableJob(String id);

    /**
     * Disable job.
     *
     * @param id
     */
    Job disableJob(String id);

    /**
     * Run job (ad hoc)
     *
     * @param id
     * @return
     */
    long runJob(String id);

    /**
     * Abort job, if the job implements the InterruptableJob - interface.
     *
     * @param id
     * @return
     */
    boolean abortJob(String id);

    /**
     * Stops all running jobs.
     */
    void abortAllRunningJobs();

    /**
     * Returns a job.
     *
     * @param className
     * @return
     */
    List<Job> getJobByClass(String className);


    /**
     * Get our job ID by Obsidian's ID
     *
     * @param obId
     * @return
     */
    String getJobIdByObId(long obId);

    /**
     * Create / disable / deprecate jobs.
     * @param jobSyncRequests
     */
    void initializeJobs(List<Job> jobSyncRequests);

}
