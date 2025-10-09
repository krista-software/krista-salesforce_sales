# Value Of Opportunities Closed For Current Quarter

## Overview

This catalog request returns the values of opportunities that have been closed (won) during the current quarter. It can be filtered by specific owner IDs to get individual or team performance data.

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
| Quarter Revenue | List<MultiField> | List of closed opportunity values with details |

## Usage Examples

### Request (All Owners)
```json
{
  "catalogRequest": "Value Of Opportunities Closed For Current Quarter"
}
```

### Request (Specific Owner)
```json
{
  "catalogRequest": "Value Of Opportunities Closed For Current Quarter",
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
      "closedAmount": 250000,
      "opportunityCount": 5
    },
    {
      "ownerName": "Sarah Johnson",
      "ownerId": "0035g00000hUxJQAA0", 
      "closedAmount": 180000,
      "opportunityCount": 3
    }
  ]
}
```

## Business Use Cases

1. **Performance Analysis**: Track individual and team closed revenue performance
2. **Commission Calculations**: Support commission and bonus calculations
3. **Quota Tracking**: Monitor progress against quarterly quotas
4. **Team Comparisons**: Compare performance across sales team members
5. **Territory Analysis**: Analyze performance by territory or region

## Related Requests

- [Get Revenue Closed In Current Quarter](pages/GetRevenueClosedInCurrentQuarter.md)
- [YTD Sales Revenue Closed](pages/YTDSalesRevenueClosed.md)
- [Sales Quota For The Quarter](pages/SalesQuotaForTheQuarter.md)

## See Also

- [Opportunity Entity](pages/entities/Opportunity.md)
- [User Entity](pages/entities/User.md)
