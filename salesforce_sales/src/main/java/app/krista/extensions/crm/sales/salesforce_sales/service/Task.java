package app.krista.extensions.crm.sales.salesforce_sales.service;

public interface Task {

    String getId();

    String getOwnerId();

    String getSubject();

    String getPriority();

    Long getActivityDate();

    String getDescription();

    String getStatus();

    String getWhatId();

    String getOwner();

    String getAssociatedToName();

    String getAssociatedToType();

}
