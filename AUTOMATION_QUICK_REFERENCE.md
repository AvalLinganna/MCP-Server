# Quick Reference Guide - Zurich Spring POC Automation

## 🚀 Quick Start

### Basic Usage
```
Story Number: KAN-X
Additional Context: [optional]
Validation Level: standard
```

### Example Commands
```
Story Number: KAN-5
Additional Context: Focus on claims API enhancement
Validation Level: comprehensive
```

## 🔧 MCP Tools Reference

| Tool | Purpose | Example Usage |
|------|---------|---------------|
| `mcp_atlassian-mcp_get_jira_issue` | Get story details | `issueKey="KAN-5"` |
| `mcp_atlassian-mcp_get_jira_issues` | List issues | `jql="project=KAN AND status='To Do'"` |
| `mcp_atlassian-mcp_search_across_spaces` | Find docs | `query="API specification KAN-5"` |
| `mcp_atlassian-mcp_get_specific_confluence_page` | Get design docs | `pageName="Technical Specifications"` |

## 🏗️ Build Commands Cheat Sheet

| Command | Purpose | When to Use |
|---------|---------|-------------|
| `mvn clean compile` | Basic compilation check | Before making changes |
| `mvn test -Punit-tests` | Unit tests only | Quick validation |
| `mvn verify -Pintegration-tests` | Integration tests | Full API testing |
| `mvn verify -Pall-tests` | Complete test suite | Final validation |
| `mvn jacoco:report` | Coverage report | Code quality check |

## 📁 Project Structure Quick Reference

```
src/main/java/com/zurich/poc/
├── controller/     → REST endpoints
├── service/        → Business logic
├── model/          → Data models  
├── repository/     → Data access
├── config/         → Configuration
└── exception/      → Error handling
```

## 🎯 Common Use Cases

### New Feature Development
1. Get story: `KAN-X`
2. Check design docs in Confluence
3. Implement in appropriate layer(s)
4. Run full test suite
5. Generate coverage report

### Bug Fix
1. Get bug details: `KAN-X`
2. Identify root cause area
3. Apply minimal fix
4. Run regression tests
5. Verify no side effects

### API Enhancement  
1. Review existing endpoint
2. Check API contracts
3. Implement changes
4. Update/add tests
5. Verify backward compatibility

## ⚠️ Validation Levels

| Level | Tests Run | Coverage Check | Quality Gates |
|-------|-----------|---------------|---------------|
| `basic` | Unit tests | No | Compilation only |
| `standard` | Unit + Integration | Yes | Coverage + Build |
| `comprehensive` | All tests | Yes | Full quality scan |

## 🔍 Troubleshooting

### Common Issues:
- **MCP Connection**: Check `.env` file configuration
- **Build Failures**: Verify Java 21 and Maven 3.6+
- **Test Failures**: Check TestContainers Docker access
- **Coverage Issues**: Review test completeness

### Error Codes:
- `401`: Jira/Confluence authentication issue
- `404`: Story or page not found  
- `Build FAILURE`: Compilation or test errors
- `Coverage FAILURE`: Below 80% threshold

## 📊 Success Metrics

✅ **Good Implementation**:
- All tests pass
- Coverage > 80%
- Build time < 2 minutes
- No security vulnerabilities

⚠️ **Needs Attention**:
- Some test failures
- Coverage 70-79%
- Build warnings present
- Minor security issues

❌ **Requires Fix**:
- Build failures
- Coverage < 70%
- Critical security issues
- Breaking changes

## 🔄 Workflow Checklist

- [ ] Fetch Jira story details
- [ ] Check Confluence for design docs  
- [ ] Analyze existing code structure
- [ ] Plan implementation approach
- [ ] Apply code changes
- [ ] Run appropriate tests
- [ ] Verify coverage requirements
- [ ] Check for regressions
- [ ] Update documentation
- [ ] Report results

## 📞 Support

**Configuration Issues**: Check `atlassian-mcp-server/.env`
**Build Problems**: Review `zurich-spring-poc/pom.xml`
**Test Failures**: Check `target/surefire-reports/`
**Coverage Reports**: View `target/site/jacoco/index.html`