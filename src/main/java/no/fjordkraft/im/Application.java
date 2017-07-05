package no.fjordkraft.im;

/**
 * Created by bhavik on 4/28/2017.
 */

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;

//@ServletComponentScan({"no.fjordkraft.security","no.fjordkraft.im"})
//@SpringBootApplication(scanBasePackages = {"no.fjordkraft.security","no.fjordkraft.im","no.fjordkraft.security.jpa.repository"} )
//@EnableJpaRepositories(basePackages = {"no.fjordkraft.im","no.fjordkraft.security.jpa.repository"})
//@EntityScan(basePackages={"no.fjordkraft.im","no.fjordkraft.security"})

@ServletComponentScan({"no.fjordkraft.im"})
@SpringBootApplication(scanBasePackages = {"no.fjordkraft.im","no.fjordkraft.im.controller"})
@EnableJpaRepositories(basePackages = {"no.fjordkraft.im"})
//@EntityScan(basePackages={"no.fjordkraft.im","no.fjordkraft.security"})
//@EnableScheduling
//@ComponentScan(basePackages = {"no.fjordkraft.im","no.fjordkraft.im.controller"})
//@EnableWebMvc
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

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

    /*@Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }

        };
    }*/

}