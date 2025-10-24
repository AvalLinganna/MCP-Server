import { Server } from '@modelcontextprotocol/sdk/server/index.js';
import { StdioServerTransport } from '@modelcontextprotocol/sdk/server/stdio.js';
import {
  CallToolRequestSchema,
  ErrorCode,
  ListToolsRequestSchema,
  McpError,
} from '@modelcontextprotocol/sdk/types.js';

// Import configurations
import { serverConfig } from './src/config/config.js';

// Import tool schemas
import { jiraToolSchemas } from './src/schemas/jiraSchemas.js';
import { confluenceToolSchemas } from './src/schemas/confluenceSchemas.js';
import { swaggerToolSchemas } from './src/schemas/swaggerSchemas.js';
import { imageToolSchemas } from './src/schemas/imageSchemas.js';
import { gitToolSchemas } from './src/schemas/gitSchemas.js';

// Import services
import { JiraService } from './src/services/jiraService.js';
import { ConfluenceService } from './src/services/confluenceService.js';
import { SwaggerService } from './src/services/swaggerService.js';
import { ImageService } from './src/services/imageService.js';
import { GitService } from './src/services/gitService.js';

class AtlassianMCPServer {
  constructor() {
    this.server = new Server(
      {
        name: serverConfig.name,
        version: serverConfig.version,
      },
      {
        capabilities: {
          tools: {},
        },
      }
    );

    this.setupToolHandlers();
  }

  setupToolHandlers() {
    this.server.setRequestHandler(ListToolsRequestSchema, async () => ({
      tools: [
        ...jiraToolSchemas,
        ...confluenceToolSchemas,
        ...swaggerToolSchemas,
  ...imageToolSchemas,
  ...gitToolSchemas
      ],
    }));

    this.server.setRequestHandler(CallToolRequestSchema, async (request) => {
      const { name, arguments: args } = request.params;

      try {
        switch (name) {
          // Git tools
          case 'git_add':
            return await GitService.gitAdd(args);
          case 'git_commit':
            return await GitService.gitCommit(args);
          case 'git_merge':
            return await GitService.gitMerge(args);
          case 'git_pull':
            return await GitService.gitPull(args);
          case 'git_push':
            return await GitService.gitPush(args);
          case 'git_status':
            return await GitService.gitStatus(args);
          case 'git_checkout':
            return await GitService.gitCheckout(args);
          case 'git_log':
            return await GitService.gitLog(args);
          case 'git_diff':
            return await GitService.gitDiff(args);
          case 'get_jira_issues':
            return await JiraService.getJiraIssues(args);
          case 'get_user_stories':
            return await JiraService.getUserStories(args);
          case 'create_jira_issue':
            return await JiraService.createJiraIssue(args);
          case 'get_project_info':
            return await JiraService.getProjectInfo();
          case 'get_jira_issue':
            return await JiraService.getJiraIssue(args);
          case 'update_jira_issue':
            return await JiraService.updateJiraIssue(args);
          case 'get_confluence_pages':
            return await ConfluenceService.getConfluencePages(args);
          case 'create_confluence_page':
            return await ConfluenceService.createConfluencePage(args);
          case 'update_confluence_page':
            return await ConfluenceService.updateConfluencePage(args);
          case 'get_confluence_page':
            return await ConfluenceService.getConfluencePage(args);
          case 'get_specific_confluence_page':
            return await ConfluenceService.getSpecificConfluencePage(args);
          case 'get_all_spaces_content':
            return await ConfluenceService.getAllSpacesContent(args);
          case 'search_across_spaces':
            return await ConfluenceService.searchAcrossSpaces(args);
          case 'fetch_swagger_spec':
            return await SwaggerService.fetchSwaggerSpec(args);
          case 'fetch_swaggerhub_spec':
            return await SwaggerService.fetchSwaggerHubSpec(args);
          case 'search_swagger_specs':
            return await SwaggerService.searchSwaggerSpecs(args);
          case 'parse_swagger_spec':
            return await SwaggerService.parseSwaggerSpec(args);
          case 'read_image_from_url':
            return await ImageService.readImageFromUrl(args);
          case 'download_image':
            return await ImageService.downloadImage(args);
          case 'read_image_from_confluence':
            return await ImageService.readImageFromConfluence(args);
          case 'extract_images_from_jira':
            return await ImageService.extractImagesFromJira(args);
          case 'get_image_info':
            return await ImageService.getImageInfo(args);
          default:
            throw new McpError(ErrorCode.MethodNotFound, `Unknown tool: ${name}`);
        }
      } catch (error) {
        throw new McpError(ErrorCode.InternalError, `Tool execution failed: ${error.message}`);
      }
    });
  }

  async run() {
    const transport = new StdioServerTransport();
    await this.server.connect(transport);
    console.error('Atlassian MCP server running on stdio');
  }
}

const server = new AtlassianMCPServer();
server.run().catch(console.error);
