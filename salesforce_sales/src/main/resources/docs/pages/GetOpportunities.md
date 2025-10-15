# Get Opportunities

## Overview

This catalog request retrieves a list of opportunities from Salesforce based on either a custom SOQL query or a time-based filter for recent opportunities. It provides flexible access to opportunity data for reporting, analysis, and workflow automation.

## Request Details

- **Area**: Opportunity Management
- **Type**: Query System
- **Authentication**: Supports both User Authentication and Service Account modes

## Input Parameters

| Parameter Name | Parameter Type | Required | Description | Example |
|----------------|----------------|----------|-------------|---------|
| SOQL Query | Text | No | Custom SOQL query to filter opportunities | `SELECT Id, Name, Amount FROM Opportunity WHERE StageName = 'Closed Won'` |
| Last Days | Number | No | Number of days to look back for opportunities | `30` |

## Output Parameters

| Parameter Name | Parameter Type | Description |
|----------------|----------------|-------------|
| Opportunities | List&lt;Entity(Opportunity)> | List of opportunity records matching the criteria |

## Validation Rules

| Rule | Description | Error Message |
|------|-------------|---------------|
| Parameter Validation | At least one parameter (SOQL Query or Last Days) should be provided | "Missing required input. Please provide either a SOQL Query to filter opportunities or specify the number of Last Days to retrieve recent opportunities." |
| SOQL Syntax | If SOQL Query is provided, it must be valid SOQL syntax | "Invalid SOQL query syntax" |
| Days Range | If Last Days is provided, it must be a positive number | "Last Days must be a positive number" |

## Error Handling

### Input Errors (INPUT_ERROR)
**Cause**: Missing or invalid input parameters
**Error Message**: "Missing required input. Please provide either a SOQL Query to filter opportunities or specify the number of Last Days to retrieve recent opportunities."
**Resolution**:
1. Provide a valid SOQL Query string, OR
2. Specify the number of Last Days as a positive number
3. Ensure at least one parameter is provided

### Logic Errors (LOGIC_ERROR)
**Cause**: Invalid query syntax or business logic violations
**Error Messages**:
- "No opportunities found matching your criteria. Please verify your search parameters and try again."
- "Invalid SOQL query syntax"

**Resolution**:
1. Verify your SOQL query syntax is correct
2. Check that the query references valid fields and objects
3. Ensure your search criteria match existing opportunities
4. Verify field names are spelled correctly

### System Errors (SYSTEM_ERROR)
**Cause**: Connection issues, authentication failures, or API limits
**Error Messages**:
- "Request failed. Please verify that all required fields are provided and valid."
- "Authentication failed due to invalid credentials."
- "Unable to establish OAuth connection with Salesforce."

**Resolution**:
1. Check your network connection to Salesforce
2. Verify your authentication credentials are valid
3. Ensure you haven't exceeded Salesforce API limits
4. Check Salesforce service status

### Authorization Errors
**Cause**: Insufficient permissions to access opportunities or specific fields
**Resolution**:
1. Verify the authenticated user has read access to Opportunity objects
2. Check field-level security settings for queried fields
3. Ensure proper sharing rules are in place
4. Contact your Salesforce administrator if permissions are needed

## Usage Examples

### Example 1: Get Recent Opportunities
```
Input: 
- SOQL Query: (empty)
- Last Days: 7

Output: List of opportunities created or modified in the last 7 days
```

### Example 2: Custom SOQL Query
```
Input:
- SOQL Query: "SELECT Id, Name, Amount, StageName FROM Opportunity WHERE Amount > 10000"
- Last Days: (empty)

Output: List of opportunities with amount greater than $10,000
```

### Example 3: Specific Stage Opportunities
```
Input:
- SOQL Query: "SELECT Id, Name, Amount FROM Opportunity WHERE StageName = 'Proposal/Price Quote'"
- Last Days: (empty)

Output: List of opportunities in the Proposal/Price Quote stage
```

## Business Rules

1. **Query Priority**: If both parameters are provided, SOQL Query takes precedence
2. **Default Behavior**: If Last Days is used, retrieves opportunities modified in the specified timeframe
3. **Field Access**: Only returns fields the authenticated user has permission to access
4. **Record Limits**: Respects Salesforce query limits (typically 2000 records per query)

## Limitations

- Maximum 2000 records per query (Salesforce limitation)
- SOQL queries must be valid and follow Salesforce syntax rules
- Results limited by user's field-level and object-level permissions
- Complex queries may impact performance

## Best Practices

1. **Specific Queries**: Use specific SOQL queries for better performance
2. **Field Selection**: Only select fields you need to improve query performance
3. **Date Filtering**: Use appropriate date ranges to limit result sets
4. **Error Handling**: Always handle empty results gracefully
5. **Permissions**: Ensure authenticated user has appropriate Opportunity access

## Common Use Cases

### 1. Sales Pipeline Review
```
Scenario: Weekly sales pipeline analysis
Action: Get opportunities modified in the last 7 days
Result: Current pipeline status for team review
```

### 2. High-Value Opportunity Tracking
```
Scenario: Monitor large deals
Action: Query opportunities above a certain amount threshold
Result: Focus on high-impact opportunities
```

### 3. Stage-Specific Reporting
```
Scenario: Track opportunities by sales stage
Action: Filter opportunities by specific stage names
Result: Stage-specific performance metrics
```

## Related Catalog Requests

- [Opportunity Change Alert](pages/OpportunityChangeAlert.md) - Get notified of opportunity changes
- [Get Opportunities Summary](pages/GetOpportunitiesSummary.md) - Get aggregated opportunity data
- [Sales Pipeline Summary](pages/SalesPipelineSummary.md) - View pipeline by stage

## Technical Implementation

### Service Method
- **Class**: OpportunityDaoImpl
- **Method**: getOpportunities()
- **Package**: app.krista.extensions.crm.sales.salesforce_sales.impl

### Validation
- Parameter validation in OpportunityManagementArea
- SOQL syntax validation in SalesforceManager
- Date range validation for Last Days parameter

### Telemetry Metrics
- Query execution time
- Number of records returned
- Query type (SOQL vs. time-based)
- Success/failure rates

## Troubleshooting

### Empty Results
**Cause**: No opportunities match the criteria or insufficient permissions
**Solution**: 
1. Verify query criteria
2. Check user permissions
3. Confirm opportunities exist in the specified timeframe

### Query Errors
**Cause**: Invalid SOQL syntax or field access issues
**Solution**:
1. Validate SOQL syntax
2. Verify field names and accessibility
3. Check object-level permissions

### Performance Issues
**Cause**: Complex queries or large result sets
**Solution**:
1. Add appropriate filters to limit results
2. Use indexed fields in WHERE clauses
3. Consider pagination for large datasets

## See Also

- [Extension Configuration](pages/ExtensionConfiguration.md)
- [Authentication](pages/Authentication.md)
- [SOQL Query Guide](pages/SOQLQueryGuide.md)
