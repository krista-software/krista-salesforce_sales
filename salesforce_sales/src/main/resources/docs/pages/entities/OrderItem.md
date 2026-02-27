# Order Item Entity

## Overview

The **Order Item** entity (also known as Order Product) represents a line item in a customer order. Each Order Item represents one product on an order, including quantity, pricing, and other line-item details. This entity is essential for the Purchase Order to Order creation workflow.

## Entity Details

| Property | Value |
|----------|-------|
| **Entity Name** | Order Item |
| **Alternative Name** | Order Product |
| **Entity ID** | `localDomainEntity_b2c3d4e5-6f7a-8b9c-0d1e-2f3a4b5c6d7e` |
| **Primary Key** | Id |
| **Support Store** | Yes (CRUD operations supported) |

## Entity Fields

### Core Fields

| **Field Name** | **Type** | **Required** | **Description** |
|----------------|----------|--------------|-----------------|
| Id | Text | Auto-generated | Salesforce Order Item ID (e.g., 8025w00000AbcDeAAZ) |
| Order Id | Text | Yes | ID of the parent Order |
| Pricebook Entry Id | Text | Yes* | ID of the Pricebook Entry (required for creation) |
| Product Id | Text | Auto-populated | ID of the Product (from Pricebook Entry) |
| Order Item Number | Text | Auto-generated | Line item number (e.g., 0001) |

### Quantity and Pricing Fields

| **Field Name** | **Type** | **Required** | **Description** |
|----------------|----------|--------------|-----------------|
| Quantity | Number | Yes | Quantity of the product |
| Unit Price | Number | Yes | Price per unit |
| List Price | Number | Auto-populated | List price from pricebook |
| Total Price | Number | Auto-calculated | Total line amount (Quantity × Unit Price) |

### Description Fields

| **Field Name** | **Type** | **Required** | **Description** |
|----------------|----------|--------------|-----------------|
| Description | Text | No | Line item description |

### Service Date Fields

| **Field Name** | **Type** | **Required** | **Description** |
|----------------|----------|--------------|-----------------|
| Service Date | Date | No | Date when service begins |
| End Date | Date | No | Date when service ends |

### System Fields

| **Field Name** | **Type** | **Required** | **Description** |
|----------------|----------|--------------|-----------------|
| Created Date | Date | Auto-generated | Timestamp when item was created |
| Created By Id | Text | Auto-generated | ID of user who created the item |
| Last Modified Date | Date | Auto-generated | Last modification timestamp |
| Last Modified By Id | Text | Auto-generated | ID of user who last modified |

### Custom Fields Support

| **Field Name** | **Type** | **Required** | **Description** |
|----------------|----------|--------------|-----------------|
| Custom Fields | FreeForm | No | Map of any Salesforce custom fields (ending with __c) |

## Supported Operations

### Search Order Items
- Search by Order Id or Product Id
- Returns list of matching order items

### Get Order Item
- Retrieve a single order item by ID
- Returns complete item details

### Create Order Item
- Add a product to an order
- Required: Order Id, Pricebook Entry Id, Quantity, Unit Price
- The order must be in "Draft" status

### Update Order Item
- Modify quantity, unit price, description, dates
- Cannot modify if parent order is activated

### Delete Order Item
- Remove a line item from an order
- Only allowed if parent order is in "Draft" status

## Usage Examples

### Example 1: Create Order Item

**Input:**
```json
{
  "Order Id": "8015w00000XyzAbAAZ",
  "Pricebook Entry Id": "01u5w00000AbCdEAAX",
  "Quantity": 5.0,
  "Unit Price": 99.99,
  "Description": "Widget Pro - Premium Model",
  "Service Date": 1707955200000,
  "End Date": 1710720000000,
  "Custom Fields": {
    "Discount_Percent__c": 10,
    "Line_Notes__c": "Rush order"
  }
}
```

**Output:**
```json
{
  "Id": "8025w00000AbcDeAAZ",
  "Order Id": "8015w00000XyzAbAAZ",
  "Pricebook Entry Id": "01u5w00000AbCdEAAX",
  "Product Id": "01t5w00000XyZaAAAX",
  "Order Item Number": "0001",
  "Quantity": 5.0,
  "Unit Price": 99.99,
  "List Price": 99.99,
  "Total Price": 499.95,
  "Description": "Widget Pro - Premium Model",
  "Service Date": 1707955200000,
  "End Date": 1710720000000,
  "Created Date": 1705276800000,
  "Custom Fields": {
    "Discount_Percent__c": 10,
    "Line_Notes__c": "Rush order"
  }
}
```

### Example 2: Update Order Item Quantity

**Input:**
```json
{
  "Id": "8025w00000AbcDeAAZ",
  "Quantity": 10.0,
  "Unit Price": 89.99
}
```

**Output:**
```json
{
  "Id": "8025w00000AbcDeAAZ",
  "Quantity": 10.0,
  "Unit Price": 89.99,
  "Total Price": 899.90,
  "Last Modified Date": 1705363200000
}
```

## Error Handling

| **Error Scenario** | **Error Message** | **Resolution** |
|--------------------|-------------------|----------------|
| Order Id missing | "Order Id is required." | Provide a valid Order ID |
| Pricebook Entry Id missing | "Pricebook Entry Id is required for creation." | Provide a valid Pricebook Entry ID |
| Quantity missing or zero | "Quantity must be greater than zero." | Provide a positive quantity |
| Unit Price missing | "Unit Price is required." | Provide a unit price |
| Invalid Order Id | "Order not found." | Verify the Order ID exists |
| Invalid Pricebook Entry Id | "Pricebook Entry not found." | Verify the Pricebook Entry ID exists |
| Order is activated | "Cannot modify order items on an activated order." | Order must be in Draft status |
| Product not active | "Product is not active." | Activate the product in Salesforce |

## Calculated Fields

### Total Price Calculation
```
Total Price = Quantity × Unit Price
```

**Example:**
- Quantity: 5
- Unit Price: 99.99
- Total Price: 499.95

## Complete PO to Order Item Workflow

### Step 1: Create or Find Account
```
Find Or Create Account:
  - Account Name: "Acme Corporation"
Output: Account.Id = "0015w00002inu5PAAP"
```

### Step 2: Create Order
```
Create Order:
  - Account Id: "0015w00002inu5PAAP"
  - Effective Date: 1705276800000
  - Status: "Draft"
Output: Order.Id = "8015w00000XyzAbAAZ"
```

### Step 3: Find or Create Product
```
Find Or Create Product:
  - Product Name: "Widget Pro"
  - Product Code: "WDGT-001"
Output: Product.Id = "01t5w00000XyZaAAAX"
```

### Step 4: Create Pricebook Entry
```
Find Or Create Pricebook Entry:
  - Pricebook2 Id: "01s5w00000AbCdEAAX"
  - Product2 Id: "01t5w00000XyZaAAAX"
  - Unit Price: 99.99
Output: PricebookEntry.Id = "01u5w00000AbCdEAAX"
```

### Step 5: Create Order Item
```
Create Order Item:
  - Order Id: "8015w00000XyzAbAAZ"
  - Pricebook Entry Id: "01u5w00000AbCdEAAX"
  - Quantity: 5
  - Unit Price: 99.99
  - Description: "Widget Pro"
Output: OrderItem.Id = "8025w00000AbcDeAAZ", Total Price = 499.95
```

### Step 6: Activate Order
```
Update Order:
  - Id: "8015w00000XyzAbAAZ"
  - Status: "Activated"
```

## Relationships

- **Order** (Many-to-One) - Order Item belongs to one Order
- **Pricebook Entry** (Many-to-One) - References pricing information
- **Product** (Many-to-One) - References the product being ordered

## Related Entities

- [Order](pages/entities/Order.md) - The parent order
- [Product](pages/entities/Product.md) - The product being ordered
- [Pricebook Entry](pages/entities/PricebookEntry.md) - Pricing information

## Implementation Notes

- Total Price is automatically calculated (Quantity × Unit Price)
- Order Item Number is auto-generated by Salesforce
- Product Id is automatically populated from the Pricebook Entry
- The parent Order must be in "Draft" status to create or modify Order Items
- Custom fields with __c suffix are fully supported
- List Price comes from the Pricebook Entry, but Unit Price can be overridden
- Service Date and End Date are optional but useful for subscription products
