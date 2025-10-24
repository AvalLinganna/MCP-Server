export const confluenceToolSchemas = [
  {
    name: 'get_confluence_pages',
    description: 'Get pages from Confluence space',
    inputSchema: {
      type: 'object',
      properties: {
        spaceKey: {
          type: 'string',
          description: 'Space key to search in'  
                 },
        title: {
          type: 'string',
          description: 'Page title to search for (optional)'
        },
        limit: {
          type: 'number',
          description: 'Maximum number of pages to return',
          default: 25
        }
      }
    },
  },
  {
    name: 'create_confluence_page',
    description: 'Create a new page in Confluence',
    inputSchema: {
      type: 'object',
      properties: {
        spaceKey: {
          type: 'string',
          description: 'Space key where to create the page'
        },
        title: {
          type: 'string',
          description: 'Page title'
        },
        content: {
          type: 'string',
          description: 'Page content in Confluence storage format or plain text'
        },
        parentId: {
          type: 'string',
          description: 'Parent page ID (optional)'
        }
      },
      required: ['title', 'content']
    },
  },
  {
    name: 'update_confluence_page',
    description: 'Update an existing Confluence page',
    inputSchema: {
      type: 'object',
      properties: {
        pageId: {
          type: 'string',
          description: 'Page ID to update'
        },
        title: {
          type: 'string',
          description: 'New page title (optional)'
        },
        content: {
          type: 'string',
          description: 'New page content'
        },
        version: {
          type: 'number',
          description: 'Current page version number (auto-detected if not provided)'
        }
      },
      required: ['pageId', 'content']
    },
  },
  {
    name: 'get_confluence_page',
    description: 'Get a specific Confluence page by ID or title',
    inputSchema: {
      type: 'object',
      properties: {
        pageId: {
          type: 'string',
          description: 'Page ID'
        },
        title: {
          type: 'string',
          description: 'Page title (alternative to pageId)'
        },
        spaceKey: {
          type: 'string',
          description: 'Space key (required when using title)'
        },
        expand: {
          type: 'string',
          description: 'Comma-separated list of properties to expand',
          default: 'body.storage,version,space'
        }
      }
    },
  },
  {
    name: 'get_specific_confluence_page',
    description: 'Get specific Confluence pages like Policy Management, Claim Management, etc.',
    inputSchema: {
      type: 'object',
      properties: {
        pageName: {
          type: 'string',
          description: 'Specific page name to fetch',
          enum: ['Policy Management', 'Claim Management', 'API Documentation', 'User Guide', 'Technical Specifications']
        },
        spaceKey: {
          type: 'string',
          description: 'Space key to search in'
        },
        includeContent: {
          type: 'boolean',
          description: 'Include full page content in response',
          default: true
        },
        format: {
          type: 'string',
          description: 'Response format: "detailed" or "summary"',
          enum: ['detailed', 'summary'],
          default: 'detailed'
        }
      },
      required: ['pageName']
    },
  },
  {
    name: 'get_all_spaces_content',
    description: 'Get content from all configured Confluence spaces',
    inputSchema: {
      type: 'object',
      properties: {
        limit: {
          type: 'number',
          description: 'Maximum number of pages per space',
          default: 10
        },
        includeContent: {
          type: 'boolean',
          description: 'Include page content in response',
          default: false
        }
      }
    },
  },
  {
    name: 'search_across_spaces',
    description: 'Search for content across multiple Confluence spaces',
    inputSchema: {
      type: 'object',
      properties: {
        query: {
          type: 'string',
          description: 'Search query text'
        },
        spaceKeys: {
          type: 'array',
          items: {
            type: 'string'
          },
          description: 'Array of space keys to search in (optional, uses all configured spaces if not provided)'
        },
        limit: {
          type: 'number',
          description: 'Maximum number of results per space',
          default: 5
        }
      },
      required: ['query']
    },
  }
];