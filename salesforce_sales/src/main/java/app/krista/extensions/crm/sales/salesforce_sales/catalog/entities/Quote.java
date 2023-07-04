package app.krista.extensions.crm.sales.salesforce_sales.catalog.entities;


import app.krista.extension.impl.anno.*;

import java.util.Map;

@Domain(id = "catEntryDomain_086f33da-aaea-4d0c-816f-3d5f20b4a273",
        name = "Sales",
        ecosystemId = "catEntryEcosystem_8a0e8ce7-6289-492f-9202-52284024280c",
        ecosystemName = "CRM",
        ecosystemVersion = "db320c64-6f6f-4c9e-8daa-41d3f796d6a6")
@Entity(name = "Quote", id = "localDomainEntity_d9fc6f39-0586-4304-97d4-76e65ac52205", primaryKey = "Id")
public class Quote {

    @Field(name = "Id", type = "Text", required = false)
    public String id;

    @Searchable
    @ToString
    @Field(name = "Name", type = "Text")
    public String name;

    @Field(name = "Created Date", type = "Date", required = false, attributes = @Attribute(name = "includeTimeOfDay", value = "true"))
    public Long createdDate;

    @Searchable
    @ToString
    @Field(name = "Opportunity Id", type = "Text")
    public String opportunityId;

    @Field(name = "Pricebook Id", type = "Text", required = false)
    public String pricebookId;

    @Field(name = "Contact Id", type = "Text", required = false)
    public String contactId;

    @Field(name = "Shipping Handling", type = "Number", required = false)
    public Double shippingHandling;

    @Field(name = "Tax", type = "Number", required = false)
    public Double tax;

    @Field(name = "Expiration Date", type = "Date", required = false)
    public Long expirationDate;

    @Field(name = "Subtotal", type = "Number", required = false)
    public Double subtotal;

    @Field(name = "Line Item Count", type = "Number", required = false)
    public Double lineItemCount;

    @Field(name = "Billing Name", type = "Text", required = false)
    public String billingName;

    @Searchable
    @ToString
    @Field(name = "Account Id", type = "Text", required = false)
    public String accountId;

    @Field(name = "Quote Number", type = "Number", required = false)
    public Double quoteNumber;

    @Searchable
    @ToString
    @Field(name = "Status", type = "PickOne", required = false)
    public String status;

    @Field(name = "Description", type = "Paragraph", required = false)
    public String description;

    @Field(name = "Total Price", type = "Currency", required = false)
    public Double totalPrice;

    @Field(name = "Discount", type = "Percentage", required = false)
    public Double discount;

    @Field(name = "Grand Total", type = "Currency", required = false)
    public Double grandTotal;

    public static Quote fromFields(Map<String, Object> fields) {
        Quote quote = new Quote();
        quote.id = (String) fields.get("Id");
        quote.name = (String) fields.get("Name");
        quote.createdDate = (Long) fields.get("Created Date");
        quote.opportunityId = (String) fields.get("Opportunity Id");
        quote.pricebookId = (String) fields.get("Pricebook Id");
        quote.contactId = (String) fields.get("Contact Id");
        quote.shippingHandling = (Double) fields.get("Shipping Handling");
        quote.tax = (Double) fields.get("Tax");
        quote.expirationDate = (Long) fields.get("Expiration Date");
        quote.subtotal = (Double) fields.get("Subtotal");
        quote.lineItemCount = (Double) fields.get("Line Item Count");
        quote.billingName = (String) fields.get("Billing Name");
        quote.accountId = (String) fields.get("Account Id");
        quote.quoteNumber = (Double) fields.get("Quote Number");
        quote.status = (String) fields.get("Status");
        quote.description = (String) fields.get("Description");
        quote.totalPrice = (Double) fields.get("Total Price");
        quote.discount = (Double) fields.get("Discount");
        quote.grandTotal = (Double) fields.get("GrandTotal");
        return quote;
    }

}