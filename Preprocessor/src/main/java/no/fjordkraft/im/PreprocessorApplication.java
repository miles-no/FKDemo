package no.fjordkraft.im;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ServletComponentScan({"no.fjordkraft.im"})
@EnableJpaRepositories(basePackages = {"no.fjordkraft.im"})
@SpringBootApplication
public class PreprocessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(PreprocessorApplication.class, args);
	}
}
