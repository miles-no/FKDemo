package no.fjordkraft.im.jobs.schedulerjobs;


import no.fjordkraft.im.jobs.domain.Job;
import no.fjordkraft.im.jobs.domain.JobInfo;
import no.fjordkraft.im.jobs.services.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.ServletContextAware;

import javax.annotation.PreDestroy;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

//@Service
@WebListener
public class JobInitializer implements ServletContextListener, ServletContextAware {

    private static String BASE_PACKAGE = "no.fjordkraft";

    private boolean initialized = false;

    @Autowired
    private JobService jobService;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("----Spring context initialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

    @PreDestroy
    public void predestroy() {
        if (initialized) {
            jobService.abortAllRunningJobs();
        }
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        //if (correctContext(servletContext)) {
            init();
        //}
    }

    private boolean correctContext(ServletContext servletContext) {
       // log.debug("contextPath: " + servletContext.getContextPath() + " " + servletContext);
        return "/afijob".equals(servletContext.getContextPath());
    }

    private void init() {
        if (jobService == null) {
            //log.error("JobInitializer:init: jobService is null");
            return;
        }

       //log.debug("job initializing started");
        initialized = true;
        //jobProgressService.clearRunningStateAllJobs();
        List<Job> jobSyncRequests = createJobSyncRequests();
        jobService.initializeJobs(jobSyncRequests);
        //log.info("jobs initialized");
    }

    private List<Job> createJobSyncRequests() {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(JobInfo.class));
        Set<BeanDefinition> candidates = provider.findCandidateComponents(BASE_PACKAGE);

        List<Job> jobSyncRequests = new ArrayList<Job>();

        for (final BeanDefinition candidate : candidates) {
            Class clz = ClassUtils.resolveClassName(candidate.getBeanClassName(),
                    ClassUtils.getDefaultClassLoader());

            final JobInfo jobInfo = (JobInfo) clz.getAnnotation(JobInfo.class);

            Job job = new Job(new Long(0), jobInfo.name(), candidate.getBeanClassName(),
                    jobInfo.manualAllowed(), jobInfo.editAllowed(),
                    jobInfo.schedule(), jobInfo.status());
            jobSyncRequests.add(job);
        }
        return jobSyncRequests;
    }

}
