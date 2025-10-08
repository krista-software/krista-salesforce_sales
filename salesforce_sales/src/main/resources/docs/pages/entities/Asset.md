# Asset Entity

## Overview

The Asset entity represents products or services that have been purchased by customers in Salesforce Sales. Assets track what customers own, helping with support, renewals, upselling, and service delivery. They provide a complete view of the customer's product portfolio and purchase history.

## Entity Details

- **Entity Name**: Asset
- **Primary Key**: Id
- **Searchable Fields**: Id, Name
- **Display Field**: Name
- **Store Support**: No

## Fields

### Core Fields

| Field Name | Type | Required | Description |
|------------|------|----------|-------------|
| Id | Text | No | Unique identifier for the Asset record (searchable) |
| Name | Text | Yes | Name of the asset (searchable, used for display) |
| Owner Id | Text | No | ID of the user who owns this asset |

### Relationship Fields

| Field Name | Type | Required | Description |
|------------|------|----------|-------------|
| Account Id | Text | No | ID of the account that owns this asset |
| Contact Id | Text | No | ID of the primary contact for this asset |
| Root Asset Id | Text | No | ID of the parent asset (for hierarchical assets) |

### Asset Classification

| Field Name | Type | Required | Description |
|------------|------|----------|-------------|
| Is Competitor Product | Switch | No | Whether this asset is a competitor's product |
| Is Deleted | Switch | No | Whether this asset has been marked as deleted |

### System Fields

| Field Name | Type | Required | Description |
|------------|------|----------|-------------|
| Created Date | Date | No | Date and time when the asset was created |
| Created By Id | Text | No | ID of the user who created this asset |
| Last Modified Date | Date | No | Date and time of last modification |

## Asset Hierarchy

Assets can be organized in a hierarchical structure using the **Root Asset Id** field:

- **Parent Assets**: Top-level products or services
- **Child Assets**: Components, add-ons, or related items
- **Asset Families**: Grouped related assets under a common parent

## Usage in Catalog Requests

The Asset entity is used in the following catalog requests:

- **Delete An Asset**: Remove an asset record
- **Update An Asset**: Modify asset information
- **Asset-related reporting**: Track customer product portfolios

## Relationships

- **Many-to-One with Account**: Each asset belongs to one account
- **Many-to-One with Contact**: Each asset has one primary contact
- **Many-to-One with User (Owner)**: Each asset has one owner
- **Self-Referencing**: Assets can have parent-child relationships

## Asset Lifecycle

### Creation
1. **Purchase**: Customer buys a product/service
2. **Asset Creation**: Asset record created in Salesforce
3. **Account Association**: Linked to customer account
4. **Contact Assignment**: Primary contact designated

### Management
1. **Tracking**: Monitor asset status and usage
2. **Support**: Link support cases to specific assets
3. **Renewals**: Track renewal dates and opportunities
4. **Upgrades**: Manage product upgrades and changes

### Retirement
1. **End of Life**: Product reaches end of support
2. **Replacement**: Asset replaced with newer version
3. **Deletion**: Asset marked as deleted (soft delete)

## Validation Rules

- **Name**: Required field, cannot be empty
- **Account Id**: Must reference a valid account if provided
- **Contact Id**: Must reference a valid contact if provided
- **Root Asset Id**: Must reference a valid asset if provided (no circular references)

## Best Practices

1. **Naming Convention**: Use consistent naming that includes product and version
2. **Account Association**: Always link assets to the correct account
3. **Hierarchy Management**: Use parent-child relationships for complex products
4. **Competitor Tracking**: Mark competitor products for competitive analysis
5. **Regular Updates**: Keep asset information current and accurate
6. **Soft Deletion**: Use "Is Deleted" flag rather than hard deletion

## Common Use Cases

1. **Customer Portfolio Management**: Track what customers own
2. **Support Case Management**: Link support issues to specific assets
3. **Renewal Management**: Identify assets due for renewal
4. **Upsell/Cross-sell**: Find opportunities based on current assets
5. **Competitive Analysis**: Track competitor product penetration
6. **Service Delivery**: Manage service delivery for specific assets

## Asset Types (Examples)

### Software Assets
- Software licenses
- Subscription services
- Support contracts
- Training packages

### Hardware Assets
- Physical equipment
- Devices and components
- Maintenance contracts
- Warranty coverage

### Service Assets
- Professional services
- Consulting engagements
- Managed services
- Support agreements

## Example Usage

```json
{
  "id": "02i5w00000ABC123",
  "name": "Salesforce Sales Cloud Enterprise - 100 Users",
  "accountId": "0015w00000ABC123",
  "contactId": "0035w00000ABC123",
  "ownerId": "0055w00000ABC123",
  "isCompetitorProduct": false,
  "isDeleted": false,
  "createdDate": 1640995200000,
  "rootAssetId": null
}
```

### Hierarchical Asset Example

```json
{
  "parentAsset": {
    "id": "02i5w00000PARENT1",
    "name": "Enterprise Software Suite",
    "accountId": "0015w00000ABC123"
  },
  "childAssets": [
    {
      "id": "02i5w00000CHILD1",
      "name": "CRM Module",
      "rootAssetId": "02i5w00000PARENT1"
    },
    {
      "id": "02i5w00000CHILD2", 
      "name": "Analytics Module",
      "rootAssetId": "02i5w00000PARENT1"
    }
  ]
}
```

## See Also

- [Account Entity](pages/entities/Account.md)
- [User Entity](pages/entities/User.md)
- [Delete An Asset](pages/DeleteAnAsset.md)
- [Update An Asset](pages/UpdateAnAsset.md)
