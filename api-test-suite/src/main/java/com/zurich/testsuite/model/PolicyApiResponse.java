package com.zurich.testsuite.model;

import java.util.List;

/**
 * API Response wrapper for policy list operations
 * Standardizes API response format across all endpoints
 */
public class PolicyApiResponse {
    
    private boolean success;
    private String message;
    private String errorCode;
    private int totalNumberofPolicies;
    private List<PolicySummary> policies;
    private PaginationInfo pagination;
    private long timestamp;
    private String requestId;
    
    // Default constructor
    public PolicyApiResponse() {
        this.timestamp = System.currentTimeMillis();
    }
    
    // Success constructor
    public PolicyApiResponse(List<PolicySummary> policies, int totalCount) {
        this();
        this.success = true;
        this.policies = policies;
        this.totalNumberofPolicies = totalCount;
        this.message = "Policies retrieved successfully";
    }
    
    // Error constructor
    public PolicyApiResponse(String errorMessage, String errorCode) {
        this();
        this.success = false;
        this.message = errorMessage;
        this.errorCode = errorCode;
    }
    
    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    
    public int getTotalNumberofPolicies() {
        return totalNumberofPolicies;
    }
    
    public void setTotalNumberofPolicies(int totalNumberofPolicies) {
        this.totalNumberofPolicies = totalNumberofPolicies;
    }
    
    public List<PolicySummary> getPolicies() {
        return policies;
    }
    
    public void setPolicies(List<PolicySummary> policies) {
        this.policies = policies;
        if (policies != null) {
            this.totalNumberofPolicies = policies.size();
        }
    }
    
    public PaginationInfo getPagination() {
        return pagination;
    }
    
    public void setPagination(PaginationInfo pagination) {
        this.pagination = pagination;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getRequestId() {
        return requestId;
    }
    
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
    
    // Utility methods for testing
    public boolean hasError() {
        return !success || errorCode != null;
    }
    
    public boolean isEmpty() {
        return policies == null || policies.isEmpty();
    }
    
    public int getPolicyCount() {
        return policies != null ? policies.size() : 0;
    }
    
    public boolean hasPagination() {
        return pagination != null;
    }
    
    @Override
    public String toString() {
        return "PolicyApiResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", totalNumberofPolicies=" + totalNumberofPolicies +
                ", policyCount=" + getPolicyCount() +
                ", timestamp=" + timestamp +
                ", requestId='" + requestId + '\'' +
                '}';
    }
    
    /**
     * Inner class for pagination information
     */
    public static class PaginationInfo {
        private int page;
        private int size;
        private int totalPages;
        private long totalElements;
        private boolean hasNext;
        private boolean hasPrevious;
        
        public PaginationInfo() {}
        
        public PaginationInfo(int page, int size, long totalElements) {
            this.page = page;
            this.size = size;
            this.totalElements = totalElements;
            this.totalPages = (int) Math.ceil((double) totalElements / size);
            this.hasNext = page < totalPages - 1;
            this.hasPrevious = page > 0;
        }
        
        // Getters and Setters
        public int getPage() { return page; }
        public void setPage(int page) { this.page = page; }
        
        public int getSize() { return size; }
        public void setSize(int size) { this.size = size; }
        
        public int getTotalPages() { return totalPages; }
        public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
        
        public long getTotalElements() { return totalElements; }
        public void setTotalElements(long totalElements) { this.totalElements = totalElements; }
        
        public boolean isHasNext() { return hasNext; }
        public void setHasNext(boolean hasNext) { this.hasNext = hasNext; }
        
        public boolean isHasPrevious() { return hasPrevious; }
        public void setHasPrevious(boolean hasPrevious) { this.hasPrevious = hasPrevious; }
    }
}