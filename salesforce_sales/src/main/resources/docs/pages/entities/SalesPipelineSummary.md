# Sales Pipeline Summary Entity

## Overview

The Sales Pipeline Summary entity provides aggregated data about opportunities grouped by their stage or status. It offers insights into the sales pipeline performance, showing average deal sizes and total values for each stage of the sales process.

## Entity Details

- **Entity Name**: Sales Pipeline Summary
- **Primary Key**: Status
- **Display Fields**: Status, Average, Total

## Fields

| Field Name | Type | Required | Description |
|------------|------|----------|-------------|
| Status | Text | Yes | Sales stage or status (primary key) |
| Average | Currency | Yes | Average opportunity amount for this status |
| Total | Currency | Yes | Total value of all opportunities in this status |

## Pipeline Stages (Common)

- **Prospecting**: Initial stage, identifying potential opportunities
- **Qualification**: Qualifying leads and determining fit
- **Needs Analysis**: Understanding customer requirements
- **Value Proposition**: Presenting solution and value
- **Proposal/Price Quote**: Formal proposal and pricing stage
- **Negotiation/Review**: Contract negotiation and review
- **Closed Won**: Successfully closed deals
- **Closed Lost**: Lost opportunities

## Usage in Catalog Requests

The Sales Pipeline Summary entity is used in:

- **Sales Pipeline Summary**: Get pipeline analysis by stage
- **Sales reporting and analytics**: Pipeline performance metrics
- **Forecasting**: Revenue projections based on pipeline data

## Pipeline Analysis

### Key Metrics
- **Total Pipeline Value**: Sum of all opportunities in active stages
- **Average Deal Size**: Average opportunity amount by stage
- **Stage Distribution**: How opportunities are distributed across stages
- **Conversion Rates**: Success rates between stages

### Performance Indicators
- **Pipeline Velocity**: How quickly deals move through stages
- **Stage Duration**: Average time spent in each stage
- **Win Rate**: Percentage of opportunities that close successfully
- **Pipeline Coverage**: Pipeline value vs. quota requirements

## Common Use Cases

1. **Sales Forecasting**: Predict future revenue based on pipeline
2. **Performance Analysis**: Analyze sales team and individual performance
3. **Process Optimization**: Identify bottlenecks in the sales process
4. **Resource Planning**: Allocate resources based on pipeline needs
5. **Goal Setting**: Set realistic targets based on pipeline data
6. **Executive Reporting**: Provide leadership with pipeline insights

## Example Usage

```json
[
  {
    "status": "Prospecting",
    "average": 25000.00,
    "total": 500000.00
  },
  {
    "status": "Qualification", 
    "average": 35000.00,
    "total": 350000.00
  },
  {
    "status": "Proposal/Price Quote",
    "average": 50000.00,
    "total": 400000.00
  },
  {
    "status": "Negotiation/Review",
    "average": 75000.00,
    "total": 225000.00
  },
  {
    "status": "Closed Won",
    "average": 60000.00,
    "total": 1200000.00
  }
]
```

## Pipeline Health Indicators

### Healthy Pipeline
- Balanced distribution across stages
- Consistent average deal sizes
- Regular movement between stages
- Strong conversion rates

### Warning Signs
- Too many opportunities in early stages
- Deals stuck in middle stages
- Declining average deal sizes
- Low conversion rates

## Reporting Applications

### Management Dashboards
- Visual pipeline representation
- Trend analysis over time
- Comparison to quotas and targets
- Team performance metrics

### Sales Team Tools
- Individual pipeline views
- Stage-specific action items
- Deal prioritization
- Forecast accuracy tracking

## See Also

- [Opportunity Entity](pages/entities/Opportunity.md)
- [Sales Pipeline Summary](pages/SalesPipelineSummary.md)
- [Get Opportunities](pages/GetOpportunities.md)
