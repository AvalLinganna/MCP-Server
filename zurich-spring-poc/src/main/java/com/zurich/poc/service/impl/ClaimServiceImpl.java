package com.zurich.poc.service.impl;

import com.zurich.poc.exception.ResourceNotFoundException;
import com.zurich.poc.model.Claim;
import com.zurich.poc.model.ClaimDTO;
import com.zurich.poc.model.PolicySummary;
import com.zurich.poc.repository.ClaimRepository;
import com.zurich.poc.service.ClaimService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClaimServiceImpl implements ClaimService {

    private final ClaimRepository claimRepository;
    private final RestTemplate restTemplate;

    @Value("${policy-service.url}")
    private String policyServiceUrl;

    @Value("${policy-service.username}")
    private String policyServiceUsername;

    @Value("${policy-service.password}")
    private String policyServicePassword;

    @Override
    public Claim createClaim(ClaimDTO claimDTO) {
        log.info("Creating new claim with policy number: {}", claimDTO.getPolicyNumber());
        
        Claim claim = new Claim();
        BeanUtils.copyProperties(claimDTO, claim);
        
        // Set default status if not provided
        if (claim.getStatus() == null) {
            claim.setStatus(Claim.ClaimStatus.SUBMITTED);
        }
        
        return claimRepository.save(claim);
    }

    @Override
    public Claim updateClaim(UUID id, ClaimDTO claimDTO) {
        log.info("Updating claim with ID: {}", id);
        
        Claim existingClaim = claimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found with id: " + id));
        
        // Update fields from DTO
        BeanUtils.copyProperties(claimDTO, existingClaim, "id", "createdAt");
        
        return claimRepository.save(existingClaim);
    }

    @Override
    public Optional<Claim> getClaimById(UUID id) {
        log.debug("Fetching claim with ID: {}", id);
        return claimRepository.findById(id);
    }

    @Override
    public Optional<Claim> getClaimByClaimNumber(String claimNumber) {
        log.debug("Fetching claim with number: {}", claimNumber);
        return claimRepository.findByClaimNumber(claimNumber);
    }

    @Override
    public List<Claim> getClaimsByPolicyNumber(String policyNumber) {
        log.debug("Fetching claims for policy number: {}", policyNumber);
        return claimRepository.findByPolicyNumber(policyNumber);
    }

    @Override
    public Page<Claim> getClaimsByPolicyNumber(String policyNumber, Pageable pageable) {
        log.debug("Fetching paginated claims for policy number: {}", policyNumber);
        return claimRepository.findByPolicyNumber(policyNumber, pageable);
    }

    @Override
    public void deleteClaim(UUID id) {
        log.info("Deleting claim with ID: {}", id);
        claimRepository.deleteById(id);
    }

    @Override
    public List<Claim> getAllClaims() {
        log.debug("Fetching all claims");
        return claimRepository.findAll();
    }

    @Override
    public Page<Claim> getAllClaims(Pageable pageable) {
        log.debug("Fetching all claims with pagination");
        return claimRepository.findAll(pageable);
    }

    @Override
    public List<Claim> getClaimsByStatus(Claim.ClaimStatus status) {
        log.debug("Fetching claims with status: {}", status);
        return claimRepository.findByStatus(status);
    }

    @Override
    public List<Claim> getClaimsByType(Claim.ClaimType type) {
        log.debug("Fetching claims of type: {}", type);
        return claimRepository.findByType(type);
    }

    // Jira integration will be added later
    /*
    @Override
    public Optional<Claim> getClaimByJiraIssueKey(String jiraIssueKey) {
        log.debug("Fetching claim by Jira issue key: {}", jiraIssueKey);
        return claimRepository.findByJiraIssueKey(jiraIssueKey);
    }
    */

    @Override
    public boolean validatePolicy(String policyNumber, String email) {
        log.info("Validating policy number: {} for email: {}", policyNumber, email);
        
        try {
            // Call MuleSoft service to validate policy
            String url = policyServiceUrl + "/policy/list?emailId=" + email;
            
            HttpHeaders headers = new HttpHeaders();
            String auth = policyServiceUsername + ":" + policyServicePassword;
            String encodedAuth = java.util.Base64.getEncoder().encodeToString(auth.getBytes());
            headers.set("Authorization", "Basic " + encodedAuth);
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<PolicyListResponse> response = restTemplate.exchange(
                    url, 
                    HttpMethod.GET, 
                    entity, 
                    PolicyListResponse.class);
            
            PolicyListResponse body = response.getBody();
            if (body != null && body.getPolicies() != null) {
                // Check if the policy number exists in the list
                return body.getPolicies().stream()
                        .anyMatch(policy -> policyNumber.equals(policy.getPolicyNumber()));
            }
            
            return false;
        } catch (Exception e) {
            log.error("Error validating policy: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Optional<PolicySummary> getPolicyDetails(String policyNumber, String email) {
        log.info("Fetching policy details for policy number: {} and email: {}", policyNumber, email);
        
        try {
            // Call MuleSoft service to get policy details
            String url = policyServiceUrl + "/policy/list?emailId=" + email;
            
            HttpHeaders headers = new HttpHeaders();
            String auth = policyServiceUsername + ":" + policyServicePassword;
            String encodedAuth = java.util.Base64.getEncoder().encodeToString(auth.getBytes());
            headers.set("Authorization", "Basic " + encodedAuth);
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<PolicyListResponse> response = restTemplate.exchange(
                    url, 
                    HttpMethod.GET, 
                    entity, 
                    PolicyListResponse.class);

            PolicyListResponse responseBody = response.getBody();
            if (responseBody != null && responseBody.getPolicies() != null) {
                // Find the policy with matching policy number
                return responseBody.getPolicies().stream()
                        .filter(policy -> policyNumber.equals(policy.getPolicyNumber()))
                        .findFirst();
            }

            return Optional.empty();
        } catch (Exception e) {
            log.error("Error fetching policy details: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    // Jira integration will be added later
    /*
    @Override
    public Claim linkClaimToJiraIssue(UUID claimId, String jiraIssueKey) {
        log.info("Linking claim ID: {} to Jira issue key: {}", claimId, jiraIssueKey);
        
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found with id: " + claimId));
        
        claim.setJiraIssueKey(jiraIssueKey);
        return claimRepository.save(claim);
    }
    */
    
    // Inner class for policy list response
    public static class PolicyListResponse {
        private int totalNumberofPolicies;
        private List<PolicySummary> policies;
        
        public int getTotalNumberofPolicies() {
            return totalNumberofPolicies;
        }
        
        public void setTotalNumberofPolicies(int totalNumberofPolicies) {
            this.totalNumberofPolicies = totalNumberofPolicies;
        }
        
        public List<PolicySummary> getPolicies() {
            return policies;
        }
        
        public void setPolicies(List<PolicySummary> policies) {
            this.policies = policies;
        }
    }
}
