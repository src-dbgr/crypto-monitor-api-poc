package com.sam.coin.datasource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariDataSource;

/**
 * Configuration class for setting up the PostgreSQL datasource.
 */
@Configuration
public class PostgresDatasource {

	/**
	 * Configures and returns a HikariDataSource for the application.
	 *
	 * @return A configured HikariDataSource.
	 */
	@Bean
	@ConfigurationProperties("app.datasource")
	public HikariDataSource hikariDataSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}
	
}
