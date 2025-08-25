package br.com.gunthercloud.libraryapi.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DatabaseConfiguration {
	
	@Value("${spring.datasource.url}")
	String url;

	@Value("${spring.datasource.username}")
	String username;

	@Value("${spring.datasource.password}")
	String password;

	@Value("${spring.datasource.driver-class-name}")
	String driver;

	// @Bean
	DataSource dataSource() {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setUrl(url);
		ds.setUsername(username);
		ds.setPassword(password);
		ds.setDriverClassName(driver);
		return ds;
	}
	
	DataSource hikariDataSource() {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(url);
		config.setUsername(username);
		config.setPassword(password);
		config.setDriverClassName(driver);

		config.setMaximumPoolSize(10); // Liberar no máximo 10 conexões
		config.setMinimumIdle(1); // Tamanho inicial com o pool de 1 até 10 conexões
		config.setPoolName("nome-da-db"); // (opcional) nome que irá aparecer no console
		config.setMaxLifetime(600000); // durar até 10 min a conexão
		config.setConnectionTimeout(150000);  // tempo para conseguir uma conexão
		config.setConnectionTestQuery("select 1"); // teste de conexão 
		
		return new HikariDataSource(config);
	}
}
