# 🧪 MCP SERVER TEST SUITE DOCUMENTATION

This directory contains comprehensive test scripts for all 18 tools in the Atlassian MCP Server.

## 📁 Test Files Overview

### Individual Tool Test Scripts

1. **`imageServiceTest.js`** - Tests all 5 Image Processing Tools
   - `read_image_from_url` - Analyze images from URLs with metadata
   - `download_image` - Download images with size limits and protection
   - `read_image_from_confluence` - Extract images from Confluence pages
   - `extract_images_from_jira` - Download images from Jira attachments
   - `get_image_info` - Get image metadata without downloading

2. **`jiraToolsTest.js`** - Tests all 6 Jira Integration Tools
   - `get_jira_issues` - Retrieve issues with JQL filtering
   - `get_user_stories` - Get user story type issues
   - `create_jira_issue` - Create new issues programmatically
   - `get_project_info` - Get project metadata and configuration
   - `get_jira_issue` - Fetch specific issue details
   - `update_jira_issue` - Update issues, status, comments, labels

3. **`confluenceToolsTest.js`** - Tests all 7 Confluence Documentation Tools
   - `get_confluence_pages` - List and search pages in spaces
   - `create_confluence_page` - Create new documentation pages
   - `update_confluence_page` - Modify existing pages with version control
   - `get_confluence_page` - Retrieve specific page content
   - `get_specific_confluence_page` - Get predefined page types
   - `get_all_spaces_content` - Content from multiple spaces
   - `search_across_spaces` - Cross-space content search

4. **`swaggerToolsTest.js`** - Tests all 4 Swagger/OpenAPI Tools
   - `fetch_swagger_spec` - Download API specifications from URLs
   - `search_swagger_specs` - Discover API documentation locations
   - `parse_swagger_spec` - Analyze and extract API information
   - `fetch_swaggerhub_spec` - Access SwaggerHub private APIs

### Master Test Runner

5. **`masterTestRunner.js`** - Comprehensive Test Suite
   - Runs all individual test suites sequentially
   - Provides detailed reporting and success metrics
   - Calculates overall system health
   - Generates troubleshooting recommendations

### Utility Test Scripts

6. **`simpleTest.js`** - Quick connectivity test
7. **`demonstrateImageTools.js`** - Image functionality demonstration

## 🚀 How to Run Tests

### Run Individual Test Suites

```bash
# Test Image Processing Tools (5 tools)
node test/imageServiceTest.js

# Test Jira Integration Tools (6 tools) 
node test/jiraToolsTest.js

# Test Confluence Documentation Tools (7 tools)
node test/confluenceToolsTest.js

# Test Swagger/OpenAPI Tools (4 tools)
node test/swaggerToolsTest.js
```

### Run Complete Test Suite

```bash
# Run all 18 tools with comprehensive reporting
node test/masterTestRunner.js
```

### Quick Connectivity Test

```bash
# Simple test to verify basic connectivity
node test/simpleTest.js
```

## 📊 Expected Test Results

### ✅ Successful Test Output
- **Image Tools**: Should pass if internet connectivity is available
- **Jira Tools**: Requires valid Jira configuration in .env
- **Confluence Tools**: Requires valid Confluence configuration in .env  
- **Swagger Tools**: Mix of public APIs (should work) and private APIs (requires tokens)

### ❌ Common Failure Scenarios
1. **Authentication Errors**: Invalid API tokens in .env file
2. **Permission Errors**: User lacks access to specified Jira project or Confluence space
3. **Network Errors**: Connectivity issues to Atlassian services
4. **Configuration Errors**: Incorrect URLs or space/project keys

## 🔧 Prerequisites and Configuration

### Required Environment Variables (.env file)

```env
# Jira Configuration
JIRA_BASE_URL=https://your-domain.atlassian.net
JIRA_API_TOKEN=your_jira_api_token
JIRA_PROJECT_KEY=YOUR_PROJECT_KEY
JIRA_PROJECT_NAME=Your Project Name
JIRA_EMAIL=your-email@domain.com

# Confluence Configuration  
CONFLUENCE_BASE_URL=https://your-domain.atlassian.net
CONFLUENCE_EMAIL=your-email@domain.com
CONFLUENCE_API_TOKEN=your_confluence_api_token
CONFLUENCE_SPACE_KEY=YOUR_SPACE_KEY
CONFLUENCE_SPACE_NAME=Your Space Name

# Optional: SwaggerHub Configuration
SWAGGERHUB_API_TOKEN=your_swaggerhub_token
SWAGGERHUB_DEFAULT_OWNER=your-organization

# MCP Server Configuration
MCP_SERVER_NAME=Atlassian-mcp-server
MCP_SERVER_VERSION=1.0.0
LOG_LEVEL=info
```

### API Token Generation
1. Go to [Atlassian Account Settings](https://id.atlassian.com/manage-profile/security/api-tokens)
2. Create API tokens for both Jira and Confluence
3. Add tokens to your .env file

## 📈 Test Reporting Features

### Individual Test Results
- ✅ Pass/Fail status for each tool
- 📝 Detailed error messages for failures
- 📊 Success rate calculations
- 🔍 Response content previews

### Master Test Suite Results
- 📈 Overall success rate across all 18 tools
- 📋 Category-wise performance (Image, Jira, Confluence, Swagger)
- ⏱️ Execution duration tracking
- 💡 Troubleshooting recommendations
- 🔧 Configuration validation suggestions

## 🎯 Test Scenarios Covered

### Functional Testing
- **CRUD Operations**: Create, read, update operations for Jira/Confluence
- **Search and Filter**: Various search and filtering capabilities
- **File Operations**: Image download, upload, and metadata extraction
- **API Integration**: External API documentation fetching and parsing

### Error Handling Testing  
- **Authentication Failures**: Invalid credentials handling
- **Permission Denied**: Access control validation
- **Network Issues**: Connectivity problem handling
- **Invalid Input**: Parameter validation testing

### Integration Testing
- **Cross-Service Workflows**: Jira ↔ Confluence integration
- **Image Processing Pipelines**: Multi-step image operations
- **API Documentation Workflows**: Fetch → Parse → Document cycle

## 🧹 Test Data Management

### Created Test Data
The tests may create:
- **Jira Issues**: Test issues with `[TEST]` prefix
- **Confluence Pages**: Test pages with timestamps
- **Downloaded Images**: Sample images in test directories

### Cleanup Recommendations
1. Delete test Jira issues after validation
2. Remove test Confluence pages
3. Clean up downloaded test images
4. Review and remove test labels/comments

## 🔍 Debugging Failed Tests

### Step 1: Check Configuration
```bash
# Verify environment variables are loaded
node -e "require('dotenv').config(); console.log(process.env.JIRA_BASE_URL);"
```

### Step 2: Test Individual Components
```bash
# Test specific failing component
node test/simpleTest.js
```

### Step 3: Enable Debug Logging
Set `LOG_LEVEL=debug` in .env file for detailed request/response logging.

### Step 4: Check Network Connectivity
```bash
# Test basic connectivity to Atlassian
curl -I https://your-domain.atlassian.net
```

## 📚 Integration with VS Code

These test scripts can be used with VS Code's integrated terminal:

1. Open VS Code in the MCP server directory
2. Use `Ctrl+`` to open integrated terminal
3. Run any test script using the commands above
4. Review results in the terminal output

## 🎉 Success Criteria

### 100% Success (All 18 tools working)
- All APIs accessible and authenticated correctly
- All tools responding with expected data formats
- Image processing working with external URLs
- Swagger/OpenAPI tools can fetch and parse specifications

### Acceptable Success (>80% tools working)
- Core Jira and Confluence functionality operational
- Minor issues with specialized tools (SwaggerHub, specific image sources)
- Most integration workflows functional

### Requires Investigation (<80% tools working)
- Significant configuration or connectivity issues
- Authentication problems across multiple services
- Network or permission restrictions blocking API access

---

**Last Updated**: October 20, 2025  
**Version**: 1.0.0  
**Total Tools Tested**: 18 (5 Image + 6 Jira + 7 Confluence + 4 Swagger)