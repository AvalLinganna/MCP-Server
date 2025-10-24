package com.zurich.poc.automation.data;

import com.zurich.poc.model.PolicySummary;
import com.zurich.poc.util.TestDataBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Test Data Manager for Automation Testing
 * 
 * Provides comprehensive test data setup and cleanup for automated testing scenarios.
 * Supports different test data sets for various test categories.
 */
@Component
@Profile({"test", "automation-test", "regression-test"})
public class TestDataManager {

    private static final Logger log = LoggerFactory.getLogger(TestDataManager.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private DataSource dataSource;
    
    private List<String> createdPolicyIds = new ArrayList<>();
    
    /**
     * Setup test data for unit tests
     */
    public void setupUnitTestData() {
        log.info("Setting up unit test data...");
        
        // Create basic test policies
        List<PolicySummary> basicPolicies = createBasicTestPolicies();
        persistTestPolicies(basicPolicies);
        
        log.info("Unit test data setup completed. Created {} policies", basicPolicies.size());
    }
    
    /**
     * Setup test data for integration tests
     */
    public void setupIntegrationTestData() {
        log.info("Setting up integration test data...");
        
        // Create comprehensive test data for integration scenarios
        List<PolicySummary> integrationPolicies = createIntegrationTestPolicies();
        persistTestPolicies(integrationPolicies);
        
        // Setup additional test data relationships
        setupTestDataRelationships();
        
        log.info("Integration test data setup completed. Created {} policies", integrationPolicies.size());
    }
    
    /**
     * Setup test data for regression tests
     */
    public void setupRegressionTestData() {
        log.info("Setting up regression test data...");
        
        // Create data covering all implemented features
        List<PolicySummary> regressionPolicies = createRegressionTestPolicies();
        persistTestPolicies(regressionPolicies);
        
        // Verify data integrity
        verifyTestDataIntegrity();
        
        log.info("Regression test data setup completed. Created {} policies", regressionPolicies.size());
    }
    
    /**
     * Setup test data for performance tests
     */
    public void setupPerformanceTestData() {
        log.info("Setting up performance test data...");
        
        // Create large data set for performance testing
        List<PolicySummary> performancePolicies = createPerformanceTestPolicies(1000);
        persistTestPoliciesInBatches(performancePolicies, 100);
        
        log.info("Performance test data setup completed. Created {} policies", performancePolicies.size());
    }
    
    /**
     * Create basic test policies for unit tests
     */
    private List<PolicySummary> createBasicTestPolicies() {
        List<PolicySummary> policies = new ArrayList<>();
        
        // Policy with all KAN-24 nominee fields
        PolicySummary nomineePolicy = TestDataBuilder.policy()
                .withPolicyNumber(generateTestPolicyNumber())
                .withPolicyHolderName("John Nominee Test")
                .male()
                .withNomineeName("Jane Nominee")
                .withNomineeRelationship("Spouse")
                .withNomineeContactInfo("jane.nominee@test.com")
                .withNomineeIdentification("NOM123456")
                .build();
        policies.add(nomineePolicy);
        
        // Policy with all KAN-25 address fields
        PolicySummary addressPolicy = TestDataBuilder.policy()
                .withPolicyNumber(generateTestPolicyNumber())
                .withPolicyHolderName("Jane Address Test")
                .female()
                .withStreetAddress("123 Test Street")
                .withCity("Test City")
                .withState("Test State")
                .withPostalCode("12345")
                .withCountry("Test Country")
                .withAddressType("Residential")
                .build();
        policies.add(addressPolicy);
        
        // Policy with both nominee and address fields
        PolicySummary completePolicy = TestDataBuilder.policy()
                .withPolicyNumber(generateTestPolicyNumber())
                .withPolicyHolderName("Complete Test Policy")
                .nonBinary()
                .withNomineeName("Complete Nominee")
                .withNomineeRelationship("Child")
                .withNomineeContactInfo("complete.nominee@test.com")
                .withNomineeIdentification("COM123456")
                .withStreetAddress("456 Complete Avenue")
                .withCity("Complete City")
                .withState("Complete State")
                .withPostalCode("67890")
                .withCountry("Complete Country")
                .withAddressType("Business")
                .build();
        policies.add(completePolicy);
        
        return policies;
    }
    
    /**
     * Create comprehensive test policies for integration tests
     */
    private List<PolicySummary> createIntegrationTestPolicies() {
        List<PolicySummary> policies = new ArrayList<>();
        
        // Business address policy
        policies.add(TestDataBuilder.createPolicyWithBusinessAddress());
        
        // International address policy
        policies.add(TestDataBuilder.createPolicyWithInternationalAddress());
        
        // Mailing address policy
        policies.add(TestDataBuilder.createPolicyWithMailingAddress());
        
        // Edge case policies
        policies.add(createEdgeCasePolicy());
        policies.add(createMinimalPolicy());
        policies.add(createMaximalPolicy());
        
        return policies;
    }
    
    /**
     * Create comprehensive test policies for regression tests
     */
    private List<PolicySummary> createRegressionTestPolicies() {
        List<PolicySummary> policies = new ArrayList<>();
        
        // Add all basic test policies
        policies.addAll(createBasicTestPolicies());
        
        // Add all integration test policies
        policies.addAll(createIntegrationTestPolicies());
        
        // Add specific regression scenarios
        policies.add(createRegressionScenario1());
        policies.add(createRegressionScenario2());
        policies.add(createRegressionScenario3());
        
        return policies;
    }
    
    /**
     * Create large number of policies for performance tests
     */
    private List<PolicySummary> createPerformanceTestPolicies(int count) {
        List<PolicySummary> policies = new ArrayList<>();
        
        for (int i = 0; i < count; i++) {
            PolicySummary policy = TestDataBuilder.policy()
                    .withPolicyNumber(generateTestPolicyNumber())
                    .withPolicyHolderName("Performance Test Policy " + i)
                    .withGender(i % 2 == 0 ? "Male" : "Female")
                    .withNomineeName("Nominee " + i)
                    .withNomineeRelationship(getNomineeRelationship(i))
                    .withStreetAddress(i + " Performance Street")
                    .withCity("Performance City " + (i % 10))
                    .withState("Performance State " + (i % 5))
                    .withPostalCode(String.format("%05d", i))
                    .withCountry("Performance Country")
                    .withAddressType(getAddressType(i))
                    .build();
            
            policies.add(policy);
        }
        
        return policies;
    }
    
    /**
     * Create edge case policy for testing boundary conditions
     */
    private PolicySummary createEdgeCasePolicy() {
        return TestDataBuilder.policy()
                .withPolicyNumber(generateTestPolicyNumber())
                .withPolicyHolderName("Edge Case Policy with Very Long Name That Tests Maximum Length Limits")
                .withGender("Non-binary")
                .withNomineeName("Edge")
                .withNomineeRelationship("Other")
                .withStreetAddress("1 A")  // Minimal address
                .withCity("X")  // Single character city
                .withState("Y")  // Single character state
                .withPostalCode("00000")  // All zeros
                .withCountry("Z")  // Single character country
                .withAddressType("Other")
                .build();
    }
    
    /**
     * Create minimal policy with only required fields
     */
    private PolicySummary createMinimalPolicy() {
        return TestDataBuilder.policy()
                .withPolicyNumber(generateTestPolicyNumber())
                .withPolicyHolderName("Minimal Policy")
                .build();
    }
    
    /**
     * Create maximal policy with all fields populated
     */
    private PolicySummary createMaximalPolicy() {
        return TestDataBuilder.policy()
                .withPolicyNumber(generateTestPolicyNumber())
                .withPolicyHolderName("Maximal Test Policy with Complete Information Set")
                .withGender("Female")
                .withDateOfBirth(LocalDate.of(1990, 1, 1))
                .withPolicyStartDate(LocalDate.now())
                .withPolicyEndDate(LocalDate.now().plusYears(1))
                .withPremiumAmount(1000.00)
                .withCoverageAmount(100000.00)
                .withNomineeName("Maximal Nominee with Complete Details")
                .withNomineeRelationship("Spouse")
                .withNomineeContactInfo("maximal.nominee@complete.test.com")
                .withNomineeIdentification("MAX123456789")
                .withStreetAddress("123 Maximal Street, Apartment 456, Building Complex ABC")
                .withCity("Maximal City with Long Name")
                .withState("Maximal State Province")
                .withPostalCode("MAX-12345")
                .withCountry("Maximal Country Federation")
                .withAddressType("Residential")
                .build();
    }
    
    /**
     * Create specific regression test scenarios
     */
    private PolicySummary createRegressionScenario1() {
        // KAN-24 regression: Nominee information validation
        return TestDataBuilder.policy()
                .withPolicyNumber("REG-KAN24-001")
                .withPolicyHolderName("KAN-24 Regression Test")
                .withNomineeName("Regression Nominee")
                .withNomineeRelationship("Child")
                .withNomineeContactInfo("regression@kan24.test")
                .withNomineeIdentification("REG24001")
                .build();
    }
    
    private PolicySummary createRegressionScenario2() {
        // KAN-25 regression: Address information validation
        return TestDataBuilder.policy()
                .withPolicyNumber("REG-KAN25-001")
                .withPolicyHolderName("KAN-25 Regression Test")
                .withStreetAddress("123 Regression Avenue")
                .withCity("Regression City")
                .withState("Regression State")
                .withPostalCode("REG25")
                .withCountry("Regression Country")
                .withAddressType("Business")
                .build();
    }
    
    private PolicySummary createRegressionScenario3() {
        // Combined KAN-24 + KAN-25 regression
        return TestDataBuilder.policy()
                .withPolicyNumber("REG-COMBINED-001")
                .withPolicyHolderName("Combined Features Regression")
                .withNomineeName("Combined Nominee")
                .withNomineeRelationship("Parent")
                .withNomineeContactInfo("combined@regression.test")
                .withNomineeIdentification("COMB001")
                .withStreetAddress("456 Combined Street")
                .withCity("Combined City")
                .withState("Combined State")
                .withPostalCode("CMB123")
                .withCountry("Combined Country")
                .withAddressType("Mailing")
                .build();
    }
    
    /**
     * Persist test policies to database
     */
    private void persistTestPolicies(List<PolicySummary> policies) {
        for (PolicySummary policy : policies) {
            persistPolicy(policy);
            createdPolicyIds.add(policy.getPolicyNumber());
        }
    }
    
    /**
     * Persist test policies in batches for performance
     */
    private void persistTestPoliciesInBatches(List<PolicySummary> policies, int batchSize) {
        for (int i = 0; i < policies.size(); i += batchSize) {
            List<PolicySummary> batch = policies.subList(i, Math.min(i + batchSize, policies.size()));
            persistTestPolicies(batch);
            
            if (i % (batchSize * 10) == 0) {
                log.debug("Persisted {} policies...", i);
            }
        }
    }
    
    /**
     * Persist individual policy
     */
    private void persistPolicy(PolicySummary policy) {
        // Implementation depends on your repository layer
        // This is a placeholder for the actual persistence logic
        log.trace("Persisting test policy: {}", policy.getPolicyNumber());
    }
    
    /**
     * Setup additional test data relationships
     */
    private void setupTestDataRelationships() {
        log.debug("Setting up test data relationships...");
        // Implementation for setting up related entities like claims, endorsements, etc.
    }
    
    /**
     * Verify test data integrity
     */
    private void verifyTestDataIntegrity() {
        log.debug("Verifying test data integrity...");
        // Implementation for data validation checks
    }
    
    /**
     * Clean up all test data
     */
    public void cleanupTestData() {
        log.info("Cleaning up test data...");
        
        try {
            // Clean up policies by policy numbers
            for (String policyNumber : createdPolicyIds) {
                cleanupPolicy(policyNumber);
            }
            
            // Clean up related data
            cleanupRelatedTestData();
            
            // Reset sequences if needed
            resetDatabaseSequences();
            
            createdPolicyIds.clear();
            
            log.info("Test data cleanup completed");
        } catch (Exception e) {
            log.error("Error during test data cleanup", e);
            throw new RuntimeException("Failed to cleanup test data", e);
        }
    }
    
    /**
     * Clean up individual policy
     */
    private void cleanupPolicy(String policyNumber) {
        log.trace("Cleaning up policy: {}", policyNumber);
        // Implementation for policy cleanup
    }
    
    /**
     * Clean up related test data
     */
    private void cleanupRelatedTestData() {
        log.debug("Cleaning up related test data...");
        // Clean up claims, endorsements, and other related entities
    }
    
    /**
     * Reset database sequences
     */
    private void resetDatabaseSequences() {
        log.debug("Resetting database sequences...");
        // Reset auto-increment sequences if needed for consistent testing
    }
    
    /**
     * Generate unique test policy number
     */
    private String generateTestPolicyNumber() {
        return "TEST-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    /**
     * Get nominee relationship for performance test data
     */
    private String getNomineeRelationship(int index) {
        String[] relationships = {"Spouse", "Child", "Parent", "Sibling", "Other"};
        return relationships[index % relationships.length];
    }
    
    /**
     * Get address type for performance test data
     */
    private String getAddressType(int index) {
        String[] types = {"Residential", "Business", "Mailing"};
        return types[index % types.length];
    }
}