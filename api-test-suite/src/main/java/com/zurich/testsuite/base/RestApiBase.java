package com.zurich.testsuite.base;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;
import java.util.HashMap;

/**
 * RestApiBase class provides common REST API methods using REST Assured
 * Contains reusable GET, POST, PUT, DELETE methods with comprehensive logging and reporting
 * 
 * @author Zurich Test Automation Team
 * @version 1.0
 */
@Component
public class RestApiBase {
    
    private static final Logger logger = LogManager.getLogger(RestApiBase.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private RequestSpecification requestSpec;
    
    public RestApiBase() {
        // initialize default request specification
        resetRequestSpec();
    }
    
    public Response get(String path, Map<String, String> headers, Object payload, ContentType contentType, Integer expectedStatusCode) {

        RequestSpecification requestSpecification = getRequestSpec(headers,payload,contentType);
                return executeRequest(requestSpecification, Method.GET, path, expectedStatusCode);
            
    }
    
    public Response post(String path, Map<String, String> headers, Object payload, ContentType contentType, Integer expectedStatusCode) {

        RequestSpecification requestSpecification = getRequestSpec(headers,payload,contentType);
                return executeRequest(requestSpecification, Method.POST, path, expectedStatusCode);
            
    }
    
    
    
      
    /**
     * Convert POJO to JSON string
     * 
     * @param pojo POJO object
     * @return JSON string representation
     */
    public String convertPojoToJson(Object pojo) {
        try {
            String jsonString = objectMapper.writeValueAsString(pojo);
            logger.debug("Converted POJO to JSON: {}", jsonString);
            return jsonString;
        } catch (Exception e) {
            logger.error("Failed to convert POJO to JSON", e);
            throw new RuntimeException("Failed to convert POJO to JSON", e);
        }
    }
    
    /**
     * Convert JSON string to POJO
     * 
     * @param json JSON string
     * @param clazz Target POJO class
     * @param <T> Type parameter
     * @return POJO object
     */
    public <T> T convertJsonToPojo(String json, Class<T> clazz) {
        try {
            T pojo = objectMapper.readValue(json, clazz);
            logger.debug("Converted JSON to POJO: {}", pojo);
            return pojo;
        } catch (Exception e) {
            logger.error("Failed to convert JSON to POJO", e);
            throw new RuntimeException("Failed to convert JSON to POJO", e);
        }
    }
    
    /**
     * Extract POJO from response
     * 
     * @param response Response object
     * @param clazz Target POJO class
     * @param <T> Type parameter
     * @return POJO object
     */
    public <T> T extractPojoFromResponse(Response response, Class<T> clazz) {
        try {
            String responseBody = response.getBody().asString();
            return convertJsonToPojo(responseBody, clazz);
        } catch (Exception e) {
            logger.error("Failed to extract POJO from response", e);
            throw new RuntimeException("Failed to extract POJO from response", e);
        }
    }
    
    
    /**
     * Reset request specification to default
     */
    public void resetRequestSpec() {
        requestSpec = RestAssured.given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON);
        logger.debug("Request specification reset to default");
    }
    
    /**
     * Get current request specification
     * 
     * @return Current RequestSpecification
     */
    public RequestSpecification getRequestSpec() {
        return requestSpec;
    }
    
    /**
     * Execute HTTP request with given parameters
     * 
     * @param requestSpecification Request specification
     * @param method HTTP method
     * @param path API endpoint path
     * @param expectedStatusCode Expected status code
     * @return Response object
     */
    private Response executeRequest(RequestSpecification requestSpecification, Method method, String path, Integer expectedStatusCode) {
        logger.info("Executing {} request to: {}", method, path);
        
        Response response = requestSpecification.request(method, path);
        
        logger.info("Response status: {}", response.getStatusCode());
        logger.debug("Response body: {}", response.getBody().asString());
        
        if (expectedStatusCode != null && response.getStatusCode() != expectedStatusCode) {
            logger.error("Expected status code: {}, but got: {}", expectedStatusCode, response.getStatusCode());
        }
        
        return response;
    }

    /**
     * Build a RequestSpecification using provided headers, payload and contentType.
     *
     * @param headers Map of header names and values
     * @param payload Request body object (can be String or POJO)
     * @param contentType ContentType for the request; if null defaults to JSON
     * @return Built RequestSpecification
     */
    public RequestSpecification getRequestSpec(Map<String, String> headers, Object payload, ContentType contentType) {
        RequestSpecification spec = RestAssured.given();
        
        // apply content type (default to JSON if null)
        if (contentType != null) {
            spec = spec.contentType(contentType);
        } else {
            spec = spec.contentType(ContentType.JSON);
        }
        spec = spec.accept(ContentType.JSON);

        // apply headers if present
        if (headers != null && !headers.isEmpty()) {
            spec = spec.headers(headers);
            logger.debug("Added headers to RequestSpecification: {}", headers);
        }

        // apply body if present; try to serialize non-string payloads
        if (payload != null) {
            if (payload instanceof String) {
                spec = spec.body(payload);
            } else {
                try {
                    String json = objectMapper.writeValueAsString(payload);
                    spec = spec.body(json);
                } catch (Exception e) {
                    logger.debug("Failed to serialize payload to JSON, using raw payload", e);
                    spec = spec.body(payload);
                }
            }
            logger.debug("Added payload to RequestSpecification: {}", payload);
        }

        return spec;
    }
}