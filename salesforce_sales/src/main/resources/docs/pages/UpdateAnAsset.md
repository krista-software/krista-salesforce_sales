# Update An Asset

## Overview

The Update An Asset catalog request modifies existing asset records in Salesforce Sales. This request allows you to update asset information such as ownership, status, and other relevant details.

## Request Details

- **Catalog Request Name**: Update An Asset
- **Description**: Update existing asset records in Salesforce
- **Type**: Update Operation
- **Returns**: Update confirmation and results

## Parameters

### Required Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| assetId | Text | Yes | Unique identifier of the asset to update |

### Optional Update Fields

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| name | Text | No | Asset name |
| accountId | Text | No | Associated account ID |
| contactId | Text | No | Primary contact ID |
| ownerId | Text | No | Asset owner ID |
| rootAssetId | Text | No | Parent asset ID (for hierarchical assets) |
| isCompetitorProduct | Switch | No | Whether this is a competitor product |

### Additional Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| validateOnly | Switch | No | If true, validates updates without committing changes |
| updateReason | Text | No | Reason for asset update (for audit trail) |
| notifyOwner | Switch | No | Send notification to asset owner about changes |

## Request Examples

### Basic Asset Update
```json
{
  "assetId": "02i5w00000ABC123",
  "name": "Enterprise Software License - Updated",
  "ownerId": "0055w00000DEF456",
  "updateReason": "Ownership transfer to new account manager"
}
```

### Account Transfer
```json
{
  "assetId": "02i5w00000ABC123",
  "accountId": "0015w00000NEW123",
  "contactId": "0035w00000NEW123",
  "ownerId": "0055w00000NEW123",
  "updateReason": "Asset transferred to new account after acquisition",
  "notifyOwner": true
}
```

### Hierarchy Update
```json
{
  "assetId": "02i5w00000CHILD1",
  "rootAssetId": "02i5w00000PARENT2",
  "updateReason": "Reorganized asset hierarchy"
}
```

### Validation Only
```json
{
  "assetId": "02i5w00000ABC123",
  "accountId": "0015w00000NEW123",
  "validateOnly": true
}
```

## Response Format

### Successful Update
```json
{
  "success": true,
  "assetId": "02i5w00000ABC123",
  "updatedFields": ["Name", "Owner Id", "Account Id"],
  "updatedDate": 1640995200000,
  "message": "Asset successfully updated",
  "previousValues": {
    "name": "Enterprise Software License",
    "ownerId": "0055w00000ABC123",
    "accountId": "0015w00000ABC123"
  },
  "newValues": {
    "name": "Enterprise Software License - Updated",
    "ownerId": "0055w00000DEF456", 
    "accountId": "0015w00000ABC123"
  }
}
```

### Validation Response
```json
{
  "success": true,
  "assetId": "02i5w00000ABC123",
  "canUpdate": true,
  "warnings": [
    "Changing account will affect related service cases"
  ],
  "validationResults": {
    "accountId": "valid",
    "contactId": "valid",
    "ownerId": "valid"
  }
}
```

### Error Response
```json
{
  "success": false,
  "assetId": "02i5w00000ABC123",
  "error": "INVALID_ID",
  "message": "Specified account ID does not exist",
  "field": "accountId",
  "value": "0015w00000INVALID"
}
```

## Business Rules and Validation

### Update Restrictions
- Asset must exist and be accessible to the user
- Account and Contact must be valid if specified
- Owner must be an active user
- Root Asset must exist and not create circular references

### Field Dependencies
- **Contact**: Must belong to the specified Account
- **Root Asset**: Cannot reference itself or create circular hierarchy
- **Owner**: Must have appropriate permissions for the asset
- **Account**: Must be active and accessible

## Common Use Cases

1. **Ownership Changes**: Transfer assets to new account managers
2. **Account Transfers**: Move assets when accounts are transferred or acquired
3. **Hierarchy Management**: Reorganize asset parent-child relationships
4. **Contact Updates**: Update primary contacts for assets
5. **Data Correction**: Fix incorrect asset information
6. **Status Updates**: Update asset status and flags

## Asset Hierarchy Management

### Parent-Child Relationships
- Assets can have hierarchical relationships using rootAssetId
- Parent assets can have multiple child assets
- Child assets can only have one parent
- Circular references are not allowed

### Hierarchy Update Examples
```json
{
  "assetId": "02i5w00000CHILD1",
  "rootAssetId": "02i5w00000PARENT1",
  "updateReason": "Moved asset under new parent"
}
```

### Remove from Hierarchy
```json
{
  "assetId": "02i5w00000CHILD1",
  "rootAssetId": null,
  "updateReason": "Made asset independent"
}
```

## Best Practices

1. **Validate First**: Use validateOnly to check for issues before updating
2. **Document Changes**: Provide clear update reasons for audit trails
3. **Check Dependencies**: Verify related records won't be negatively affected
4. **Owner Notification**: Notify owners of significant changes
5. **Hierarchy Integrity**: Maintain logical asset hierarchies
6. **Data Quality**: Ensure all referenced records are valid and active

## Error Handling

### Common Errors
- **INVALID_ID**: Asset, Account, Contact, or Owner ID not found
- **INSUFFICIENT_ACCESS**: User lacks permission to update asset
- **FIELD_CUSTOM_VALIDATION_EXCEPTION**: Custom validation rules failed
- **CIRCULAR_DEPENDENCY**: Root Asset would create circular reference
- **ENTITY_IS_LOCKED**: Asset is locked by another process

### Error Prevention
- Verify all IDs exist before updating
- Check user permissions for asset updates
- Validate hierarchy changes don't create circular references
- Ensure asset is not locked by workflows or approvals

## Audit and Compliance

### Change Tracking
Asset updates should be tracked with:
- Who made the changes
- When changes were made
- What fields were changed
- Previous and new values
- Reason for changes

### Compliance Considerations
- Some changes may require approval workflows
- Asset transfers may need customer notification
- Regulatory requirements may mandate change documentation
- Service level agreements may be affected by ownership changes

## See Also

- [Asset Entity](pages/entities/Asset.md)
- [Delete An Asset](pages/DeleteAnAsset.md)
- [Account Entity](pages/entities/Account.md)
- [User Entity](pages/entities/User.md)
