package no.fjordkraft.im;

import com.carfey.ops.job.di.SpringSchedulerStarter;
import com.itextpdf.text.FontFactory;
import no.fjordkraft.im.configuration.DbPlaceholderConfigurer;
import no.fjordkraft.im.util.IMConstants;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.task.TaskExecutor;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.castor.CastorMarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StopWatch;

import java.util.concurrent.Executor;
import java.util.logging.Level;

/**
 * Created by bhavi on 5/4/2017.
 */
@Configuration
public class AppConfig {

    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);


    @Bean
    public SpringSchedulerStarter getSpringSchedulerStarter() {return new SpringSchedulerStarter();}

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
    public TaskExecutor getFileSplitterExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(500);
        executor.initialize();
        return executor;
    }

    @Bean(name="PreprocessorExecutor")
     public TaskExecutor getPreprocessorExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(35);
        executor.setQueueCapacity(500);
        executor.initialize();
        return executor;
    }

    @Bean(name="PDFGeneratorExecutor")
    public TaskExecutor getPDFGeneratorExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(35);
        executor.setQueueCapacity(500);
        executor.initialize();
        return executor;
    }



    @Bean(name="BirtEngine")
    public IReportEngine getBirtEngine() throws BirtException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("Initializing Birt engine");
        FontFactory.register("E:\\FuelKraft\\font\\TrueType_FjorkraftNeoSans\\FjordkraftNeoSan.ttf", "Fjordkraft Neo Sans");
        FontFactory.register("E:\\FuelKraft\\font\\TrueType_FjorkraftNeoSans\\FjordkraftNeoSanMed.ttf", "Fjordkraft Neo Sans Medium");
        FontFactory.register("E:\\FuelKraft\\font\\TrueType_FjorkraftNeoSans\\FjordNeoSanLigIta.ttf", "Fjordkraft Neo Sans Lt It");
        FontFactory.register("E:\\FuelKraft\\font\\TrueType_FjorkraftNeoSans\\FjordkraftNeoSanBol.ttf", "Fjordkraft Neo Sans Bd");
        FontFactory.register("E:\\FuelKraft\\font\\TrueType_FjorkraftNeoSans\\FjordNeoSanBolIta.ttf", "Fjordkraft Neo Sans Bd It");
        FontFactory.register("E:\\FuelKraft\\font\\TrueType_FjorkraftNeoSans\\FjordNeoSanMedIta.ttf", "Fjordkraft Neo Sans Med It");
        FontFactory.register("E:\\FuelKraft\\font\\TrueType_FjorkraftNeoSans\\FjordNeoSanIta.ttf", "Fjordkraft Neo Sans It");
        FontFactory.register("E:\\FuelKraft\\font\\TrueType_FjorkraftNeoSans\\FjordkraftNeoSanLig.ttf ", "Fjordkraft Neo Sans Lt");
        EngineConfig engineConfig = new EngineConfig();
        IReportEngine engine = null;
        engineConfig.setEngineHome(IMConstants.BIRT_ENGINE_HOME_PATH);
        engineConfig.setLogConfig(IMConstants.BIRT_ENGINE_LOG_PATH, Level.FINE);

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
