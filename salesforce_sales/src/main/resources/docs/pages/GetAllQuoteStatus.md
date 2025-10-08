# Get All Quote Status

## Overview

This catalog request retrieves all available status values for quotes in your Salesforce organization. This is useful for understanding the possible states a quote can be in and for building workflows that depend on quote status values.

## Request Details

- **Area**: Quote Management
- **Type**: Query System
- **Authentication**: Supports both User Authentication and Service Account modes

## Input Parameters

This request requires no input parameters.

## Output Parameters

| Parameter Name | Parameter Type | Description |
|----------------|----------------|-------------|
| Statuses | List&lt;Text> | A list of all available quote status values in the Salesforce organization |

## Validation Rules

No validation is required as this request has no input parameters.

## Error Handling

### System Errors
- **Connection Error**: If unable to connect to Salesforce, an empty list is returned
- **Authentication Error**: If authentication fails, the request will throw an authentication exception
- **API Error**: If Salesforce API returns an error, an empty list is returned with error logged

### Business Rule Errors
None - this is a simple query request with no business rules.

### Validation Errors
None - no input parameters to validate.

### User Errors
None - no user input required.

## Usage Examples

### Example 1: Basic Usage
```
Input: (No parameters required)
Output: ["Draft", "Needs Review", "In Review", "Approved", "Rejected", "Presented"]
```

### Example 2: Empty Organization
```
Input: (No parameters required)  
Output: []
```

## Business Rules

1. **Status Retrieval**: Returns all picklist values configured for the Quote Status field
2. **Organization Specific**: Results are specific to your Salesforce organization's configuration
3. **Real-time Data**: Always returns current status values from Salesforce

## Limitations

- Returns only active status values
- Dependent on Salesforce organization configuration
- Requires appropriate permissions to access Quote object metadata

## Best Practices

1. **Cache Results**: Status values don't change frequently, consider caching for performance
2. **Error Handling**: Always check for empty results and handle gracefully
3. **Permissions**: Ensure the authenticated user has access to Quote object metadata

## Common Use Cases

### 1. Workflow Configuration
```
Scenario: Setting up quote approval workflows
Action: Retrieve available statuses to configure workflow transitions
Result: Dynamic workflow configuration based on available statuses
```

### 2. User Interface Population
```
Scenario: Building a quote management interface
Action: Use status list to populate dropdown menus
Result: Consistent status options across all interfaces
```

### 3. Data Validation
```
Scenario: Validating quote status updates
Action: Check if proposed status is in the valid list
Result: Prevent invalid status assignments
```

## Related Catalog Requests

- [Update Quote Status By Id](pages/UpdateQuoteStatusById.md) - Update a quote's status
- [Get Quote Details](pages/GetQuoteDetails.md) - Retrieve detailed quote information

## Technical Implementation

### Service Method
- **Class**: QuoteDaoImpl
- **Method**: getQuoteStatuses()
- **Package**: app.krista.extensions.crm.sales.salesforce_sales.impl

### Validation
No validation required for this request.

### Telemetry Metrics
- Request execution time
- Success/failure rates
- Error types and frequencies

## Troubleshooting

### Empty Results
**Cause**: No quote statuses configured or insufficient permissions
**Solution**: 
1. Verify Quote object is accessible
2. Check user permissions for metadata access
3. Confirm Quote Status field has picklist values configured

### Authentication Errors
**Cause**: Invalid or expired authentication credentials
**Solution**: 
1. Verify extension configuration
2. Check OAuth token validity
3. Ensure proper Salesforce permissions

## See Also

- [Extension Configuration](pages/ExtensionConfiguration.md)
- [Authentication](pages/Authentication.md)
- [Quote Management Overview](pages/QuoteManagement.md)
