package com.zurich.poc.model;

import com.zurich.poc.model.Claim.ClaimStatus;
import com.zurich.poc.model.Claim.ClaimType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaimDTO {
    
    private UUID id;
    
    @NotBlank(message = "Claim number is required")
    private String claimNumber;
    
    @NotBlank(message = "Policy number is required")
    private String policyNumber;
    
    @NotNull(message = "Incident date is required")
    @PastOrPresent(message = "Incident date cannot be in the future")
    private LocalDate incidentDate;
    
    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters")
    private String description;
    
    @PositiveOrZero(message = "Estimated amount must be positive or zero")
    private BigDecimal estimatedAmount;
    
    @NotNull(message = "Claim type is required")
    private ClaimType type;
    
    private ClaimStatus status = ClaimStatus.SUBMITTED;
    
    @NotBlank(message = "Claimant name is required")
    private String claimantName;
    
    @Email(message = "Email must be valid")
    private String claimantEmail;
    
    @Pattern(regexp = "^\\+?[0-9\\s-]{10,15}$", message = "Phone number must be valid")
    private String claimantPhone;
    
    private String additionalDetails;
    
    private String assignedAdjuster;
    
    private String jiraIssueKey;
}
