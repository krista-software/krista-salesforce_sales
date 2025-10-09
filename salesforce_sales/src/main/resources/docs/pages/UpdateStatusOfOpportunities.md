# Update Status Of Opportunities

## Overview

The Update Status Of Opportunities catalog request allows you to modify the stage/status of one or more opportunity records in Salesforce Sales. This request enables bulk updates to opportunity progression through the sales pipeline.

## Request Details

- **Catalog Request Name**: Update Status Of Opportunities
- **Description**: Update the stage/status of multiple opportunities
- **Type**: Update Operation
- **Returns**: Update confirmation and results

## Parameters

### Required Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| opportunities | Array | Yes | Array of opportunities to update |

### Opportunity Update Object

Each opportunity in the array should contain:

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| id | Text | Yes | Unique identifier of the opportunity to update |
| stageName | PickOne | Yes | New stage/status for the opportunity |
| closeDate | Date | No | Updated close date (if stage requires it) |
| probability | Number | No | Updated probability percentage (0-100) |
| amount | Currency | No | Updated opportunity amount |
| description | Text | No | Updated description or notes |

### Optional Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| validateOnly | Switch | No | If true, validates updates without committing changes |
| notifyOwners | Switch | No | Send notification to opportunity owners about changes |
| updateReason | Text | No | Reason for the status update (for audit trail) |

## Opportunity Stages

### Standard Sales Stages

| Stage Name | Description | Probability | Typical Actions |
|------------|-------------|-------------|-----------------|
| Prospecting | Initial opportunity identification | 10% | Qualify opportunity |
| Qualification | Qualifying the opportunity | 25% | Needs analysis |
| Needs Analysis | Understanding requirements | 50% | Develop solution |
| Value Proposition | Presenting solution | 60% | Create proposal |
| Id. Decision Makers | Identifying key stakeholders | 70% | Engage decision makers |
| Perception Analysis | Understanding customer perception | 75% | Address concerns |
| Proposal/Price Quote | Formal proposal submitted | 80% | Negotiate terms |
| Negotiation/Review | Contract negotiation | 90% | Finalize agreement |
| Closed Won | Successfully closed deal | 100% | Implementation planning |
| Closed Lost | Lost opportunity | 0% | Post-mortem analysis |

## Request Examples

### Single Opportunity Update
```json
{
  "opportunities": [
    {
      "id": "0065w00000ABC123",
      "stageName": "Proposal/Price Quote",
      "probability": 80,
      "closeDate": "2024-03-31"
    }
  ],
  "notifyOwners": true,
  "updateReason": "Customer requested formal proposal"
}
```

### Bulk Status Update
```json
{
  "opportunities": [
    {
      "id": "0065w00000ABC123",
      "stageName": "Negotiation/Review",
      "probability": 90,
      "amount": 150000
    },
    {
      "id": "0065w00000DEF456", 
      "stageName": "Closed Won",
      "probability": 100,
      "closeDate": "2024-02-15"
    },
    {
      "id": "0065w00000GHI789",
      "stageName": "Closed Lost",
      "probability": 0,
      "description": "Lost to competitor - price sensitivity"
    }
  ],
  "notifyOwners": false,
  "updateReason": "End of quarter pipeline review"
}
```

### Validation Only
```json
{
  "opportunities": [
    {
      "id": "0065w00000ABC123",
      "stageName": "Closed Won",
      "probability": 100
    }
  ],
  "validateOnly": true
}
```

## Response Format

### Successful Update Response
```json
{
  "success": true,
  "updatedCount": 2,
  "results": [
    {
      "id": "0065w00000ABC123",
      "success": true,
      "previousStage": "Proposal/Price Quote",
      "newStage": "Negotiation/Review",
      "updatedFields": ["StageName", "Probability", "Amount"]
    },
    {
      "id": "0065w00000DEF456",
      "success": true,
      "previousStage": "Negotiation/Review", 
      "newStage": "Closed Won",
      "updatedFields": ["StageName", "Probability", "CloseDate"]
    }
  ],
  "errors": []
}
```

### Partial Success Response
```json
{
  "success": false,
  "updatedCount": 1,
  "results": [
    {
      "id": "0065w00000ABC123",
      "success": true,
      "previousStage": "Qualification",
      "newStage": "Needs Analysis"
    },
    {
      "id": "0065w00000INVALID",
      "success": false,
      "error": "INVALID_ID",
      "message": "Opportunity not found or access denied"
    }
  ],
  "errors": [
    {
      "opportunityId": "0065w00000INVALID",
      "errorCode": "INVALID_ID",
      "message": "Opportunity not found or access denied"
    }
  ]
}
```

## Business Rules and Validation

### Stage Progression Rules
- Some organizations enforce sequential stage progression
- Certain stages may require specific fields to be populated
- Probability should align with stage expectations
- Close date may be required for final stages

### Field Dependencies
- **Close Date**: Required for "Closed Won" and "Closed Lost" stages
- **Amount**: Should be populated before final stages
- **Probability**: Should match stage-appropriate percentages
- **Next Steps**: May be required for active stages

## Common Use Cases

1. **Pipeline Management**: Move opportunities through sales stages
2. **Bulk Updates**: Update multiple opportunities after team meetings
3. **Quarter-End Processing**: Close won/lost opportunities at period end
4. **Stage Alignment**: Ensure opportunities are in correct stages
5. **Probability Updates**: Adjust win probability based on new information
6. **Close Date Management**: Update expected close dates

## Best Practices

1. **Validate Before Update**: Use validateOnly flag to check for issues
2. **Appropriate Stages**: Ensure stage transitions make business sense
3. **Required Fields**: Populate all required fields for target stages
4. **Audit Trail**: Use updateReason for change tracking
5. **Owner Notification**: Notify owners of significant changes
6. **Batch Processing**: Group related updates for efficiency

## Error Handling

### Common Errors
- **INVALID_ID**: Opportunity ID not found or no access
- **REQUIRED_FIELD_MISSING**: Required fields not populated
- **INVALID_STAGE**: Stage name not valid for opportunity type
- **INSUFFICIENT_ACCESS**: User lacks permission to update opportunity
- **FIELD_CUSTOM_VALIDATION_EXCEPTION**: Custom validation rules failed

### Error Prevention
- Verify opportunity IDs before updating
- Check required fields for target stages
- Validate user permissions
- Test with validateOnly flag first

## See Also

- [Opportunity Entity](pages/entities/Opportunity.md)
- [Get Opportunities](pages/GetOpportunities.md)
- [Sales Pipeline Summary](pages/SalesPipelineSummary.md)
- [User Entity](pages/entities/User.md)
