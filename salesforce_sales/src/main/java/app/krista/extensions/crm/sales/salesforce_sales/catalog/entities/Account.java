package app.krista.extensions.crm.sales.salesforce_sales.catalog.entities;


import app.krista.extension.impl.anno.*;
import app.krista.model.base.PhoneNumber;

import java.util.Map;

@Domain(id = "catEntryDomain_086f33da-aaea-4d0c-816f-3d5f20b4a273",
        name = "Sales",
        ecosystemId = "catEntryEcosystem_8a0e8ce7-6289-492f-9202-52284024280c",
        ecosystemName = "CRM",
        ecosystemVersion = "db320c64-6f6f-4c9e-8daa-41d3f796d6a6")
@Entity(name = "Account", id = "localDomainEntity_f48532cb-46ca-44ed-9eab-462339f9a807", primaryKey = "Id", supportStore = true)
public class Account {

    @Searchable
    @Field(name = "Id", type = "Text", required = false)
    public String id;

    @Searchable
    @ToString
    @Field(name = "Name", type = "Text")
    public String name;

    @Field(name = "Fax", type = "Text", required = false)
    public String fax;

    @Field(name = "Account Number", type = "Text", required = false)
    public String accountNumber;

    @Field(name = "Website", type = "Text", required = false)
    public String website;

    @Field(name = "Sic", type = "Text", required = false)
    public String sic;

    @Field(name = "Owner Id", type = "Text", required = false)
    public String ownerId;

    @Field(name = "Created By Id", type = "Text", required = false)
    public String createdById;

    @Field(name = "Clean Status", type = "Text", required = false)
    public String cleanStatus;

    @Field(name = "SLA Serial Number", type = "Text", required = false)
    public String sLASerialNumber;

    @Field(name = "Created Date", type = "Date", required = false)
    public Long createdDate;

    @Field(name = "Type", type = "PickOne", required = false)
    public String type;

    @Field(name = "Billing Street", type = "Street", required = false)
    public String billingStreet;

    @Field(name = "Billing City", type = "City", required = false)
    public String billingCity;

    @Field(name = "Billing State", type = "State", required = false)
    public String billingState;

    @Field(name = "Billing Postal Code", type = "PostalCode", required = false)
    public String billingPostalCode;

    @Field(name = "Billing Country", type = "Country", required = false)
    public String billingCountry;

    @Field(name = "Shipping Street", type = "Street", required = false)
    public String shippingStreet;

    @Field(name = "Shipping City", type = "City", required = false)
    public String shippingCity;

    @Field(name = "Shipping State", type = "State", required = false)
    public String shippingState;

    @Field(name = "Shipping Postal Code", type = "PostalCode", required = false)
    public String shippingPostalCode;

    @Field(name = "Shipping Country", type = "Country", required = false)
    public String shippingCountry;

    @Field(name = "Phone", type = "Phone", required = false)
    public PhoneNumber phone;

    @Field(name = "Industry", type = "PickOne", required = false)
    public String industry;

    @Field(name = "Number Of Employees", type = "Number", required = false)
    public Double numberOfEmployees;

    @Field(name = "Ownership", type = "PickOne", required = false)
    public String ownership;

    @Field(name = "CustomerPriority", type = "PickOne", required = false)
    public String customerPriority;

    @Field(name = "SLA Expiration Date", type = "Date", required = false)
    public Long sLAExpirationDate;

    @Field(name = " Annual Revenue", type = "Number", required = false)
    public Double annualRevenue;

    @Field(name = "Custom Fields", type = "FreeForm", required = false)
    public Map<String, Object> customFields;

    public static Account fromFields(Map<String, Object> account) {
        Account newAccount = new Account();
        newAccount.accountNumber = (String) account.get("Account Number");
        newAccount.ownership = (String) account.get("Ownership");
        newAccount.createdDate = (Long) account.get("Created Date");
        newAccount.website = (String) account.get("Website");
        newAccount.sLAExpirationDate = (Long) account.get("SLA Expiration Date");
        newAccount.name = (String) account.get("Name");
        newAccount.industry = (String) account.get("Industry");
        newAccount.numberOfEmployees = (Double) account.get("Number Of Employees");
        newAccount.billingCity = (String) account.get("Billing City");
        newAccount.ownerId = (String) account.get("Owner Id");
        newAccount.shippingStreet = (String) account.get("Shipping Street");
        newAccount.phone = (PhoneNumber) account.get("Phone");
        newAccount.sLASerialNumber = (String) account.get("SLA Serial Number");
        newAccount.shippingState = (String) account.get("Shipping State");
        newAccount.createdById = (String) account.get("Created By Id");
        newAccount.billingState = (String) account.get("Billing State");
        newAccount.shippingCity = (String) account.get("Shipping City");
        newAccount.billingCountry = (String) account.get("Billing Country");
        newAccount.sic = (String) account.get("Sic");
        newAccount.shippingCountry = (String) account.get("Shipping Country");
        newAccount.shippingPostalCode = (String) account.get("Shipping Postal Code");
        newAccount.type = (String) account.get("Type");
        newAccount.billingStreet = (String) account.get("Billing Street");
        newAccount.billingPostalCode = (String) account.get("Billing Postal Code");
        newAccount.customerPriority = (String) account.get("CustomerPriority");
        newAccount.cleanStatus = (String) account.get("Clean Status");
        newAccount.id = (String) account.get("Id");
        newAccount.fax = (String) account.get("Fax");
        newAccount.annualRevenue = (Double) account.get(" Annual Revenue");

        // Extract custom fields from the "Custom Fields" FreeForm
        Object customFieldsObj = account.get("Custom Fields");
        if (customFieldsObj instanceof Map) {
            newAccount.customFields = (Map<String, Object>) customFieldsObj;
        }

        return newAccount;
    }

}