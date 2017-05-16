package no.fjordkraft.im;

import com.carfey.ops.job.di.SpringSchedulerStarter;
import no.fjordkraft.im.configuration.DbPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.castor.CastorMarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

/**
 * Created by bhavi on 5/4/2017.
 */
@Configuration
public class AppConfig {

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
}
