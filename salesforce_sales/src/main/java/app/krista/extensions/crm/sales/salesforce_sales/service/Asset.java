package app.krista.extensions.crm.sales.salesforce_sales.service;

public interface Asset {
    String getId();

    String getContactId();

    String getAccountId();

    String getRootAssetId();

    Boolean getIsCompetitorProduct();

    Long getCreatedDate();

    String getCreatedById();

    Long getLastModifiedDate();

    Boolean getIsDeleted();

    String getName();

    String getOwnerId();
}
