export const imageToolSchemas = [
  {
    name: 'read_image_from_url',
    description: 'Read and analyze an image from a URL, extracting metadata and basic information',
    inputSchema: {
      type: 'object',
      properties: {
        url: {
          type: 'string',
          description: 'The URL of the image to read and analyze'
        },
        includeMetadata: {
          type: 'boolean',
          description: 'Whether to include detailed metadata (EXIF data, etc.)',
          default: true
        },
        maxSize: {
          type: 'number',
          description: 'Maximum file size in MB to download (default: 10MB)',
          default: 10
        }
      },
      required: ['url']
    }
  },
  {
    name: 'download_image',
    description: 'Download an image from a URL and save it to a specified location',
    inputSchema: {
      type: 'object',
      properties: {
        url: {
          type: 'string',
          description: 'The URL of the image to download'
        },
        fileName: {
          type: 'string',
          description: 'The name to save the file as (optional, will use URL filename if not provided)'
        },
        downloadPath: {
          type: 'string',
          description: 'The directory path to save the image (default: current directory)',
          default: '.'
        },
        maxSize: {
          type: 'number',
          description: 'Maximum file size in MB to download (default: 50MB)',
          default: 50
        },
        overwrite: {
          type: 'boolean',
          description: 'Whether to overwrite existing files with the same name',
          default: false
        }
      },
      required: ['url']
    }
  },
  {
    name: 'read_image_from_confluence',
    description: 'Read and download images attached to a Confluence page',
    inputSchema: {
      type: 'object',
      properties: {
        pageId: {
          type: 'string',
          description: 'The Confluence page ID to read images from'
        },
        downloadPath: {
          type: 'string',
          description: 'The directory path to save images (optional)',
          default: './confluence-images'
        },
        includeMetadata: {
          type: 'boolean',
          description: 'Whether to include detailed metadata for each image',
          default: true
        }
      },
      required: ['pageId']
    }
  },
  {
    name: 'extract_images_from_jira',
    description: 'Extract and download images from Jira issue attachments',
    inputSchema: {
      type: 'object',
      properties: {
        issueKey: {
          type: 'string',
          description: 'The Jira issue key (e.g., PROJ-123)'
        },
        downloadPath: {
          type: 'string',
          description: 'The directory path to save images (optional)',
          default: './jira-images'
        },
        imageTypes: {
          type: 'array',
          items: {
            type: 'string'
          },
          description: 'Array of image file extensions to filter (e.g., ["jpg", "png", "gif"])',
          default: ['jpg', 'jpeg', 'png', 'gif', 'webp', 'svg', 'bmp']
        }
      },
      required: ['issueKey']
    }
  },
  {
    name: 'get_image_info',
    description: 'Get detailed information about an image file without downloading it',
    inputSchema: {
      type: 'object',
      properties: {
        url: {
          type: 'string',
          description: 'The URL of the image to analyze'
        },
        headers: {
          type: 'object',
          description: 'Additional headers to send with the request',
          additionalProperties: {
            type: 'string'
          }
        }
      },
      required: ['url']
    }
  }
];