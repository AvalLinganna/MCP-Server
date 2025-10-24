import { ApiClient } from '../utils/apiClient.js';
import { confluenceConfig } from '../config/config.js';

export class ConfluenceService {
  // Helper method to get space information by key
  static async getSpaceInfo(spaceKey) {
    try {
      const response = await ApiClient.makeConfluenceRequest(
        confluenceConfig.baseUrl,
        confluenceConfig.email,
        confluenceConfig.apiToken,
        `space/${spaceKey}`
      );
      return response;
    } catch (error) {
      throw new Error(`Failed to get space info for '${spaceKey}': ${error.message}`);
    }
  }

  static async getConfluencePages(args) {
    const { spaceKey = confluenceConfig.spaceKey, title, limit = 25 } = args;
    
    try {
      let endpoint = `content?spaceKey=${spaceKey}&limit=${limit}&expand=body.storage,version,space`;
      
      if (title) {
        endpoint += `&title=${encodeURIComponent(title)}`;
      }
      
      const response = await ApiClient.makeConfluenceRequest(
        confluenceConfig.baseUrl,
        confluenceConfig.email,
        confluenceConfig.apiToken,
        endpoint
      );
      
      return {
        content: [
          {
            type: 'text',
            text: `Found ${response.results?.length || 0} pages in space ${spaceKey}:\n\n${
              response.results?.map(page => 
                `📄 **${page.title}**\n` +
                `   ID: ${page.id}\n` +
                `   Type: ${page.type}\n` +
                `   Status: ${page.status}\n` +
                `   Created: ${page.version?.when}\n` +
                `   URL: ${confluenceConfig.baseUrl}/wiki${page._links?.webui}\n`
              ).join('\n') || 'No pages found.'
            }`
          }
        ]
      };
    } catch (error) {
      return {
        content: [
          {
            type: 'text',
            text: `Error fetching Confluence pages: ${error.message}`
          }
        ]
      };
    }
  }

  static async createConfluencePage(args) {
    const { spaceKey = confluenceConfig.spaceKey, title, content, parentId } = args;
    
    try {
      const pageData = {
        type: 'page',
        title,
        space: {
          key: spaceKey
        },
        body: {
          storage: {
            value: content.includes('<') ? content : `<p>${content}</p>`,
            representation: 'storage'
          }
        }
      };

      if (parentId) {
        pageData.ancestors = [{ id: parentId }];
      }

      const response = await ApiClient.makeConfluenceRequest(
        confluenceConfig.baseUrl,
        confluenceConfig.email,
        confluenceConfig.apiToken,
        'content',
        'POST',
        pageData
      );
      
      return {
        content: [
          {
            type: 'text',
            text: `✅ Successfully created Confluence page!\n\n` +
                  `📄 **${response.title}**\n` +
                  `   ID: ${response.id}\n` +
                  `   Space: ${response.space?.name} (${response.space?.key})\n` +
                  `   URL: ${confluenceConfig.baseUrl}/wiki${response._links?.webui}\n` +
                  `   Status: ${response.status}\n` +
                  `   Created: ${response.version?.when}`
          }
        ]
      };
    } catch (error) {
      return {
        content: [
          {
            type: 'text',
            text: `❌ Error creating Confluence page: ${error.message}`
          }
        ]
      };
    }
  }

  static async updateConfluencePage(args) {
    const { pageId, title, content, version } = args;
    
    try {
      let currentVersion = version;
      let currentPage;
      if (!currentVersion) {
        currentPage = await ApiClient.makeConfluenceRequest(
          confluenceConfig.baseUrl,
          confluenceConfig.email,
          confluenceConfig.apiToken,
          `content/${pageId}?expand=version,space`
        );
        currentVersion = currentPage.version?.number;
      } else {
        currentPage = await ApiClient.makeConfluenceRequest(
          confluenceConfig.baseUrl,
          confluenceConfig.email,
          confluenceConfig.apiToken,
          `content/${pageId}?expand=space`
        );
      }
      
      const updateData = {
        version: {
          number: currentVersion + 1
        },
        body: {
          storage: {
            value: content.includes('<') ? content : `<p>${content}</p>`,
            representation: 'storage'
          }
        }
      };

      if (title) {
        updateData.title = title;
      }

      const response = await ApiClient.makeConfluenceRequest(
        confluenceConfig.baseUrl,
        confluenceConfig.email,
        confluenceConfig.apiToken,
        `content/${pageId}`,
        'PUT',
        updateData
      );
      
      return {
        content: [
          {
            type: 'text',
            text: `✅ Successfully updated Confluence page!\n\n` +
                  `📄 **${response.title}**\n` +
                  `   ID: ${response.id}\n` +
                  `   Version: ${response.version?.number}\n` +
                  `   URL: ${confluenceConfig.baseUrl}/wiki${response._links?.webui}\n` +
                  `   Updated: ${response.version?.when}`
          }
        ]
      };
    } catch (error) {
      return {
        content: [
          {
            type: 'text',
            text: `❌ Error updating Confluence page: ${error.message}`
          }
        ]
      };
    }
  }

  static async getConfluencePage(args) {
    const { pageId, title, spaceKey = confluenceConfig.spaceKey, expand = 'body.storage,version,space' } = args;
    
    try {
      let response;
      
      if (pageId) {
        response = await ApiClient.makeConfluenceRequest(
          confluenceConfig.baseUrl,
          confluenceConfig.email,
          confluenceConfig.apiToken,
          `content/${pageId}?expand=${expand}`
        );
      } else if (title && spaceKey) {
        const searchResponse = await ApiClient.makeConfluenceRequest(
          confluenceConfig.baseUrl,
          confluenceConfig.email,
          confluenceConfig.apiToken,
          `content?spaceKey=${spaceKey}&title=${encodeURIComponent(title)}&expand=${expand}`
        );
        if (searchResponse.results?.length > 0) {
          response = searchResponse.results[0];
        } else {
          throw new Error(`Page with title "${title}" not found in space ${spaceKey}`);
        }
      } else {
        throw new Error('Either pageId or both title and spaceKey must be provided');
      }
      
      return {
        content: [
          {
            type: 'text',
            text: `📄 **${response.title}**\n\n` +
                  `**Details:**\n` +
                  `   ID: ${response.id}\n` +
                  `   Type: ${response.type}\n` +
                  `   Status: ${response.status}\n` +
                  `   Space: ${response.space?.name} (${response.space?.key})\n` +
                  `   Version: ${response.version?.number}\n` +
                  `   Created: ${response.version?.when}\n` +
                  `   URL: ${confluenceConfig.baseUrl}/wiki${response._links?.webui}\n\n` +
                  `**Content:**\n${response.body?.storage?.value || 'No content available'}`
          }
        ]
      };
    } catch (error) {
      return {
        content: [
          {
            type: 'text',
            text: `❌ Error fetching Confluence page: ${error.message}`
          }
        ]
      };
    }
  }

  static async getSpecificConfluencePage(args) {
    const { pageName, spaceKey = confluenceConfig.spaceKey, includeContent = true, format = 'detailed' } = args;
    
    try {
      console.log(`🎯 Fetching specific page: ${pageName}`);
      
      const pageMapping = {
        'Policy Management': ['Policy Management', 'Policy', 'Policies'],
        'Claim Management': ['Claim Management', 'Claims', 'Claim Process'],
        'API Documentation': ['API Documentation', 'API Docs', 'API Guide'],
        'User Guide': ['User Guide', 'User Manual', 'Help'],
        'Technical Specifications': ['Technical Specifications', 'Tech Specs', 'Requirements']
      };
      
      const searchTerms = pageMapping[pageName] || [pageName];
      let foundPage = null;
      
      for (const searchTerm of searchTerms) {
        try {
          const searchResponse = await ApiClient.makeConfluenceRequest(
            confluenceConfig.baseUrl,
            confluenceConfig.email,
            confluenceConfig.apiToken,
            `content?spaceKey=${encodeURIComponent(spaceKey)}&title=${encodeURIComponent(searchTerm)}&expand=body.storage,version,space`
          );
          
          if (searchResponse.results?.length > 0) {
            foundPage = searchResponse.results[0];
            console.log(`✅ Found page with title: "${foundPage.title}"`);
            break;
          }
        } catch (searchError) {
          console.log(`⚠️ Search term "${searchTerm}" not found, trying next...`);
        }
      }
      
      if (!foundPage) {
        const allPagesResponse = await ApiClient.makeConfluenceRequest(
          confluenceConfig.baseUrl,
          confluenceConfig.email,
          confluenceConfig.apiToken,
          `content?spaceKey=${encodeURIComponent(spaceKey)}&limit=50&expand=body.storage,version,space`
        );
        
        const fuzzyMatch = allPagesResponse.results?.find(page => 
          searchTerms.some(term => 
            page.title.toLowerCase().includes(term.toLowerCase()) ||
            term.toLowerCase().includes(page.title.toLowerCase())
          )
        );
        
        if (fuzzyMatch) {
          foundPage = fuzzyMatch;
          console.log(`✅ Found page via fuzzy search: "${foundPage.title}"`);
        }
      }
      
      if (!foundPage) {
        return {
          content: [
            {
              type: 'text',
              text: `❌ Could not find page "${pageName}" in space ${spaceKey}.\n\n` +
                    `Searched for: ${searchTerms.join(', ')}\n\n` +
                    `💡 Available pages can be found using get_confluence_pages tool.`
            }
          ]
        };
      }
      
      if (format === 'summary') {
        return {
          content: [
            {
              type: 'text',
              text: `📄 **${foundPage.title}** (SUMMARY)\n\n` +
                    `🆔 ID: ${foundPage.id}\n` +
                    `📁 Space: ${foundPage.space?.name}\n` +
                    `🟢 Status: ${foundPage.status}\n` +
                    `📊 Version: ${foundPage.version?.number}\n` +
                    `📅 Updated: ${foundPage.version?.when}\n` +
                    `🔗 URL: ${confluenceConfig.baseUrl}/wiki${foundPage._links?.webui}`
            }
          ]
        };
      }
      
      let content = 'No content available';
      if (includeContent && foundPage.body?.storage?.value) {
        content = foundPage.body.storage.value;
        
        if (format === 'detailed') {
          content = content.replace(/<ac:structured-macro[^>]*>/g, '\n--- MACRO ---\n');
          content = content.replace(/<\/ac:structured-macro>/g, '\n--- END MACRO ---\n');
          content = content.replace(/<p[^>]*>/g, '\n');
          content = content.replace(/<\/p>/g, '\n');
          content = content.replace(/<h([1-6])[^>]*>/g, (match, level) => `\n${'#'.repeat(parseInt(level))} `);
          content = content.replace(/<\/h[1-6]>/g, '\n');
          content = content.replace(/<strong[^>]*>/g, '**');
          content = content.replace(/<\/strong>/g, '**');
          content = content.replace(/<em[^>]*>/g, '*');
          content = content.replace(/<\/em>/g, '*');
          content = content.replace(/<li[^>]*>/g, '• ');
          content = content.replace(/<\/li>/g, '\n');
          content = content.replace(/<[^>]*>/g, '');
          content = content.replace(/\n\n+/g, '\n\n');
          content = content.trim();
        }
      }
      
      return {
        content: [
          {
            type: 'text',
            text: `📄 **${foundPage.title}**\n\n` +
                  `**Details:**\n` +
                  `   ID: ${foundPage.id}\n` +
                  `   Type: ${foundPage.type}\n` +
                  `   Status: ${foundPage.status}\n` +
                  `   Space: ${foundPage.space?.name} (${foundPage.space?.key})\n` +
                  `   Version: ${foundPage.version?.number}\n` +
                  `   Created: ${foundPage.version?.when}\n` +
                  `   URL: ${confluenceConfig.baseUrl}/wiki${foundPage._links?.webui}\n\n` +
                  (includeContent ? `**Content:**\n${content}` : `**Content excluded** (set includeContent=true to view)`)
          }
        ]
      };
      
    } catch (error) {
      return {
        content: [
          {
            type: 'text',
            text: `❌ Error fetching specific Confluence page "${pageName}": ${error.message}`
          }
        ]
      };
    }
  }

  static async getAllSpacesContent(args) {
    const { limit = 10, includeContent = false } = args;
    
    try {
      const spaceKeys = [confluenceConfig.spaceKey].filter(Boolean);
        
      if (spaceKeys.length === 0) {
        return {
          content: [
            {
              type: 'text',
              text: '❌ No space keys configured. Please set CONFLUENCE_SPACE_KEY in your environment variables.'
            }
          ]
        };
      }

      let allContent = `📚 **Content from ${spaceKeys.length} Confluence Spaces**\n\n`;
      let totalPages = 0;

      for (const spaceKey of spaceKeys) {
        try {
          const response = await ApiClient.makeConfluenceRequest(
            confluenceConfig.baseUrl,
            confluenceConfig.email,
            confluenceConfig.apiToken,
            `content?spaceKey=${spaceKey}&limit=${limit}&expand=body.storage,version,space`
          );

          const spaceInfo = await this.getSpaceInfo(spaceKey);
          const pages = response.results || [];
          totalPages += pages.length;

          allContent += `## 📁 **${spaceInfo.name} (${spaceKey})**\n`;
          allContent += `   Found ${pages.length} pages:\n\n`;

          for (const page of pages) {
            allContent += `📄 **${page.title}**\n`;
            allContent += `   ID: ${page.id} | Status: ${page.status} | Created: ${page.version?.when}\n`;
            allContent += `   URL: ${confluenceConfig.baseUrl}/wiki${page._links?.webui}\n`;
            
            if (includeContent && page.body?.storage?.value) {
              const content = page.body.storage.value.replace(/<[^>]*>/g, '').substring(0, 200);
              allContent += `   Content Preview: ${content}${content.length >= 200 ? '...' : ''}\n`;
            }
            allContent += '\n';
          }
          allContent += '\n';

        } catch (error) {
          allContent += `## ❌ **Error accessing space ${spaceKey}**\n`;
          allContent += `   ${error.message}\n\n`;
        }
      }

      allContent += `\n📊 **Summary:** Found ${totalPages} total pages across ${spaceKeys.length} spaces`;

      return {
        content: [
          {
            type: 'text',
            text: allContent
          }
        ]
      };

    } catch (error) {
      return {
        content: [
          {
            type: 'text',
            text: `❌ Error fetching content from multiple spaces: ${error.message}`
          }
        ]
      };
    }
  }

  static async searchAcrossSpaces(args) {
    const { query, spaceKeys, limit = 5 } = args;
    
    try {
      const searchSpaces = spaceKeys || [confluenceConfig.spaceKey].filter(Boolean);
        
      if (searchSpaces.length === 0) {
        return {
          content: [
            {
              type: 'text',
              text: '❌ No space keys configured for search. Please set CONFLUENCE_SPACE_KEY in your environment variables.'
            }
          ]
        };
      }

      let searchResults = `🔍 **Search Results for "${query}"**\n\n`;
      let totalResults = 0;

      for (const spaceKey of searchSpaces) {
        try {
          // Search using CQL (Confluence Query Language)
          const cqlQuery = `space = "${spaceKey}" AND text ~ "${query}"`;
          const response = await ApiClient.makeConfluenceRequest(
            confluenceConfig.baseUrl,
            confluenceConfig.email,
            confluenceConfig.apiToken,
            `content/search?cql=${encodeURIComponent(cqlQuery)}&limit=${limit}&expand=body.view,version,space`
          );

          const spaceInfo = await this.getSpaceInfo(spaceKey);
          const results = response.results || [];
          totalResults += results.length;

          if (results.length > 0) {
            searchResults += `## 📁 **${spaceInfo.name} (${spaceKey})** - ${results.length} results\n\n`;

            for (const result of results) {
              searchResults += `📄 **${result.title}**\n`;
              searchResults += `   ID: ${result.id} | Type: ${result.type} | Status: ${result.status}\n`;
              searchResults += `   URL: ${confluenceConfig.baseUrl}/wiki${result._links?.webui}\n`;
              
              if (result.body?.view?.value) {
                const excerpt = result.body.view.value.replace(/<[^>]*>/g, '').substring(0, 150);
                searchResults += `   Preview: ${excerpt}${excerpt.length >= 150 ? '...' : ''}\n`;
              }
              searchResults += '\n';
            }
          } else {
            searchResults += `## 📁 **${spaceInfo.name} (${spaceKey})** - No results found\n\n`;
          }

        } catch (error) {
          searchResults += `## ❌ **Error searching space ${spaceKey}**\n`;
          searchResults += `   ${error.message}\n\n`;
        }
      }

      searchResults += `\n📊 **Summary:** Found ${totalResults} total results across ${searchSpaces.length} spaces`;

      return {
        content: [
          {
            type: 'text',
            text: searchResults
          }
        ]
      };

    } catch (error) {
      return {
        content: [
          {
            type: 'text',
            text: `❌ Error searching across spaces: ${error.message}`
          }
        ]
      };
    }
  }
}