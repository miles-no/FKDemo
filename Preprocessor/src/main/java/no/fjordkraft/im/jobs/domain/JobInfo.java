package no.fjordkraft.im.jobs.domain;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface JobInfo {

    String name() default "";

    String schedule() default "@hourly";

    boolean manualAllowed() default false;

    boolean editAllowed() default false;

    boolean checkForWorkingDay() default true;

    JobStatus status() default JobStatus.disabled;

    String filetype() default "";

    /**
     * Max service time in millis.
     * 0 means unlimited service time.
     */
    long maxServiceTime() default 0;
}