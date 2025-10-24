package com.zurich.testsuite.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration class for API settings and environment-specific values
 * Loads configuration from properties files and environment variables
 */
public class ApiConfiguration {
    
    private static final String DEFAULT_CONFIG_FILE = "config.properties";
    
    private String baseUrl;
    private String username;
    private String password;
    private String apiKey;
    private String bearerToken;
    private String environment;
    private int connectionTimeout;
    private int readTimeout;
    private boolean enableLogging;
    private String testDataPath;
    
    // Default constructor - loads from default config file
    public ApiConfiguration() {
        loadConfiguration(DEFAULT_CONFIG_FILE);
    }
    
    // Constructor with custom config file
    public ApiConfiguration(String configFile) {
        loadConfiguration(configFile);
    }
    
    // Constructor with direct configuration
    public ApiConfiguration(String baseUrl, String username, String password) {
        this.baseUrl = baseUrl;
        this.username = username;
        this.password = password;
        setDefaults();
    }
    
    /**
     * Load configuration from properties file
     */
    private void loadConfiguration(String configFile) {
        Properties props = new Properties();
        
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(configFile)) {
            if (input == null) {
                System.err.println("Unable to find " + configFile + ", using defaults");
                setDefaults();
                return;
            }
            
            props.load(input);
            
            // Load API configuration
            this.environment = getProperty(props, "test.environment", "local");
            this.baseUrl = getProperty(props, "api.base.url." + environment, "http://localhost:8080");
            this.username = getProperty(props, "api.username", null);
            this.password = getProperty(props, "api.password", null);
            this.apiKey = getProperty(props, "api.key", null);
            this.bearerToken = getProperty(props, "api.bearer.token", null);
            
            // Load timeout settings
            this.connectionTimeout = Integer.parseInt(getProperty(props, "api.connection.timeout", "10000"));
            this.readTimeout = Integer.parseInt(getProperty(props, "api.read.timeout", "30000"));
            
            // Load other settings
            this.enableLogging = Boolean.parseBoolean(getProperty(props, "api.logging.enabled", "true"));
            this.testDataPath = getProperty(props, "test.data.path", "src/test/resources/data");
            
            // Override with environment variables if available
            overrideWithEnvVariables();
            
        } catch (IOException e) {
            System.err.println("Error loading configuration: " + e.getMessage());
            setDefaults();
        }
    }
    
    /**
     * Override configuration with environment variables
     */
    private void overrideWithEnvVariables() {
        String envBaseUrl = System.getenv("API_BASE_URL");
        if (envBaseUrl != null) {
            this.baseUrl = envBaseUrl;
        }
        
        String envUsername = System.getenv("API_USERNAME");
        if (envUsername != null) {
            this.username = envUsername;
        }
        
        String envPassword = System.getenv("API_PASSWORD");
        if (envPassword != null) {
            this.password = envPassword;
        }
        
        String envApiKey = System.getenv("API_KEY");
        if (envApiKey != null) {
            this.apiKey = envApiKey;
        }
        
        String envToken = System.getenv("API_BEARER_TOKEN");
        if (envToken != null) {
            this.bearerToken = envToken;
        }
        
        String envEnvironment = System.getenv("TEST_ENVIRONMENT");
        if (envEnvironment != null) {
            this.environment = envEnvironment;
        }
    }
    
    /**
     * Set default configuration values
     */
    private void setDefaults() {
        this.environment = "local";
        this.baseUrl = "http://localhost:8080";
        this.connectionTimeout = 10000;
        this.readTimeout = 30000;
        this.enableLogging = true;
        this.testDataPath = "src/test/resources/data";
    }
    
    /**
     * Get property with default value
     */
    private String getProperty(Properties props, String key, String defaultValue) {
        String value = props.getProperty(key);
        return value != null ? value.trim() : defaultValue;
    }
    
    // Getters and Setters
    
    public String getBaseUrl() {
        return baseUrl;
    }
    
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getApiKey() {
        return apiKey;
    }
    
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
    
    public String getBearerToken() {
        return bearerToken;
    }
    
    public void setBearerToken(String bearerToken) {
        this.bearerToken = bearerToken;
    }
    
    public String getEnvironment() {
        return environment;
    }
    
    public void setEnvironment(String environment) {
        this.environment = environment;
    }
    
    public int getConnectionTimeout() {
        return connectionTimeout;
    }
    
    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }
    
    public int getReadTimeout() {
        return readTimeout;
    }
    
    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }
    
    public boolean isEnableLogging() {
        return enableLogging;
    }
    
    public void setEnableLogging(boolean enableLogging) {
        this.enableLogging = enableLogging;
    }
    
    public String getTestDataPath() {
        return testDataPath;
    }
    
    public void setTestDataPath(String testDataPath) {
        this.testDataPath = testDataPath;
    }
    
    // Utility methods
    
    /**
     * Check if basic authentication is configured
     */
    public boolean hasBasicAuth() {
        return username != null && password != null;
    }
    
    /**
     * Check if API key authentication is configured
     */
    public boolean hasApiKey() {
        return apiKey != null;
    }
    
    /**
     * Check if bearer token authentication is configured
     */
    public boolean hasBearerToken() {
        return bearerToken != null;
    }
    
    /**
     * Get full API URL with path
     */
    public String getFullUrl(String path) {
        if (path.startsWith("/")) {
            return baseUrl + path;
        } else {
            return baseUrl + "/" + path;
        }
    }
    
    /**
     * Check if running in local environment
     */
    public boolean isLocalEnvironment() {
        return "local".equalsIgnoreCase(environment);
    }
    
    /**
     * Check if running in development environment
     */
    public boolean isDevelopmentEnvironment() {
        return "dev".equalsIgnoreCase(environment) || "development".equalsIgnoreCase(environment);
    }
    
    /**
     * Check if running in test environment
     */
    public boolean isTestEnvironment() {
        return "test".equalsIgnoreCase(environment) || "testing".equalsIgnoreCase(environment);
    }
    
    /**
     * Check if running in production environment
     */
    public boolean isProductionEnvironment() {
        return "prod".equalsIgnoreCase(environment) || "production".equalsIgnoreCase(environment);
    }
    
    @Override
    public String toString() {
        return "ApiConfiguration{" +
                "baseUrl='" + baseUrl + '\'' +
                ", environment='" + environment + '\'' +
                ", hasBasicAuth=" + hasBasicAuth() +
                ", hasApiKey=" + hasApiKey() +
                ", hasBearerToken=" + hasBearerToken() +
                ", connectionTimeout=" + connectionTimeout +
                ", readTimeout=" + readTimeout +
                ", enableLogging=" + enableLogging +
                '}';
    }
    
    /**
     * Create configuration for specific environment
     */
    public static ApiConfiguration forEnvironment(String environment) {
        ApiConfiguration config = new ApiConfiguration();
        config.setEnvironment(environment);
        
        // Set environment-specific base URLs
        switch (environment.toLowerCase()) {
            case "local":
                config.setBaseUrl("http://localhost:8080");
                break;
            case "dev":
            case "development":
                config.setBaseUrl("https://dev-api.zurich.com");
                break;
            case "test":
            case "testing":
                config.setBaseUrl("https://test-api.zurich.com");
                break;
            case "staging":
            case "stage":
                config.setBaseUrl("https://staging-api.zurich.com");
                break;
            case "prod":
            case "production":
                config.setBaseUrl("https://api.zurich.com");
                break;
            default:
                config.setBaseUrl("http://localhost:8080");
        }
        
        return config;
    }
    
    /**
     * Create configuration with basic authentication
     */
    public static ApiConfiguration withBasicAuth(String baseUrl, String username, String password) {
        return new ApiConfiguration(baseUrl, username, password);
    }
    
    /**
     * Create configuration with API key authentication
     */
    public static ApiConfiguration withApiKey(String baseUrl, String apiKey) {
        ApiConfiguration config = new ApiConfiguration();
        config.setBaseUrl(baseUrl);
        config.setApiKey(apiKey);
        return config;
    }
    
    /**
     * Create configuration with bearer token authentication
     */
    public static ApiConfiguration withBearerToken(String baseUrl, String bearerToken) {
        ApiConfiguration config = new ApiConfiguration();
        config.setBaseUrl(baseUrl);
        config.setBearerToken(bearerToken);
        return config;
    }
}