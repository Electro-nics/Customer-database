package com.demoapplication;

import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.github.javafaker.Faker;

@Testcontainers
public abstract class AbstractTestContainer {
	@BeforeAll
	static void  databseMigration() {
		Flyway flyway = Flyway.configure().dataSource(
				postgreSQLContainer.getJdbcUrl(),
				postgreSQLContainer.getUsername(),
				postgreSQLContainer.getPassword()
				).load();
		flyway.migrate();
	}
	@Container
	protected static final PostgreSQLContainer<?> postgreSQLContainer=
			new PostgreSQLContainer<>("postgres:latest")
				.withDatabaseName("customer-dao-unit-test")
				.withUsername("springuser")
				.withPassword("password");
	@DynamicPropertySource
    public static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", 
        		postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username",
        		postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password",
        		postgreSQLContainer::getPassword);
    }
	
	protected static DataSource getDataSource() {
		return DataSourceBuilder.create()
				.driverClassName(postgreSQLContainer.getDriverClassName())
				.url(postgreSQLContainer.getJdbcUrl())
				.username(postgreSQLContainer.getUsername())
				.password(postgreSQLContainer.getPassword()).build();

	}
	protected static JdbcTemplate getJdbcTemplate() {
		return new JdbcTemplate(getDataSource());
	}
	protected static final Faker fake= new Faker();
	
	
	

}
