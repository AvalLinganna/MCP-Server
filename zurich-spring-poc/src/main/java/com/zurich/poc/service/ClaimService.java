package com.zurich.poc.service;

import com.zurich.poc.model.Claim;
import com.zurich.poc.model.ClaimDTO;
import com.zurich.poc.model.PolicySummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClaimService {
    
    Claim createClaim(ClaimDTO claimDTO);
    
    Claim updateClaim(UUID id, ClaimDTO claimDTO);
    
    Optional<Claim> getClaimById(UUID id);
    
    Optional<Claim> getClaimByClaimNumber(String claimNumber);
    
    List<Claim> getClaimsByPolicyNumber(String policyNumber);
    
    Page<Claim> getClaimsByPolicyNumber(String policyNumber, Pageable pageable);
    
    void deleteClaim(UUID id);
    
    List<Claim> getAllClaims();
    
    Page<Claim> getAllClaims(Pageable pageable);
    
    List<Claim> getClaimsByStatus(Claim.ClaimStatus status);
    
    List<Claim> getClaimsByType(Claim.ClaimType type);
    
    // Method to validate policy exists by calling the MuleSoft service
    boolean validatePolicy(String policyNumber, String email);
    
    // Method to get policy details from MuleSoft service
    Optional<PolicySummary> getPolicyDetails(String policyNumber, String email);
    
}
