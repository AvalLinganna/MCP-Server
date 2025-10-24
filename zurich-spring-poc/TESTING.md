# Zurich Spring POC - Automated Testing Suite

## Overview

This document provides comprehensive information about the automated testing suite for the Zurich Spring POC project. The suite includes unit tests, integration tests, regression tests, performance tests, and automated CI/CD pipeline integration.

## Table of Contents

1. [Test Architecture](#test-architecture)
2. [Test Categories](#test-categories)
3. [Running Tests](#running-tests)
4. [Test Configuration](#test-configuration)
5. [CI/CD Integration](#cicd-integration)
6. [Test Data Management](#test-data-management)
7. [Reporting](#reporting)
8. [Troubleshooting](#troubleshooting)

## Test Architecture

The testing suite follows a layered architecture approach:

```
├── Unit Tests (Fast, Isolated)
├── Integration Tests (Component Interaction)
├── Regression Tests (Feature Validation)
├── Performance Tests (Load & Stress)
└── End-to-End Tests (Complete Workflows)
```

### Key Components

- **Base Test Classes**: Provide common setup and configuration
- **Test Data Manager**: Handles test data creation and cleanup
- **Automation Scripts**: PowerShell and Bash scripts for CI/CD
- **Maven Profiles**: Different configurations for test categories
- **GitHub Actions**: Automated pipeline execution

## Test Categories

### 1. Unit Tests
- **Purpose**: Test individual components in isolation
- **Scope**: Model classes, service methods, utility functions
- **Features Tested**: 
  - KAN-24: Nominee information fields
  - KAN-25: Address information fields
  - Business logic validation
  - Data transformation
- **Execution Time**: < 5 minutes
- **Coverage Target**: > 80%

### 2. Integration Tests
- **Purpose**: Test component interactions
- **Scope**: Service layer integration, repository operations, API endpoints
- **Features Tested**:
  - Database operations
  - Service orchestration
  - API contract validation
- **Execution Time**: 5-15 minutes
- **Prerequisites**: Test database, mock services

### 3. Regression Tests
- **Purpose**: Validate existing functionality after changes
- **Scope**: Critical business workflows, previously fixed bugs
- **Features Tested**:
  - Complete policy management workflow
  - Data integrity across operations
  - Backward compatibility
- **Execution Time**: 15-30 minutes
- **Frequency**: Daily automated runs

### 4. Performance Tests
- **Purpose**: Validate system performance under load
- **Scope**: API response times, database performance, memory usage
- **Metrics**:
  - Response time < 2 seconds
  - Throughput > 100 requests/second
  - Memory usage < 1GB
- **Execution Time**: 30-60 minutes

### 5. Smoke Tests
- **Purpose**: Quick validation of core functionality
- **Scope**: Critical path verification, basic API health checks
- **Execution Time**: < 2 minutes
- **Frequency**: Every commit, PR validation

## Running Tests

### Local Development

#### Prerequisites
- Java 21
- Maven 3.8+
- Git

#### Command Line Execution

```bash
# Run all unit tests
./scripts/run-tests.sh --type unit --coverage

# Run integration tests
./scripts/run-tests.sh --type integration --parallel

# Run regression tests
./scripts/run-tests.sh --type regression --coverage --fail-fast

# Run performance tests
./scripts/run-tests.sh --type performance --verbose

# Run all tests
./scripts/run-tests.sh --type all --coverage --parallel
```

#### Windows PowerShell

```powershell
# Run unit tests with coverage
.\scripts\run-tests.ps1 -TestType unit -Coverage

# Run regression tests with parallel execution
.\scripts\run-tests.ps1 -TestType regression -Parallel -FailFast

# Run all tests with verbose output
.\scripts\run-tests.ps1 -TestType all -Coverage -Parallel -Verbose
```

#### Maven Direct Execution

```bash
# Unit tests only
mvn clean test -Punit-tests

# Integration tests
mvn clean verify -Pintegration-tests

# Regression tests
mvn clean test -Pregression-tests

# All tests with coverage
mvn clean verify -Pall-tests jacoco:report
```

### IDE Integration

#### IntelliJ IDEA
1. Import project as Maven project
2. Set JDK to version 21
3. Enable annotation processing for Lombok
4. Run tests using built-in test runner
5. View coverage reports in IDE

#### Eclipse
1. Import as existing Maven project
2. Install Lombok plugin
3. Configure JDK compliance to version 21
4. Use JUnit view for test execution

## Test Configuration

### Maven Profiles

| Profile | Purpose | Test Types | Coverage |
|---------|---------|------------|----------|
| `unit-tests` | Unit testing | *Test.java | Yes |
| `integration-tests` | Integration testing | *IntegrationTest.java, *IT.java | Yes |
| `regression-tests` | Regression testing | @RegressionTest tagged | Yes |
| `smoke-tests` | Smoke testing | @Tag("smoke") | No |
| `performance-tests` | Performance testing | @Tag("performance") | No |
| `all-tests` | Complete suite | All test types | Yes |

### Configuration Files

- `application-regression-test.properties`: Regression test configuration
- `pom.xml`: Maven profiles and plugin configuration
- `BaseRegressionTest.java`: Base class for regression tests
- `TestDataManager.java`: Test data management

### Environment Variables

```bash
# Test execution configuration
TEST_PROFILE=regression-test
TEST_PARALLEL_EXECUTION=true
TEST_COVERAGE_ENABLED=true
TEST_FAIL_FAST=false

# Database configuration
DB_URL=jdbc:h2:mem:testdb
DB_USERNAME=sa
DB_PASSWORD=

# Reporting configuration
REPORT_PATH=target/test-reports
REPORT_FORMAT=HTML,JSON,XML
```

## CI/CD Integration

### GitHub Actions Workflow

The automated testing pipeline includes:

1. **Smoke Tests**: Quick validation on every PR
2. **Unit Tests**: Comprehensive unit testing with matrix execution
3. **Integration Tests**: Full integration testing with database
4. **Regression Tests**: Scheduled daily regression testing
5. **Performance Tests**: Load testing on demand
6. **Security Scan**: OWASP dependency checking
7. **Code Quality**: SonarCloud analysis
8. **Report Aggregation**: Centralized test reporting

### Triggers

- **Pull Request**: Smoke + Unit + Integration tests
- **Push to Main/Develop**: Full test suite
- **Scheduled**: Daily regression tests at 2 AM UTC
- **Manual**: On-demand execution with custom parameters

### Artifacts

- Test reports (HTML, XML, JSON)
- Coverage reports (JaCoCo)
- Performance metrics
- Security scan results
- Aggregated test dashboard

## Test Data Management

### TestDataManager

The `TestDataManager` class provides comprehensive test data management:

```java
// Setup test data for different scenarios
testDataManager.setupUnitTestData();
testDataManager.setupIntegrationTestData();
testDataManager.setupRegressionTestData();
testDataManager.setupPerformanceTestData();

// Cleanup after tests
testDataManager.cleanupTestData();
```

### Test Data Categories

1. **Basic Test Data**: Core policy information with KAN-24/KAN-25 fields
2. **Integration Test Data**: Complex scenarios with relationships
3. **Regression Test Data**: Historical bug scenarios and edge cases
4. **Performance Test Data**: Large datasets for load testing

### Data Isolation

- Each test category uses isolated data sets
- Automatic cleanup after test execution
- Unique identifiers prevent conflicts
- Database rollback for integration tests

## Reporting

### Test Reports Location

```
target/
├── test-reports/           # Custom test reports
├── surefire-reports/       # Unit test results
├── failsafe-reports/       # Integration test results
└── site/jacoco/           # Coverage reports
```

### Report Formats

- **HTML**: Interactive web reports with drill-down capabilities
- **XML**: Machine-readable for CI/CD integration
- **JSON**: Structured data for custom analysis
- **Console**: Real-time feedback during execution

### Coverage Reports

- **Line Coverage**: Minimum 80% required
- **Branch Coverage**: Tracked and reported
- **Method Coverage**: Per-class analysis
- **Package Coverage**: High-level overview

### Performance Metrics

- **Response Time**: Average, median, 95th percentile
- **Throughput**: Requests per second
- **Error Rate**: Percentage of failed requests
- **Resource Usage**: CPU, memory, database connections

## Troubleshooting

### Common Issues

#### 1. Test Failures Due to Data Issues

**Symptoms**: Tests fail with data-related errors
**Solution**: 
```bash
# Clean and reset test data
mvn clean
./scripts/run-tests.sh --type unit --verbose
```

#### 2. Out of Memory Errors

**Symptoms**: `OutOfMemoryError` during test execution
**Solution**:
```bash
# Increase memory allocation
export MAVEN_OPTS="-Xmx2048m -XX:+UseG1GC"
mvn clean test
```

#### 3. Port Conflicts in Integration Tests

**Symptoms**: `BindException` or port already in use
**Solution**:
```bash
# Use random ports or kill conflicting processes
./scripts/run-tests.sh --type integration --parallel=false
```

#### 4. Slow Test Execution

**Symptoms**: Tests taking too long to execute
**Solution**:
```bash
# Enable parallel execution
./scripts/run-tests.sh --type unit --parallel
```

### Debug Mode

Enable debug logging for troubleshooting:

```bash
# Enable verbose logging
./scripts/run-tests.sh --type unit --verbose

# Maven debug mode
mvn clean test -X -Pregression-tests
```

### Test Data Debugging

```bash
# Enable test data validation
mvn test -Dtest.data.validation.enabled=true

# Keep test data for inspection
mvn test -Dtest.data.cleanup.enabled=false
```

## Best Practices

### Writing Tests

1. **Follow AAA Pattern**: Arrange, Act, Assert
2. **Use Descriptive Names**: Test method names should explain what is being tested
3. **Test One Thing**: Each test should validate one specific behavior
4. **Use Test Data Builders**: Leverage `TestDataBuilder` for consistent test data
5. **Clean Up Resources**: Always clean up test resources

### Test Organization

1. **Group Related Tests**: Use nested test classes (`@Nested`)
2. **Use Appropriate Tags**: Tag tests by category (`@Tag`)
3. **Maintain Test Independence**: Tests should not depend on each other
4. **Mock External Dependencies**: Use mocks for external services
5. **Validate Test Coverage**: Ensure adequate coverage of business logic

### Performance Considerations

1. **Minimize Database Calls**: Use transactions and batch operations
2. **Use In-Memory Databases**: H2 for fast test execution
3. **Parallel Execution**: Enable parallel test execution where safe
4. **Resource Cleanup**: Properly clean up test resources
5. **Test Data Size**: Keep test datasets minimal but representative

## Contributing

### Adding New Tests

1. Create test class extending appropriate base class
2. Use `TestDataBuilder` for test data creation
3. Follow naming conventions (`*Test.java`, `*IntegrationTest.java`)
4. Add appropriate tags and categories
5. Update documentation if needed

### Modifying Test Configuration

1. Update Maven profiles in `pom.xml`
2. Modify properties files for environment-specific configuration
3. Update CI/CD pipeline if execution changes
4. Test changes locally before committing
5. Update this documentation

## Support

For questions or issues with the testing suite:

1. Check this documentation first
2. Review existing test examples
3. Check CI/CD pipeline logs
4. Create issue in project repository
5. Contact development team

---

**Last Updated**: October 2025  
**Version**: 1.0  
**Maintained By**: Zurich Development Team