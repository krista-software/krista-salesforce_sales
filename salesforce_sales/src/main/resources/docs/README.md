[comment]: <> (Home Page)

# Salesforce Sales Extension

Welcome to the Krista Salesforce Sales Extension documentation. This extension enables seamless integration between Krista and Salesforce Sales Cloud, providing powerful automation capabilities for sales management, opportunity tracking, and revenue optimization.

## Overview

The Salesforce Sales Extension connects Krista to Salesforce Sales Cloud, enabling automated sales processes, opportunity management, and real-time revenue tracking. Built with OAuth 2.0 security and comprehensive error handling, this extension provides reliable access to Salesforce Sales functionality.

## Key Features

- **Opportunity Management**: Track opportunities, update stages, and monitor sales pipeline progression
- **Lead Management**: Capture, qualify, and convert leads with automated workflows
- **Revenue Analytics**: Access real-time revenue data, forecasts, and quota tracking
- **Account & Asset Management**: Manage customer accounts and associated assets
- **Real-time Alerts**: Automated notifications for opportunity, lead, account, and task changes
- **Quote Management**: Handle pricing, quotes, and product configurations
- **OAuth 2.0 Authentication**: Secure, token-based authentication with automatic refresh
- **Comprehensive Validation**: Input validation with user-friendly error handling
- **Retry Support**: Built-in retry mechanisms for reliable operations

## Quick Start Guide

### 1. Prerequisites
- Salesforce org with Sales Cloud access
- Krista platform access
- System Administrator access to Salesforce

### 2. Setup Steps
1. **[Create Salesforce Connected App](pages/CreatingSalesforceApp.md)** - Configure OAuth application
2. **[Configure Extension](pages/ExtensionConfiguration.md)** - Set up connection parameters
3. **[Authenticate](pages/Authentication.md)** - Connect to your Salesforce instance
4. **Test Connection** - Verify setup with a simple catalog request

### 3. First Request
Try the "Get Opportunities" request to verify your setup is working correctly.

## Documentation Structure

### 🚀 Getting Started
- [Extension Configuration](pages/ExtensionConfiguration.md) - Complete setup and configuration guide
- [Authentication](pages/Authentication.md) - OAuth 2.0 authentication and token management
- [Creating Salesforce App](pages/CreatingSalesforceApp.md) - Connected App creation and setup

### 🎯 Forecasting
- [Get Sales Forecast For A Specific Quarter](pages/GetSalesForecastForASpecificQuarter.md) - Quarterly sales forecasting and predictions

### 🏢 Account Management
- [Account Change Alert](pages/AccountChangeAlert.md) - Monitor and respond to account changes

### 🎯 Lead Management
- [Get Leads](pages/GetLeads.md) - Retrieve and filter lead data
- [Lead Change Alert](pages/LeadChangeAlert.md) - Track lead status and qualification changes

### 💼 Opportunity Management
- [Get Opportunities](pages/GetOpportunities.md) - Access opportunity data and pipeline information
- [Update Status Of Opportunities](pages/UpdateStatusOfOpportunities.md) - Modify opportunity stages and status
- [Opportunity Change Alert](pages/OpportunityChangeAlert.md) - Monitor opportunity progression and changes

### ✅ Task Management
- [Get Task Statuses](pages/GetTaskStatuses.md) - Retrieve available task status options
- [Task Change Alert](pages/TaskChangeAlert.md) - Track task updates and completions

### 💰 Quote Management
- [Get All Quote Status](pages/GetAllQuoteStatus.md) - Access quote status information and workflows

### 🏗️ Asset Management
- [Delete An Asset](pages/DeleteAnAsset.md) - Remove assets from customer accounts
- [Update An Asset](pages/UpdateAnAsset.md) - Modify asset information and ownership

### 📧 Actions & Communication
- [Send A Mail](pages/SendAMail.md) - Send emails through Salesforce integration
- [Send Verification Mail](pages/SendVerificationMail.md) - Send verification and confirmation emails

### 📊 Reporting & Analytics
- [Get Opportunities Summary](pages/GetOpportunitiesSummary.md) - Aggregated opportunity metrics and insights
- [Process Query Request](pages/ProcessQueryRequest.md) - Execute custom SOQL queries
- [Process Query Request Async](pages/ProcessQueryRequestAsync.md) - Handle large data queries asynchronously
- [Get Results for Process Query Request Async](pages/GetResultsForProcessQueryRequestAsync.md) - Retrieve async query results
- [YTD Sales Revenue Closed](pages/YTDSalesRevenueClosed.md) - Year-to-date revenue analysis
- [Sales Pipeline Summary](pages/SalesPipelineSummary.md) - Pipeline stage analysis and metrics
- [Sales Quota For The Quarter](pages/SalesQuotaForTheQuarter.md) - Quarterly quota tracking and performance
- [Value Of Opportunities Closed For Current Quarter](pages/ValueOfOpportunitiesClosedForCurrentQuarter.md) - Quarterly closed deal analysis
- [Total Potential Opportunity Value For Quarter](pages/TotalPotentialOpportunityValueForQuarter.md) - Pipeline value projections

### 📈 Weekly Summary
- [Get Revenue Closed In Current Quarter](pages/GetRevenueClosedInCurrentQuarter.md) - Current quarter closed revenue tracking
- [Get Revenue Expected To Close In Current Quarter](pages/GetRevenueExpectedToCloseInCurrentQuarter.md) - Revenue forecasting and projections

### 🔧 Content & Custom Fields
- [Retrieve Custom Fields For Given Object](pages/RetrieveCustomFieldsForGivenObject.md) - Access custom field definitions and metadata

### 🏢 Entity Operations
- [Account Entity](pages/entities/Account.md) - Organization and company management
- [Asset Entity](pages/entities/Asset.md) - Customer asset tracking and management
- [Event Entity](pages/entities/Event.md) - Calendar events and scheduling
- [Latest Posts Entity](pages/entities/LatestPosts.md) - Social and collaboration features
- [Lead Entity](pages/entities/Lead.md) - Lead qualification and conversion
- [Opportunity Entity](pages/entities/Opportunity.md) - Sales opportunity tracking
- [PricebookEntry Entity](pages/entities/PricebookEntry.md) - Product pricing and catalog management
- [Quote Entity](pages/entities/Quote.md) - Quote generation and management
- [Sales Pipeline Summary Entity](pages/entities/SalesPipelineSummary.md) - Pipeline analytics and reporting
- [Task Entity](pages/entities/Task.md) - Task management and tracking
- [User Entity](pages/entities/User.md) - User information and permissions

### 📖 Reference & Support
- [Supported Requests](pages/supportedRequests.md) - Complete catalog and entity request reference
- [Troubleshooting](pages/Troubleshooting.md) - Common issues, solutions, and best practices
- [Release Notes](pages/releaseNotes.md) - Version history, updates, and changes

## Support & Resources

- **Extension Version**: 2.0.14
- **Salesforce API**: REST API v58.0+
- **Authentication**: OAuth 2.0 Authorization Code Grant
- **Java Version**: Java 21
- **Supported Salesforce Objects**: Account, Asset, Event, Lead, Opportunity, Quote, Task, User

## Need Help?

1. **Configuration Issues**: Check the [Extension Configuration](pages/ExtensionConfiguration.md) guide
2. **Authentication Problems**: Review the [Authentication](pages/Authentication.md) documentation
3. **Request Errors**: Consult the [Troubleshooting](pages/Troubleshooting.md) guide
4. **API Questions**: Reference the individual catalog request pages

---

*This documentation follows Krista Extension Documentation Guidelines to ensure consistency, accuracy, and usability across all extension documentation.*
