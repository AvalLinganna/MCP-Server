# Zurich Spring POC Development Automation Prompt

## Task Overview:
You have access to a configured MCP server (`Atlassian-mcp-server`) that connects to Jira and Confluence with the following configuration:
- **Jira Instance**: https://3012sakshi.atlassian.net 
- **Project**: KAN (test_mcp)
- **Confluence Space**: MCPPoCZuri (MCP_PoC_Zurich)

Automate the following development workflow for the Zurich Spring POC application:

1. Fetch Jira story details and technical requirements
2. Fetch Design document linked to Jira story from Confluence  
3. Apply code changes to the Spring Boot application
4. Build, test, and validate changes
5. Report results and any issues
6. Generate Business Requirements Document (BRD)
7. Generate Technical Document (TD)
8. Upload documents to JIRA ticket

## Available MCP Tools:

### Jira Tools:
- `mcp_atlassian-mcp_get_jira_issue` - Fetch specific issue details by key (e.g., KAN-X)
- `mcp_atlassian-mcp_get_jira_issues` - List project issues with JQL filtering
- `mcp_atlassian-mcp_create_jira_issue` - Create new issues
- `mcp_atlassian-mcp_get_user_stories` - Get user story formatted issues
- `mcp_atlassian-mcp_get_project_info` - Get project metadata
- `mcp_atlassian-mcp_update_jira_issue` - Update issue status/comments

### Confluence Tools:
- `mcp_atlassian-mcp_get_confluence_page` - Get specific page by ID or title  
- `mcp_atlassian-mcp_get_confluence_pages` - List pages in space
- `mcp_atlassian-mcp_search_across_spaces` - Search content across spaces
- `mcp_atlassian-mcp_get_specific_confluence_page` - Get specific pages like "Technical Specifications"

## Workflow Steps:

### 1. Issue Analysis
**Input**: Jira story number (format: KAN-X)

**Actions**:
```
1. Use `mcp_atlassian-mcp_get_jira_issue` with issueKey="KAN-X"
2. Extract key information:
   - Story title and description
   - Acceptance criteria
   - Technical requirements (in description/comments)
   - Linked issues or dependencies
   - Labels, components, and story points
   - Assignee and current status
```

**Extract Data Points**:
- User story format: "As a... I want... So that..."
- Technical specifications and constraints  
- Definition of Done criteria
- Dependencies on other stories
- API endpoints to implement/modify

### 2. Confluence Documentation Analysis
**Actions**:
```
1. Search for related design documents:
   - Use `mcp_atlassian-mcp_search_across_spaces` with query="KAN-X" or story title
   - Use `mcp_atlassian-mcp_get_specific_confluence_page` for "Technical Specifications"
   
2. If documents found, extract:
   - Architecture diagrams
   - API specifications  
   - Database schema changes
   - Integration requirements
   - Security considerations
```

### 3. Code Analysis & Changes

Based on Jira story requirements, Confluence documentation, and Swagger specifications, implement API changes across all three projects:

#### A. Target Project: `zurich-spring-poc` (Maven Spring Boot 3.3.0 with Java 21)

**API Development Workflow**:
1. **Extract Swagger/OpenAPI Specification** from Confluence or Jira attachments
2. **Analyze existing API patterns** and code structure
3. **Generate/Update API components** following Spring Boot best practices
4. **Implement business logic** based on Confluence code snippets
5. **Create/Update tests** for API endpoints

**Project Structure**:
```
src/main/java/com/zurich/poc/
├── controller/          # REST controllers with OpenAPI annotations
├── service/            # Business logic services  
├── service/impl/       # Service implementations
├── model/              # Entities and DTOs with validation
├── repository/         # JPA repositories with custom queries
├── config/            # OpenAPI/Swagger configuration
└── exception/         # Custom exception handling
```

**Spring Boot API Implementation Steps**:

**Step 1: OpenAPI Configuration**
```java
// Create OpenAPIConfig.java in config package
@Configuration
@OpenAPIDefinition(
    info = @Info(title = "Zurich Insurance API", version = "1.0"),
    servers = @Server(url = "/", description = "Default Server URL")
)
public class OpenAPIConfig {
    // Swagger UI configuration
}
```

**Step 2: Entity and DTO Creation**
```java
// Create entity classes with JPA annotations
@Entity
@Table(name = "endorsements")
public class Endorsement {
    // Fields with validation annotations
    // JPA relationships
    // Audit fields
}

// Create DTO classes with validation
@Schema(description = "Endorsement request/response DTO")
public class EndorsementDTO {
    // API fields with OpenAPI annotations
    // Validation constraints
}
```

**Step 3: Repository Layer**
```java
// Create repository interfaces extending JpaRepository
@Repository
public interface EndorsementRepository extends JpaRepository<Endorsement, Long> {
    // Custom query methods
    // Named queries
    // Native queries if needed
}
```

**Step 4: Service Layer Implementation**
```java
// Implement service interfaces with business logic from Confluence
@Service
@Transactional
public class EndorsementServiceImpl implements EndorsementService {
    // Business logic implementation
    // Integration with external services
    // Data validation and transformation
}
```

**Step 5: Controller Implementation**
```java
// Create REST controllers with OpenAPI documentation
@RestController
@RequestMapping("/v1/policy/endorsement")
@Tag(name = "Endorsement", description = "Policy Endorsement API")
public class EndorsementController {
    
    @PostMapping
    @Operation(summary = "Create new endorsement")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Endorsement created"),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<EndorsementDTO> createEndorsement(@Valid @RequestBody EndorsementDTO request) {
        // Implementation based on Swagger spec
    }
}
```

#### B. Target Project: `zurich-mule-poc` (MuleSoft Mule 4.8 - Anypoint Studio Compatible)

**MuleSoft API Development Workflow**:
1. **Import/Update RAML specification** from Swagger/OpenAPI
2. **Generate APIKit flows** using Anypoint Studio
3. **Implement business logic flows** from Confluence code snippets
4. **Configure data transformations** with DataWeave
5. **Add error handling** and security configurations

**Project Structure** (Anypoint Studio Compatible):
```
zurich-mule-poc/
├── src/main/
│   ├── mule/
│   │   ├── common/
│   │   │   ├── globals.xml          # Global configurations & properties
│   │   │   └── tls-config.xml       # TLS/SSL security configurations
│   │   ├── implementation/
│   │   │   ├── endorsement-impl.xml     # Endorsement business logic flows
│   │   │   ├── policy-list-impl.xml     # Policy list business logic
│   │   │   └── policy-details-impl.xml  # Policy details business logic
│   │   ├── system/
│   │   │   └── microservice-calls.xml   # External service integrations
│   │   └── zurich-mule-poc.xml      # Main API flows & routing
│   └── resources/
│       ├── api/
│       │   └── zurich-mule-poc.raml # RAML 1.0 API specification (from Swagger)
│       ├── dataweave/               # DataWeave transformation scripts
│       └── config/local/
│           └── local.yaml           # Environment configuration
```

**MuleSoft Implementation Steps**:

**Step 1: RAML API Specification (Convert from Swagger)**
```yaml
#%RAML 1.0
title: Zurich Insurance Policy API
version: v1
baseUri: https://api.zurich.com/{version}

/policy:
  /endorsement:
    post:
      description: Create new policy endorsement
      body:
        application/json:
          type: EndorsementRequest
      responses:
        201:
          body:
            application/json:
              type: EndorsementResponse
```

**Step 2: APIKit Main Flow Generation**
```xml
<!-- Auto-generated from RAML using APIKit -->
<flow name="api-main">
    <http:listener doc:name="HTTP" config-ref="httpListenerConfig" path="/api/*"/>
    <apikit:router config-ref="api-config"/>
</flow>
```

**Step 3: Implementation Flows**
```xml
<!-- endorsement-impl.xml -->
<flow name="post:\policy\endorsement:api-config">
    <logger level="INFO" message="Creating endorsement for payload: #[payload]"/>
    
    <!-- Input validation -->
    <validation:is-not-null value="#[payload.policyNumber]"/>
    
    <!-- Data transformation -->
    <ee:transform doc:name="Transform Request">
        <ee:message>
            <ee:set-payload><![CDATA[%dw 2.0
output application/json
---
{
    endorsementNumber: "END-" ++ (now() as String),
    policyNumber: payload.policyNumber,
    // Implementation from Confluence code snippets
}]]></ee:set-payload>
        </ee:message>
    </ee:transform>
    
    <!-- Business logic implementation -->
    <flow-ref doc:name="endorsement-business-logic"/>
    
    <!-- Response transformation -->
    <ee:transform doc:name="Transform Response">
        <ee:message>
            <ee:set-payload><![CDATA[%dw 2.0
output application/json
---
{
    status: "success",
    endorsement: payload
}]]></ee:set-payload>
        </ee:message>
    </ee:transform>
</flow>
```

**Step 4: External System Integration**
```xml
<!-- microservice-calls.xml -->
<flow name="endorsement-business-logic">
    <!-- Call external policy validation service -->
    <http:request method="GET" doc:name="Validate Policy" 
                  config-ref="HTTP_Request_configuration" 
                  path="/policy/validate/{policyNumber}">
        <http:uri-params><![CDATA[#[{'policyNumber': payload.policyNumber}]]]></http:uri-params>
    </http:request>
    
    <!-- Process business rules from Confluence -->
    <choice doc:name="Choice">
        <when expression="#[payload.eligible == true]">
            <flow-ref doc:name="process-endorsement"/>
        </when>
        <otherwise>
            <raise-error type="POLICY:NOT_ELIGIBLE" description="Policy not eligible for endorsement"/>
        </otherwise>
    </choice>
</flow>
```

#### C. Target Project: `api-test-suite` (Java TestNG API Testing Framework)

**API Test Development Workflow**:
1. **Update PathJava constants** with new endpoints from Swagger
2. **Create service classes** for API operations using RestAssured
3. **Implement test scenarios** based on Swagger examples
4. **Create test data files** (XML requests, JSON responses)
5. **Generate comprehensive test reports**

**API Test Implementation Steps**:

**Step 1: Update PathJava with Swagger Endpoints**
```java
// Extract from Swagger specification and add to PathJava.java
public static final String ENDORSEMENT_BASE = API_VERSION + "/policy/endorsement";
public static final String ENDORSEMENT_CREATE = ENDORSEMENT_BASE;
public static final String ENDORSEMENT_UPDATE = ENDORSEMENT_BASE + "/{id}";
public static final String ENDORSEMENT_GET = ENDORSEMENT_BASE + "/{id}";
public static final String ENDORSEMENT_LIST = ENDORSEMENT_BASE;
public static final String ENDORSEMENT_APPROVE = ENDORSEMENT_BASE + "/{id}/approve";
public static final String ENDORSEMENT_REJECT = ENDORSEMENT_BASE + "/{id}/reject";
```

**Step 2: Create Service Classes from Swagger**
```java
// EndorsementService.java - Generated from Swagger operations
public class EndorsementService {
    
    public Response createEndorsement(String requestBody) {
        return RestAssured.given()
            .spec(requestSpec)
            .body(requestBody)
            .post(PathJava.ENDORSEMENT_CREATE);
    }
    
    public Response getEndorsement(String endorsementId) {
        return RestAssured.given()
            .spec(requestSpec)
            .pathParam("id", endorsementId)
            .get(PathJava.ENDORSEMENT_GET);
    }
    
    // Additional methods from Swagger operations
}
```

**Step 3: Generate Test Classes from Swagger Examples**
```java
@Test
public class EndorsementAPITest extends BaseTest {
    
    @Test(description = "Create endorsement - Happy path from Swagger example")
    public void testCreateEndorsement_Success() {
        // Use test data from Swagger examples
        String requestBody = loadTestData("endorsement-create-request.json");
        
        Response response = endorsementService.createEndorsement(requestBody);
        
        // Assertions based on Swagger response schema
        response.then()
            .statusCode(201)
            .body("status", equalTo("success"))
            .body("endorsement.endorsementNumber", matchesPattern("END-.*"));
    }
    
    @Test(description = "Validation scenarios from Swagger constraints")
    public void testCreateEndorsement_ValidationErrors() {
        // Test cases for each validation rule in Swagger
    }
}
```

**Step 4: Swagger-to-TestNG Data Provider**
```java
// Generate test data providers from Swagger examples
@DataProvider(name = "endorsementTestData")
public Object[][] getEndorsementTestData() {
    return new Object[][] {
        {"valid-endorsement.json", 201, "success"},
        {"invalid-policy.json", 400, "validation_error"},
        {"missing-fields.json", 400, "required_field_missing"}
    };
}
```

#### D. Confluence Code Integration Workflow

**Extract and Implement Code Snippets**:

1. **Search Confluence for Code Examples**:
```
Use mcp_atlassian-mcp_search_across_spaces with query="[Story ID] code implementation"
Use mcp_atlassian-mcp_get_specific_confluence_page for "Technical Specifications"
```

2. **Parse Code Snippets from Confluence**:
- Extract Java code blocks for Spring Boot implementation
- Extract DataWeave scripts for MuleSoft transformations  
- Extract JSON examples for test data
- Extract SQL scripts for database changes

3. **Implement Confluence Code Patterns**:
```java
// Example: Implement business logic from Confluence code snippet
public class EndorsementServiceImpl {
    
    // Code pattern from Confluence documentation
    public EndorsementDTO processEndorsement(EndorsementDTO request) {
        // Business rules implementation from Confluence
        validatePolicyEligibility(request.getPolicyNumber());
        calculatePremiumAdjustment(request);
        applyEndorsementRules(request);
        return request;
    }
}
```

#### E. Swagger/OpenAPI Integration Steps

**Swagger-Driven Development Process**:

1. **Import Swagger Specification**:
```bash
# Download Swagger JSON/YAML from Confluence or Jira
curl -o api-spec.yaml [Confluence URL]/swagger-spec.yaml

# Generate Spring Boot code from Swagger
mvn clean generate-sources
```

2. **Validate API Implementation Against Swagger**:
```bash
# Validate Spring Boot endpoints match Swagger spec
mvn spring-boot:run
swagger-codegen validate -i api-spec.yaml -l spring

# Validate MuleSoft RAML against Swagger
mvn mule:validate-raml
```

3. **Generate Test Cases from Swagger**:
```bash
# Generate RestAssured tests from Swagger examples
swagger-codegen generate -i api-spec.yaml -l java -o test-generated/
```

4. **Update Documentation from Implementation**:
```bash
# Generate updated Swagger from Spring Boot annotations
mvn springdoc-openapi:generate
```

#### B. Target Project: `zurich-mule-poc` (MuleSoft Mule 4.8 - Anypoint Studio Compatible)

**Project Structure** (Anypoint Studio Compatible):
```
zurich-mule-poc/
├── src/main/
│   ├── mule/
│   │   ├── common/
│   │   │   ├── globals.xml          # Global configurations & properties
│   │   │   └── tls-config.xml       # TLS/SSL security configurations
│   │   ├── implementation/
│   │   │   ├── get-policy-list-impl.xml     # Policy list business logic
│   │   │   └── get-policy-details-impl.xml  # Policy details business logic
│   │   ├── system/
│   │   │   └── microservice-calls.xml       # External service integrations
│   │   └── zurich-mule-poc.xml      # Main API flows & routing
│   └── resources/
│       ├── api/
│       │   └── zurich-mule-poc.raml # RAML 1.0 API specification
│       └── config/local/
│           └── local.yaml           # Environment configuration
├── src/test/munit/
│   └── policy-*-test.xml            # MUnit test suites
└── mule-artifact.json               # Deployment descriptor
```

**Key Areas for Modification**:
- **API Specification**: RAML files (`zurich-mule-poc.raml`) - maintain APIKit compatibility
- **Main Flows**: API routing and HTTP listeners (`zurich-mule-poc.xml`)
- **Implementation Flows**: Business logic flows (`*-impl.xml`)
- **Global Configurations**: Connection configs, TLS, properties (`globals.xml`)
- **System Integration**: External service calls (`microservice-calls.xml`)
- **Configuration**: Environment properties (`local.yaml`)
- **Testing**: MUnit test suites (`*-test.xml`)

**Anypoint Studio Compatibility Requirements**:
- **RAML 1.0 Compliance**: All API specs must be valid RAML 1.0 format
- **APIKit Scaffolding**: Ensure RAML changes can regenerate flows via APIKit
- **Mule 4.8+ Components**: Use only supported connectors and modules
- **Global Element Definitions**: Proper configuration of HTTP listeners, TLS contexts
- **Secure Properties**: Use `secure::` prefix for sensitive configuration values
- **Flow Naming Conventions**: Follow kebab-case for flow names
- **Error Handling**: Implement proper error scopes with standardized error types
- **DataWeave 2.0**: All transformations must use DW 2.0 syntax

**MuleSoft-Specific Changes Based On**:
- **RAML API Contract**: API endpoint definitions and data models
- **Flow Design Patterns**: Request-response, one-way, batch processing
- **Security Requirements**: TLS configuration, authentication, authorization
- **Integration Patterns**: System API, Process API, Experience API layers
- **Error Handling Strategy**: Global vs. flow-specific error handling
- **Performance Considerations**: Streaming, batch size, timeout configurations

- **Anypoint Studio Validation Checklist**:
- ✅ Project imports successfully in Anypoint Studio 7.16+
- ✅ RAML specification is valid and generates APIKit flows
- ✅ All flows can be debugged using Anypoint Studio debugger
- ✅ No compilation errors in Mule configuration files
- ✅ MUnit tests execute successfully within Studio
- ✅ TLS configuration uses proper keystore/truststore setup
- ✅ Secure properties are properly externalized
- ✅ Application deploys successfully to embedded Mule runtime

#### C. Target Project: `api-test-suite` (Java TestNG API Testing Framework)

**Project Structure**:
```
api-test-suite/
├── src/main/java/
│   ├── java/service/
│   │   ├── PathJava.java          # API endpoint constants
│   │   └── [ServiceName]Service.java  # Service classes for API calls
│   └── resources/
│       ├── config.properties      # Configuration properties
│       └── data/                  # Test data files
├── src/test/java/
│   └── com/testau/               # Test automation classes
└── src/test/resources/
    ├── xml/                      # XML request files
    ├── json/                     # JSON response files
    └── testng.xml               # TestNG configuration
```

**API Test Suite Development Steps**:

**Step 1: Update PathJava.java Constants**
```java
// Add new API endpoint constants
public static final String NEW_API_BASE = API_VERSION + "/new-endpoint";
public static final String NEW_API_CREATE = NEW_API_BASE + "/create";
public static final String NEW_API_UPDATE = NEW_API_BASE + "/update";
public static final String NEW_API_DETAILS = NEW_API_BASE + "/details";
public static final String NEW_API_LIST = NEW_API_BASE + "/list";

// Add path variables for new endpoints
public static final String NEW_API_BY_ID = NEW_API_DETAILS + PATH_VAR_ID;
```

**Step 2: Create Service Class**
```java
// Create [EndpointName]Service.java in src/main/java/java/service/
public class [EndpointName]Service {
    // Implement REST API methods using RestAssured
    // - POST create operations
    // - GET retrieve operations  
    // - PUT update operations
    // - DELETE operations
    // - Parameter validation
    // - Response handling
}
```

**Step 3: Create Test Automation Class**
```java
// Create [EndpointName]Test.java in src/test/java/com/testau/
@Test
public class [EndpointName]Test extends BaseTest {
    // Test methods with assertions
    // - Positive test scenarios
    // - Negative test scenarios
    // - Boundary value testing
    // - Data validation tests
    // - Error handling tests
}
```

**Step 4: Create Test Data Files**
- **XML Request Files**: `src/test/resources/xml/[endpoint]-request.xml`
- **JSON Response Files**: `src/test/resources/json/[endpoint]-response.json`
- **Test Data Sets**: Various scenarios and edge cases

**Step 5: Update TestNG Configuration**
```xml
<!-- Add new test suite to testng.xml -->
<suite name="[EndpointName]TestSuite">
    <test name="[EndpointName]Tests">
        <classes>
            <class name="com.testau.[EndpointName]Test"/>
        </classes>
    </test>
</suite>
```

**API Testing Implementation Requirements**:
- **RestAssured Integration**: HTTP client for API calls
- **TestNG Annotations**: @Test, @BeforeMethod, @AfterMethod
- **Assertion Framework**: AssertJ or TestNG assertions
- **Data Providers**: External data sources (Excel, JSON, XML)
- **Extent Reports**: Test reporting and documentation
- **Configuration Management**: Environment-specific properties
- **Error Handling**: Custom exceptions and error validation
- **Request/Response Logging**: Detailed API call logging

**Test Scenarios to Implement**:
- **Happy Path Tests**: Valid requests with expected responses
- **Validation Tests**: Invalid data and boundary conditions
- **Security Tests**: Authentication and authorization scenarios
- **Performance Tests**: Response time and load testing
- **Integration Tests**: End-to-end workflow testing
- **Contract Tests**: API schema validation
- **Error Handling Tests**: HTTP error codes and error messages

**API Test Execution Commands**:
```bash
# Run specific test suite
mvn test -Dtest=[EndpointName]Test

# Run all API tests
mvn test -DsuiteXmlFile=testng.xml

# Run with specific environment
mvn test -Denv=test -DsuiteXmlFile=testng.xml

# Generate extent reports
mvn test -Dreporting=true

# Run with parallel execution
mvn test -Dparallel=methods -DthreadCount=3
```

### 4. Build & Test Execution

#### A. Spring Boot Project (zurich-spring-poc)

**Build Commands** (from zurich-spring-poc directory):
```bash
# Clean and compile
mvn clean compile

# Package application  
mvn clean package -DskipTests

# Full build with tests
mvn clean install
```

**Test Execution**:
```bash
# Unit tests only
mvn test -Punit-tests

# Integration tests only  
mvn verify -Pintegration-tests

# All tests
mvn verify -Pall-tests

# Smoke tests
mvn test -Psmoke-tests

# Regression tests
mvn test -Pregression-tests

# Using existing scripts
./scripts/run-tests.ps1     # Windows PowerShell
./scripts/run-tests.sh      # Linux/Mac

# Coverage report
mvn jacoco:report
```

**Test Framework Details**:
- **Unit Tests**: JUnit 5, Mockito, AssertJ
- **Integration Tests**: TestContainers, PostgreSQL
- **Contract Testing**: Spring Cloud Contract
- **Mock Services**: WireMock
- **Coverage**: JaCoCo (minimum 80% required)

#### B. MuleSoft Project (zurich-mule-poc)

**Build Commands** (from zurich-mule-poc directory):
```bash
# Clean and compile (Anypoint Studio compatible)
mvn clean compile

# Package Mule application
mvn clean package

# Full build with MUnit tests
mvn clean install

# Package for deployment (creates deployable JAR)
mvn clean package -DattachMuleSources
```

**Anypoint Studio Build Process**:
```bash
# Validate RAML specification
mvn mule:validate-raml

# Generate APIKit flows from RAML
mvn mule:generate-flows

# Run application in embedded runtime
mvn mule:run

# Deploy to CloudHub (requires credentials)
mvn mule:deploy -Dmule.runtime=4.8.1
```

**MUnit Test Execution**:
```bash
# Run all MUnit tests
mvn test

# Run specific MUnit test suite
mvn test -Dmunit.test=policy-details-endpoint-test

# Run MUnit tests with coverage
mvn clean test -Dmunit.coverage.report=true

# Generate MUnit HTML report
mvn munit:coverage-report
```

**Anypoint Studio Testing**:
- **MUnit Tests**: Component and flow testing
- **RAML Validation**: API contract testing
- **Debug Mode**: Step-through debugging in Studio
- **Mock Services**: WireMock integration for external services
- **Performance Testing**: Load testing with MUnit performance suite

**Deployment Validation**:
```bash
# Validate mule-artifact.json
mvn mule:validate-deployment-configuration

# Test TLS configuration
openssl s_client -connect localhost:8091 -servername localhost

# Health check endpoint
curl -k https://localhost:8091/health

# API endpoint validation
curl -k https://localhost:8091/poc/policy/list
```

#### C. API Test Suite Project (api-test-suite)

**Build Commands** (from api-test-suite directory):
```bash
# Clean and compile test framework
mvn clean compile

# Compile test classes
mvn clean test-compile

# Package test framework
mvn clean package -DskipTests
```

**Test Execution Commands**:
```bash
# Run all API tests
mvn clean test

# Run specific test suite
mvn test -Dtest=[TestClassName]

# Run with specific TestNG XML file
mvn test -DsuiteXmlFile=src/test/resources/testng.xml

# Run tests for specific environment
mvn test -Denv=test -DbaseUrl=https://test-api.zurich.com

# Run tests with parallel execution
mvn test -Dparallel=methods -DthreadCount=3

# Run regression test suite
mvn test -DsuiteXmlFile=src/test/resources/regression-suite.xml

# Run smoke test suite
mvn test -DsuiteXmlFile=src/test/resources/smoke-suite.xml
```

**Report Generation**:
```bash
# Generate Extent Reports
mvn test -Dreporting=true

# Generate Surefire reports
mvn surefire-report:report

# Generate comprehensive test report
mvn clean test surefire-report:report
```

**API Test Framework Features**:
- **RestAssured**: HTTP client for REST API testing
- **TestNG**: Test framework with annotations and parallel execution
- **Extent Reports**: Rich HTML test reports with screenshots
- **Data Providers**: External data sources (Excel, JSON, XML, CSV)
- **Configuration Management**: Environment-specific property files
- **Request/Response Logging**: Detailed API call logging with Log4j
- **Assertion Libraries**: AssertJ for fluent assertions
- **Test Data Management**: XML requests and JSON response validation

### 4. Execute Builds & Tests

#### A. Pre-Build Setup and Validation

**Swagger/OpenAPI Specification Validation**:
```bash
# Validate all API specifications before build
# Spring Boot project - validate OpenAPI compliance
cd zurich-spring-poc
mvn clean compile
mvn springdoc-openapi:generate

# MuleSoft project - validate RAML syntax and consistency
cd ../zurich-mule-poc  
mvn clean compile
mvn mule:validate

# API Test Suite - validate test specifications
cd ../api-test-suite
mvn clean compile
mvn test-compile
```

#### B. Execute Comprehensive Build Process

**1. Zurich Spring POC (Spring Boot 3.3.0)**:
```bash
cd zurich-spring-poc

# Clean and compile with dependencies
mvn clean compile

# Run unit tests with coverage
mvn test jacoco:report

# Integration tests with TestContainers
mvn integration-test -Dspring.profiles.active=test

# Package application
mvn package -DskipTests=false

# Generate OpenAPI documentation
mvn springdoc-openapi:generate

# Quality gates and static analysis
mvn sonar:sonar spotbugs:check

# Build success validation
echo "✅ Spring Boot build completed successfully"
```

**2. Zurich Mule POC (MuleSoft Mule 4.8)**:
```bash
cd zurich-mule-poc

# Clean and validate RAML specifications
mvn clean validate

# Compile Mule application for Anypoint Studio compatibility
mvn compile -Dmule.version=4.8.0

# Run MUnit tests
mvn test -Dmunit.version=2.3.15

# Package deployable archive (.jar)
mvn package -DskipTests=false

# Validate Anypoint Studio compatibility
mvn mule:validate-studio-compatibility

# APIKit validation for RAML compliance
mvn mule:validate-apikit

# Build success validation  
echo "✅ MuleSoft application build completed successfully"
```

**3. API Test Suite (TestNG + RestAssured)**:
```bash
cd api-test-suite

# Clean and compile test framework
mvn clean compile test-compile

# Execute API test suites with different profiles
mvn test -Dtest.profile=regression
mvn test -Dtest.profile=smoke  
mvn test -Dtest.profile=integration

# Generate comprehensive test reports
mvn surefire-report:report
mvn site:site

# Validate API contract compliance (if Swagger available)
mvn test -Dtest.contract.validation=true

# Performance and load tests (if configured)
mvn test -Dtest.performance=true

# Build success validation
echo "✅ API test suite execution completed successfully"
```

#### C. Cross-Project Integration Validation

**API Contract Testing**:
```bash
# Validate API contracts between projects
# Test Spring Boot endpoints against defined Swagger spec
cd zurich-spring-poc
mvn test -Dspring.test.contract=true

# Test MuleSoft RAML implementation against Spring Boot API
cd ../zurich-mule-poc
mvn test -Dmunit.contract.test=spring-boot-endpoints

# Validate test suite coverage against both implementations  
cd ../api-test-suite
mvn test -Dapi.target=spring-boot -Dapi.target=mule-app
```

#### D. Build Failure Handling

**Automated Issue Resolution**:
```bash
# If Spring Boot build fails
if [ $? -ne 0 ]; then
    echo "❌ Spring Boot build failed - analyzing..."
    mvn dependency:analyze
    mvn dependency:tree
    mvn help:effective-pom
    
    # Check for common issues
    mvn versions:display-dependency-updates
    mvn compile -X  # Debug mode
fi

# If MuleSoft build fails  
if [ $? -ne 0 ]; then
    echo "❌ MuleSoft build failed - analyzing..."
    mvn mule:help -Ddetail=true
    mvn dependency:tree -Dincludes=org.mule.*
    
    # Validate Anypoint Studio compatibility
    mvn mule:validate-studio-compatibility -X
fi

# If API tests fail
if [ $? -ne 0 ]; then
    echo "❌ API tests failed - analyzing..."
    # Generate detailed failure reports
    mvn surefire-report:report-only
    # Check API endpoint availability
    curl -f http://localhost:8080/actuator/health || echo "Spring Boot service not available"
fi
```

#### E. Build Success Criteria

**Validation Checklist**:
- ✅ All Maven builds complete without errors (exit code 0)
- ✅ Unit tests pass with >80% code coverage  
- ✅ Integration tests validate API contracts
- ✅ MuleSoft application compatible with Anypoint Studio 7.16+
- ✅ Spring Boot application starts successfully with embedded server
- ✅ API test suite validates all endpoints with assertions
- ✅ OpenAPI/Swagger documentation generated and accessible
- ✅ RAML specifications valid and consistent with implementation
- ✅ No critical security vulnerabilities detected
- ✅ Quality gates met (SonarQube/SpotBugs if configured)

### 5. Validation & Quality Checks

**Automated Checks**:
```bash
# Code coverage validation
mvn jacoco:check

# Static code analysis (if configured)
mvn spotbugs:check

# Security scan (if configured)  
mvn dependency-check:check
```

**Manual Validation Points**:
- Compilation success with no errors
- All tests pass (unit, integration, contract)
- Code coverage meets 80% threshold
- No regression in existing functionality
- API contracts maintained
- Database migrations (if any) are reversible

### 6. Reporting & Documentation

**Generate Reports**:
```bash
# Test reports location
target/surefire-reports/     # Unit test results
target/failsafe-reports/     # Integration test results  
target/site/jacoco/         # Coverage reports
```

### 7. Generate Documentation and Upload to Jira

**CRITICAL**: This step is MANDATORY and must NOT be skipped. Document generation and upload is essential for compliance and project tracking.

After tests and validation are complete, generate documentation artifacts and upload them to the Jira story:

#### A. Document Generation Prerequisites

**STEP 1: Verify Spring Boot Version Compatibility**
```bash
# Check Spring Boot version in pom.xml to prevent compilation errors
cat zurich-spring-poc/pom.xml | grep -A5 "spring-boot-starter-parent"

# Verify current version is compatible with:
# - Spring Boot 3.x requires Jakarta EE namespace (not javax.*)
# - Java 17+ for Spring Boot 3.x
# - Maven 3.6+ for build compatibility
```

**STEP 2: Validate Build Status Before Documentation**
```bash
# Ensure both projects build successfully
cd zurich-spring-poc && mvn clean compile
cd ../zurich-mule-poc && mvn clean compile

# Only proceed with documentation if both builds are successful
```

#### B. Business Requirements Document (BRD) Generation

**MANDATORY**: Create a comprehensive Business Requirements Document with the following structure:

```markdown
# Business Function Document (BFD)
## Story: [JIRA_KEY] - [TITLE]

### 1. Executive Summary
- **Business Problem**: [Extracted from Jira description]
- **Solution Overview**: [API implementation across Spring Boot, MuleSoft, and test automation]
- **Business Impact**: [Expected benefits and value delivery]

### 2. Functional Requirements
- **API Specifications**: [Swagger/OpenAPI definitions]
- **Business Rules**: [From Confluence documentation] 
- **Data Flow**: [Request/Response patterns]
- **Integration Points**: [External systems and services]

### 3. Implementation Overview
#### Spring Boot Implementation:
- **Endpoints Created**: [List of REST endpoints with URLs]
- **Business Logic**: [Service layer implementations]
- **Data Models**: [Entity and DTO structures]
- **Validation Rules**: [Input validation and constraints]

#### MuleSoft Implementation:
- **RAML Specifications**: [API contract definitions]
- **Integration Flows**: [DataWeave transformations and routing]
- **Error Handling**: [Exception management patterns]
- **Anypoint Studio Compatibility**: [Version compatibility notes]

#### API Test Coverage:
- **Test Scenarios**: [Happy path, validation, error scenarios]
- **Data Coverage**: [Test data sources and examples]
- **Automation Coverage**: [Regression and integration test scope]

### 4. Configuration and Deployment
- **Environment Requirements**: [Development, staging, production settings]
- **Dependencies**: [External services and database requirements]
- **Performance Considerations**: [Expected load and response times]

### 5. Risk Assessment and Mitigation
- **Technical Risks**: [Implementation and integration challenges]
- **Business Risks**: [Impact assessment and contingency plans]
- **Security Considerations**: [Authentication, authorization, data protection]

### 6. Success Criteria and Acceptance
- **Functional Acceptance**: [Business requirement validation]
- **Technical Acceptance**: [Performance and quality metrics]
- **User Acceptance**: [End-user validation criteria]
```

#### B. Technical Document (TD) Generation

Create a detailed Technical Document with implementation specifics:

```markdown
# Technical Document (TD)
## Story: [JIRA_KEY] - [TITLE]

### 1. Architecture Overview
- **System Architecture**: [Multi-project integration diagram]
- **Technology Stack**: [Spring Boot 3.3.0, MuleSoft 4.8, TestNG framework]
- **Design Patterns**: [Applied patterns and architectural decisions]

### 2. API Design and Implementation

#### 2.1 Spring Boot Implementation Details
**Controller Layer**:
```java
// Generated controller code with annotations
@RestController
@RequestMapping("/v1/[resource]")
public class [Resource]Controller {
    // Implementation details from automation
}
```

**Service Layer**:
```java
// Business logic implementation
@Service
public class [Resource]ServiceImpl implements [Resource]Service {
    // Code extracted from Confluence and implemented
}
```

**Data Layer**:
```java
// Entity and repository implementation
@Entity
public class [Resource] {
    // Database mapping and relationships
}
```

#### 2.2 MuleSoft Implementation Details
**RAML Specification**:
```yaml
#%RAML 1.0
title: [API Title]
# Complete RAML definition generated from Swagger
```

**Flow Implementation**:
```xml
<!-- Main API flows -->
<flow name="[resource]-main-flow">
    <!-- Implementation with DataWeave transformations -->
</flow>
```

#### 2.3 API Test Implementation
**Test Structure**:
```java
// TestNG test classes with RestAssured
public class [Resource]APITest extends BaseTest {
    // Comprehensive test scenarios
}
```

### 3. Database Design (if applicable)
- **Schema Changes**: [DDL scripts and migration files]
- **Data Relationships**: [Entity relationship diagrams]
- **Performance Optimization**: [Indexing and query optimization]

### 4. Integration Specifications
- **External APIs**: [Third-party service integrations]
- **Message Formats**: [Request/response schemas with examples]
- **Error Handling**: [Exception mapping and error codes]

### 5. Security Implementation
- **Authentication**: [Security mechanisms and protocols]
- **Authorization**: [Role-based access control]
- **Data Protection**: [Encryption and data masking]

### 6. Performance and Monitoring
- **Performance Metrics**: [Response times and throughput expectations]
- **Monitoring Setup**: [Logging and metrics collection]
- **Alerting**: [Error detection and notification systems]

### 7. Deployment and Operations
- **Build Process**: [Maven build lifecycle and configurations]
- **Environment Configuration**: [Property files and environment variables]
- **Deployment Strategy**: [Release and rollback procedures]

### 8. Testing Strategy
- **Unit Testing**: [Test coverage and mocking strategies]
- **Integration Testing**: [End-to-end test scenarios]
- **Performance Testing**: [Load and stress testing approach]

### 9. Code Quality and Standards
- **Coding Standards**: [Style guides and best practices followed]
- **Code Review**: [Review checklist and quality gates]
- **Static Analysis**: [SonarQube/SpotBugs configurations and results]

### 10. Troubleshooting Guide
- **Common Issues**: [Known problems and solutions]
- **Diagnostic Tools**: [Logging and debugging approaches]
- **Support Procedures**: [Escalation and resolution processes]
```

#### C. Document Generation and Upload Process

**Automated Document Creation**:
```bash
# Generate BFD from templates and Jira data
echo "Generating Business Function Document..."
cat > BFD_${JIRA_KEY}.md << EOF
[Generated BFD content based on implementation and Jira story]
EOF

# Generate TD with code examples and technical details
echo "Generating Technical Document..."
cat > TD_${JIRA_KEY}.md << EOF
[Generated TD content with actual implementation code]
EOF

# Generate API documentation from Swagger/OpenAPI
cd zurich-spring-poc
mvn springdoc-openapi:generate
cp target/generated-docs/openapi.yaml ../API_SPEC_${JIRA_KEY}.yaml

# Generate MuleSoft RAML documentation
cd ../zurich-mule-poc
cp src/main/resources/api/zurich-mule-poc.raml ../RAML_SPEC_${JIRA_KEY}.raml

# Generate test execution reports
cd ../api-test-suite
mvn surefire-report:report-only
cp target/site/surefire-report.html ../TEST_REPORT_${JIRA_KEY}.html
```

**Upload to Jira Ticket**:
```bash
# Use MCP Atlassian tools to upload documents
# Upload BFD
echo "Uploading Business Function Document to Jira..."
# Note: File upload would require additional MCP functionality

# Add comment with documentation summary
echo "Adding implementation summary comment to Jira..."
cat > jira_comment.md << EOF
## Implementation Complete ✅

### Summary
- **Spring Boot API**: Implemented with OpenAPI documentation
- **MuleSoft Integration**: RAML-compliant flows with Anypoint Studio compatibility  
- **API Test Coverage**: Comprehensive test automation with assertions
- **Swagger Integration**: API-first development with contract validation

### Documentation Generated
- Business Function Document (BFD): Attached
- Technical Document (TD): Attached  
- API Specifications: OpenAPI/RAML files attached
- Test Reports: Comprehensive test execution results

### Build Status
- ✅ Spring Boot: Build successful, tests passing
- ✅ MuleSoft: Package created, Anypoint Studio compatible
- ✅ API Tests: All scenarios validated, assertions successful

### Next Steps
- Review attached documentation
- Deploy to staging environment for UAT
- Schedule business acceptance testing
EOF

# Post comment to Jira issue using MCP tools
# mcp_atlassian-mcp_update_jira_issue with comment
```

#### D. Confluence Documentation Update

**Update Confluence with Implementation Details**:
```bash
# Create or update Confluence page with implementation
echo "Updating Confluence with implementation details..."

# Extract code snippets for future reference
mkdir -p confluence-updates
echo "# Implementation Code Snippets for ${JIRA_KEY}" > confluence-updates/code-snippets.md

# Add Spring Boot code examples
echo "## Spring Boot Implementation" >> confluence-updates/code-snippets.md
find zurich-spring-poc/src/main/java -name "*.java" -exec echo "### {}" \; -exec cat {} \; >> confluence-updates/code-snippets.md

# Add MuleSoft code examples  
echo "## MuleSoft Implementation" >> confluence-updates/code-snippets.md
find zurich-mule-poc/src/main/mule -name "*.xml" -exec echo "### {}" \; -exec cat {} \; >> confluence-updates/code-snippets.md

# Upload to Confluence using MCP tools
# mcp_atlassian-mcp_create_confluence_page or mcp_atlassian-mcp_update_confluence_page
```

**Final Validation**:
- ✅ BFD generated with business requirements and implementation overview
- ✅ TD created with detailed technical specifications and code examples
- ✅ API documentation updated with Swagger/OpenAPI specifications
- ✅ Test reports generated with comprehensive coverage analysis
- ✅ All documents uploaded to Jira ticket for stakeholder review
- ✅ Confluence updated with reusable code snippets and patterns
- ✅ Implementation ready for staging deployment and business acceptance testing
   - Content: Business goals, user stories, acceptance criteria, user journeys, data requirements, reporting requirements, and high-level UI/UX notes.
   - Format: Markdown or Word (e.g., `KAN-X-business-function.md` or `.docx`).
   - Template (minimum):
     - Title, Story ID, Author, Date
     - Executive Summary
     - Business Objectives
     - Scope and Non-Functional Requirements
     - Acceptance Criteria
     - User Journeys / Wireframes (if any)
     - Data Requirements
     - Dependencies and Assumptions

2. Technical Document (TD)
   - Content: API contracts, data models, sequence diagrams, integration points, security requirements, deployment and configuration notes, and test strategy summary.
   - Format: Markdown or Word (e.g., `KAN-X-technical.md` or `.docx`).
   - Template (minimum):
     - Title, Story ID, Author, Date
     - Technical Overview
     - API Endpoints and Payloads (RAML/OpenAPI snippets)
     - Data Models and DB changes
     - Integration Points and Sequence Diagrams
     - Security & TLS Configuration
     - Build / Run / Test Instructions
     - Migration and Rollback Notes

3. Generate files automatically (example scripts):
```bash
# Create BFD markdown from template variables
python tools/generate_bfd.py --story KAN-X --output docs/KAN-X-business-function.md

# Create TD markdown from template variables
python tools/generate_td.py --story KAN-X --output docs/KAN-X-technical.md
```

4. Attach files to Jira story (example MCP tool usage)
```
# Use MCP server to upload attachments (example using available MCP tool):
# Note: the MCP tool supports updating issues and adding attachments via its API. Use the appropriate client method in your environment.
mcp_atlassian-mcp_update_jira_issue
  --issueKey "KAN-X"
  --comment "Attaching Business Function Document (BFD) and Technical Document (TD)"
  --attachments "docs/KAN-X-business-function.md,docs/KAN-X-technical.md"
```

Notes:
- If automatic generation cannot populate diagrams, create placeholders and link Confluence pages.
- Ensure sensitive configuration values are not checked into repo; include references to secure configs instead.


## Technical Constraints & Environment:

### A. Spring Boot Application Stack:
- **Spring Boot**: 3.3.0
- **Java**: 21
- **Database**: H2 (runtime), PostgreSQL (integration tests)
- **Security**: JWT tokens
- **Testing**: JUnit 5, TestContainers
- **Build**: Maven 3.6+

### B. MuleSoft Application Stack:
- **Mule Runtime**: 4.8.1
- **Anypoint Studio**: 7.16+ required
- **Java**: 8 or 11 (for Mule runtime compatibility)
- **RAML**: 1.0 specification
- **Security**: TLS/SSL with certificate management
- **Testing**: MUnit framework
- **Build**: Maven with Mule Maven Plugin 4.3.0+

### C. API Test Suite Stack:
- **Java**: 8 or 11
- **Testing Framework**: TestNG
- **HTTP Client**: RestAssured  
- **Reporting**: Extent Reports
- **Build**: Maven 3.6+
- **Logging**: Log4j2
- **Assertions**: AssertJ, TestNG assertions
- **Data Management**: XML, JSON, Excel support

### MCP Server Configuration:
- **Transport**: stdio
- **Port**: 3001  
- **Name**: Atlassian-mcp-server
- **Version**: 1.0.0

### Configuration Files:

#### Spring Boot Configuration:
- `application.yml` - Main application config
- `application-test.yml` - Test-specific config  
- `.env` - Environment variables
- `local.yaml` - Local development settings

#### MuleSoft Configuration:
- `mule-artifact.json` - Deployment descriptor
- `src/main/resources/config/local/local.yaml` - Environment properties
- `src/main/mule/common/globals.xml` - Global configurations
#### API Test Suite Configuration:
- `config.properties` - Environment and API configuration
- `extent.properties` - Extent reporting configuration
- `log4j2.properties` - Logging configuration
- `testng.xml` - TestNG suite configuration
- `src/test/resources/xml/` - XML request templates
- `src/test/resources/json/` - JSON response samples

## Error Handling Strategy:

### Jira/Confluence Access Issues:
- Handle API rate limits gracefully
- Retry failed requests with exponential backoff
- Log authentication/authorization failures
- Fallback to manual requirement gathering if MCP unavailable

### Build/Test Failures:
- Parse Maven output for specific error types
- Identify compilation vs. test vs. packaging failures
- Provide actionable error messages
- Suggest fixes for common issues (dependencies, Java version, etc.)

### Code Analysis Challenges:
- Handle missing or incomplete requirements
- Identify conflicting specifications
- Flag potential breaking changes
- Suggest incremental implementation approach

## Expected Input Format:
```
Story Number: KAN-X
Additional Context: [Optional - specific focus areas, constraints, or special instructions]
Validation Level: [basic|standard|comprehensive]
```

## Expected Output Format:

```markdown
## Analysis Summary
- **Story**: [KAN-X] [Story title]
- **Type**: [Story/Bug/Task/Epic] 
- **Priority**: [High/Medium/Low]
- **Story Points**: [X points]
- **Current Status**: [To Do/In Progress/Done]
- **Assignee**: [Developer name]

### Requirements Analysis
- **User Story**: As a [user]... I want [goal]... So that [benefit]...
- **Acceptance Criteria**: 
  - [ ] Criterion 1
  - [ ] Criterion 2
- **Technical Requirements**:
  - API endpoints: [list]
  - Data models: [list]  
  - Business rules: [list]
- **Dependencies**: [Linked issues or blockers]

### Confluence Documentation
- **Design Documents Found**: [Y/N]
- **Key Specifications**:
  - Architecture changes: [summary]
  - API contracts: [changes]
  - Database schema: [modifications]
- **Additional Context**: [relevant details]

## Implementation Plan
### Impacted Areas:

#### Spring Boot Project:
- **Controllers**: [files to modify/create]
- **Services**: [business logic changes]  
- **Models**: [new entities/DTOs]
- **Configuration**: [config updates]
- **Database**: [schema changes]

#### MuleSoft Project:
- **API Specification**: [RAML changes]
- **Main Flows**: [API routing modifications]
- **Implementation Flows**: [business logic flows]
- **Global Configurations**: [connection/TLS configs]
- **System Integration**: [external service changes]
#### API Test Suite Project:
- **PathJava Constants**: [new endpoint constants]
- **Service Classes**: [API service implementations]
- **Test Classes**: [test automation classes]
- **Test Data**: [XML requests, JSON responses]
- **TestNG Configuration**: [suite and test definitions]
- **Reports Configuration**: [extent and logging setup]

### Implementation Approach:
1. [Step 1 with rationale]
2. [Step 2 with rationale]  
3. [Step 3 with rationale]

## Changes Applied
- **[File path]**: [Description of changes and reasoning]
- **[File path]**: [Description of changes and reasoning]
- **[File path]**: [Description of changes and reasoning]

## Build & Test Results

### A. Spring Boot Results:
#### Compilation: ✅ Success / ❌ Failure
- **Build Time**: [X seconds]
- **Warnings**: [count and summary]
- **Errors**: [none or details]

#### Test Execution:
- **Unit Tests**: ✅ [X passed] / ❌ [Y failed] 
- **Integration Tests**: ✅ [X passed] / ❌ [Y failed]
- **Contract Tests**: ✅ [X passed] / ❌ [Y failed]
- **Total Test Time**: [X seconds]

#### Code Coverage:
- **Overall Coverage**: [X%] (Target: 80%)
- **Line Coverage**: [X%]
- **Branch Coverage**: [X%]  
- **Coverage Trend**: [↑ Improved / ↓ Decreased / → Maintained]

### B. MuleSoft Results:
#### Anypoint Studio Compatibility: ✅ Success / ❌ Failure
- **RAML Validation**: ✅ Valid / ❌ Invalid
- **APIKit Flow Generation**: ✅ Success / ❌ Failed
- **Studio Import**: ✅ Success / ❌ Failed
- **Mule Artifact Validation**: ✅ Valid / ❌ Invalid

#### Build & Package:
- **Compilation**: ✅ Success / ❌ Failure
- **Package Creation**: ✅ Success / ❌ Failed
- **Deployment Validation**: ✅ Success / ❌ Failed

#### MUnit Test Execution:
- **MUnit Tests**: ✅ [X passed] / ❌ [Y failed]
- **Coverage**: [X%] (flows/components covered)
- **Test Execution Time**: [X seconds]

#### Runtime Validation:
- **Application Startup**: ✅ Success / ❌ Failed
- **HTTP Listeners**: ✅ Active / ❌ Failed
- **TLS Configuration**: ✅ Valid / ❌ Invalid
- **API Endpoints**: ✅ Accessible / ❌ Failed

### C. API Test Suite Results:
#### Test Execution: ✅ Success / ❌ Failure  
- **Total Tests**: [X executed]
- **Passed**: ✅ [X passed] 
- **Failed**: ❌ [Y failed]
- **Skipped**: [Z skipped]
- **Test Execution Time**: [X seconds]

#### API Test Coverage:
- **Endpoints Tested**: [X/Y endpoints]
- **Test Scenarios**: [positive, negative, boundary, security]
- **Response Validation**: ✅ Schema valid / ❌ Schema errors
- **Performance**: ✅ Within SLA / ❌ Exceeded thresholds

#### Test Reports Generated:
- **Extent Report**: ✅ Generated / ❌ Failed
- **Surefire Report**: ✅ Generated / ❌ Failed  
- **API Documentation**: ✅ Updated / ❌ Needs update

### Quality Gates:
- **Coverage Threshold**: ✅ Met / ❌ Failed
- **Static Analysis**: ✅ Clean / ❌ Issues found
- **Security Scan**: ✅ Clean / ❌ Vulnerabilities found
- **Anypoint Studio Compatibility**: ✅ Compatible / ❌ Issues found

## Issues & Recommendations

### ❌ Critical Issues:
- [Issue description with immediate action needed]

### ⚠️ Warnings:
- [Warning description with suggested action]  

### ✅ Improvements:
- [Enhancement suggestion for future consideration]

### 🔄 Next Steps:
1. [Immediate action item]
2. [Follow-up task]  
3. [Future enhancement]

### 📋 Update Jira:
- **Status Update**: [Suggest status change if appropriate]
- **Comments**: [Summary to add to Jira issue]
- **Time Logged**: [Development time spent]

## Artifacts Generated:

### Spring Boot Artifacts:
- **Test Reports**: `target/surefire-reports/index.html`
- **Coverage Report**: `target/site/jacoco/index.html`  
- **Build Logs**: [timestamp-build.log]
- **API Documentation**: [if generated]

### MuleSoft Artifacts:
- **MUnit Test Reports**: `target/munit-reports/munit-report.html`
- **MUnit Coverage Report**: `target/munit-coverage/munit-coverage.html`
- **Deployable Application**: `target/zurich-mule-poc-1.0.0-SNAPSHOT-mule-application.jar`
- **RAML Documentation**: `src/main/resources/api/zurich-mule-poc.raml`
- **Build Logs**: [timestamp-mule-build.log]
- **Studio Project Files**: `.mule/` directory for Anypoint Studio import

### API Test Suite Artifacts:
- **Extent Reports**: `test-output/ExtentReports/extent-report.html`
- **Surefire Reports**: `target/surefire-reports/index.html`
- **Test Logs**: `logs/api-test-execution.log`
- **Request/Response Logs**: `logs/api-calls.log`
- **Test Data Files**: `src/test/resources/xml/` and `src/test/resources/json/`
- **TestNG Results**: `test-output/testng-results.xml`
```

## Advanced Usage Examples:

### Example 1: Simple Feature Implementation
```
Input: 
Story Number: KAN-5
Additional Context: Focus on API endpoint creation only
Validation Level: standard

Expected: Implement new REST endpoint, basic tests, standard validation
```

### Example 2: Complex Integration
```
Input:
Story Number: KAN-12  
Additional Context: Database schema changes required, integration with external service
Validation Level: comprehensive

Expected: Full analysis, database migrations, integration tests, security review
```

### Example 3: Bug Fix
```
Input:
Story Number: KAN-8
Additional Context: Production hotfix, minimal changes
Validation Level: basic

Expected: Targeted fix, regression tests, fast turnaround
```

## Best Practices Reminders:

1. **Always fetch fresh Jira data** before starting work
2. **Check for linked Confluence pages** for additional context  
3. **Validate existing tests pass** before making changes
4. **Follow existing code patterns** and naming conventions
5. **Update relevant documentation** if API changes are made
6. **Consider backward compatibility** for public APIs
7. **Log development time** for project tracking
8. **Update Jira status** upon completion

## CRITICAL: Document Generation and Upload Checklist

**⚠️ MANDATORY STEPS - DO NOT SKIP**:

### Pre-Documentation Validation:
- [ ] **Spring Boot Version Check**: Verify compatibility (3.x requires Jakarta namespace)
- [ ] **Build Validation**: Both Spring Boot and MuleSoft projects compile successfully  
- [ ] **Endpoint Testing**: All implemented APIs tested and functional
- [ ] **Database Schema**: Changes documented and validated
- [ ] **Integration Flows**: MuleSoft flows tested and working

### Document Generation Requirements:
- [ ] **Business Requirements Document (BRD)**: Created in `zurich-spring-poc/docs/`
- [ ] **Technical Document (TD)**: Created in `zurich-spring-poc/docs/[JIRA_KEY]-Technical-Document.md`
- [ ] **Real Implementation Details**: Documents contain actual code examples, not placeholders
- [ ] **API Documentation**: Actual request/response payloads included
- [ ] **Build Instructions**: Tested and working deployment procedures

### Jira Integration Requirements:
- [ ] **Implementation Summary**: Comprehensive comment added to Jira issue
- [ ] **Build Status**: Success/failure status reported with details
- [ ] **Documentation Links**: References to generated documents included
- [ ] **Ready Status**: Implementation marked as complete and ready for review

### Failure Conditions (STOP if any occur):
- ❌ Spring Boot version incompatibility detected (e.g., javax.* in Spring Boot 3.x)
- ❌ Compilation errors in either project after implementation
- ❌ Document generation fails or produces incomplete content
- ❌ Jira upload fails or comment not added successfully
- ❌ Build validation shows failures or errors

### Success Criteria:
- ✅ Both projects build without compilation errors
- ✅ All endpoints implemented and tested
- ✅ Complete BRD and TD documents generated with real implementation details
- ✅ Jira issue updated with comprehensive implementation summary
- ✅ Documentation references accessible and complete
- ✅ Ready for stakeholder review and staging deployment

**AUTOMATION WORKFLOW IS INCOMPLETE** until all documentation and upload steps are verified successful.

This prompt enables fully automated development workflows while maintaining high code quality and proper documentation standards.