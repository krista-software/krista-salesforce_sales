# Link File to Order

## Overview

The **Link File to Order** catalog request links an uploaded file (represented by a Content Document ID) to a specific Order in Salesforce. This creates a ContentDocumentLink relationship, making the file appear in the Order's related files/attachments.

## Use Cases

- **PO to Order Workflow**: Attach uploaded purchase order PDFs to the corresponding Order record
- **Document Organization**: Link supporting documents (contracts, invoices, receipts) to orders
- **Audit Trail**: Maintain document references for compliance and auditing purposes

## Catalog Request Details

| Property | Value |
|----------|-------|
| **Request Name** | Link File to Order |
| **Request ID** | `localDomainRequest_file_link_001` |
| **Area** | Order Management |
| **Type** | CHANGE_SYSTEM |

## Input Parameters

| **Parameter Name** | **Parameter Type** | **Mandatory** | **Example** | **Description** |
|--------------------|--------------------|---------------|-------------|-----------------|
| Content Document Id | Text | Yes | "0695w00000AbcDeAAZ" | The Content Document ID from Upload PO File |
| Order Id | Text | Yes | "8015w00000XyzAbAAZ" | The Salesforce Order ID to link the file to |
| Share Type | Text | No | "V" | Share type (V=Viewer, C=Collaborator, I=Inferred) |
| Visibility | Text | No | "AllUsers" | Visibility setting (AllUsers, InternalUsers, SharedUsers) |

### Share Type Values

| **Value** | **Description** |
|-----------|-----------------|
| V (default) | Viewer - Users can view the file |
| C | Collaborator - Users can edit the file |
| I | Inferred - Visibility is inferred from record |

### Visibility Values

| **Value** | **Description** |
|-----------|-----------------|
| AllUsers (default) | All users with record access can see the file |
| InternalUsers | Only internal users can see the file |
| SharedUsers | Only explicitly shared users can see the file |

## Output Parameters

| **Parameter Name** | **Parameter Type** | **Description** |
|--------------------|--------------------|-----------------|
| Link Id | Text | The Content Document Link ID |

## Usage Examples

### Example 1: Link PDF to Order (Default Settings)

**Input:**
```
Content Document Id: "0695w00000AbcDeAAZ"
Order Id: "8015w00000XyzAbAAZ"
```

**Output:**
```
Link Id: "06A5w00000Link123AAZ"
```

### Example 2: Link with Custom Share Settings

**Input:**
```
Content Document Id: "0695w00000AbcDeAAZ"
Order Id: "8015w00000XyzAbAAZ"
Share Type: "V"
Visibility: "InternalUsers"
```

**Output:**
```
Link Id: "06A5w00000Link456AAZ"
```

## Error Handling

| **Error Scenario** | **Error Message** | **Resolution** |
|--------------------|-------------------|----------------|
| Content Document Id is empty | "Content Document Id is required to link a file." | Provide a valid Content Document ID |
| Order Id is empty | "Order Id is required to link a file." | Provide a valid Order ID |
| Invalid Content Document Id | "Content Document not found." | Verify the Content Document ID is correct |
| Invalid Order Id | "Order not found." | Verify the Order ID exists in Salesforce |
| Already linked | "File is already linked to this record." | No action needed, file is already linked |

## Complete PO Upload Workflow

### Step 1: Upload the File
```
Upload PO File:
  - Upload File: PurchaseOrder_12345.pdf
  - Description: "PO from Acme Corp"

Output: Content Document Id = "0695w00000AbcDeAAZ"
```

### Step 2: Link to Order
```
Link File to Order:
  - Content Document Id: "0695w00000AbcDeAAZ"
  - Order Id: "8015w00000XyzAbAAZ"

Output: Link Id = "06A5w00000Link123AAZ"
```

### Step 3: Verify in Salesforce
The file now appears in the Order's Files/Attachments related list.

## Related Requests

- [Upload PO File](pages/UploadPOFile.md) - Upload the file before linking
- [Order](pages/entities/Order.md) - Create or find the order to link files to

## Implementation Notes

- A file can be linked to multiple records using multiple Link File requests
- The same file can be linked to Orders, Accounts, Opportunities, etc.
- Linking a file does not create a copy; it creates a reference to the original
- Deleting the link does not delete the file itself
- Files are accessible based on the visibility settings and user's record access