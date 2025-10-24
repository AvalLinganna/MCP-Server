package com.zurich.poc.controller;

import com.zurich.poc.exception.ApiResponse;
import com.zurich.poc.exception.ResourceNotFoundException;
import com.zurich.poc.model.Claim;
import com.zurich.poc.model.ClaimDTO;
import com.zurich.poc.model.PolicySummary;
import com.zurich.poc.service.ClaimService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/claims")
@RequiredArgsConstructor
@Slf4j
public class ClaimController {
    
    private final ClaimService claimService;
    
    @PostMapping
    public ResponseEntity<ApiResponse<Claim>> createClaim(@Valid @RequestBody ClaimDTO claimDTO) {
        log.info("Request received to create claim for policy: {}", claimDTO.getPolicyNumber());
        
        // Validate policy exists by calling MuleSoft service
        boolean policyValid = claimService.validatePolicy(claimDTO.getPolicyNumber(), claimDTO.getClaimantEmail());
        if (!policyValid) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(HttpStatus.BAD_REQUEST, "Invalid policy number or email"));
        }
        
        Claim createdClaim = claimService.createClaim(claimDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(createdClaim, "Claim created successfully"));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Claim>> getClaimById(@PathVariable UUID id) {
        log.info("Request received to get claim by ID: {}", id);
        
        return claimService.getClaimById(id)
                .map(claim -> ResponseEntity.ok(ApiResponse.success(claim, "Claim retrieved successfully")))
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found with id: " + id));
    }
    
    @GetMapping("/number/{claimNumber}")
    public ResponseEntity<ApiResponse<Claim>> getClaimByNumber(@PathVariable String claimNumber) {
        log.info("Request received to get claim by number: {}", claimNumber);
        
        return claimService.getClaimByClaimNumber(claimNumber)
                .map(claim -> ResponseEntity.ok(ApiResponse.success(claim, "Claim retrieved successfully")))
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found with number: " + claimNumber));
    }
    
    @GetMapping("/policy/{policyNumber}")
    public ResponseEntity<ApiResponse<List<Claim>>> getClaimsByPolicyNumber(@PathVariable String policyNumber) {
        log.info("Request received to get claims by policy number: {}", policyNumber);
        
        List<Claim> claims = claimService.getClaimsByPolicyNumber(policyNumber);
        return ResponseEntity.ok(ApiResponse.success(claims, "Claims retrieved successfully"));
    }
    
    @GetMapping("/policy/{policyNumber}/pageable")
    public ResponseEntity<ApiResponse<Page<Claim>>> getClaimsByPolicyNumberPageable(
            @PathVariable String policyNumber,
            @PageableDefault(size = 10) Pageable pageable) {
        log.info("Request received to get paginated claims by policy number: {}", policyNumber);
        
        Page<Claim> claims = claimService.getClaimsByPolicyNumber(policyNumber, pageable);
        return ResponseEntity.ok(ApiResponse.success(claims, "Claims retrieved successfully"));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Claim>> updateClaim(
            @PathVariable UUID id, 
            @Valid @RequestBody ClaimDTO claimDTO) {
        log.info("Request received to update claim with ID: {}", id);
        
        Claim updatedClaim = claimService.updateClaim(id, claimDTO);
        return ResponseEntity.ok(ApiResponse.success(updatedClaim, "Claim updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteClaim(@PathVariable UUID id) {
        log.info("Request received to delete claim with ID: {}", id);
        
        claimService.deleteClaim(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Claim deleted successfully"));
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<Claim>>> getAllClaims() {
        log.info("Request received to get all claims");
        
        List<Claim> claims = claimService.getAllClaims();
        return ResponseEntity.ok(ApiResponse.success(claims, "Claims retrieved successfully"));
    }
    
    @GetMapping("/pageable")
    public ResponseEntity<ApiResponse<Page<Claim>>> getAllClaimsPageable(@PageableDefault(size = 10) Pageable pageable) {
        log.info("Request received to get all claims with pagination");
        
        Page<Claim> claims = claimService.getAllClaims(pageable);
        return ResponseEntity.ok(ApiResponse.success(claims, "Claims retrieved successfully"));
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<Claim>>> getClaimsByStatus(@PathVariable Claim.ClaimStatus status) {
        log.info("Request received to get claims by status: {}", status);
        
        List<Claim> claims = claimService.getClaimsByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(claims, "Claims retrieved successfully"));
    }
    
    @GetMapping("/type/{type}")
    public ResponseEntity<ApiResponse<List<Claim>>> getClaimsByType(@PathVariable Claim.ClaimType type) {
        log.info("Request received to get claims by type: {}", type);
        
        List<Claim> claims = claimService.getClaimsByType(type);
        return ResponseEntity.ok(ApiResponse.success(claims, "Claims retrieved successfully"));
    }
    
    @GetMapping("/policy-details/{policyNumber}")
    public ResponseEntity<ApiResponse<PolicySummary>> getPolicyDetails(
            @PathVariable String policyNumber,
            @RequestParam String email) {
        log.info("Request received to get policy details for policy number: {}", policyNumber);
        
        return claimService.getPolicyDetails(policyNumber, email)
                .map(policy -> ResponseEntity.ok(ApiResponse.success(policy, "Policy details retrieved successfully")))
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found with number: " + policyNumber));
    }
    
    // Jira integration will be added later
    /*
    @PostMapping("/{id}/jira/{jiraIssueKey}")
    public ResponseEntity<ApiResponse<Claim>> linkClaimToJiraIssue(
            @PathVariable UUID id,
            @PathVariable String jiraIssueKey) {
        log.info("Request received to link claim ID: {} to Jira issue key: {}", id, jiraIssueKey);
        
        Claim updatedClaim = claimService.linkClaimToJiraIssue(id, jiraIssueKey);
        return ResponseEntity.ok(ApiResponse.success(updatedClaim, "Claim linked to Jira issue successfully"));
    }
    
    @GetMapping("/jira/{jiraIssueKey}")
    public ResponseEntity<ApiResponse<Claim>> getClaimByJiraIssueKey(@PathVariable String jiraIssueKey) {
        log.info("Request received to get claim by Jira issue key: {}", jiraIssueKey);
        
        return claimService.getClaimByJiraIssueKey(jiraIssueKey)
                .map(claim -> ResponseEntity.ok(ApiResponse.success(claim, "Claim retrieved successfully")))
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found with Jira issue key: " + jiraIssueKey));
    }
    */
}
