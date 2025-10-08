# YTD Sales Revenue Closed

## Overview

This catalog request returns the total amount of closed opportunity values for the current calendar year (Year-To-Date). It can be filtered by specific owner IDs to get individual or team performance data.

## Request Details

- **Area**: Reporting & Analytics
- **Input Parameters**: Optional owner filtering
- **Authentication**: Required

## Input Parameters

| Parameter Name | Parameter Type | Mandatory | Description | Example |
|----------------|----------------|-----------|-------------|---------|
| Owner Id | Text | No | Filter results by specific opportunity owner | 0035g00000hUxJPAA0 |

## Output Parameters

| Parameter Name | Parameter Type | Description |
|----------------|----------------|-------------|
| Quarter Revenue | List<FreeForm> | Year-to-date closed revenue data |

## Usage Examples

### Request (All Owners)
```json
{
  "catalogRequest": "YTD Sales Revenue Closed"
}
```

### Request (Specific Owner)
```json
{
  "catalogRequest": "YTD Sales Revenue Closed",
  "ownerId": "0035g00000hUxJPAA0"
}
```

### Response
```json
{
  "quarterRevenue": [
    {
      "ownerName": "John Smith",
      "ownerId": "0035g00000hUxJPAA0",
      "ytdClosedAmount": 850000,
      "opportunityCount": 15,
      "quarters": {
        "Q1": 200000,
        "Q2": 300000,
        "Q3": 250000,
        "Q4": 100000
      }
    }
  ]
}
```

## Business Use Cases

1. **Annual Performance Tracking**: Monitor year-to-date sales performance
2. **Annual Commission Calculations**: Support annual bonus and commission calculations
3. **Yearly Quota Analysis**: Track progress against annual quotas
4. **Trend Analysis**: Analyze sales trends throughout the year
5. **Territory Performance**: Compare YTD performance across territories

## Related Requests

- [Value Of Opportunities Closed For Current Quarter](pages/ValueOfOpportunitiesClosedForCurrentQuarter.md)
- [Get Revenue Closed In Current Quarter](pages/GetRevenueClosedInCurrentQuarter.md)
- [Sales Quota For The Quarter](pages/SalesQuotaForTheQuarter.md)

## See Also

- [Opportunity Entity](pages/entities/Opportunity.md)
- [User Entity](pages/entities/User.md)
