package app.krista.extensions.crm.sales.salesforce_sales.impl;

import app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes.AccountCatalogType;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFAccount;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFAccountHistory;
import app.krista.extensions.crm.sales.salesforce_sales.service.Account;
import app.krista.extensions.crm.sales.salesforce_sales.service.AccountDao;
import app.krista.extensions.crm.sales.salesforce_sales.util.CustomFieldsSerializer;
import app.krista.extensions.crm.sales.salesforce_sales.util.SalesforceListResponse;
import com.google.gson.reflect.TypeToken;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.GSON;

@Service
public class AccountDaoImpl implements AccountDao {

    public static final String ACCOUNT = "Account";
    // Note: FIELDS(CUSTOM) retrieves ALL custom fields, so we don't need to list them explicitly
    private static final String QUERY_FIELDS = "id, createdDate, phone, type, website, Name, ownership, AccountNumber, Industry, NumberOfEmployees, fax, sic, ownerId, createdById, cleanStatus, " +
            "BillingStreet, BillingCity, BillingState, BillingCountry, BillingPostalCode, ShippingStreet, ShippingCity, ShippingState, ShippingPostalCode, ShippingCountry, AnnualRevenue, FIELDS(CUSTOM)";

    // Gson instance with custom deserializer for SFAccount to handle dynamic custom fields
    private static final com.google.gson.Gson ACCOUNT_GSON = new com.google.gson.GsonBuilder()
            .registerTypeAdapter(SFAccount.class, new app.krista.extensions.crm.sales.salesforce_sales.util.SFAccountDeserializer())
            .create();

    private final SalesforceManager salesforceManager;

    @Inject
    public AccountDaoImpl(SalesforceManager salesforceManager) {
        this.salesforceManager = salesforceManager;
    }

    @Override
    public Account getAccountById(String id) {
        return new AccountImpl(ACCOUNT_GSON.fromJson(salesforceManager.getRecordById(id, ACCOUNT), SFAccount.class));
    }

    @Override
    public List<Account> getAccountByName(String name, long size, long page) {
        // Use exact match (=) instead of LIKE to avoid matching partial names
        // Example: searching "test" won't match "test123"
        String query = "Select " + QUERY_FIELDS + " from Account where name = '" + name + "' ORDER BY Name LIMIT " + size + " OFFSET " + Math.max((page - 1) * size, 0);
        return getAccountByQuery(query);
    }

    @Override
    public List<Account> getAccounts(long size, long page) {
        String query = "Select " + QUERY_FIELDS + " from Account ORDER BY Name LIMIT " + size + " OFFSET " + Math.max((page - 1) * size, 0);
        return getAccountByQuery(query);
    }

    @Override
    public String addAccount(Map<String, Object> entityAttributeField) {
        SFAccount sfAccount = SFAccount.from(entityAttributeField);

        // Serialize account with custom fields flattened into root JSON
        String accountJson = serializeAccountWithCustomFields(sfAccount);

        System.out.println("========================================");
        System.out.println("AccountDaoImpl.addAccount() - Final JSON to Salesforce:");
        System.out.println(accountJson);
        System.out.println("========================================");

        String responseJson = salesforceManager.createRecord(accountJson, ACCOUNT);
        Map<String, Object> response = GSON.fromJson(responseJson, Map.class);
        return String.valueOf(response.get("id"));
    }

    /**
     * Serializes an SFAccount with custom fields flattened into the root JSON object.
     * Delegates to CustomFieldsSerializer utility for reusable logic.
     *
     * @param account SFAccount with customFields map
     * @return JSON string with custom fields flattened to root level
     */
    private String serializeAccountWithCustomFields(SFAccount account) {
        return CustomFieldsSerializer.serializeWithCustomFields(account, account.customFields, "Account");
    }

    @Override
    public void updateAccount(Account account) {
        if (account.getId() == null || account.getId().isEmpty())
            throw new IllegalArgumentException("Account ID is required. Please provide the Salesforce ID of the account you want to update.");
        SFAccount updatedopportunity = SFAccount.from(AccountCatalogType.fromSFAccount(account));
        SFAccount oldOpportunity = SFAccount.from(AccountCatalogType.fromSFAccount(getAccountById(updatedopportunity.id)));
        Map<String, Object> diff = salesforceManager.getDifference(oldOpportunity, updatedopportunity);
        salesforceManager.updateRecord(updatedopportunity.id, GSON.toJson(diff), ACCOUNT);
    }

    private List<Account> getAccountByQuery(String query) {
        String response = salesforceManager.getRecordByQuery(query);
        SalesforceListResponse<SFAccount> oppList =
                ACCOUNT_GSON.fromJson(response, new TypeToken<SalesforceListResponse<SFAccount>>() {
                }.getType());
        if (oppList.records.size() > 0) {
            return oppList.records.stream().map(AccountImpl::new).collect(Collectors.toList());
        } else {
            return List.of();
        }
    }

    @Override
    public Map<String, Object> getAccountUpdatedFields(String query) {
        String response = salesforceManager.getRecordByQuery(query);
        SFAccountHistory sfHistory = GSON.fromJson(response, SFAccountHistory.class);
        return sfHistory.toMap();
    }

    @Override
    public void deleteAccount(String key) {
        salesforceManager.deleteRecordById(key, ACCOUNT);
    }
}
