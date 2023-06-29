package app.krista.extensions.crm.sales.salesforce_sales.service;

public interface Event {
    String getId();
    String getLeadId();
    String getSubject();
    String getOwnerId();
    String getAccountId();
    Long getCreatedDate();
    Boolean getIsPrivate();
    Boolean getIsGroupEvent();
    Long getLastModifiedDate();
    String getLocation();
    Long getStartDateTime();
    Long getEndDateTime();
}
