package no.fjordkraft.im;

import com.itextpdf.text.FontFactory;
import no.fjordkraft.im.consumer.InvoiceConsumer;
import no.fjordkraft.im.services.ConfigService;
import no.fjordkraft.im.util.IMConstants;
import no.fjordkraft.security.filter.SecurityFilter;
import org.apache.commons.lang.StringUtils;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StopWatch;

import javax.sql.DataSource;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.logging.Level;


/**
 * Created by bhavi on 5/4/2017.
 */
@Configuration
public class AppConfig {

    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    /*@Autowired
    private DataSource dataSource;*/

    private @Autowired
    AutowireCapableBeanFactory beanFactory;

    @Autowired
    private ConfigService configService;




    @Bean(name="PDFGeneratorExecutor")
    @DependsOn("BirtEngine")
    public TaskExecutor getPDFGeneratorExecutor(ConfigService configService) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int maxPool = configService.getInteger(IMConstants.NUM_OF_THREAD_PDFGENERATOR);
        executor.setCorePoolSize(maxPool);
        executor.setMaxPoolSize(maxPool);
        executor.setQueueCapacity((Integer.valueOf(Integer.MAX_VALUE)));
        executor.initialize();
        return executor;
    }


    @Bean(name="BirtEngine")
    public IReportEngine getBirtEngine(ConfigService configService) throws BirtException, MalformedURLException {
        /*try {
            dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("Initializing Birt engine");
        String fontPath = configService.getString(IMConstants.CUSTOM_FONT_PATH);

        EngineConfig engineConfig = new EngineConfig();

        IReportEngine engine = null;
        String logPath = configService.getString(IMConstants.BIRT_ENGINE_LOG_PATH);
       if(StringUtils.isNotEmpty(logPath)) {
            engineConfig.setLogConfig(logPath, Level.FINE);
        }

        URL url = new URL("file:"+fontPath + File.separator +"fontsConfig.xml");
        logger.debug("Font config url "+url.toString());
        engineConfig.setFontConfig(url);

        String birtResourcePath = configService.getString(IMConstants.BIRT_RESOURCE_PATH);
        engineConfig.setResourcePath(birtResourcePath);
        logger.debug("Birt Resource path "+engineConfig.getResourcePath());

        Platform.startup(engineConfig);
        IReportEngineFactory factory = (IReportEngineFactory) Platform
                .createFactoryObject( IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY );
        engine = factory.createReportEngine( engineConfig );
        engine.changeLogLevel( Level.SEVERE );

        stopWatch.stop();
        logger.debug(stopWatch.prettyPrint());
        return engine;
    }

   /* @Bean
    public FilterRegistrationBean myFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        SecurityFilter securityFilter = new SecurityFilter();
        beanFactory.autowireBean(securityFilter);
        registration.setFilter(securityFilter);
        //registration.addUrlPatterns("/api*//*");
        registration.addUrlPatterns("/api*//**//*");

        return registration;
    }*/

   /* @Bean
    @DependsOn("BirtEngine")
    public InvoiceConsumer kafkaConsumer(){
        logger.debug("java temp dir "+System.getProperty("java.io.tmpdir"));
        Boolean useKafkaForPDFProcessing = configService.getBoolean(IMConstants.USE_KAFKA_PDF_PROCESSING);
        if(useKafkaForPDFProcessing) {
            InvoiceConsumer consumer = beanFactory.createBean(InvoiceConsumer.class);
            logger.debug(" kafka consumer created ");
            return consumer;
        } else {
            return null;
        }
    }*/
}
