import { ImageService } from '../src/services/imageService.js';
import { ConfluenceService } from '../src/services/confluenceService.js';

async function extractPolicyOrchestratorImages() {
  console.log('🖼️ Extracting images from Policy Orchestrator [MS-016] page...\n');

  const pageId = '50298881'; // Found from previous search
  
  try {
    // First, get the page content to see what's there
    console.log('1️⃣ Getting page content...');
    const pageContent = await ConfluenceService.getConfluencePage({
      pageId: pageId,
      expand: 'body.storage,version,space'
    });
    
    console.log('Page content:');
    console.log(pageContent.content[0].text.substring(0, 1000) + '...\n');
    
    // Now extract images
    console.log('2️⃣ Extracting images...');
    const imageResult = await ImageService.readImageFromConfluence({
      pageId: pageId,
      downloadPath: './policy-orchestrator-images',
      includeMetadata: true
    });
    
    console.log('Image extraction result:');
    console.log(imageResult.content[0].text);
    
    return imageResult;
    
  } catch (error) {
    console.error('❌ Error:', error.message);
    return null;
  }
}

extractPolicyOrchestratorImages().catch(console.error);