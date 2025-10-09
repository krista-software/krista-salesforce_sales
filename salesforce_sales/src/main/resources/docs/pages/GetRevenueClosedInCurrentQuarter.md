# Get Revenue Closed In Current Quarter

## Overview

This catalog request returns the total revenue amount that has been closed (won) during the current quarter. It provides a quick summary of actual revenue performance for the current quarter period.

## Request Details

- **Area**: Weekly Summary
- **Input Parameters**: None required
- **Authentication**: Required

## Input Parameters

No input parameters are required for this request.

## Output Parameters

| Parameter Name | Parameter Type | Description |
|----------------|----------------|-------------|
| Closed Revenue | Text | Total revenue amount closed in the current quarter |

## Usage Example

### Request
```json
{
  "catalogRequest": "Get Revenue Closed In Current Quarter"
}
```

### Response
```json
{
  "closedRevenue": "$1,250,000"
}
```

## Business Use Cases

1. **Quarterly Performance Tracking**: Monitor actual revenue achievement against quarterly targets
2. **Executive Reporting**: Provide leadership with current quarter performance metrics
3. **Sales Team Motivation**: Share team success and progress toward goals
4. **Forecasting**: Use closed revenue data for future quarter planning
5. **Commission Calculations**: Support commission and bonus calculations

## Related Requests

- [Get Revenue Expected To Close In Current Quarter](pages/GetRevenueExpectedToCloseInCurrentQuarter.md) - Expected revenue for current quarter
- [Sales Quota For The Quarter](pages/SalesQuotaForTheQuarter.md) - Quarterly sales targets
- [YTD Sales Revenue Closed](pages/YTDSalesRevenueClosed.md) - Year-to-date closed revenue

## Notes

- Revenue amounts are calculated based on opportunities with "Closed Won" status
- The current quarter is determined by the system date
- Results may be formatted as currency text (e.g., "$1,250,000")
- Data reflects real-time opportunity status

## See Also

- [Opportunity Entity](pages/entities/Opportunity.md)
- [Sales Pipeline Summary](pages/SalesPipelineSummary.md)
