/*
 * Salesforce Sales Extension for Krista
 * Copyright (C) 2025 Krista Software
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package app.krista.extensions.crm.sales.salesforce_sales.catalog.entities;


import app.krista.extension.impl.anno.*;

import java.util.LinkedHashMap;
import java.util.Map;

@Searchable
@Domain(id = "catEntryDomain_086f33da-aaea-4d0c-816f-3d5f20b4a273",
        name = "Sales",
        ecosystemId = "catEntryEcosystem_8a0e8ce7-6289-492f-9202-52284024280c",
        ecosystemName = "CRM",
        ecosystemVersion = "db320c64-6f6f-4c9e-8daa-41d3f796d6a6")
@Entity(name = "Quote", id = "localDomainEntity_d9fc6f39-0586-4304-97d4-76e65ac52205", primaryKey = "Id", supportStore = true, description = "Represents a sales quote in Salesforce. Belongs to an Opportunity (via Opportunity Id referencing Opportunity.Id). Account Id references Account.Id. Contact Id references a Contact.")
public class Quote {

    @Field(name = "Id", type = "Text", required = false, description = "Unique Salesforce identifier for the quote.")
    public String id;

    @Searchable
    @ToString
    @Field(name = "Name", type = "Text", description = "Name of the quote.")
    public String name;

    @Field(name = "Created Date", type = "Date", required = false, attributes = @Attribute(name = "includeTimeOfDay", value = "true"), description = "Date when the quote was created.")
    public Long createdDate;

    @Searchable
    @ToString
    @Field(name = "Opportunity Id", type = "Text", description = "Id of the associated opportunity. References Opportunity.Id.")
    public String opportunityId;

    @Field(name = "Pricebook Id", type = "Text", required = false, description = "Id of the associated pricebook.")
    public String pricebookId;

    @Field(name = "Contact Id", type = "Text", required = false, description = "Id of the primary contact for the quote.")
    public String contactId;

    @Field(name = "Shipping Handling", type = "Number", required = false, description = "Shipping and handling charges.")
    public Double shippingHandling;

    @Field(name = "Tax", type = "Number", required = false, description = "Tax amount.")
    public Double tax;

    @Field(name = "Expiration Date", type = "Date", required = false, description = "Date when the quote expires.")
    public Long expirationDate;

    @Field(name = "Subtotal", type = "Number", required = false, description = "Subtotal before tax and shipping.")
    public Double subtotal;

    @Field(name = "Line Item Count", type = "Number", required = false, description = "Number of line items in the quote.")
    public Double lineItemCount;

    @Field(name = "Billing Name", type = "Text", required = false, description = "Name for billing.")
    public String billingName;

    @Searchable
    @ToString
    @Field(name = "Account Id", type = "Text", required = false, description = "Id of the associated account. References Account.Id.")
    public String accountId;

    @Field(name = "Quote Number", type = "Number", required = false, description = "Auto-generated quote number.")
    public Double quoteNumber;

    @Searchable
    @ToString
    @Field(name = "Status", type = "PickOne", required = false, description = "Current status of the quote.")
    public String status;

    @Field(name = "Description", type = "Paragraph", required = false, description = "Description of the quote.")
    public String description;

    @Field(name = "Total Price", type = "Currency", required = false, description = "Total price of the quote.")
    public Double totalPrice;

    @Field(name = "Discount", type = "Percentage", required = false, description = "Discount percentage or amount.")
    public Double discount;

    @Field(name = "Grand Total", type = "Currency", required = false, description = "Grand total including tax and shipping.")
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

    public Map<String, Object> toFields() {
        Map<String, Object> fieldMap = new LinkedHashMap<>();
        fieldMap.put("Id", id);
        fieldMap.put("Name", name);
        fieldMap.put("Created Date", createdDate);
        fieldMap.put("Opportunity Id", opportunityId);
        fieldMap.put("Pricebook Id", pricebookId);
        fieldMap.put("Contact Id", contactId);
        fieldMap.put("Shipping Handling", shippingHandling);
        fieldMap.put("Tax", tax);
        fieldMap.put("Expiration Date", expirationDate);
        fieldMap.put("Subtotal", subtotal);
        fieldMap.put("Line Item Count", lineItemCount);
        fieldMap.put("Billing Name", billingName);
        fieldMap.put("Account Id", accountId);
        fieldMap.put("Quote Number", quoteNumber);
        fieldMap.put("Status", status);
        fieldMap.put("Description", description);
        fieldMap.put("Total Price", totalPrice);
        fieldMap.put("Discount", discount);
        fieldMap.put("Grand Total", grandTotal);
        return fieldMap;
    }

}
