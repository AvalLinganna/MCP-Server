package com.zurich.poc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolicySummary {
    
    private String policyNumber;
    private String policyHolderName;
    private String email;
    private String gender; // KAN-13: Added policy holder gender field
    private LocalDate dateOfBirth; // Date of birth for policy holder
    private String policyType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private Double premiumAmount; // Premium amount
    private Double coverageAmount; // Coverage amount
    
    // KAN-24: Added nominee information fields
    private String nomineeName;
    private String nomineeRelationship;
    private String nomineeContactInfo;
    private String nomineeIdentification;
    
    // KAN-25: Added address information fields
    private String streetAddress;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String addressType; // Residential, Business, Mailing
    
    // Additional fields that might be present in the policy data
    private List<Coverage> coverages;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Coverage {
        private String type;
        private double amount;
        private String description;
    }
}
