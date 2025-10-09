# Get Results for Process Query Request Async

## Overview

This catalog request retrieves the results of an asynchronous query request using the Job ID returned from a previous "Process Query Request Async" call. It allows you to check the status and get results of long-running queries.

## Request Details

- **Area**: Reporting & Analytics
- **Input Parameters**: Job ID required
- **Authentication**: Required

## Input Parameters

| Parameter Name | Parameter Type | Mandatory | Description | Example |
|----------------|----------------|-----------|-------------|---------|
| Job Id | String | Yes | The Job ID returned from Process Query Request Async | "job_12345_abc" |

## Output Parameters

| Parameter Name | Parameter Type | Description |
|----------------|----------------|-------------|
| Response | FreeForm | Generic response containing query results or status |

## Usage Example

### Request
```json
{
  "catalogRequest": "Get Results for Process Query Request Async",
  "jobId": "job_12345_abc"
}
```

### Response (Completed)
```json
{
  "response": {
    "status": "completed",
    "results": [
      {
        "id": "0065w00000ABC123",
        "name": "Enterprise Deal",
        "amount": 100000,
        "stage": "Proposal"
      }
    ],
    "totalRecords": 1,
    "executionTime": "2.5 seconds"
  }
}
```

### Response (In Progress)
```json
{
  "response": {
    "status": "in_progress",
    "progress": "75%",
    "estimatedCompletion": "30 seconds"
  }
}
```

## Job Status Values

- **queued**: Job is waiting to be processed
- **in_progress**: Job is currently running
- **completed**: Job finished successfully
- **failed**: Job encountered an error
- **cancelled**: Job was cancelled

## Business Use Cases

1. **Large Data Queries**: Handle queries that return large datasets
2. **Complex Reports**: Process complex analytical queries
3. **Background Processing**: Run queries without blocking user interface
4. **Performance Optimization**: Avoid timeouts on long-running queries
5. **Batch Operations**: Process multiple records asynchronously

## Error Handling

### Invalid Job ID
```json
{
  "response": {
    "status": "error",
    "message": "Job ID not found or expired"
  }
}
```

### Failed Job
```json
{
  "response": {
    "status": "failed",
    "error": "Query timeout exceeded",
    "details": "Query execution time exceeded 5 minutes"
  }
}
```

## Related Requests

- [Process Query Request Async](pages/ProcessQueryRequestAsync.md) - Initiate asynchronous query
- [Process Query Request](pages/ProcessQueryRequest.md) - Synchronous query processing

## Best Practices

1. **Polling Interval**: Check results every 5-10 seconds for in-progress jobs
2. **Timeout Handling**: Implement reasonable timeout limits
3. **Error Recovery**: Handle failed jobs gracefully
4. **Job ID Storage**: Store Job IDs for result retrieval
5. **Status Monitoring**: Monitor job progress for user feedback

## See Also

- [Process Query Request Async](pages/ProcessQueryRequestAsync.md)
- [Process Query Request](pages/ProcessQueryRequest.md)
