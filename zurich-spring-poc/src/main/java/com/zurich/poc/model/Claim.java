package com.zurich.poc.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "insurance_claims")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Claim {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false)
    private String claimNumber;
    
    @Column(nullable = false)
    private String policyNumber;
    
    @Column(nullable = false)
    private LocalDate incidentDate;
    
    @Column(nullable = false)
    private String description;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal estimatedAmount;
    
    @Enumerated(EnumType.STRING)
    private ClaimType type;
    
    @Enumerated(EnumType.STRING)
    private ClaimStatus status;
    
    @Column(nullable = false)
    private String claimantName;
    
    private String claimantEmail;
    
    private String claimantPhone;
    
    @Column(length = 5000)
    private String additionalDetails;
    
    private String assignedAdjuster;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
  
    
    public enum ClaimType {
        AUTO, 
        HOME, 
        HEALTH, 
        LIFE, 
        TRAVEL, 
        LIABILITY,
        BUSINESS,
        PROPERTY,
        OTHER
    }
    
    public enum ClaimStatus {
        SUBMITTED, 
        UNDER_REVIEW, 
        PENDING_DOCUMENTS, 
        APPROVED, 
        PARTIAL_APPROVED,
        REJECTED, 
        CLOSED,
        APPEALED
    }
}
