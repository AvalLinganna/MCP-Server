import { ImageService } from '../src/services/imageService.js';

async function testImageService() {
  console.log('🧪 Testing Image Service...\n');

  // Test 1: Get image info
  console.log('1️⃣ Testing get_image_info...');
  try {
    const result = await ImageService.getImageInfo({
      url: 'https://httpbin.org/image/png'
    });
    console.log('✅ get_image_info test passed');
    console.log(result.content[0].text.substring(0, 200) + '...\n');
  } catch (error) {
    console.log('❌ get_image_info test failed:', error.message, '\n');
  }

  // Test 2: Read image from URL
  console.log('2️⃣ Testing read_image_from_url...');
  try {
    const result = await ImageService.readImageFromUrl({
      url: 'https://httpbin.org/image/jpeg',
      includeMetadata: true,
      maxSize: 5
    });
    console.log('✅ read_image_from_url test passed');
    console.log(result.content[0].text.substring(0, 200) + '...\n');
  } catch (error) {
    console.log('❌ read_image_from_url test failed:', error.message, '\n');
  }

  // Test 3: Download image (to test directory)
  console.log('3️⃣ Testing download_image...');
  try {
    const result = await ImageService.downloadImage({
      url: 'https://httpbin.org/image/png',
      fileName: 'test_image.png',
      downloadPath: './test-downloads',
      maxSize: 5,
      overwrite: true
    });
    console.log('✅ download_image test passed');
    console.log(result.content[0].text.substring(0, 200) + '...\n');
  } catch (error) {
    console.log('❌ download_image test failed:', error.message, '\n');
  }

  // Test 4: Test error handling with invalid URL
  console.log('4️⃣ Testing error handling...');
  try {
    const result = await ImageService.getImageInfo({
      url: 'https://httpbin.org/status/404'
    });
    console.log('❓ Expected error but got result:', result.content[0].text.substring(0, 100) + '...\n');
  } catch (error) {
    console.log('✅ Error handling test passed:', error.message, '\n');
  }

  console.log('🎉 Image Service testing completed!');
}

// Check if running directly
if (import.meta.url === `file://${process.argv[1]}`) {
  testImageService().catch(console.error);
}

export { testImageService };