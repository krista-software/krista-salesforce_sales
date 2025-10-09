# Account Entity

## Overview

The Account entity represents a company, organization, or individual that your business has a relationship with in Salesforce Sales. Accounts are fundamental objects in Salesforce that serve as the foundation for many other records like Opportunities, Contacts, and Assets.

## Entity Details

- **Entity Name**: Account
- **Primary Key**: Id
- **Searchable Field**: Name
- **Display Field**: Name
- **Store Support**: No

## Fields

### Core Fields

| Field Name | Type | Required | Description |
|------------|------|----------|-------------|
| Id | Text | No | Unique identifier for the Account record |
| Name | Text | Yes | Name of the account (searchable, used for display) |
| Account Number | Text | No | Custom account number for tracking |
| Type | PickOne | No | Type of account (e.g., Customer, Partner, Prospect) |
| Owner Id | Text | No | ID of the user who owns this account |
| Created By Id | Text | No | ID of the user who created this account |
| Created Date | Date | No | Date and time when the account was created |

### Contact Information

| Field Name | Type | Required | Description |
|------------|------|----------|-------------|
| Phone | Phone | No | Primary phone number for the account |
| Fax | Text | No | Fax number for the account |
| Website | Text | No | Website URL for the account |

### Address Information

#### Billing Address
| Field Name | Type | Required | Description |
|------------|------|----------|-------------|
| Billing Street | Street | No | Street address for billing |
| Billing City | City | No | City for billing address |
| Billing State | State | No | State/province for billing address |
| Billing Postal Code | PostalCode | No | Postal/ZIP code for billing address |
| Billing Country | Country | No | Country for billing address |

#### Shipping Address
| Field Name | Type | Required | Description |
|------------|------|----------|-------------|
| Shipping Street | Street | No | Street address for shipping |
| Shipping City | City | No | City for shipping address |
| Shipping State | State | No | State/province for shipping address |
| Shipping Postal Code | PostalCode | No | Postal/ZIP code for shipping address |
| Shipping Country | Country | No | Country for shipping address |

### Business Information

| Field Name | Type | Required | Description |
|------------|------|----------|-------------|
| Industry | PickOne | No | Industry classification for the account |
| Number Of Employees | Number | No | Total number of employees |
| Annual Revenue | Number | No | Annual revenue amount |
| Ownership | PickOne | No | Ownership type (e.g., Public, Private) |
| Sic | Text | No | Standard Industrial Classification code |

### Service Level Agreement (SLA)

| Field Name | Type | Required | Description |
|------------|------|----------|-------------|
| SLA Serial Number | Text | No | Serial number for SLA tracking |
| SLA Expiration Date | Date | No | Date when the SLA expires |
| Customer Priority | PickOne | No | Priority level for customer service |

### Data Quality

| Field Name | Type | Required | Description |
|------------|------|----------|-------------|
| Clean Status | Text | No | Data cleanliness status indicator |

## Usage in Catalog Requests

The Account entity is used in the following catalog requests:

- **Account Change Alert**: Monitors changes to account records
- **Get Opportunities**: Opportunities are linked to accounts
- **Update An Asset**: Assets can be associated with accounts
- **Various Reporting requests**: Account data is used in sales analytics

## Relationships

- **One-to-Many with Opportunities**: An account can have multiple opportunities
- **One-to-Many with Assets**: An account can own multiple assets
- **One-to-Many with Contacts**: An account can have multiple contacts
- **Many-to-One with User (Owner)**: Each account has one owner

## Validation Rules

- **Name**: Required field, cannot be empty
- **Phone**: Must be in valid phone number format
- **Website**: Must be a valid URL format if provided
- **Email addresses**: Must be valid email format if provided
- **Dates**: Must be valid date/time values

## Best Practices

1. **Naming Convention**: Use consistent naming for accounts (e.g., "Company Name, Inc.")
2. **Complete Address Information**: Fill both billing and shipping addresses when available
3. **Industry Classification**: Use standard industry categories for better reporting
4. **Owner Assignment**: Always assign an account owner for accountability
5. **Data Quality**: Regularly review and update account information

## Common Use Cases

1. **Customer Management**: Track customer information and relationships
2. **Sales Pipeline**: Link opportunities to accounts for sales tracking
3. **Territory Management**: Organize accounts by geography or industry
4. **Reporting**: Generate account-based sales and revenue reports
5. **Asset Tracking**: Associate company assets with account records

## Example Usage

```json
{
  "id": "0015w00000ABC123",
  "name": "Acme Corporation",
  "type": "Customer",
  "industry": "Technology",
  "numberOfEmployees": 500,
  "annualRevenue": 10000000,
  "billingStreet": "123 Main St",
  "billingCity": "San Francisco",
  "billingState": "CA",
  "billingPostalCode": "94105",
  "billingCountry": "USA",
  "phone": "+1-555-123-4567",
  "website": "https://www.acme.com"
}
```

## See Also

- [Opportunity Entity](pages/entities/Opportunity.md)
- [Asset Entity](pages/entities/Asset.md)
- [User Entity](pages/entities/User.md)
- [Account Change Alert](pages/AccountChangeAlert.md)
