export const jiraToolSchemas = [
  {
    name: 'get_jira_issues',
    description: 'Get issues from Jira project',
    inputSchema: {
      type: 'object',
      properties: {
        jql: {
          type: 'string',
          description: 'JQL query to filter issues'
        },
        maxResults: {
          type: 'number',
          description: 'Maximum number of results to return',
          default: 50
        },
        fields: {
          type: 'string',
          description: 'Comma-separated list of fields to return',
          default: 'summary,description,status,assignee,priority,issuetype,created,updated'
        }
      },
    },
  },
  {
    name: 'get_user_stories',
    description: 'Get user stories from Jira project',
    inputSchema: {
      type: 'object',
      properties: {
        maxResults: {
          type: 'number',
          description: 'Maximum number of user stories to return',
          default: 50
        }
      },
    },
  },
  {
    name: 'create_jira_issue',
    description: 'Create a new issue in Jira project',
    inputSchema: {
      type: 'object',
      properties: {
        summary: {
          type: 'string',
          description: 'Issue summary/title'
        },
        description: {
          type: 'string',
          description: 'Issue description'
        },
        issueType: {
          type: 'string',
          description: 'Issue type (Story, Bug, Task, etc.)',
          default: 'Story'
        },
        priority: {
          type: 'string',
          description: 'Issue priority',
          default: 'Medium'
        }
      },
      required: ['summary', 'description']
    },
  },
  {
    name: 'get_project_info',
    description: 'Get information about the Jira project',
    inputSchema: {
      type: 'object',
      properties: {},
    },
  },
  {
    name: 'get_jira_issue',
    description: 'Get a specific Jira issue by key or ID',
    inputSchema: {
      type: 'object',
      properties: {
        issueKey: {
          type: 'string',
          description: 'Issue key or ID (e.g., PROJ-10066 or 10066)'
        },
        fields: {
          type: 'string',
          description: 'Comma-separated list of fields to return',
          default: 'summary,description,status,assignee,priority,issuetype,created,updated,reporter,labels,components'
        }
      },
      required: ['issueKey']
    },
  },
  {
    name: 'update_jira_issue',
    description: 'Update a Jira issue (status, assignee, comments, etc.)',
    inputSchema: {
      type: 'object',
      properties: {
        issueKey: {
          type: 'string',
          description: 'Issue key or ID (e.g., PROJ-1, PROJ-2)'
        },
        transition: {
          type: 'string',
          description: 'Status transition (e.g., "Done", "In Progress", "To Do")',
        },
        assignee: {
          type: 'string',
          description: 'Assignee account ID or email'
        },
        comment: {
          type: 'string',
          description: 'Comment to add to the issue'
        },
        summary: {
          type: 'string',
          description: 'Update issue summary/title'
        },
        description: {
          type: 'string',
          description: 'Update issue description'
        },
        priority: {
          type: 'string',
          description: 'Update issue priority (Highest, High, Medium, Low, Lowest)'
        },
        labels: {
          type: 'array',
          items: { type: 'string' },
          description: 'Array of labels to set on the issue'
        }
      },
      required: ['issueKey']
    },
  }
];