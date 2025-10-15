# Update An Asset

## Overview

The Update An Asset catalog request modifies existing asset records in Salesforce Sales. This request allows you to update asset information including name, account, contact, and competitor product status.

## Request Details

- **Area**: Assets Management
- **Type**: CHANGE_SYSTEM
- **Retry Support**: ❌ No
- **Authentication**: Supports both User Authentication and Service Account modes

## Input Parameters

| Parameter Name | Type | Required | Description | Example |
|----------------|------|----------|-------------|---------|
| Id | Text | Yes | Salesforce ID of the asset to update | "02i5w00000ABC123" |
| Name | Text | Yes | Name of the asset | "Enterprise Software License" |
| Contact Id | Text | No | Salesforce ID of the primary contact for this asset | "0035w00000DEF456" |
| Account Id | Text | No | Salesforce ID of the account that owns this asset | "0015w00000GHI789" |
| Is Competitor Product | Switch (Boolean) | No | Indicates whether this is a competitor's product | true or false |

## Output Parameters

| Parameter Name | Type | Description |
|----------------|------|-------------|
| Asset | Entity(Asset) | The updated asset record with all current field values |

See [Asset Entity](pages/entities/Asset.md) for complete field definitions.

## Validation Rules

| Validation | Error Message | Resolution |
|------------|---------------|------------|
| ID Required | "Asset ID is required. Please provide the Salesforce ID of the asset you want to update." | Provide a valid Salesforce asset ID in the Id field |
| Name Required | "Asset Name is required. Please provide a name for the asset." | Provide a name for the asset in the Name field |
| ID Format | Asset ID must be a valid Salesforce ID format | Ensure the ID is a valid 15 or 18-character Salesforce ID |

## Error Handling

### Input Errors (INPUT_ERROR)
**Cause**: Missing or invalid required parameters
**Error Messages**:
- "Asset ID is required. Please provide the Salesforce ID of the asset you want to update."
- "Asset Name is required. Please provide a name for the asset."

**Resolution**:
1. Ensure the Id parameter is provided and not empty
2. Ensure the Name parameter is provided and not empty
3. Verify the ID is a valid Salesforce asset ID format (15 or 18 characters)
4. Check that all required fields have valid values

### Logic Errors (LOGIC_ERROR)
**Cause**: Asset ID does not exist or invalid field values
**Resolution**:
1. Verify the asset ID exists in Salesforce
2. Check that the authenticated user has access to the asset
3. Ensure Contact Id and Account Id (if provided) are valid Salesforce IDs
4. Verify the asset hasn't been deleted

### System Errors (SYSTEM_ERROR)
**Cause**: Salesforce API errors or connection issues
**Resolution**:
1. Check network connectivity to Salesforce
2. Verify authentication credentials are valid
3. Ensure Salesforce API limits haven't been exceeded
4. Check Salesforce service status

### Authorization Errors
**Cause**: Insufficient permissions to update assets
**Resolution**:
1. Verify the authenticated user has edit permission on Asset objects
2. Check sharing rules allow modification of the specific asset
3. Ensure field-level security permits editing of the specified fields
4. Contact your Salesforce administrator if permissions are needed

## Usage Examples

### Example 1: Update Asset Name and Account
**Input**:
```
Id: "02i5w00000ABC123"
Name: "Updated Enterprise License"
Account Id: "0015w00000NEW789"
Contact Id: (empty)
Is Competitor Product: (empty)
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
