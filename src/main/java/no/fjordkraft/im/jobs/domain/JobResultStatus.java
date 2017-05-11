package no.fjordkraft.im.jobs.domain;

/**
 * Job Result Status.
 *
 * ok: job was OK
 * error: some error
 * warning: job with warnings
 * nop: Job was OK, but nothing was done. No operation.
 */
public enum JobResultStatus {
    ok, error, warning, nop
}
