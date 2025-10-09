# Get Leads

## Overview

The Get Leads catalog request retrieves lead records from Salesforce Sales based on specified criteria. This request allows you to search for and filter leads to find potential customers and prospects that match your requirements.

## Request Details

- **Catalog Request Name**: Get Leads
- **Description**: Retrieve leads with filtering and search capabilities
- **Returns**: List of Lead entities

## Parameters

### Search and Filter Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| searchText | Text | No | Search text to find leads by name, company, or email |
| status | PickOne | No | Filter by lead status (Open, Working, Closed, etc.) |
| leadSource | PickOne | No | Filter by lead source (Web, Phone, Referral, etc.) |
| ownerId | Text | No | Filter by lead owner ID |
| industry | PickOne | No | Filter by industry classification |
| rating | PickOne | No | Filter by lead rating (Hot, Warm, Cold) |

### Date Range Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| createdDateFrom | Date | No | Filter leads created on or after this date |
| createdDateTo | Date | No | Filter leads created on or before this date |
| lastModifiedFrom | Date | No | Filter leads modified on or after this date |
| lastModifiedTo | Date | No | Filter leads modified on or before this date |

### Pagination Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| limit | Number | No | Maximum number of records to return (default: 100) |
| offset | Number | No | Number of records to skip for pagination |

## Response

Returns an array of [Lead entities](pages/entities/Lead.md) with the following key fields:

- **Id**: Unique lead identifier
- **Name**: Full name of the lead
- **Company**: Company name
- **Email**: Primary email address
- **Phone**: Primary phone number
- **Status**: Current lead status
- **Owner**: Lead owner information
- **Lead Source**: Source that generated the lead
- **Created Date**: When the lead was created

## Example Usage

### Basic Lead Search
```json
{
  "searchText": "John Smith",
  "limit": 50
}
```

### Filter by Status and Owner
```json
{
  "status": "Working - Contacted",
  "ownerId": "0055w00000ABC123",
  "limit": 25
}
```

### Date Range and Industry Filter
```json
{
  "industry": "Technology",
  "createdDateFrom": "2024-01-01",
  "createdDateTo": "2024-03-31",
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
