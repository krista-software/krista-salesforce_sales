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
| Parameter Validation | At least one parameter (SOQL Query or Last Days) should be provided | "Either SOQL Query or Last Days parameter must be provided" |
| SOQL Syntax | If SOQL Query is provided, it must be valid SOQL syntax | "Invalid SOQL query syntax" |
| Days Range | If Last Days is provided, it must be a positive number | "Last Days must be a positive number" |

## Error Handling

### System Errors
- **Connection Error**: Returns empty list if unable to connect to Salesforce
- **Authentication Error**: Throws authentication exception if credentials are invalid
- **API Limit Error**: Returns partial results if API limits are exceeded

### Business Rule Errors
- **Invalid Query**: Returns empty list if SOQL query is malformed
- **Permission Error**: Returns empty list if user lacks permission to access opportunities

### Validation Errors
- **Missing Parameters**: Throws IllegalArgumentException if both parameters are null/empty
- **Invalid Days**: Throws IllegalArgumentException if Last Days is negative or zero

### User Errors
- **No Results**: Returns empty list if no opportunities match the criteria
- **Query Timeout**: Returns empty list if query execution times out

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
