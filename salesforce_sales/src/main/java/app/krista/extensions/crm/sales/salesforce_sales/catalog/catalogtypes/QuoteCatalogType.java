package app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes;

import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Quote;

public final class QuoteCatalogType {

    private QuoteCatalogType() {
    }

    public static Quote fromSFQuote(app.krista.extensions.crm.sales.salesforce_sales.service.Quote quote) {
        Quote newQuote = new Quote();
        newQuote.id = quote.getId();
        newQuote.name = quote.getName();
        newQuote.createdDate = quote.getCreatedDate();
        newQuote.opportunityId = quote.getOpportunityId();
        newQuote.pricebookId = quote.getPricebookId();
        newQuote.contactId = quote.getContactId();
        newQuote.shippingHandling = quote.getShippingHandling();
        newQuote.tax = quote.getTax();
        newQuote.expirationDate = quote.getExpirationDate();
        newQuote.subtotal = quote.getSubtotal();
        newQuote.lineItemCount = quote.getLineItemCount();
        newQuote.billingName = quote.getBillingName();
        newQuote.accountId = quote.getAccountId();
        newQuote.quoteNumber = quote.getQuoteNumber();
        newQuote.status = quote.getStatus();
        newQuote.description = quote.getDescription();
        newQuote.totalPrice = quote.getTotalPrice();
        newQuote.discount = quote.getDiscount();
        newQuote.grandTotal = quote.getGrandTotal();
        return newQuote;
    }
}
