import { ConfluenceService } from '../src/services/confluenceService.js';

async function testConfluenceConnection() {
  console.log('🔍 Testing Confluence connection...\n');

  try {
    // First, let's get all pages to see what's available
    console.log('1️⃣ Getting all pages from the space...');
    const allPages = await ConfluenceService.getConfluencePages({
      limit: 50
    });
    
    console.log('All pages result:');
    console.log(allPages.content[0].text);
    
    return allPages;
    
  } catch (error) {
    console.error('❌ Error:', error.message);
    return null;
  }
}

testConfluenceConnection().catch(console.error);