package app.krista.extensions.crm.sales.salesforce_sales.service;

public interface Opportunity {

    String getName();
    String getId();
    String getContactId();
    String getOwnerId();
    String getOwnerName();
    String getAccountId();
    String getAccountName();
    String getType();
    String getLeadSource();
    String getPricebookId();
    String getStageName();
    String getNextStep();
    Double getAmount();
    Double getAcv();
    String getPartner();
    String getCompetition();
    Double getProbability();
    String getDescription();
    Double getExpectedRevenue();
    Long getCreatedDate();
    Long getLastModifiedDate();
    Long getCloseDate();
}
