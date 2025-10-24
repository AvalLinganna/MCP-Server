import { JiraService } from '../src/services/jiraService.js';

async function testJiraTools() {
  console.log('🎯 COMPREHENSIVE JIRA TOOLS TEST SUITE\n');
  console.log('Testing all 6 Jira tools with the MCP server...\n');
  
  const testResults = {
    passed: 0,
    failed: 0,
    tests: []
  };

  // Test 1: get_project_info
  console.log('1️⃣ Testing get_project_info...');
  try {
    const projectInfo = await JiraService.getProjectInfo();
    console.log('✅ get_project_info - SUCCESS');
    console.log(projectInfo.content[0].text.substring(0, 300) + '...\n');
    testResults.passed++;
    testResults.tests.push({ name: 'get_project_info', status: 'PASSED' });
  } catch (error) {
    console.log('❌ get_project_info - FAILED:', error.message, '\n');
    testResults.failed++;
    testResults.tests.push({ name: 'get_project_info', status: 'FAILED', error: error.message });
  }

  // Test 2: get_jira_issues
  console.log('2️⃣ Testing get_jira_issues...');
  try {
    const issues = await JiraService.getJiraIssues({
      maxResults: 10,
      jql: 'project = KAN ORDER BY created DESC'
    });
    console.log('✅ get_jira_issues - SUCCESS');
    console.log(issues.content[0].text.substring(0, 400) + '...\n');
    testResults.passed++;
    testResults.tests.push({ name: 'get_jira_issues', status: 'PASSED' });
  } catch (error) {
    console.log('❌ get_jira_issues - FAILED:', error.message, '\n');
    testResults.failed++;
    testResults.tests.push({ name: 'get_jira_issues', status: 'FAILED', error: error.message });
  }

  // Test 3: get_user_stories
  console.log('3️⃣ Testing get_user_stories...');
  try {
    const userStories = await JiraService.getUserStories({
      maxResults: 5
    });
    console.log('✅ get_user_stories - SUCCESS');
    console.log(userStories.content[0].text.substring(0, 400) + '...\n');
    testResults.passed++;
    testResults.tests.push({ name: 'get_user_stories', status: 'PASSED' });
  } catch (error) {
    console.log('❌ get_user_stories - FAILED:', error.message, '\n');
    testResults.failed++;
    testResults.tests.push({ name: 'get_user_stories', status: 'FAILED', error: error.message });
  }

  // Test 4: create_jira_issue (TEST ISSUE)
  console.log('4️⃣ Testing create_jira_issue...');
  let createdIssueKey = null;
  try {
    const newIssue = await JiraService.createJiraIssue({
      summary: '[TEST] MCP Server Image Tools Integration Test',
      description: 'This is a test issue created by the MCP server test suite to validate the create_jira_issue functionality. This issue demonstrates the new image processing capabilities added to the MCP server.',
      issueType: 'Task',
      priority: 'Low'
    });
    console.log('✅ create_jira_issue - SUCCESS');
    console.log(newIssue.content[0].text.substring(0, 400) + '...\n');
    
    // Extract issue key for later tests
    const issueKeyMatch = newIssue.content[0].text.match(/Key:\s*([A-Z]+-\d+)/);
    if (issueKeyMatch) {
      createdIssueKey = issueKeyMatch[1];
      console.log(`📝 Created issue key: ${createdIssueKey}\n`);
    }
    
    testResults.passed++;
    testResults.tests.push({ name: 'create_jira_issue', status: 'PASSED', issueKey: createdIssueKey });
  } catch (error) {
    console.log('❌ create_jira_issue - FAILED:', error.message, '\n');
    testResults.failed++;
    testResults.tests.push({ name: 'create_jira_issue', status: 'FAILED', error: error.message });
  }

  // Test 5: get_jira_issue (using created issue or fallback)
  console.log('5️⃣ Testing get_jira_issue...');
  const testIssueKey = createdIssueKey || 'KAN-1'; // Use created issue or fallback
  try {
    const issue = await JiraService.getJiraIssue({
      issueKey: testIssueKey,
      fields: 'summary,description,status,assignee,priority,issuetype,created'
    });
    console.log(`✅ get_jira_issue (${testIssueKey}) - SUCCESS`);
    console.log(issue.content[0].text.substring(0, 400) + '...\n');
    testResults.passed++;
    testResults.tests.push({ name: 'get_jira_issue', status: 'PASSED', issueKey: testIssueKey });
  } catch (error) {
    console.log(`❌ get_jira_issue (${testIssueKey}) - FAILED:`, error.message, '\n');
    testResults.failed++;
    testResults.tests.push({ name: 'get_jira_issue', status: 'FAILED', error: error.message });
  }

  // Test 6: update_jira_issue (using created issue or fallback)
  console.log('6️⃣ Testing update_jira_issue...');
  try {
    const updateResult = await JiraService.updateJiraIssue({
      issueKey: testIssueKey,
      comment: `Test comment added by MCP server test suite at ${new Date().toISOString()}. This validates the update_jira_issue functionality and image processing integration.`,
      labels: ['mcp-test', 'image-tools', 'automated-test']
    });
    console.log(`✅ update_jira_issue (${testIssueKey}) - SUCCESS`);
    console.log(updateResult.content[0].text.substring(0, 400) + '...\n');
    testResults.passed++;
    testResults.tests.push({ name: 'update_jira_issue', status: 'PASSED', issueKey: testIssueKey });
  } catch (error) {
    console.log(`❌ update_jira_issue (${testIssueKey}) - FAILED:`, error.message, '\n');
    testResults.failed++;
    testResults.tests.push({ name: 'update_jira_issue', status: 'FAILED', error: error.message });
  }

  // Display comprehensive test results
  console.log('='.repeat(80));
  console.log('📊 JIRA TOOLS TEST RESULTS SUMMARY');
  console.log('='.repeat(80));
  console.log(`✅ Tests Passed: ${testResults.passed}`);
  console.log(`❌ Tests Failed: ${testResults.failed}`);
  console.log(`📈 Success Rate: ${((testResults.passed / (testResults.passed + testResults.failed)) * 100).toFixed(1)}%`);
  console.log('\n📋 Detailed Results:');
  
  testResults.tests.forEach((test, index) => {
    const status = test.status === 'PASSED' ? '✅' : '❌';
    console.log(`   ${index + 1}. ${status} ${test.name} - ${test.status}`);
    if (test.issueKey) {
      console.log(`      Issue Key: ${test.issueKey}`);
    }
    if (test.error) {
      console.log(`      Error: ${test.error}`);
    }
  });

  if (createdIssueKey) {
    console.log('\n💡 Note: Test issue created with key:', createdIssueKey);
    console.log('   You may want to close or delete this test issue after verification.');
  }

  console.log('\n🎉 Jira tools testing completed!');
  return testResults;
}

// Export for use in master test runner
export { testJiraTools };

// Run directly if called
if (import.meta.url === `file://${process.argv[1]}`) {
  testJiraTools().catch(console.error);
}