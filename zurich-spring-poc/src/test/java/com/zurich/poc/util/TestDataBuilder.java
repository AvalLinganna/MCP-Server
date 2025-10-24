package com.zurich.poc.util;

import com.zurich.poc.model.Claim;
import com.zurich.poc.model.ClaimDTO;
import com.zurich.poc.model.PolicySummary;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Test Data Builder utility class for creating test objects
 * Implements KAN-1 requirement for test data builders and fixtures
 * Provides fluent API for building test data with default and customizable values
 */
public class TestDataBuilder {

    /**
     * Builder for Claim entities
     */
    public static class ClaimBuilder {
        private Claim claim = new Claim();

        public ClaimBuilder() {
            // Set sensible defaults
            claim.setId(UUID.randomUUID());
            claim.setClaimNumber("CLM-" + System.currentTimeMillis());
            claim.setPolicyNumber("POL-001");
            claim.setEstimatedAmount(new BigDecimal("1000.00"));
            claim.setDescription("Default test claim description");
            claim.setStatus(Claim.ClaimStatus.SUBMITTED);
            claim.setType(Claim.ClaimType.AUTO);
            claim.setCreatedAt(LocalDateTime.now().minusDays(1));
            claim.setUpdatedAt(LocalDateTime.now());
        }

        public ClaimBuilder withId(UUID id) {
            claim.setId(id);
            return this;
        }

        public ClaimBuilder withClaimNumber(String claimNumber) {
            claim.setClaimNumber(claimNumber);
            return this;
        }

        public ClaimBuilder withPolicyNumber(String policyNumber) {
            claim.setPolicyNumber(policyNumber);
            return this;
        }

        public ClaimBuilder withAmount(BigDecimal amount) {
            claim.setEstimatedAmount(amount);
            return this;
        }

        public ClaimBuilder withAmount(String amount) {
            claim.setEstimatedAmount(new BigDecimal(amount));
            return this;
        }

        public ClaimBuilder withDescription(String description) {
            claim.setDescription(description);
            return this;
        }

        public ClaimBuilder withStatus(Claim.ClaimStatus status) {
            claim.setStatus(status);
            return this;
        }

        public ClaimBuilder withType(Claim.ClaimType type) {
            claim.setType(type);
            return this;
        }

        public ClaimBuilder withCreatedAt(LocalDateTime createdAt) {
            claim.setCreatedAt(createdAt);
            return this;
        }

        public ClaimBuilder withUpdatedAt(LocalDateTime updatedAt) {
            claim.setUpdatedAt(updatedAt);
            return this;
        }

        public ClaimBuilder submitted() {
            claim.setStatus(Claim.ClaimStatus.SUBMITTED);
            return this;
        }

        public ClaimBuilder underReview() {
            claim.setStatus(Claim.ClaimStatus.UNDER_REVIEW);
            return this;
        }

        public ClaimBuilder approved() {
            claim.setStatus(Claim.ClaimStatus.APPROVED);
            return this;
        }

        public ClaimBuilder rejected() {
            claim.setStatus(Claim.ClaimStatus.REJECTED);
            return this;
        }

        public ClaimBuilder autoClaim() {
            claim.setType(Claim.ClaimType.AUTO);
            return this;
        }

        public ClaimBuilder healthClaim() {
            claim.setType(Claim.ClaimType.HEALTH);
            return this;
        }

        public ClaimBuilder propertyClaim() {
            claim.setType(Claim.ClaimType.PROPERTY);
            return this;
        }

        public Claim build() {
            return claim;
        }
    }

    /**
     * Builder for ClaimDTO objects
     */
    public static class ClaimDTOBuilder {
        private ClaimDTO claimDTO = new ClaimDTO();

        public ClaimDTOBuilder() {
            // Set sensible defaults
            claimDTO.setPolicyNumber("POL-001");
            claimDTO.setEstimatedAmount(new BigDecimal("1000.00"));
            claimDTO.setDescription("Default test claim DTO description");
            claimDTO.setStatus(Claim.ClaimStatus.SUBMITTED);
            claimDTO.setType(Claim.ClaimType.AUTO);
        }

        public ClaimDTOBuilder withPolicyNumber(String policyNumber) {
            claimDTO.setPolicyNumber(policyNumber);
            return this;
        }

        public ClaimDTOBuilder withAmount(BigDecimal amount) {
            claimDTO.setEstimatedAmount(amount);
            return this;
        }

        public ClaimDTOBuilder withAmount(String amount) {
            claimDTO.setEstimatedAmount(new BigDecimal(amount));
            return this;
        }

        public ClaimDTOBuilder withDescription(String description) {
            claimDTO.setDescription(description);
            return this;
        }

        public ClaimDTOBuilder withStatus(Claim.ClaimStatus status) {
            claimDTO.setStatus(status);
            return this;
        }

        public ClaimDTOBuilder withType(Claim.ClaimType type) {
            claimDTO.setType(type);
            return this;
        }

        public ClaimDTO build() {
            return claimDTO;
        }
    }

    /**
     * Builder for PolicySummary objects
     */
    public static class PolicySummaryBuilder {
        private PolicySummary policy = new PolicySummary();

        public PolicySummaryBuilder() {
            // Set sensible defaults
            policy.setPolicyNumber("POL-001");
            policy.setPolicyHolderName("John Doe");
            policy.setEmail("john.doe@example.com");
            policy.setGender("Male"); // KAN-13: Default gender for test data
            policy.setPolicyType("Auto");
            policy.setStartDate(LocalDate.now().minusYears(1));
            policy.setEndDate(LocalDate.now().plusYears(1));
            policy.setStatus("ACTIVE");
            // KAN-24: Default nominee information for test data
            policy.setNomineeName("Jane Doe");
            policy.setNomineeRelationship("Spouse");
            policy.setNomineeContactInfo("jane.doe@example.com");
            policy.setNomineeIdentification("ID123456789");
            // KAN-25: Default address information for test data
            policy.setStreetAddress("123 Main Street");
            policy.setCity("Springfield");
            policy.setState("IL");
            policy.setPostalCode("62701");
            policy.setCountry("USA");
            policy.setAddressType("Residential");
        }

        public PolicySummaryBuilder withPolicyNumber(String policyNumber) {
            policy.setPolicyNumber(policyNumber);
            return this;
        }

        public PolicySummaryBuilder withPolicyHolderName(String name) {
            policy.setPolicyHolderName(name);
            return this;
        }

        public PolicySummaryBuilder withEmail(String email) {
            policy.setEmail(email);
            return this;
        }

        public PolicySummaryBuilder withGender(String gender) {
            policy.setGender(gender);
            return this;
        }

        public PolicySummaryBuilder withDateOfBirth(LocalDate dateOfBirth) {
            policy.setDateOfBirth(dateOfBirth);
            return this;
        }

        public PolicySummaryBuilder withPolicyType(String type) {
            policy.setPolicyType(type);
            return this;
        }

        public PolicySummaryBuilder withPolicyStartDate(LocalDate startDate) {
            policy.setStartDate(startDate);
            return this;
        }

        public PolicySummaryBuilder withPolicyEndDate(LocalDate endDate) {
            policy.setEndDate(endDate);
            return this;
        }

        public PolicySummaryBuilder withPremiumAmount(Double premiumAmount) {
            policy.setPremiumAmount(premiumAmount);
            return this;
        }

        public PolicySummaryBuilder withCoverageAmount(Double coverageAmount) {
            policy.setCoverageAmount(coverageAmount);
            return this;
        }

        public PolicySummaryBuilder withStartDate(LocalDate startDate) {
            policy.setStartDate(startDate);
            return this;
        }

        public PolicySummaryBuilder withEndDate(LocalDate endDate) {
            policy.setEndDate(endDate);
            return this;
        }

        public PolicySummaryBuilder withStatus(String status) {
            policy.setStatus(status);
            return this;
        }

        // KAN-24: Nominee information builder methods
        public PolicySummaryBuilder withNomineeName(String nomineeName) {
            policy.setNomineeName(nomineeName);
            return this;
        }

        public PolicySummaryBuilder withNomineeRelationship(String nomineeRelationship) {
            policy.setNomineeRelationship(nomineeRelationship);
            return this;
        }

        public PolicySummaryBuilder withNomineeContactInfo(String nomineeContactInfo) {
            policy.setNomineeContactInfo(nomineeContactInfo);
            return this;
        }

        public PolicySummaryBuilder withNomineeIdentification(String nomineeIdentification) {
            policy.setNomineeIdentification(nomineeIdentification);
            return this;
        }

        // KAN-25: Address information builder methods
        public PolicySummaryBuilder withStreetAddress(String streetAddress) {
            policy.setStreetAddress(streetAddress);
            return this;
        }

        public PolicySummaryBuilder withCity(String city) {
            policy.setCity(city);
            return this;
        }

        public PolicySummaryBuilder withState(String state) {
            policy.setState(state);
            return this;
        }

        public PolicySummaryBuilder withPostalCode(String postalCode) {
            policy.setPostalCode(postalCode);
            return this;
        }

        public PolicySummaryBuilder withCountry(String country) {
            policy.setCountry(country);
            return this;
        }

        public PolicySummaryBuilder withAddressType(String addressType) {
            policy.setAddressType(addressType);
            return this;
        }

        public PolicySummaryBuilder active() {
            policy.setStatus("ACTIVE");
            return this;
        }

        public PolicySummaryBuilder expired() {
            policy.setStatus("EXPIRED");
            return this;
        }

        public PolicySummaryBuilder cancelled() {
            policy.setStatus("CANCELLED");
            return this;
        }

        public PolicySummaryBuilder male() {
            policy.setGender("Male");
            return this;
        }

        public PolicySummaryBuilder female() {
            policy.setGender("Female");
            return this;
        }

        public PolicySummaryBuilder nonBinary() {
            policy.setGender("Non-binary");
            return this;
        }

        public PolicySummary build() {
            return policy;
        }
    }

    // Static factory methods for convenience

    public static ClaimBuilder claim() {
        return new ClaimBuilder();
    }

    public static ClaimDTOBuilder claimDTO() {
        return new ClaimDTOBuilder();
    }

    public static PolicySummaryBuilder policy() {
        return new PolicySummaryBuilder();
    }

    // Quick creation methods for common test scenarios

    public static Claim createDefaultClaim() {
        return claim().build();
    }

    public static ClaimDTO createDefaultClaimDTO() {
        return claimDTO().build();
    }

    public static PolicySummary createDefaultPolicy() {
        return policy().build();
    }

    // Scenario-specific factory methods

    public static Claim createApprovedAutoClaim() {
        return claim()
                .autoClaim()
                .approved()
                .withAmount("2500.00")
                .withDescription("Approved auto insurance claim for collision damage")
                .build();
    }

    public static Claim createRejectedHealthClaim() {
        return claim()
                .healthClaim()
                .rejected()
                .withAmount("5000.00")
                .withDescription("Rejected health insurance claim - pre-existing condition")
                .build();
    }

    public static PolicySummary createExpiredPolicy() {
        return policy()
                .expired()
                .withEndDate(LocalDate.now().minusDays(30))
                .withPolicyType("Health")
                .build();
    }

    // KAN-13: Gender-specific test data factory methods
    public static PolicySummary createMalePolicyHolder() {
        return policy()
                .male()
                .withPolicyHolderName("John Smith")
                .withEmail("john.smith@example.com")
                .build();
    }

    public static PolicySummary createFemalePolicyHolder() {
        return policy()
                .female()
                .withPolicyHolderName("Jane Doe")
                .withEmail("jane.doe@example.com")
                .build();
    }

    public static PolicySummary createNonBinaryPolicyHolder() {
        return policy()
                .nonBinary()
                .withPolicyHolderName("Alex Johnson")
                .withEmail("alex.johnson@example.com")
                .build();
    }

    // KAN-24: Nominee-specific test data factory methods
    public static PolicySummary createPolicyWithSpouseNominee() {
        return policy()
                .withPolicyHolderName("John Smith")
                .withEmail("john.smith@example.com")
                .withNomineeName("Mary Smith")
                .withNomineeRelationship("Spouse")
                .withNomineeContactInfo("mary.smith@example.com")
                .withNomineeIdentification("ID123456789")
                .build();
    }

    public static PolicySummary createPolicyWithChildNominee() {
        return policy()
                .withPolicyHolderName("Sarah Johnson")
                .withEmail("sarah.johnson@example.com")
                .withNomineeName("Emma Johnson")
                .withNomineeRelationship("Child")
                .withNomineeContactInfo("emma.johnson@example.com")
                .withNomineeIdentification("ID987654321")
                .build();
    }

    public static PolicySummary createPolicyWithParentNominee() {
        return policy()
                .withPolicyHolderName("Michael Brown")
                .withEmail("michael.brown@example.com")
                .withNomineeName("Robert Brown")
                .withNomineeRelationship("Parent")
                .withNomineeContactInfo("robert.brown@example.com")
                .withNomineeIdentification("ID555666777")
                .build();
    }

    // KAN-25: Address-specific test data factory methods
    public static PolicySummary createPolicyWithBusinessAddress() {
        return policy()
                .withPolicyHolderName("ABC Corporation")
                .withEmail("contact@abccorp.com")
                .withStreetAddress("100 Business Plaza")
                .withCity("Chicago")
                .withState("IL")
                .withPostalCode("60601")
                .withCountry("USA")
                .withAddressType("Business")
                .build();
    }

    public static PolicySummary createPolicyWithInternationalAddress() {
        return policy()
                .withPolicyHolderName("International Client")
                .withEmail("client@international.co.uk")
                .withStreetAddress("10 Downing Street")
                .withCity("London")
                .withState("England")
                .withPostalCode("SW1A 2AA")
                .withCountry("United Kingdom")
                .withAddressType("Residential")
                .build();
    }

    public static PolicySummary createPolicyWithMailingAddress() {
        return policy()
                .withPolicyHolderName("Remote Worker")
                .withEmail("remote@worker.com")
                .withStreetAddress("P.O. Box 12345")
                .withCity("Remote City")
                .withState("CA")
                .withPostalCode("90210")
                .withCountry("USA")
                .withAddressType("Mailing")
                .build();
    }
}
