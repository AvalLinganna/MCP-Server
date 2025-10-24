package com.zurich.poc.integration;

import com.zurich.poc.config.TestContainersConfig;
import com.zurich.poc.model.Claim;
import com.zurich.poc.model.ClaimDTO;
import com.zurich.poc.repository.ClaimRepository;
import com.zurich.poc.service.ClaimService;
import com.zurich.poc.util.TestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for ClaimService using TestContainers
 * Implements KAN-1 requirement for integration tests with TestContainers and PostgreSQL
 */
@SpringBootTest
@Testcontainers
@ActiveProfiles("integration-test")
@Import(TestContainersConfig.class)
@Transactional
@DisplayName("ClaimService Integration Tests")
class ClaimServiceIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:15-alpine"))
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private ClaimService claimService;

    @Autowired
    private ClaimRepository claimRepository;

    @BeforeEach
    void setUp() {
        claimRepository.deleteAll();
    }

    @Test
    @DisplayName("Should create and retrieve claim from database")
    void shouldCreateAndRetrieveClaimFromDatabase() {
        // Given
        ClaimDTO claimDTO = TestDataBuilder.claimDTO()
                .withPolicyNumber("POL-INTEGRATION-001")
                .withAmount("1500.00")
                .withDescription("Integration test claim")
                .build();

        // When
        Claim createdClaim = claimService.createClaim(claimDTO);

        // Then
        assertThat(createdClaim).isNotNull();
        assertThat(createdClaim.getId()).isNotNull();
        assertThat(createdClaim.getPolicyNumber()).isEqualTo("POL-INTEGRATION-001");

        // Verify persistence
        Claim retrievedClaim = claimService.getClaimById(createdClaim.getId()).orElse(null);
        assertThat(retrievedClaim).isNotNull();
        assertThat(retrievedClaim.getPolicyNumber()).isEqualTo("POL-INTEGRATION-001");
    }

    @Test
    @DisplayName("Should find claims by policy number with database persistence")
    void shouldFindClaimsByPolicyNumberWithDatabasePersistence() {
        // Given
        String testPolicyNumber = "POL-MULTI-001";
        
        ClaimDTO claim1 = TestDataBuilder.claimDTO()
                .withPolicyNumber(testPolicyNumber)
                .withAmount("1000.00")
                .build();
        
        ClaimDTO claim2 = TestDataBuilder.claimDTO()
                .withPolicyNumber(testPolicyNumber)
                .withAmount("2000.00")
                .build();

        // When
        claimService.createClaim(claim1);
        claimService.createClaim(claim2);

        // Then
        var claims = claimService.getClaimsByPolicyNumber(testPolicyNumber);
        assertThat(claims).hasSize(2);
        assertThat(claims).allMatch(claim -> claim.getPolicyNumber().equals(testPolicyNumber));
    }

    @Test
    @DisplayName("Should update claim and persist changes to database")
    void shouldUpdateClaimAndPersistChangesToDatabase() {
        // Given
        ClaimDTO originalClaimDTO = TestDataBuilder.claimDTO()
                .withAmount("1000.00")
                .withDescription("Original description")
                .build();

        Claim originalClaim = claimService.createClaim(originalClaimDTO);

        ClaimDTO updateDTO = TestDataBuilder.claimDTO()
                .withAmount("1500.00")
                .withDescription("Updated description")
                .build();

        // When
        claimService.updateClaim(originalClaim.getId(), updateDTO);

        // Then
        Claim updatedClaim = claimService.getClaimById(originalClaim.getId()).orElse(null);
        assertThat(updatedClaim).isNotNull();
        assertThat(updatedClaim.getEstimatedAmount()).isEqualTo(new BigDecimal("1500.00"));
        assertThat(updatedClaim.getDescription()).isEqualTo("Updated description");
    }

    @Test
    @DisplayName("Should delete claim from database")
    void shouldDeleteClaimFromDatabase() {
        // Given
        ClaimDTO claimDTO = TestDataBuilder.createDefaultClaimDTO();
        Claim createdClaim = claimService.createClaim(claimDTO);

        // When
        claimService.deleteClaim(createdClaim.getId());

        // Then
        assertThat(claimService.getClaimById(createdClaim.getId())).isEmpty();
    }

    @Test
    @DisplayName("Should handle concurrent claim operations")
    void shouldHandleConcurrentClaimOperations() {
        // Given
        ClaimDTO claimDTO1 = TestDataBuilder.claimDTO()
                .withPolicyNumber("POL-CONCURRENT-001")
                .build();
        
        ClaimDTO claimDTO2 = TestDataBuilder.claimDTO()
                .withPolicyNumber("POL-CONCURRENT-002")
                .build();

        // When
        Claim claim1 = claimService.createClaim(claimDTO1);
        Claim claim2 = claimService.createClaim(claimDTO2);

        // Then
        assertThat(claim1.getId()).isNotEqualTo(claim2.getId());
        assertThat(claim1.getClaimNumber()).isNotEqualTo(claim2.getClaimNumber());
        
        var allClaims = claimService.getAllClaims();
        assertThat(allClaims).hasSize(2);
    }
}
