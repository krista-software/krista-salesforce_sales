# Find Or Create Product

## Overview

The **Find Or Create Product** catalog request searches for a product by its product code in Salesforce. If a product with the given code exists, it returns the existing product. If not found, it creates a new product with the given name and code, then returns the newly created product.

## Use Cases

- **PO to Order Workflow**: Automatically find or create products when processing purchase orders
- **Product Catalog Management**: Ensure products exist before creating Orders and Order Items
- **Integration Scenarios**: Sync products from external systems to Salesforce

## Catalog Request Details

| Property | Value |
|----------|-------|
| **Request Name** | Find Or Create Product |
| **Request ID** | `localDomainRequest_b2c3d4e5-6f7a-8901-bcde-f12345678901` |
| **Area** | Order Management |
| **Type** | QUERY_SYSTEM |

## Input Parameters

| **Parameter Name** | **Parameter Type** | **Mandatory** | **Example** | **Description** |
|--------------------|--------------------|---------------|-------------|-----------------|
| Product Name | Text | Yes | "Widget Pro" | The name of the product |
| Product Code | Text | Yes | "WDGT-001" | The unique product code/SKU |

## Output Parameters

| **Parameter Name** | **Parameter Type** | **Description** |
|--------------------|--------------------|-----------------|
| Product | Entity(Product) | The found or created Product entity |

## Product Entity Fields

The returned Product entity includes the following fields:

| **Field Name** | **Type** | **Description** |
|----------------|----------|-----------------|
| Id | Text | Salesforce Product ID |
| Name | Text | Product name |
| Product Code | Text | Unique product code/SKU |
| Description | Text | Product description |
| Is Active | Boolean | Whether the product is active |
| Family | Text | Product family/category |
| Created Date | Date | Creation timestamp |
| Created By Id | Text | ID of user who created |
| Last Modified Date | Date | Last modification timestamp |
| Last Modified By Id | Text | ID of user who last modified |

## Usage Examples

### Example 1: Find Existing Product

**Input:**
```
Product Name: "Widget Pro"
Product Code: "WDGT-001"
```

**Output (if found):**
```json
{
  "Id": "01t5w00000XyZaAAAX",
  "Name": "Widget Pro",
  "Product Code": "WDGT-001",
  "Description": "High-quality professional widget",
  "Is Active": true,
  "Family": "Hardware",
  "Created Date": "2024-01-10T10:00:00Z",
  "Last Modified Date": "2024-01-15T14:30:00Z"
}
```

### Example 2: Create New Product

**Input:**
```
Product Name: "Super Widget XL"
Product Code: "SW-XL-001"
```

**Output (if not found - new product created):**
```json
{
  "Id": "01t5w00000NEW123AAZ",
  "Name": "Super Widget XL",
  "Product Code": "SW-XL-001",
  "Description": null,
  "Is Active": true,
  "Family": null,
  "Created Date": "2024-02-27T10:00:00Z",
  "Last Modified Date": "2024-02-27T10:00:00Z"
}
```

## Error Handling

| **Error Scenario** | **Error Message** | **Resolution** |
|--------------------|-------------------|----------------|
| Product Name is empty | "Product Name is required." | Provide a valid product name |
| Product Code is empty | "Product Code is required." | Provide a valid product code |
| Salesforce API error | "Failed to search/create product: [error details]" | Check Salesforce connectivity and permissions |

## Complete Product Workflow

### Step 1: Find or Create Product
```
Find Or Create Product:
  - Product Name: "Widget Pro"
  - Product Code: "WDGT-001"

Output: Product.Id = "01t5w00000XyZaAAAX"
```

### Step 2: Create Pricebook Entry
```
Find Or Create Pricebook Entry:
  - Pricebook2 Id: "01s5w00000AbCdEAAX"
  - Product2 Id: "01t5w00000XyZaAAAX" (from Step 1)
  - Unit Price: 99.99

Output: PricebookEntry.Id = "01u5w00000AbCdEAAX"
```

### Step 3: Create Order Item
```
Create Order Item:
  - Order Id: "8015w00000XyzAbAAZ"
  - Pricebook Entry Id: "01u5w00000AbCdEAAX" (from Step 2)
  - Quantity: 5
  - Unit Price: 99.99
```

## Related Requests

- [Find Or Create Pricebook Entry](pages/FindOrCreatePricebookEntry.md) - Create pricing for the product
- [Find Or Create Order](pages/Order.md) - Create orders using the product
- [Create Order Item](pages/OrderItem.md) - Add products as line items to orders

## Implementation Notes

- Product Code must be unique in Salesforce
- The search is case-insensitive on Product Code
- New products are created with `Is Active = true` by default
- Product Family can be set later using Update Product entity operation
- Standard Pricebook entry is automatically created when a product is created