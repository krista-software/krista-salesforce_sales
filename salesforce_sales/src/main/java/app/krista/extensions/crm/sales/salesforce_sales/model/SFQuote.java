package app.krista.extensions.crm.sales.salesforce_sales.model;

import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Quote;
import app.krista.extensions.crm.sales.salesforce_sales.util.DatePlus;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.Map;

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
        quote.id = (String) entityAttributeField.get("Id");
        quote.accountId = (String) entityAttributeField.get("Account Id");
        quote.name = (String) entityAttributeField.get("Name");
        quote.billingName = (String) entityAttributeField.get("Billing Name");
        quote.description = (String) entityAttributeField.get(" Description");
        quote.contactId = (String) entityAttributeField.get("Contact Id");
        quote.discount = (Double) entityAttributeField.get("Discount");
        quote.grandTotal = (Double) entityAttributeField.get("Grand Total");
        quote.quoteNumber = (Double) entityAttributeField.get("Quote Number");
        quote.shippingHandling = (Double) entityAttributeField.get("Shipping Handling");
        quote.status = (String) entityAttributeField.get("Status");
        quote.subtotal = (Double) entityAttributeField.get("Subtotal");
        quote.tax = (Double) entityAttributeField.get("Tax");
        quote.totalPrice = (Double) entityAttributeField.get("Total Price");
        quote.pricebookId = (String) entityAttributeField.get("Pricebook Id");
        quote.lineItemCount = (Double) entityAttributeField.get("Line Item Count");
        quote.opportunityId = (String) entityAttributeField.get("Opportunity Id");
        if (entityAttributeField.get("Created Date") != null)
            quote.createdDate = new Date((Long) entityAttributeField.get("Created Date"));
        if (entityAttributeField.get("Expiration Date") != null && (Long) entityAttributeField.get("Expiration Date") > 0)
            quote.expirationDate = DatePlus.getYYYYMMDD((Long) entityAttributeField.get("Expiration Date"));
        return quote;
    }

}
