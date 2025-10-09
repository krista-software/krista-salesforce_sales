# Sales Quota For The Quarter

## Overview

This catalog request returns sales quota details including owner names and quota amounts for the current quarter. It provides visibility into individual and team sales targets.

## Request Details

- **Area**: Reporting & Analytics
- **Input Parameters**: None required
- **Authentication**: Required

## Input Parameters

No input parameters are required for this request.

## Output Parameters

| Parameter Name | Parameter Type | Description |
|----------------|----------------|-------------|
| Sales Quota | List<MultiField> | List of quota records with owner details and amounts |

## Usage Example

### Request
```json
{
  "catalogRequest": "Sales Quota For The Quarter"
}
```

### Response
```json
{
  "salesQuota": [
    {
      "ownerName": "John Smith",
      "quotaAmount": 500000,
      "territory": "West Coast"
    },
    {
      "ownerName": "Sarah Johnson", 
      "quotaAmount": 750000,
      "territory": "Enterprise"
    }
  ]
}
```

## Business Use Cases

1. **Performance Tracking**: Monitor individual and team progress against quotas
2. **Territory Management**: Review quota distribution across territories
3. **Goal Setting**: Establish and communicate quarterly targets
4. **Commission Planning**: Support commission and incentive calculations
5. **Resource Allocation**: Allocate resources based on quota requirements

## Related Requests

- [Get Revenue Closed In Current Quarter](pages/GetRevenueClosedInCurrentQuarter.md)
- [Get Revenue Expected To Close In Current Quarter](pages/GetRevenueExpectedToCloseInCurrentQuarter.md)
- [YTD Sales Revenue Closed](pages/YTDSalesRevenueClosed.md)

## See Also

- [User Entity](pages/entities/User.md)
- [Opportunity Entity](pages/entities/Opportunity.md)
