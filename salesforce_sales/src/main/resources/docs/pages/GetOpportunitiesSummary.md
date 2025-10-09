# Get Opportunities Summary

## Overview

The Get Opportunities Summary catalog request provides aggregated data about opportunities in Salesforce Sales. This request delivers key metrics and insights for sales performance analysis and reporting.

## Request Details

- **Catalog Request Name**: Get Opportunities Summary
- **Description**: Get aggregated opportunity metrics and analytics
- **Returns**: Summary statistics and metrics
- **Type**: Analytics Request

## Parameters

### Filter Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| ownerId | Text | No | Filter by specific opportunity owner |
| teamIds | Text Array | No | Filter by team or role hierarchy |
| stageName | PickOne Array | No | Filter by opportunity stages |
| accountTypes | PickOne Array | No | Filter by account types |
| minAmount | Currency | No | Minimum opportunity amount |
| maxAmount | Currency | No | Maximum opportunity amount |

### Date Range Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| closeDateFrom | Date | No | Include opportunities closing on or after this date |
| closeDateTo | Date | No | Include opportunities closing on or before this date |
| createdDateFrom | Date | No | Include opportunities created on or after this date |
| createdDateTo | Date | No | Include opportunities created on or before this date |

### Grouping Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| groupBy | PickOne | No | Group results by: Stage, Owner, Type, Month |
| includeClosedWon | Switch | No | Include closed won opportunities (default: true) |
| includeClosedLost | Switch | No | Include closed lost opportunities (default: false) |

## Response Format

```json
{
  "summary": {
    "totalOpportunities": 150,
    "totalValue": 5000000.00,
    "averageDealSize": 33333.33,
    "weightedPipelineValue": 2500000.00,
    "winRate": 65.5,
    "averageSalesCycle": 45
  },
  "byStage": [
    {
      "stageName": "Prospecting",
      "count": 25,
      "totalValue": 500000.00,
      "averageValue": 20000.00,
      "probability": 10
    }
  ],
  "byOwner": [
    {
      "ownerId": "0055w00000ABC123",
      "ownerName": "John Smith",
      "count": 20,
      "totalValue": 800000.00,
      "winRate": 70.0
    }
  ],
  "trends": {
    "monthlyCreated": [
      {"month": "2024-01", "count": 15, "value": 450000.00},
      {"month": "2024-02", "count": 18, "value": 520000.00}
    ],
    "monthlyClosed": [
      {"month": "2024-01", "won": 8, "lost": 3, "value": 240000.00},
      {"month": "2024-02", "won": 12, "lost": 2, "value": 380000.00}
    ]
  }
}
```

## Key Metrics

### Summary Metrics
- **Total Opportunities**: Count of all opportunities in scope
- **Total Value**: Sum of all opportunity amounts
- **Average Deal Size**: Average opportunity amount
- **Weighted Pipeline Value**: Probability-adjusted pipeline value
- **Win Rate**: Percentage of opportunities closed won
- **Average Sales Cycle**: Average days from creation to close

### Stage Analysis
- Opportunity distribution across sales stages
- Value and count by stage
- Stage conversion rates
- Average time in each stage

### Owner Performance
- Individual sales rep performance
- Opportunity counts and values by owner
- Win rates by sales rep
- Territory performance analysis

## Common Use Cases

1. **Sales Performance Analysis**: Analyze team and individual performance
2. **Pipeline Management**: Monitor pipeline health and distribution
3. **Forecasting**: Generate revenue forecasts based on pipeline data
4. **Goal Tracking**: Track progress against sales targets
5. **Trend Analysis**: Identify trends in opportunity creation and closure
6. **Resource Planning**: Allocate resources based on opportunity data

## Example Requests

### Basic Summary
```json
{
  "includeClosedWon": true,
  "includeClosedLost": false
}
```

### Team Performance
```json
{
  "teamIds": ["00G5w00000ABC123"],
  "groupBy": "Owner",
  "closeDateFrom": "2024-01-01",
  "closeDateTo": "2024-12-31"
}
```

### Quarterly Analysis
```json
{
  "closeDateFrom": "2024-01-01",
  "closeDateTo": "2024-03-31",
  "groupBy": "Stage",
  "minAmount": 10000
}
```

## Best Practices

1. **Regular Monitoring**: Review opportunity summaries regularly
2. **Trend Analysis**: Compare current performance to historical data
3. **Goal Alignment**: Align metrics with business objectives
4. **Data Quality**: Ensure opportunity data is accurate and complete
5. **Actionable Insights**: Use data to drive sales actions and decisions

## See Also

- [Opportunity Entity](pages/entities/Opportunity.md)
- [Get Opportunities](pages/GetOpportunities.md)
- [Sales Pipeline Summary](pages/SalesPipelineSummary.md)
- [User Entity](pages/entities/User.md)
