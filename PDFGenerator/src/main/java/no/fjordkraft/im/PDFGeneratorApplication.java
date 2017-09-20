package no.fjordkraft.im;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@ServletComponentScan({"no.fjordkraft.im"})
@SpringBootApplication(scanBasePackages = {"no.fjordkraft.im","no.fjordkraft.im.controller","no.fjordkraft.security"})
@EnableAutoConfiguration(exclude={LiquibaseAutoConfiguration.class})
@EnableEurekaClient
@EnableJpaRepositories(basePackages = {"no.fjordkraft.im","no.fjordkraft.security.jpa.repository"})
@EntityScan(basePackages={"no.fjordkraft.im","no.fjordkraft.security"})
public class PDFGeneratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(PDFGeneratorApplication.class, args);
	}

}
