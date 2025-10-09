# Quote Entity

## Overview

The Quote entity represents formal price quotes generated for opportunities in Salesforce Sales. Quotes contain detailed pricing information, terms, and conditions for products or services being offered to customers.

## Entity Details

- **Entity Name**: Quote
- **Primary Key**: Id
- **Searchable Fields**: Name, Opportunity Id
- **Display Fields**: Name, Opportunity Id

## Fields

### Core Fields

| Field Name | Type | Required | Description |
|------------|------|----------|-------------|
| Id | Text | No | Unique identifier for the Quote record |
| Name | Text | Yes | Name of the quote (searchable, used for display) |
| Opportunity Id | Text | Yes | ID of the associated opportunity (searchable, used for display) |
| Contact Id | Text | No | ID of the primary contact for this quote |
| Pricebook Id | Text | No | ID of the pricebook used for pricing |

### Financial Information

| Field Name | Type | Required | Description |
|------------|------|----------|-------------|
| Subtotal | Number | No | Subtotal amount before tax and shipping |
| Tax | Number | No | Tax amount |
| Shipping Handling | Number | No | Shipping and handling charges |
| Total Price | Number | No | Total quote amount |
| Grand Total | Number | No | Final total including all charges |
| Line Item Count | Number | No | Number of line items in the quote |

### Status and Dates

| Field Name | Type | Required | Description |
|------------|------|----------|-------------|
| Status | Text | No | Current status of the quote |
| Created Date | Date | No | Date and time when quote was created (includes time) |
| Expiration Date | Date | No | Date when the quote expires |

### Additional Information

| Field Name | Type | Required | Description |
|------------|------|----------|-------------|
| Description | Paragraph | No | Additional description or notes |
| Quote Number | Text | No | Auto-generated quote number |
| Discount | Number | No | Total discount amount |

## Quote Status Values (Common)

- **Draft**: Quote is being prepared, not yet sent to customer
- **In Review**: Quote is under internal review
- **Presented**: Quote has been sent to customer
- **Accepted**: Customer has accepted the quote
- **Denied**: Customer has rejected the quote
- **Expired**: Quote has passed its expiration date

## Usage in Catalog Requests

The Quote entity is used in the following catalog requests:

- **Get All Quote Status**: Retrieve available quote status values
- **Quote-related reporting**: Track quote performance and conversion rates

## Quote Lifecycle

### Creation
1. **Opportunity Assessment**: Determine need for formal quote
2. **Product Selection**: Choose products/services to include
3. **Pricing Calculation**: Apply pricing, discounts, taxes
4. **Quote Generation**: Create formal quote document

### Review and Approval
1. **Internal Review**: Review pricing and terms
2. **Management Approval**: Get approval for discounts/special terms
3. **Final Preparation**: Prepare quote for customer presentation

### Customer Interaction
1. **Quote Presentation**: Send quote to customer
2. **Negotiation**: Handle customer questions and requests
3. **Revision**: Update quote based on feedback
4. **Decision**: Customer accepts, rejects, or requests changes

## Relationships

- **Many-to-One with Opportunity**: Each quote belongs to one opportunity
- **Many-to-One with Contact**: Each quote has one primary contact
- **Many-to-One with Pricebook**: Each quote uses one pricebook for pricing
- **One-to-Many with Quote Line Items**: Each quote can have multiple line items

## Best Practices

1. **Clear Naming**: Use descriptive names that include customer and date
2. **Accurate Pricing**: Ensure all pricing is current and accurate
3. **Expiration Dates**: Set realistic expiration dates
4. **Regular Updates**: Keep quote status current
5. **Version Control**: Track quote revisions and changes
6. **Follow-up**: Monitor quote status and follow up appropriately

## Common Use Cases

1. **Opportunity Conversion**: Convert opportunities to formal quotes
2. **Pricing Documentation**: Provide detailed pricing to customers
3. **Sales Process**: Formalize the sales process with official quotes
4. **Revenue Tracking**: Track quoted vs. actual revenue
5. **Approval Workflows**: Manage quote approval processes
6. **Customer Communication**: Professional customer-facing documents

## Example Usage

```json
{
  "id": "0Q05w00000ABC123",
  "name": "Acme Corp - Enterprise Software Quote",
  "opportunityId": "0065w00000ABC123",
  "contactId": "0035w00000ABC123",
  "pricebookId": "01s5w00000ABC123",
  "status": "Presented",
  "subtotal": 45000.00,
  "tax": 3600.00,
  "shippingHandling": 500.00,
  "totalPrice": 49100.00,
  "expirationDate": 1640995200000,
  "lineItemCount": 5,
  "createdDate": 1640908800000
}
```

## See Also

- [Opportunity Entity](pages/entities/Opportunity.md)
- [PricebookEntry Entity](pages/entities/PricebookEntry.md)
- [Get All Quote Status](pages/GetAllQuoteStatus.md)
