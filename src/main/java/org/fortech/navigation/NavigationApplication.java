package org.fortech.navigation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;

@SpringBootApplication
@EnableScheduling
public class NavigationApplication {

	public static void main(String[] args) {
		SpringApplication.run(NavigationApplication.class, args);
	}

	@Bean
	public DataSource dataSource() {
		return DataSourceBuilder.create().username(System.getenv("DB_USER")).password(System.getenv("DB_PASSWORD")).url(System.getenv("DB_URL"))
				.driverClassName("org.postgresql.Driver").build();
	}
}
