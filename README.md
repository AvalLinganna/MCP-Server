# Zurich UK Test Projects Workspace

This workspace contains multiple projects and proof-of-concepts (POCs) for Zurich Insurance UK operations. It includes various technology stacks and integration patterns for modern insurance systems.

## Projects Overview

### 🧪 [api-test-suite](./api-test-suite/)
A comprehensive Java-based API testing framework using TestNG, RestAssured, and Extent Reports for automated testing of insurance APIs.

**Technology Stack:**
- Java with Maven
- TestNG for test framework
- RestAssured for API testing
- Extent Reports for test reporting
- Log4j for logging

### 🔧 [atlassian-mcp-server](./atlassian-mcp-server/)
A Node.js Model Context Protocol (MCP) server for integrating with Atlassian tools (Jira and Confluence) to facilitate automated workflows and documentation.

**Technology Stack:**
- Node.js/JavaScript
- Model Context Protocol (MCP)
- Atlassian REST APIs (Jira & Confluence)

### 🌀 [zurich-mule-poc](./zurich-mule-poc/)
A MuleSoft Mule 4 application providing REST endpoints for policy management operations with enterprise-grade security and monitoring.

**Technology Stack:**
- MuleSoft Mule 4.8
- REST APIs with RAML specifications
- TLS/SSL security
- MUnit testing

### 🍃 [zurich-spring-poc](./zurich-spring-poc/)
A Spring Boot application demonstrating modern Java development patterns for insurance domain services.

**Technology Stack:**
- Spring Boot
- Java with Maven
- REST APIs
- Comprehensive testing framework

## Getting Started

Each project has its own README file with specific setup instructions. Choose the project you want to work with:

```bash
# For API Testing
cd api-test-suite

# For Atlassian Integration
cd atlassian-mcp-server

# For MuleSoft Integration
cd zurich-mule-poc

# For Spring Boot Services
cd zurich-spring-poc
```

## Prerequisites

### General Requirements
- **Java 8 or 11** (for Java-based projects)
- **Node.js 16+** (for Node.js projects)
- **Maven 3.6+** (for Java projects)
- **Git** for version control

### IDE Recommendations
- **IntelliJ IDEA** or **Eclipse** for Java projects
- **Anypoint Studio** for MuleSoft projects
- **VS Code** for Node.js and general development

## Development Guidelines

### Code Quality
- Follow language-specific coding standards
- Maintain comprehensive test coverage
- Use meaningful commit messages
- Document API changes

### Security
- Never commit sensitive credentials
- Use environment variables for configuration
- Follow security best practices for each technology stack
- Regular dependency updates and vulnerability scanning

### Testing
- Unit tests for all business logic
- Integration tests for API endpoints
- End-to-end tests for critical user journeys
- Performance testing for production readiness

## Architecture Patterns

This workspace demonstrates several architectural patterns:

- **Microservices**: Independent, deployable services
- **API-First Design**: RAML/OpenAPI specifications
- **Event-Driven Architecture**: Asynchronous communication patterns
- **Test Automation**: Comprehensive testing strategies
- **DevOps Integration**: CI/CD pipeline compatibility

## Documentation

Each project contains detailed documentation:

- `README.md` - Project overview and setup
- `docs/` - Additional technical documentation
- API specifications (RAML/OpenAPI)
- Architecture diagrams and business requirements

## Contributing

1. **Fork the repository** (if applicable)
2. **Create a feature branch**: `git checkout -b feature/your-feature-name`
3. **Follow project-specific guidelines** in each project's README
4. **Write tests** for new functionality
5. **Update documentation** as needed
6. **Submit a pull request** with clear description

## Project Structure

```
test3/
├── .github/                 # GitHub workflows and templates
├── .vscode/                 # VS Code workspace settings
├── api-test-suite/          # Java API testing framework
├── atlassian-mcp-server/    # Node.js MCP server
├── zurich-mule-poc/         # MuleSoft Mule application
├── zurich-spring-poc/       # Spring Boot application
├── package.json             # Workspace dependencies
└── README.md                # This file
```

## Scripts and Automation

Common workspace-level scripts:

```bash
# Install all Node.js dependencies
npm install

# Run all tests (where applicable)
npm run test:all

# Lint all projects
npm run lint:all

# Build all projects
npm run build:all
```

## Environment Setup

### Development Environment
1. Clone the workspace repository
2. Install required tools (Java, Node.js, Maven)
3. Set up IDE configurations
4. Install project-specific dependencies

### CI/CD Integration
- GitHub Actions workflows in `.github/workflows/`
- Docker configurations for containerized deployments
- Environment-specific configuration management

## Support and Resources

### Internal Resources
- Zurich Insurance development guidelines
- Architecture review board documentation
- Security compliance requirements

### External Resources
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [MuleSoft Documentation](https://docs.mulesoft.com/)
- [TestNG Documentation](https://testng.org/doc/)
- [Model Context Protocol](https://modelcontextprotocol.io/)

## License

This workspace contains proprietary code for Zurich Insurance Group. All rights reserved.

## Contact

For questions or support:
- Technical Lead: [Contact Information]
- Architecture Team: [Contact Information]
- DevOps Team: [Contact Information]

---

**Last Updated:** October 2025  
**Workspace Version:** 1.0.0