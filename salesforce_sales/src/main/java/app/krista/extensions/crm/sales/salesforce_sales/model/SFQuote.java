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

package app.krista.extensions.crm.sales.salesforce_sales.model;

import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Quote;
import app.krista.extensions.crm.sales.salesforce_sales.util.DatePlus;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.Map;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Validators.returnNullOrValue;

public final class SFQuote {
    @SerializedName("Status")
    public String status;

    @SerializedName("ExpirationDate")
    public String expirationDate;

    @SerializedName("AccountId")
    public String accountId;

    @SerializedName("TotalPrice")
    public Double totalPrice;

    @SerializedName("Description")
    public String description;

    @SerializedName("Discount")
    public Double discount;

    @SerializedName("CreatedDate")
    public Date createdDate;

    @SerializedName("Subtotal")
    public Double subtotal;

    @SerializedName("Tax")
    public Double tax;

    @SerializedName("Pricebook2Id")
    public String pricebookId;

    @SerializedName("LineItemCount")
    public Double lineItemCount;

    @SerializedName("Name")
    public String name;

    @SerializedName("OpportunityId")
    public String opportunityId;

    @SerializedName("ShippingHandling")
    public Double shippingHandling;

    @SerializedName("BillingName")
    public String billingName;

    @SerializedName("Id")
    public String id;

    @SerializedName("QuoteNumber")
    public Double quoteNumber;

    @SerializedName("ContactId")
    public String contactId;

    @SerializedName("GrandTotal")
    public Double grandTotal;

    @SerializedName("CreatedById")
    public String createdById;

    @SerializedName("IsSyncing")
    public Boolean isSyncing;

    @SerializedName("BillingStreet")
    public String billingStreet;

    @SerializedName("BillingCity")
    public String billingCity;

    @SerializedName("BillingState")
    public String billingState;

    @SerializedName("BillingPostalCode")
    public String billingPostalCode;

    @SerializedName("BillingCountry")
    public String billingCountry;

    @SerializedName("ShippingStreet")
    public String shippingStreet;

    @SerializedName("ShippingCity")
    public String shippingCity;

    @SerializedName("ShippingState")
    public String shippingState;

    @SerializedName("ShippingPostalCode")
    public String shippingPostalCode;

    @SerializedName("ShippingCountry")
    public String shippingCountry;

    @SerializedName("ShippingName")
    public String shippingName;

    @SerializedName("Email")
    public String email;

    @SerializedName("Phone")
    public String phone;

    @SerializedName("Fax")
    public String fax;

    public static SFQuote from(Quote sfQuote) {
        SFQuote quote = new SFQuote();
        quote.id = sfQuote.id;
        quote.accountId = sfQuote.accountId;
        quote.name = sfQuote.name;
        quote.billingName = sfQuote.billingName;
        quote.description = sfQuote.description;
        quote.contactId = sfQuote.contactId;
        quote.discount = sfQuote.discount;
        quote.grandTotal = sfQuote.grandTotal;
        quote.quoteNumber = sfQuote.quoteNumber;
        quote.shippingHandling = sfQuote.shippingHandling;
        quote.status = sfQuote.status;
        quote.subtotal = sfQuote.subtotal;
        quote.tax = sfQuote.tax;
        quote.totalPrice = sfQuote.totalPrice;
        quote.pricebookId = sfQuote.pricebookId;
        quote.lineItemCount = sfQuote.lineItemCount;
        quote.opportunityId = sfQuote.opportunityId;
        if (sfQuote.createdDate != null)
            quote.createdDate = new Date(sfQuote.createdDate);
        if (sfQuote.expirationDate != null && sfQuote.expirationDate > 0)
            quote.expirationDate = DatePlus.getYYYYMMDD(sfQuote.expirationDate);
        return quote;
    }

    public static SFQuote from(Map<String, Object> entityAttributeField) {
        SFQuote quote = new SFQuote();
        quote.accountId = returnNullOrValue((String) entityAttributeField.get("Account Id"));
        quote.name = returnNullOrValue((String) entityAttributeField.get("Name"));
        quote.billingName = returnNullOrValue((String) entityAttributeField.get("Billing Name"));
        quote.description = returnNullOrValue((String) entityAttributeField.get(" Description"));
        quote.contactId = returnNullOrValue((String) entityAttributeField.get("Contact Id"));
        quote.discount = (Double) entityAttributeField.get("Discount");
        quote.grandTotal = (Double) entityAttributeField.get("Grand Total");
        quote.quoteNumber = (Double) entityAttributeField.get("Quote Number");
        quote.shippingHandling = (Double) entityAttributeField.get("Shipping Handling");
        quote.status = returnNullOrValue((String) entityAttributeField.get("Status"));
        quote.subtotal = (Double) entityAttributeField.get("Subtotal");
        quote.tax = (Double) entityAttributeField.get("Tax");
        quote.totalPrice = (Double) entityAttributeField.get("Total Price");
        quote.pricebookId = returnNullOrValue((String) entityAttributeField.get("Pricebook Id"));
        quote.lineItemCount = (Double) entityAttributeField.get("Line Item Count");
        quote.opportunityId = returnNullOrValue((String) entityAttributeField.get("Opportunity Id"));
        if (entityAttributeField.get("Created Date") != null)
            quote.createdDate = new Date((Long) entityAttributeField.get("Created Date"));
        if (entityAttributeField.get("Expiration Date") != null && (Long) entityAttributeField.get("Expiration Date") > 0)
            quote.expirationDate = DatePlus.getYYYYMMDD((Long) entityAttributeField.get("Expiration Date"));
        return quote;
    }

}
