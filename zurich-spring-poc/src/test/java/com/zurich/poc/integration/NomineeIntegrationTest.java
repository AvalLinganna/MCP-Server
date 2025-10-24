package com.zurich.poc.integration;

import com.zurich.poc.model.PolicySummary;
import com.zurich.poc.util.TestDataBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for KAN-24: Nominee Information functionality
 * Demonstrates end-to-end nominee information handling in policy management
 */
@DisplayName("KAN-24: Nominee Information Integration Tests")
class NomineeIntegrationTest {

    @Test
    @DisplayName("Should create comprehensive policy with complete policyholder and nominee information")
    void shouldCreateComprehensivePolicyWithCompleteInformation() {
        // Given: A comprehensive policy scenario as per Policy Management requirements
        PolicySummary comprehensivePolicy = TestDataBuilder.policy()
                // Policy Holder Information
                .withPolicyNumber("POL-2024-001")
                .withPolicyHolderName("John Michael Smith")
                .withEmail("john.smith@zurich-customer.com")
                .withGender("Male")
                .withPolicyType("Life Insurance")
                .active()
                // Nominee Information (KAN-24)
                .withNomineeName("Mary Elizabeth Smith")
                .withNomineeRelationship("Spouse")
                .withNomineeContactInfo("mary.smith@zurich-customer.com")
                .withNomineeIdentification("PASSPORT-AB123456")
                .build();

        // When & Then: Verify all policyholder information
        assertThat(comprehensivePolicy.getPolicyNumber()).isEqualTo("POL-2024-001");
        assertThat(comprehensivePolicy.getPolicyHolderName()).isEqualTo("John Michael Smith");
        assertThat(comprehensivePolicy.getEmail()).isEqualTo("john.smith@zurich-customer.com");
        assertThat(comprehensivePolicy.getGender()).isEqualTo("Male");
        assertThat(comprehensivePolicy.getPolicyType()).isEqualTo("Life Insurance");
        assertThat(comprehensivePolicy.getStatus()).isEqualTo("ACTIVE");

        // KAN-24: Verify all nominee information
        assertThat(comprehensivePolicy.getNomineeName()).isEqualTo("Mary Elizabeth Smith");
        assertThat(comprehensivePolicy.getNomineeRelationship()).isEqualTo("Spouse");
        assertThat(comprehensivePolicy.getNomineeContactInfo()).isEqualTo("mary.smith@zurich-customer.com");
        assertThat(comprehensivePolicy.getNomineeIdentification()).isEqualTo("PASSPORT-AB123456");
    }

    @Test
    @DisplayName("Should support life insurance policy with child nominee scenario")
    void shouldSupportLifeInsurancePolicyWithChildNomineeScenario() {
        // Given: A life insurance policy with child as nominee
        PolicySummary lifeInsurancePolicy = TestDataBuilder.createPolicyWithChildNominee();

        // When & Then: Verify life insurance specific nominee scenario
        assertThat(lifeInsurancePolicy.getPolicyHolderName()).isEqualTo("Sarah Johnson");
        assertThat(lifeInsurancePolicy.getNomineeName()).isEqualTo("Emma Johnson");
        assertThat(lifeInsurancePolicy.getNomineeRelationship()).isEqualTo("Child");
        assertThat(lifeInsurancePolicy.getNomineeContactInfo()).isEqualTo("emma.johnson@example.com");
        assertThat(lifeInsurancePolicy.getNomineeIdentification()).isEqualTo("ID987654321");

        // Verify this is appropriate for life insurance context
        assertThat(lifeInsurancePolicy.getNomineeRelationship()).isIn("Child", "Spouse", "Parent");
    }

    @Test
    @DisplayName("Should support comprehensive nominee scenarios as per Policy Management requirements")
    void shouldSupportComprehensiveNomineeScenarios() {
        // Scenario 1: Spouse as nominee
        PolicySummary spouseNomineePolicy = TestDataBuilder.createPolicyWithSpouseNominee();
        assertThat(spouseNomineePolicy.getNomineeRelationship()).isEqualTo("Spouse");
        assertThat(spouseNomineePolicy.getNomineeName()).contains("Smith");

        // Scenario 2: Child as nominee  
        PolicySummary childNomineePolicy = TestDataBuilder.createPolicyWithChildNominee();
        assertThat(childNomineePolicy.getNomineeRelationship()).isEqualTo("Child");
        assertThat(childNomineePolicy.getNomineeName()).contains("Johnson");

        // Scenario 3: Parent as nominee
        PolicySummary parentNomineePolicy = TestDataBuilder.createPolicyWithParentNominee();
        assertThat(parentNomineePolicy.getNomineeRelationship()).isEqualTo("Parent");
        assertThat(parentNomineePolicy.getNomineeName()).contains("Brown");

        // Verify all policies have complete nominee information
        PolicySummary[] policies = {spouseNomineePolicy, childNomineePolicy, parentNomineePolicy};
        for (PolicySummary policy : policies) {
            assertThat(policy.getNomineeName()).isNotBlank();
            assertThat(policy.getNomineeRelationship()).isNotBlank();
            assertThat(policy.getNomineeContactInfo()).isNotBlank();
            assertThat(policy.getNomineeIdentification()).isNotBlank();
        }
    }

    @Test
    @DisplayName("Should maintain data integrity between policyholder and nominee information")
    void shouldMaintainDataIntegrityBetweenPolicyholderAndNomineeInformation() {
        // Given: A policy where nominee shares family name with policyholder
        PolicySummary familyPolicy = TestDataBuilder.policy()
                .withPolicyHolderName("Robert Wilson")
                .withEmail("robert.wilson@family.com")
                .withNomineeName("Linda Wilson")
                .withNomineeRelationship("Spouse")
                .withNomineeContactInfo("linda.wilson@family.com")
                .withNomineeIdentification("DL-WILSON-001")
                .build();

        // When & Then: Verify logical consistency
        String policyHolderLastName = familyPolicy.getPolicyHolderName().split(" ")[1];
        String nomineeLastName = familyPolicy.getNomineeName().split(" ")[1];
        
        // Family members typically share last names
        assertThat(nomineeLastName).isEqualTo(policyHolderLastName);
        
        // Contact domains should be related for family members
        String policyHolderDomain = familyPolicy.getEmail().split("@")[1];
        String nomineeDomain = familyPolicy.getNomineeContactInfo().split("@")[1];
        assertThat(nomineeDomain).isEqualTo(policyHolderDomain);
        
        // Identification should be unique and meaningful
        assertThat(familyPolicy.getNomineeIdentification()).contains("WILSON");
    }

    @Test
    @DisplayName("Should handle nominee information updates while preserving policyholder data")
    void shouldHandleNomineeInformationUpdatesWhilePreservingPolicyholderData() {
        // Given: An existing policy
        PolicySummary originalPolicy = TestDataBuilder.createDefaultPolicy();
        String originalPolicyHolder = originalPolicy.getPolicyHolderName();
        String originalEmail = originalPolicy.getEmail();
        String originalPolicyNumber = originalPolicy.getPolicyNumber();

        // When: Nominee information is updated
        PolicySummary updatedPolicy = TestDataBuilder.policy()
                .withPolicyNumber(originalPolicyNumber)
                .withPolicyHolderName(originalPolicyHolder)
                .withEmail(originalEmail)
                .withGender(originalPolicy.getGender())
                .withPolicyType(originalPolicy.getPolicyType())
                .withStatus(originalPolicy.getStatus())
                // Updated nominee information
                .withNomineeName("Updated Nominee Name")
                .withNomineeRelationship("Updated Relationship")
                .withNomineeContactInfo("updated.nominee@example.com")
                .withNomineeIdentification("UPDATED-ID-001")
                .build();

        // Then: Policyholder information should remain unchanged
        assertThat(updatedPolicy.getPolicyHolderName()).isEqualTo(originalPolicyHolder);
        assertThat(updatedPolicy.getEmail()).isEqualTo(originalEmail);
        assertThat(updatedPolicy.getPolicyNumber()).isEqualTo(originalPolicyNumber);

        // And nominee information should be updated
        assertThat(updatedPolicy.getNomineeName()).isEqualTo("Updated Nominee Name");
        assertThat(updatedPolicy.getNomineeRelationship()).isEqualTo("Updated Relationship");
        assertThat(updatedPolicy.getNomineeContactInfo()).isEqualTo("updated.nominee@example.com");
        assertThat(updatedPolicy.getNomineeIdentification()).isEqualTo("UPDATED-ID-001");
    }
}