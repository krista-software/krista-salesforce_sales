package app.krista.extensions.crm.sales.salesforce_sales.service;

import app.krista.model.base.PhoneNumber;

public interface Lead {
    String getId();

    String getTitle();

    String getCompany();

    String getWebsite();

    String getOwnerId();

    Boolean getIsConverted();

    String getConvertedAccountId();

    String getConvertedContactId();

    String getConvertedOpportunityId();

    String getCleanStatus();

    PhoneNumber getPhone();

    PhoneNumber getMobilePhone();

    String getEmail();

    String getDescription();

    String getStatus();

    String getLeadSource();

    String getIndustry();

    String getRating();

    Double getAnnualRevenue();

    Long getConvertedDate();

    Long getCreatedDate();

    Long getLastModifiedDate();

    String getName();
}
