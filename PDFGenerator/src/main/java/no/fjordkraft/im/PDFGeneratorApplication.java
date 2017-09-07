package no.fjordkraft.im;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
@EnableJpaRepositories(basePackages = {"no.fjordkraft.im"})
@SpringBootApplication(exclude = {MongoAutoConfiguration.class,})
@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class})
@EnableEurekaClient
public class PDFGeneratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(PDFGeneratorApplication.class, args);
	}

}
