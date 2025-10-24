# Zurich Mule POC - Policy Management API

## Overview
This is a Mule 4.8 application that provides REST endpoints for policy management operations. The application is fully compatible with Anypoint Studio and includes comprehensive error handling, security configurations, and test coverage.

## New Endpoint Added

### GET /poc/policy/details/{policyId}
Retrieves detailed information for a specific policy by its ID.

**Request:**
- Method: GET
- Path: `/poc/policy/details/{policyId}`
- URI Parameter: `policyId` (required) - The unique identifier for the policy

**Response:**
- **200 OK**: Returns policy details in JSON format
- **400 Bad Request**: When policy ID is missing or invalid
- **404 Not Found**: When policy is not found
- **500 Internal Server Error**: For system errors

**Example Request:**
```
GET https://localhost:8091/poc/policy/details/POL123456
```

**Example Response:**
```json
{
  "policyId": "POL123456",
  "policyNumber": "ZUR-2024-001",
  "emailId": "customer@example.com",
  "status": "ACTIVE",
  "createdDate": "2024-01-15",
  "premiumAmount": 1200.00,
  "coverageDetails": {
    "type": "AUTO",
    "coverage": "COMPREHENSIVE"
  },
  "lastModified": "2024-10-10T10:30:45Z"
}
```

## Existing Endpoint Enhanced

### GET /poc/policy/list
Enhanced to include proper query parameter handling and improved response structure.

**Query Parameters:**
- `emailId` (optional): Filter policies by email ID
- `pageNo` (optional, default: 1): Page number for pagination  
- `pageSize` (optional, default: 10): Number of items per page

## Features Added

### 1. Security Enhancements
- Removed insecure TLS configuration (`insecure="true"`)
- Added proper TLS context with keystore and truststore configurations
- Implemented secure properties for sensitive data
- Added proper authentication handling

### 2. Error Handling
- Comprehensive error handling with appropriate HTTP status codes
- Structured error responses with meaningful messages
- Proper logging for debugging and monitoring

### 3. Input Validation
- URI parameter validation for policy ID
- Query parameter handling with defaults
- Proper error responses for invalid inputs

### 4. Anypoint Studio Compatibility
- Updated `mule-artifact.json` with proper configuration
- Added secure properties declaration
- Structured project layout following Mule best practices
- Added comprehensive MUnit tests

### 5. Configuration Management
- Environment-specific configuration files
- Proper property externalization
- TLS configuration for secure communications

## Prerequisites

1. **Anypoint Studio 7.16+** with Mule Runtime 4.8.x
2. **Java 8 or 11**
3. **Maven 3.6+**
4. **TLS Certificates** (for HTTPS endpoints)

## Setup Instructions

### 1. Import Project in Anypoint Studio
1. Open Anypoint Studio
2. File → Import → Anypoint Studio → Anypoint Studio project from File System
3. Select the project root folder
4. Click "Finish"

### 2. Configure TLS Certificates
1. Create `src/main/resources/keystore` directory
2. Add your keystore and truststore files:
   - `zurich-poc-keystore.jks`
   - `zurich-poc-truststore.jks`
   - `outbound-truststore.jks`
3. Update passwords in `src/main/resources/config/local/local.yaml`

### 3. Configure Secure Properties (Recommended)
1. Install Secure Configuration Properties module
2. Create `secure-local.yaml` with encrypted values
3. Update `globals.xml` to use secure properties configuration

### 4. Run the Application
1. Right-click project in Package Explorer
2. Select "Run As" → "Mule Application (configure)"
3. Ensure JRE version is Java 8 or 11
4. Click "Run"

### 5. Test the Endpoints
The application will start on `https://localhost:8091`

**Test Policy List:**
```bash
curl -k "https://localhost:8091/poc/policy/list?emailId=test@example.com&pageNo=1&pageSize=5"
```

**Test Policy Details:**
```bash
curl -k "https://localhost:8091/poc/policy/details/POL123456"
```

## Project Structure

```
zurich-mule-poc/
├── src/main/
│   ├── mule/
│   │   ├── common/
│   │   │   ├── globals.xml          # Global configurations
│   │   │   └── tls-config.xml       # TLS security configurations
│   │   ├── implementation/
│   │   │   ├── get-policy-list-impl.xml     # Policy list logic
│   │   │   └── get-policy-details-impl.xml  # Policy details logic (NEW)
│   │   ├── system/
│   │   │   └── microservice-calls.xml       # External service calls
│   │   └── zurich-mule-poc.xml      # Main API flows
│   └── resources/
│       ├── api/
│       │   └── zurich-mule-poc.raml # API specification (UPDATED)
│       └── config/local/
│           └── local.yaml           # Configuration properties
└── src/test/munit/
    └── policy-details-endpoint-test.xml # Unit tests (NEW)
```

## Configuration Properties

Key properties in `local.yaml`:

```yaml
# Microservice endpoints
ms.policy.list.path: "/v1/policy/list"
ms.policy.details.path: "/v1/policy/details/{policyId}"

# API configurations
api.ms.host: "your-api-host.com"
api.ms.port: "443"
api.ms.protocol: "HTTPS"

# TLS configurations (update with your certificate paths)
tls.keystore.path: "keystore/zurich-poc-keystore.jks"
tls.truststore.path: "keystore/zurich-poc-truststore.jks"
```

## Testing

### Unit Tests
Run MUnit tests in Anypoint Studio:
1. Right-click on `src/test/munit` folder
2. Select "Run MUnit Tests"

### Integration Testing
1. Start the application
2. Use Postman, curl, or any REST client
3. Test both endpoints with various scenarios

## Deployment

### CloudHub Deployment
1. Right-click project → Anypoint Platform → Deploy to CloudHub
2. Configure environment-specific properties
3. Ensure certificates are properly configured

### On-Premises Deployment
1. Package application: `mvn clean package`
2. Deploy generated JAR to Mule Runtime
3. Configure environment properties

## Security Considerations

1. **Never use `insecure="true"` in production**
2. **Always encrypt sensitive properties**
3. **Use proper TLS certificates from trusted CAs**
4. **Implement proper authentication and authorization**
5. **Regular security scanning and updates**

## Monitoring and Logging

The application includes comprehensive logging at key points:
- Request/response logging
- Error logging with context
- Performance metrics logging

Log categories follow the pattern: `com.zurich.poc.*`

## Troubleshooting

### Common Issues:

1. **TLS Certificate Issues**
   - Verify certificate paths and passwords
   - Check certificate validity and trust chains

2. **Studio Import Issues**
   - Ensure correct Mule runtime version
   - Verify Maven dependencies are resolved

3. **Runtime Errors**
   - Check log files for detailed error messages
   - Verify all configuration properties are set

## Support

For technical support or questions about this implementation, please refer to:
- Mule 4 Documentation
- Anypoint Studio User Guide
- Internal development team documentation