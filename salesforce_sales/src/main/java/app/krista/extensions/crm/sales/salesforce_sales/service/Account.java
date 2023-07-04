package app.krista.extensions.crm.sales.salesforce_sales.service;

import app.krista.model.base.PhoneNumber;

public interface Account {
    String getId();
    String getName();
    String getFax();
    String getAccountNumber();
    String getWebsite();
    String getSic();
    String getOwnerId();
    String getCreatedById();
    String getCleanStatus();
    String getSLASerialNumber();
    Long getCreatedDate();
    String getType();
    String getBillingStreet();
    String getBillingCity();
    String getBillingState();
    String getBillingPostalCode();
    String getBillingCountry();
    String getShippingStreet();
    String getShippingCity();
    String getShippingState();
    String getShippingPostalCode();
    String getShippingCountry();
    PhoneNumber getPhone();
    String getIndustry();
    Double getNumberOfEmployees();
    String getOwnership();
    String getCustomerPriority();
    Long getSLAExpirationDate();
    Double getAnnualRevenue();
}
