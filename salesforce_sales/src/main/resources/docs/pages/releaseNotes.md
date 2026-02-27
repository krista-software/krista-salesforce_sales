# Release Notes - Krista Salesforce Sale Module Extension

## Version 2.0.16

- **Developer Name** : Vrushali Gaikwad
- **Krista Service APIs Java** : 1.0.120
- **Global Catalog Version** : GC-2026.02.04
- **Release Date** :Feb 2026

## What's New

### 🆕 PO to Order Creation Support

#### New Catalog Requests (5)
1. **Find Or Create Account** - Searches for an account by name; creates new if not found
2. **Upload PO File** - Upload Purchase Order files (PDF, Word, Excel) to Salesforce
3. **Link File to Order** - Link uploaded files to Orders via ContentDocumentLink
4. **Find Or Create Product** - Searches for product by code; creates new if not found
5. **Find Or Create Pricebook Entry** - Searches for pricebook entry; creates new if not found

#### New Entities (3)
* **Order** - Full Order entity with billing/shipping addresses and custom field support
* **Order Item** - Order line items with quantity, pricing, and service dates
* **Product** - Product2 entity with product codes, families, and activation status

#### Key Features
* **Enhanced Salesforce Extension**: Full support for Purchase Order to Order creation workflow
* **Account Management**: Create and manage Salesforce Accounts with dynamic custom fields
* **Order Processing**: Create and update Orders with comprehensive field support
* **Order Items**: Manage Order Items (Order Products) with quantity, pricing, and custom fields
* **Product Catalog**: Full Product2 entity support with product codes, families, and activation status
* **Pricebook Entries**: Automated PricebookEntry creation for product pricing
* **File Attachments**: Upload and link files to Orders via ContentVersion and ContentDocumentLink
* **Dynamic Custom Fields**: Support for all Salesforce custom fields (`__c` suffix) without hardcoding

#### New Catalog Areas
* **AccountsManagementArea** - Account management with Find Or Create functionality
* **OrderManagementArea** - Order, Product, and Pricebook Entry operations
* **ContentArea** - File upload and custom field retrieval operations

### 🎯 Enhanced User Experience
* **Improved Error Messages**: All error messages across the extension have been rewritten to be more user-friendly, actionable, and provide clear resolution steps
* **Consistent Error Handling**: Standardized error message format across all catalog areas and DAO implementations
* **Better Validation Messages**: Enhanced validation error messages with specific guidance on what went wrong and how to fix it

### 🧪 Comprehensive Test Suite
* **99 New Test Cases**: Added comprehensive real-world test cases for all public static utility methods
* **100% Test Coverage**: Complete coverage of Validators, DatePlus, and SFHelper utility classes
* **No Mockito Dependencies**: All tests use real concrete implementations for better reliability
* **Production-Ready Quality**: Tests serve as excellent documentation and usage examples

### ⚙️ Technical Improvements
* **Java 21 Support**: Upgraded to Java 21 with modern language features
* **Gradle 8.10**: Updated Gradle wrapper to version 8.10 for better Java 21 compatibility
* **Build System Modernization**: Enhanced build configuration for improved performance

### 📚 Documentation Updates
* **Updated Troubleshooting Guide**: Comprehensive error scenarios with solutions
* **Enhanced Catalog Request Documentation**: Updated error messages in GetOpportunities, GetLeads, DeleteAnAsset, UpdateAnAsset, SendAMail, UpdateStatusOfOpportunities, and ProcessQueryRequest pages
* **Authentication Documentation**: Improved error handling documentation
* **Test Results Documentation**: Added comprehensive test execution summary and guidelines

### 📖 New Comprehensive Documentation
* **Architecture Documentation (Architecture.md)**: Added comprehensive 1,471-line architecture document with:
  - Complete 5-layer architecture breakdown with detailed descriptions
  - 24 Mermaid diagrams covering component architecture, data flows, authentication, error handling, performance, security, and deployment
  - Performance characteristics and Salesforce API limitations
  - Error handling patterns and common error scenarios
  - Security architecture and best practices
  - Deployment topology and high-availability configurations
  - Production-ready documentation for developers, architects, and operations teams
* **Complete Documentation (Salesforce_Sales_Complete_Documentation.docx)**: Added LLM-optimized comprehensive documentation with:
  - 11 major sections with 95 hierarchical headings
  - 61 detailed reference tables for quick lookup
  - 528 paragraphs of comprehensive content
  - Complete documentation of all 28+ catalog requests with parameters, validation rules, and error scenarios
  - All 11 entity definitions with field specifications
  - Troubleshooting guide with actionable solutions
  - Configuration and setup instructions
  - Optimized for AI/LLM consumption to answer user queries

## Improvements

### Error Message Enhancements

#### Catalog Area Classes (4 files)
* **OpportunityManagementArea**: Improved validation messages for opportunity operations
* **LeadsManagementArea**: Enhanced error messages for lead management
* **AssetsManagementArea**: Better error handling for asset operations
* **ForecastingArea**: Clearer validation messages for forecast queries

#### DAO Implementations (8 files)
* **AccountDaoImpl**: User-friendly error messages for account operations
* **AssetDaoImpl**: Improved error handling for asset CRUD operations
* **EventDaoImpl**: Enhanced validation messages for event management
* **LeadDaoImpl**: Better error messages for lead operations
* **OpportunityDaoImpl**: Clearer error handling for opportunity management
* **QuoteDaoImpl**: Improved validation messages for quote operations
* **TaskDaoImpl**: Enhanced error messages for task management
* **PriceBookEntryDaoImpl**: Better error handling for pricebook operations

#### Store Classes (9 files)
* **AccountStore, AssetStore, EventStore, LeadStore, OpportunityStore, PricebookEntryStore, QuoteStore, TaskStore, UserStore**: Consistent deletion error messages with actionable guidance

#### Core Utilities (3 files)
* **Constants.java**: Centralized all error message constants for consistency
* **Validators.java**: Enhanced email validation error messages
* **SalesforceManager.java**: Improved API error messages with clear resolution steps

### Test Suite Details

#### ValidatorsTest.java (45+ tests)
* Email validation (single and multiple recipients)
* Timezone validation (UTC, America/New_York, Asia/Tokyo, Europe/London)
* Data type conversion (String, Double)
* Null and empty value handling
* Special characters and Unicode support

#### DatePlusTest.java (30+ tests)
* Date parsing and formatting
* ISO 8601 date conversion
* Quarter date calculations (Q1-Q4)
* Leap year handling
* Timezone-aware operations
* SimpleDateFormat leniency behavior

#### SFHelperTest.java (30+ tests)
* OAuth vs Password authentication flow
* Admin and non-admin context handling
* Account data processing (billing/shipping addresses)
* Lead conversion tracking
* Task management with due dates
* Missing optional field handling

### Documentation Improvements

#### Updated Pages (9 files)
* **Troubleshooting.md**: Added comprehensive error scenarios and solutions
* **GetOpportunities.md**: Updated error messages and validation rules
* **GetLeads.md**: Enhanced error handling documentation
* **DeleteAnAsset.md**: Improved error messages
* **UpdateAnAsset.md**: Better validation error documentation
* **SendAMail.md**: Enhanced email validation messages
* **UpdateStatusOfOpportunities.md**: Improved error handling
* **ProcessQueryRequest.md**: Better query validation messages
* **Authentication.md**: Enhanced authentication error documentation

#### New Documentation Files (2 files)
* **Architecture.md**: Comprehensive architecture documentation with 24 Mermaid diagrams
* **Salesforce_Sales_Complete_Documentation.docx**: LLM-optimized complete reference documentation

## Technical Details

### Build Configuration
* **Java Version**: 21 (upgraded from 13)
* **Gradle Version**: 8.10 (upgraded from 7.4)
* **Source Compatibility**: Java 21
* **Target Compatibility**: Java 21

### Test Execution
* **Total Tests**: 586
* **Pass Rate**: 100%
* **Execution Time**: ~10-15 seconds
* **Framework**: JUnit 4
* **Mocking**: Minimal (mostly real implementations)

## Migration Notes

### For Developers
* Tests now require Java 21 and Gradle 8.10
* All error messages follow a consistent three-part format:
  1. Clearly identify the problem
  2. Explain what is required
  3. Provide actionable guidance
* New comprehensive documentation available:
  - Architecture.md for technical architecture details
  - Salesforce_Sales_Complete_Documentation.docx for complete reference

### For Users
* Error messages are now more descriptive and helpful
* Validation errors include specific resolution steps
* Authentication errors provide clear next actions
* Comprehensive documentation available for troubleshooting and configuration

## Files Modified

### Source Code (35+ files)
* **7 Catalog Area classes** (4 existing + 3 new: AccountsManagementArea, OrderManagementArea, ContentArea)
* **11 DAO Implementation classes** (8 existing + 3 new: OrderDaoImpl, OrderItemDaoImpl, ProductDaoImpl, ContentVersionDaoImpl, ContentDocumentLinkDaoImpl)
* **12 Store classes** (9 existing + 3 new: OrderStore, OrderItemStore, ProductStore)
* **3 Utility classes**
* **1 Manager class**
* **6 New Entity classes**: Order, OrderItem, Product, SFOrder, SFOrderItem, SFProduct
* **3 New Catalog Type classes**: OrderCatalogType, OrderItemCatalogType, ProductCatalogType

### Test Files (3 files)
* ValidatorsTest.java (45+ tests)
* DatePlusTest.java (30+ tests)
* SFHelperTest.java (30+ tests)

### Documentation (13+ files)
* Troubleshooting guide
* 8 Catalog request pages (updated with new error messages)
* **NEW**: Architecture.md (comprehensive architecture documentation)
* **NEW**: Salesforce_Sales_Complete_Documentation.docx (LLM-optimized reference)
* **UPDATED**: supportedRequests.md - Added 5 new catalog requests and 3 new entities
* **UPDATED**: _sidebar.md - Added navigation for new features
* **NEW Entity Documentation**: Order, OrderItem, Product entity pages

### Build Configuration (2 files)
* build.gradle
* gradle-wrapper.properties (Gradle 7.4 → 8.10)

## Known Issues

None

---

## Version 2.0.14

- **Developer Name** : Vaibhav Choudhary
- **Krista Service APIs Java** : 1.0.117
- **Global Catalog Version** : GC-2025.10.2

### Resolved Bugs

* [KE-2428](https://antbrains.atlassian.net/browse/KE-2428) Salesforce Customer Service Extension (then all the rest) Should Support Client Credentials Flow.

### Known Issues

None




