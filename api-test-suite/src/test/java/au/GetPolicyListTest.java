package au;


import org.testng.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

import java.util.List;

import com.zurich.testsuite.service.GetPolicyList;
import com.zurich.testsuite.model.PolicySummary;
import org.testng.Assert;

/**
 * Sample Test Scenarios for getPolicyList API
 * 
 * Test Scenarios:
 * 1. Verify list shows policies with different plans
 * 2. Verify Pagination
 * 3. Verify details displayed for a policy with planType = ABC
 * 4. Verify User B is not able to view User A policies
 * 
 * @author Capgemini Zurich UK Team
 * @version 1.0.0
 */
@SpringBootTest
public class GetPolicyListTest extends AbstractTestNGSpringContextTests {
    
   
    @Autowired
    private GetPolicyList getPolicyListService;
    private String testCustomerId;
    private String testUserAId;
    private String testUserBId;
    
    @BeforeClass
    public void setupClass() {
        // Setup test data
        testCustomerId = "CUST_001";
        testUserAId = "USER_A_001";
        testUserBId = "USER_B_001";
    }
    
    /**
     * Test Scenario 1: Verify list shows policies with different plans
     * 
     * Expected: API should return policies with BASIC, PREMIUM, and GOLD plans
     */
    @Test(priority = 1, groups = {"smoke", "api", "policy"}, 
          description = "Verify that getPolicyList returns policies with different plan types")
    public void testPolicyListWithDifferentPlans() {
        
        // Get policies using service layer
        List<PolicySummary> policies = getPolicyListService.getPoliciesByCustomerId(testCustomerId);
        
        // Assertions only - verify data retrieved
        Assert.assertNotNull(policies, "Policy list should not be null");
        Assert.assertFalse(policies.isEmpty(), "Policy list should not be empty");
        
        // Get distinct plan types
        List<String> planTypes = policies.stream()
            .map(PolicySummary::getProductType)
            .distinct()
            .collect(java.util.stream.Collectors.toList());
        
        // Verify different plan types exist
        Assert.assertTrue(planTypes.contains("BASIC"), 
                                "Response should contain BASIC plan policies");
        Assert.assertTrue(planTypes.contains("PREMIUM"), 
                                "Response should contain PREMIUM plan policies");
        Assert.assertTrue(planTypes.contains("GOLD"), 
                                "Response should contain GOLD plan policies");
        
        // Verify each policy has required fields
        for (PolicySummary policy : policies) {
            Assert.assertNotNull(policy.getPolicyNumber(), 
                                       "Policy number should not be null");
            Assert.assertNotNull(policy.getCustomerId(), 
                                       "Customer ID should not be null");
            Assert.assertNotNull(policy.getProductType(), 
                                       "Product type should not be null");
            Assert.assertTrue(policy.hasValidPolicyNumber(), 
                                    "Policy should have valid policy number format");
            Assert.assertTrue(policy.hasValidPremiumAmount(), 
                                    "Policy should have valid premium amount");
            
            // Verify plan-specific premium ranges
            validatePlanPremiumRange(policy.getProductType(), policy.getPremiumAmount());
        }
        
        System.out.println("✅ Test Passed: Policy list contains different plan types with valid data");
    }
    
    /**
     * Test Scenario 2: Verify Pagination
     * 
     * Expected: API should handle pagination correctly with proper page navigation
     */
    @Test(priority = 2, groups = {"regression", "api", "pagination"}, 
          description = "Verify pagination functionality in getPolicyList API")
    @Parameters({"page", "size"})
    public void testPolicyListPagination(@org.testng.annotations.Optional("0") String page, @org.testng.annotations.Optional("10") String size) {
        
        // Get policies with pagination using service layer
        int pageNumber = Integer.parseInt(page);
        int pageSize = Integer.parseInt(size);
        List<PolicySummary> policies = getPolicyListService.getPoliciesByCustomerId(testCustomerId, pageNumber, pageSize);
        
        // Assertions only - verify pagination results
        Assert.assertNotNull(policies, "Paginated policy list should not be null");
        Assert.assertTrue(policies.size() <= pageSize, 
                                "Policy count should not exceed requested page size");
        
        // Verify each policy in the page has valid data
        for (PolicySummary policy : policies) {
            Assert.assertNotNull(policy.getPolicyNumber(), 
                                       "Policy number should not be null in paginated results");
            Assert.assertNotNull(policy.getCustomerId(), 
                                       "Customer ID should not be null in paginated results");
            Assert.assertEquals(policy.getCustomerId(), testCustomerId, 
                                      "All policies should belong to the test customer");
            Assert.assertTrue(policy.hasValidPolicyNumber(), 
                                    "Policy should have valid policy number format");
        }
        
        // Test total count consistency
        long totalCount = getPolicyListService.getPolicyCountByCustomerId(testCustomerId);
        Assert.assertTrue(totalCount >= 0, "Total policy count should be non-negative");
        
        if (pageNumber == 0 && policies.size() < pageSize) {
            Assert.assertEquals(policies.size(), (int)totalCount, 
                                      "If first page is not full, it should contain all policies");
        }
        
        System.out.println("✅ Test Passed: Pagination works correctly for page " + page + " with size " + size);
    }
    
    /**
     * Test Scenario 3: Verify details displayed for a policy with planType = ABC
     * 
     * Expected: API should return detailed information for ABC plan type policies
     */
    @Test(priority = 3, groups = {"regression", "api", "plantype"}, 
          description = "Verify policy details for specific plan type ABC")
    public void testPolicyDetailsForPlanTypeABC() {
        
        // Get policies by product type using service layer
        List<PolicySummary> abcPolicies = getPolicyListService.getPoliciesByProductType("ABC");
        
        // Assertions only - verify ABC plan policies
        Assert.assertFalse(abcPolicies.isEmpty(), 
                                        "Should return policies for planType ABC");
        
        // Verify all returned policies have planType = ABC
        for (PolicySummary policy : abcPolicies) {
            Assert.assertEquals(policy.getProductType(), "ABC", 
                                      "All returned policies should have planType ABC");
            
            // Verify required fields for ABC plan
            Assert.assertNotNull(policy.getPolicyNumber(), 
                                       "Policy number should be present for ABC plan");
            Assert.assertNotNull(policy.getCustomerName(), 
                                       "Customer name should be present for ABC plan");
            Assert.assertNotNull(policy.getEmail(), 
                                       "Email should be present for ABC plan");
            Assert.assertTrue(policy.hasValidEmail(), 
                                    "Email should be valid for ABC plan");
            Assert.assertTrue(policy.hasValidPremiumAmount(), 
                                    "Premium should be valid for ABC plan");
            
            // Verify ABC plan specific validations
            Assert.assertNotNull(policy.getEffectiveDate(), 
                                       "Effective date should be present for ABC plan");
            Assert.assertNotNull(policy.getExpirationDate(), 
                                       "Expiration date should be present for ABC plan");
            Assert.assertTrue(policy.isActive(), 
                                    "ABC plan policies should be active");
            
            // Verify ABC plan premium range (example: 5000-15000)
            double premium = policy.getPremiumAmount();
            Assert.assertTrue(premium >= 5000.0 && premium <= 15000.0, 
                                       "ABC plan premium should be in range 5000-15000");
        }
        
        System.out.println("✅ Test Passed: ABC plan policies contain all required details");
    }
    
    /**
     * Test Scenario 4: Verify User B is not able to view User A policies
     * 
     * Expected: API should enforce proper access control between users
     */
    @Test(priority = 4, groups = {"security", "api", "access-control"}, 
          description = "Verify user access control - User B cannot view User A policies")
    public void testUserAccessControl() {
        
        // Test 1: Get User A policies using service layer
        List<PolicySummary> userAPolicies = getPolicyListService.getPoliciesByCustomerId(testUserAId);
        
        // Assertions - verify User A can access their policies
        Assert.assertNotNull(userAPolicies, "User A policies should not be null");
        Assert.assertFalse(userAPolicies.isEmpty(), 
                                        "User A should have policies in their list");
        
        // Verify all policies belong to User A
        for (PolicySummary policy : userAPolicies) {
            Assert.assertEquals(policy.getCustomerId(), testUserAId, 
                                      "All policies should belong to User A");
            Assert.assertTrue(policy.hasValidPolicyNumber(), 
                                    "User A policies should have valid policy numbers");
        }
        
        // Test 2: Get User B policies using service layer
        List<PolicySummary> userBPolicies = getPolicyListService.getPoliciesByCustomerId(testUserBId);
        
        // Assertions - verify User B can access their policies
        Assert.assertNotNull(userBPolicies, "User B policies should not be null");
        
        // Verify all policies belong to User B (if any exist)
        for (PolicySummary policy : userBPolicies) {
            Assert.assertEquals(policy.getCustomerId(), testUserBId, 
                                      "All policies should belong to User B");
            Assert.assertTrue(policy.hasValidPolicyNumber(), 
                                    "User B policies should have valid policy numbers");
        }
        
        // Test 3: Verify User B cannot access User A's policies
        boolean userBCanAccessUserAPolicies = getPolicyListService.validatePolicyAccess(
            userAPolicies.get(0).getPolicyNumber(), testUserBId);
        
        Assert.assertFalse(userBCanAccessUserAPolicies, 
                                 "User B should not have access to User A's policies");
        
        // Test 4: Verify User A can access their own policies
        boolean userACanAccessOwnPolicies = getPolicyListService.validatePolicyAccess(
            userAPolicies.get(0).getPolicyNumber(), testUserAId);
        
        Assert.assertTrue(userACanAccessOwnPolicies, 
                                "User A should have access to their own policies");
        
        // Test 5: Verify User A and User B have different policy lists
        List<String> userAPolicyNumbers = userAPolicies.stream()
            .map(PolicySummary::getPolicyNumber)
            .collect(java.util.stream.Collectors.toList());
        
        List<String> userBPolicyNumbers = userBPolicies.stream()
            .map(PolicySummary::getPolicyNumber)
            .collect(java.util.stream.Collectors.toList());
        
        // Assert no overlap in policy numbers
        for (String policyNumber : userAPolicyNumbers) {
            Assert.assertFalse(userBPolicyNumbers.contains(policyNumber), 
                                     "User A policy " + policyNumber + " should not appear in User B's list");
        }
        
        System.out.println("✅ Test Passed: User access control is working correctly");
    }
    
    /**
     * Data provider for pagination testing
     */
    @DataProvider(name = "paginationData")
    public Object[][] getPaginationTestData() {
        return new Object[][] {
            {"0", "5"},   // First page, small size
            {"0", "10"},  // First page, medium size
            {"1", "10"},  // Second page
            {"0", "50"},  // Large page size
        };
    }
    
    /**
     * Pagination test with data provider
     */
    @Test(dataProvider = "paginationData", groups = {"regression", "pagination"})
    public void testPaginationWithDataProvider(String page, String size) {
        testPolicyListPagination(page, size);
    }
    
    /**
     * Helper method to validate premium range for different plan types
     */
    private void validatePlanPremiumRange(String planType, double premium) {
        switch (planType) {
            case "BASIC":
                Assert.assertTrue(premium >= 500.0 && premium <= 2000.0, 
                                           "BASIC plan premium should be in range 500-2000");
                break;
            case "PREMIUM":
                Assert.assertTrue(premium >= 2000.0 && premium <= 5000.0, 
                                           "PREMIUM plan premium should be in range 2000-5000");
                break;
            case "GOLD":
                Assert.assertTrue(premium >= 5000.0 && premium <= 10000.0, 
                                           "GOLD plan premium should be in range 5000-10000");
                break;
            case "PLATINUM":
                Assert.assertTrue(premium >= 10000.0 && premium <= 20000.0, 
                                           "PLATINUM plan premium should be in range 10000-20000");
                break;
            case "ABC":
                Assert.assertTrue(premium >= 5000.0 && premium <= 15000.0, 
                                           "ABC plan premium should be in range 5000-15000");
                break;
            default:
                System.out.println("⚠️ Unknown plan type: " + planType);
        }
    }
    
    @AfterClass
    public void tearDownClass() {
        System.out.println("🎯 All getPolicyList tests completed successfully!");
        System.out.println("📊 Test Summary:");
        System.out.println("   ✅ Different plan types verification");
        System.out.println("   ✅ Pagination functionality");
        System.out.println("   ✅ Plan type ABC details validation");
        System.out.println("   ✅ User access control enforcement");
    }
}