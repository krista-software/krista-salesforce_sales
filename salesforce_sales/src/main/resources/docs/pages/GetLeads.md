# Get Leads

## Overview

The Get Leads catalog request retrieves lead records from Salesforce Sales filtered by either user ownership or creation date. This request allows you to find leads owned by a specific user or retrieve newly created leads from a specified date.

## Request Details

- **Area**: Leads Management
- **Type**: QUERY_SYSTEM
- **Retry Support**: ❌ No
- **Authentication**: Supports both User Authentication and Service Account modes

## Input Parameters

| Parameter Name | Type | Required | Description | Example |
|----------------|------|----------|-------------|---------|
| Field | PickOne(By User\|New Leads) | Yes | Selection to filter leads by user or by creation date | "By User" or "New Leads" |
| Owner Id | Text | Conditional* | Salesforce user ID who owns the leads (required when Field = "By User") | "0055w00000ABC123" |
| Start Date Time | Date | Conditional* | Date from which to retrieve new leads (required when Field = "New Leads") | 1704067200000 (Unix timestamp) |

**Conditional Requirements***:
- When Field = "By User", Owner Id is required
- When Field = "New Leads", Start Date Time is required

## Output Parameters

| Parameter Name | Type | Description |
|----------------|------|-------------|
| Leads | List&lt;Entity(Lead)> | List of lead records matching the filter criteria |

See [Lead Entity](pages/entities/Lead.md) for complete field definitions.

## Validation Rules

| Validation | Error Message | Resolution |
|------------|---------------|------------|
| Field Selection Required | "Field selection is required. Please choose either 'By User' or 'New Leads' to filter the leads." | Select either "By User" or "New Leads" from the Field dropdown |
| Owner ID Required (By User) | "Owner ID is required when filtering by user. Please provide the Salesforce user ID who owns the leads." | Provide a valid Salesforce user ID in the Owner Id field |
| Start Date Required (New Leads) | "Start Date Time is required when filtering new leads. Please provide the date from which to retrieve new leads." | Provide a valid date/timestamp in the Start Date Time field |

## Error Handling

### Input Errors (INPUT_ERROR)
**Cause**: Missing or invalid input parameters
**Error Messages**:
- "Field selection is required. Please choose either 'By User' or 'New Leads' to filter the leads."
- "Owner ID is required when filtering by user. Please provide the Salesforce user ID who owns the leads."
- "Start Date Time is required when filtering new leads. Please provide the date from which to retrieve new leads."

**Resolution**:
1. Ensure Field parameter is provided and set to either "By User" or "New Leads"
2. If Field = "By User", provide a valid Owner Id
3. If Field = "New Leads", provide a valid Start Date Time
4. Verify all required parameters are not null or empty

### Logic Errors (LOGIC_ERROR)
**Cause**: Invalid user ID or date values
**Resolution**:
1. Verify the Owner Id exists in Salesforce
2. Ensure the Start Date Time is a valid Unix timestamp
3. Check that the date range is reasonable

### System Errors (SYSTEM_ERROR)
**Cause**: Connection issues or Salesforce API errors
**Resolution**:
1. Check network connectivity to Salesforce
2. Verify authentication credentials are valid
3. Ensure Salesforce API limits haven't been exceeded

### Authorization Errors
**Cause**: Insufficient permissions to access leads
**Resolution**:
1. Verify the authenticated user has read access to Lead objects
2. Check sharing rules allow access to the requested leads
3. Ensure field-level security permits access to lead fields

## Usage Examples

### Example 1: Get Leads by Owner
**Input**:
```
Field: "By User"
Owner Id: "0055w00000ABC123"
Start Date Time: (empty)
```

**Output**: List of all leads owned by user 0055w00000ABC123

**Result**: Returns leads assigned to the specified user

### Example 2: Get New Leads Since Date
**Input**:
```
Field: "New Leads"
Owner Id: (empty)
Start Date Time: 1704067200000
```

**Output**: List of leads created on or after January 1, 2024

**Result**: Returns recently created leads for follow-up
  "limit": 100
}
```

## Response Example

```json
{
  "leads": [
    {
      "id": "00Q5w00000ABC123",
      "name": "John Smith",
      "company": "Tech Solutions Inc",
      "title": "IT Director",
      "email": "john.smith@techsolutions.com",
      "phone": "+1-555-987-6543",
      "status": "Working - Contacted",
      "rating": "Hot",
      "leadSource": "Web",
      "industry": "Technology",
      "ownerId": "0055w00000ABC123",
      "createdDate": 1640995200000,
      "isConverted": false
    }
  ],
  "totalCount": 1,
  "hasMore": false
}
```

## Common Use Cases

1. **Lead Qualification**: Find leads that match specific criteria for follow-up
2. **Territory Management**: Retrieve leads assigned to specific sales reps
3. **Lead Scoring**: Filter leads by rating and source for prioritization
4. **Reporting**: Generate lead reports and analytics
5. **Lead Assignment**: Find unassigned leads for distribution
6. **Campaign Analysis**: Analyze leads from specific sources or campaigns

## Best Practices

1. **Use Filters**: Apply appropriate filters to reduce result set size
2. **Pagination**: Use limit and offset for large result sets
3. **Date Ranges**: Use date filters for time-based analysis
4. **Search Optimization**: Use specific search terms for better performance
5. **Regular Monitoring**: Regularly check for new leads requiring attention

## Error Handling

Common error scenarios:
- **Invalid Owner ID**: Specified owner ID doesn't exist
- **Invalid Date Format**: Date parameters must be in correct format
- **Permission Denied**: User lacks permission to view certain leads
- **Limit Exceeded**: Requested limit exceeds maximum allowed

## Related Requests

- [Lead Change Alert](pages/LeadChangeAlert.md) - Monitor lead changes
- [Account Change Alert](pages/AccountChangeAlert.md) - Monitor related account changes
- [Get Opportunities](pages/GetOpportunities.md) - Find related opportunities

## See Also

- [Lead Entity](pages/entities/Lead.md)
- [User Entity](pages/entities/User.md)
- [Account Entity](pages/entities/Account.md)
