# Process Query Request

## Overview

The Process Query Request catalog request executes custom SOQL (Salesforce Object Query Language) queries against Salesforce Sales data. This request provides flexible data retrieval capabilities for complex reporting and analytics needs.

## Request Details

- **Area**: Reporting
- **Type**: CHANGE_SYSTEM
- **Retry Support**: ❌ No
- **Authentication**: Supports both User Authentication and Service Account modes

## Input Parameters

| Parameter Name | Type | Required | Description | Example |
|----------------|------|----------|-------------|---------|
| Query | Paragraph | Yes | SOQL query statement to execute | "SELECT Id, Name, Amount FROM Opportunity WHERE Amount > 50000" |

## Output Parameters

| Parameter Name | Type | Description |
|----------------|------|-------------|
| Response | Composite (Object) | Query results as a JSON object containing the records and metadata |

## Validation Rules

| Validation | Error Message | Resolution |
|------------|---------------|------------|
| Query Required | Query parameter must be provided | Provide a valid SOQL query string |
| Valid SOQL Syntax | Query must use valid SOQL syntax | Verify your SOQL query syntax is correct |

## SOQL Query Structure

### Basic Query Format
```sql
SELECT field1, field2, field3
FROM ObjectName
WHERE condition
ORDER BY field
LIMIT number
```

### Supported Objects
- Account
- Opportunity
- Lead
- Contact
- Task
- Event
- Asset
- Quote
- User
- Custom Objects

## Request Examples

### Basic Opportunity Query
```json
{
  "query": "SELECT Id, Name, Amount, StageName, CloseDate FROM Opportunity WHERE Amount > 50000 ORDER BY CloseDate DESC LIMIT 100"
}
```

### Account with Related Opportunities
```json
{
  "query": "SELECT Id, Name, (SELECT Id, Name, Amount FROM Opportunities WHERE StageName != 'Closed Lost') FROM Account WHERE Type = 'Customer - Direct'"
}
```

### Lead Analysis Query
```json
{
  "query": "SELECT LeadSource, COUNT(Id) LeadCount, AVG(Rating) AvgRating FROM Lead WHERE CreatedDate = THIS_QUARTER GROUP BY LeadSource"
}
```

### Complex Filtering
```json
{
  "query": "SELECT Id, Name, Amount, Owner.Name FROM Opportunity WHERE Amount > 100000 AND StageName IN ('Proposal/Price Quote', 'Negotiation/Review') AND CloseDate >= TODAY ORDER BY Amount DESC",
  "maxResults": 50
}
```

## Response Format

### Standard Query Response
```json
{
  "success": true,
  "records": [
    {
      "Id": "0065w00000ABC123",
      "Name": "Acme Corp - Enterprise Deal",
      "Amount": 150000.00,
      "StageName": "Negotiation/Review",
      "CloseDate": "2024-03-31"
    }
  ],
  "totalSize": 25,
  "done": true
}
```

### Aggregate Query Response
```json
{
  "success": true,
  "records": [
    {
      "LeadSource": "Web",
      "LeadCount": 45,
      "AvgRating": 2.8
    },
    {
      "LeadSource": "Referral",
      "LeadCount": 23,
      "AvgRating": 3.2
    }
  ],
  "totalSize": 5,
  "done": true
}
```

## SOQL Features

### Field Selection
- Select specific fields: `SELECT Id, Name, Amount`
- Select all fields: `SELECT FIELDS(ALL)`
- Select standard fields: `SELECT FIELDS(STANDARD)`
- Select custom fields: `SELECT FIELDS(CUSTOM)`

### Filtering (WHERE Clause)
- Comparison operators: `=`, `!=`, `<`, `>`, `<=`, `>=`
- Logical operators: `AND`, `OR`, `NOT`
- List operators: `IN`, `NOT IN`
- Pattern matching: `LIKE`
- Null checks: `IS NULL`, `IS NOT NULL`

### Sorting and Limiting
- Order by: `ORDER BY field ASC/DESC`
- Limit results: `LIMIT number`
- Offset results: `OFFSET number`

### Aggregate Functions
- `COUNT()` - Count records
- `SUM()` - Sum numeric values
- `AVG()` - Average numeric values
- `MIN()` - Minimum value
- `MAX()` - Maximum value

### Date Functions
- `TODAY` - Current date
- `YESTERDAY` - Previous date
- `THIS_WEEK` - Current week
- `THIS_MONTH` - Current month
- `THIS_QUARTER` - Current quarter
- `THIS_YEAR` - Current year

## Common Use Cases

1. **Custom Reports**: Generate reports not available through standard reporting
2. **Data Analysis**: Perform complex data analysis and aggregation
3. **Integration**: Extract data for external system integration
4. **Bulk Operations**: Identify records for bulk processing
5. **Data Validation**: Validate data quality and consistency
6. **Performance Analysis**: Analyze sales performance metrics

## Best Practices

1. **Selective Fields**: Only select fields you need to improve performance
2. **Appropriate Filters**: Use WHERE clauses to limit result sets
3. **Indexing**: Use indexed fields in WHERE clauses when possible
4. **Limit Results**: Use LIMIT to prevent large result sets
5. **Query Optimization**: Optimize queries for better performance
6. **Error Handling**: Handle query errors gracefully

## Query Limitations

### Governor Limits
- Maximum query execution time: 120 seconds
- Maximum records returned: 50,000
- Maximum SOQL queries per transaction: 100
- Maximum query length: 20,000 characters

### Security Restrictions
- Field-level security enforced
- Object permissions enforced
- Sharing rules applied
- Profile restrictions honored

## Error Handling

### Input Errors (INPUT_ERROR)
**Cause**: Missing or invalid query parameter
**Resolution**:
1. Ensure Query parameter is provided and not empty
2. Verify the query is a valid SOQL statement
3. Check that the query syntax follows Salesforce SOQL rules

### Logic Errors (LOGIC_ERROR)
**Cause**: Invalid SOQL syntax or query execution errors
**Error Messages**:
- "INVALID_QUERY": SOQL syntax error
- "INVALID_FIELD": Field doesn't exist or not accessible
- "INVALID_TYPE": Invalid data type in comparison
- "MALFORMED_QUERY": Query structure is incorrect

**Resolution**:
1. Validate SOQL syntax before execution
2. Check that all field names are spelled correctly
3. Verify all referenced objects exist
4. Ensure data types in comparisons are compatible
5. Test the query in Salesforce Developer Console first

### System Errors (SYSTEM_ERROR)
**Cause**: Salesforce API errors, connection issues, or query timeouts
**Error Messages**:
- "QUERY_TIMEOUT": Query execution exceeded time limit
- "Request failed. Please verify that all required fields are provided and valid."

**Resolution**:
1. Check network connectivity to Salesforce
2. Verify authentication credentials are valid
3. Optimize complex queries to reduce execution time
4. Add appropriate filters to limit result sets
5. Ensure Salesforce API limits haven't been exceeded

### Authorization Errors
**Cause**: Insufficient permissions to access objects or fields
**Error Message**: "INSUFFICIENT_ACCESS": User lacks permission to query objects or fields

**Resolution**:
1. Verify the authenticated user has read access to queried objects
2. Check field-level security for all fields in the SELECT clause
3. Ensure sharing rules allow access to the records
4. Contact your Salesforce administrator if permissions are needed

## See Also

- [Process Query Request Async](pages/ProcessQueryRequestAsync.md)
- [Get Results for Process Query Request Async](pages/GetResultsForProcessQueryRequestAsync.md)
- [Opportunity Entity](pages/entities/Opportunity.md)
- [Account Entity](pages/entities/Account.md)
