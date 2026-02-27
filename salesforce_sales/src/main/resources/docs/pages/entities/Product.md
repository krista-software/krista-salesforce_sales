# Product Entity

## Overview

The **Product** entity (represented as Product2 in Salesforce) represents a product or service that your company sells. Products are the foundation of the sales process and are used in Opportunities, Quotes, Orders, and Pricebooks. This entity is essential for the Purchase Order to Order creation workflow.

## Entity Details

| Property | Value |
|----------|-------|
| **Entity Name** | Product |
| **Salesforce API Name** | Product2 |
| **Entity ID** | `localDomainEntity_c3d4e5f6-7a8b-9c0d-1e2f-3a4b5c6d7e8f` |
| **Primary Key** | Id |
| **Support Store** | Yes (CRUD operations supported) |

## Entity Fields

### Core Fields

| **Field Name** | **Type** | **Required** | **Description** |
|----------------|----------|--------------|-----------------|
| Id | Text | Auto-generated | Salesforce Product ID (e.g., 01t5w00000XyZaAAAX) |
| Name | Text | Yes | Product name |
| Product Code | Text | Yes | Unique product code/SKU |

### Description Fields

| **Field Name** | **Type** | **Required** | **Description** |
|----------------|----------|--------------|-----------------|
| Description | Text | No | Detailed product description |

### Status and Classification Fields

| **Field Name** | **Type** | **Required** | **Description** |
|----------------|----------|--------------|-----------------|
| Is Active | Boolean | No | Whether the product can be used in orders (default: true) |
| Family | Text | No | Product family/category (e.g., "Hardware", "Software", "Services") |

### System Fields

| **Field Name** | **Type** | **Required** | **Description** |
|----------------|----------|--------------|-----------------|
| Created Date | Date | Auto-generated | Timestamp when product was created |
| Created By Id | Text | Auto-generated | ID of user who created the product |
| Last Modified Date | Date | Auto-generated | Last modification timestamp |
| Last Modified By Id | Text | Auto-generated | ID of user who last modified |

## Supported Operations

### Search Products
- Search by Name, Product Code, or Family
- Supports partial matching on text fields
- Returns list of matching products

### Get Product
- Retrieve a single product by ID
- Returns complete product details

### Create Product
- Create a new product with specified fields
- Required: Name, Product Code
- Optional: Description, Family, Is Active

### Update Product
- Update existing product fields
- Can activate/deactivate products
- Can update description and family

### Delete Product
- Delete a product by ID
- Only products not used in transactions can typically be deleted

## Usage Examples

### Example 1: Create Product

**Input:**
```json
{
  "Name": "Widget Pro",
  "Product Code": "WDGT-001",
  "Description": "High-quality professional widget for industrial applications",
  "Is Active": true,
  "Family": "Hardware"
}
```

**Output:**
```json
{
  "Id": "01t5w00000XyZaAAAX",
  "Name": "Widget Pro",
  "Product Code": "WDGT-001",
  "Description": "High-quality professional widget for industrial applications",
  "Is Active": true,
  "Family": "Hardware",
  "Created Date": 1705276800000,
  "Created By Id": "0055w00000D9a8iAAC",
  "Last Modified Date": 1705276800000,
  "Last Modified By Id": "0055w00000D9a8iAAC"
}
```

### Example 2: Update Product

**Input:**
```json
{
  "Id": "01t5w00000XyZaAAAX",
  "Description": "Updated description with new features",
  "Family": "Industrial Hardware"
}
```

**Output:**
```json
{
  "Id": "01t5w00000XyZaAAAX",
  "Name": "Widget Pro",
  "Product Code": "WDGT-001",
  "Description": "Updated description with new features",
  "Family": "Industrial Hardware",
  "Last Modified Date": 1705363200000
}
```

### Example 3: Deactivate Product

**Input:**
```json
{
  "Id": "01t5w00000XyZaAAAX",
  "Is Active": false
}
```

**Output:**
```json
{
  "Id": "01t5w00000XyZaAAAX",
  "Is Active": false,
  "Last Modified Date": 1705363200000
}
```

## Error Handling

| **Error Scenario** | **Error Message** | **Resolution** |
|--------------------|-------------------|----------------|
| Name is missing | "Product Name is required." | Provide a product name |
| Product Code is missing | "Product Code is required." | Provide a unique product code |
| Duplicate Product Code | "Product Code already exists." | Use a unique product code |
| Product not found | "Product not found." | Verify the Product ID exists |
| Product in use | "Cannot delete product in use." | Remove from all opportunities/quotes/orders first |

## Common Product Families

| **Family** | **Use Case** |
|------------|--------------|
| Hardware | Physical products, equipment |
| Software | Licensed software products |
| Services | Professional services, consulting |
| Subscriptions | Recurring subscription products |
| Consumables | Disposable items, supplies |

## Complete Product Setup Workflow

### Step 1: Create Product
```
Create Product:
  - Name: "Widget Pro"
  - Product Code: "WDGT-001"
  - Description: "High-quality widget"
  - Family: "Hardware"
  - Is Active: true
Output: Product.Id = "01t5w00000XyZaAAAX"
```

### Step 2: Get Standard Pricebook Id
Query Salesforce for the Standard Pricebook ID or use a known ID.

### Step 3: Create Standard Pricebook Entry
```
Find Or Create Pricebook Entry:
  - Pricebook2 Id: "01s5w00000AbCdEAAX" (Standard Pricebook)
  - Product2 Id: "01t5w00000XyZaAAAX"
  - Unit Price: 99.99
Output: PricebookEntry.Id = "01u5w00000AbCdEAAX"
```

### Step 4: Create Custom Pricebook Entry (Optional)
```
Find Or Create Pricebook Entry:
  - Pricebook2 Id: "01s5w00000Wholesale" (Wholesale Pricebook)
  - Product2 Id: "01t5w00000XyZaAAAX"
  - Unit Price: 79.99 (discounted price)
Output: PricebookEntry.Id = "01u5w00000WholesaleEntry"
```

## Product States

### Active Products
- Can be added to Opportunities, Quotes, and Orders
- Appear in product searches by default
- Can have Pricebook Entries

### Inactive Products
- Cannot be added to new Opportunities, Quotes, or Orders
- Still appear in existing transactions
- Can be reactivated by setting Is Active = true

## Relationships

- **Pricebook Entries** (One-to-Many) - Product can have entries in multiple pricebooks
- **Opportunity Line Items** (One-to-Many) - Product can be on multiple opportunities
- **Quote Line Items** (One-to-Many) - Product can be on multiple quotes
- **Order Items** (One-to-Many) - Product can be on multiple orders

## Related Entities

- [Pricebook Entry](pages/entities/PricebookEntry.md) - Pricing information for the product
- [Order Item](pages/entities/OrderItem.md) - Orders containing this product
- [Opportunity](pages/entities/Opportunity.md) - Opportunities containing this product
- [Quote](pages/entities/Quote.md) - Quotes containing this product

## Implementation Notes

- Product Code must be unique across the organization
- Name is the display name shown to users
- Family is optional but highly recommended for organization
- Products should be created before creating Pricebook Entries
- Inactive products are hidden from most searches but not deleted
- Standard Pricebook entry is automatically created in some Salesforce configurations
- Custom fields with __c suffix can be added to products for additional attributes
