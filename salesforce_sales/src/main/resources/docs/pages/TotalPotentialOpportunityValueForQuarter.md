# Total Potential Opportunity Value For Quarter

## Overview

This catalog request returns the total amount of potential opportunity value for the current quarter, representing the sum of all active opportunities that could potentially close.

## Request Details

- **Area**: Reporting & Analytics
- **Input Parameters**: None required

## Input Parameters

No input parameters are required.

## Output Parameters

| Parameter Name | Parameter Type | Description |
|----------------|----------------|-------------|
| Projected Sum | Number | Total potential opportunity value for the current quarter |

## Usage Example

### Request
```json
{
  "catalogRequest": "Total Potential Opportunity Value For Quarter"
}
```

### Response
```json
{
  "projectedSum": 5250000
}
```

## Business Use Cases

1. **Pipeline Analysis**: Assess total pipeline potential for the quarter
2. **Capacity Planning**: Understand maximum possible revenue if all deals close
3. **Risk Assessment**: Compare potential value against quota requirements

## Related Requests

- [Get Revenue Expected To Close In Current Quarter](pages/GetRevenueExpectedToCloseInCurrentQuarter.md)
- [Sales Quota For The Quarter](pages/SalesQuotaForTheQuarter.md)

## See Also

- [Opportunity Entity](pages/entities/Opportunity.md)
