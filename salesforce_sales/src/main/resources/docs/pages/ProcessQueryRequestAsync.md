# Process Query Request Async

## Overview

The Process Query Request Async catalog request executes custom SOQL queries asynchronously against Salesforce Sales data. This request is designed for large datasets that may exceed synchronous query limits.

## Request Details

- **Catalog Request Name**: Process Query Request Async
- **Description**: Execute custom SOQL queries asynchronously for large datasets
- **Type**: Async Query Operation
- **Returns**: Query job ID for result retrieval

## Parameters

### Required Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| query | Text | Yes | SOQL query statement to execute |

### Optional Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| batchSize | Number | No | Batch size for processing (default: 10000) |
| includeDeleted | Switch | No | Include deleted records (default: false) |
| maxWaitTime | Number | No | Maximum wait time in minutes (default: 60) |

## Request Examples

### Large Dataset Query
```json
{
  "query": "SELECT Id, Name, Amount, StageName, CloseDate, Owner.Name FROM Opportunity WHERE CreatedDate = LAST_N_YEARS:2",
  "batchSize": 5000
}
```

### Complex Aggregate Query
```json
{
  "query": "SELECT Account.Name, COUNT(Id) OpportunityCount, SUM(Amount) TotalValue FROM Opportunity WHERE CloseDate >= LAST_N_MONTHS:12 GROUP BY Account.Name HAVING COUNT(Id) > 5",
  "maxWaitTime": 30
}
```

## Response Format

### Job Submission Response
```json
{
  "success": true,
  "jobId": "7505w00000ABC123",
  "status": "Queued",
  "submittedDate": 1640995200000,
  "estimatedRecords": 50000,
  "query": "SELECT Id, Name, Amount FROM Opportunity WHERE CreatedDate = LAST_N_YEARS:2"
}
```

### Job Status Response
```json
{
  "success": true,
  "jobId": "7505w00000ABC123",
  "status": "InProgress",
  "recordsProcessed": 25000,
  "totalRecords": 50000,
  "percentComplete": 50,
  "startTime": 1640995200000,
  "estimatedCompletion": 1640995800000
}
```

## Job Statuses

| Status | Description |
|--------|-------------|
| Queued | Job submitted and waiting to start |
| InProgress | Job is currently executing |
| Completed | Job finished successfully |
| Failed | Job failed with errors |
| Aborted | Job was cancelled or aborted |

## Common Use Cases

1. **Large Data Exports**: Export large datasets for analysis
2. **Historical Analysis**: Query years of historical data
3. **Data Migration**: Extract data for system migrations
4. **Bulk Reporting**: Generate reports on large datasets
5. **Data Warehousing**: Extract data for data warehouse loading

## Best Practices

1. **Appropriate Batch Size**: Use optimal batch sizes for your data volume
2. **Query Optimization**: Optimize queries for async processing
3. **Monitor Progress**: Check job status regularly
4. **Error Handling**: Handle job failures gracefully
5. **Resource Management**: Don't submit too many concurrent jobs

## See Also

- [Get Results for Process Query Request Async](pages/GetResultsForProcessQueryRequestAsync.md)
- [Process Query Request](pages/ProcessQueryRequest.md)
