package no.fjordkraft.im.jobs.services;


import no.fjordkraft.im.jobs.domain.Job;

import java.util.List;

public interface JobInitService {

    /**

     * Initialize job.
     *
     * @return
     */
    void initializeJob(Job job, List<Job> existingJobs);

}
