# Integration Architecture: Spring Boot and MuleSoft

This document describes the integration architecture between the Spring Boot Claims Management application and the MuleSoft Policy Management application.

## Overview

The architecture consists of two main components:

1. **Spring Boot Claims Service** - Manages insurance claims processing
2. **MuleSoft Policy Service** - Manages insurance policies and policy-related operations

Both applications integrate with Jira for requirements management and task assignment.

## Communication Flow

### Spring Boot to MuleSoft

The Spring Boot application calls the MuleSoft API in the following scenarios:

1. **Policy Validation** - When a new claim is submitted, the Spring Boot app validates that the policy exists and is active by calling the MuleSoft API
2. **Policy Details Retrieval** - To get policy details for display or for claim processing

```
[Spring Boot Claims Service] --HTTP--> [MuleSoft Policy Service]
  GET /policy/list?emailId={email}
```

### Requirements Routing

Both applications connect to Jira to fetch technical requirements from user stories. The JiraRequirementsListener component in each application determines whether a requirement should be handled by:

1. Spring Boot Claims Service
2. MuleSoft Policy Service

This determination is made based on:
- Explicit prefixes in requirements (e.g., "Spring Boot:" or "MuleSoft:")
- Keyword analysis for domain-specific terms

```
[Jira] <--Poll-- [Spring Boot JiraRequirementsListener]
[Jira] <--Poll-- [MuleSoft JiraRequirementsListener]
```

## Authentication

### Spring Boot to MuleSoft
- Basic authentication using configured username/password

### Applications to Jira
- Basic authentication using email/API token
- Configuration stored in application.yml or mcp.json

## Error Handling

The integration includes the following error handling mechanisms:

1. **Connection Timeout** - 10 seconds for connection, 30 seconds for read
2. **Retry Mechanism** - Not implemented in POC but recommended for production
3. **Circuit Breaker** - Not implemented in POC but recommended for production
4. **Error Logging** - All integration errors are logged with detailed information

## Data Model Mapping

### Policy Data from MuleSoft to Spring Boot

```
MuleSoft Response:
{
  "totalNumberofPolicies": 1,
  "policies": [
    {
      "policyNumber": "POL-001",
      "policyHolderName": "John Doe",
      "email": "john.doe@example.com",
      "policyType": "Auto",
      "startDate": "2023-01-01",
      "endDate": "2024-01-01",
      "status": "ACTIVE"
    }
  ]
}

Maps to Spring Boot Model:
PolicySummary {
    policyNumber: String
    policyHolderName: String
    email: String
    policyType: String
    startDate: LocalDate
    endDate: LocalDate
    status: String
}
```

## Setup Requirements

1. Both applications must be configured with correct endpoints
2. Jira project key must be the same in both applications
3. Applications should use the same Jira credentials or have appropriate permissions

## Monitoring and Troubleshooting

The integration includes logging at various levels:

- INFO: Standard operation logging
- DEBUG: Detailed request/response information
- WARN: Potential issues or degraded service
- ERROR: Failed operations

## Future Enhancements

1. **Implement OAuth2** - Replace Basic Auth with OAuth2
2. **Circuit Breaker** - Add circuit breaker for fault tolerance
3. **API Gateway** - Add an API gateway for routing and security
4. **Unified Logging** - Implement ELK stack for unified logging
5. **API Contract Testing** - Implement contract tests between the services
