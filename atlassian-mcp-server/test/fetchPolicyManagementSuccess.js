import { ConfluenceService } from '../src/services/confluenceService.js';
import { ImageService } from '../src/services/imageService.js';

async function fetchPolicyManagementPageAndImages() {
  console.log('🎯 POLICY MANAGEMENT PAGE & IMAGE EXTRACTION');
  console.log('='.repeat(60));
  console.log('Found Policy Management page in ~test space!\n');

  const pageId = '2621441';
  
  try {
    // Step 1: Get the full Policy Management page content
    console.log('1️⃣ Retrieving Policy Management page content...');
    const policyPage = await ConfluenceService.getConfluencePage({
      pageId: pageId,
      expand: 'body.storage,version,space'
    });
    
    console.log('✅ Successfully retrieved Policy Management page:');
    console.log(policyPage.content[0].text);
    console.log('\n');
    
    // Step 2: Extract images from the page
    console.log('2️⃣ Extracting images from Policy Management page...');
    const imageResult = await ImageService.readImageFromConfluence({
      pageId: pageId,
      downloadPath: './policy-management-images',
      includeMetadata: true
    });
    
    console.log('🖼️ Image extraction results:');
    console.log(imageResult.content[0].text);
    console.log('\n');
    
    // Step 3: Generate comprehensive report
    console.log('='.repeat(80));
    console.log('📋 POLICY MANAGEMENT PAGE ANALYSIS REPORT');
    console.log('='.repeat(80));
    
    console.log('\n📄 PAGE INFORMATION:');
    console.log('✅ Policy Management page found and retrieved');
    console.log(`   Page ID: ${pageId}`);
    console.log('   Space: ~test');
    console.log('   URL: https://capgemini-team-a7b68jv7.atlassian.net//wiki/spaces/~test/pages/2621441/Policy+Management');
    
    // Extract page details
    const pageText = policyPage.content[0].text;
    const titleMatch = pageText.match(/\*\*([^*]+)\*\*/);
    if (titleMatch) {
      console.log(`   Title: ${titleMatch[1]}`);
    }
    
    const versionMatch = pageText.match(/Version:\s*(\d+)/);
    if (versionMatch) {
      console.log(`   Version: ${versionMatch[1]}`);
    }
    
    const createdMatch = pageText.match(/Created:\s*([^\\n]+)/);
    if (createdMatch) {
      console.log(`   Created: ${createdMatch[1]}`);
    }
    
    console.log('\n🖼️ IMAGE ANALYSIS:');
    const imageText = imageResult.content[0].text;
    
    if (imageText.includes('Found 0 total attachments')) {
      console.log('📝 No images attached to the Policy Management page');
      console.log('   This is normal - the page exists but has no image attachments');
      console.log('   Consider adding relevant images to enhance the documentation:');
      console.log('     • Process flow diagrams');
      console.log('     • System architecture diagrams'); 
      console.log('     • User interface screenshots');
      console.log('     • Policy workflow charts');
    } else if (imageText.includes('Downloaded') && !imageText.includes('0/')) {
      console.log('✅ Images successfully downloaded from Policy Management page');
      
      const downloadMatch = imageText.match(/Downloaded (\d+)\/(\d+) images/);
      if (downloadMatch) {
        console.log(`   Success rate: ${downloadMatch[1]} of ${downloadMatch[2]} images`);
      }
      
      const pathMatch = imageText.match(/to ([^\\s]+)/);
      if (pathMatch) {
        console.log(`   Location: ${pathMatch[1]}`);
      }
      
      console.log('\n   📁 Downloaded images can be used for:');
      console.log('     • Documentation enhancement');
      console.log('     • Process analysis');
      console.log('     • Training materials');
      console.log('     • System documentation');
    } else {
      console.log('⚠️ Mixed results during image extraction');
      console.log('   Check the detailed output above for specific information');
    }
    
    console.log('\n💡 POLICY MANAGEMENT PAGE CONTENT PREVIEW:');
    if (pageText.includes('Content:')) {
      const contentIndex = pageText.indexOf('Content:');
      const contentPreview = pageText.substring(contentIndex + 8, contentIndex + 300);
      console.log('   ' + contentPreview.replace(/\n/g, '\n   ') + '...');
    }
    
    console.log('\n🎯 TASK COMPLETION STATUS:');
    console.log('   ✅ Policy Management page found');
    console.log('   ✅ Page content retrieved and analyzed');
    console.log('   ✅ Image extraction attempted');
    console.log('   ✅ Comprehensive report generated');
    
    console.log('\n🔧 AVAILABLE ACTIONS:');
    console.log('   • View page content in detail above');
    console.log('   • Check ./policy-management-images/ for downloaded images');
    console.log('   • Use page content for documentation or analysis');
    console.log('   • Add images to the page if needed for enhancement');
    
    console.log('\n='.repeat(80));
    console.log('🏁 POLICY MANAGEMENT ANALYSIS COMPLETE');
    console.log('='.repeat(80));
    
    return {
      success: true,
      pageId: pageId,
      pageContent: policyPage,
      imageResult: imageResult
    };
    
  } catch (error) {
    console.error('❌ Error during Policy Management page analysis:', error.message);
    return {
      success: false,
      error: error.message
    };
  }
}

// Run the analysis
fetchPolicyManagementPageAndImages().catch(console.error);