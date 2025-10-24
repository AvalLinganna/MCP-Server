import fetch from 'node-fetch';
import fs from 'fs/promises';
import path from 'path';
import { fileURLToPath } from 'url';
import { ApiClient } from '../utils/apiClient.js';
import { confluenceConfig, jiraConfig } from '../config/config.js';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

export class ImageService {
  
  static async readImageFromUrl(args) {
    const { url, includeMetadata = true, maxSize = 10 } = args;
    
    try {
      console.error(`[ImageService] Reading image from URL: ${url}`);
      
      // First, get image headers to check size and type
      const headResponse = await fetch(url, { method: 'HEAD' });
      
      if (!headResponse.ok) {
        throw new Error(`Failed to access image: ${headResponse.status} ${headResponse.statusText}`);
      }

      const contentType = headResponse.headers.get('content-type');
      const contentLength = headResponse.headers.get('content-length');
      
      // Validate content type
      if (!contentType || !contentType.startsWith('image/')) {
        throw new Error(`URL does not point to an image. Content-Type: ${contentType}`);
      }

      // Check file size
      if (contentLength) {
        const sizeMB = parseInt(contentLength) / (1024 * 1024);
        if (sizeMB > maxSize) {
          throw new Error(`Image too large: ${sizeMB.toFixed(2)}MB (max: ${maxSize}MB)`);
        }
      }

      // Get basic image info
      const imageInfo = {
        url,
        contentType,
        sizeBytes: contentLength ? parseInt(contentLength) : 'Unknown',
        sizeMB: contentLength ? (parseInt(contentLength) / (1024 * 1024)).toFixed(2) : 'Unknown',
        lastModified: headResponse.headers.get('last-modified'),
        etag: headResponse.headers.get('etag'),
        server: headResponse.headers.get('server')
      };

      let result = `🖼️ **Image Analysis**\n\n` +
                  `📍 **URL:** ${url}\n` +
                  `📄 **Content Type:** ${contentType}\n` +
                  `📊 **Size:** ${imageInfo.sizeMB}MB (${imageInfo.sizeBytes} bytes)\n`;

      if (imageInfo.lastModified) {
        result += `📅 **Last Modified:** ${imageInfo.lastModified}\n`;
      }

      if (includeMetadata) {
        result += `\n🔍 **Additional Metadata:**\n`;
        result += `   ETag: ${imageInfo.etag || 'Not available'}\n`;
        result += `   Server: ${imageInfo.server || 'Not available'}\n`;
        
        // Try to get more detailed image information
        try {
          const response = await fetch(url);
          const buffer = await response.buffer();
          
          // Basic image format detection
          const formatInfo = this.detectImageFormat(buffer);
          if (formatInfo) {
            result += `   Format Details: ${formatInfo}\n`;
          }
          
        } catch (metadataError) {
          result += `   Extended metadata: Failed to retrieve (${metadataError.message})\n`;
        }
      }

      result += `\n✅ **Status:** Successfully read image information`;

      return {
        content: [
          {
            type: 'text',
            text: result
          }
        ]
      };

    } catch (error) {
      return {
        content: [
          {
            type: 'text',
            text: `❌ Error reading image from URL: ${error.message}`
          }
        ]
      };
    }
  }

  static async downloadImage(args) {
    const { url, fileName, downloadPath = '.', maxSize = 50, overwrite = false } = args;
    
    try {
      console.error(`[ImageService] Downloading image from: ${url}`);
      
      // Check if URL is accessible
      const headResponse = await fetch(url, { method: 'HEAD' });
      if (!headResponse.ok) {
        throw new Error(`Cannot access image: ${headResponse.status} ${headResponse.statusText}`);
      }

      const contentType = headResponse.headers.get('content-type');
      const contentLength = headResponse.headers.get('content-length');
      
      if (!contentType || !contentType.startsWith('image/')) {
        throw new Error(`URL does not point to an image. Content-Type: ${contentType}`);
      }

      // Check file size
      if (contentLength) {
        const sizeMB = parseInt(contentLength) / (1024 * 1024);
        if (sizeMB > maxSize) {
          throw new Error(`Image too large: ${sizeMB.toFixed(2)}MB (max: ${maxSize}MB)`);
        }
      }

      // Determine filename
      let finalFileName = fileName;
      if (!finalFileName) {
        const urlPath = new URL(url).pathname;
        finalFileName = path.basename(urlPath) || `image_${Date.now()}.${this.getExtensionFromContentType(contentType)}`;
      }

      // Ensure download directory exists
      await fs.mkdir(downloadPath, { recursive: true });
      
      const filePath = path.join(downloadPath, finalFileName);
      
      // Check if file exists and overwrite setting
      try {
        await fs.access(filePath);
        if (!overwrite) {
          throw new Error(`File already exists: ${filePath}. Set overwrite=true to replace it.`);
        }
      } catch (error) {
        // File doesn't exist, which is fine
        if (error.code !== 'ENOENT') {
          throw error;
        }
      }

      // Download the image
      const response = await fetch(url);
      if (!response.ok) {
        throw new Error(`Download failed: ${response.status} ${response.statusText}`);
      }

      const buffer = await response.buffer();
      await fs.writeFile(filePath, buffer);

      const stats = await fs.stat(filePath);
      const sizeMB = (stats.size / (1024 * 1024)).toFixed(2);

      return {
        content: [
          {
            type: 'text',
            text: `✅ **Image Downloaded Successfully**\n\n` +
                  `📍 **Source URL:** ${url}\n` +
                  `💾 **Saved to:** ${filePath}\n` +
                  `📄 **Content Type:** ${contentType}\n` +
                  `📊 **File Size:** ${sizeMB}MB (${stats.size} bytes)\n` +
                  `📅 **Downloaded:** ${new Date().toISOString()}`
          }
        ]
      };

    } catch (error) {
      return {
        content: [
          {
            type: 'text',
            text: `❌ Error downloading image: ${error.message}`
          }
        ]
      };
    }
  }

  static async readImageFromConfluence(args) {
    const { pageId, downloadPath = './confluence-images', includeMetadata = true } = args;
    
    try {
      console.error(`[ImageService] Reading images from Confluence page: ${pageId}`);
      
      // Get page attachments
      const attachmentsResponse = await ApiClient.makeConfluenceRequest(
        confluenceConfig.baseUrl,
        confluenceConfig.email,
        confluenceConfig.apiToken,
        `content/${pageId}/child/attachment?expand=metadata,version`
      );

      const attachments = attachmentsResponse.results || [];
      const imageAttachments = attachments.filter(attachment => 
        attachment.metadata?.mediaType?.startsWith('image/')
      );

      if (imageAttachments.length === 0) {
        return {
          content: [
            {
              type: 'text',
              text: `📄 **Confluence Page ${pageId}**\n\n` +
                    `🔍 Found ${attachments.length} total attachments, but no images.\n\n` +
                    `Available attachments:\n${attachments.map(att => 
                      `   • ${att.title} (${att.metadata?.mediaType || 'unknown type'})`
                    ).join('\n') || '   None'}`
            }
          ]
        };
      }

      let result = `📄 **Confluence Page Images**\n\n` +
                  `🆔 **Page ID:** ${pageId}\n` +
                  `🖼️ **Found ${imageAttachments.length} image(s)**\n\n`;

      // Create download directory
      await fs.mkdir(downloadPath, { recursive: true });

      const downloadedImages = [];

      for (const attachment of imageAttachments) {
        try {
          const downloadUrl = `${confluenceConfig.baseUrl}/wiki${attachment._links.download}`;
          const fileName = attachment.title;
          const filePath = path.join(downloadPath, fileName);

          // Download the image
          const auth = Buffer.from(`${confluenceConfig.email}:${confluenceConfig.apiToken}`).toString('base64');
          const response = await fetch(downloadUrl, {
            headers: {
              'Authorization': `Basic ${auth}`
            }
          });

          if (!response.ok) {
            throw new Error(`Failed to download: ${response.status} ${response.statusText}`);
          }

          const buffer = await response.buffer();
          await fs.writeFile(filePath, buffer);

          const stats = await fs.stat(filePath);
          const sizeMB = (stats.size / (1024 * 1024)).toFixed(2);

          downloadedImages.push({
            fileName,
            filePath,
            size: stats.size,
            sizeMB,
            contentType: attachment.metadata?.mediaType,
            created: attachment.version?.when
          });

          result += `✅ **${fileName}**\n`;
          result += `   💾 Saved to: ${filePath}\n`;
          result += `   📊 Size: ${sizeMB}MB\n`;
          result += `   📄 Type: ${attachment.metadata?.mediaType}\n`;
          if (includeMetadata && attachment.version?.when) {
            result += `   📅 Created: ${attachment.version.when}\n`;
          }
          result += '\n';

        } catch (downloadError) {
          result += `❌ **${attachment.title}**\n`;
          result += `   Error: ${downloadError.message}\n\n`;
        }
      }

      result += `\n📊 **Summary:** Downloaded ${downloadedImages.length}/${imageAttachments.length} images to ${downloadPath}`;

      return {
        content: [
          {
            type: 'text',
            text: result
          }
        ]
      };

    } catch (error) {
      return {
        content: [
          {
            type: 'text',
            text: `❌ Error reading images from Confluence: ${error.message}`
          }
        ]
      };
    }
  }

  static async extractImagesFromJira(args) {
    const { issueKey, downloadPath = './jira-images', imageTypes = ['jpg', 'jpeg', 'png', 'gif', 'webp', 'svg', 'bmp'] } = args;
    
    try {
      console.error(`[ImageService] Extracting images from Jira issue: ${issueKey}`);
      
      // Get issue with attachments
      const issue = await ApiClient.makeJiraRequest(
        jiraConfig.baseUrl,
        jiraConfig.email,
        jiraConfig.apiToken,
        `issue/${issueKey}?expand=attachment`
      );

      const attachments = issue.fields?.attachment || [];
      const imageAttachments = attachments.filter(attachment => {
        const fileName = attachment.filename?.toLowerCase() || '';
        return imageTypes.some(type => fileName.endsWith(`.${type.toLowerCase()}`));
      });

      if (imageAttachments.length === 0) {
        return {
          content: [
            {
              type: 'text',
              text: `🎫 **Jira Issue ${issueKey}**\n\n` +
                    `🔍 Found ${attachments.length} total attachments, but no images matching types: ${imageTypes.join(', ')}\n\n` +
                    `Available attachments:\n${attachments.map(att => 
                      `   • ${att.filename} (${att.mimeType || 'unknown type'})`
                    ).join('\n') || '   None'}`
            }
          ]
        };
      }

      let result = `🎫 **Jira Issue Images**\n\n` +
                  `🆔 **Issue:** ${issueKey}\n` +
                  `📌 **Title:** ${issue.fields?.summary || 'N/A'}\n` +
                  `🖼️ **Found ${imageAttachments.length} image(s)**\n\n`;

      // Create download directory
      await fs.mkdir(downloadPath, { recursive: true });

      const downloadedImages = [];

      for (const attachment of imageAttachments) {
        try {
          const downloadUrl = attachment.content;
          const fileName = attachment.filename;
          const filePath = path.join(downloadPath, `${issueKey}_${fileName}`);

          // Download the image
          const auth = Buffer.from(`${jiraConfig.email}:${jiraConfig.apiToken}`).toString('base64');
          const response = await fetch(downloadUrl, {
            headers: {
              'Authorization': `Basic ${auth}`
            }
          });

          if (!response.ok) {
            throw new Error(`Failed to download: ${response.status} ${response.statusText}`);
          }

          const buffer = await response.buffer();
          await fs.writeFile(filePath, buffer);

          const stats = await fs.stat(filePath);
          const sizeMB = (stats.size / (1024 * 1024)).toFixed(2);

          downloadedImages.push({
            fileName,
            filePath,
            size: stats.size,
            sizeMB,
            contentType: attachment.mimeType,
            created: attachment.created
          });

          result += `✅ **${fileName}**\n`;
          result += `   💾 Saved to: ${filePath}\n`;
          result += `   📊 Size: ${sizeMB}MB\n`;
          result += `   📄 Type: ${attachment.mimeType}\n`;
          result += `   👤 Author: ${attachment.author?.displayName || 'Unknown'}\n`;
          result += `   📅 Created: ${attachment.created}\n\n`;

        } catch (downloadError) {
          result += `❌ **${attachment.filename}**\n`;
          result += `   Error: ${downloadError.message}\n\n`;
        }
      }

      result += `\n📊 **Summary:** Downloaded ${downloadedImages.length}/${imageAttachments.length} images to ${downloadPath}`;

      return {
        content: [
          {
            type: 'text',
            text: result
          }
        ]
      };

    } catch (error) {
      return {
        content: [
          {
            type: 'text',
            text: `❌ Error extracting images from Jira: ${error.message}`
          }
        ]
      };
    }
  }

  static async getImageInfo(args) {
    const { url, headers = {} } = args;
    
    try {
      console.error(`[ImageService] Getting image info for: ${url}`);
      
      const requestHeaders = {
        'User-Agent': 'AtlassianMCPServer/1.0',
        ...headers
      };

      const response = await fetch(url, { 
        method: 'HEAD',
        headers: requestHeaders
      });
      
      if (!response.ok) {
        throw new Error(`Failed to access image: ${response.status} ${response.statusText}`);
      }

      const contentType = response.headers.get('content-type');
      const contentLength = response.headers.get('content-length');
      const lastModified = response.headers.get('last-modified');
      const etag = response.headers.get('etag');
      const cacheControl = response.headers.get('cache-control');
      const server = response.headers.get('server');

      if (!contentType || !contentType.startsWith('image/')) {
        return {
          content: [
            {
              type: 'text',
              text: `❌ **Not an Image**\n\n` +
                    `📍 **URL:** ${url}\n` +
                    `📄 **Content-Type:** ${contentType || 'Not specified'}\n` +
                    `⚠️ This URL does not point to an image resource.`
            }
          ]
        };
      }

      const sizeMB = contentLength ? (parseInt(contentLength) / (1024 * 1024)).toFixed(2) : 'Unknown';
      const sizeKB = contentLength ? (parseInt(contentLength) / 1024).toFixed(2) : 'Unknown';

      let result = `🖼️ **Image Information**\n\n` +
                  `📍 **URL:** ${url}\n` +
                  `📄 **Content Type:** ${contentType}\n` +
                  `📊 **Size:** ${sizeMB}MB (${sizeKB}KB)\n`;

      if (contentLength) {
        result += `📏 **Bytes:** ${parseInt(contentLength).toLocaleString()}\n`;
      }

      if (lastModified) {
        result += `📅 **Last Modified:** ${lastModified}\n`;
      }

      if (etag) {
        result += `🏷️ **ETag:** ${etag}\n`;
      }

      if (cacheControl) {
        result += `🗄️ **Cache Control:** ${cacheControl}\n`;
      }

      if (server) {
        result += `🖥️ **Server:** ${server}\n`;
      }

      // Additional headers analysis
      const additionalHeaders = [];
      response.headers.forEach((value, key) => {
        if (!['content-type', 'content-length', 'last-modified', 'etag', 'cache-control', 'server'].includes(key.toLowerCase())) {
          additionalHeaders.push(`   ${key}: ${value}`);
        }
      });

      if (additionalHeaders.length > 0) {
        result += `\n🔍 **Additional Headers:**\n${additionalHeaders.join('\n')}\n`;
      }

      result += `\n✅ **Status:** Image accessible and ready for download`;

      return {
        content: [
          {
            type: 'text',
            text: result
          }
        ]
      };

    } catch (error) {
      return {
        content: [
          {
            type: 'text',
            text: `❌ Error getting image information: ${error.message}`
          }
        ]
      };
    }
  }

  // Helper method to detect image format from buffer
  static detectImageFormat(buffer) {
    if (buffer.length < 8) return null;
    
    // Check magic numbers
    const header = buffer.subarray(0, 8);
    
    if (header[0] === 0xFF && header[1] === 0xD8 && header[2] === 0xFF) {
      return 'JPEG';
    }
    
    if (header[0] === 0x89 && header[1] === 0x50 && header[2] === 0x4E && header[3] === 0x47) {
      return 'PNG';
    }
    
    if (header[0] === 0x47 && header[1] === 0x49 && header[2] === 0x46) {
      return 'GIF';
    }
    
    if (header[0] === 0x42 && header[1] === 0x4D) {
      return 'BMP';
    }
    
    if (header[0] === 0x52 && header[1] === 0x49 && header[2] === 0x46 && header[3] === 0x46) {
      return 'WEBP';
    }
    
    return 'Unknown format';
  }

  // Helper method to get file extension from content type
  static getExtensionFromContentType(contentType) {
    const typeMap = {
      'image/jpeg': 'jpg',
      'image/jpg': 'jpg',
      'image/png': 'png',
      'image/gif': 'gif',
      'image/webp': 'webp',
      'image/svg+xml': 'svg',
      'image/bmp': 'bmp',
      'image/tiff': 'tiff'
    };
    
    return typeMap[contentType.toLowerCase()] || 'bin';
  }
}