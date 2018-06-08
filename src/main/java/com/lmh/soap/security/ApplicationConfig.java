package com.lmh.soap.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.lmh.util.Utilities;

@Configuration
public class ApplicationConfig {

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(Utilities.getProperty("derby.driver"));
		dataSource.setUrl(Utilities.getProperty("derby.url"));
		return dataSource;
	}
}

