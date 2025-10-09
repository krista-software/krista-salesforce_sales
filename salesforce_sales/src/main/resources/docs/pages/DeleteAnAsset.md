# Delete An Asset

## Overview

The Delete An Asset catalog request removes asset records from Salesforce Sales. This request allows you to delete assets that are no longer needed or relevant to customer accounts.

## Request Details

- **Catalog Request Name**: Delete An Asset
- **Description**: Delete asset records from Salesforce
- **Type**: Delete Operation
- **Returns**: Deletion confirmation and results

## Parameters

### Required Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| assetId | Text | Yes | Unique identifier of the asset to delete |

### Optional Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| validateOnly | Switch | No | If true, validates deletion without committing changes |
| deleteReason | Text | No | Reason for asset deletion (for audit trail) |
| cascadeDelete | Switch | No | Delete related records (default: false) |

## Request Examples

### Basic Asset Deletion
```json
{
  "assetId": "02i5w00000ABC123",
  "deleteReason": "Product discontinued"
}
```

### Validation Only
```json
{
  "assetId": "02i5w00000ABC123",
  "validateOnly": true
}
```

### Cascade Deletion
```json
{
  "assetId": "02i5w00000ABC123",
  "cascadeDelete": true,
  "deleteReason": "Customer account closed"
}
```

## Response Format

### Successful Deletion
```json
{
  "success": true,
  "assetId": "02i5w00000ABC123",
  "deletedDate": 1640995200000,
  "message": "Asset successfully deleted",
  "relatedRecordsDeleted": 0
}
```

### Validation Response
```json
{
  "success": true,
  "assetId": "02i5w00000ABC123",
  "canDelete": true,
  "warnings": [],
  "relatedRecords": {
    "cases": 2,
    "contracts": 1,
    "opportunities": 0
  }
}
```

### Error Response
```json
{
  "success": false,
  "assetId": "02i5w00000ABC123",
  "error": "ENTITY_IS_DELETED",
  "message": "Asset has already been deleted or does not exist"
}
```

## Business Rules and Validation

### Deletion Restrictions
- Assets with active service contracts may require special handling
- Assets linked to open cases may need case resolution first
- Some organizations may require approval for asset deletion
- Historical assets may need to be preserved for compliance

### Related Record Impact
When deleting an asset, consider impact on:
- **Service Cases**: Open cases linked to the asset
- **Contracts**: Active service or maintenance contracts
- **Opportunities**: Related renewal or upgrade opportunities
- **Work Orders**: Pending maintenance or service work

## Common Use Cases

1. **Product Discontinuation**: Remove discontinued products from customer portfolios
2. **Account Cleanup**: Clean up assets when customer accounts are closed
3. **Data Quality**: Remove duplicate or incorrect asset records
4. **Compliance**: Delete assets per data retention policies
5. **System Maintenance**: Regular cleanup of obsolete records
6. **Migration Cleanup**: Remove temporary assets after data migration

## Best Practices

1. **Validate First**: Always use validateOnly to check for dependencies
2. **Document Reasons**: Provide clear deletion reasons for audit trails
3. **Check Dependencies**: Review related records before deletion
4. **Backup Data**: Ensure asset data is backed up if needed for compliance
5. **User Permissions**: Verify user has appropriate deletion permissions
6. **Cascade Carefully**: Use cascade deletion only when appropriate

## Error Handling

### Common Errors
- **ENTITY_IS_DELETED**: Asset already deleted or doesn't exist
- **INSUFFICIENT_ACCESS**: User lacks permission to delete asset
- **DELETE_FAILED**: Deletion failed due to system constraints
- **FIELD_CUSTOM_VALIDATION_EXCEPTION**: Custom validation rules prevent deletion
- **ENTITY_IS_LOCKED**: Asset is locked by another process

### Error Prevention
- Verify asset exists before attempting deletion
- Check user permissions for asset deletion
- Review custom validation rules that may prevent deletion
- Ensure asset is not locked by workflows or approvals

## Soft Delete vs Hard Delete

### Soft Delete (Recommended)
- Asset marked as deleted but data preserved
- Can be restored if needed
- Maintains referential integrity
- Supports compliance requirements

### Hard Delete
- Asset permanently removed from system
- Cannot be restored
- May break references in related records
- Use only when data must be permanently removed

## Audit and Compliance

### Audit Trail
Asset deletions should be logged with:
- Who deleted the asset
- When the deletion occurred
- Reason for deletion
- Related records affected

### Compliance Considerations
- Some industries require asset history preservation
- Regulatory requirements may prevent certain deletions
- Data retention policies may mandate soft delete only
- Customer contracts may require asset data preservation

## See Also

- [Asset Entity](pages/entities/Asset.md)
- [Update An Asset](pages/UpdateAnAsset.md)
- [Account Entity](pages/entities/Account.md)
- [User Entity](pages/entities/User.md)
