package no.fjordkraft.im.jobs.services;


import no.fjordkraft.im.jobs.domain.Job;
import no.fjordkraft.im.jobs.domain.JobHost;

import java.util.List;

/**
 * Service for handling Obsidian scheduler.
 */
public interface JobSchedulerService {

    /**
     * Returns list of jobs in scheduler.
     *
     * @return
     */
    List<Job> findAllScheduledJobs();

    /**
     * Update scheduler with job - configuration.
     */
    void updateSchedulerAllJobs(List<Job> jobs);

    /**
     * Updates configuration for a job.
     *
     * @param job
     * @return
     */
    void updateSchedulerOneJob(Job job);

    /**
     * Run a job ad hoc.
     *
     * @param obsidianId
     * @return
     */
    long runJobAdHoc(long obsidianId);

    /**
     * Abort all running jobs for a specific job.
     *
     * @param obsidianId
     * @return number of interrupted jobs
     */
    int abortRunningJobs(long obsidianId);

    /**
     * Abort all running jobs.
     *
     * @return number of interrupted jobs
     */
    int abortAllRunningJobs();

    /**
     * Get list of hosts.
     *
     * @return
     */
    List<JobHost> getHosts();

}
