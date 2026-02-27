# Upload PO File

## Overview

The **Upload PO File** catalog request uploads a Purchase Order file (PDF, Word, Excel, etc.) to Salesforce using the ContentVersion API. The file is uploaded to Salesforce's content management system and a Content Document ID is returned, which can be used to link the file to Orders or other Salesforce records.

## Use Cases

- **PO to Order Workflow**: Upload purchase order documents received from customers
- **Document Management**: Store supporting documents for orders (contracts, invoices, etc.)
- **Integration Scenarios**: Attach external documents to Salesforce records programmatically

## Catalog Request Details

| Property | Value |
|----------|-------|
| **Request Name** | Upload PO File |
| **Request ID** | `localDomainRequest_file_upload_001` |
| **Area** | Order Management |
| **Type** | CHANGE_SYSTEM |

## Input Parameters

| **Parameter Name** | **Parameter Type** | **Mandatory** | **Example** | **Description** |
|--------------------|--------------------|---------------|-------------|-----------------|
| Upload File | File | Yes | PurchaseOrder.pdf | The file to upload (PDF, Word, Excel, etc.) |
| Description | Text | No | "PO for Order #12345" | Optional description for the file |

## Output Parameters

| **Parameter Name** | **Parameter Type** | **Description** |
|--------------------|--------------------|-----------------|
| Content Document Id | Text | The Salesforce Content Document ID for the uploaded file |

## Supported File Formats

- **PDF** (.pdf) - Portable Document Format
- **Microsoft Word** (.doc, .docx)
- **Microsoft Excel** (.xls, .xlsx)
- **Microsoft PowerPoint** (.ppt, .pptx)
- **Text Files** (.txt)
- **Images** (.jpg, .jpeg, .png, .gif)
- **Other formats supported by Salesforce Content**

## Usage Examples

### Example 1: Upload PDF Purchase Order

**Input:**
```
Upload File: PurchaseOrder_12345.pdf
Description: "Purchase order from Acme Corp dated 2024-01-15"
```

**Output:**
```
Content Document Id: 0695w00000AbcDeAAZ
```

### Example 2: Upload Excel Spreadsheet

**Input:**
```
Upload File: OrderDetails.xlsx
Description: "Order line items and pricing details"
```

**Output:**
```
Content Document Id: 0695w00000XyzAbAAZ
```

## Error Handling

| **Error Scenario** | **Error Message** | **Resolution** |
|--------------------|-------------------|----------------|
| Upload File is null | "Upload File is required to upload a file to Salesforce." | Select a file to upload |
| File name is empty | "File name cannot be empty." | Ensure the file has a valid name |
| File size exceeds limit | "File size exceeds Salesforce limit." | Reduce file size or split into smaller files |
| Invalid file format | "File format not supported." | Use a supported file format |
| Salesforce API error | "Failed to upload file: [error details]" | Check Salesforce connectivity and storage limits |

## Next Steps After Upload

After uploading a file, you typically want to link it to a specific record (like an Order):

1. Note the returned `Content Document Id`
2. Use the [Link File to Order](pages/LinkFileToOrder.md) request to associate the file with an Order
3. Or use Salesforce's ContentDocumentLink to link to other record types

## Related Requests

- [Link File to Order](pages/LinkFileToOrder.md) - Link the uploaded file to an Order
- [Find Or Create Order](pages/Order.md) - Create the order to link files to

## Implementation Notes

- Files are stored in Salesforce's Content Management System (CMS)
- The Content Document ID can be used to reference the file across all Salesforce
- Files are versioned automatically if uploaded multiple times with the same name
- Maximum file size is determined by Salesforce organization limits (typically 2GB)
- Files are accessible to users based on Salesforce sharing settings