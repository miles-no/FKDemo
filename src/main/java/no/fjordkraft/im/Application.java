package no.fjordkraft.im;

/**
 * Created by bhavik on 4/28/2017.
 */
import java.io.File;
import java.util.Arrays;

import no.fjordkraft.im.util.IMConstants;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@ServletComponentScan("no.fjordkraft.im")
@SpringBootApplication
//@EnableScheduling
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        /*String destinationPath = "E:\\XMLTOPDF\\ExampleCode\\Output\\";//systemConfigRepository.getConfigValue(IMConstants.DESTINATION_PATH);
        String folderName = "20170321_FKAS_39142654_statement.xml".substring(0, "20170321_FKAS_39142654_statement.xml".indexOf('.'));
        new File(new File(destinationPath) , folderName).mkdir();*/
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }

        };
    }

}