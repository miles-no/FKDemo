package no.fjordkraft.im;

import com.itextpdf.text.FontFactory;
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
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StopWatch;

import javax.sql.DataSource;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
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


    @Bean(name="BirtEngine")
    //@DependsOn({"liquibase","SpringSchedulerStarter"})
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
