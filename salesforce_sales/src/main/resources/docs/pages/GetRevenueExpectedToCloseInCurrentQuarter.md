# Get Revenue Expected To Close In Current Quarter

## Overview

This catalog request returns the total revenue amount that is expected to close during the current quarter based on active opportunities in the sales pipeline.

## Request Details

- **Area**: Weekly Summary
- **Input Parameters**: None required
- **Authentication**: Required

## Input Parameters

No input parameters are required for this request.

## Output Parameters

| Parameter Name | Parameter Type | Description |
|----------------|----------------|-------------|
| Expected Revenue | Text | Total revenue amount expected to close in the current quarter |

## Usage Example

### Request
```json
{
  "catalogRequest": "Get Revenue Expected To Close In Current Quarter"
}
```

### Response
```json
{
  "expectedRevenue": "$2,750,000"
}
```

## Business Use Cases

1. **Revenue Forecasting**: Predict potential revenue for the current quarter
2. **Pipeline Management**: Assess the health and potential of the sales pipeline
3. **Resource Planning**: Allocate resources based on expected deal closures
4. **Goal Tracking**: Compare expected revenue against quarterly targets

## Related Requests

- [Get Revenue Closed In Current Quarter](pages/GetRevenueClosedInCurrentQuarter.md)
- [Sales Quota For The Quarter](pages/SalesQuotaForTheQuarter.md)

## See Also

- [Opportunity Entity](pages/entities/Opportunity.md)
- [Sales Pipeline Summary](pages/SalesPipelineSummary.md)
