package no.fjordkraft.im;

import liquibase.integration.spring.SpringLiquibase;
import no.fjordkraft.im.filter.RequestLoggingFilter;
import no.fjordkraft.security.filter.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication(scanBasePackages = {"no.fjordkraft.im","no.fjordkraft.im.controller","no.fjordkraft.security"})

@EnableAutoConfiguration(exclude={LiquibaseAutoConfiguration.class})
@EnableEurekaClient
@EnableJpaRepositories(basePackages = {"no.fjordkraft.im","no.fjordkraft.security.jpa.repository"})
@EntityScan(basePackages={"no.fjordkraft.im","no.fjordkraft.security"})
public class InvoiceManagerUIApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvoiceManagerUIApplication.class, args);
	}

	@Bean(name="liquibase")
	public SpringLiquibase liquibase() throws SQLException {

		// Configure Liquibase
		SpringLiquibase liquibase = new SpringLiquibase();
		liquibase.setShouldRun(false);
		// Verbose logging
		Map<String, String> params = new HashMap<String, String>();
		params.put("verbose", "true");
		liquibase.setChangeLogParameters(params);
		return liquibase;
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
		registration.setOrder(Integer.MAX_VALUE-1);
		return registration;
	}

	@Bean
	public FilterRegistrationBean requestLoggingFilter() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		RequestLoggingFilter requestLoggingFilter = new RequestLoggingFilter();
		registration.setFilter(requestLoggingFilter);
		registration.setOrder(Integer.MAX_VALUE);
		return registration;
	}

	//@Bean
	public CommonsRequestLoggingFilter logFilter() {
		CommonsRequestLoggingFilter filter
				= new CommonsRequestLoggingFilter();
		filter.setIncludeQueryString(true);
		filter.setIncludePayload(true);
		filter.setMaxPayloadLength(100000);
		filter.setIncludeHeaders(false);
		filter.setAfterMessagePrefix("REQUEST DATA : ");
		return filter;
	}
}
