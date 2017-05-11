package no.fjordkraft.im.jobs.domain;

/**
 * enabled: will be added to the job - db-table if missing, as enabled
 * disabled: will be added to the job - db-table if missing, as disabled
 * deprecated: will be removed from the job - db-table
 */
public enum JobStatus {
    enabled, disabled, deprecated
}
