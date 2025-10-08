# Extension Documentation Checklist

Use this checklist to ensure complete and accurate documentation for any Krista Extension.

## Pre-Documentation Phase

### Information Gathering
- [ ] Extension version number identified
- [ ] Service API documentation URL collected
- [ ] All OAuth scopes identified
- [ ] All catalog requests listed
- [ ] Configuration parameters documented
- [ ] Authentication modes identified
- [ ] Access to service provider console (for screenshots)

### Code Review Preparation
- [ ] Located all Area classes (`*Area.java`)
- [ ] Located all Helper classes (`*Helper.java`)
- [ ] Located all Validator classes (`*Validator.java`)
- [ ] Located Service classes (`*Service.java`)
- [ ] Located Extension class (`*Extension.java`)
- [ ] Located Attributes class (`*Attributes.java`)

## Core Documentation Files

### README.md
- [ ] File created at `src/main/resources/docs/README.md`
- [ ] Overview section written
- [ ] Key Features listed (5-7 items)
- [ ] Quick Start Guide included
- [ ] Catalog requests organized by category
- [ ] Documentation structure explained
- [ ] Support & Resources section added
- [ ] Version number included
- [ ] API scope URL included
- [ ] Links to all pages verified

### _sidebar.md
- [ ] File created at `src/main/resources/docs/_sidebar.md`
- [ ] Getting Started section created
- [ ] Catalog requests grouped by category
- [ ] All pages linked
- [ ] Hierarchical structure maintained
- [ ] Links tested and working

### pages/ Folder
- [ ] Folder created at `src/main/resources/docs/pages/`
- [ ] All required files present

## Configuration Documentation

### ExtensionConfiguration.md
- [ ] Overview section written
- [ ] Configuration Parameters table created
  - [ ] All parameters listed
  - [ ] Types specified
  - [ ] Required/Optional indicated
  - [ ] Descriptions clear
  - [ ] Examples provided
- [ ] Step-by-Step Setup instructions written
- [ ] Authentication Type Selection explained
- [ ] Redirect URI Configuration documented
- [ ] Allow Retry Flag explained
- [ ] Security Considerations added
- [ ] Troubleshooting section included
- [ ] Code reviewed: Extension class
- [ ] Code reviewed: Attributes class

## Authentication Documentation

### Authentication.md
- [ ] Overview section written
- [ ] Authentication Modes section complete
  - [ ] Service Account authentication explained
  - [ ] User Account authentication explained
  - [ ] When to use each mode documented
- [ ] OAuth 2.0 Scopes section complete
  - [ ] All required scopes listed with full URLs
  - [ ] Permission level explained
  - [ ] What each scope allows documented
  - [ ] Alternative scopes explained (why not used)
- [ ] OAuth 2.0 Grant Types section complete
  - [ ] Authorization Code Grant documented
    - [ ] Grant type specified: `authorization_code`
    - [ ] Flow explained
    - [ ] Request example included
    - [ ] Response example included
  - [ ] Refresh Token Grant documented
    - [ ] Grant type specified: `refresh_token`
    - [ ] Flow explained
    - [ ] Request example included
    - [ ] Response example included
  - [ ] Comparison table added
- [ ] Authentication Flow section complete
  - [ ] Step-by-step flow documented
  - [ ] Authorization code exchange detailed
  - [ ] Token storage explained
- [ ] Token Management section complete
  - [ ] Access token expiration documented
    - [ ] Lifetime specified (e.g., 1 hour)
    - [ ] Refresh mechanism explained
    - [ ] Error handling described
  - [ ] Refresh token expiration documented
    - [ ] Unverified app policy (6 months)
    - [ ] Verified app policy (indefinite)
    - [ ] Revocation scenarios listed (6+)
  - [ ] Token refresh flow explained
  - [ ] Re-authentication scenarios listed
- [ ] Token Expiration Handling flow documented
  - [ ] Access token expired scenario
  - [ ] Refresh token expired scenario
  - [ ] Both tokens invalid scenario
- [ ] Security Best Practices included
- [ ] Troubleshooting section added
- [ ] Domain-Wide Delegation documented (if applicable)

## Service Setup Documentation

### Creating[ServiceName]App.md
- [ ] Overview section written
- [ ] Prerequisites listed
- [ ] Step-by-step instructions complete
  - [ ] Project/app creation
  - [ ] API enablement
  - [ ] OAuth consent screen configuration
  - [ ] Credentials creation
  - [ ] Redirect URI setup
- [ ] Screenshots added for major steps
- [ ] Verification steps included
- [ ] Next Steps section added

## Catalog Request Documentation

### For Each Catalog Request

#### File Creation
- [ ] File created: `pages/[RequestName].md`
- [ ] File name uses PascalCase
- [ ] File name matches catalog request name

#### Required Sections

**1. Overview**
- [ ] Clear, concise description (1-2 sentences)

**2. Request Details**
- [ ] Area specified
- [ ] Type specified (CHANGE_SYSTEM, QUERY_SYSTEM, etc.)
- [ ] Retry Support indicated (✅ or ❌ with explanation)
- [ ] ❌ NO Catalog Request ID included

**3. Input Parameters**
- [ ] Table created with all parameters
- [ ] Columns: Parameter Name, Type, Required, Description, Example
- [ ] Detailed parameter descriptions below table
- [ ] Validation rules for each parameter

**4. Output Parameters**
- [ ] Table created with output parameters
- [ ] Entity structure detailed (if applicable)
- [ ] Example output values provided

**5. Validation Rules**
- [ ] Table created: Validation, Error Message, Resolution
- [ ] All validation scenarios covered
- [ ] Error messages match code exactly

**6. Error Handling**
- [ ] Input Errors (INPUT_ERROR) documented
  - [ ] Cause explained
  - [ ] Error message (exact from code)
  - [ ] Common scenarios listed
  - [ ] Resolution steps provided
- [ ] Logic Errors (LOGIC_ERROR) documented
  - [ ] Cause explained
  - [ ] Error message (exact from code)
  - [ ] Common scenarios listed
  - [ ] Resolution steps provided
- [ ] System Errors (SYSTEM_ERROR) documented
  - [ ] Cause explained
  - [ ] Error message (exact from code)
  - [ ] Common scenarios listed
  - [ ] Resolution steps provided
- [ ] Authorization Errors documented
  - [ ] Cause explained
  - [ ] Resolution steps provided

**7. Usage Examples**
- [ ] At least 2-3 examples provided
- [ ] Input values shown
- [ ] Expected output shown
- [ ] Result described
- [ ] Retry scenario example (if retry supported)

**8. Business Rules**
- [ ] Numbered list of rules
- [ ] Constraints documented
- [ ] Behavioral expectations clear

**9. Limitations**
- [ ] Technical limitations listed
- [ ] API limitations documented
- [ ] Rate limits mentioned
- [ ] Scope restrictions noted

**10. Best Practices**
- [ ] Numbered list with headings
- [ ] Practical recommendations
- [ ] Do's and don'ts

**11. Common Use Cases**
- [ ] Real-world scenarios provided
- [ ] Step-by-step workflows
- [ ] Expected results described

**12. Related Catalog Requests**
- [ ] Links to related operations
- [ ] Cross-references to complementary requests

**13. Technical Implementation**
- [ ] Helper class name and package
- [ ] Validation flow described
- [ ] Service delegation explained
- [ ] Telemetry metrics listed

**14. Troubleshooting**
- [ ] Common issues documented
- [ ] Error scenarios covered
- [ ] Resolution steps provided

**15. See Also**
- [ ] Links to related documentation
- [ ] Configuration references
- [ ] Authentication requirements

#### Code Review Verification
- [ ] Helper class reviewed: `[RequestName]Helper.java`
  - [ ] Validation rules verified
  - [ ] Error messages verified (exact text)
  - [ ] Telemetry metrics verified
- [ ] Area class reviewed: `@CatalogRequest` annotation
  - [ ] Parameter names verified
  - [ ] Parameter types verified
  - [ ] Request type verified
- [ ] Service class reviewed
  - [ ] Business logic verified
  - [ ] Error handling verified
- [ ] Validator classes reviewed
  - [ ] Validation logic verified
  - [ ] Error messages verified

## Quality Assurance

### Content Review
- [ ] All required sections present in all files
- [ ] No catalog request IDs exposed
- [ ] OAuth scopes documented with full URLs
- [ ] Grant types documented with examples
- [ ] Token expiration comprehensive
- [ ] Error messages match code exactly
- [ ] Validation rules match code exactly
- [ ] All links work correctly
- [ ] All tables formatted correctly
- [ ] Spelling checked
- [ ] Grammar checked
- [ ] Consistent terminology used
- [ ] Consistent formatting applied

### Code Accuracy
- [ ] All helper classes reviewed
- [ ] All validation logic verified
- [ ] All error messages verified
- [ ] All telemetry metrics verified
- [ ] All business rules verified
- [ ] All parameter types verified
- [ ] All return types verified

### User Testing
- [ ] New user can configure extension
- [ ] New user can authenticate successfully
- [ ] User can find catalog requests
- [ ] Error messages are helpful
- [ ] Examples are clear and realistic
- [ ] Troubleshooting is effective

### Cross-References
- [ ] All internal links work
- [ ] All external links work
- [ ] Related requests linked
- [ ] Configuration linked where needed
- [ ] Authentication linked where needed

## Finalization

### Final Touches
- [ ] Version number in README
- [ ] Release notes updated
- [ ] Screenshots added where helpful
- [ ] All cross-references reviewed
- [ ] Consistent formatting throughout

### Cleanup
- [ ] Old combined documentation files removed
- [ ] Outdated screenshots removed
- [ ] Unused files cleaned up
- [ ] File structure organized

### Publication
- [ ] Documentation reviewed by peer
- [ ] Documentation approved
- [ ] Documentation published
- [ ] Users notified of new documentation

## Post-Publication

### Maintenance
- [ ] Feedback collection process established
- [ ] Support questions tracked
- [ ] Common issues documented
- [ ] Regular review scheduled

### Continuous Improvement
- [ ] User feedback collected
- [ ] Troubleshooting updated based on support tickets
- [ ] Examples added based on user needs
- [ ] Clarity improved based on user confusion

## Summary Statistics

Track your progress:

- Total catalog requests: ___
- Catalog requests documented: ___
- Pages created: ___
- Code reviews completed: ___
- Screenshots added: ___
- Links verified: ___
- Completion percentage: ___%

## Sign-Off

Documentation completed by: ________________
Date: ________________
Reviewed by: ________________
Date: ________________
Approved by: ________________
Date: ________________

---

**Note**: This checklist should be used for every extension documentation project to ensure consistency and completeness.

