package com.zurich.testsuite.service;

import com.zurich.testsuite.model.PolicySummary;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for retrieving policy lists and policy-related data in Selenium test framework
 * Provides methods for fetching policies by various criteria for testing purposes
 */
public interface GetPolicyList {
    
    /**
     * Get all policies for a customer
     * @param customerId The customer identifier
     * @return List of policy summaries
     */
    List<PolicySummary> getPoliciesByCustomerId(String customerId);
    
    /**
     * Get policies for a customer with pagination
     * @param customerId The customer identifier
     * @param page Page number (0-based)
     * @param size Page size
     * @return List of policy summaries for the specified page
     */
    List<PolicySummary> getPoliciesByCustomerId(String customerId, int page, int size);
    
    /**
     * Get policy by policy number
     * @param policyNumber The policy number
     * @return Optional policy summary
     */
    Optional<PolicySummary> getPolicyByNumber(String policyNumber);
    
    /**
     * Get policies by email address
     * @param email The customer email address
     * @return List of policy summaries
     */
    List<PolicySummary> getPoliciesByEmail(String email);
    
    /**
     * Get policies by email address with pagination
     * @param email The customer email address
     * @param page Page number (0-based)
     * @param size Page size
     * @return List of policy summaries for the specified page
     */
    List<PolicySummary> getPoliciesByEmail(String email, int page, int size);
    
    /**
     * Get policies by status
     * @param status The policy status (ACTIVE, EXPIRED, CANCELLED, etc.)
     * @return List of policy summaries
     */
    List<PolicySummary> getPoliciesByStatus(String status);
    
    /**
     * Get policies by product type
     * @param productType The product type (HOME, AUTO, LIFE, etc.)
     * @return List of policy summaries
     */
    List<PolicySummary> getPoliciesByProductType(String productType);
    
    /**
     * Get all active policies
     * @return List of active policy summaries
     */
    List<PolicySummary> getActivePolicies();
    
    /**
     * Get active policies with pagination
     * @param page Page number (0-based)
     * @param size Page size
     * @return List of active policy summaries for the specified page
     */
    List<PolicySummary> getActivePolicies(int page, int size);
    
    /**
     * Search policies by partial policy number or customer name
     * @param searchTerm The search term
     * @return List of matching policy summaries
     */
    List<PolicySummary> searchPolicies(String searchTerm);
    
    /**
     * Search policies with pagination
     * @param searchTerm The search term
     * @param page Page number (0-based)
     * @param size Page size
     * @return List of matching policy summaries for the specified page
     */
    List<PolicySummary> searchPolicies(String searchTerm, int page, int size);
    
    /**
     * Get policy count by customer ID
     * @param customerId The customer identifier
     * @return Number of policies for the customer
     */
    long getPolicyCountByCustomerId(String customerId);
    
    /**
     * Check if policy exists and is active
     * @param policyNumber The policy number
     * @return true if policy exists and is active, false otherwise
     */
    boolean isPolicyActive(String policyNumber);
    
    /**
     * Validate policy accessibility for a customer
     * @param policyNumber The policy number
     * @param customerId The customer identifier
     * @return true if customer has access to the policy, false otherwise
     */
    boolean validatePolicyAccess(String policyNumber, String customerId);
    
    /**
     * Get policies by date range
     * @param startDate Start date in yyyy-MM-dd format
     * @param endDate End date in yyyy-MM-dd format
     * @return List of policies created within the date range
     */
    List<PolicySummary> getPoliciesByDateRange(String startDate, String endDate);
    
    /**
     * Get policies by premium amount range
     * @param minAmount Minimum premium amount
     * @param maxAmount Maximum premium amount
     * @return List of policies within the premium range
     */
    List<PolicySummary> getPoliciesByPremiumRange(double minAmount, double maxAmount);
    
    /**
     * Get expired policies
     * @return List of expired policy summaries
     */
    List<PolicySummary> getExpiredPolicies();
    
    /**
     * Get policies expiring soon (within specified days)
     * @param days Number of days from today
     * @return List of policies expiring within the specified days
     */
    List<PolicySummary> getPoliciesExpiringSoon(int days);
}