package com.zurich.poc.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.mock;

/**
 * Test configuration for unit and integration tests
 * Implements KAN-1 requirement for test profiles with H2 configuration
 */
@TestConfiguration
@Profile("test")
public class TestConfig {

    /**
     * Mock RestTemplate for unit tests to avoid actual HTTP calls
     */
    @Bean
    @Primary
    public RestTemplate mockRestTemplate() {
        return mock(RestTemplate.class);
    }
}
