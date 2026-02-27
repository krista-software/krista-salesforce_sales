# Find Or Create Account

## Overview

The **Find Or Create Account** catalog request searches for an account by name in Salesforce. If an account with the given name exists, it returns the existing account. If not found, it creates a new account with the given name and returns the newly created account.

## Use Cases

- **PO to Order Workflow**: Automatically find or create customer accounts when processing purchase orders
- **Data Deduplication**: Prevent duplicate accounts by checking existence before creation
- **Integration Scenarios**: Ensure accounts exist in Salesforce before creating related records (Opportunities, Orders, etc.)

## Catalog Request Details

| Property | Value |
|----------|-------|
| **Request Name** | Find Or Create Account |
| **Request ID** | `localDomainRequest_a1b2c3d4-5e6f-7890-abcd-ef1234567890` |
| **Area** | Accounts Management |
| **Type** | QUERY_SYSTEM |

## Input Parameters

| **Parameter Name** | **Parameter Type** | **Mandatory** | **Example** | **Description** |
|--------------------|--------------------|---------------|-------------|-----------------|
| Account Name | Text | Yes | "Acme Corporation" | The name of the account to find or create |

## Output Parameters

| **Parameter Name** | **Parameter Type** | **Description** |
|--------------------|--------------------|-----------------|
| Account | Entity(Account) | The found or created Account entity |

## Account Entity Fields

The returned Account entity includes the following fields:

| **Field Name** | **Type** | **Description** |
|----------------|----------|-----------------|
| Id | Text | Salesforce Account ID |
| Name | Text | Account name |
| Account Number | Text | Account number |
| Account Source | Text | Source of the account |
| Annual Revenue | Number | Annual revenue |
| Billing Street | Street | Billing street address |
| Billing City | City | Billing city |
| Billing State | State | Billing state |
| Billing Postal Code | PostalCode | Billing postal code |
| Billing Country | Country | Billing country |
| Shipping Street | Street | Shipping street address |
| Shipping City | City | Shipping city |
| Shipping State | State | Shipping state |
| Shipping Postal Code | PostalCode | Shipping postal code |
| Shipping Country | Country | Shipping country |
| Phone | Phone | Phone number |
| Fax | Text | Fax number |
| Website | Text | Website URL |
| Industry | Pick one | Industry type |
| Number Of Employees | Number | Number of employees |
| Ownership | Pick one | Ownership type |
| Type | Pick one | Account type |
| Customer Priority | Pick one | Customer priority level |
| SLA Expiration Date | Date | SLA expiration date |
| SLA Serial Number | Text | SLA serial number |
| Created Date | Date | Creation timestamp |
| Created By Id | Text | ID of user who created |
| Last Modified Date | Date | Last modification timestamp |
| Owner Id | Text | ID of account owner |
| Clean Status | Text | Data.com clean status |

## Usage Examples

### Example 1: Find Existing Account

**Input:**
```
Account Name: "Acme Corporation"
```

**Output (if found):**
```json
{
  "Id": "0015g00000u7ZScAAM",
  "Name": "Acme Corporation",
  "Account Number": "ACC-001",
  "Billing City": "San Francisco",
  "Billing State": "CA",
  "Phone": "(555) 123-4567",
  "Industry": "Technology",
  ...
}
```

### Example 2: Create New Account

**Input:**
```
Account Name: "New Customer Inc"
```

**Output (if not found - new account created):**
```json
{
  "Id": "0015g00000NEW123AAM",
  "Name": "New Customer Inc",
  "Billing City": null,
  "Billing State": null,
  ...
}
```

## Error Handling

| **Error Scenario** | **Error Message** | **Resolution** |
|--------------------|-------------------|----------------|
| Account Name is empty | "Account Name is required to find or create an account." | Provide a valid account name |
| Salesforce API error | "Failed to search/create account: [error details]" | Check Salesforce connectivity and permissions |

## Related Requests

- [Account Change Alert](pages/AccountChangeAlert.md) - Receive alerts when accounts are created or updated
- [Get Opportunities](pages/GetOpportunities.md) - Get opportunities related to an account
- [Create Order](pages/Order.md) - Create orders for the account

## Implementation Notes

- The search is case-insensitive
- If multiple accounts match the name, the first one found is returned
- New accounts are created with minimal required fields (Name only)
- Additional fields can be populated by using the Update Account entity operation after creation