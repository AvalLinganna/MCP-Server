package com.zurich.testsuite.service.impl;

import com.zurich.testsuite.client.PolicyApiClient;
import com.zurich.testsuite.config.ApiConfiguration;
import com.zurich.testsuite.model.PolicyApiResponse;
import com.zurich.testsuite.model.PolicySummary;
import com.zurich.testsuite.service.GetPolicyList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of PolicyService for handling policy-related operations
 * Integrates with REST API client and provides business logic for policy management
 */
public class PolicyServiceImpl implements GetPolicyList {
    
    private static final Logger logger = LoggerFactory.getLogger(PolicyServiceImpl.class);
    
    private final PolicyApiClient apiClient;
    private final ApiConfiguration config;
    
    // Constants for business logic
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 100;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    /**
     * Constructor with API configuration
     */
    public PolicyServiceImpl(ApiConfiguration config) {
        this.config = config;
        this.apiClient = new PolicyApiClient(config);
        logger.info("PolicyServiceImpl initialized with environment: {}", config.getEnvironment());
    }
    
    /**
     * Default constructor - uses default configuration
     */
    public PolicyServiceImpl() {
        this(new ApiConfiguration());
    }
    
    @Override
    public List<PolicySummary> getPoliciesByCustomerId(String customerId) {
        logger.info("Fetching all policies for customer: {}", customerId);
        
        if (customerId == null || customerId.trim().isEmpty()) {
            logger.warn("Customer ID is null or empty");
            return Collections.emptyList();
        }
        
        try {
            PolicyApiResponse response = apiClient.getPoliciesByCustomerId(customerId);
            
            if (response.isSuccess() && !response.isEmpty()) {
                logger.debug("Retrieved {} policies for customer: {}", response.getPolicyCount(), customerId);
                return response.getPolicies();
            } else {
                logger.warn("No policies found for customer: {} - {}", customerId, response.getMessage());
                return Collections.emptyList();
            }
            
        } catch (Exception e) {
            logger.error("Error fetching policies for customer: {}", customerId, e);
            return Collections.emptyList();
        }
    }
    
    @Override
    public List<PolicySummary> getPoliciesByCustomerId(String customerId, int page, int size) {
        logger.info("Fetching policies for customer: {}, page: {}, size: {}", customerId, page, size);
        
        if (customerId == null || customerId.trim().isEmpty()) {
            logger.warn("Customer ID is null or empty");
            return Collections.emptyList();
        }
        
        // Validate pagination parameters
        if (page < 0) {
            logger.warn("Invalid page number: {}, using 0", page);
            page = 0;
        }
        
        if (size <= 0 || size > MAX_PAGE_SIZE) {
            logger.warn("Invalid page size: {}, using default: {}", size, DEFAULT_PAGE_SIZE);
            size = DEFAULT_PAGE_SIZE;
        }
        
        try {
            PolicyApiResponse response = apiClient.getPoliciesByCustomerId(customerId, page, size);
            
            if (response.isSuccess() && !response.isEmpty()) {
                logger.debug("Retrieved {} policies for customer: {} (page {})", 
                    response.getPolicyCount(), customerId, page);
                return response.getPolicies();
            } else {
                logger.warn("No policies found for customer: {} on page {} - {}", 
                    customerId, page, response.getMessage());
                return Collections.emptyList();
            }
            
        } catch (Exception e) {
            logger.error("Error fetching policies for customer: {} (page {})", customerId, page, e);
            return Collections.emptyList();
        }
    }
    
    @Override
    public Optional<PolicySummary> getPolicyByNumber(String policyNumber) {
        logger.info("Fetching policy by number: {}", policyNumber);
        
        if (policyNumber == null || policyNumber.trim().isEmpty()) {
            logger.warn("Policy number is null or empty");
            return Optional.empty();
        }
        
        try {
            PolicyApiResponse response = apiClient.getPolicyByNumber(policyNumber);
            
            if (response.isSuccess() && !response.isEmpty()) {
                PolicySummary policy = response.getPolicies().get(0);
                logger.debug("Retrieved policy: {}", policy.getPolicyNumber());
                return Optional.of(policy);
            } else {
                logger.warn("Policy not found: {} - {}", policyNumber, response.getMessage());
                return Optional.empty();
            }
            
        } catch (Exception e) {
            logger.error("Error fetching policy: {}", policyNumber, e);
            return Optional.empty();
        }
    }
    
    @Override
    public List<PolicySummary> getPoliciesByEmail(String email) {
        logger.info("Fetching all policies for email: {}", email);
        
        if (email == null || email.trim().isEmpty() || !isValidEmail(email)) {
            logger.warn("Invalid email address: {}", email);
            return Collections.emptyList();
        }
        
        try {
            PolicyApiResponse response = apiClient.getPoliciesByEmail(email);
            
            if (response.isSuccess() && !response.isEmpty()) {
                logger.debug("Retrieved {} policies for email: {}", response.getPolicyCount(), email);
                return response.getPolicies();
            } else {
                logger.warn("No policies found for email: {} - {}", email, response.getMessage());
                return Collections.emptyList();
            }
            
        } catch (Exception e) {
            logger.error("Error fetching policies for email: {}", email, e);
            return Collections.emptyList();
        }
    }
    
    @Override
    public List<PolicySummary> getPoliciesByEmail(String email, int page, int size) {
        logger.info("Fetching policies for email: {}, page: {}, size: {}", email, page, size);
        
        if (email == null || email.trim().isEmpty() || !isValidEmail(email)) {
            logger.warn("Invalid email address: {}", email);
            return Collections.emptyList();
        }
        
        // Validate pagination parameters
        if (page < 0) page = 0;
        if (size <= 0 || size > MAX_PAGE_SIZE) size = DEFAULT_PAGE_SIZE;
        
        try {
            PolicyApiResponse response = apiClient.getPoliciesByEmail(email, page, size);
            
            if (response.isSuccess() && !response.isEmpty()) {
                logger.debug("Retrieved {} policies for email: {} (page {})", 
                    response.getPolicyCount(), email, page);
                return response.getPolicies();
            } else {
                logger.warn("No policies found for email: {} on page {} - {}", 
                    email, page, response.getMessage());
                return Collections.emptyList();
            }
            
        } catch (Exception e) {
            logger.error("Error fetching policies for email: {} (page {})", email, page, e);
            return Collections.emptyList();
        }
    }
    
    @Override
    public List<PolicySummary> getPoliciesByStatus(String status) {
        logger.info("Fetching policies by status: {}", status);
        
        if (status == null || status.trim().isEmpty()) {
            logger.warn("Status is null or empty");
            return Collections.emptyList();
        }
        
        // Validate status
        if (!isValidStatus(status)) {
            logger.warn("Invalid policy status: {}", status);
            return Collections.emptyList();
        }
        
        try {
            PolicyApiResponse response = apiClient.getPoliciesByStatus(status);
            
            if (response.isSuccess() && !response.isEmpty()) {
                logger.debug("Retrieved {} policies with status: {}", response.getPolicyCount(), status);
                return response.getPolicies();
            } else {
                logger.warn("No policies found with status: {} - {}", status, response.getMessage());
                return Collections.emptyList();
            }
            
        } catch (Exception e) {
            logger.error("Error fetching policies by status: {}", status, e);
            return Collections.emptyList();
        }
    }
    
    @Override
    public List<PolicySummary> getPoliciesByProductType(String productType) {
        logger.info("Fetching policies by product type: {}", productType);
        
        if (productType == null || productType.trim().isEmpty()) {
            logger.warn("Product type is null or empty");
            return Collections.emptyList();
        }
        
        try {
            // Since there's no direct API endpoint for product type, we'll fetch all active policies
            // and filter by product type
            List<PolicySummary> allPolicies = getActivePolicies();
            
            List<PolicySummary> filteredPolicies = allPolicies.stream()
                    .filter(policy -> productType.equalsIgnoreCase(policy.getProductType()))
                    .collect(Collectors.toList());
            
            logger.debug("Retrieved {} policies with product type: {}", filteredPolicies.size(), productType);
            return filteredPolicies;
            
        } catch (Exception e) {
            logger.error("Error fetching policies by product type: {}", productType, e);
            return Collections.emptyList();
        }
    }
    
    @Override
    public List<PolicySummary> getActivePolicies() {
        logger.info("Fetching all active policies");
        
        try {
            PolicyApiResponse response = apiClient.getActivePolicies();
            
            if (response.isSuccess() && !response.isEmpty()) {
                logger.debug("Retrieved {} active policies", response.getPolicyCount());
                return response.getPolicies();
            } else {
                logger.warn("No active policies found - {}", response.getMessage());
                return Collections.emptyList();
            }
            
        } catch (Exception e) {
            logger.error("Error fetching active policies", e);
            return Collections.emptyList();
        }
    }
    
    @Override
    public List<PolicySummary> getActivePolicies(int page, int size) {
        logger.info("Fetching active policies, page: {}, size: {}", page, size);
        
        // Validate pagination parameters
        if (page < 0) page = 0;
        if (size <= 0 || size > MAX_PAGE_SIZE) size = DEFAULT_PAGE_SIZE;
        
        try {
            PolicyApiResponse response = apiClient.getActivePolicies(page, size);
            
            if (response.isSuccess() && !response.isEmpty()) {
                logger.debug("Retrieved {} active policies (page {})", response.getPolicyCount(), page);
                return response.getPolicies();
            } else {
                logger.warn("No active policies found on page {} - {}", page, response.getMessage());
                return Collections.emptyList();
            }
            
        } catch (Exception e) {
            logger.error("Error fetching active policies (page {})", page, e);
            return Collections.emptyList();
        }
    }
    
    @Override
    public List<PolicySummary> searchPolicies(String searchTerm) {
        logger.info("Searching policies with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            logger.warn("Search term is null or empty");
            return Collections.emptyList();
        }
        
        try {
            PolicyApiResponse response = apiClient.searchPolicies(searchTerm);
            
            if (response.isSuccess() && !response.isEmpty()) {
                logger.debug("Found {} policies matching search term: {}", response.getPolicyCount(), searchTerm);
                return response.getPolicies();
            } else {
                logger.warn("No policies found matching search term: {} - {}", searchTerm, response.getMessage());
                return Collections.emptyList();
            }
            
        } catch (Exception e) {
            logger.error("Error searching policies with term: {}", searchTerm, e);
            return Collections.emptyList();
        }
    }
    
    @Override
    public List<PolicySummary> searchPolicies(String searchTerm, int page, int size) {
        logger.info("Searching policies with term: {}, page: {}, size: {}", searchTerm, page, size);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            logger.warn("Search term is null or empty");
            return Collections.emptyList();
        }
        
        // Validate pagination parameters
        if (page < 0) page = 0;
        if (size <= 0 || size > MAX_PAGE_SIZE) size = DEFAULT_PAGE_SIZE;
        
        try {
            PolicyApiResponse response = apiClient.searchPolicies(searchTerm, page, size);
            
            if (response.isSuccess() && !response.isEmpty()) {
                logger.debug("Found {} policies matching search term: {} (page {})", 
                    response.getPolicyCount(), searchTerm, page);
                return response.getPolicies();
            } else {
                logger.warn("No policies found matching search term: {} on page {} - {}", 
                    searchTerm, page, response.getMessage());
                return Collections.emptyList();
            }
            
        } catch (Exception e) {
            logger.error("Error searching policies with term: {} (page {})", searchTerm, page, e);
            return Collections.emptyList();
        }
    }
    
    @Override
    public long getPolicyCountByCustomerId(String customerId) {
        logger.info("Getting policy count for customer: {}", customerId);
        
        if (customerId == null || customerId.trim().isEmpty()) {
            logger.warn("Customer ID is null or empty");
            return 0;
        }
        
        try {
            List<PolicySummary> policies = getPoliciesByCustomerId(customerId);
            long count = policies.size();
            logger.debug("Customer {} has {} policies", customerId, count);
            return count;
            
        } catch (Exception e) {
            logger.error("Error getting policy count for customer: {}", customerId, e);
            return 0;
        }
    }
    
    @Override
    public boolean isPolicyActive(String policyNumber) {
        logger.info("Checking if policy is active: {}", policyNumber);
        
        if (policyNumber == null || policyNumber.trim().isEmpty()) {
            logger.warn("Policy number is null or empty");
            return false;
        }
        
        try {
            Optional<PolicySummary> policy = getPolicyByNumber(policyNumber);
            
            if (policy.isPresent()) {
                boolean isActive = policy.get().isActive();
                logger.debug("Policy {} is active: {}", policyNumber, isActive);
                return isActive;
            } else {
                logger.warn("Policy not found: {}", policyNumber);
                return false;
            }
            
        } catch (Exception e) {
            logger.error("Error checking policy status: {}", policyNumber, e);
            return false;
        }
    }
    
    @Override
    public boolean validatePolicyAccess(String policyNumber, String customerId) {
        logger.info("Validating policy access - Policy: {}, Customer: {}", policyNumber, customerId);
        
        if (policyNumber == null || policyNumber.trim().isEmpty() || 
            customerId == null || customerId.trim().isEmpty()) {
            logger.warn("Policy number or customer ID is null or empty");
            return false;
        }
        
        try {
            PolicyApiResponse response = apiClient.validatePolicyAccess(policyNumber, customerId);
            boolean hasAccess = response.isSuccess();
            
            logger.debug("Customer {} has access to policy {}: {}", customerId, policyNumber, hasAccess);
            return hasAccess;
            
        } catch (Exception e) {
            logger.error("Error validating policy access - Policy: {}, Customer: {}", 
                policyNumber, customerId, e);
            return false;
        }
    }
    
    @Override
    public List<PolicySummary> getPoliciesByDateRange(String startDate, String endDate) {
        logger.info("Fetching policies by date range: {} to {}", startDate, endDate);
        
        if (!isValidDateFormat(startDate) || !isValidDateFormat(endDate)) {
            logger.warn("Invalid date format. Expected yyyy-MM-dd");
            return Collections.emptyList();
        }
        
        try {
            // Since there's no direct API endpoint for date range, we'll fetch all policies
            // and filter by date range
            List<PolicySummary> allPolicies = getActivePolicies();
            
            LocalDate start = LocalDate.parse(startDate, DATE_FORMATTER);
            LocalDate end = LocalDate.parse(endDate, DATE_FORMATTER);
            
            List<PolicySummary> filteredPolicies = allPolicies.stream()
                    .filter(policy -> isDateInRange(policy.getCreatedDate(), start, end))
                    .collect(Collectors.toList());
            
            logger.debug("Retrieved {} policies in date range {} to {}", 
                filteredPolicies.size(), startDate, endDate);
            return filteredPolicies;
            
        } catch (Exception e) {
            logger.error("Error fetching policies by date range: {} to {}", startDate, endDate, e);
            return Collections.emptyList();
        }
    }
    
    @Override
    public List<PolicySummary> getPoliciesByPremiumRange(double minAmount, double maxAmount) {
        logger.info("Fetching policies by premium range: {} to {}", minAmount, maxAmount);
        
        if (minAmount < 0 || maxAmount < 0 || minAmount > maxAmount) {
            logger.warn("Invalid premium range: {} to {}", minAmount, maxAmount);
            return Collections.emptyList();
        }
        
        try {
            // Since there's no direct API endpoint for premium range, we'll fetch all policies
            // and filter by premium amount
            List<PolicySummary> allPolicies = getActivePolicies();
            
            List<PolicySummary> filteredPolicies = allPolicies.stream()
                    .filter(policy -> policy.getPremiumAmount() >= minAmount && 
                                    policy.getPremiumAmount() <= maxAmount)
                    .collect(Collectors.toList());
            
            logger.debug("Retrieved {} policies in premium range {} to {}", 
                filteredPolicies.size(), minAmount, maxAmount);
            return filteredPolicies;
            
        } catch (Exception e) {
            logger.error("Error fetching policies by premium range: {} to {}", minAmount, maxAmount, e);
            return Collections.emptyList();
        }
    }
    
    @Override
    public List<PolicySummary> getExpiredPolicies() {
        logger.info("Fetching expired policies");
        
        try {
            List<PolicySummary> expiredPolicies = getPoliciesByStatus("EXPIRED");
            logger.debug("Retrieved {} expired policies", expiredPolicies.size());
            return expiredPolicies;
            
        } catch (Exception e) {
            logger.error("Error fetching expired policies", e);
            return Collections.emptyList();
        }
    }
    
    @Override
    public List<PolicySummary> getPoliciesExpiringSoon(int days) {
        logger.info("Fetching policies expiring within {} days", days);
        
        if (days < 0) {
            logger.warn("Invalid days parameter: {}", days);
            return Collections.emptyList();
        }
        
        try {
            // Fetch all active policies and filter by expiration date
            List<PolicySummary> activePolicies = getActivePolicies();
            LocalDate cutoffDate = LocalDate.now().plusDays(days);
            
            List<PolicySummary> expiringSoon = activePolicies.stream()
                    .filter(policy -> isExpiringBefore(policy.getExpirationDate(), cutoffDate))
                    .collect(Collectors.toList());
            
            logger.debug("Retrieved {} policies expiring within {} days", expiringSoon.size(), days);
            return expiringSoon;
            
        } catch (Exception e) {
            logger.error("Error fetching policies expiring within {} days", days, e);
            return Collections.emptyList();
        }
    }
    
    // Utility methods
    
    /**
     * Validate email format
     */
    private boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }
    
    /**
     * Validate policy status
     */
    private boolean isValidStatus(String status) {
        String[] validStatuses = {"ACTIVE", "EXPIRED", "CANCELLED", "PENDING", "SUSPENDED"};
        
        for (String validStatus : validStatuses) {
            if (validStatus.equalsIgnoreCase(status)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Validate date format (yyyy-MM-dd)
     */
    private boolean isValidDateFormat(String date) {
        if (date == null || date.trim().isEmpty()) {
            return false;
        }
        
        try {
            LocalDate.parse(date, DATE_FORMATTER);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if date is within range
     */
    private boolean isDateInRange(String dateStr, LocalDate start, LocalDate end) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return false;
        }
        
        try {
            LocalDate date = LocalDate.parse(dateStr, DATE_FORMATTER);
            return !date.isBefore(start) && !date.isAfter(end);
        } catch (Exception e) {
            logger.warn("Invalid date format: {}", dateStr);
            return false;
        }
    }
    
    /**
     * Check if policy is expiring before cutoff date
     */
    private boolean isExpiringBefore(String expirationDateStr, LocalDate cutoffDate) {
        if (expirationDateStr == null || expirationDateStr.trim().isEmpty()) {
            return false;
        }
        
        try {
            LocalDate expirationDate = LocalDate.parse(expirationDateStr, DATE_FORMATTER);
            return !expirationDate.isAfter(cutoffDate);
        } catch (Exception e) {
            logger.warn("Invalid expiration date format: {}", expirationDateStr);
            return false;
        }
    }
    
    /**
     * Get API client for testing purposes
     */
    public PolicyApiClient getApiClient() {
        return apiClient;
    }
    
    /**
     * Get configuration for testing purposes
     */
    public ApiConfiguration getConfig() {
        return config;
    }
    
    /**
     * Check if service is healthy
     */
    public boolean isServiceHealthy() {
        try {
            return apiClient.isServiceHealthy();
        } catch (Exception e) {
            logger.error("Health check failed", e);
            return false;
        }
    }
}