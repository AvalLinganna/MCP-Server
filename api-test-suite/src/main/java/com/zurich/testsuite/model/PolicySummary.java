package com.zurich.testsuite.model;

/**
 * Policy Summary model for Selenium test framework
 * Represents a simplified view of policy information for testing purposes
 */
public class PolicySummary {
    
    private String policyNumber;
    private String customerId;
    private String customerName;
    private String email;
    private String productType;
    private String status;
    private double premiumAmount;
    private String effectiveDate;
    private String expirationDate;
    private String createdDate;
    private String lastModified;
    private boolean isActive;
    
    // Default constructor
    public PolicySummary() {}
    
    // Constructor with essential fields
    public PolicySummary(String policyNumber, String customerId, String customerName, 
                        String email, String productType, String status) {
        this.policyNumber = policyNumber;
        this.customerId = customerId;
        this.customerName = customerName;
        this.email = email;
        this.productType = productType;
        this.status = status;
        this.isActive = "ACTIVE".equalsIgnoreCase(status);
    }
    
    // Full constructor
    public PolicySummary(String policyNumber, String customerId, String customerName, 
                        String email, String productType, String status, double premiumAmount,
                        String effectiveDate, String expirationDate, String createdDate) {
        this(policyNumber, customerId, customerName, email, productType, status);
        this.premiumAmount = premiumAmount;
        this.effectiveDate = effectiveDate;
        this.expirationDate = expirationDate;
        this.createdDate = createdDate;
        this.lastModified = createdDate;
    }
    
    // Getters and Setters
    public String getPolicyNumber() {
        return policyNumber;
    }
    
    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }
    
    public String getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getProductType() {
        return productType;
    }
    
    public void setProductType(String productType) {
        this.productType = productType;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
        this.isActive = "ACTIVE".equalsIgnoreCase(status);
    }
    
    public double getPremiumAmount() {
        return premiumAmount;
    }
    
    public void setPremiumAmount(double premiumAmount) {
        this.premiumAmount = premiumAmount;
    }
    
    public String getEffectiveDate() {
        return effectiveDate;
    }
    
    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
    
    public String getExpirationDate() {
        return expirationDate;
    }
    
    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }
    
    public String getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
    
    public String getLastModified() {
        return lastModified;
    }
    
    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    // Utility methods for testing
    public boolean isExpired() {
        return "EXPIRED".equalsIgnoreCase(status);
    }
    
    public boolean isCancelled() {
        return "CANCELLED".equalsIgnoreCase(status);
    }
    
    public boolean isPending() {
        return "PENDING".equalsIgnoreCase(status);
    }
    
    public boolean isHomeInsurance() {
        return "HOME".equalsIgnoreCase(productType);
    }
    
    public boolean isAutoInsurance() {
        return "AUTO".equalsIgnoreCase(productType);
    }
    
    public boolean isLifeInsurance() {
        return "LIFE".equalsIgnoreCase(productType);
    }
    
    public boolean isHealthInsurance() {
        return "HEALTH".equalsIgnoreCase(productType);
    }
    
    // Test data validation methods
    public boolean hasValidPolicyNumber() {
        return policyNumber != null && !policyNumber.trim().isEmpty();
    }
    
    public boolean hasValidCustomerId() {
        return customerId != null && !customerId.trim().isEmpty();
    }
    
    public boolean hasValidEmail() {
        return email != null && email.contains("@") && email.contains(".");
    }
    
    public boolean hasValidPremiumAmount() {
        return premiumAmount > 0;
    }
    
    @Override
    public String toString() {
        return "PolicySummary{" +
                "policyNumber='" + policyNumber + '\'' +
                ", customerId='" + customerId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", email='" + email + '\'' +
                ", productType='" + productType + '\'' +
                ", status='" + status + '\'' +
                ", premiumAmount=" + premiumAmount +
                ", effectiveDate='" + effectiveDate + '\'' +
                ", expirationDate='" + expirationDate + '\'' +
                ", isActive=" + isActive +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        PolicySummary that = (PolicySummary) obj;
        return policyNumber != null ? policyNumber.equals(that.policyNumber) : that.policyNumber == null;
    }
    
    @Override
    public int hashCode() {
        return policyNumber != null ? policyNumber.hashCode() : 0;
    }
}