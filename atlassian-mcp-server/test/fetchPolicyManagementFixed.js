import { ConfluenceService } from '../src/services/confluenceService.js';
import { ImageService } from '../src/services/imageService.js';

async function searchPolicyManagementInCorrectSpace() {
  console.log('🔍 SEARCHING FOR POLICY MANAGEMENT PAGE\n');
  console.log('Using correct Confluence space: MCPPoCZuri\n');

  try {
    // First, let's see what pages are available in the MCPPoCZuri space
    console.log('1️⃣ Getting all pages in MCPPoCZuri space...');
    const allPages = await ConfluenceService.getConfluencePages({
      spaceKey: 'MCPPoCZuri',
      limit: 50
    });
    
    console.log('📄 Available pages in space:');
    console.log(allPages.content[0].text);
    console.log('\n');
    
    // Look for Policy Management or related pages
    const pagesText = allPages.content[0].text;
    const policyKeywords = [
      'Policy Management',
      'Policy', 
      'policy',
      'Management',
      'management'
    ];
    
    let foundPolicyPage = false;
    let policyPageId = null;
    
    console.log('2️⃣ Searching for Policy Management related content...');
    
    for (const keyword of policyKeywords) {
      if (pagesText.includes(keyword)) {
        console.log(`✅ Found content containing "${keyword}"`);
        foundPolicyPage = true;
        
        // Try to extract page ID if this is the Policy Management page
        if (keyword.toLowerCase().includes('policy') && keyword.toLowerCase().includes('management')) {
          const lines = pagesText.split('\n');
          for (let i = 0; i < lines.length; i++) {
            if (lines[i].includes(keyword)) {
              // Look for ID in surrounding lines
              for (let j = Math.max(0, i-3); j < Math.min(lines.length, i+3); j++) {
                const idMatch = lines[j].match(/ID:\s*(\d+)/);
                if (idMatch) {
                  policyPageId = idMatch[1];
                  console.log(`📝 Found potential Policy Management page ID: ${policyPageId}`);
                  break;
                }
              }
              if (policyPageId) break;
            }
          }
        }
      }
    }
    
    if (!foundPolicyPage) {
      console.log('❌ No policy-related content found in available pages');
    }
    
    // Method 2: Try specific searches
    console.log('\n3️⃣ Trying specific page searches...');
    
    const searchTerms = [
      'Policy Management',
      'Policy',
      'Policies',
      'Management'
    ];
    
    for (const term of searchTerms) {
      try {
        console.log(`   Searching for: "${term}"`);
        const searchResult = await ConfluenceService.getConfluencePage({
          title: term,
          spaceKey: 'MCPPoCZuri'
        });
        
        if (!searchResult.content[0].text.includes('❌')) {
          console.log(`   ✅ Found page with title: "${term}"`);
          const idMatch = searchResult.content[0].text.match(/ID:\s*(\d+)/);
          if (idMatch) {
            policyPageId = idMatch[1];
            console.log(`   📝 Page ID: ${policyPageId}`);
            return { pageId: policyPageId, searchTerm: term, pageContent: searchResult };
          }
        }
      } catch (error) {
        console.log(`   ❌ Search for "${term}" failed: ${error.message}`);
      }
    }
    
    // Method 3: Search across spaces with CQL
    console.log('\n4️⃣ Performing cross-space search...');
    try {
      const searchResult = await ConfluenceService.searchAcrossSpaces({
        query: 'Policy Management',
        spaceKeys: ['MCPPoCZuri'],
        limit: 10
      });
      
      console.log('Search results:');
      console.log(searchResult.content[0].text);
      
      // Extract page ID from search results
      const searchText = searchResult.content[0].text;
      const idMatches = searchText.match(/ID:\s*(\d+)/g);
      if (idMatches && idMatches.length > 0) {
        policyPageId = idMatches[0].replace('ID: ', '');
        console.log(`📝 Found page ID from search: ${policyPageId}`);
      }
      
    } catch (error) {
      console.log('❌ Cross-space search failed:', error.message);
    }
    
    // If we found a potential page ID, try to get the full page
    if (policyPageId) {
      console.log(`\n5️⃣ Retrieving full page content for ID: ${policyPageId}...`);
      try {
        const fullPage = await ConfluenceService.getConfluencePage({
          pageId: policyPageId,
          expand: 'body.storage,version,space'
        });
        
        console.log('✅ Successfully retrieved page content:');
        console.log(fullPage.content[0].text.substring(0, 500) + '...\n');
        
        return { pageId: policyPageId, pageContent: fullPage };
      } catch (error) {
        console.log('❌ Failed to retrieve full page:', error.message);
      }
    }
    
    return { pageId: policyPageId, pageContent: null };
    
  } catch (error) {
    console.error('❌ Critical error in search:', error.message);
    return { pageId: null, pageContent: null };
  }
}

async function extractImagesFromPolicyPage(pageId) {
  console.log(`\n🖼️ EXTRACTING IMAGES FROM POLICY MANAGEMENT PAGE`);
  console.log(`Page ID: ${pageId}\n`);

  if (!pageId) {
    console.log('❌ No page ID available for image extraction');
    return null;
  }

  try {
    console.log('🔍 Checking for attached images...');
    
    const imageResult = await ImageService.readImageFromConfluence({
      pageId: pageId,
      downloadPath: './policy-management-images',
      includeMetadata: true
    });
    
    console.log('📊 Image extraction results:');
    console.log(imageResult.content[0].text);
    
    // Check if any images were found and downloaded
    const resultText = imageResult.content[0].text;
    if (resultText.includes('Downloaded') && resultText.includes('images to')) {
      console.log('\n✅ Images successfully downloaded!');
      
      // Extract download information
      const downloadMatch = resultText.match(/Downloaded (\d+)\/(\d+) images/);
      if (downloadMatch) {
        console.log(`📈 Success rate: ${downloadMatch[1]} of ${downloadMatch[2]} images downloaded`);
      }
      
      const pathMatch = resultText.match(/to ([^\s]+)/);
      if (pathMatch) {
        console.log(`📁 Download location: ${pathMatch[1]}`);
      }
    } else if (resultText.includes('Found 0 total attachments')) {
      console.log('\n📝 No images attached to this page');
      console.log('   This is normal - not all pages have image attachments');
    }
    
    return imageResult;
    
  } catch (error) {
    console.error('❌ Error extracting images:', error.message);
    return null;
  }
}

async function main() {
  console.log('🎯 POLICY MANAGEMENT PAGE FINDER & IMAGE EXTRACTOR');
  console.log('=' .repeat(60));
  console.log('Searching for Policy Management page in MCPPoCZuri space...\n');

  // Step 1: Search for Policy Management page
  const { pageId, pageContent } = await searchPolicyManagementInCorrectSpace();
  
  // Step 2: Extract images if page found
  let imageResult = null;
  if (pageId) {
    imageResult = await extractImagesFromPolicyPage(pageId);
  }
  
  // Step 3: Generate final report
  console.log('\n' + '='.repeat(80));
  console.log('📋 FINAL RESULTS SUMMARY');
  console.log('='.repeat(80));
  
  if (pageContent) {
    console.log('✅ POLICY MANAGEMENT PAGE: FOUND');
    console.log(`   Page ID: ${pageId}`);
    
    const pageText = pageContent.content[0].text;
    const titleMatch = pageText.match(/\*\*([^*]+)\*\*/);
    if (titleMatch) {
      console.log(`   Title: ${titleMatch[1]}`);
    }
    
    const urlMatch = pageText.match(/URL:\s*(https?:\/\/[^\s]+)/);
    if (urlMatch) {
      console.log(`   URL: ${urlMatch[1]}`);
    }
  } else {
    console.log('❌ POLICY MANAGEMENT PAGE: NOT FOUND');
    console.log('   The page may not exist in the MCPPoCZuri space');
    console.log('   Available pages were searched but no exact match found');
  }
  
  if (imageResult) {
    const imageText = imageResult.content[0].text;
    if (imageText.includes('Downloaded') && !imageText.includes('0/')) {
      console.log('✅ IMAGES: SUCCESSFULLY DOWNLOADED');
      console.log('   Check ./policy-management-images/ directory');
    } else if (imageText.includes('Found 0 total attachments')) {
      console.log('📝 IMAGES: NONE FOUND');
      console.log('   Page exists but has no image attachments');
    } else {
      console.log('⚠️ IMAGES: MIXED RESULTS');
      console.log('   Some images may have been processed');
    }
  } else {
    console.log('❌ IMAGES: NOT PROCESSED');
    console.log('   No page available for image extraction');
  }
  
  console.log('\n💡 NEXT STEPS:');
  if (!pageContent) {
    console.log('   1. Verify "Policy Management" page exists in MCPPoCZuri space');
    console.log('   2. Check if page title is exactly "Policy Management"');
    console.log('   3. Try alternative page titles or search terms');
  }
  
  if (pageContent && imageResult && imageResult.content[0].text.includes('Downloaded')) {
    console.log('   1. Review downloaded images in ./policy-management-images/');
    console.log('   2. Verify image content and quality');
    console.log('   3. Use images for documentation or analysis');
  }
  
  console.log('\n='.repeat(80));
  console.log('🏁 SEARCH AND EXTRACTION COMPLETE');
  console.log('='.repeat(80));
}

main().catch(console.error);