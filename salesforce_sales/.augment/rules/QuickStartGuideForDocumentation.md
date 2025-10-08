# Quick Start Guide: Documenting Krista Extensions

## Overview

This quick start guide helps you apply the Extension Documentation Guidelines to any Krista Extension. Follow these steps to create comprehensive, user-friendly documentation.

## Step 1: Prepare Your Workspace

### 1.1 Create Documentation Structure

```
[extension-name]/src/main/resources/docs/
├── README.md
├── _sidebar.md
├── pages/
│   ├── ExtensionConfiguration.md
│   ├── Authentication.md
│   ├── Creating[ServiceName]App.md
│   ├── [CatalogRequest1].md
│   ├── [CatalogRequest2].md
│   └── ...
└── _media/
    └── [screenshots]
```

### 1.2 Gather Information

Before starting, collect:
- [ ] Extension version number
- [ ] Service API documentation URL
- [ ] OAuth scopes required
- [ ] List of all catalog requests
- [ ] Configuration parameters
- [ ] Authentication modes supported

## Step 2: Create Core Documentation Files

### 2.1 README.md

**Time Estimate**: 30-45 minutes

**Template Sections**:
1. Overview (what the extension does)
2. Key Features (5-7 bullet points)
3. Quick Start Guide
4. Catalog Requests (organized by category)
5. Documentation Structure
6. Support & Resources

**Action Items**:
- [ ] Write overview paragraph
- [ ] List key features
- [ ] Organize catalog requests into logical categories
- [ ] Add links to all documentation pages
- [ ] Include version and API information

### 2.2 _sidebar.md

**Time Estimate**: 15-20 minutes

**Structure**:
```markdown
- **Getting Started**
  - [Extension Configuration](pages/ExtensionConfiguration.md)
  - [Authentication](pages/Authentication.md)
  - [Creating [Service] App](pages/Creating[Service]App.md)

- **[Category 1]**
  - [Request 1](pages/Request1.md)
  - [Request 2](pages/Request2.md)

- **[Category 2]**
  - [Request 3](pages/Request3.md)
```

**Action Items**:
- [ ] Group catalog requests by functional category
- [ ] Add all page links
- [ ] Ensure hierarchical organization

## Step 3: Document Configuration

### 3.1 ExtensionConfiguration.md

**Time Estimate**: 1-2 hours

**Required Information**:
- Configuration parameters from extension setup
- OAuth credentials setup
- Redirect URI configuration
- Allow Retry flag explanation

**Action Items**:
- [ ] Create parameter table with all setup fields
- [ ] Write step-by-step setup instructions
- [ ] Document redirect URI configuration
- [ ] Explain retry mechanism
- [ ] Add security considerations
- [ ] Include troubleshooting section

**Code Review**:
- Review: `[Extension]Extension.java` for configuration parameters
- Review: `[Extension]Attributes.java` for attribute definitions

## Step 4: Document Authentication

### 4.1 Authentication.md

**Time Estimate**: 2-3 hours

**Critical Sections** (DO NOT SKIP):

1. **OAuth 2.0 Scopes**:
   - [ ] List all required scopes with full URLs
   - [ ] Explain what each scope allows
   - [ ] Document why full scope is needed

2. **OAuth 2.0 Grant Types**:
   - [ ] Document Authorization Code Grant
   - [ ] Document Refresh Token Grant
   - [ ] Include request/response examples
   - [ ] Add comparison table

3. **Token Expiration**:
   - [ ] Access token lifetime (usually 1 hour)
   - [ ] Refresh token lifetime (verified vs unverified apps)
   - [ ] Revocation scenarios (6+ scenarios)
   - [ ] Expiration handling flow

4. **Authentication Modes**:
   - [ ] Service Account authentication
   - [ ] User Account authentication
   - [ ] When to use each mode

**Code Review**:
- Review: OAuth flow implementation
- Review: Token refresh logic
- Review: Scope definitions

## Step 5: Document Service Setup

### 5.1 Creating[ServiceName]App.md

**Time Estimate**: 1-2 hours

**Action Items**:
- [ ] Step-by-step project/app creation
- [ ] API enablement instructions
- [ ] OAuth consent screen configuration
- [ ] Credentials creation
- [ ] Redirect URI setup
- [ ] Add screenshots for each major step

**Resources Needed**:
- Access to service provider console (e.g., Google Cloud Console)
- Screenshots of each step

## Step 6: Document Catalog Requests

### 6.1 Identify All Catalog Requests

**Action Items**:
- [ ] List all @CatalogRequest methods in Area classes
- [ ] Group by functional category
- [ ] Prioritize by importance/usage

**Code Review**:
- Review: `src/main/java/.../catalog/*Area.java` files
- Note: Request names, descriptions, parameters

### 6.2 Create Individual Catalog Request Pages

**Time Estimate**: 1-2 hours per catalog request

**For Each Catalog Request**:

1. **Code Review** (30 minutes):
   - [ ] Review Helper class: `[RequestName]Helper.java`
   - [ ] Review Area class: `@CatalogRequest` annotation
   - [ ] Review Validator classes
   - [ ] Review Service class methods
   - [ ] Note validation rules
   - [ ] Note error messages (exact text)
   - [ ] Note telemetry metrics

2. **Write Documentation** (30-60 minutes):
   - [ ] Overview
   - [ ] Request Details (NO catalog request ID)
   - [ ] Input Parameters table
   - [ ] Output Parameters table
   - [ ] Validation Rules table
   - [ ] Error Handling (all 4 types)
   - [ ] Usage Examples (2-3 examples)
   - [ ] Business Rules
   - [ ] Limitations
   - [ ] Best Practices
   - [ ] Common Use Cases
   - [ ] Related Catalog Requests
   - [ ] Technical Implementation
   - [ ] Troubleshooting
   - [ ] See Also

3. **Verify Accuracy** (15 minutes):
   - [ ] Error messages match code
   - [ ] Validation rules match code
   - [ ] Parameters match code
   - [ ] Telemetry metrics match code

## Step 7: Quality Assurance

### 7.1 Content Review

**Checklist**:
- [ ] All required sections present
- [ ] No catalog request IDs exposed
- [ ] OAuth scopes documented with full URLs
- [ ] Grant types documented with examples
- [ ] Token expiration comprehensive
- [ ] Error messages match code
- [ ] Validation rules match code
- [ ] All links work
- [ ] All tables formatted correctly
- [ ] Spelling and grammar correct

### 7.2 Code Accuracy Verification

**For Each Catalog Request**:
- [ ] Helper class reviewed
- [ ] Validation logic verified
- [ ] Error messages verified (exact text)
- [ ] Telemetry metrics verified
- [ ] Business rules verified
- [ ] Parameter types verified

### 7.3 User Testing

**Test Documentation**:
- [ ] Can a new user configure the extension?
- [ ] Can a user authenticate successfully?
- [ ] Can a user find and use catalog requests?
- [ ] Are error messages helpful?
- [ ] Are examples clear and realistic?

## Step 8: Finalization

### 8.1 Final Touches

- [ ] Add version number to README
- [ ] Update release notes
- [ ] Add screenshots where helpful
- [ ] Review all cross-references
- [ ] Ensure consistent formatting

### 8.2 Remove Old Files

- [ ] Remove old combined documentation files
- [ ] Remove outdated screenshots
- [ ] Clean up unused files

## Time Estimates

### Total Time by Extension Size

**Small Extension** (5-10 catalog requests):
- Core files: 2-3 hours
- Configuration: 1-2 hours
- Authentication: 2-3 hours
- Service setup: 1-2 hours
- Catalog requests: 5-20 hours (1-2 hours each)
- QA: 2-3 hours
- **Total**: 13-33 hours

**Medium Extension** (11-20 catalog requests):
- Core files: 2-3 hours
- Configuration: 1-2 hours
- Authentication: 2-3 hours
- Service setup: 1-2 hours
- Catalog requests: 11-40 hours (1-2 hours each)
- QA: 3-4 hours
- **Total**: 20-54 hours

**Large Extension** (21+ catalog requests):
- Core files: 3-4 hours
- Configuration: 2-3 hours
- Authentication: 3-4 hours
- Service setup: 2-3 hours
- Catalog requests: 21-60+ hours (1-2 hours each)
- QA: 4-6 hours
- **Total**: 35-80+ hours

## Tips for Efficiency

### Batch Similar Work

1. **Code Review Phase**: Review all helper classes first
2. **Writing Phase**: Write all catalog request pages
3. **QA Phase**: Verify all pages at once

### Use Templates

- Copy the catalog request template for each new page
- Reuse similar sections across requests
- Maintain consistent formatting

### Prioritize

1. Start with most-used catalog requests
2. Document complex requests thoroughly
3. Simple requests can be shorter

### Automate Where Possible

- Extract parameter lists from code
- Generate telemetry metric lists
- Use code comments as documentation source

## Common Pitfalls to Avoid

### ❌ Don't Do This

1. **Don't expose catalog request IDs** - Internal implementation detail
2. **Don't skip code review** - Documentation must match code
3. **Don't use generic error messages** - Use exact text from code
4. **Don't forget OAuth details** - Scopes and grant types are critical
5. **Don't skip token expiration** - Users need to understand token lifecycle
6. **Don't combine catalog requests** - Each needs its own page
7. **Don't forget cross-references** - Link related requests
8. **Don't skip examples** - Users learn from examples

### ✅ Do This

1. **Review code first** - Understand implementation before writing
2. **Use exact error messages** - Copy from code
3. **Document all scenarios** - Cover edge cases
4. **Add realistic examples** - Use real-world values
5. **Include troubleshooting** - Anticipate user problems
6. **Cross-reference liberally** - Help users navigate
7. **Update regularly** - Keep documentation current
8. **Test with users** - Validate documentation usability

## Resources

### Reference Documents

- **Full Guidelines**: `ExtensionDocumentationGuidelines.md`
- **Catalog Request Template**: See Section 16 in Guidelines
- **Google Calendar Example**: Complete reference implementation

### Code Locations

- **Area Classes**: `src/main/java/.../catalog/*Area.java`
- **Helper Classes**: `src/main/java/.../catalog/helper/*Helper.java`
- **Validators**: `src/main/java/.../catalog/validators/*Validator.java`
- **Service Classes**: `src/main/java/.../service/*Service.java`
- **Extension Class**: `src/main/java/.../[Extension]Extension.java`
- **Attributes**: `src/main/java/.../[Extension]Attributes.java`

## Success Criteria

Your documentation is complete when:

✅ All catalog requests have individual pages
✅ All pages follow the required structure
✅ All code has been reviewed and verified
✅ OAuth scopes and grant types are documented
✅ Token expiration scenarios are comprehensive
✅ All error messages match code
✅ All validation rules match code
✅ All links work correctly
✅ A new user can configure and use the extension
✅ Common issues have troubleshooting guidance

## Next Steps

After completing documentation:

1. **Publish**: Make documentation available to users
2. **Gather Feedback**: Collect user questions and confusion points
3. **Iterate**: Update based on feedback
4. **Maintain**: Keep documentation current with code changes
5. **Share**: Use as reference for other extensions

---

**Remember**: Good documentation is as important as good code. Take the time to do it right!

