# Send A Mail

## Overview

The Send A Mail catalog request sends email messages through Salesforce Sales. This request enables automated email communication with customers, prospects, and team members directly from the CRM system.

## Request Details

- **Catalog Request Name**: Send A Mail
- **Description**: Send email messages through Salesforce
- **Type**: Communication Action
- **Returns**: Email send confirmation and status

## Parameters

### Required Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| toAddresses | Email Array | Yes | Recipient email addresses |
| subject | Text | Yes | Email subject line |
| body | RichText | Yes | Email message body (supports HTML) |

### Optional Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| ccAddresses | Email Array | No | CC recipient email addresses |
| bccAddresses | Email Array | No | BCC recipient email addresses |
| fromAddress | Email | No | Sender email address (must be verified) |
| templateId | Text | No | Salesforce email template ID |
| attachments | File Array | No | File attachments |
| relatedToId | Text | No | Related record ID (Account, Opportunity, etc.) |
| priority | PickOne | No | Email priority (High, Normal, Low) |

## Request Examples

### Basic Email
```json
{
  "toAddresses": ["customer@example.com"],
  "subject": "Follow-up on Your Inquiry",
  "body": "<p>Dear Customer,</p><p>Thank you for your interest in our products.</p>",
  "priority": "Normal"
}
```

### Email with Attachments
```json
{
  "toAddresses": ["prospect@company.com"],
  "subject": "Product Proposal",
  "body": "<p>Please find attached our proposal.</p>",
  "attachments": [
    {
      "fileName": "Proposal.pdf",
      "fileContent": "base64encodedcontent",
      "contentType": "application/pdf"
    }
  ],
  "relatedToId": "0065w00000ABC123"
}
```

## Response Format

### Successful Send
```json
{
  "success": true,
  "emailId": "02s5w00000ABC123",
  "sentDate": 1640995200000,
  "recipientCount": 1,
  "status": "Sent"
}
```

## Common Use Cases

1. **Follow-up Communications**: Send follow-up emails after meetings
2. **Proposal Delivery**: Send proposals and quotes to prospects
3. **Customer Updates**: Notify customers of important changes
4. **Welcome Messages**: Send welcome emails to new customers

## See Also

- [Send Verification Mail](pages/SendVerificationMail.md)
- [Account Entity](pages/entities/Account.md)
- [Lead Entity](pages/entities/Lead.md)
