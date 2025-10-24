import { ApiClient } from '../utils/apiClient.js';
import { jiraConfig } from '../config/config.js';

export class JiraService {
  static async getJiraIssues(args) {
    const {
      jql = `project = "${jiraConfig.projectKey}"`,
      maxResults = 50,
      fields = 'summary,description,status,assignee,priority,issuetype,created,updated'
    } = args;

    const response = await ApiClient.makeJiraRequest(
      jiraConfig.baseUrl,
      jiraConfig.email,
      jiraConfig.apiToken,
      `search/jql?jql=${encodeURIComponent(jql)}&maxResults=${maxResults}&fields=${fields}`
    );

    return {
      content: [
        {
          type: 'text',
          text: `Found ${response.issues.length} issues in ${jiraConfig.projectName}:\n\n${JSON.stringify(response, null, 2)}`
        }
      ]
    };
  }

  static async getUserStories(args) {
    const { maxResults = 50 } = args;
    const jql = `project = "${jiraConfig.projectKey}" AND issuetype = "Story"`;

    const response = await ApiClient.makeJiraRequest(
      jiraConfig.baseUrl,
      jiraConfig.email,
      jiraConfig.apiToken,
      `search/jql?jql=${encodeURIComponent(jql)}&maxResults=${maxResults}&fields=summary,description,status,assignee,priority,created,updated`
    );

    const userStories = response.issues.map(issue => ({
      key: issue.key,
      summary: issue.fields.summary,
      description: issue.fields.description,
      status: issue.fields.status.name,
      assignee: issue.fields.assignee ? issue.fields.assignee.displayName : 'Unassigned',
      priority: issue.fields.priority.name,
      created: issue.fields.created,
      updated: issue.fields.updated
    }));

    return {
      content: [
        {
          type: 'text',
          text: `Found ${userStories.length} user stories in ${jiraConfig.projectName}:\n\n${JSON.stringify(userStories, null, 2)}`
        }
      ]
    };
  }

  static async createJiraIssue(args) {
    const { summary, description, issueType = 'Story', priority = 'Medium' } = args;

    const issueData = {
      fields: {
        project: {
          key: jiraConfig.projectKey
        },
        summary,
        description: {
          type: 'doc',
          version: 1,
          content: [
            {
              type: 'paragraph',
              content: [
                {
                  type: 'text',
                  text: description
                }
              ]
            }
          ]
        },
        issuetype: {
          name: issueType
        },
        priority: {
          name: priority
        }
      }
    };

    const response = await ApiClient.makeJiraRequest(
      jiraConfig.baseUrl,
      jiraConfig.email,
      jiraConfig.apiToken,
      'issue',
      'POST',
      issueData
    );

    return {
      content: [
        {
          type: 'text',
          text: `Created issue ${response.key} in ${jiraConfig.projectName}:\n${JSON.stringify(response, null, 2)}`
        }
      ]
    };
  }

  static async getProjectInfo() {
    const response = await ApiClient.makeJiraRequest(
      jiraConfig.baseUrl,
      jiraConfig.email,
      jiraConfig.apiToken,
      `project/${jiraConfig.projectKey}`
    );

    return {
      content: [
        {
          type: 'text',
          text: `Project Information for ${jiraConfig.projectName}:\n\n${JSON.stringify(response, null, 2)}`
        }
      ]
    };
  }

  static async getJiraIssue(args) {
    const {
      issueKey,
      fields = 'summary,description,status,assignee,priority,issuetype,created,updated,reporter,labels,components'
    } = args;

    const fullIssueKey = issueKey.includes('-') ? issueKey : `${jiraConfig.projectKey}-${issueKey}`;

    try {
      const response = await ApiClient.makeJiraRequest(
        jiraConfig.baseUrl,
        jiraConfig.email,
        jiraConfig.apiToken,
        `issue/${fullIssueKey}?fields=${fields}`
      );

      return {
        content: [
          {
            type: 'text',
            text: `Issue ${response.key} details:\n\n${JSON.stringify(response, null, 2)}`
          }
        ]
      };
    } catch (error) {
      return {
        content: [
          {
            type: 'text',
            text: `Error retrieving issue ${fullIssueKey}: ${error.message}`
          }
        ]
      };
    }
  }

  static async updateJiraIssue(args) {
    const {
      issueKey,
      transition,
      assignee,
      comment,
      summary,
      description,
      priority,
      labels
    } = args;

    const fullIssueKey = issueKey.includes('-') ? issueKey : `${jiraConfig.projectKey}-${issueKey}`;

    try {
      let updateResults = [];

      // Handle status transition
      if (transition) {
        try {
          const transitionsResponse = await ApiClient.makeJiraRequest(
            jiraConfig.baseUrl,
            jiraConfig.email,
            jiraConfig.apiToken,
            `issue/${fullIssueKey}/transitions`
          );
          const availableTransitions = transitionsResponse.transitions;
          
          const targetTransition = availableTransitions.find(t => 
            t.name.toLowerCase() === transition.toLowerCase() ||
            t.to.name.toLowerCase() === transition.toLowerCase()
          );

          if (targetTransition) {
            await ApiClient.makeJiraRequest(
              jiraConfig.baseUrl,
              jiraConfig.email,
              jiraConfig.apiToken,
              `issue/${fullIssueKey}/transitions`,
              'POST',
              {
                transition: {
                  id: targetTransition.id
                }
              }
            );
            updateResults.push(`✅ Status updated to: ${targetTransition.to.name}`);
          } else {
            updateResults.push(`❌ Transition '${transition}' not available. Available: ${availableTransitions.map(t => t.name).join(', ')}`);
          }
        } catch (error) {
          updateResults.push(`❌ Failed to update status: ${error.message}`);
        }
      }

      // Handle field updates
      const fieldsToUpdate = {};
      
      if (summary) fieldsToUpdate.summary = summary;
      
      if (description) {
        fieldsToUpdate.description = {
          type: 'doc',
          version: 1,
          content: [
            {
              type: 'paragraph',
              content: [
                {
                  type: 'text',
                  text: description
                }
              ]
            }
          ]
        };
      }
      
      if (priority) fieldsToUpdate.priority = { name: priority };
      
      if (assignee) {
        if (assignee.includes('@')) {
          try {
            const userResponse = await ApiClient.makeJiraRequest(
              jiraConfig.baseUrl,
              jiraConfig.email,
              jiraConfig.apiToken,
              `user/search?query=${assignee}`
            );
            if (userResponse.length > 0) {
              fieldsToUpdate.assignee = { accountId: userResponse[0].accountId };
            } else {
              updateResults.push(`❌ User not found: ${assignee}`);
            }
          } catch (error) {
            updateResults.push(`❌ Failed to find user ${assignee}: ${error.message}`);
          }
        } else {
          fieldsToUpdate.assignee = { accountId: assignee };
        }
      }
      
      if (labels) {
        fieldsToUpdate.labels = labels.map(label => ({ add: label }));
      }

      // Update fields if any are specified
      if (Object.keys(fieldsToUpdate).length > 0) {
        try {
          await ApiClient.makeJiraRequest(
            jiraConfig.baseUrl,
            jiraConfig.email,
            jiraConfig.apiToken,
            `issue/${fullIssueKey}`,
            'PUT',
            { fields: fieldsToUpdate }
          );
          updateResults.push(`✅ Fields updated: ${Object.keys(fieldsToUpdate).join(', ')}`);
        } catch (error) {
          updateResults.push(`❌ Failed to update fields: ${error.message}`);
        }
      }

      // Add comment if provided
      if (comment) {
        try {
          await ApiClient.makeJiraRequest(
            jiraConfig.baseUrl,
            jiraConfig.email,
            jiraConfig.apiToken,
            `issue/${fullIssueKey}/comment`,
            'POST',
            {
              body: {
                type: 'doc',
                version: 1,
                content: [
                  {
                    type: 'paragraph',
                    content: [
                      {
                        type: 'text',
                        text: comment
                      }
                    ]
                  }
                ]
              }
            }
          );
          updateResults.push(`✅ Comment added successfully`);
        } catch (error) {
          updateResults.push(`❌ Failed to add comment: ${error.message}`);
        }
      }

      // Get updated issue details
      const updatedIssue = await ApiClient.makeJiraRequest(
        jiraConfig.baseUrl,
        jiraConfig.email,
        jiraConfig.apiToken,
        `issue/${fullIssueKey}?fields=summary,status,assignee,priority,updated`
      );

      return {
        content: [
          {
            type: 'text',
            text: `Updated issue ${fullIssueKey}:\n\n${updateResults.join('\n')}\n\nCurrent Status:\n- Summary: ${updatedIssue.fields.summary}\n- Status: ${updatedIssue.fields.status.name}\n- Assignee: ${updatedIssue.fields.assignee ? updatedIssue.fields.assignee.displayName : 'Unassigned'}\n- Priority: ${updatedIssue.fields.priority.name}\n- Last Updated: ${updatedIssue.fields.updated}`
          }
        ]
      };

    } catch (error) {
      return {
        content: [
          {
            type: 'text',
            text: `Error updating issue ${fullIssueKey}: ${error.message}`
          }
        ]
      };
    }
  }
}