package no.fjordkraft.im;

import com.carfey.ops.job.di.SpringSchedulerStarter;
//import com.ibm.icu.util.ULocale;
import com.itextpdf.text.FontFactory;
import liquibase.integration.spring.SpringLiquibase;
import no.fjordkraft.im.services.ConfigService;
import no.fjordkraft.im.util.IMConstants;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import javax.sql.DataSource;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

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
    @DependsOn("BirtEngine")
    public TaskExecutor getFileSplitterExecutor(ConfigService configService) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        int maxPool = configService.getInteger(IMConstants.NUM_OF_THREAD_FILESPLITTER);
        executor.setMaxPoolSize(maxPool);
        executor.setQueueCapacity(Integer.valueOf(IMConstants.MAX_QUEUE_CAPACITY));
        executor.initialize();
        return executor;
    }

    @Bean(name="PreprocessorExecutor")
    @DependsOn("BirtEngine")
     public TaskExecutor getPreprocessorExecutor(ConfigService configService) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int maxPool = configService.getInteger(IMConstants.NUM_OF_THREAD_PREPROCESSOR);
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(maxPool);
        executor.setQueueCapacity((Integer.valueOf(IMConstants.MAX_QUEUE_CAPACITY)));
        executor.initialize();
        return executor;
    }

    @Bean(name="PDFGeneratorExecutor")
    @DependsOn("BirtEngine")
    public TaskExecutor getPDFGeneratorExecutor(ConfigService configService) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int maxPool = configService.getInteger(IMConstants.NUM_OF_THREAD_PDFGENERATOR);
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


    @Bean(name="BirtEngine")
    @DependsOn({"liquibase","SpringSchedulerStarter"})
    public IReportEngine getBirtEngine(ConfigService configService) throws BirtException, MalformedURLException {
        StopWatch stopWatch = new StopWatch();
     //   System.out.print("ULocale.getDefault(ULocale.Category.FORMAT) :: "+ULocale.getDefault(ULocale.Category.FORMAT));
        stopWatch.start("Initializing Birt engine");
        String fontPath = configService.getString(IMConstants.CUSTOM_FONT_PATH);
        try {
            FontFactory.register(fontPath + File.separator + "FjordkraftNeoSan.ttf", "Fjordkraft Neo Sans");
            FontFactory.register(fontPath + File.separator + "FjordkraftNeoSanMed.ttf", "Fjordkraft Neo Sans Medium");
            FontFactory.register(fontPath + File.separator + "FjordNeoSanLigIta.ttf", "Fjordkraft Neo Sans Lt It");
            FontFactory.register(fontPath + File.separator + "FjordkraftNeoSanBol.ttf", "Fjordkraft Neo Sans Bd");
            FontFactory.register(fontPath + File.separator + "FjordNeoSanBolIta.ttf", "Fjordkraft Neo Sans Bd It");
            FontFactory.register(fontPath + File.separator + "FjordNeoSanMedIta.ttf", "Fjordkraft Neo Sans Med It");
            FontFactory.register(fontPath + File.separator + "FjordNeoSanIta.ttf", "Fjordkraft Neo Sans It");
            FontFactory.register(fontPath + File.separator + "FjordkraftNeoSanLig.ttf ", "Fjordkraft Neo Sans Lt");
        } catch(Exception e) {
            logger.error("Exception registering Fonts",e);
        }
        EngineConfig engineConfig = new EngineConfig();

        IReportEngine engine = null;
        //engineConfig.setEngineHome(IMConstants.BIRT_ENGINE_HOME_PATH);
        String logPath = configService.getString(IMConstants.BIRT_ENGINE_LOG_PATH);
       if(StringUtils.isNotEmpty(logPath)) {
            engineConfig.setLogConfig(logPath, Level.FINE);
        }

        URL url = new URL("file:"+File.separator+fontPath + File.separator +"fontsConfig.xml");
        logger.debug("Font config url "+url.toString());
        engineConfig.setFontConfig(url);

        String birtResourcePath = configService.getString(IMConstants.BIRT_RESOURCE_PATH);
        engineConfig.setResourcePath(birtResourcePath);
        logger.debug("Birt Resource path "+engineConfig.getResourcePath());

        Platform.startup(engineConfig);
        IReportEngineFactory factory = (IReportEngineFactory) Platform
                .createFactoryObject( IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY );
        engine = factory.createReportEngine( engineConfig );
        engine.changeLogLevel( Level.WARNING );

        stopWatch.stop();
        logger.debug(stopWatch.prettyPrint());


            Runtime runtime = Runtime.getRuntime();
            long totalMemory = runtime.totalMemory(); // current heap allocated to the VM process
            System.out.println("Total memomry = "+ totalMemory);
            long freeMemory = runtime.freeMemory(); // out of the current heap, how much is free
            long maxMemory = runtime.maxMemory(); // Max heap VM can use e.g. Xmx setting
            long usedMemory = totalMemory - freeMemory; // how much of the current heap the VM is using
            long availableMemory = maxMemory - usedMemory; // available memory i.e. Maximum heap size minus the current amount used
            System.out.println("Total memomry = "+ totalMemory+ " Free memory "+ freeMemory + " max memory "+ maxMemory+" used memory "+ usedMemory+ " available memory "+ availableMemory);

        //}
        return engine;
    }
}
