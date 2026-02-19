# Order Management Area - Testing Guide

This guide provides detailed instructions on how to test Order Management operations.

---

## Prerequisites

Before testing, ensure you have:
1. **Valid Salesforce Account ID** - Get from Salesforce (starts with `001`)
2. **Valid Pricebook Entry ID** - Get from Salesforce (starts with `01u`)

---

## Architecture Overview

The Order Management Area follows the **EntityStore pattern** for CRUD operations:

- **Create Operations**: Use `OrderStore.create()` and `OrderItemStore.create()` (EntityStore pattern)
- **Update Operations**: Use catalog requests "Update Order" and "Update Order Item" (for custom logic)
- **File Operations**: Use catalog requests "Upload PO File" and "Link File to Order"

---

## Using EntityStore for Create Operations

### Create Order via OrderStore

Order and OrderItem entities have `supportStore = true`, which means they automatically support CRUD operations through their EntityStores.

**To create an Order:**
1. Use the **OrderStore** entity store
2. Call the `create()` method with Order entity data

**Example Order Entity:**
```json
{
  "accountId": "001xx000000001AAAQ",
  "effectiveDate": 1704067200000,
  "status": "Draft",
  "description": "Purchase order from vendor ABC"
}
```

### Create Order Item via OrderItemStore

**To create an Order Item:**
1. Use the **OrderItemStore** entity store
2. Call the `create()` method with OrderItem entity data

**Example OrderItem Entity:**
```json
{
  "orderId": "801xx000000001AAAQ",
  "pricebookEntryId": "01uxx000000001AAAQ",
  "quantity": 5.0,
  "unitPrice": 100.0,
  "description": "Laptop - Dell XPS 15"
}
```

**How to get PricebookEntry ID:**
- Use **PricebookEntryStore** to search by Product2Id
- Example: Search for entries where `Product2Id = "01txx000000001AAAQ"`

---

## 1. Update Order

**Request Name:** `Update Order`
**Type:** CHANGE_SYSTEM
**Description:** Update an existing order with new field values

### Input Parameters

| Parameter | Type | Required | Example | Description |
|-----------|------|----------|---------|-------------|
| Order Id | Text | **Yes** | `801xx000000001AAAQ` | Salesforce Order ID to update |
| Fields To Update | FreeForm | **Yes** | See below | Fields to update as key-value pairs |

### Test Scenarios

#### Scenario 1: Update Order Status
```json
{
  "Order Id": "801xx000000001AAAQ",
  "Fields To Update": {
    "Status": "Activated"
  }
}
```
**Expected Result:** Returns `true`, order status updated to "Activated"

#### Scenario 2: Update Multiple Fields
```json
{
  "Order Id": "801xx000000001AAAQ",
  "Fields To Update": {
    "Status": "Activated",
    "Description": "Updated order description",
    "PO Number": "PO-2024-001-REVISED"
  }
}
```
**Expected Result:** Returns `true`, all fields updated

---

## 2. Update Order Item

**Request Name:** `Update Order Item`
**Type:** CHANGE_SYSTEM
**Description:** Update an existing order item with new field values

### Input Parameters

| Parameter | Type | Required | Example | Description |
|-----------|------|----------|---------|-------------|
| Order Item Id | Text | **Yes** | `802xx000000001AAAQ` | Salesforce Order Item ID to update |
| Fields To Update | FreeForm | **Yes** | See below | Fields to update as key-value pairs |

### Test Scenarios

#### Scenario 1: Update Quantity
```json
{
  "Order Item Id": "802xx000000001AAAQ",
  "Fields To Update": {
    "Quantity": 15
  }
}
```
**Expected Result:** Returns `true`, quantity updated to 15

#### Scenario 2: Update Quantity and Price
```json
{
  "Order Item Id": "802xx000000001AAAQ",
  "Fields To Update": {
    "Quantity": 20,
    "Unit Price": 95.00
  }
}
```
**Expected Result:** Returns `true`, quantity and price updated

#### Scenario 3: Update Description
```json
{
  "Order Item Id": "802xx000000001AAAQ",
  "Fields To Update": {
    "Description": "Updated product description"
  }
}
```
**Expected Result:** Returns `true`, description updated

---

## 3. Upload PO File

**Request Name:** `Upload PO File`
**Type:** CHANGE_SYSTEM
**Description:** Upload a Purchase Order file (PDF, Word, Excel, etc.) to Salesforce

### Input Parameters

| Parameter | Type | Required | Example | Description |
|-----------|------|----------|---------|-------------|
| Upload File | File | **Yes** | (File upload) | The file to upload (PDF, Word, Excel, etc.) |
| Description | Text | No | `Purchase Order from Vendor ABC` | File description |

### Test Scenarios

#### Scenario 1: Upload PDF File
- **Upload File:** Select a PDF file (e.g., `PO-2024-001.pdf`)
- **Description:** `Purchase Order for Q1 2024`

**Expected Result:** Returns Content Document ID (starts with `069`)

#### Scenario 2: Upload Excel File
- **Upload File:** Select an Excel file (e.g., `PO-2024-001-Details.xlsx`)
- **Description:** `Detailed line items for PO-2024-001`

**Expected Result:** Returns Content Document ID

#### Scenario 3: Upload Word Document
- **Upload File:** Select a Word document (e.g., `PO-2024-001-Terms.docx`)
- **Description:** (Optional)

**Expected Result:** Returns Content Document ID

**Note:** The file upload is handled through Krista's file upload interface. Simply select the file from your local system - no need to manually convert to Base64.

---

## 4. Link File to Order

**Request Name:** `Link File to Order`
**Type:** CHANGE_SYSTEM
**Description:** Link an uploaded file (ContentDocument) to an Order

### Input Parameters

| Parameter | Type | Required | Example | Description |
|-----------|------|----------|---------|-------------|
| Content Document Id | Text | **Yes** | `069xx000000001AAAQ` | Content Document ID from upload |
| Order Id | Text | **Yes** | `801xx000000001AAAQ` | Salesforce Order ID |
| Share Type | Text | No | `V` | Share type (V=Viewer, C=Collaborator, I=Inferred) |
| Visibility | Text | No | `AllUsers` | Visibility (AllUsers, InternalUsers, SharedUsers) |

### Test Scenarios

#### Scenario 1: Link File with Default Settings
```json
{
  "Content Document Id": "069xx000000001AAAQ",
  "Order Id": "801xx000000001AAAQ"
}
```
**Expected Result:** Returns Content Document Link ID (starts with `06A`), file linked with default settings (ShareType=V, Visibility=AllUsers)

#### Scenario 2: Link File with Custom Settings
```json
{
  "Content Document Id": "069xx000000001AAAQ",
  "Order Id": "801xx000000001AAAQ",
  "Share Type": "C",
  "Visibility": "InternalUsers"
}
```
**Expected Result:** Returns Content Document Link ID with custom share settings

**Share Type Options:**
- `V` - Viewer (read-only access)
- `C` - Collaborator (can edit)
- `I` - Inferred (permissions based on record access)

**Visibility Options:**
- `AllUsers` - All users can see the file
- `InternalUsers` - Only internal users can see
- `SharedUsers` - Only users with access to the record can see


---

## Complete Workflow Example: PO to Order

This example shows how to use all the requests together to create a complete Purchase Order to Order workflow.

### Step 1: Search or Create Account (Using AccountStore)

**Note:** Use the AccountStore entity operations to search for or create accounts. The Account entity has `supportStore = false` but AccountStore provides full CRUD operations.

**Search Account:**
- Use AccountStore.search() to find existing account by name
- If found, use the Account ID

**Create Account (if not found):**
- Use AccountStore.create() with account data
- Returns new Account ID

**Result:** `001xx000000001AAAQ` (Account ID)

---

### Step 2: Create Order (Using OrderStore)

**Use OrderStore.create() with Order entity data:**
```json
{
  "accountId": "001xx000000001AAAQ",
  "effectiveDate": 1704067200000,
  "status": "Draft",
  "description": "Q1 2024 Hardware Purchase"
}
```
**Result:** `801xx000000001AAAQ` (Order ID)

---

### Step 3: Create Order Items (Using OrderItemStore)

**Item 1: Laptops**

**Use OrderItemStore.create() with OrderItem entity data:**
```json
{
  "orderId": "801xx000000001AAAQ",
  "pricebookEntryId": "01uxx000000001AAAQ",
  "quantity": 10.0,
  "unitPrice": 1200.0,
  "description": "Dell Latitude 5520 Laptops"
}
```
**Result:** `802xx000000001AAAQ` (Order Item ID)

**Item 2: Monitors**

**Use OrderItemStore.create() with OrderItem entity data:**
```json
{
  "orderId": "801xx000000001AAAQ",
  "pricebookEntryId": "01uxx000000002AAAQ",
  "quantity": 20.0,
  "unitPrice": 300.0,
  "description": "27-inch 4K Monitors"
}
```
**Result:** `802xx000000002AAAQ` (Order Item ID)

---

### Step 4: Upload PO File
- **Upload File:** Select the PO file (e.g., `PO-2024-001.pdf`)
- **Description:** `Purchase Order PDF from Acme Corporation`

**Result:** `069xx000000001AAAQ` (Content Document ID)

---

### Step 5: Link File to Order
```json
{
  "Content Document Id": "069xx000000001AAAQ",
  "Order Id": "801xx000000001AAAQ",
  "Share Type": "V",
  "Visibility": "AllUsers"
}
```
**Result:** `06Axx000000001AAAQ` (Content Document Link ID)

---

### Step 6: Activate Order (Optional)
```json
{
  "Order Id": "801xx000000001AAAQ",
  "Fields To Update": {
    "Status": "Activated"
  }
}
```
**Result:** `true` (Order activated)

---

## Common Salesforce ID Prefixes

| Object | ID Prefix | Example |
|--------|-----------|---------|
| Account | `001` | `001xx000000001AAAQ` |
| Order | `801` | `801xx000000001AAAQ` |
| OrderItem | `802` | `802xx000000001AAAQ` |
| Product2 | `01t` | `01txx000000001AAAQ` |
| PricebookEntry | `01u` | `01uxx000000001AAAQ` |
| ContentVersion | `068` | `068xx000000001AAAQ` |
| ContentDocument | `069` | `069xx000000001AAAQ` |
| ContentDocumentLink | `06A` | `06Axx000000001AAAQ` |

---

## Error Handling

### Common Errors and Solutions

| Error | Cause | Solution |
|-------|-------|----------|
| `Account Id is required` | Missing Account ID | Provide valid Account ID from Salesforce |
| `Order not found` | Invalid Order ID | Verify Order ID exists in Salesforce |
| `Pricebook Entry Id is required` | Missing Pricebook Entry | Get valid Pricebook Entry ID from Salesforce |
| `Order must be in Draft status` | Order already activated | Cannot add items to activated orders |
| `Upload File is required` | Missing file upload | Select a file to upload |
| `Content Document Id is required` | Missing document ID | Upload file first to get Content Document ID |

---

## Tips for Testing

1. **Get Real IDs from Salesforce:**
   - Log into Salesforce
   - Navigate to the object (Account, Product, etc.)
   - Copy the ID from the URL (e.g., `/001xx000000001AAAQ`)

2. **Test in Order:**
   - Always create Account first
   - Then create Order
   - Then create Order Items
   - Finally upload and link files

3. **Use Postman or Similar Tool:**
   - Save test scenarios as collections
   - Use environment variables for IDs
   - Chain requests together

4. **Verify in Salesforce:**
   - After each request, check Salesforce UI
   - Verify data was created/updated correctly
   - Check file attachments in the Files tab

---

## Next Steps

After testing individual requests, you can:
1. Create integration tests that combine multiple requests
2. Test error scenarios (invalid IDs, missing fields, etc.)
3. Test performance with large datasets
4. Automate testing with CI/CD pipelines


