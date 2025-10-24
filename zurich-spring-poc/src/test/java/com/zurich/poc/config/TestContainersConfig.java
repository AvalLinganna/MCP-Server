package com.zurich.poc.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * TestContainers configuration for integration tests
 * Implements KAN-1 requirement for TestContainers with PostgreSQL
 */
@TestConfiguration
@Profile("integration-test")
public class TestContainersConfig {

    // Hold the container as a field so Spring manages its lifecycle
    private static PostgreSQLContainer<?> container;

    /**
     * PostgreSQL container for integration tests
     * Manual configuration for Spring Boot 3.2.2
     */
    @Bean(destroyMethod = "stop")
    public PostgreSQLContainer<?> postgresContainer() {
        container = new PostgreSQLContainer<>(DockerImageName.parse("postgres:15-alpine"))
                .withDatabaseName("testdb")
                .withUsername("test")
                .withPassword("test");
        container.start();
        // Set system properties for Spring Boot to pick up
        System.setProperty("spring.datasource.url", container.getJdbcUrl());
        System.setProperty("spring.datasource.username", container.getUsername());
        System.setProperty("spring.datasource.password", container.getPassword());
        System.setProperty("spring.datasource.driver-class-name", container.getDriverClassName());
        return container;
    }
}
