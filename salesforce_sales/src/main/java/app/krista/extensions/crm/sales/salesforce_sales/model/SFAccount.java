package app.krista.extensions.crm.sales.salesforce_sales.model;

import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Account;
import app.krista.extensions.crm.sales.salesforce_sales.util.DatePlus;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.Map;

public final class SFAccount {
    @SerializedName("AccountNumber")
    public String accountNumber;

    @SerializedName("Ownership")
    public String ownership;

    @SerializedName("CreatedDate")
    public Date createdDate;

    @SerializedName("Website")
    public String website;

    @SerializedName("SLAExpirationDate__c")
    public String sLAExpirationDate;

    @SerializedName("Name")
    public String name;

    @SerializedName("Industry")
    public String industry;

    @SerializedName("NumberOfEmployees")
    public Double numberOfEmployees;

    @SerializedName("BillingCity")
    public String billingCity;

    @SerializedName("OwnerId")
    public String ownerId;

    @SerializedName("ShippingStreet")
    public String shippingStreet;

    @SerializedName("Phone")
    public String phone;

    @SerializedName("SLASerialNumber__c")
    public String sLASerialNumber;

    @SerializedName("ShippingState")
    public String shippingState;

    @SerializedName("CreatedById")
    public String createdById;

    @SerializedName("BillingState")
    public String billingState;

    @SerializedName("ShippingCity")
    public String shippingCity;

    @SerializedName("BillingCountry")
    public String billingCountry;

    @SerializedName("Sic")
    public String sic;

    @SerializedName("ShippingCountry")
    public String shippingCountry;

    @SerializedName("ShippingPostalCode")
    public String shippingPostalCode;

    @SerializedName("Type")
    public String type;

    @SerializedName("BillingStreet")
    public String billingStreet;

    @SerializedName("BillingPostalCode")
    public String billingPostalCode;

    @SerializedName("CustomerPriority__c")
    public String customerPriority;

    @SerializedName("CleanStatus")
    public String cleanStatus;

    @SerializedName("Id")
    public String id;

    @SerializedName("Fax")
    public String fax;

    @SerializedName("AnnualRevenue")
    public Double annualRevenue;

    public static SFAccount from(Map<String, Object> entityAttributeField) {
        SFAccount account = new SFAccount();
        account.accountNumber = (String) entityAttributeField.get("Account Number");
        account.ownership = (String) entityAttributeField.get("Ownership");
        Long createdDate = (Long) entityAttributeField.get("Created Date");
        account.createdDate = createdDate != null ? new Date(createdDate) : null;
        account.website = (String) entityAttributeField.get("Website");
        Long sLAExpirationDate = (Long) entityAttributeField.get("SLA Expiration Date");
        account.sLAExpirationDate = sLAExpirationDate != null && sLAExpirationDate > 0 ? DatePlus.getYYYYMMDD(sLAExpirationDate) : null;
        account.name = (String) entityAttributeField.get("Name");
        account.industry = (String) entityAttributeField.get("Industry");
        account.numberOfEmployees = (Double) entityAttributeField.get("Number Of Employees");
        account.billingCity = (String) entityAttributeField.get("Billing City");
        account.ownerId = (String) entityAttributeField.get("Owner Id");
        account.shippingStreet = (String) entityAttributeField.get("Shipping Street");
        account.phone = entityAttributeField.get("Phone") != null ? entityAttributeField.get("Phone").toString() : "";

        account.sLASerialNumber = (String) entityAttributeField.get("SLA Serial Number");
        account.shippingState = (String) entityAttributeField.get("Shipping State");
        account.createdById = (String) entityAttributeField.get("Created By Id");
        account.billingState = (String) entityAttributeField.get("Billing State");
        account.shippingCity = (String) entityAttributeField.get("Shipping City");
        account.billingCountry = (String) entityAttributeField.get("Billing Country");
        account.sic = (String) entityAttributeField.get("Sic");
        account.shippingCountry = (String) entityAttributeField.get("Shipping Country");
        account.shippingPostalCode = (String) entityAttributeField.get("Shipping Postal Code");
        account.type = (String) entityAttributeField.get("Type");
        account.billingStreet = (String) entityAttributeField.get("Billing Street");
        account.billingPostalCode = (String) entityAttributeField.get("Billing Postal Code");
        account.customerPriority = (String) entityAttributeField.get("CustomerPriority");
        account.cleanStatus = (String) entityAttributeField.get("Clean Status");
        account.id = (String) entityAttributeField.get("Id");
        account.fax = (String) entityAttributeField.get("Fax");
        account.annualRevenue = (Double) entityAttributeField.get(" Annual Revenue");
        return account;
    }

    public static SFAccount from(Account sfAccount) {
        SFAccount account = new SFAccount();
        account.accountNumber = sfAccount.accountNumber;
        account.ownership = sfAccount.ownership;
        if (sfAccount.createdDate != null)
            account.createdDate = new Date(sfAccount.createdDate);
        account.website = sfAccount.website;
        if (sfAccount.sLAExpirationDate != null && sfAccount.sLAExpirationDate > 0)
            account.sLAExpirationDate = DatePlus.getYYYYMMDD(sfAccount.sLAExpirationDate);
        account.name = sfAccount.name;
        account.industry = sfAccount.industry;
        account.numberOfEmployees = sfAccount.numberOfEmployees;
        account.billingCity = sfAccount.billingCity;
        account.ownerId = sfAccount.ownerId;
        account.shippingStreet = sfAccount.shippingStreet;
        account.phone = sfAccount.phone != null ? sfAccount.phone.toString() : "";
        account.sLASerialNumber = sfAccount.sLASerialNumber;
        account.shippingState = sfAccount.shippingState;
        account.createdById = sfAccount.createdById;
        account.billingState = sfAccount.billingState;
        account.shippingCity = sfAccount.shippingCity;
        account.billingCountry = sfAccount.billingCountry;
        account.sic = sfAccount.sic;
        account.shippingCountry = sfAccount.shippingCountry;
        account.shippingPostalCode = sfAccount.shippingPostalCode;
        account.type = sfAccount.type;
        account.billingStreet = sfAccount.billingStreet;
        account.billingPostalCode = sfAccount.billingPostalCode;
        account.customerPriority = sfAccount.customerPriority;
        account.cleanStatus = sfAccount.cleanStatus;
        account.id = sfAccount.id;
        account.fax = sfAccount.fax;
        account.annualRevenue = sfAccount.annualRevenue;
        return account;
    }
}
