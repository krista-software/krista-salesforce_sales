package app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes;

import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Account;

public final class AccountCatalogType {

    private AccountCatalogType() {
    }

    public static Account fromSFAccount(app.krista.extensions.crm.sales.salesforce_sales.service.Account account) {
        Account newAccount = new Account();
        newAccount.id = account.getId();
        newAccount.name = account.getName();
        newAccount.fax = account.getFax();
        newAccount.accountNumber = account.getAccountNumber();
        newAccount.website = account.getWebsite();
        newAccount.sic = account.getSic();
        newAccount.ownerId = account.getOwnerId();
        newAccount.createdById = account.getCreatedById();
        newAccount.cleanStatus = account.getCleanStatus();
        newAccount.sLASerialNumber = account.getSLASerialNumber();
        newAccount.createdDate = account.getCreatedDate();
        newAccount.type = account.getType();
        newAccount.billingStreet = account.getBillingStreet();
        newAccount.billingCity = account.getBillingCity();
        newAccount.billingState = account.getBillingState();
        newAccount.billingPostalCode = account.getBillingPostalCode();
        newAccount.billingCountry = account.getBillingCountry();
        newAccount.shippingStreet = account.getShippingStreet();
        newAccount.shippingCity = account.getShippingCity();
        newAccount.shippingState = account.getShippingState();
        newAccount.shippingPostalCode = account.getShippingPostalCode();
        newAccount.shippingCountry = account.getShippingCountry();
        //ToDo implement phone number
        newAccount.phone = account.getPhone();
        newAccount.industry = account.getIndustry();
        newAccount.numberOfEmployees = account.getNumberOfEmployees();
        newAccount.ownership = account.getOwnership();
        newAccount.customerPriority = account.getCustomerPriority();
        newAccount.sLAExpirationDate = account.getSLAExpirationDate();
        newAccount.annualRevenue = account.getAnnualRevenue();
        return newAccount;
    }

}
