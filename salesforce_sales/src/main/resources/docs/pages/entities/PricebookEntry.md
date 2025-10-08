# PricebookEntry Entity

## Overview

The PricebookEntry entity represents the pricing information for products within specific pricebooks in Salesforce Sales. It defines the unit price for products in different pricebooks, enabling flexible pricing strategies for different markets, customer segments, or sales channels.

## Entity Details

- **Entity Name**: Pricebook Entry
- **Primary Key**: Id
- **Searchable Fields**: Name
- **Display Fields**: Name

## Fields

### Core Fields

| Field Name | Type | Required | Description |
|------------|------|----------|-------------|
| Id | Text | No | Unique identifier for the PricebookEntry record |
| Name | Text | No | Name of the pricebook entry (searchable, used for display) |
| Pricebook Id | Text | Yes | ID of the pricebook this entry belongs to |
| Product Id | Text | Yes | ID of the product this entry prices |
| Product Code | Text | No | Code or SKU of the product |

### Pricing Information

| Field Name | Type | Required | Description |
|------------|------|----------|-------------|
| Unit Price | Currency | Yes | Price per unit for this product in this pricebook |

### System Fields

| Field Name | Type | Required | Description |
|------------|------|----------|-------------|
| Created Date | Date | No | Date and time when entry was created (includes time) |
| Last Modified Date | Date | No | Date and time of last modification (includes time) |

## Pricebook Structure

### Standard Pricebook
- Every product must have an entry in the Standard Pricebook
- Standard Pricebook serves as the baseline for all other pricebooks
- Cannot be deleted or deactivated

### Custom Pricebooks
- Created for specific markets, regions, or customer segments
- Can have different pricing than Standard Pricebook
- Used for quotes, opportunities, and orders

## Usage in Catalog Requests

PricebookEntry is used in:
- Quote generation and pricing
- Opportunity product selection
- Price list management
- Revenue calculations

## Pricing Strategies

### Geographic Pricing
- Different prices for different regions or countries
- Account for local market conditions and competition
- Support multiple currencies

### Customer Segment Pricing
- Volume discounts for enterprise customers
- Special pricing for partners or resellers
- Educational or non-profit pricing

### Channel Pricing
- Different prices for direct vs. partner sales
- Online vs. offline pricing strategies
- Seasonal or promotional pricing

## Relationships

- **Many-to-One with Pricebook**: Each entry belongs to one pricebook
- **Many-to-One with Product**: Each entry prices one product
- **One-to-Many with Quote Line Items**: Used in quote line items
- **One-to-Many with Opportunity Products**: Used in opportunity products

## Best Practices

1. **Consistent Naming**: Use clear, descriptive names for pricebook entries
2. **Regular Updates**: Keep pricing current with market conditions
3. **Version Control**: Track pricing changes and effective dates
4. **Approval Process**: Implement approval workflows for price changes
5. **Currency Management**: Handle multi-currency pricing appropriately
6. **Competitive Analysis**: Regular review against competitor pricing

## Common Use Cases

1. **Multi-Market Pricing**: Different prices for different geographic markets
2. **Customer Tier Pricing**: Volume-based pricing for different customer tiers
3. **Channel Pricing**: Different pricing for different sales channels
4. **Promotional Pricing**: Temporary promotional or discount pricing
5. **Product Bundling**: Pricing for product bundles and packages
6. **Contract Pricing**: Special pricing for specific customer contracts

## Example Usage

### Standard Pricebook Entry
```json
{
  "id": "01u5w00000ABC123",
  "name": "Enterprise Software License - Standard",
  "pricebookId": "01s000000000001", 
  "productId": "01t5w00000ABC123",
  "productCode": "ESL-001",
  "unitPrice": 1000.00,
  "createdDate": 1640995200000
}
```

### Custom Pricebook Entry
```json
{
  "id": "01u5w00000ABC124",
  "name": "Enterprise Software License - Volume Discount",
  "pricebookId": "01s5w00000ABC123",
  "productId": "01t5w00000ABC123", 
  "productCode": "ESL-001",
  "unitPrice": 800.00,
  "createdDate": 1640995200000
}
```

## Pricing Workflow

### Price Setting
1. **Product Creation**: Create product in Salesforce
2. **Standard Price**: Set price in Standard Pricebook
3. **Custom Prices**: Add entries to custom pricebooks as needed
4. **Approval**: Get pricing approved through defined process

### Price Management
1. **Regular Review**: Periodically review and update pricing
2. **Market Analysis**: Analyze competitor and market pricing
3. **Performance Tracking**: Monitor pricing effectiveness
4. **Adjustments**: Make pricing adjustments as needed

## Multi-Currency Considerations

- Each pricebook entry is in a specific currency
- Exchange rates may affect pricing across currencies
- Consider local purchasing power and market conditions
- Maintain pricing consistency across related markets

## See Also

- [Quote Entity](pages/entities/Quote.md)
- [Opportunity Entity](pages/entities/Opportunity.md)
- [Get All Quote Status](pages/GetAllQuoteStatus.md)
