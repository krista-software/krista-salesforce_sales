package app.krista.extensions.crm.sales.salesforce_sales.util;

import java.util.Map;
import java.util.Objects;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Validators.getDoubleOrZero;
import static app.krista.extensions.crm.sales.salesforce_sales.util.Validators.getStringOrEmpty;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.*;


public class SFHelper {

    public static final String CREATED_DATE = "CreatedDate";

    private SFHelper() {
    }

    /**
     * This method is used to put entity fields in map.
     *
     * @param accountData
     * @param accountMap
     */
    public static void upsertAccountDetails(Map<String, Object> accountData, Map<String, Object> accountMap) {
        String accountName = "";
        if (accountData.containsKey("Name")) {
            accountName = accountData.get("Name").toString();
        } else if (accountData.containsKey("TextName")) {
            accountName = accountData.get("TextName").toString();
        }
        accountMap.put("Account Name", accountName);
        accountMap.put("Account Fax", getStringOrEmpty(accountData.get("Fax")));
        accountMap.put("Account Number", getStringOrEmpty(accountData.get("AccountNumber")));
        accountMap.put("Account Website", getStringOrEmpty(accountData.get("Website")));
        accountMap.put("Account Sic", getStringOrEmpty(accountData.get("Sic")));
        accountMap.put("Account Clean Status", getStringOrEmpty(accountData.get("CleanStatus")));
        accountMap.put("Account SLA Serial Number", getStringOrEmpty(accountData.get("SLASerialNumber__c")));
        accountMap.put("Account Type", getStringOrEmpty(accountData.get("Type")));
        accountMap.put("Account Billing Street", getStringOrEmpty(accountData.get("BillingStreet")));
        accountMap.put("Account Billing City", getStringOrEmpty(accountData.get("BillingCity")));
        accountMap.put("Account Billing State", getStringOrEmpty(accountData.get("BillingState")));
        accountMap.put("Account Billing Postal Code", getStringOrEmpty(accountData.get("BillingPostalCode")));
        accountMap.put("Account Billing Country", getStringOrEmpty(accountData.get("BillingCountry")));
        accountMap.put("Account Shipping Street", getStringOrEmpty(accountData.get("ShippingStreet")));
        accountMap.put("Account Shipping City", getStringOrEmpty(accountData.get("ShippingCity")));
        accountMap.put("Account Shipping State", getStringOrEmpty(accountData.get("ShippingState")));
        accountMap.put("Account Shipping Postal Code", getStringOrEmpty(accountData.get("ShippingPostalCode")));
        accountMap.put("Account Shipping Country", getStringOrEmpty(accountData.get("ShippingCountry")));
        accountMap.put("Account Phone", getStringOrEmpty(accountData.get("Phone")));
        accountMap.put("Account Industry", getStringOrEmpty(accountData.get("Industry")));
        accountMap.put("Account Number Of Employees", getDoubleOrZero(accountData.get("NumberOfEmployees")));
        accountMap.put("Account Ownership", getStringOrEmpty(accountData.get("Ownership")));
        accountMap.put("Account Customer Priority", getStringOrEmpty(accountData.get("CustomerPriority__c")));
        accountMap.put("Account Annual Revenue", getDoubleOrZero(accountData.get("AnnualRevenue")));
        accountMap.put("Account SLA Expiration Date", accountData.get("SLAExpirationDate__c") != null
                ? Objects.requireNonNull(
                DatePlus.fromYYYYMMDD(accountData.get("SLAExpirationDate__c").toString())).getTime()
                : null);
        accountMap.put("Account Owner Id", getStringOrEmpty(accountData.get("OwnerId")));
        accountMap.put("Account Created By Id", getStringOrEmpty(accountData.get("CreatedById")));
        accountMap.put("Account Created Date", accountData.get(CREATED_DATE) != null
                ? Long.parseLong((String) accountData.get(CREATED_DATE))
                : null);

    }

    /**
     * This method is used to put entity fields in map.
     *
     * @param leadData
     * @param leadMap
     */
    public static void upsertLeadDetails(Map<String, Object> leadData, Map<String, Object> leadMap) {
        leadMap.put("Lead Name", leadData.get(NAME));
        leadMap.put("Lead Title", getStringOrEmpty(leadData.get("Title")));
        leadMap.put("Lead Company", getStringOrEmpty(leadData.get("Company")));
        leadMap.put("Lead Website", getStringOrEmpty(leadData.get("Website")));
        leadMap.put("Lead Owner Id", getStringOrEmpty(leadData.get("OwnerId")));
        leadMap.put("Lead Is Converted", getStringOrEmpty(leadData.get("IsConverted")));
        leadMap.put("Lead Converted Account Id", getStringOrEmpty(leadData.get("ConvertedAccountId")));
        leadMap.put("Lead Converted Contact Id", getStringOrEmpty(leadData.get("ConvertedContactId")));
        leadMap.put("Lead Converted Opportunity Id", getStringOrEmpty(leadData.get("ConvertedOpportunityId")));
        leadMap.put("Lead Clean Status", getStringOrEmpty(leadData.get("CleanStatus")));
        leadMap.put("Lead Phone", getStringOrEmpty(leadData.get("Phone")));
        leadMap.put("Lead MobilePhone", getStringOrEmpty(leadData.get("MobilePhone")));
        leadMap.put("Lead Email", getStringOrEmpty(leadData.get("Email")));
        leadMap.put("Lead Description", getStringOrEmpty(leadData.get("Description")));
        leadMap.put("Lead Status", getStringOrEmpty(leadData.get("Status")));
        leadMap.put("Lead LeadSource", getStringOrEmpty(leadData.get("LeadSource")));
        leadMap.put("Lead Industry", getStringOrEmpty(leadData.get("Industry")));
        leadMap.put("Lead Rating", getStringOrEmpty(leadData.get("Rating")));
        leadMap.put("Lead Annual Revenue", getDoubleOrZero(leadData.get("AnnualRevenue")));
        leadMap.put("Lead Created Date", leadData.get(CREATED_DATE) != null
                ? Long.parseLong((String) leadData.get(CREATED_DATE)) : null);
        leadMap.put("Lead Converted Date", leadData.get("ConvertedDate") != null
                ? Objects.requireNonNull(DatePlus.fromYYYYMMDD(leadData.get("ConvertedDate").toString())).getTime()
                : null);
        leadMap.put("Lead Last Modified Date", leadData.get("LastModifiedDate") != null
                ? Long.parseLong((String) leadData.get("LastModifiedDate")) : null);

    }

}
