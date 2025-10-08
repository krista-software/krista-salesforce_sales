# Sales Pipeline Summary

## Overview

The Sales Pipeline Summary catalog request provides aggregated analytics about your sales pipeline, showing opportunity distribution and values across different stages. This request delivers key insights for sales forecasting and pipeline management.

## Request Details

- **Catalog Request Name**: Sales Pipeline Summary
- **Description**: Get aggregated pipeline data by opportunity stage
- **Returns**: [Sales Pipeline Summary entities](pages/entities/SalesPipelineSummary.md)
- **Type**: Analytics Request

## Parameters

### Filter Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| ownerId | Text | No | Filter by specific opportunity owner |
| teamIds | Text Array | No | Filter by team or role hierarchy |
| accountTypes | PickOne Array | No | Filter by account types |
| opportunityTypes | PickOne Array | No | Filter by opportunity types |
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
| groupBy | PickOne | No | Group results by: Stage, Owner, Type, CloseDate |
| includeClosedWon | Switch | No | Include closed won opportunities (default: false) |
| includeClosedLost | Switch | No | Include closed lost opportunities (default: false) |

## Response Format

Returns an array of pipeline summary objects grouped by stage:

```json
{
  "pipelineSummary": [
    {
      "status": "Prospecting",
      "total": 500000.00,
      "average": 25000.00,
      "count": 20,
      "probability": 10
    },
    {
      "status": "Qualification",
      "total": 350000.00,
      "average": 35000.00,
      "count": 10,
      "probability": 25
    }
  ],
  "totalPipelineValue": 2475000.00,
  "totalOpportunities": 85,
  "averageDealSize": 29117.65,
  "weightedPipelineValue": 1237500.00
}
```

## Example Requests

### Basic Pipeline Summary
```json
{
  "includeClosedWon": false,
  "includeClosedLost": false
}
```

### Team Pipeline Analysis
```json
{
  "teamIds": ["00G5w00000ABC123", "00G5w00000DEF456"],
  "closeDateFrom": "2024-01-01",
  "closeDateTo": "2024-12-31",
  "minAmount": 10000
}
```

### Owner-Specific Pipeline
```json
{
  "ownerId": "0055w00000ABC123",
  "groupBy": "Stage",
  "includeClosedWon": true
}
```

### Quarterly Pipeline Review
```json
{
  "closeDateFrom": "2024-01-01",
  "closeDateTo": "2024-03-31",
  "accountTypes": ["Customer - Direct", "Customer - Channel"],
  "minAmount": 50000
}
```

## Pipeline Metrics

### Key Performance Indicators

| Metric | Description | Calculation |
|--------|-------------|-------------|
| Total Pipeline Value | Sum of all opportunity amounts | Sum(Amount) |
| Weighted Pipeline Value | Probability-adjusted pipeline value | Sum(Amount × Probability) |
| Average Deal Size | Average opportunity amount | Total Value ÷ Count |
| Stage Distribution | Percentage of opportunities per stage | Stage Count ÷ Total Count |
| Pipeline Velocity | Rate of stage progression | Time-based analysis |

### Stage Analysis

Each stage summary includes:
- **Total Value**: Sum of all opportunities in the stage
- **Average Value**: Average opportunity amount for the stage
- **Count**: Number of opportunities in the stage
- **Probability**: Average probability for the stage

## Common Use Cases

1. **Sales Forecasting**: Predict future revenue based on pipeline data
2. **Performance Analysis**: Analyze team and individual pipeline performance
3. **Resource Planning**: Allocate resources based on pipeline distribution
4. **Goal Setting**: Set realistic targets based on historical pipeline data
5. **Process Optimization**: Identify bottlenecks in the sales process
6. **Executive Reporting**: Provide leadership with pipeline insights

## Pipeline Health Indicators

### Healthy Pipeline Characteristics
- Balanced distribution across stages
- Consistent opportunity flow between stages
- Appropriate average deal sizes per stage
- Strong weighted pipeline value

### Warning Signs
- Too many opportunities in early stages
- Stagnant opportunities in middle stages
- Declining average deal sizes
- Low probability-weighted values

## Reporting Applications

### Dashboard Visualizations
- Pipeline funnel charts
- Stage distribution pie charts
- Trend analysis over time
- Comparative team performance

### Management Reports
- Executive pipeline summaries
- Team performance comparisons
- Forecast accuracy tracking
- Pipeline coverage analysis

## Best Practices

1. **Regular Monitoring**: Review pipeline summary regularly (weekly/monthly)
2. **Trend Analysis**: Compare current pipeline to historical data
3. **Stage Management**: Ensure opportunities progress through stages appropriately
4. **Data Quality**: Maintain accurate opportunity amounts and probabilities
5. **Forecasting**: Use weighted pipeline values for more accurate forecasts
6. **Action Planning**: Develop action plans based on pipeline insights

## Integration with Other Requests

### Related Analytics
- [Get Opportunities](pages/GetOpportunities.md) - Detailed opportunity data
- [Update Status Of Opportunities](pages/UpdateStatusOfOpportunities.md) - Pipeline management
- [YTD Sales Revenue Closed](pages/YTDSalesRevenueClosed.md) - Historical performance

### Workflow Integration
- Use pipeline data to trigger alerts and notifications
- Integrate with CRM workflows for stage management
- Connect to forecasting and planning systems

## See Also

- [Sales Pipeline Summary Entity](pages/entities/SalesPipelineSummary.md)
- [Opportunity Entity](pages/entities/Opportunity.md)
- [Get Opportunities](pages/GetOpportunities.md)
- [User Entity](pages/entities/User.md)
