# Find Or Create Pricebook Entry

## Overview

The **Find Or Create Pricebook Entry** catalog request searches for a pricebook entry by product and pricebook in Salesforce. If a matching entry exists, it returns the existing entry. If not found, it creates a new pricebook entry with the specified unit price and returns the newly created entry.

## Use Cases

- **PO to Order Workflow**: Ensure products have pricing in the standard pricebook before creating orders
- **Price Management**: Automatically create pricing entries for new products
- **Multi-Pricebook Support**: Create entries in custom pricebooks for different customer segments

## Catalog Request Details

| Property | Value |
|----------|-------|
| **Request Name** | Find Or Create Pricebook Entry |
| **Request ID** | `localDomainRequest_c3d4e5f6-7a8b-9012-cdef-123456789012` |
| **Area** | Order Management |
| **Type** | QUERY_SYSTEM |

## Input Parameters

| **Parameter Name** | **Parameter Type** | **Mandatory** | **Example** | **Description** |
|--------------------|--------------------|---------------|-------------|-----------------|
| Pricebook2 Id | Text | Yes | "01s5w00000AbCdEAAX" | The Salesforce Pricebook ID |
| Product2 Id | Text | Yes | "01t5w00000XyZaAAAX" | The Salesforce Product ID |
| Unit Price | Number | Yes | 99.99 | The unit price for the product in this pricebook |

## Output Parameters

| **Parameter Name** | **Parameter Type** | **Description** |
|--------------------|--------------------|-----------------|
| Pricebook Entry | Entity(Pricebook Entry) | The found or created Pricebook Entry entity |

## Pricebook Entry Entity Fields

The returned Pricebook Entry entity includes the following fields:

| **Field Name** | **Type** | **Description** |
|----------------|----------|-----------------|
| Id | Text | Salesforce Pricebook Entry ID |
| Name | Text | Entry name |
| Pricebook Id | Text | ID of the associated pricebook |
| Product Id | Text | ID of the associated product |
| Product Code | Text | Product code (from Product2) |
| Unit Price | Currency | Price per unit |
| Is Active | Boolean | Whether the entry is active |
| Use Standard Price | Boolean | Whether to use the standard pricebook price |
| Created Date | Date | Creation timestamp |
| Last Modified Date | Date | Last modification timestamp |

## Usage Examples

### Example 1: Find Existing Pricebook Entry

**Input:**
```
Pricebook2 Id: "01s5w00000AbCdEAAX"
Product2 Id: "01t5w00000XyZaAAAX"
Unit Price: 99.99
```

**Output (if found):**
```json
{
  "Id": "01u5w00000AbCdEAAX",
  "Name": "Widget Pro",
  "Pricebook Id": "01s5w00000AbCdEAAX",
  "Product Id": "01t5w00000XyZaAAAX",
  "Product Code": "WDGT-001",
  "Unit Price": 99.99,
  "Is Active": true,
  "Use Standard Price": false,
  "Created Date": "2024-01-10T10:00:00Z",
  "Last Modified Date": "2024-01-15T14:30:00Z"
}
```

### Example 2: Create New Pricebook Entry

**Input:**
```
Pricebook2 Id: "01s5w00000AbCdEAAX"
Product2 Id: "01t5w00000NEW123AAZ"
Unit Price: 149.99
```

**Output (if not found - new entry created):**
```json
{
  "Id": "01u5w00000NEW456AAZ",
  "Name": "Super Widget XL",
  "Pricebook Id": "01s5w00000AbCdEAAX",
  "Product Id": "01t5w00000NEW123AAZ",
  "Product Code": "SW-XL-001",
  "Unit Price": 149.99,
  "Is Active": true,
  "Use Standard Price": false,
  "Created Date": "2024-02-27T10:00:00Z",
  "Last Modified Date": "2024-02-27T10:00:00Z"
}
```

## Error Handling

| **Error Scenario** | **Error Message** | **Resolution** |
|--------------------|-------------------|----------------|
| Pricebook2 Id is empty | "Pricebook2 Id is required." | Provide a valid Pricebook ID |
| Product2 Id is empty | "Product2 Id is required." | Provide a valid Product ID |
| Unit Price is invalid | "Unit Price must be a positive number." | Provide a valid positive number |
| Invalid Pricebook2 Id | "Pricebook not found." | Verify the Pricebook ID exists |
| Invalid Product2 Id | "Product not found." | Verify the Product ID exists |
| Pricebook entry exists with different price | "Pricebook entry exists with different unit price." | Use existing entry or update it separately |

## Complete Pricing Workflow

### Step 1: Find or Create Product
```
Find Or Create Product:
  - Product Name: "Widget Pro"
  - Product Code: "WDGT-001"

Output: Product.Id = "01t5w00000XyZaAAAX"
```

### Step 2: Get Standard Pricebook Id
Use Salesforce query or reference data to get the Standard Pricebook ID (or any custom pricebook).

### Step 3: Create Pricebook Entry
```
Find Or Create Pricebook Entry:
  - Pricebook2 Id: "01s5w00000AbCdEAAX"
  - Product2 Id: "01t5w00000XyZaAAAX" (from Step 1)
  - Unit Price: 99.99

Output: PricebookEntry.Id = "01u5w00000AbCdEAAX"
```

### Step 4: Create Order Item
```
Create Order Item:
  - Order Id: "8015w00000XyzAbAAZ"
  - Pricebook Entry Id: "01u5w00000AbCdEAAX" (from Step 3)
  - Quantity: 5
  - Unit Price: 99.99
```

## Related Requests

- [Find Or Create Product](pages/FindOrCreateProduct.md) - Create the product before adding pricing
- [Order Item](pages/entities/OrderItem.md) - Use the pricebook entry when creating order line items
- [Get Quote Details](pages/GetQuoteDetails.md) - View quotes that use pricebook entries

## Implementation Notes

- Every product must have a pricebook entry before it can be added to an Order
- The Standard Pricebook is typically used for most B2C scenarios
- Custom pricebooks can be created for different customer segments (Wholesale, VIP, etc.)
- If `Use Standard Price` is true, the price is inherited from the standard pricebook
- Pricebook entries are automatically marked as active when created
- Only one pricebook entry can exist per product per pricebook