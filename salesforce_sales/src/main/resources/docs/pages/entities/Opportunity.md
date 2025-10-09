# Opportunity Entity

## Overview

The Opportunity entity represents a potential sale or deal in Salesforce Sales. Opportunities track the progress of deals through various stages of the sales process, from initial qualification to closing. They are central to sales forecasting, pipeline management, and revenue tracking.

## Entity Details

- **Entity Name**: Opportunity
- **Primary Key**: Id
- **Searchable Fields**: Name, Id, Owner Id, Account Id, Stage Name, Amount, Close Date, and more
- **Display Fields**: Name, Stage Name, Amount, Close Date
- **Store Support**: No

## Fields

### Core Fields

| Field Name | Type | Required | Description |
|------------|------|----------|-------------|
| Id | Text | No | Unique identifier for the Opportunity record (searchable) |
| Name | Text | Yes | Name of the opportunity (searchable, used for display) |
| Stage Name | PickOne | Yes | Current stage in the sales process (searchable, used for display) |
| Close Date | Date | Yes | Expected or actual close date (searchable, used for display) |
| Amount | Currency | No | Expected revenue amount (searchable, used for display) |
| Probability | Percentage | No | Probability of closing (0-100%) |

### Stage Name Values

The Stage Name field accepts the following predefined values:
- Appointment scheduled
- Qualified to buy
- Presentation scheduled
- Decision maker bought-in
- Contract sent
- Closed won
- Closed lost

### Relationship Fields

| Field Name | Type | Required | Description |
|------------|------|----------|-------------|
| Account Id | Text | No | ID of the associated account (searchable) |
| Account Name | Text | No | Name of the associated account (custom, searchable) |
| Contact Id | Text | No | ID of the primary contact |
| Owner Id | Text | No | ID of the opportunity owner (searchable) |
| Owner Name | Text | No | Name of the opportunity owner (custom, searchable) |
| Pricebook Id | Text | No | ID of the associated pricebook |

### Sales Information

| Field Name | Type | Required | Description |
|------------|------|----------|-------------|
| Expected Revenue | Currency | No | Calculated expected revenue (Amount × Probability) |
| Type | Text | No | Type of opportunity (custom, searchable) |
| Lead Source | Text | No | Source that generated this opportunity (custom, searchable) |
| Next Step | Text | No | Next action to be taken (custom, searchable) |
| ACV | Currency | No | Annual Contract Value (custom, searchable) |

### Competitive Information

| Field Name | Type | Required | Description |
|------------|------|----------|-------------|
| Partner | Text | No | Partner involved in the deal (custom, searchable) |
| Competition | Text | No | Competitive information (custom, searchable) |

### System Fields

| Field Name | Type | Required | Description |
|------------|------|----------|-------------|
| Created Date | Date | No | Date and time when created (searchable, includes time) |
| Last Modified Date | Date | No | Date and time of last modification (searchable, includes time) |
| Description | Paragraph | No | Detailed description of the opportunity |

## Usage in Catalog Requests

The Opportunity entity is used in the following catalog requests:

- **Get Opportunities**: Retrieve opportunities with filtering options
- **Update Status Of Opportunities**: Change the stage of opportunities
- **Get Opportunities Summary**: Regional opportunity summaries
- **Sales Pipeline Summary**: Pipeline analysis by stage
- **YTD Sales Revenue Closed**: Year-to-date closed revenue
- **Value Of Opportunities Closed For Current Quarter**: Quarterly revenue analysis
- **Total Potential Opportunity Value For Quarter**: Quarterly projections

## Relationships

- **Many-to-One with Account**: Each opportunity belongs to one account
- **Many-to-One with Contact**: Each opportunity has one primary contact
- **Many-to-One with User (Owner)**: Each opportunity has one owner
- **Many-to-One with Pricebook**: Each opportunity can use one pricebook

## Validation Rules

- **Name**: Required field, cannot be empty
- **Stage Name**: Must be one of the predefined values
- **Close Date**: Required field, must be a valid date
- **Amount**: Must be a positive number if provided
- **Probability**: Must be between 0 and 100 if provided

## Stage Progression

Typical opportunity stage progression:
1. **Appointment scheduled** → Initial meeting set up
2. **Qualified to buy** → Prospect has budget, authority, need, timeline
3. **Presentation scheduled** → Product/service demonstration planned
4. **Decision maker bought-in** → Key stakeholders are convinced
5. **Contract sent** → Legal documents in review
6. **Closed won** → Deal successfully closed
7. **Closed lost** → Deal lost to competition or cancelled

## Best Practices

1. **Naming Convention**: Use descriptive names that include company and product/service
2. **Stage Management**: Keep stages updated to reflect actual deal progress
3. **Amount Accuracy**: Regularly update amounts based on latest information
4. **Close Date Realism**: Set realistic close dates and update as needed
5. **Probability Alignment**: Ensure probability aligns with stage
6. **Next Steps**: Always define clear next steps for active opportunities

## Common Use Cases

1. **Sales Pipeline Management**: Track deals through sales stages
2. **Revenue Forecasting**: Predict future revenue based on pipeline
3. **Performance Analysis**: Analyze win rates and sales cycles
4. **Territory Management**: Organize opportunities by owner or region
5. **Competitive Analysis**: Track competition and win/loss reasons

## Example Usage

```json
{
  "id": "0065w00000ABC123",
  "name": "Acme Corp - Software License Renewal",
  "accountId": "0015w00000ABC123",
  "accountName": "Acme Corporation",
  "ownerId": "0055w00000ABC123",
  "ownerName": "John Smith",
  "stageName": "Presentation scheduled",
  "amount": 50000.00,
  "probability": 60,
  "closeDate": 1640995200000,
  "expectedRevenue": 30000.00,
  "type": "Existing Customer - Renewal",
  "leadSource": "Inbound Call",
  "nextStep": "Schedule product demo"
}
```

## See Also

- [Account Entity](pages/entities/Account.md)
- [User Entity](pages/entities/User.md)
- [PricebookEntry Entity](pages/entities/PricebookEntry.md)
- [Get Opportunities](pages/GetOpportunities.md)
- [Update Status Of Opportunities](pages/UpdateStatusOfOpportunities.md)
