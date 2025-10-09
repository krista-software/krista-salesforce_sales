# Retrieve Custom Fields For Given Object

## Overview

This catalog request retrieves custom field information for a specific Salesforce object. It allows you to get details about custom fields that have been added to standard or custom objects in your Salesforce org.

## Request Details

- **Area**: Content & Custom Fields
- **Input Parameters**: Object ID, Object Type, and Custom Fields required
- **Authentication**: Required

## Input Parameters

| Parameter Name | Parameter Type | Mandatory | Description | Example |
|----------------|----------------|-----------|-------------|---------|
| Object Id | Text | Yes | The ID of the specific object record | 123456 |
| Object Type | Pick List | Yes | The type of Salesforce object | Account |
| Custom Fields | List<String> | Yes | List of custom field API names to retrieve | Competition__c |

## Object Type Values

Common object types include:
- **Account** - Customer accounts
- **Opportunity** - Sales opportunities
- **Lead** - Potential customers
- **Contact** - Individual contacts
- **Case** - Support cases
- **Custom Objects** - Organization-specific custom objects

## Output Parameters

| Parameter Name | Parameter Type | Description |
|----------------|----------------|-------------|
| Field Details | FreeForm | Detailed information about the requested custom fields |

## Usage Example

### Request
```json
{
  "catalogRequest": "Retrieve Custom Fields For Given Object",
  "objectId": "0015w00000ABC123",
  "objectType": "Account",
  "customFields": ["Competition__c", "Industry_Segment__c", "Annual_Contract_Value__c"]
}
```

### Response
```json
{
  "fieldDetails": {
    "Competition__c": {
      "label": "Primary Competition",
      "type": "Picklist",
      "value": "Microsoft",
      "options": ["Microsoft", "Oracle", "SAP", "Other"]
    },
    "Industry_Segment__c": {
      "label": "Industry Segment",
      "type": "Text",
      "value": "Healthcare",
      "maxLength": 255
    },
    "Annual_Contract_Value__c": {
      "label": "Annual Contract Value",
      "type": "Currency",
      "value": 250000,
      "precision": 2
    }
  }
}
```

## Custom Field Types

Common custom field types include:
- **Text** - Single line text fields
- **Textarea** - Multi-line text fields
- **Picklist** - Single-select dropdown
- **Multi-Picklist** - Multi-select dropdown
- **Number** - Numeric fields
- **Currency** - Currency fields
- **Date** - Date fields
- **DateTime** - Date and time fields
- **Checkbox** - Boolean fields
- **Formula** - Calculated fields
- **Lookup** - References to other records

## Business Use Cases

1. **Data Integration**: Retrieve custom field data for external systems
2. **Reporting**: Include custom fields in reports and analytics
3. **Data Migration**: Extract custom field information for migrations
4. **API Development**: Build applications that work with custom fields
5. **Configuration Management**: Document custom field configurations

## Error Handling

### Invalid Object ID
```json
{
  "fieldDetails": {
    "error": "Object not found",
    "message": "No record found with the specified Object ID"
  }
}
```

### Invalid Custom Field
```json
{
  "fieldDetails": {
    "Competition__c": {
      "error": "Field not found",
      "message": "Custom field 'Competition__c' does not exist on Account object"
    }
  }
}
```

## Related Requests

- [Account Change Alert](pages/AccountChangeAlert.md) - Monitor account changes including custom fields
- [Process Query Request](pages/ProcessQueryRequest.md) - Query records with custom fields

## Best Practices

1. **Field API Names**: Use the API name (with __c suffix) for custom fields
2. **Object Validation**: Ensure the Object Type matches the Object ID
3. **Field Existence**: Verify custom fields exist before requesting them
4. **Permission Checks**: Ensure user has access to the requested fields
5. **Error Handling**: Handle cases where fields don't exist or aren't accessible

## See Also

- [Account Entity](pages/entities/Account.md)
- [Opportunity Entity](pages/entities/Opportunity.md)
- [Lead Entity](pages/entities/Lead.md)
