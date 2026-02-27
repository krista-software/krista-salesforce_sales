# Order Entity

## Overview

The **Order** entity represents a customer order in Salesforce. Orders are used to track customer purchases, including billing and shipping information, order status, and associated order items (line items). This entity supports the full Purchase Order to Order creation workflow.

## Entity Details

| Property | Value |
|----------|-------|
| **Entity Name** | Order |
| **Entity ID** | `localDomainEntity_a1b2c3d4-5e6f-7a8b-9c0d-1e2f3a4b5c6d` |
| **Primary Key** | Id |
| **Support Store** | Yes (CRUD operations supported) |

## Entity Fields

### Core Fields

| **Field Name** | **Type** | **Required** | **Description** |
|----------------|----------|--------------|-----------------|
| Id | Text | Auto-generated | Salesforce Order ID (e.g., 8015w00000XyzAbAAZ) |
| Order Number | Text | Auto-generated | System-generated order number (e.g., 00000123) |
| Account Id | Text | Yes | ID of the associated Account |
| Effective Date | Date | Yes | Date when the order becomes effective |
| Status | Pick One | Yes | Order status: "Draft" or "Activated" |
| Description | Text | No | Order description or notes |

### Authorization Fields

| **Field Name** | **Type** | **Required** | **Description** |
|----------------|----------|--------------|-----------------|
| Customer Authorized By Id | Text | No | Contact ID who authorized on behalf of customer |
| Company Authorized By Id | Text | No | User ID who authorized on behalf of company |

### Type Field

| **Field Name** | **Type** | **Required** | **Description** |
|----------------|----------|--------------|-----------------|
| Type | PickOne | No | Order type (e.g., "Standard", "Renewal", "Replacement") |

### Billing Address Fields

| **Field Name** | **Type** | **Required** | **Description** |
|----------------|----------|--------------|-----------------|
| Billing Street | Street | No | Billing street address |
| Billing City | City | No | Billing city |
| Billing State | State | No | Billing state/province |
| Billing Postal Code | PostalCode | No | Billing postal/ZIP code |
| Billing Country | Country | No | Billing country |

### Shipping Address Fields

| **Field Name** | **Type** | **Required** | **Description** |
|----------------|----------|--------------|-----------------|
| Shipping Street | Street | No | Shipping street address |
| Shipping City | City | No | Shipping city |
| Shipping State | State | No | Shipping state/province |
| Shipping Postal Code | PostalCode | No | Shipping postal/ZIP code |
| Shipping Country | Country | No | Shipping country |

### Financial Fields

| **Field Name** | **Type** | **Required** | **Description** |
|----------------|----------|--------------|-----------------|
| Total Amount | Number | Auto-calculated | Total order amount (sum of order items) |

### System Fields

| **Field Name** | **Type** | **Required** | **Description** |
|----------------|----------|--------------|-----------------|
| Created Date | Date | Auto-generated | Timestamp when order was created |
| Created By Id | Text | Auto-generated | ID of user who created the order |
| Owner Id | Text | No | ID of the order owner |

### Custom Fields Support

| **Field Name** | **Type** | **Required** | **Description** |
|----------------|----------|--------------|-----------------|
| Custom Fields | FreeForm | No | Map of any Salesforce custom fields (ending with __c) |

## Supported Operations

### Search Orders
- Search by Order Number, Account Id, or Status
- Returns list of matching orders

### Get Order
- Retrieve a single order by ID
- Returns complete order details

### Create Order
- Create a new order with specified fields
- Required: Account Id, Effective Date, Status
- Optional: All address fields, custom fields

### Update Order
- Update existing order fields
- Can update status from "Draft" to "Activated"
- Can modify addresses and custom fields

### Delete Order
- Delete an order by ID
- Only draft orders can typically be deleted

## Status Values

| **Status** | **Description** |
|------------|-----------------|
| Draft | Initial status, order can be modified |
| Activated | Order is active and cannot be modified (requires certain fields) |

## Usage Examples

### Example 1: Create Order

**Input:**
```json
{
  "Account Id": "0015w00002inu5PAAP",
  "Effective Date": 1705276800000,
  "Status": "Draft",
  "Description": "Purchase Order #12345",
  "Billing Street": "123 Main St",
  "Billing City": "San Francisco",
  "Billing State": "CA",
  "Billing Postal Code": "94105",
  "Billing Country": "USA",
  "Shipping Street": "456 Oak Ave",
  "Shipping City": "Los Angeles",
  "Shipping State": "CA",
  "Shipping Postal Code": "90001",
  "Shipping Country": "USA",
  "Custom Fields": {
    "PO_Number__c": "PO-12345",
    "Customer_Ref__c": "CUST-001"
  }
}
```

**Output:**
```json
{
  "Id": "8015w00000XyzAbAAZ",
  "Order Number": "00000123",
  "Account Id": "0015w00002inu5PAAP",
  "Effective Date": 1705276800000,
  "Status": "Draft",
  "Description": "Purchase Order #12345",
  "Total Amount": 0.0,
  "Billing City": "San Francisco",
  "Billing State": "CA",
  "Shipping City": "Los Angeles",
  "Shipping State": "CA",
  "Created Date": 1705276800000,
  "Custom Fields": {
    "PO_Number__c": "PO-12345",
    "Customer_Ref__c": "CUST-001"
  }
}
```

### Example 2: Update Order Status

**Input:**
```json
{
  "Id": "8015w00000XyzAbAAZ",
  "Status": "Activated"
}
```

**Output:**
```json
{
  "Id": "8015w00000XyzAbAAZ",
  "Status": "Activated",
  "Last Modified Date": 1705363200000
}
```

## Error Handling

| **Error Scenario** | **Error Message** | **Resolution** |
|--------------------|-------------------|----------------|
| Account Id missing | "Account Id is required to create an order." | Provide a valid Account ID |
| Invalid Account Id | "Account not found." | Verify the Account ID exists |
| Effective Date missing | "Effective Date is required." | Provide an effective date |
| Status missing | "Status is required." | Specify Draft or Activated |
| Invalid status transition | "Cannot deactivate an activated order." | Orders can only go from Draft to Activated |
| Activated order modification | "Cannot modify an activated order." | Activate order only after all details are final |

## Relationships

- **Account** (Many-to-One) - Order belongs to one Account
- **Order Items** (One-to-Many) - Order can have multiple Order Items
- **Files** (via ContentDocumentLink) - Files can be attached to Orders

## Related Entities

- [Account](pages/entities/Account.md) - The customer account
- [Order Item](pages/entities/OrderItem.md) - Line items in the order
- [Product](pages/entities/Product.md) - Products referenced in order items

## Implementation Notes

- Total Amount is automatically calculated from associated Order Items
- An order must be in "Draft" status to add or modify Order Items
- Once activated, orders typically cannot be modified (Salesforce standard behavior)
- Custom fields with __c suffix are supported for all custom Salesforce fields
- Address fields follow standard Salesforce address structure
- Order Number is auto-generated by Salesforce and cannot be modified
