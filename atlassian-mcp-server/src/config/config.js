import dotenv from 'dotenv';

// Load environment variables
dotenv.config();

export const jiraConfig = {
  baseUrl: process.env.JIRA_BASE_URL,
  apiToken: process.env.JIRA_API_TOKEN,
  projectKey: process.env.JIRA_PROJECT_KEY,
  projectName: process.env.JIRA_PROJECT_NAME,
  email: process.env.JIRA_EMAIL 
};

export const confluenceConfig = {
  baseUrl: process.env.CONFLUENCE_BASE_URL,
  apiToken: process.env.CONFLUENCE_API_TOKEN,
  email: process.env.CONFLUENCE_EMAIL,
  spaceKey: process.env.CONFLUENCE_SPACE_KEY, // Default space key
  spaceName: process.env.CONFLUENCE_SPACE_NAME
 };

export const swaggerConfig = {
  swaggerhub: {
    token: process.env.SWAGGERHUB_API_TOKEN,
    defaultOwner: process.env.SWAGGERHUB_DEFAULT_OWNER || 'capgemini-5bf'
  }
};

export const serverConfig = {
  name: process.env.MCP_SERVER_NAME || 'Atlassian-mcp-server',
  version: process.env.MCP_SERVER_VERSION || '1.0.0',
  port: process.env.MCP_SERVER_PORT || 3001,
  logLevel: process.env.LOG_LEVEL || 'info'
};