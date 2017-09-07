package no.fjordkraft.im;

import com.carfey.ops.job.di.SpringSchedulerStarter;
import liquibase.integration.spring.SpringLiquibase;
import no.fjordkraft.im.services.ConfigService;
import no.fjordkraft.im.util.IMConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.task.TaskExecutor;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

//import com.ibm.icu.util.ULocale;

/**
 * Created by bhavi on 5/4/2017.
 */
@Configuration
public class AppConfig {

    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private Environment env;

    @Bean(name="SpringSchedulerStarter")
    @DependsOn("liquibase")
    public SpringSchedulerStarter getSpringSchedulerStarter() {
        logger.debug("Initializing obsidian");
        SpringSchedulerStarter s = new SpringSchedulerStarter();
        return s;
    }


    @Bean(name="marshaller")
    public Marshaller getJaxb2Marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setPackagesToScan("no.fjordkraft.im.if320.models");
        return marshaller;
    }

    @Bean
    public Unmarshaller getJaxb2UnMarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setPackagesToScan("no.fjordkraft.im.if320.models");
        return marshaller;
    }


    @Bean(name="FileSplitterExecutor")
    //@DependsOn("BirtEngine")
    public TaskExecutor getFileSplitterExecutor(ConfigService configService) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //executor.setCorePoolSize(5);
        int maxPool = configService.getInteger(IMConstants.NUM_OF_THREAD_FILESPLITTER);
        executor.setMaxPoolSize(maxPool);
        executor.setQueueCapacity(Integer.valueOf(IMConstants.MAX_QUEUE_CAPACITY));
        executor.initialize();
        return executor;
    }

    @Bean(name="PreprocessorExecutor")
    //@DependsOn("BirtEngine")
     public TaskExecutor getPreprocessorExecutor(ConfigService configService) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int maxPool = configService.getInteger(IMConstants.NUM_OF_THREAD_PREPROCESSOR);
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(maxPool);
        executor.setQueueCapacity((Integer.valueOf(IMConstants.MAX_QUEUE_CAPACITY)));
        executor.initialize();
        return executor;
    }



    @Bean(name="liquibase")
    public SpringLiquibase liquibase() throws SQLException {

        // Locate change log file
        String changelogFile = "classpath:liquidbase/db-changelog.xml";
        Resource resource = resourceLoader.getResource(changelogFile);
        Assert.state(resource.exists(), "Unable to find file: " + changelogFile);

        // Configure Liquibase
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog(changelogFile);
        liquibase.setDataSource(dataSource);
        liquibase.setDropFirst(false);
        String liquibaseStatus = env.getProperty("liquibase.enabled");
        if(null !=liquibaseStatus &&  "DISABLE".equalsIgnoreCase(liquibaseStatus.toUpperCase())) {
            liquibase.setShouldRun(false);
        }
        String context = env.getProperty("liquibase.context");
        liquibase.setContexts(context);

        // Verbose logging
        Map<String, String> params = new HashMap<String, String>();
        params.put("verbose", "true");
        liquibase.setChangeLogParameters(params);

        return liquibase;
    }

    /*@LoadBalanced
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }*/

}
