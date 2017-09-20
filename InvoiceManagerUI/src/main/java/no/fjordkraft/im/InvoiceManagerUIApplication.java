package no.fjordkraft.im;


import no.fjordkraft.security.filter.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Arrays;

@SpringBootApplication(scanBasePackages = {"no.fjordkraft.im","no.fjordkraft.im.controller","no.fjordkraft.security"})
@EnableAutoConfiguration(exclude={LiquibaseAutoConfiguration.class})
@EnableEurekaClient
@EnableJpaRepositories(basePackages = {"no.fjordkraft.im","no.fjordkraft.security.jpa.repository"})
@EntityScan(basePackages={"no.fjordkraft.im","no.fjordkraft.security"})
public class InvoiceManagerUIApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvoiceManagerUIApplication.class, args);
	}

	private @Autowired
	AutowireCapableBeanFactory beanFactory;

	@Bean
	public FilterRegistrationBean myFilter() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		SecurityFilter securityFilter = new SecurityFilter();
		beanFactory.autowireBean(securityFilter);
		registration.setFilter(securityFilter);
		registration.addUrlPatterns("/api/*");
		return registration;
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
