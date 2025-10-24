import fetch from 'node-fetch';
import { serverConfig, swaggerConfig } from '../config/config.js';

export class SwaggerService {
  /**
   * Fetch Swagger/OpenAPI specification from a URL with authentication support
   * @param {Object} args - Arguments containing url, authentication, and optional headers
   * @returns {Object} MCP tool response with swagger content
   */
  static async fetchSwaggerSpec(args) {
    const { 
      url, 
      headers = {}, 
      format = 'json',
      auth = null,
      credentials = null
    } = args;

    if (!url) {
      throw new Error('URL is required to fetch Swagger specification');
    }

    try {
      // Build authentication headers
      const authHeaders = this.buildAuthHeaders(auth, credentials);
      
      // Set default headers for Swagger/OpenAPI requests
      const defaultHeaders = {
        'Accept': 'application/json, application/yaml, text/yaml, text/plain',
        'User-Agent': 'Atlassian-MCP-Server/1.0.0',
        'Cache-Control': 'no-cache',
        ...authHeaders,
        ...headers // User headers override defaults
      };

      console.error(`Fetching Swagger spec from: ${url}`);
      if (auth) {
        console.error(`Using authentication: ${auth.type}`);
      }

      // Special handling for SwaggerHub URLs
      let fetchUrl = url;
      if (url.includes('app.swaggerhub.com')) {
        // Convert app.swaggerhub.com URLs to API endpoint URLs
        const swaggerHubMatch = url.match(/app\.swaggerhub\.com\/apis\/([^\/]+)\/([^\/]+)\/([^\/]+)/);
        if (swaggerHubMatch) {
          const [, owner, api, version] = swaggerHubMatch;
          fetchUrl = `https://api.swaggerhub.com/apis/${owner}/${api}/${version}`;
          console.error(`Converted SwaggerHub URL to API endpoint: ${fetchUrl}`);
        }
      }

      const fetchOptions = {
        method: 'GET',
        headers: defaultHeaders,
        timeout: 30000, // 30 seconds timeout
        follow: 5, // Limit redirects to 5
        credentials: credentials || 'omit' // Handle CORS credentials
      };

      // Remove redirect option for node-fetch compatibility
      delete fetchOptions.redirect;

      let response;
      try {
        response = await fetch(fetchUrl, fetchOptions);
      } catch (fetchError) {
        // If the API endpoint fails and it's a SwaggerHub URL, try alternative formats
        if (fetchUrl.includes('api.swaggerhub.com')) {
          console.error(`API endpoint failed, trying alternative formats...`);
          const swaggerHubMatch = fetchUrl.match(/api\.swaggerhub\.com\/apis\/([^\/]+)\/([^\/]+)\/([^\/]+)/);
          if (swaggerHubMatch) {
            const [, owner, api, version] = swaggerHubMatch;
            const alternativeUrls = [
              `https://api.swaggerhub.com/apis/${owner}/${api}/${version}/swagger.json`,
              `https://api.swaggerhub.com/apis/${owner}/${api}/${version}/swagger.yaml`,
              `https://virtserver.swaggerhub.com/${owner}/${api}/${version}/swagger.json`
            ];

            for (const altUrl of alternativeUrls) {
              try {
                console.error(`Trying alternative URL: ${altUrl}`);
                response = await fetch(altUrl, fetchOptions);
                if (response.ok) {
                  console.error(`Success with alternative URL: ${altUrl}`);
                  break;
                }
              } catch (altError) {
                console.error(`Alternative URL failed: ${altError.message}`);
                continue;
              }
            }
          }
        }
        
        // If all attempts failed, throw the original error
        if (!response) {
          throw fetchError;
        }
      }

      if (!response.ok) {
        return {
          content: [{
            type: 'text',
            text: `❌ **Failed to fetch Swagger specification**\n\n**URL:** ${url}\n**Status:** ${response.status} ${response.statusText}\n**Error:** Unable to access the Swagger documentation. This may be due to:\n- Private/protected API documentation\n- Authentication required (try using auth parameter)\n- Invalid credentials\n- URL moved or deprecated\n- Network access restrictions\n\n**Authentication Help:**\n- For SwaggerHub: Use \`auth: {type: "bearer", token: "your-swaggerhub-token"}\`\n- For Basic Auth: Use \`auth: {type: "basic", username: "user", password: "pass"}\`\n- For API Key: Use \`auth: {type: "apikey", key: "x-api-key", value: "your-key"}\``
          }]
        };
      }

      const contentType = response.headers.get('content-type') || '';
      let swaggerContent;
      let parsedContent = null;

      // Get content based on content type
      if (contentType.includes('application/json')) {
        swaggerContent = await response.text();
        try {
          parsedContent = JSON.parse(swaggerContent);
        } catch (e) {
          // Keep as text if JSON parsing fails
        }
      } else {
        swaggerContent = await response.text();
      }

      // Try to parse as JSON even if content-type doesn't indicate it
      if (!parsedContent && swaggerContent.trim().startsWith('{')) {
        try {
          parsedContent = JSON.parse(swaggerContent);
        } catch (e) {
          // Keep as text
        }
      }

      // Format the response
      if (parsedContent) {
        return this.formatSwaggerResponse(url, parsedContent, 'json');
      } else {
        return this.formatSwaggerResponse(url, swaggerContent, 'text');
      }

    } catch (error) {
      console.error(`Error fetching Swagger spec: ${error.message}`);
      return {
        content: [{
          type: 'text',
          text: `❌ **Error fetching Swagger specification**\n\n**URL:** ${url}\n**Error:** ${error.message}\n\n**Possible causes:**\n- Network connectivity issues\n- Invalid URL\n- Server timeout\n- CORS restrictions\n- Authentication required`
        }]
      };
    }
  }

  /**
   * Search for Swagger/OpenAPI specifications in common locations
   * @param {Object} args - Arguments containing baseUrl and optional paths
   * @returns {Object} MCP tool response with search results
   */
  static async searchSwaggerSpecs(args) {
    const { baseUrl, customPaths = [] } = args;

    if (!baseUrl) {
      throw new Error('Base URL is required to search for Swagger specifications');
    }

    // Common Swagger/OpenAPI paths
    const commonPaths = [
      '/swagger.json',
      '/swagger.yaml',
      '/api-docs',
      '/api/docs',
      '/v1/api-docs',
      '/v2/api-docs',
      '/v3/api-docs',
      '/openapi.json',
      '/openapi.yaml',
      '/docs/swagger.json',
      '/docs/openapi.json',
      '/swagger/v1/swagger.json',
      '/swagger/docs',
      '/api/swagger.json',
      '/api/openapi.json',
      ...customPaths
    ];

    const results = [];
    const baseUrlClean = baseUrl.replace(/\/$/, ''); // Remove trailing slash

    console.error(`Searching for Swagger specs at: ${baseUrlClean}`);

    for (const path of commonPaths) {
      const fullUrl = `${baseUrlClean}${path}`;
      
      try {
        const response = await fetch(fullUrl, {
          method: 'HEAD', // Use HEAD to check if resource exists
          timeout: 10000, // 10 seconds timeout
        });

        if (response.ok) {
          results.push({
            url: fullUrl,
            status: 'Found',
            contentType: response.headers.get('content-type') || 'unknown'
          });
        }
      } catch (error) {
        // Silently continue - we expect many 404s
        results.push({
          url: fullUrl,
          status: 'Not Found',
          error: error.message
        });
      }
    }

    const foundSpecs = results.filter(r => r.status === 'Found');
    
    let resultText = `🔍 **Swagger/OpenAPI Specification Search Results**\n\n**Base URL:** ${baseUrlClean}\n**Paths Searched:** ${commonPaths.length}\n\n`;

    if (foundSpecs.length > 0) {
      resultText += `✅ **Found ${foundSpecs.length} specification(s):**\n\n`;
      foundSpecs.forEach(spec => {
        resultText += `- **${spec.url}**\n  - Status: ${spec.status}\n  - Content-Type: ${spec.contentType}\n\n`;
      });
      
      resultText += `💡 **Next Steps:** Use the \`fetch_swagger_spec\` tool with any of the found URLs to retrieve the full specification.\n`;
    } else {
      resultText += `❌ **No Swagger/OpenAPI specifications found**\n\nCommon paths checked include:\n`;
      commonPaths.slice(0, 10).forEach(path => {
        resultText += `- ${baseUrlClean}${path}\n`;
      });
      resultText += `\n💡 **Suggestions:**\n- Check if the API documentation is at a different path\n- Verify the base URL is correct\n- API documentation may require authentication\n- Check the API provider's documentation for the correct URL`;
    }

    return {
      content: [{
        type: 'text',
        text: resultText
      }]
    };
  }

  /**
   * Parse and extract key information from Swagger/OpenAPI specification
   * @param {Object} args - Arguments containing swagger spec content
   * @returns {Object} MCP tool response with parsed swagger info
   */
  static async parseSwaggerSpec(args) {
    const { content, url = 'Unknown' } = args;

    if (!content) {
      throw new Error('Swagger specification content is required');
    }

    try {
      let spec;
      
      // Parse content if it's a string
      if (typeof content === 'string') {
        try {
          spec = JSON.parse(content);
        } catch (e) {
          throw new Error('Invalid JSON format in Swagger specification');
        }
      } else {
        spec = content;
      }

      return this.formatSwaggerSummary(url, spec);

    } catch (error) {
      console.error(`Error parsing Swagger spec: ${error.message}`);
      return {
        content: [{
          type: 'text',
          text: `❌ **Error parsing Swagger specification**\n\n**Error:** ${error.message}\n\n**URL:** ${url}`
        }]
      };
    }
  }

  /**
   * Format Swagger response for MCP
   * @private
   */
  static formatSwaggerResponse(url, content, type) {
    if (type === 'json' && typeof content === 'object') {
      // Generate summary first
      const summary = this.generateSwaggerSummary(content);
      
      return {
        content: [{
          type: 'text',
          text: `✅ **Swagger/OpenAPI Specification Retrieved**\n\n**URL:** ${url}\n\n${summary}\n\n---\n\n**Full Specification:**\n\n\`\`\`json\n${JSON.stringify(content, null, 2)}\n\`\`\``
        }]
      };
    } else {
      return {
        content: [{
          type: 'text',
          text: `✅ **Swagger/OpenAPI Specification Retrieved**\n\n**URL:** ${url}\n**Format:** ${type}\n\n**Content:**\n\n\`\`\`\n${content}\n\`\`\``
        }]
      };
    }
  }

  /**
   * Format Swagger summary for MCP
   * @private
   */
  static formatSwaggerSummary(url, spec) {
    const summary = this.generateSwaggerSummary(spec);
    
    return {
      content: [{
        type: 'text',
        text: `📋 **Swagger/OpenAPI Specification Summary**\n\n**URL:** ${url}\n\n${summary}`
      }]
    };
  }

  /**
   * Generate a summary of Swagger/OpenAPI specification
   * @private
   */
  static generateSwaggerSummary(spec) {
    let summary = '';

    // Basic info
    if (spec.info) {
      summary += `## 📖 **API Information**\n`;
      summary += `**Title:** ${spec.info.title || 'N/A'}\n`;
      summary += `**Version:** ${spec.info.version || 'N/A'}\n`;
      summary += `**Description:** ${spec.info.description || 'N/A'}\n\n`;
    }

    // OpenAPI version
    if (spec.openapi) {
      summary += `**OpenAPI Version:** ${spec.openapi}\n\n`;
    } else if (spec.swagger) {
      summary += `**Swagger Version:** ${spec.swagger}\n\n`;
    }

    // Servers
    if (spec.servers && spec.servers.length > 0) {
      summary += `## 🌐 **Servers**\n`;
      spec.servers.forEach(server => {
        summary += `- **${server.url}** - ${server.description || 'No description'}\n`;
      });
      summary += '\n';
    } else if (spec.host) {
      summary += `## 🌐 **Server**\n`;
      summary += `**Host:** ${spec.host}\n`;
      if (spec.basePath) summary += `**Base Path:** ${spec.basePath}\n`;
      summary += '\n';
    }

    // Paths/Endpoints
    if (spec.paths) {
      const pathCount = Object.keys(spec.paths).length;
      summary += `## 🛠️ **Endpoints** (${pathCount} total)\n\n`;

      let endpointCount = 0;
      for (const [path, methods] of Object.entries(spec.paths)) {
        if (endpointCount >= 10) {
          summary += `... and ${pathCount - endpointCount} more endpoints\n`;
          break;
        }

        for (const [method, details] of Object.entries(methods)) {
          if (typeof details === 'object' && method !== 'parameters') {
            summary += `**${method.toUpperCase()}** \`${path}\``;
            if (details.summary) {
              summary += ` - ${details.summary}`;
            }
            summary += '\n';
            endpointCount++;
          }
        }
      }
      summary += '\n';
    }

    // Components/Definitions
    if (spec.components?.schemas || spec.definitions) {
      const schemas = spec.components?.schemas || spec.definitions;
      const schemaCount = Object.keys(schemas).length;
      summary += `## 📊 **Data Models** (${schemaCount} schemas)\n\n`;
      
      const schemaNames = Object.keys(schemas).slice(0, 10);
      schemaNames.forEach(name => {
        summary += `- \`${name}\`\n`;
      });
      
      if (schemaCount > 10) {
        summary += `... and ${schemaCount - 10} more schemas\n`;
      }
      summary += '\n';
    }

    // Tags
    if (spec.tags && spec.tags.length > 0) {
      summary += `## 🏷️ **Tags**\n\n`;
      spec.tags.forEach(tag => {
        summary += `- **${tag.name}** - ${tag.description || 'No description'}\n`;
      });
      summary += '\n';
    }

    return summary;
  }

  /**
   * Build authentication headers based on auth configuration
   * @private
   */
  static buildAuthHeaders(auth, credentials) {
    if (!auth) return {};

    const authHeaders = {};

    switch (auth.type?.toLowerCase()) {
      case 'bearer':
      case 'jwt':
        if (auth.token) {
          authHeaders['Authorization'] = `Bearer ${auth.token}`;
        }
        break;

      case 'basic':
        if (auth.username && auth.password) {
          const encoded = Buffer.from(`${auth.username}:${auth.password}`).toString('base64');
          authHeaders['Authorization'] = `Basic ${encoded}`;
        }
        break;

      case 'apikey':
      case 'api-key':
        if (auth.key && auth.value) {
          authHeaders[auth.key] = auth.value;
        }
        break;

      case 'cookie':
        if (auth.cookies) {
          authHeaders['Cookie'] = auth.cookies;
        }
        break;

      case 'oauth':
        if (auth.token) {
          authHeaders['Authorization'] = `OAuth ${auth.token}`;
        }
        break;

      case 'swaggerhub':
        // Special handling for SwaggerHub authentication
        if (auth.token) {
          authHeaders['Authorization'] = `Bearer ${auth.token}`;
          authHeaders['X-SwaggerHub-API-Key'] = auth.token;
        }
        break;

      default:
        console.error(`Unknown authentication type: ${auth.type}`);
    }

    return authHeaders;
  }

  /**
   * Fetch Swagger spec with SwaggerHub-specific authentication
   * @param {Object} args - Arguments with SwaggerHub-specific options
   * @returns {Object} MCP tool response
   */
  static async fetchSwaggerHubSpec(args) {
    const { owner, api, version = '1.0.0', token = null } = args;

    if (!owner || !api) {
      throw new Error('Owner and API name are required for SwaggerHub');
    }

    // Use API endpoint directly to avoid redirect issues
    const url = `https://api.swaggerhub.com/apis/${owner}/${api}/${version}`;
    
    // Use provided token or fall back to config
    const authToken = token || swaggerConfig.swaggerhub?.token;
    const authConfig = authToken ? {
      type: 'swaggerhub',
      token: authToken
    } : null;

    return this.fetchSwaggerSpec({
      url,
      auth: authConfig,
      credentials: 'omit' // Use omit for API endpoints
    });
  }
}