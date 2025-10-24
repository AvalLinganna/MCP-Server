package com.zurich.poc.config;

import com.zurich.poc.model.Claim;
import com.zurich.poc.repository.ClaimRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {
    
    private final ClaimRepository claimRepository;
    
    @Bean
    @Profile("!test") // Don't run this in test profile
    public CommandLineRunner initData() {
        return args -> {
            log.info("Initializing sample data");
            
            // Only initialize if the repository is empty
            if (claimRepository.count() == 0) {
                // Create sample claims
                Claim claim1 = new Claim();
                claim1.setId(UUID.randomUUID());
                claim1.setClaimNumber("CLM-2023-001");
                claim1.setPolicyNumber("POL-001");
                claim1.setIncidentDate(LocalDate.now().minusDays(5));
                claim1.setDescription("Car accident on Main Street. Front bumper damaged.");
                claim1.setEstimatedAmount(new BigDecimal("1500.00"));
                claim1.setType(Claim.ClaimType.AUTO);
                claim1.setStatus(Claim.ClaimStatus.SUBMITTED);
                claim1.setClaimantName("John Doe");
                claim1.setClaimantEmail("john.doe@example.com");
                claim1.setClaimantPhone("+1234567890");
                
                Claim claim2 = new Claim();
                claim2.setId(UUID.randomUUID());
                claim2.setClaimNumber("CLM-2023-002");
                claim2.setPolicyNumber("POL-002");
                claim2.setIncidentDate(LocalDate.now().minusDays(10));
                claim2.setDescription("Water damage in kitchen due to pipe burst.");
                claim2.setEstimatedAmount(new BigDecimal("5000.00"));
                claim2.setType(Claim.ClaimType.HOME);
                claim2.setStatus(Claim.ClaimStatus.UNDER_REVIEW);
                claim2.setClaimantName("Jane Smith");
                claim2.setClaimantEmail("jane.smith@example.com");
                claim2.setClaimantPhone("+1987654321");
                claim2.setAssignedAdjuster("Mike Johnson");
                
                Claim claim3 = new Claim();
                claim3.setId(UUID.randomUUID());
                claim3.setClaimNumber("CLM-2023-003");
                claim3.setPolicyNumber("POL-003");
                claim3.setIncidentDate(LocalDate.now().minusDays(15));
                claim3.setDescription("Medical expenses for emergency surgery.");
                claim3.setEstimatedAmount(new BigDecimal("12000.00"));
                claim3.setType(Claim.ClaimType.HEALTH);
                claim3.setStatus(Claim.ClaimStatus.APPROVED);
                claim3.setClaimantName("Robert Brown");
                claim3.setClaimantEmail("robert.brown@example.com");
                claim3.setClaimantPhone("+1567890123");
                claim3.setAssignedAdjuster("Sarah Wilson");
                
                // Save to repository
                claimRepository.save(claim1);
                claimRepository.save(claim2);
                claimRepository.save(claim3);
                
                log.info("Created {} sample claims", claimRepository.count());
            }
        };
    }
}
