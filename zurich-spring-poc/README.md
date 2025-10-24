# Zurich Insurance Spring Boot POC

This project is a Spring Boot application that operates within the insurance domain, providing claims management capabilities. It's designed to work alongside a MuleSoft application (which handles policy management) as part of a Proof of Concept.

## Architecture

This project demonstrates a microservices architecture where:

1. **Spring Boot Application (This project)** - Handles claims management
2. **MuleSoft Application** - Handles policy management and lookup

Both applications integrate with Jira to:
- Fetch technical requirements directly from Jira user stories
- Perform corresponding tasks in either the Java or MuleSoft application based on the story context

## Key Features

- **Claims Management**: Create, retrieve, update, and delete insurance claims
- **Policy Validation**: Verify policy existence by calling the MuleSoft API
- **Jira Integration**: Bidirectional integration with Jira for requirements management
- **Task Routing**: Automatic determination of whether Spring Boot or MuleSoft should handle a requirement

## Technical Stack

- **Framework**: Spring Boot 3.2.2
- **Java Version**: 17
- **Database**: H2 Database (in-memory for POC)
- **API Documentation**: SpringDoc OpenAPI
- **Authentication**: JWT-based security
- **Build Tool**: Maven

## API Endpoints

### Claims API

- `GET /api/claims` - Get all claims
- `GET /api/claims/{id}` - Get claim by ID
- `POST /api/claims` - Create a new claim
- `PUT /api/claims/{id}` - Update a claim
- `DELETE /api/claims/{id}` - Delete a claim
- `GET /api/claims/policy/{policyNumber}` - Get claims by policy number
- `GET /api/claims/status/{status}` - Get claims by status
- `GET /api/claims/type/{type}` - Get claims by type

### Jira Integration API

- `GET /api/jira/issue/{issueKey}` - Get Jira issue details
- `POST /api/jira/claim/{claimId}/create-issue` - Create a Jira issue for a claim
- `POST /api/jira/issue/{issueKey}/comment` - Add a comment to a Jira issue
- `POST /api/jira/claim/{claimId}/update-issue` - Update Jira issue for a claim

## Setup and Configuration

### Prerequisites

- Java 17+
- Maven
- Jira account with API token

### Configuration

Key configuration in `application.yml`:

```yaml
jira:
  base-url: https://your-jira-instance.atlassian.net
  email: your-jira-email@example.com
  api-token: your-api-token
  project-key: GenAI

policy-service:
  url: http://localhost:8091/poc
  username: user
  password: password
```

Alternatively, the application can read configuration from `mcp.json` in the VS Code user directory.

### Building and Running

```bash
# Build
mvn clean install

# Run
mvn spring-boot:run
```

The application will start on http://localhost:8092/api

## Integration with Jira and MuleSoft

### Jira Integration

The application automatically polls Jira every 5 minutes to fetch requirements from user stories that contain a "Technical Requirements" section.

Requirements are parsed and routed to either this Spring Boot application or marked for the MuleSoft application based on content analysis.

### MuleSoft Integration

This application calls the MuleSoft API to validate policies and retrieve policy details before creating or updating claims.

## Testing

The project includes unit tests for repositories, services, and controllers. Run tests with:

```bash
mvn test
```
