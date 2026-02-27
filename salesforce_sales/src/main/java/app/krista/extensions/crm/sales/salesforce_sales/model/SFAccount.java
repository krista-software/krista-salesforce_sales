package app.krista.extensions.crm.sales.salesforce_sales.model;

import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Account;
import app.krista.extensions.crm.sales.salesforce_sales.util.CustomFieldsExtractor;
import app.krista.extensions.crm.sales.salesforce_sales.util.DatePlus;
import app.krista.model.base.FreeForm;
import com.google.gson.annotations.SerializedName;

import java.util.*;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Validators.returnNullOrValue;

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

    // Dynamic custom fields map (not serialized directly, handled separately)
    public Map<String, Object> customFields = new HashMap<>();

    public static SFAccount from(Map<String, Object> entityAttributeField) {
        SFAccount account = new SFAccount();
        account.accountNumber = returnNullOrValue((String) entityAttributeField.get("Account Number"));
        account.ownership = returnNullOrValue((String) entityAttributeField.get("Ownership"));
        Long createdDate = (Long) entityAttributeField.get("Created Date");
        account.createdDate = createdDate != null ? new Date(createdDate) : null;
        account.website = returnNullOrValue((String) entityAttributeField.get("Website"));
        Long sLAExpirationDate = (Long) entityAttributeField.get("SLA Expiration Date");
        account.sLAExpirationDate = sLAExpirationDate != null && sLAExpirationDate > 0 ? DatePlus.getYYYYMMDD(sLAExpirationDate) : null;
        account.name = returnNullOrValue((String) entityAttributeField.get("Name"));
        account.industry = returnNullOrValue((String) entityAttributeField.get("Industry"));
        account.numberOfEmployees = (Double) entityAttributeField.get("Number Of Employees");
        account.billingCity = returnNullOrValue((String) entityAttributeField.get("Billing City"));
        account.ownerId = returnNullOrValue((String) entityAttributeField.get("Owner Id"));
        account.shippingStreet = returnNullOrValue((String) entityAttributeField.get("Shipping Street"));
        account.phone = entityAttributeField.get("Phone") != null ? entityAttributeField.get("Phone").toString() : "";
        account.sLASerialNumber = returnNullOrValue((String) entityAttributeField.get("SLA Serial Number"));
        account.shippingState = returnNullOrValue((String) entityAttributeField.get("Shipping State"));
        account.createdById = returnNullOrValue((String) entityAttributeField.get("Created By Id"));
        account.billingState = returnNullOrValue((String) entityAttributeField.get("Billing State"));
        account.shippingCity = returnNullOrValue((String) entityAttributeField.get("Shipping City"));
        account.billingCountry = returnNullOrValue((String) entityAttributeField.get("Billing Country"));
        account.sic = returnNullOrValue((String) entityAttributeField.get("Sic"));
        account.shippingCountry = returnNullOrValue((String) entityAttributeField.get("Shipping Country"));
        account.shippingPostalCode = returnNullOrValue((String) entityAttributeField.get("Shipping Postal Code"));
        account.type = returnNullOrValue((String) entityAttributeField.get("Type"));
        account.billingStreet = returnNullOrValue((String) entityAttributeField.get("Billing Street"));
        account.billingPostalCode = returnNullOrValue((String) entityAttributeField.get("Billing Postal Code"));
        account.customerPriority = returnNullOrValue((String) entityAttributeField.get("CustomerPriority"));
        account.cleanStatus = returnNullOrValue((String) entityAttributeField.get("Clean Status"));
        account.fax = returnNullOrValue((String) entityAttributeField.get("Fax"));
        account.annualRevenue = (Double) entityAttributeField.get(" Annual Revenue");

        // Extract custom fields
        account.customFields = extractCustomFields(entityAttributeField);

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

    /**
     * Extracts custom fields from the entity attribute field map.
     * Delegates to CustomFieldsExtractor utility for reusable logic.
     *
     * @param entityAttributeField Map containing all field values
     * @return Map of custom fields with Salesforce API names and converted values
     */
    private static Map<String, Object> extractCustomFields(Map<String, Object> entityAttributeField) {
        // List of standard Account fields (should NOT be treated as custom fields)
        Set<String> standardFields = new HashSet<>(Arrays.asList(
            "Id", "Name", "Account Number", "Ownership", "Created Date", "Website",
            "SLA Expiration Date", "Industry", "Number Of Employees", "Billing City",
            "Owner Id", "Shipping Street", "Phone", "SLA Serial Number", "Shipping State",
            "Created By Id", "Billing State", "Shipping City", "Billing Country", "Sic",
            "Shipping Country", "Shipping Postal Code", "Type", "Billing Street",
            "Billing Postal Code", "CustomerPriority", "Clean Status", "Fax", " Annual Revenue"
        ));

        return CustomFieldsExtractor.extractCustomFields(entityAttributeField, standardFields, "SFAccount");
    }
}
