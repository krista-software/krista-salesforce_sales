# Delete An Asset

## Overview

The Delete An Asset catalog request removes an asset record from Salesforce Sales. This request permanently deletes the specified asset from your Salesforce organization.

## Request Details

- **Area**: Assets Management
- **Type**: CHANGE_SYSTEM
- **Retry Support**: ❌ No
- **Authentication**: Supports both User Authentication and Service Account modes

## Input Parameters

| Parameter Name | Type | Required | Description | Example |
|----------------|------|----------|-------------|---------|
| Id | Text | Yes | Salesforce ID of the asset to delete | "02i5w00000ABC123" |

## Output Parameters

| Parameter Name | Type | Description |
|----------------|------|-------------|
| Status | Switch (Boolean) | Returns `true` if deletion was successful, `false` if it failed |

## Validation Rules

| Validation | Error Message | Resolution |
|------------|---------------|------------|
| ID Required | "Asset ID is required. Please provide the Salesforce ID of the asset you want to delete." | Provide a valid Salesforce asset ID in the Id field |
| ID Format | Asset ID must be a valid Salesforce ID format | Ensure the ID is a valid 15 or 18-character Salesforce ID |

## Error Handling

### Input Errors (INPUT_ERROR)
**Cause**: Missing or invalid asset ID
**Error Message**: "Asset ID is required. Please provide the Salesforce ID of the asset you want to delete."
**Resolution**:
1. Ensure the Id parameter is provided
2. Verify the ID is not null or empty
3. Check that the ID is a valid Salesforce asset ID format (15 or 18 characters)

### Logic Errors (LOGIC_ERROR)
**Cause**: Asset ID does not exist or is not accessible
**Error Message**: "Provided asset ID does not exist or is not accessible"
**Resolution**:
1. Verify the asset ID exists in Salesforce
2. Check that the asset hasn't already been deleted
3. Ensure the authenticated user has access to the asset
4. Confirm the ID is for an Asset object (not another object type)

### System Errors (SYSTEM_ERROR)
**Cause**: Salesforce API errors or connection issues
**Resolution**:
1. Check network connectivity to Salesforce
2. Verify authentication credentials are valid
3. Ensure Salesforce API limits haven't been exceeded
4. Check Salesforce service status

### Authorization Errors
**Cause**: Insufficient permissions to delete assets
**Resolution**:
1. Verify the authenticated user has delete permission on Asset objects
2. Check sharing rules allow deletion of the specific asset
3. Ensure no custom validation rules prevent deletion
4. Contact your Salesforce administrator if permissions are needed

## Usage Examples

### Example 1: Delete an Asset
**Input**:
```
Id: "02i5w00000ABC123"
```

**Output**: `true`

**Result**: Asset successfully deleted from Salesforce

### Example 2: Delete Non-Existent Asset
**Input**:
```
Id: "02i5w00000XYZ999"
```

**Output**: `false`

**Result**: Deletion failed because asset ID does not exist

### Example 3: Missing Asset ID
**Input**:
```
Id: (empty)
```

**Output**: Error thrown

**Error**: "Asset ID is required. Please provide the Salesforce ID of the asset you want to delete."

## Business Rules

1. **Permanent Deletion**: This operation permanently deletes the asset from Salesforce
2. **No Undo**: Deleted assets cannot be restored through this extension
3. **Related Records**: Deletion may affect related records depending on Salesforce configuration
4. **Permissions Required**: User must have delete permission on Asset objects
5. **Validation Rules**: Custom Salesforce validation rules may prevent deletion

## Limitations

1. **No Soft Delete**: This operation performs a hard delete (permanent removal)
2. **No Cascade Options**: Related records are handled according to Salesforce configuration
3. **No Validation Mode**: Cannot preview deletion impact before committing
4. **Single Asset**: Only one asset can be deleted per request
5. **No Bulk Delete**: Cannot delete multiple assets in a single request

## Best Practices

### 1. Verify Before Deleting
Always verify the asset ID is correct before deletion to avoid accidental data loss.

### 2. Check Permissions
Ensure the authenticated user has appropriate delete permissions before attempting deletion.

### 3. Handle Errors Gracefully
Implement proper error handling to manage failed deletions and provide user feedback.

### 4. Audit Trail
Maintain your own audit log of deletions for compliance and troubleshooting purposes.

### 5. Backup Important Data
Consider exporting asset data before deletion if it may be needed for historical reference.

## Common Use Cases

### 1. Data Cleanup
```
Scenario: Remove duplicate or incorrect asset records
Action: Delete the duplicate asset by ID
Result: Clean, accurate asset data in Salesforce
```

### 2. Account Closure
```
Scenario: Customer account is closed and assets need removal
Action: Delete all assets associated with the closed account
Result: Removed obsolete asset records
```

### 3. Product Discontinuation
```
Scenario: Product line discontinued and assets need cleanup
Action: Delete assets for discontinued products
Result: Updated asset inventory reflecting current products
```

## Related Catalog Requests

- [Update An Asset](pages/UpdateAnAsset.md) - Modify asset properties
- [Asset Entity](pages/entities/Asset.md) - Asset entity field definitions

## Technical Implementation

### Helper Class
- **Class**: AssetStore
- **Package**: app.krista.extensions.crm.sales.salesforce_sales.catalog.stores
- **Method**: deleteAssetById()

### Validation
- ID validation in AssetsManagementArea
- Existence check in AssetDaoImpl

### Telemetry Metrics
- ASSET_DELETION_SUCCESS: Successful asset deletion
- ASSET_DELETION_FAILURE: Failed asset deletion attempt

## Troubleshooting

### Asset Not Found
**Cause**: The provided asset ID does not exist in Salesforce
**Solution**:
1. Verify the asset ID is correct
2. Check if the asset was already deleted
3. Ensure you're searching in the correct Salesforce org (production vs sandbox)

### Permission Denied
**Cause**: User lacks delete permission on Asset objects
**Solution**:
1. Contact your Salesforce administrator
2. Request delete permission on Asset objects
3. Verify sharing rules allow access to the asset

### Deletion Failed
**Cause**: Salesforce validation rules or triggers prevent deletion
**Solution**:
1. Check for custom validation rules on Asset object
2. Review any before-delete triggers
3. Ensure no required related records prevent deletion
4. Contact your Salesforce administrator for assistance

## See Also

- [Extension Configuration](pages/ExtensionConfiguration.md)
- [Authentication](pages/Authentication.md)
- [Asset Entity](pages/entities/Asset.md)
