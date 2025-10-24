package com.zurich.testsuite.service;

import com.zurich.testsuite.config.ApiConfiguration;
import com.zurich.testsuite.service.impl.PolicyServiceImpl;

/**
 * Factory class for creating service instances
 * Provides centralized service creation and configuration management
 */
public class ServiceFactory {
    
    private static ServiceFactory instance;
    private ApiConfiguration defaultConfig;
    
    private ServiceFactory() {
        this.defaultConfig = new ApiConfiguration();
    }
    
    /**
     * Get singleton instance of ServiceFactory
     */
    public static ServiceFactory getInstance() {
        if (instance == null) {
            synchronized (ServiceFactory.class) {
                if (instance == null) {
                    instance = new ServiceFactory();
                }
            }
        }
        return instance;
    }
    
    /**
     * Create PolicyService with default configuration
     */
    public GetPolicyList createPolicyService() {
        return new PolicyServiceImpl(defaultConfig);
    }
    
    /**
     * Create PolicyService with custom configuration
     */
    public GetPolicyList createPolicyService(ApiConfiguration config) {
        return new PolicyServiceImpl(config);
    }
    
    /**
     * Create PolicyService for specific environment
     */
    public GetPolicyList createPolicyService(String environment) {
        ApiConfiguration config = ApiConfiguration.forEnvironment(environment);
        return new PolicyServiceImpl(config);
    }
    
    /**
     * Create PolicyService with basic authentication
     */
    public GetPolicyList createPolicyServiceWithBasicAuth(String baseUrl, String username, String password) {
        ApiConfiguration config = ApiConfiguration.withBasicAuth(baseUrl, username, password);
        return new PolicyServiceImpl(config);
    }
    
    /**
     * Create PolicyService with API key authentication
     */
    public GetPolicyList createPolicyServiceWithApiKey(String baseUrl, String apiKey) {
        ApiConfiguration config = ApiConfiguration.withApiKey(baseUrl, apiKey);
        return new PolicyServiceImpl(config);
    }
    
    /**
     * Create PolicyService with bearer token authentication
     */
    public GetPolicyList createPolicyServiceWithBearerToken(String baseUrl, String bearerToken) {
        ApiConfiguration config = ApiConfiguration.withBearerToken(baseUrl, bearerToken);
        return new PolicyServiceImpl(config);
    }
    
    /**
     * Set default configuration for all services
     */
    public void setDefaultConfiguration(ApiConfiguration config) {
        this.defaultConfig = config;
    }
    
    /**
     * Get default configuration
     */
    public ApiConfiguration getDefaultConfiguration() {
        return defaultConfig;
    }
    
    /**
     * Reset factory to default state
     */
    public void reset() {
        this.defaultConfig = new ApiConfiguration();
    }
}