package app.krista.extensions.crm.sales.salesforce_sales.service;

public interface Quote {
    String getId();

    String getName();

    Long getCreatedDate();

    String getOpportunityId();

    String getPricebookId();

    String getContactId();

    Double getShippingHandling();

    Double getTax();

    Long getExpirationDate();

    Double getSubtotal();

    Double getLineItemCount();

    String getBillingName();

    String getAccountId();

    Double getQuoteNumber();

    String getStatus();

    String getDescription();

    Double getTotalPrice();

    Double getDiscount();

    Double getGrandTotal();
}
