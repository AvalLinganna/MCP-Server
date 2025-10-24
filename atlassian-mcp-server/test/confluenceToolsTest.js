import { ConfluenceService } from '../src/services/confluenceService.js';

async function testConfluenceTools() {
  console.log('📚 COMPREHENSIVE CONFLUENCE TOOLS TEST SUITE\n');
  console.log('Testing all Confluence tools with the MCP server...\n');
  
  const testResults = {
    passed: 0,
    failed: 0,
    tests: []
  };

  let createdPageId = null;
  const testPageTitle = `[TEST] MCP Image Tools Test Page - ${new Date().toISOString().split('T')[0]}`;

  // Test 1: get_confluence_pages
  console.log('1️⃣ Testing get_confluence_pages...');
  try {
    const pages = await ConfluenceService.getConfluencePages({
      limit: 10
    });
    console.log('✅ get_confluence_pages - SUCCESS');
    console.log(pages.content[0].text.substring(0, 400) + '...\n');
    testResults.passed++;
    testResults.tests.push({ name: 'get_confluence_pages', status: 'PASSED' });
  } catch (error) {
    console.log('❌ get_confluence_pages - FAILED:', error.message, '\n');
    testResults.failed++;
    testResults.tests.push({ name: 'get_confluence_pages', status: 'FAILED', error: error.message });
  }

  // Test 2: create_confluence_page
  console.log('2️⃣ Testing create_confluence_page...');
  try {
    const newPage = await ConfluenceService.createConfluencePage({
      title: testPageTitle,
      content: `
        <h1>MCP Server Image Tools Test Page</h1>
        <p>This page was created by the MCP server test suite to validate Confluence integration.</p>
        
        <h2>Image Processing Capabilities</h2>
        <p>The MCP server now includes the following image processing tools:</p>
        <ul>
          <li><strong>read_image_from_url</strong> - Analyze images from URLs</li>
          <li><strong>download_image</strong> - Download images with size limits</li>
          <li><strong>read_image_from_confluence</strong> - Extract images from Confluence pages</li>
          <li><strong>extract_images_from_jira</strong> - Download images from Jira attachments</li>
          <li><strong>get_image_info</strong> - Get image metadata without downloading</li>
        </ul>
        
        <h2>Test Information</h2>
        <p><strong>Created:</strong> ${new Date().toISOString()}</p>
        <p><strong>Purpose:</strong> Automated testing of MCP server tools</p>
        <p><strong>Status:</strong> This is a test page and can be safely deleted after validation</p>
        
        <h2>Integration Features</h2>
        <p>This page demonstrates the seamless integration between:</p>
        <ul>
          <li>Jira issue management</li>
          <li>Confluence documentation</li>
          <li>Image processing workflows</li>
          <li>Swagger/OpenAPI documentation</li>
        </ul>
      `
    });
    console.log('✅ create_confluence_page - SUCCESS');
    console.log(newPage.content[0].text.substring(0, 400) + '...\n');
    
    // Extract page ID for later tests
    const pageIdMatch = newPage.content[0].text.match(/ID:\s*(\d+)/);
    if (pageIdMatch) {
      createdPageId = pageIdMatch[1];
      console.log(`📝 Created page ID: ${createdPageId}\n`);
    }
    
    testResults.passed++;
    testResults.tests.push({ name: 'create_confluence_page', status: 'PASSED', pageId: createdPageId });
  } catch (error) {
    console.log('❌ create_confluence_page - FAILED:', error.message, '\n');
    testResults.failed++;
    testResults.tests.push({ name: 'create_confluence_page', status: 'FAILED', error: error.message });
  }

  // Test 3: get_confluence_page (using created page or existing)
  console.log('3️⃣ Testing get_confluence_page...');
  const testPageId = createdPageId || '50298881'; // Use created page or Policy Orchestrator page
  try {
    const page = await ConfluenceService.getConfluencePage({
      pageId: testPageId,
      expand: 'body.storage,version,space'
    });
    console.log(`✅ get_confluence_page (${testPageId}) - SUCCESS`);
    console.log(page.content[0].text.substring(0, 400) + '...\n');
    testResults.passed++;
    testResults.tests.push({ name: 'get_confluence_page', status: 'PASSED', pageId: testPageId });
  } catch (error) {
    console.log(`❌ get_confluence_page (${testPageId}) - FAILED:`, error.message, '\n');
    testResults.failed++;
    testResults.tests.push({ name: 'get_confluence_page', status: 'FAILED', error: error.message });
  }

  // Test 4: update_confluence_page (using created page)
  console.log('4️⃣ Testing update_confluence_page...');
  if (createdPageId) {
    try {
      const updatedPage = await ConfluenceService.updateConfluencePage({
        pageId: createdPageId,
        content: `
          <h1>MCP Server Image Tools Test Page - UPDATED</h1>
          <p><strong>UPDATE:</strong> This page was successfully updated by the MCP server test suite!</p>
          
          <h2>Test Results Summary</h2>
          <p>The following tests have been completed:</p>
          <ul>
            <li>✅ Confluence page creation</li>
            <li>✅ Confluence page retrieval</li>
            <li>✅ Confluence page update (this update!)</li>
            <li>🔄 Image processing tools integration</li>
          </ul>
          
          <h2>Image Processing Capabilities</h2>
          <p>The MCP server now includes comprehensive image processing:</p>
          <ul>
            <li><strong>read_image_from_url</strong> - Analyze images from URLs with metadata</li>
            <li><strong>download_image</strong> - Download images with size limits and overwrite protection</li>
            <li><strong>read_image_from_confluence</strong> - Extract all images from Confluence pages</li>
            <li><strong>extract_images_from_jira</strong> - Download images from Jira issue attachments</li>
            <li><strong>get_image_info</strong> - Get detailed image information without downloading</li>
          </ul>
          
          <h2>Test Information</h2>
          <p><strong>Originally Created:</strong> ${new Date().toISOString()}</p>
          <p><strong>Last Updated:</strong> ${new Date().toISOString()}</p>
          <p><strong>Update Test:</strong> ✅ SUCCESSFUL</p>
          <p><strong>Status:</strong> This is a test page and can be safely deleted after validation</p>
          
          <h2>Next Steps</h2>
          <p>After testing completion:</p>
          <ol>
            <li>Verify all tool functionality</li>
            <li>Test image upload and extraction</li>
            <li>Validate Jira integration</li>
            <li>Test Swagger/OpenAPI tools</li>
            <li>Clean up test data</li>
          </ol>
        `
      });
      console.log(`✅ update_confluence_page (${createdPageId}) - SUCCESS`);
      console.log(updatedPage.content[0].text.substring(0, 400) + '...\n');
      testResults.passed++;
      testResults.tests.push({ name: 'update_confluence_page', status: 'PASSED', pageId: createdPageId });
    } catch (error) {
      console.log(`❌ update_confluence_page (${createdPageId}) - FAILED:`, error.message, '\n');
      testResults.failed++;
      testResults.tests.push({ name: 'update_confluence_page', status: 'FAILED', error: error.message });
    }
  } else {
    console.log('❌ update_confluence_page - SKIPPED (no test page created)\n');
    testResults.tests.push({ name: 'update_confluence_page', status: 'SKIPPED', reason: 'No test page available' });
  }

  // Test 5: get_specific_confluence_page
  console.log('5️⃣ Testing get_specific_confluence_page...');
  try {
    const specificPage = await ConfluenceService.getSpecificConfluencePage({
      pageName: 'Policy Management',
      includeContent: false,
      format: 'summary'
    });
    console.log('✅ get_specific_confluence_page - SUCCESS');
    console.log(specificPage.content[0].text.substring(0, 400) + '...\n');
    testResults.passed++;
    testResults.tests.push({ name: 'get_specific_confluence_page', status: 'PASSED' });
  } catch (error) {
    console.log('❌ get_specific_confluence_page - FAILED:', error.message, '\n');
    testResults.failed++;
    testResults.tests.push({ name: 'get_specific_confluence_page', status: 'FAILED', error: error.message });
  }

  // Test 6: get_all_spaces_content
  console.log('6️⃣ Testing get_all_spaces_content...');
  try {
    const spacesContent = await ConfluenceService.getAllSpacesContent({
      limit: 5,
      includeContent: false
    });
    console.log('✅ get_all_spaces_content - SUCCESS');
    console.log(spacesContent.content[0].text.substring(0, 400) + '...\n');
    testResults.passed++;
    testResults.tests.push({ name: 'get_all_spaces_content', status: 'PASSED' });
  } catch (error) {
    console.log('❌ get_all_spaces_content - FAILED:', error.message, '\n');
    testResults.failed++;
    testResults.tests.push({ name: 'get_all_spaces_content', status: 'FAILED', error: error.message });
  }

  // Test 7: search_across_spaces
  console.log('7️⃣ Testing search_across_spaces...');
  try {
    const searchResults = await ConfluenceService.searchAcrossSpaces({
      query: 'Policy Orchestrator',
      limit: 3
    });
    console.log('✅ search_across_spaces - SUCCESS');
    console.log(searchResults.content[0].text.substring(0, 400) + '...\n');
    testResults.passed++;
    testResults.tests.push({ name: 'search_across_spaces', status: 'PASSED' });
  } catch (error) {
    console.log('❌ search_across_spaces - FAILED:', error.message, '\n');
    testResults.failed++;
    testResults.tests.push({ name: 'search_across_spaces', status: 'FAILED', error: error.message });
  }

  // Display comprehensive test results
  console.log('='.repeat(80));
  console.log('📊 CONFLUENCE TOOLS TEST RESULTS SUMMARY');
  console.log('='.repeat(80));
  console.log(`✅ Tests Passed: ${testResults.passed}`);
  console.log(`❌ Tests Failed: ${testResults.failed}`);
  console.log(`⏭️ Tests Skipped: ${testResults.tests.filter(t => t.status === 'SKIPPED').length}`);
  console.log(`📈 Success Rate: ${((testResults.passed / (testResults.passed + testResults.failed)) * 100).toFixed(1)}%`);
  console.log('\n📋 Detailed Results:');
  
  testResults.tests.forEach((test, index) => {
    const statusIcon = test.status === 'PASSED' ? '✅' : test.status === 'SKIPPED' ? '⏭️' : '❌';
    console.log(`   ${index + 1}. ${statusIcon} ${test.name} - ${test.status}`);
    if (test.pageId) {
      console.log(`      Page ID: ${test.pageId}`);
    }
    if (test.error) {
      console.log(`      Error: ${test.error}`);
    }
    if (test.reason) {
      console.log(`      Reason: ${test.reason}`);
    }
  });

  if (createdPageId) {
    console.log('\n💡 Note: Test page created with ID:', createdPageId);
    console.log('   Title:', testPageTitle);
    console.log('   You may want to delete this test page after verification.');
  }

  console.log('\n🎉 Confluence tools testing completed!');
  return testResults;
}

// Export for use in master test runner
export { testConfluenceTools };

// Run directly if called
if (import.meta.url === `file://${process.argv[1]}`) {
  testConfluenceTools().catch(console.error);
}