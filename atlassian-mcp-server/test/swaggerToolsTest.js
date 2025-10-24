import { SwaggerService } from '../src/services/swaggerService.js';

async function testSwaggerTools() {
  console.log('🔗 COMPREHENSIVE SWAGGER/OPENAPI TOOLS TEST SUITE\n');
  console.log('Testing all 3 Swagger/OpenAPI tools with the MCP server...\n');
  
  const testResults = {
    passed: 0,
    failed: 0,
    tests: []
  };

  let fetchedSwaggerSpec = null;

  // Test 1: search_swagger_specs
  console.log('1️⃣ Testing search_swagger_specs...');
  try {
    const searchResults = await SwaggerService.searchSwaggerSpecs({
      baseUrl: 'https://httpbin.org',
      customPaths: ['/spec', '/openapi.json']
    });
    console.log('✅ search_swagger_specs - SUCCESS');
    console.log(searchResults.content[0].text.substring(0, 400) + '...\n');
    testResults.passed++;
    testResults.tests.push({ name: 'search_swagger_specs', status: 'PASSED' });
  } catch (error) {
    console.log('❌ search_swagger_specs - FAILED:', error.message, '\n');
    testResults.failed++;
    testResults.tests.push({ name: 'search_swagger_specs', status: 'FAILED', error: error.message });
  }

  // Test 2: fetch_swagger_spec (using a known public API)
  console.log('2️⃣ Testing fetch_swagger_spec...');
  try {
    // Using JSONPlaceholder API which has OpenAPI spec
    const swaggerSpec = await SwaggerService.fetchSwaggerSpec({
      url: 'https://jsonplaceholder.typicode.com/schema',
      format: 'auto'
    });
    console.log('✅ fetch_swagger_spec - SUCCESS');
    console.log(swaggerSpec.content[0].text.substring(0, 400) + '...\n');
    
    // Store the spec for parsing test
    fetchedSwaggerSpec = swaggerSpec;
    
    testResults.passed++;
    testResults.tests.push({ name: 'fetch_swagger_spec', status: 'PASSED' });
  } catch (error) {
    console.log('❌ fetch_swagger_spec - FAILED:', error.message);
    console.log('   Trying alternative Swagger spec URL...\n');
    
    // Try alternative API
    try {
      const altSwaggerSpec = await SwaggerService.fetchSwaggerSpec({
        url: 'https://petstore.swagger.io/v2/swagger.json',
        format: 'json'
      });
      console.log('✅ fetch_swagger_spec (alternative) - SUCCESS');
      console.log(altSwaggerSpec.content[0].text.substring(0, 400) + '...\n');
      
      fetchedSwaggerSpec = altSwaggerSpec;
      testResults.passed++;
      testResults.tests.push({ name: 'fetch_swagger_spec', status: 'PASSED', note: 'Used Petstore API as fallback' });
    } catch (altError) {
      console.log('❌ fetch_swagger_spec (alternative) - FAILED:', altError.message, '\n');
      testResults.failed++;
      testResults.tests.push({ name: 'fetch_swagger_spec', status: 'FAILED', error: altError.message });
    }
  }

  // Test 3: parse_swagger_spec (using fetched spec or sample)
  console.log('3️⃣ Testing parse_swagger_spec...');
  try {
    let specToParse;
    
    if (fetchedSwaggerSpec) {
      // Extract JSON from the fetched spec content
      const content = fetchedSwaggerSpec.content[0].text;
      const jsonMatch = content.match(/```json\n([\s\S]*?)\n```/);
      if (jsonMatch) {
        specToParse = JSON.parse(jsonMatch[1]);
      }
    }
    
    // Fallback to sample spec if parsing failed
    if (!specToParse) {
      specToParse = {
        swagger: '2.0',
        info: {
          title: 'MCP Test API',
          version: '1.0.0',
          description: 'Sample API for testing MCP server Swagger parsing capabilities'
        },
        host: 'api.example.com',
        basePath: '/v1',
        schemes: ['https'],
        paths: {
          '/images': {
            get: {
              summary: 'Get images',
              description: 'Retrieve a list of images',
              parameters: [
                {
                  name: 'limit',
                  in: 'query',
                  type: 'integer',
                  description: 'Number of images to return'
                }
              ],
              responses: {
                '200': {
                  description: 'Successful response',
                  schema: {
                    type: 'array',
                    items: {
                      '$ref': '#/definitions/Image'
                    }
                  }
                }
              }
            },
            post: {
              summary: 'Upload image',
              description: 'Upload a new image',
              parameters: [
                {
                  name: 'image',
                  in: 'formData',
                  type: 'file',
                  description: 'Image file to upload'
                }
              ],
              responses: {
                '201': {
                  description: 'Image uploaded successfully'
                }
              }
            }
          },
          '/images/{id}': {
            get: {
              summary: 'Get image by ID',
              parameters: [
                {
                  name: 'id',
                  in: 'path',
                  required: true,
                  type: 'string',
                  description: 'Image ID'
                }
              ],
              responses: {
                '200': {
                  description: 'Image details'
                },
                '404': {
                  description: 'Image not found'
                }
              }
            }
          }
        },
        definitions: {
          Image: {
            type: 'object',
            properties: {
              id: {
                type: 'string',
                description: 'Unique image identifier'
              },
              url: {
                type: 'string',
                description: 'Image URL'
              },
              filename: {
                type: 'string',
                description: 'Original filename'
              },
              size: {
                type: 'integer',
                description: 'File size in bytes'
              },
              contentType: {
                type: 'string',
                description: 'MIME type'
              },
              createdAt: {
                type: 'string',
                format: 'date-time',
                description: 'Creation timestamp'
              }
            }
          }
        }
      };
    }

    const parseResult = await SwaggerService.parseSwaggerSpec({
      content: specToParse,
      url: 'https://api.example.com/swagger.json'
    });
    
    console.log('✅ parse_swagger_spec - SUCCESS');
    console.log(parseResult.content[0].text.substring(0, 400) + '...\n');
    testResults.passed++;
    testResults.tests.push({ name: 'parse_swagger_spec', status: 'PASSED' });
  } catch (error) {
    console.log('❌ parse_swagger_spec - FAILED:', error.message, '\n');
    testResults.failed++;
    testResults.tests.push({ name: 'parse_swagger_spec', status: 'FAILED', error: error.message });
  }

  // Test 4: fetch_swaggerhub_spec (using configured credentials)
  console.log('4️⃣ Testing fetch_swaggerhub_spec...');
  try {
    const swaggerhubSpec = await SwaggerService.fetchSwaggerHubSpec({
      owner: 'capgemini-5bf',
      api: 'policy-and-endorsement-api',
      version: '1.0.0'
    });
    console.log('✅ fetch_swaggerhub_spec - SUCCESS');
    console.log(swaggerhubSpec.content[0].text.substring(0, 400) + '...\n');
    testResults.passed++;
    testResults.tests.push({ name: 'fetch_swaggerhub_spec', status: 'PASSED' });
  } catch (error) {
    console.log('❌ fetch_swaggerhub_spec - FAILED:', error.message);
    console.log('   Note: This may fail if SwaggerHub credentials are not configured or API is private\n');
    testResults.failed++;
    testResults.tests.push({ name: 'fetch_swaggerhub_spec', status: 'FAILED', error: error.message, note: 'May require valid SwaggerHub credentials' });
  }

  // Display comprehensive test results
  console.log('='.repeat(80));
  console.log('📊 SWAGGER/OPENAPI TOOLS TEST RESULTS SUMMARY');
  console.log('='.repeat(80));
  console.log(`✅ Tests Passed: ${testResults.passed}`);
  console.log(`❌ Tests Failed: ${testResults.failed}`);
  console.log(`📈 Success Rate: ${((testResults.passed / (testResults.passed + testResults.failed)) * 100).toFixed(1)}%`);
  console.log('\n📋 Detailed Results:');
  
  testResults.tests.forEach((test, index) => {
    const status = test.status === 'PASSED' ? '✅' : '❌';
    console.log(`   ${index + 1}. ${status} ${test.name} - ${test.status}`);
    if (test.note) {
      console.log(`      Note: ${test.note}`);
    }
    if (test.error) {
      console.log(`      Error: ${test.error}`);
    }
  });

  console.log('\n💡 Swagger/OpenAPI Tools Information:');
  console.log('   • These tools can fetch API documentation from various sources');
  console.log('   • Support for both public APIs and private SwaggerHub repositories');
  console.log('   • Automatic format detection (JSON/YAML)');
  console.log('   • Comprehensive parsing and analysis capabilities');
  console.log('   • Integration with Confluence documentation workflows');

  console.log('\n🎉 Swagger/OpenAPI tools testing completed!');
  return testResults;
}

// Export for use in master test runner
export { testSwaggerTools };

// Run directly if called
if (import.meta.url === `file://${process.argv[1]}`) {
  testSwaggerTools().catch(console.error);
}