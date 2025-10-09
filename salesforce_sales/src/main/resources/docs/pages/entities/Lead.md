# Lead Entity

## Overview

The Lead entity represents a potential customer or prospect in Salesforce Sales. Leads are individuals or companies that have shown interest in your products or services but haven't yet been qualified as sales opportunities. They are the starting point of the sales process and can be converted into Accounts, Contacts, and Opportunities.

## Entity Details

- **Entity Name**: Lead
- **Primary Key**: Id
- **Searchable Fields**: Name, Owner Id, Status
- **Display Field**: Name
- **Store Support**: No

## Fields

### Core Fields

| Field Name | Type | Required | Description |
|------------|------|----------|-------------|
| Id | Text | No | Unique identifier for the Lead record |
| Name | Text | Yes | Full name of the lead (searchable, used for display) |
| Company | Text | Yes | Company name where the lead works |
| Title | Text | No | Job title of the lead |
| Owner Id | Text | No | ID of the user who owns this lead (searchable) |

### Contact Information

| Field Name | Type | Required | Description |
|------------|------|----------|-------------|
| Email | Email | No | Primary email address |
| Phone | Phone | No | Primary phone number |
| MobilePhone | Phone | No | Mobile phone number |
| Website | Text | No | Company website URL |

### Lead Qualification

| Field Name | Type | Required | Description |
|------------|------|----------|-------------|
| Status | PickOne | No | Current status of the lead (searchable) |
| Rating | PickOne | No | Lead rating (Hot, Warm, Cold) |
| LeadSource | PickOne | No | Source that generated this lead |
| Industry | PickOne | No | Industry classification |
| Annual Revenue | Currency | No | Company's annual revenue |

### Conversion Information

| Field Name | Type | Required | Description |
|------------|------|----------|-------------|
| Is Converted | Switch | No | Whether the lead has been converted |
| Converted Date | Date | No | Date when the lead was converted |
| Converted Account Id | Text | No | ID of the account created from conversion |
| Converted Contact Id | Text | No | ID of the contact created from conversion |
| Converted Opportunity Id | Text | No | ID of the opportunity created from conversion |

### System Fields

| Field Name | Type | Required | Description |
|------------|------|----------|-------------|
| Created Date | Date | No | Date and time when the lead was created |
| Last Modified Date | Date | No | Date and time of last modification |
| Description | Paragraph | No | Additional notes about the lead |
| Clean Status | Text | No | Data cleanliness status indicator |

## Lead Lifecycle

### Lead Status Values (Common)
- **Open - Not Contacted**: New lead, no contact made yet
- **Working - Contacted**: Lead has been contacted, working the opportunity
- **Closed - Converted**: Lead successfully converted to Account/Contact/Opportunity
- **Closed - Not Converted**: Lead closed without conversion

### Lead Rating Values (Common)
- **Hot**: High-priority lead, immediate follow-up needed
- **Warm**: Qualified lead, regular follow-up required
- **Cold**: Low-priority lead, minimal follow-up

### Lead Source Values (Common)
- **Web**: Website inquiry or form submission
- **Phone Inquiry**: Inbound phone call
- **Partner Referral**: Referred by business partner
- **Purchased List**: From purchased contact list
- **Trade Show**: Met at trade show or event
- **Word of mouth**: Referral from existing customer

## Usage in Catalog Requests

The Lead entity is used in the following catalog requests:

- **Get Leads**: Retrieve leads with filtering options
- **Lead Change Alert**: Monitor changes to lead records

## Lead Conversion Process

When a lead is qualified and ready to become a customer:

1. **Conversion Decision**: Sales rep decides to convert the lead
2. **Account Creation**: New account created (or existing account selected)
3. **Contact Creation**: Lead becomes a contact under the account
4. **Opportunity Creation**: Sales opportunity created if applicable
5. **Lead Status Update**: Lead marked as "Closed - Converted"
6. **Relationship Establishment**: All records linked together

## Relationships

### Before Conversion
- **Many-to-One with User (Owner)**: Each lead has one owner

### After Conversion
- **One-to-One with Account**: Lead converts to one account
- **One-to-One with Contact**: Lead converts to one contact  
- **One-to-One with Opportunity**: Lead may convert to one opportunity

## Validation Rules

- **Name**: Required field, cannot be empty
- **Company**: Required field, cannot be empty
- **Email**: Must be valid email format if provided
- **Phone**: Must be valid phone number format if provided
- **Website**: Must be valid URL format if provided

## Best Practices

1. **Lead Qualification**: Use consistent criteria to qualify leads
2. **Response Time**: Follow up on new leads quickly (within 24 hours)
3. **Lead Scoring**: Implement lead scoring based on engagement
4. **Data Quality**: Keep lead information complete and accurate
5. **Conversion Timing**: Convert leads when they're sales-ready
6. **Source Tracking**: Always capture lead source for ROI analysis

## Common Use Cases

1. **Lead Generation**: Capture prospects from various sources
2. **Lead Nurturing**: Develop relationships until sales-ready
3. **Lead Qualification**: Assess fit and buying potential
4. **Lead Assignment**: Route leads to appropriate sales reps
5. **Conversion Tracking**: Monitor lead-to-customer conversion rates

## Example Usage

```json
{
  "id": "00Q5w00000ABC123",
  "name": "Jane Smith",
  "company": "Tech Solutions Inc",
  "title": "IT Director",
  "email": "jane.smith@techsolutions.com",
  "phone": "+1-555-987-6543",
  "status": "Working - Contacted",
  "rating": "Hot",
  "leadSource": "Web",
  "industry": "Technology",
  "annualRevenue": 5000000,
  "isConverted": false,
  "ownerId": "0055w00000ABC123"
}
```

## See Also

- [Account Entity](pages/entities/Account.md)
- [Opportunity Entity](pages/entities/Opportunity.md)
- [User Entity](pages/entities/User.md)
- [Get Leads](pages/GetLeads.md)
- [Lead Change Alert](pages/LeadChangeAlert.md)
