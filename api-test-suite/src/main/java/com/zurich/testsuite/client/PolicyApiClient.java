package com.zurich.testsuite.client;

import com.zurich.testsuite.config.ApiConfiguration;
import com.zurich.testsuite.model.PolicyApiResponse;
import com.zurich.testsuite.model.PolicySummary;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static io.restassured.RestAssured.given;

/**
 * REST API Client for Policy Management operations
 * Handles all HTTP communications with the policy API endpoints
 */
public class PolicyApiClient {
    
    private static final Logger logger = LoggerFactory.getLogger(PolicyApiClient.class);
    
    private final ApiConfiguration config;
    private final String baseUrl;
    private final Map<String, String> defaultHeaders;
    
    public PolicyApiClient(ApiConfiguration config) {
        this.config = config;
        this.baseUrl = config.getBaseUrl();
        this.defaultHeaders = new HashMap<>();
        setupDefaultHeaders();
        configureRestAssured();
    }
    
    /**
     * Configure REST Assured global settings
     */
    private void configureRestAssured() {
        RestAssured.baseURI = baseUrl;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        
        // Configure timeouts
        RestAssured.config = RestAssured.config()
                .connectionConfig(RestAssured.config().getConnectionConfig()
                        .closeIdleConnectionsAfterEachResponse());
    }
    
    /**
     * Setup default headers for all requests
     */
    private void setupDefaultHeaders() {
        defaultHeaders.put("Content-Type", "application/json");
        defaultHeaders.put("Accept", "application/json");
        defaultHeaders.put("User-Agent", "Zurich-Test-Suite/1.0");
        defaultHeaders.put("X-Client-ID", "test-automation-client");
        
        // Add authentication if configured
        if (config.getApiKey() != null) {
            defaultHeaders.put("X-API-Key", config.getApiKey());
        }
        
        if (config.getBearerToken() != null) {
            defaultHeaders.put("Authorization", "Bearer " + config.getBearerToken());
        }
    }
    
    /**
     * Create base request specification with default settings
     */
    private RequestSpecification createBaseRequest() {
        RequestSpecification request = given()
                .headers(defaultHeaders)
                .relaxedHTTPSValidation();
        
        // Add basic auth if configured
        if (config.getUsername() != null && config.getPassword() != null) {
            request = request.auth().basic(config.getUsername(), config.getPassword());
        }
        
        // Add request ID for tracing
        String requestId = UUID.randomUUID().toString();
        request = request.header("X-Request-ID", requestId);
        
        logger.debug("Created request with ID: {}", requestId);
        return request;
    }
    
    /**
     * Get policies by email address
     */
    public PolicyApiResponse getPoliciesByEmail(String email) {
        return getPoliciesByEmail(email, null, null);
    }
    
    /**
     * Get policies by email address with pagination
     */
    public PolicyApiResponse getPoliciesByEmail(String email, Integer page, Integer size) {
        try {
            logger.info("Fetching policies for email: {}", email);
            
            RequestSpecification request = createBaseRequest()
                    .queryParam("emailId", email);
            
            if (page != null) {
                request = request.queryParam("page", page);
            }
            if (size != null) {
                request = request.queryParam("size", size);
            }
            
            Response response = request
                    .when()
                    .get("/api/v1/policy/list")
                    .then()
                    .extract()
                    .response();
            
            return handlePolicyResponse(response);
            
        } catch (Exception e) {
            logger.error("Error fetching policies for email: {}", email, e);
            return new PolicyApiResponse("Error fetching policies: " + e.getMessage(), "API_ERROR");
        }
    }
    
    /**
     * Get policy by policy number
     */
    public PolicyApiResponse getPolicyByNumber(String policyNumber) {
        try {
            logger.info("Fetching policy by number: {}", policyNumber);
            
            Response response = createBaseRequest()
                    .pathParam("policyNumber", policyNumber)
                    .when()
                    .get("/api/v1/policy/details/{policyNumber}")
                    .then()
                    .extract()
                    .response();
            
            return handlePolicyResponse(response);
            
        } catch (Exception e) {
            logger.error("Error fetching policy by number: {}", policyNumber, e);
            return new PolicyApiResponse("Error fetching policy: " + e.getMessage(), "API_ERROR");
        }
    }
    
    /**
     * Get policies by customer ID
     */
    public PolicyApiResponse getPoliciesByCustomerId(String customerId) {
        return getPoliciesByCustomerId(customerId, null, null);
    }
    
    /**
     * Get policies by customer ID with pagination
     */
    public PolicyApiResponse getPoliciesByCustomerId(String customerId, Integer page, Integer size) {
        try {
            logger.info("Fetching policies for customer: {}", customerId);
            
            RequestSpecification request = createBaseRequest()
                    .queryParam("customerId", customerId);
            
            if (page != null) {
                request = request.queryParam("page", page);
            }
            if (size != null) {
                request = request.queryParam("size", size);
            }
            
            Response response = request
                    .when()
                    .get("/api/v1/policy/list")
                    .then()
                    .extract()
                    .response();
            
            return handlePolicyResponse(response);
            
        } catch (Exception e) {
            logger.error("Error fetching policies for customer: {}", customerId, e);
            return new PolicyApiResponse("Error fetching policies: " + e.getMessage(), "API_ERROR");
        }
    }
    
    /**
     * Get policies by status
     */
    public PolicyApiResponse getPoliciesByStatus(String status) {
        try {
            logger.info("Fetching policies by status: {}", status);
            
            Response response = createBaseRequest()
                    .queryParam("status", status)
                    .when()
                    .get("/api/v1/policy/status/" + status.toLowerCase())
                    .then()
                    .extract()
                    .response();
            
            return handlePolicyResponse(response);
            
        } catch (Exception e) {
            logger.error("Error fetching policies by status: {}", status, e);
            return new PolicyApiResponse("Error fetching policies: " + e.getMessage(), "API_ERROR");
        }
    }
    
    /**
     * Search policies by search term
     */
    public PolicyApiResponse searchPolicies(String searchTerm) {
        return searchPolicies(searchTerm, null, null);
    }
    
    /**
     * Search policies with pagination
     */
    public PolicyApiResponse searchPolicies(String searchTerm, Integer page, Integer size) {
        try {
            logger.info("Searching policies with term: {}", searchTerm);
            
            RequestSpecification request = createBaseRequest()
                    .queryParam("search", searchTerm);
            
            if (page != null) {
                request = request.queryParam("page", page);
            }
            if (size != null) {
                request = request.queryParam("size", size);
            }
            
            Response response = request
                    .when()
                    .get("/api/v1/policy/search")
                    .then()
                    .extract()
                    .response();
            
            return handlePolicyResponse(response);
            
        } catch (Exception e) {
            logger.error("Error searching policies with term: {}", searchTerm, e);
            return new PolicyApiResponse("Error searching policies: " + e.getMessage(), "API_ERROR");
        }
    }
    
    /**
     * Validate policy access for a customer
     */
    public PolicyApiResponse validatePolicyAccess(String policyNumber, String customerId) {
        try {
            logger.info("Validating policy access - Policy: {}, Customer: {}", policyNumber, customerId);
            
            Response response = createBaseRequest()
                    .queryParam("policyNumber", policyNumber)
                    .queryParam("customerId", customerId)
                    .when()
                    .get("/api/v1/policy/validate")
                    .then()
                    .extract()
                    .response();
            
            return handleValidationResponse(response);
            
        } catch (Exception e) {
            logger.error("Error validating policy access - Policy: {}, Customer: {}", policyNumber, customerId, e);
            return new PolicyApiResponse("Error validating access: " + e.getMessage(), "API_ERROR");
        }
    }
    
    /**
     * Get active policies
     */
    public PolicyApiResponse getActivePolicies() {
        return getPoliciesByStatus("ACTIVE");
    }
    
    /**
     * Get active policies with pagination
     */
    public PolicyApiResponse getActivePolicies(int page, int size) {
        try {
            logger.info("Fetching active policies - Page: {}, Size: {}", page, size);
            
            Response response = createBaseRequest()
                    .queryParam("page", page)
                    .queryParam("size", size)
                    .when()
                    .get("/api/v1/policy/status/active")
                    .then()
                    .extract()
                    .response();
            
            return handlePolicyResponse(response);
            
        } catch (Exception e) {
            logger.error("Error fetching active policies", e);
            return new PolicyApiResponse("Error fetching active policies: " + e.getMessage(), "API_ERROR");
        }
    }
    
    /**
     * Handle policy list response
     */
    private PolicyApiResponse handlePolicyResponse(Response response) {
        int statusCode = response.getStatusCode();
        String responseBody = response.getBody().asString();
        
        logger.debug("API Response - Status: {}, Body: {}", statusCode, responseBody);
        
        try {
            if (statusCode >= 200 && statusCode < 300) {
                // Parse successful response
                PolicyApiResponse apiResponse = response.as(PolicyApiResponse.class);
                
                // If response doesn't have the wrapper structure, try direct parsing
                if (apiResponse.getPolicies() == null) {
                    // Try parsing as direct policy list or MuleSoft format
                    Map<String, Object> responseMap = response.as(Map.class);
                    
                    if (responseMap.containsKey("policies")) {
                        // Standard format with policies array
                        List<PolicySummary> policies = parsePoliciesToList(responseMap.get("policies"));
                        int totalCount = responseMap.containsKey("totalNumberofPolicies") ? 
                                (Integer) responseMap.get("totalNumberofPolicies") : policies.size();
                        
                        apiResponse = new PolicyApiResponse(policies, totalCount);
                    } else if (responseMap.containsKey("totalNumberofPolicies")) {
                        // MuleSoft format
                        List<PolicySummary> policies = parsePoliciesToList(responseMap.get("policies"));
                        int totalCount = (Integer) responseMap.get("totalNumberofPolicies");
                        
                        apiResponse = new PolicyApiResponse(policies, totalCount);
                    } else {
                        // Single policy or list format
                        List<PolicySummary> policies = new ArrayList<>();
                        if (responseMap.containsKey("policyNumber")) {
                            // Single policy
                            PolicySummary policy = response.as(PolicySummary.class);
                            policies.add(policy);
                        }
                        apiResponse = new PolicyApiResponse(policies, policies.size());
                    }
                }
                
                apiResponse.setRequestId(response.getHeader("X-Request-ID"));
                return apiResponse;
                
            } else {
                // Handle error response
                String errorMessage = "API Error - Status: " + statusCode;
                String errorCode = "HTTP_" + statusCode;
                
                try {
                    Map<String, Object> errorResponse = response.as(Map.class);
                    if (errorResponse.containsKey("message")) {
                        errorMessage = (String) errorResponse.get("message");
                    }
                    if (errorResponse.containsKey("errorCode")) {
                        errorCode = (String) errorResponse.get("errorCode");
                    }
                } catch (Exception e) {
                    logger.warn("Could not parse error response: {}", responseBody);
                }
                
                return new PolicyApiResponse(errorMessage, errorCode);
            }
            
        } catch (Exception e) {
            logger.error("Error parsing response: {}", responseBody, e);
            return new PolicyApiResponse("Error parsing response: " + e.getMessage(), "PARSE_ERROR");
        }
    }
    
    /**
     * Handle validation response (returns boolean result)
     */
    private PolicyApiResponse handleValidationResponse(Response response) {
        int statusCode = response.getStatusCode();
        
        if (statusCode == 200) {
            PolicyApiResponse apiResponse = new PolicyApiResponse();
            apiResponse.setSuccess(true);
            apiResponse.setMessage("Access validation successful");
            return apiResponse;
        } else if (statusCode == 403) {
            return new PolicyApiResponse("Access denied", "ACCESS_DENIED");
        } else if (statusCode == 404) {
            return new PolicyApiResponse("Policy not found", "NOT_FOUND");
        } else {
            return new PolicyApiResponse("Validation failed", "VALIDATION_ERROR");
        }
    }
    
    /**
     * Parse generic object to PolicySummary list
     */
    @SuppressWarnings("unchecked")
    private List<PolicySummary> parsePoliciesToList(Object policiesObject) {
        List<PolicySummary> policies = new ArrayList<>();
        
        if (policiesObject instanceof List) {
            List<Map<String, Object>> policyMaps = (List<Map<String, Object>>) policiesObject;
            
            for (Map<String, Object> policyMap : policyMaps) {
                PolicySummary policy = new PolicySummary();
                
                // Map fields from response to PolicySummary
                if (policyMap.containsKey("policyNumber")) {
                    policy.setPolicyNumber((String) policyMap.get("policyNumber"));
                }
                if (policyMap.containsKey("customerId")) {
                    policy.setCustomerId((String) policyMap.get("customerId"));
                }
                if (policyMap.containsKey("customerName")) {
                    policy.setCustomerName((String) policyMap.get("customerName"));
                }
                if (policyMap.containsKey("email")) {
                    policy.setEmail((String) policyMap.get("email"));
                }
                if (policyMap.containsKey("productType") || policyMap.containsKey("policyType")) {
                    String productType = (String) policyMap.getOrDefault("productType", policyMap.get("policyType"));
                    policy.setProductType(productType);
                }
                if (policyMap.containsKey("status")) {
                    policy.setStatus((String) policyMap.get("status"));
                }
                if (policyMap.containsKey("premiumAmount")) {
                    Object premium = policyMap.get("premiumAmount");
                    if (premium instanceof Number) {
                        policy.setPremiumAmount(((Number) premium).doubleValue());
                    }
                }
                if (policyMap.containsKey("effectiveDate") || policyMap.containsKey("startDate")) {
                    String effectiveDate = (String) policyMap.getOrDefault("effectiveDate", policyMap.get("startDate"));
                    policy.setEffectiveDate(effectiveDate);
                }
                if (policyMap.containsKey("expirationDate") || policyMap.containsKey("endDate")) {
                    String expirationDate = (String) policyMap.getOrDefault("expirationDate", policyMap.get("endDate"));
                    policy.setExpirationDate(expirationDate);
                }
                
                policies.add(policy);
            }
        }
        
        return policies;
    }
    
    /**
     * Health check endpoint
     */
    public boolean isServiceHealthy() {
        try {
            Response response = createBaseRequest()
                    .when()
                    .get("/health/check")
                    .then()
                    .extract()
                    .response();
            
            return response.getStatusCode() == 200;
            
        } catch (Exception e) {
            logger.error("Health check failed", e);
            return false;
        }
    }
    
    /**
     * Get API configuration
     */
    public ApiConfiguration getConfig() {
        return config;
    }
}