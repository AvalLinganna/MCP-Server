package com.zurich.testsuite.service;

/**
 * Constants class containing all API paths for Selenium test automation
 * Centralized path management for consistent endpoint testing
 */
public final class PathJava {
    
    // Private constructor to prevent instantiation
    private PathJava() {
        throw new UnsupportedOperationException("Utility class - cannot be instantiated");
    }
    
    // Base URLs for different environments
    public static final String BASE_URL_DEV = "https://dev-api.zurich.com";
    public static final String BASE_URL_TEST = "https://test-api.zurich.com";
    public static final String BASE_URL_STAGING = "https://staging-api.zurich.com";
    public static final String BASE_URL_PROD = "https://api.zurich.com";
    public static final String BASE_URL_LOCAL = "http://localhost:8080";
    
    // API version
    public static final String API_VERSION = "/v1";
    
    // Policy Management Endpoints
    public static final String POLICY_BASE = API_VERSION + "/policy";
    public static final String POLICY_ENDORSEMENT = POLICY_BASE + "/endorsement";
    public static final String POLICY_LIST = POLICY_BASE + "/list";
    public static final String POLICY_DETAILS = POLICY_BASE + "/details";
    public static final String POLICY_SEARCH = POLICY_BASE + "/search";
    public static final String POLICY_VALIDATE = POLICY_BASE + "/validate";
    public static final String POLICY_CREATE = POLICY_BASE + "/create";
    public static final String POLICY_UPDATE = POLICY_BASE + "/update";
    public static final String POLICY_DELETE = POLICY_BASE + "/delete";
    
    // Policy Status Endpoints
    public static final String POLICY_STATUS_ACTIVE = POLICY_BASE + "/status/active";
    public static final String POLICY_STATUS_EXPIRED = POLICY_BASE + "/status/expired";
    public static final String POLICY_STATUS_CANCELLED = POLICY_BASE + "/status/cancelled";
    public static final String POLICY_STATUS_PENDING = POLICY_BASE + "/status/pending";
    public static final String POLICY_STATUS_SUSPENDED = POLICY_BASE + "/status/suspended";
    
    // Claims Management Endpoints
    public static final String CLAIMS_BASE = API_VERSION + "/claims";
    public static final String CLAIMS_CREATE = CLAIMS_BASE + "/create";
    public static final String CLAIMS_UPDATE = CLAIMS_BASE + "/update";
    public static final String CLAIMS_LIST = CLAIMS_BASE + "/list";
    public static final String CLAIMS_DETAILS = CLAIMS_BASE + "/details";
    public static final String CLAIMS_STATUS = CLAIMS_BASE + "/status";
    public static final String CLAIMS_APPROVE = CLAIMS_BASE + "/approve";
    public static final String CLAIMS_REJECT = CLAIMS_BASE + "/reject";
    public static final String CLAIMS_PROCESS = CLAIMS_BASE + "/process";
    
    // Customer Management Endpoints
    public static final String CUSTOMER_BASE = API_VERSION + "/customer";
    public static final String CUSTOMER_POLICIES = CUSTOMER_BASE + "/policies";
    public static final String CUSTOMER_CLAIMS = CUSTOMER_BASE + "/claims";
    public static final String CUSTOMER_PROFILE = CUSTOMER_BASE + "/profile";
    public static final String CUSTOMER_CREATE = CUSTOMER_BASE + "/create";
    public static final String CUSTOMER_UPDATE = CUSTOMER_BASE + "/update";
    public static final String CUSTOMER_DELETE = CUSTOMER_BASE + "/delete";
    
    // Document Management Endpoints
    public static final String DOCUMENTS_BASE = API_VERSION + "/documents";
    public static final String DOCUMENTS_UPLOAD = DOCUMENTS_BASE + "/upload";
    public static final String DOCUMENTS_DOWNLOAD = DOCUMENTS_BASE + "/download";
    public static final String DOCUMENTS_LIST = DOCUMENTS_BASE + "/list";
    public static final String DOCUMENTS_DELETE = DOCUMENTS_BASE + "/delete";
    public static final String DOCUMENTS_VERIFY = DOCUMENTS_BASE + "/verify";
    
    // Authentication and Authorization Endpoints
    public static final String AUTH_BASE = API_VERSION + "/auth";
    public static final String AUTH_LOGIN = AUTH_BASE + "/login";
    public static final String AUTH_LOGOUT = AUTH_BASE + "/logout";
    public static final String AUTH_REFRESH = AUTH_BASE + "/refresh";
    public static final String AUTH_VALIDATE = AUTH_BASE + "/validate";
    public static final String AUTH_REGISTER = AUTH_BASE + "/register";
    public static final String AUTH_RESET_PASSWORD = AUTH_BASE + "/reset-password";
    
    // Health and Monitoring Endpoints
    public static final String HEALTH_BASE = "/health";
    public static final String HEALTH_CHECK = HEALTH_BASE + "/check";
    public static final String HEALTH_STATUS = HEALTH_BASE + "/status";
    public static final String HEALTH_READY = HEALTH_BASE + "/ready";
    public static final String HEALTH_LIVE = HEALTH_BASE + "/live";
    public static final String METRICS = "/metrics";
    public static final String INFO = "/info";
    
    // External Integration Endpoints
    public static final String EXTERNAL_BASE = API_VERSION + "/external";
    public static final String EXTERNAL_MULESOFT = EXTERNAL_BASE + "/mulesoft";
    public static final String EXTERNAL_THIRD_PARTY = EXTERNAL_BASE + "/third-party";
    public static final String EXTERNAL_PAYMENT = EXTERNAL_BASE + "/payment";
    public static final String EXTERNAL_NOTIFICATION = EXTERNAL_BASE + "/notification";
    
    // Test-specific Endpoints
    public static final String TEST_BASE = API_VERSION + "/test";
    public static final String TEST_DATA_SETUP = TEST_BASE + "/data/setup";
    public static final String TEST_DATA_CLEANUP = TEST_BASE + "/data/cleanup";
    public static final String TEST_MOCK_ENABLE = TEST_BASE + "/mock/enable";
    public static final String TEST_MOCK_DISABLE = TEST_BASE + "/mock/disable";
    
    // Query Parameters
    public static final String PARAM_POLICY_NUMBER = "policyNumber";
    public static final String PARAM_CUSTOMER_ID = "customerId";
    public static final String PARAM_EMAIL = "email";
    public static final String PARAM_STATUS = "status";
    public static final String PARAM_PRODUCT_TYPE = "productType";
    public static final String PARAM_PAGE = "page";
    public static final String PARAM_SIZE = "size";
    public static final String PARAM_SORT = "sort";
    public static final String PARAM_SEARCH = "search";
    public static final String PARAM_START_DATE = "startDate";
    public static final String PARAM_END_DATE = "endDate";
    public static final String PARAM_MIN_AMOUNT = "minAmount";
    public static final String PARAM_MAX_AMOUNT = "maxAmount";
    
    // Path Variables (for REST template construction)
    public static final String PATH_VAR_ID = "/{id}";
    public static final String PATH_VAR_POLICY_NUMBER = "/{policyNumber}";
    public static final String PATH_VAR_CUSTOMER_ID = "/{customerId}";
    public static final String PATH_VAR_CLAIM_ID = "/{claimId}";
    public static final String PATH_VAR_DOCUMENT_ID = "/{documentId}";
    public static final String PATH_VAR_USER_ID = "/{userId}";
    
    // Complete endpoint patterns with path variables
    public static final String POLICY_BY_NUMBER = POLICY_DETAILS + PATH_VAR_POLICY_NUMBER;
    public static final String CUSTOMER_POLICIES_BY_ID = CUSTOMER_POLICIES + PATH_VAR_CUSTOMER_ID;
    public static final String CLAIMS_BY_ID = CLAIMS_DETAILS + PATH_VAR_CLAIM_ID;
    public static final String CLAIMS_BY_POLICY = CLAIMS_LIST + PATH_VAR_POLICY_NUMBER;
    public static final String DOCUMENTS_BY_ID = DOCUMENTS_DOWNLOAD + PATH_VAR_DOCUMENT_ID;
    public static final String CUSTOMER_BY_ID = CUSTOMER_PROFILE + PATH_VAR_CUSTOMER_ID;
    
    // HTTP Headers
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_ACCEPT = "Accept";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_X_REQUEST_ID = "X-Request-ID";
    public static final String HEADER_X_CORRELATION_ID = "X-Correlation-ID";
    public static final String HEADER_X_API_KEY = "X-API-Key";
    public static final String HEADER_X_CLIENT_ID = "X-Client-ID";
    public static final String HEADER_USER_AGENT = "User-Agent";
    
    // Content Types
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String CONTENT_TYPE_XML = "application/xml";
    public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";
    public static final String CONTENT_TYPE_MULTIPART = "multipart/form-data";
    public static final String CONTENT_TYPE_TEXT = "text/plain";
    
    // HTTP Status Codes (for test assertions)
    public static final int STATUS_OK = 200;
    public static final int STATUS_CREATED = 201;
    public static final int STATUS_ACCEPTED = 202;
    public static final int STATUS_NO_CONTENT = 204;
    public static final int STATUS_BAD_REQUEST = 400;
    public static final int STATUS_UNAUTHORIZED = 401;
    public static final int STATUS_FORBIDDEN = 403;
    public static final int STATUS_NOT_FOUND = 404;
    public static final int STATUS_METHOD_NOT_ALLOWED = 405;
    public static final int STATUS_CONFLICT = 409;
    public static final int STATUS_INTERNAL_SERVER_ERROR = 500;
    public static final int STATUS_SERVICE_UNAVAILABLE = 503;
    
    // Test Environment Configuration
    public static final String TEST_USER_EMAIL = "test.user@zurich.com";
    public static final String TEST_ADMIN_EMAIL = "test.admin@zurich.com";
    public static final String TEST_CUSTOMER_ID = "TEST_CUST_001";
    public static final String TEST_POLICY_NUMBER = "TEST_POL_001";
    public static final String TEST_CLAIM_ID = "TEST_CLM_001";
    
    // Response Messages for Testing
    public static final String SUCCESS_MESSAGE = "Operation completed successfully";
    public static final String VALIDATION_ERROR = "Validation failed";
    public static final String NOT_FOUND_ERROR = "Resource not found";
    public static final String UNAUTHORIZED_ERROR = "Unauthorized access";
    public static final String FORBIDDEN_ERROR = "Access forbidden";
    public static final String SERVER_ERROR = "Internal server error";
    public static final String SERVICE_UNAVAILABLE_ERROR = "Service temporarily unavailable";
    
    /**
     * Utility method to build complete URL with base URL and path
     * @param baseUrl The base URL (dev, test, prod, etc.)
     * @param path The API path
     * @return Complete URL
     */
    public static String buildUrl(String baseUrl, String path) {
        return baseUrl + path;
    }
    
    /**
     * Utility method to build path with single parameter
     * @param basePath The base path template
     * @param pathVariable The path variable value
     * @return Complete path with variable substituted
     */
    public static String buildPath(String basePath, String pathVariable) {
        return basePath.replace("{id}", pathVariable)
                      .replace("{policyNumber}", pathVariable)
                      .replace("{customerId}", pathVariable)
                      .replace("{claimId}", pathVariable)
                      .replace("{documentId}", pathVariable)
                      .replace("{userId}", pathVariable);
    }
    
    /**
     * Utility method to build path with multiple parameters
     * @param basePath The base path template
     * @param params Variable arguments of path parameters
     * @return Complete path with all variables substituted
     */
    public static String buildPath(String basePath, String... params) {
        String result = basePath;
        String[] placeholders = {"{id}", "{policyNumber}", "{customerId}", "{claimId}", "{documentId}", "{userId}"};
        
        for (int i = 0; i < params.length && i < placeholders.length; i++) {
            result = result.replace(placeholders[i], params[i]);
        }
        
        return result;
    }
    
    /**
     * Utility method to build query string from parameters
     * @param params Key-value pairs for query parameters
     * @return Query string (without leading ?)
     */
    public static String buildQueryString(String... params) {
        if (params.length % 2 != 0) {
            throw new IllegalArgumentException("Parameters must be provided in key-value pairs");
        }
        
        StringBuilder queryString = new StringBuilder();
        for (int i = 0; i < params.length; i += 2) {
            if (queryString.length() > 0) {
                queryString.append("&");
            }
            queryString.append(params[i]).append("=").append(params[i + 1]);
        }
        
        return queryString.toString();
    }
    
    /**
     * Get base URL based on environment
     * @param environment The target environment (dev, test, staging, prod, local)
     * @return Appropriate base URL
     */
    public static String getBaseUrl(String environment) {
        switch (environment.toLowerCase()) {
            case "dev":
            case "development":
                return BASE_URL_DEV;
            case "test":
            case "testing":
                return BASE_URL_TEST;
            case "staging":
            case "stage":
                return BASE_URL_STAGING;
            case "prod":
            case "production":
                return BASE_URL_PROD;
            case "local":
                return BASE_URL_LOCAL;
            default:
                throw new IllegalArgumentException("Unknown environment: " + environment);
        }
    }
}