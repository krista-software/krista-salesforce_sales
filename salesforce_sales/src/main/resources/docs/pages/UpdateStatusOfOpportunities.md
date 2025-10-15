# Update Status Of Opportunities

## Overview

The Update Status Of Opportunities catalog request updates the stage/status of multiple opportunity records in Salesforce Sales. This request enables bulk updates to move opportunities through the sales pipeline to a specific stage.

## Request Details

- **Area**: Opportunity Management
- **Type**: CHANGE_SYSTEM
- **Retry Support**: ❌ No
- **Authentication**: Supports both User Authentication and Service Account modes

## Input Parameters

| Parameter Name | Type | Required | Description | Example |
|----------------|------|----------|-------------|---------|
| Opportunities | List&lt;Entity(Opportunity)> | Yes | List of opportunities to update (only ID is required from each opportunity) | See example below |
| Status Or Stage Name | Text | Yes | The new stage/status to apply to all opportunities | "Proposal/Price Quote" |

**Note**: For each opportunity in the list, only the `id` field is used. Other fields in the opportunity entities are ignored.

## Output Parameters

| Parameter Name | Type | Description |
|----------------|------|-------------|
| Updated Opportunities | List&lt;Entity(Opportunity)> | List of updated opportunity records with all current field values |

See [Opportunity Entity](pages/entities/Opportunity.md) for complete field definitions.

## Validation Rules

| Validation | Error Message | Resolution |
|------------|---------------|------------|
| Opportunities Required | List must contain at least one opportunity | Provide at least one opportunity with a valid ID |
| Status Required | Status Or Stage Name must be provided | Provide a valid Salesforce opportunity stage name |
| Opportunity ID Required | Each opportunity must have a valid ID | Ensure all opportunities in the list have valid Salesforce IDs |

## Error Handling

### Input Errors (INPUT_ERROR)
**Cause**: Missing or invalid input parameters
**Resolution**:
1. Ensure Opportunities list is provided and not empty
2. Ensure Status Or Stage Name is provided and not empty
3. Verify each opportunity in the list has a valid ID
4. Check that the stage name is a valid Salesforce opportunity stage

### Logic Errors (LOGIC_ERROR)
**Cause**: Invalid opportunity IDs or stage names
**Resolution**:
1. Verify all opportunity IDs exist in Salesforce
2. Check that the stage name matches a valid Salesforce opportunity stage
3. Ensure the authenticated user has access to all opportunities
4. Verify opportunities haven't been deleted

### System Errors (SYSTEM_ERROR)
**Cause**: Salesforce API errors or connection issues
**Resolution**:
1. Check network connectivity to Salesforce
2. Verify authentication credentials are valid
3. Ensure Salesforce API limits haven't been exceeded
4. Check Salesforce service status

### Authorization Errors
**Cause**: Insufficient permissions to update opportunities
**Resolution**:
1. Verify the authenticated user has edit permission on Opportunity objects
2. Check sharing rules allow modification of the specific opportunities
3. Ensure field-level security permits editing the StageName field
4. Contact your Salesforce administrator if permissions are needed

## Usage Examples

### Example 1: Update Single Opportunity Stage
**Input**:
```
Opportunities: [
  {
    "id": "0065w00000ABC123"
  }
]
Status Or Stage Name: "Proposal/Price Quote"
```

**Output**: List containing the updated opportunity with StageName = "Proposal/Price Quote"

**Result**: Opportunity successfully moved to Proposal/Price Quote stage

### Example 2: Bulk Update Multiple Opportunities
**Input**:
```
Opportunities: [
  {
    "id": "0065w00000ABC123"
  },
  {
    "id": "0065w00000DEF456"
  },
  {
    "id": "0065w00000GHI789"
  }
]
Status Or Stage Name: "Closed Won"
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
