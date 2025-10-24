export const swaggerToolSchemas = [
  {
    name: 'fetch_swagger_spec',
    description: 'Fetch Swagger/OpenAPI specification from a URL with authentication support. Supports Bearer tokens, Basic Auth, API keys, and SwaggerHub-specific authentication.',
    inputSchema: {
      type: 'object',
      properties: {
        url: {
          type: 'string',
          description: 'URL to the Swagger/OpenAPI specification (e.g., https://api.example.com/swagger.json)'
        },
        headers: {
          type: 'object',
          description: 'Optional HTTP headers to include in the request',
          additionalProperties: {
            type: 'string'
          }
        },
        auth: {
          type: 'object',
          description: 'Authentication configuration',
          properties: {
            type: {
              type: 'string',
              enum: ['bearer', 'jwt', 'basic', 'apikey', 'api-key', 'cookie', 'oauth', 'swaggerhub'],
              description: 'Authentication type'
            },
            token: {
              type: 'string',
              description: 'Token for bearer/jwt/oauth/swaggerhub authentication'
            },
            username: {
              type: 'string',
              description: 'Username for basic authentication'
            },
            password: {
              type: 'string',
              description: 'Password for basic authentication'
            },
            key: {
              type: 'string',
              description: 'Header name for API key authentication (e.g., "X-API-Key")'
            },
            value: {
              type: 'string',
              description: 'API key value'
            },
            cookies: {
              type: 'string',
              description: 'Cookie string for cookie-based authentication'
            }
          }
        },
        credentials: {
          type: 'string',
          enum: ['omit', 'include', 'same-origin'],
          description: 'Credentials mode for CORS requests',
          default: 'omit'
        },
        format: {
          type: 'string',
          description: 'Expected format of the specification',
          enum: ['json', 'yaml', 'auto'],
          default: 'auto'
        }
      },
      required: ['url']
    }
  },
  {
    name: 'fetch_swaggerhub_spec',
    description: 'Fetch Swagger/OpenAPI specification from SwaggerHub with proper authentication and URL handling.',
    inputSchema: {
      type: 'object',
      properties: {
        owner: {
          type: 'string',
          description: 'SwaggerHub organization/owner name (e.g., "capgemini-5bf")'
        },
        api: {
          type: 'string',
          description: 'API name in SwaggerHub (e.g., "policy-and-endorsement-api")'
        },
        version: {
          type: 'string',
          description: 'API version',
          default: '1.0.0'
        },
        token: {
          type: 'string',
          description: 'SwaggerHub API token for private APIs'
        }
      },
      required: ['owner', 'api']
    }
  },
  {
    name: 'search_swagger_specs',
    description: 'Search for Swagger/OpenAPI specifications at common paths on a given base URL.',
    inputSchema: {
      type: 'object',
      properties: {
        baseUrl: {
          type: 'string',
          description: 'Base URL to search for Swagger specifications (e.g., https://api.example.com)'
        },
        customPaths: {
          type: 'array',
          description: 'Additional custom paths to check beyond common ones',
          items: {
            type: 'string'
          },
          default: []
        }
      },
      required: ['baseUrl']
    }
  },
  {
    name: 'parse_swagger_spec',
    description: 'Parse and extract key information from a Swagger/OpenAPI specification.',
    inputSchema: {
      type: 'object',
      properties: {
        content: {
          type: ['string', 'object'],
          description: 'Swagger/OpenAPI specification content (JSON string or parsed object)'
        },
        url: {
          type: 'string',
          description: 'Optional URL where the specification was retrieved from',
          default: 'Unknown'
        }
      },
      required: ['content']
    }
  }
];