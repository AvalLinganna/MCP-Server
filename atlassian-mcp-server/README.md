# Atlassian MCP Server

A comprehensive Model Context Protocol (MCP) server for Atlassian Jira and Confluence integration, providing seamless project management and documentation capabilities.

## 🚀 Overview

This MCP server enables AI agents and VS Code extensions to interact with Atlassian products (Jira and Confluence) and fetch Swagger/OpenAPI documentation through a standardized interface. It provides 13 powerful tools for complete project lifecycle management and API documentation retrieval.

## 📋 Features

- **Complete Jira Integration**: Issue management, project information, and workflow automation
- **Full Confluence Support**: Documentation creation, page management, and content retrieval
- **Swagger/OpenAPI Tools**: Fetch, search, and parse API documentation from any URL
- **Image Processing**: Read, download, and analyze images from URLs, Confluence, and Jira
- **MCP Standard Compliance**: Compatible with VS Code and other MCP clients
- **Secure Authentication**: Basic Auth with API token support
- **Error Handling**: Comprehensive error messages and debugging logs
- **Flexible Configuration**: Environment-based configuration for multiple environments

## 🛠️ Available Tools (18 Total)

### 📊 Jira Tools (6 Tools)

#### 1. `get_jira_issues`
**Description**: Get issues from Jira project with advanced filtering
- **Parameters**:
  - `jql` (string): JQL query to filter issues (default: project = "KAN")
  - `maxResults` (number): Maximum number of results (default: 50)
  - `fields` (string): Comma-separated fields to return
- **Use Case**: Query and filter Jira issues with custom JQL queries

#### 2. `get_user_stories`
**Description**: Get user stories from Jira project
- **Parameters**:
  - `maxResults` (number): Maximum number of user stories (default: 50)
- **Use Case**: Specifically retrieve user story type issues for sprint planning

#### 3. `create_jira_issue`
**Description**: Create a new issue in Jira project
- **Parameters**:
  - `summary` (string, required): Issue summary/title
  - `description` (string, required): Issue description
  - `issueType` (string): Issue type - Story, Bug, Task, etc. (default: "Story")
  - `priority` (string): Issue priority (default: "Medium")
- **Use Case**: Create new Jira tickets programmatically

#### 4. `get_project_info`
**Description**: Get information about the Jira project
- **Parameters**: None
- **Use Case**: Retrieve project metadata, key, and configuration details

#### 5. `get_jira_issue`
**Description**: Get a specific Jira issue by key or ID
- **Parameters**:
  - `issueKey` (string, required): Issue key or ID (e.g., KAN-1, KAN-2, or 10066)
  - `fields` (string): Comma-separated fields to return
- **Use Case**: Fetch detailed information about specific issues

#### 6. `update_jira_issue`
**Description**: Update a Jira issue (status, assignee, comments, etc.)
- **Parameters**:
  - `issueKey` (string, required): Issue key or ID
  - `transition` (string): Status transition (e.g., "Done", "In Progress", "To Do")
  - `assignee` (string): Assignee account ID or email
  - `comment` (string): Comment to add to the issue
  - `summary` (string): Update issue summary/title
  - `description` (string): Update issue description
  - `priority` (string): Update issue priority (Highest, High, Medium, Low, Lowest)
  - `labels` (array): Array of labels to set on the issue
- **Use Case**: Modify existing Jira issues, change status, add comments

### 📚 Confluence Tools (4 Tools)

#### 7. `get_confluence_pages`
**Description**: Get pages from Confluence space
- **Parameters**:
  - `spaceKey` (string): Space key to search in (default: "KAN")
  - `title` (string): Page title to search for (optional)
  - `limit` (number): Maximum number of pages (default: 25)
- **Use Case**: List and search for Confluence pages in a space

#### 8. `create_confluence_page`
**Description**: Create a new page in Confluence
- **Parameters**:
  - `title` (string, required): Page title
  - `content` (string, required): Page content in Confluence storage format or plain text
  - `spaceKey` (string): Space key where to create the page (default: "KAN")
  - `parentId` (string): Parent page ID for hierarchical organization (optional)
- **Use Case**: Create new documentation pages programmatically

#### 9. `update_confluence_page`
**Description**: Update an existing Confluence page
- **Parameters**:
  - `pageId` (string, required): Page ID to update
  - `content` (string, required): New page content
  - `title` (string): New page title (optional)
  - `version` (number): Current page version number (auto-detected if not provided)
- **Use Case**: Modify existing documentation with automatic version control

#### 10. `get_confluence_page`
**Description**: Get a specific Confluence page by ID or title
- **Parameters**:
  - `pageId` (string): Page ID
  - `title` (string): Page title (alternative to pageId)
  - `spaceKey` (string): Space key (required when using title)
  - `expand` (string): Properties to expand (default: "body.storage,version,space")
- **Use Case**: Retrieve detailed page content and metadata

### 🔗 Swagger/OpenAPI Tools (3 Tools)

#### 11. `fetch_swagger_spec`
**Description**: Fetch Swagger/OpenAPI specification from a URL
- **Parameters**:
  - `url` (string, required): URL to the Swagger/OpenAPI specification
  - `headers` (object): Optional HTTP headers for authentication
  - `format` (string): Expected format - json, yaml, or auto (default: auto)
- **Use Case**: Retrieve API documentation from SwaggerHub, internal APIs, or public specs

#### 12. `search_swagger_specs`
**Description**: Search for Swagger/OpenAPI specifications at common paths
- **Parameters**:
  - `baseUrl` (string, required): Base URL to search for specifications
  - `customPaths` (array): Additional custom paths to check beyond common ones
- **Use Case**: Discover API documentation locations when exact URL is unknown

#### 13. `parse_swagger_spec`
**Description**: Parse and extract key information from a Swagger/OpenAPI specification
- **Parameters**:
  - `content` (string/object, required): Swagger specification content
  - `url` (string): Optional URL where the specification was retrieved from
- **Use Case**: Analyze API specifications to understand endpoints, models, and capabilities

### 🖼️ Image Processing Tools (5 Tools)

#### 14. `read_image_from_url`
**Description**: Read and analyze an image from a URL, extracting metadata and basic information
- **Parameters**:
  - `url` (string, required): The URL of the image to read and analyze
  - `includeMetadata` (boolean): Whether to include detailed metadata (default: true)
  - `maxSize` (number): Maximum file size in MB to download (default: 10MB)
- **Use Case**: Analyze images before downloading, get metadata and size information

#### 15. `download_image`
**Description**: Download an image from a URL and save it to a specified location
- **Parameters**:
  - `url` (string, required): The URL of the image to download
  - `fileName` (string): The name to save the file as (optional)
  - `downloadPath` (string): The directory path to save the image (default: current directory)
  - `maxSize` (number): Maximum file size in MB to download (default: 50MB)
  - `overwrite` (boolean): Whether to overwrite existing files (default: false)
- **Use Case**: Download images from web sources with size limits and overwrite protection

#### 16. `read_image_from_confluence`
**Description**: Read and download images attached to a Confluence page
- **Parameters**:
  - `pageId` (string, required): The Confluence page ID to read images from
  - `downloadPath` (string): The directory path to save images (default: ./confluence-images)
  - `includeMetadata` (boolean): Whether to include detailed metadata (default: true)
- **Use Case**: Extract all images from Confluence documentation pages

#### 17. `extract_images_from_jira`
**Description**: Extract and download images from Jira issue attachments
- **Parameters**:
  - `issueKey` (string, required): The Jira issue key (e.g., PROJ-123)
  - `downloadPath` (string): The directory path to save images (default: ./jira-images)
  - `imageTypes` (array): Array of image file extensions to filter (default: jpg, jpeg, png, gif, webp, svg, bmp)
- **Use Case**: Download screenshots and images from bug reports and user stories

#### 18. `get_image_info`
**Description**: Get detailed information about an image file without downloading it
- **Parameters**:
  - `url` (string, required): The URL of the image to analyze
  - `headers` (object): Additional headers to send with the request
- **Use Case**: Check image properties, size, and accessibility before processing

### Installation & Setup

### Prerequisites
- Node.js (v16 or higher)
- Atlassian Cloud account with API access
- Jira project and Confluence space

### 1. Install Dependencies
```bash
npm install @modelcontextprotocol/sdk node-fetch dotenv
```

### 2. Environment Configuration
Create a `.env` file in the project root:

```env
# Jira Configuration
JIRA_BASE_URL=https://your-domain.atlassian.net
JIRA_API_TOKEN=your_jira_api_token
JIRA_PROJECT_KEY=KAN
JIRA_PROJECT_NAME=your_project_name
JIRA_EMAIL=your_email@domain.com

# Confluence Configuration
CONFLUENCE_BASE_URL=https://your-domain.atlassian.net
CONFLUENCE_EMAIL=your_email@domain.com
CONFLUENCE_API_TOKEN=your_confluence_api_token
CONFLUENCE_SPACE_KEY=your_space_key

# MCP Server Configuration
MCP_SERVER_NAME=Atlassian-mcp-server
MCP_SERVER_VERSION=1.0.0
LOG_LEVEL=info
```

### 3. Generate API Tokens
1. Go to [Atlassian Account Settings](https://id.atlassian.com/manage-profile/security/api-tokens)
2. Create API tokens for Jira and Confluence
3. Add tokens to your `.env` file

## 🚀 Usage

### Start the MCP Server
```bash
node index.js
```

### VS Code Integration
Add to your VS Code `settings.json`:
```json
{
  "mcp.mcpServers": {
    "atlassian": {
      "command": "node",
      "args": ["path/to/atlassian-mcp-server/index.js"]
    }
  }
}
```

## 📊 Example Use Cases

### Jira Workflows
```javascript
// Get all issues in the project
await get_jira_issues({ jql: "project = KAN" });

// Create a new bug report
await create_jira_issue({
  summary: "Login button not working",
  description: "Users cannot log in after clicking the login button",
  issueType: "Bug",
  priority: "High"
});

// Update issue status and add comment
await update_jira_issue({
  issueKey: "KAN-13",
  transition: "Done",
  comment: "Issue resolved after implementing gender field"
});
```

### Confluence Documentation
```javascript
// Get policy management page
await get_confluence_page({
  title: "Policy Management",
  spaceKey: "your_space_key"
});

// Create new documentation
await create_confluence_page({
  title: "API Documentation",
  content: "<h1>API Guide</h1><p>This page contains API documentation...</p>",
  spaceKey: "DOCS"
});

// Update existing page
await update_confluence_page({
  pageId: "123456",
  content: "<h1>Updated Content</h1><p>New information...</p>"
});
```

### Swagger/OpenAPI Documentation
```javascript
// Fetch the SwaggerHub API mentioned in Confluence
await fetch_swagger_spec({
  url: "https://app.swaggerhub.com/apis/capgemini-5bf/policy-and-endorsement-api/1.0.0",
  headers: {
    "Authorization": "Bearer your-swaggerhub-token"
  }
});

// Search for API documentation on a domain
await search_swagger_specs({
  baseUrl: "https://api.zurich.com"
});

// Parse retrieved API specification
await parse_swagger_spec({
  content: swaggerJsonContent,
  url: "https://api.example.com/swagger.json"
});
```

## 🏗️ Architecture

### Server Structure
```
atlassian-mcp-server/
├── index.js                 # Main MCP server implementation
├── package.json             # Dependencies and scripts
├── .env                     # Environment configuration
├── README.md               # This documentation
└── scripts/
    ├── get-policy-page.js   # Confluence policy retrieval
    ├── get-all-issues.js    # Jira issue listing
    └── test-update.js       # Issue update testing
```

### Key Components
- **JiraMCPServer Class**: Main server implementation
- **Tool Handlers**: Individual tool implementations
- **API Helpers**: `makeJiraRequest()` and `makeConfluenceRequest()`
- **Error Handling**: Comprehensive error management and logging

## 🔐 Security Features

- **API Token Authentication**: Secure token-based authentication
- **Environment Variables**: Sensitive data stored in environment variables
- **Request Validation**: Input validation and sanitization
- **Error Handling**: Secure error messages without exposing sensitive data

## 📝 Implementation Examples

### KAN-13 Policy Management Implementation
This server was used to implement KAN-13 (Policy holder gender field):

1. **Backend Enhancement**: Added gender field to PolicySummary.java
2. **Test Coverage**: Updated TestDataBuilder with gender support methods
3. **Documentation**: Updated Confluence policy pages
4. **Issue Tracking**: Managed implementation through Jira workflows

### Real-World Usage
- **Automated Issue Creation**: Create issues from external systems
- **Status Automation**: Update issue status based on deployment pipelines
- **Documentation Sync**: Keep Confluence pages updated with code changes
- **Project Reporting**: Generate reports from Jira data

## 🐛 Troubleshooting

### Common Issues

1. **Authentication Errors**
   - Verify API tokens are valid
   - Check email addresses match Atlassian account
   - Ensure proper permissions for spaces/projects

2. **Space/Project Not Found**
   - Verify space keys and project keys
   - Check user permissions
   - Confirm URLs are correct

3. **Tool Execution Errors**
   - Check required parameters are provided
   - Verify issue keys and page IDs exist
   - Review error logs for specific issues

### Debug Mode
Enable detailed logging by setting `LOG_LEVEL=debug` in your `.env` file.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/new-tool`)
3. Commit your changes (`git commit -am 'Add new tool'`)
4. Push to the branch (`git push origin feature/new-tool`)
5. Create a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Support

For support and questions:
- Create an issue in the repository
- Check the troubleshooting section
- Review Atlassian API documentation
- Verify MCP standard compliance

## 📚 Additional Resources

- [Model Context Protocol Documentation](https://modelcontextprotocol.io/)
- [Atlassian Developer Documentation](https://developer.atlassian.com/)
- [Jira REST API Reference](https://developer.atlassian.com/cloud/jira/platform/rest/v3/)
- [Confluence REST API Reference](https://developer.atlassian.com/cloud/confluence/rest/v1/)

---

**Built with ❤️ for seamless Atlassian integration**

*Last updated: October 16, 2025*

## 🔗 New Swagger/OpenAPI Integration

The MCP server now includes comprehensive Swagger/OpenAPI documentation tools! See [SWAGGER_TOOLS.md](./SWAGGER_TOOLS.md) for detailed documentation and examples of the new tools:

- `fetch_swagger_spec` - Fetch API specifications from URLs
- `search_swagger_specs` - Discover API documentation locations  
- `parse_swagger_spec` - Analyze and extract API information

Perfect for implementing APIs like the KAN-27 Endorsement API using specifications from Confluence documentation!

## 🖼️ New Image Processing Features

The MCP server now includes powerful image processing capabilities! See [IMAGE_SERVICE_README.md](./IMAGE_SERVICE_README.md) for detailed documentation and examples of the new image tools:

- `read_image_from_url` - Analyze images from URLs with metadata extraction
- `download_image` - Download images with size limits and overwrite protection
- `read_image_from_confluence` - Extract all images from Confluence pages
- `extract_images_from_jira` - Download images from Jira issue attachments
- `get_image_info` - Get image information without downloading

Perfect for extracting screenshots from bug reports, downloading documentation images, and analyzing visual content!