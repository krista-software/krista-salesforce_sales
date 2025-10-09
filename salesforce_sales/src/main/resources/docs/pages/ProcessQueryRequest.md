# Process Query Request

## Overview

The Process Query Request catalog request executes custom SOQL queries against Salesforce Sales data. This request provides flexible data retrieval capabilities for complex reporting and analytics needs.

## Request Details

- **Catalog Request Name**: Process Query Request
- **Description**: Execute custom SOQL queries for data retrieval
- **Type**: Query Operation
- **Returns**: Query results based on SOQL statement

## Parameters

### Required Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| query | Text | Yes | SOQL query statement to execute |

### Optional Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| maxResults | Number | No | Maximum number of records to return (default: 200) |
| includeDeleted | Switch | No | Include deleted records in results (default: false) |
| batchSize | Number | No | Batch size for large result sets (default: 200) |

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

### Common Errors
- **INVALID_QUERY**: SOQL syntax error
- **INVALID_FIELD**: Field doesn't exist or not accessible
- **INVALID_TYPE**: Invalid data type in comparison
- **QUERY_TIMEOUT**: Query execution timeout
- **INSUFFICIENT_ACCESS**: Insufficient permissions

### Error Prevention
- Validate SOQL syntax before execution
- Check field accessibility and existence
- Use appropriate data types in comparisons
- Optimize queries to prevent timeouts
- Verify user permissions for objects and fields

## See Also

- [Process Query Request Async](pages/ProcessQueryRequestAsync.md)
- [Get Results for Process Query Request Async](pages/GetResultsForProcessQueryRequestAsync.md)
- [Opportunity Entity](pages/entities/Opportunity.md)
- [Account Entity](pages/entities/Account.md)
