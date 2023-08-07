package app.krista.extensions.crm.sales.salesforce_sales.catalog;


import app.krista.extension.impl.anno.CatalogRequest;
import app.krista.extension.impl.anno.Domain;
import app.krista.extension.impl.anno.Field;
import app.krista.extensions.crm.sales.salesforce_sales.impl.AccountDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.util.Constants;
import app.krista.extensions.crm.sales.salesforce_sales.util.DatePlus;
import app.krista.model.base.FreeForm;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Validators.getDoubleOrEmpty;
import static app.krista.extensions.crm.sales.salesforce_sales.util.Validators.getStringOrEmpty;

@Domain(id = "catEntryDomain_086f33da-aaea-4d0c-816f-3d5f20b4a273", name = "Sales", ecosystemId = "catEntryEcosystem_8a0e8ce7-6289-492f-9202-52284024280c", ecosystemName = "CRM", ecosystemVersion = "c93e81ed-93eb-4f29-b36c-4186cabee3d1")
public class AccountsManagementArea {
    private final AccountDaoImpl accountDao;

    @Inject
    private AccountsManagementArea(AccountDaoImpl accountDao) {
        this.accountDao = accountDao;
    }

    @CatalogRequest(id = "localDomainRequest_4c4a7002-31c1-4840-89cd-6a435f3b7bb8",
            name = "Account Change Alert",
            description = "This request is for the changes(creation and update) in the accounts.",
            area = "Accounts Management ", type = CatalogRequest.Type.WAIT_FOR_EVENT)
    @Field.Desc(name = "Response", type = "{ Account Id: Text, Account Name: Text, Account Fax: Text, Account Number: Text, Account Website: Text, Account Sic: Text, Account Owner Id: Text, Account Created By Id: Text, Account Clean Status: Text, Account SLA Serial Number: Text, Account Created Date: Date, Account Type: Text, Account Billing Street: Street, Account Billing City: City, Account Billing State: State, Account Billing Postal Code: PostalCode, Account Billing Country: Country, Account Shipping Street: Street, Account Shipping City: City, Account Shipping State: State, Account Shipping Postal Code: PostalCode, Account Shipping Country: Country, Account Phone: Text, Account Industry: Text, Account Number Of Employees: Number, Account Ownership: Text, Account Customer Priority: Text, Account SLA Expiration Date: Date, Account Annual Revenue: Number }", required = false)
    public Map<String, Object> accountChangeAlert(
            @Field(name = "eventName", type = "Text") String eventName,
            @Field(name = "eventData", type = "FreeForm") FreeForm eventData) {
        try {
            if (eventName.equals("Account Change Alert")) {
                Map<String, Object> accountMap = new HashMap<>();

                accountMap.put("Account Id", eventData.get("Id"));
                setAccountDetails(eventData, accountMap);

                updateAccountDetails(eventData, accountMap);
                return accountMap;
            }
            return Map.of();
        } catch (IllegalArgumentException cause) {
            System.err.println(cause.getMessage());
            return Map.of();
        }
    }

    private void updateAccountDetails(FreeForm eventData, Map<String, Object> accountMap) {
        String query = "SELECT Fields(ALL) FROM AccountHistory WHERE AccountId = '" + eventData.get("Id")
                + "' ORDER BY CreatedDate DESC LIMIT 20";

        Map<String, Object> fetched = accountDao.getAccountUpdatedFields(query);
        if (fetched.size() != 1 || !fetched.containsKey(Constants.CREATED)) {
            setAccountDetails(fetched, accountMap);
        }
    }

    private static void setAccountDetails(Map<String, Object> accountData, Map<String, Object> accountMap) {
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
        accountMap.put("Account Number Of Employees", getDoubleOrEmpty(accountData.get("NumberOfEmployees")));
        accountMap.put("Account Ownership", getStringOrEmpty(accountData.get("Ownership")));
        accountMap.put("Account Customer Priority", getStringOrEmpty(accountData.get("CustomerPriority__c")));
        accountMap.put("Account Annual Revenue", getDoubleOrEmpty(accountData.get("AnnualRevenue")));
        accountMap.put("Account SLA Expiration Date", accountData.get("SLAExpirationDate__c") != null
                ? Objects.requireNonNull(
                DatePlus.fromYYYYMMDD(accountData.get("SLAExpirationDate__c").toString())).getTime()
                : null);
        accountMap.put("Account Owner Id", getStringOrEmpty(accountData.get("OwnerId")));
        accountMap.put("Account Created By Id", getStringOrEmpty(accountData.get("CreatedById")));
        accountMap.put("Account Created Date", accountData.get("CreatedDate") != null
                ? Long.parseLong((String) accountData.get("CreatedDate"))
                : null);

    }
}
