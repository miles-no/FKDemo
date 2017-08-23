package no.fjordkraft.im;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication(exclude = {LiquibaseAutoConfiguration.class,})
@EnableAutoConfiguration(exclude={LiquibaseAutoConfiguration.class})
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
}
