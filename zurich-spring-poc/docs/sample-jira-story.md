# Sample Jira User Story Format

This document provides an example of how to format Jira user stories to integrate with the Spring Boot and MuleSoft applications.

## Example User Story

**Summary:** As a customer, I want to submit a claim for my auto insurance policy

**Description:**
```
*User Story*
As a customer, I want to submit a claim for my auto insurance policy, so that I can be reimbursed for damages from an accident.

*Acceptance Criteria*
- User can submit a claim with policy number, incident date, and description
- System validates that the policy exists and is active
- User receives a confirmation with claim number
- Claim is routed to an adjuster for review
- User can check the status of their claim

*Technical Requirements*
- Spring Boot: Create REST API endpoint for claim submission with validation
- Spring Boot: Add policy validation by calling policy service API
- MuleSoft: Expose policy validation endpoint to verify policy status
- Spring Boot: Implement claim number generation logic
- Spring Boot: Store claim details in the database
- MuleSoft: Send policy details to claims service when requested

*Additional Information*
Priority: High
Story Points: 5
```

## How Technical Requirements Are Processed

The application automatically detects requirements prefixed with either "Spring Boot:" or "MuleSoft:" in the Technical Requirements section. 

The system processes these requirements as follows:

1. Requirements starting with "Spring Boot:" are processed by the Spring Boot claims application
2. Requirements starting with "MuleSoft:" are logged and need to be handled by the MuleSoft policy application

If neither prefix is found, the system uses keyword analysis to determine the appropriate application:

- Spring Boot keywords: spring, java, rest api, claim, jpa, database
- MuleSoft keywords: mule, anypoint, dataweave, policy, integration, flow

## Best Practices

1. Always separate requirements with bullet points (using the "-" character)
2. Be explicit about which application should handle each requirement
3. Keep technical requirements specific and actionable
4. Include acceptance criteria that can be used for testing
