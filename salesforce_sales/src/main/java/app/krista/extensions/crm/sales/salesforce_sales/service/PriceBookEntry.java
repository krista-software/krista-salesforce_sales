package app.krista.extensions.crm.sales.salesforce_sales.service;

public interface PriceBookEntry {
    String getId();

    String getName();

    String getPriceBookId();

    String getProductId();

    String getProductCode();

    Long getCreatedDate();

    Long getLastModifiedDate();

    Double getUnitPrice();
}
