package app.krista.extensions.crm.sales.salesforce_sales.impl;

import app.krista.extensions.crm.sales.salesforce_sales.model.SFQuote;
import app.krista.extensions.crm.sales.salesforce_sales.service.Quote;
import app.krista.extensions.crm.sales.salesforce_sales.util.DatePlus;

public class QuoteImpl implements Quote {

    private final SFQuote sfQuote;

    public QuoteImpl(SFQuote sfQuote) {
        this.sfQuote = sfQuote;
    }

    @Override
    public String getId() {
        return sfQuote.id;
    }

    @Override
    public String getName() {
        return sfQuote.name;
    }

    @Override
    public Long getCreatedDate() {
        return sfQuote.createdDate == null ? null : sfQuote.createdDate.getTime();
    }

    @Override
    public String getOpportunityId() {
        return sfQuote.opportunityId;
    }

    @Override
    public String getPricebookId() {
        return sfQuote.pricebookId;
    }

    @Override
    public String getContactId() {
        return sfQuote.contactId;
    }

    @Override
    public Double getShippingHandling() {
        return sfQuote.shippingHandling;
    }

    @Override
    public Double getTax() {
        return sfQuote.tax;
    }

    @Override
    public Long getExpirationDate() {
        return sfQuote.expirationDate == null ? null : DatePlus.fromYYYYMMDD(sfQuote.expirationDate).getTime();
    }

    @Override
    public Double getSubtotal() {
        return sfQuote.subtotal;
    }

    @Override
    public Double getLineItemCount() {
        return sfQuote.lineItemCount;
    }

    @Override
    public String getBillingName() {
        return sfQuote.billingName;
    }

    @Override
    public String getAccountId() {
        return sfQuote.accountId;
    }

    @Override
    public Double getQuoteNumber() {
        return sfQuote.quoteNumber;
    }

    @Override
    public String getStatus() {
        return sfQuote.status;
    }

    @Override
    public String getDescription() {
        return sfQuote.description;
    }

    @Override
    public Double getTotalPrice() {
        return sfQuote.totalPrice;
    }

    @Override
    public Double getDiscount() {
        return sfQuote.discount;
    }

    @Override
    public Double getGrandTotal() {
        return sfQuote.grandTotal;
    }
}
