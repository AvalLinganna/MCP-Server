package com.zurich.poc.repository;

import com.zurich.poc.model.Claim;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, UUID> {
    
    Optional<Claim> findByClaimNumber(String claimNumber);
    
    List<Claim> findByPolicyNumber(String policyNumber);
    
    Page<Claim> findByPolicyNumber(String policyNumber, Pageable pageable);
    
    List<Claim> findByClaimantEmail(String claimantEmail);
    
    List<Claim> findByStatus(Claim.ClaimStatus status);
    
    List<Claim> findByType(Claim.ClaimType type);
    
    // Jira integration will be added later
    // Optional<Claim> findByJiraIssueKey(String jiraIssueKey);
}
