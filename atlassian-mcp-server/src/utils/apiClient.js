import fetch from 'node-fetch';

export class ApiClient {
  static async makeJiraRequest(baseUrl, email, apiToken, endpoint, method = 'GET', body = null) {
    const url = `${baseUrl}/rest/api/3/${endpoint}`;
    console.error(`[JiraRequest] ${method} ${url}`);
    
    const auth = Buffer.from(`${email}:${apiToken}`).toString('base64');

    const options = {
      method,
      headers: {
        'Authorization': `Basic ${auth}`,
        'Accept': 'application/json',
        'Content-Type': 'application/json',
      },
    };

    if (body) {
      options.body = JSON.stringify(body);
    }

    const response = await fetch(url, options);
    
    if (!response.ok) {
      console.error(`[JiraRequest][Error] ${response.status} ${response.statusText}`);
      throw new Error(`Jira API request failed: ${response.status} ${response.statusText}`);
    }

    return await response.json();
  }

  static async makeConfluenceRequest(baseUrl, email, apiToken, endpoint, method = 'GET', body = null) {
    const url = `${baseUrl}/wiki/rest/api/${endpoint}`;
    console.error(`[ConfluenceRequest] ${method} ${url}`);
    
    const auth = Buffer.from(`${email}:${apiToken}`).toString('base64');

    const options = {
      method,
      headers: {
        'Authorization': `Basic ${auth}`,
        'Accept': 'application/json',
        'Content-Type': 'application/json',
      },
    };

    if (body) {
      options.body = JSON.stringify(body);
    }

    const response = await fetch(url, options);
    
    if (!response.ok) {
      console.error(`[ConfluenceRequest][Error] ${response.status} ${response.statusText}`);
      throw new Error(`Confluence API request failed: ${response.status} ${response.statusText}`);
    }

    const text = await response.text();
    return text ? JSON.parse(text) : {};
  }
}