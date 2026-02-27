package app.krista.extensions.crm.sales.salesforce_sales.catalog;

import app.krista.extension.executor.ExtensionResponse;
import app.krista.extension.impl.anno.Attribute;
import app.krista.extension.impl.anno.CatalogRequest;
import app.krista.extension.impl.anno.Domain;
import app.krista.extension.impl.anno.Field;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes.AccountCatalogType;
import app.krista.extensions.crm.sales.salesforce_sales.impl.AccountDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.util.Constants;
import app.krista.model.base.FreeForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static app.krista.extensions.crm.sales.salesforce_sales.util.SFHelper.soqlEscape;
import static app.krista.extensions.crm.sales.salesforce_sales.util.SFHelper.upsertAccountDetails;

@Domain(id = "catEntryDomain_086f33da-aaea-4d0c-816f-3d5f20b4a273",
        name = "Sales",
        ecosystemId = "catEntryEcosystem_8a0e8ce7-6289-492f-9202-52284024280c",
        ecosystemName = "CRM",
        ecosystemVersion = "a792f45b-d24c-4c68-b51d-9d6a09c9729f")
public class AccountsManagementArea {
    private final AccountDaoImpl accountDao;
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountsManagementArea.class);


    @Inject
    private AccountsManagementArea(AccountDaoImpl accountDao) {
        this.accountDao = accountDao;
    }

    @CatalogRequest(
            id = "localDomainRequest_4c4a7002-31c1-4840-89cd-6a435f3b7bb8",
            name = "Account Change Alert",
            description = "This request will alert the user whenever you create a account or update any account.",
            area = "Accounts Management ",
            type = CatalogRequest.Type.WAIT_FOR_EVENT)
    @Field.Desc(name = "Response", type = "{ Account Id: Text, Account Name: Text, Account Fax: Text, Account Number: Text, Account Website: Text, Account Sic: Text, Account Owner Id: Text, Account Created By Id: Text, Account Clean Status: Text, Account SLA Serial Number: Text, Account Created Date: Date, Account Type: Text, Account Billing Street: Street, Account Billing City: City, Account Billing State: State, Account Billing Postal Code: PostalCode, Account Billing Country: Country, Account Shipping Street: Street, Account Shipping City: City, Account Shipping State: State, Account Shipping Postal Code: PostalCode, Account Shipping Country: Country, Account Phone: Text, Account Industry: Text, Account Number Of Employees: Number, Account Ownership: Text, Account Customer Priority: Text, Account SLA Expiration Date: Date, Account Annual Revenue: Number }", required = false)
    public Map<String, Object> accountChangeAlert(
            @Field(name = "eventName", type = "Text") String eventName,
            @Field(name = "eventData", type = "FreeForm") FreeForm eventData) {
        Map<String, Object> accountMap = new HashMap<>();
        try {
            if (eventName.equals("Account Change Alert")) {
                accountMap.put("Account Id", eventData.get("Id"));
                upsertAccountDetails(eventData, accountMap);
                updateAccountDetails(eventData, accountMap);
            }
        } catch (IllegalArgumentException cause) {
            LOGGER.error("Unable to create alert for account change due to " + cause.getMessage(), cause);
        }
        return accountMap;
    }

    @CatalogRequest(
            id = "localDomainRequest_15cb766f-ab9d-4aa6-9338-fb28cae94bf4",
            name = "Find Or Create Account",
            description = "Searches for an account by name. If found, returns it. If not found, creates a new account with the given name and returns it.",
            area = "Accounts Management ",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field.Desc(name = "Account", type = "Entity(Account)", required = false)
    public ExtensionResponse findOrCreateAccount(
            @Field.Text(name = "Account Name", required = true, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {}) String accountName) {
        try {
            List<app.krista.extensions.crm.sales.salesforce_sales.service.Account> results = accountDao.getAccountByName(accountName, 1, 1);
            if (!results.isEmpty()) {
                return new ExtensionResponse(ExtensionResponse.Result.SUCCESS, Map.of("Account", AccountCatalogType.fromSFAccount(results.get(0))), null, null, Map.of());
            }
            Map<String, Object> fields = new HashMap<>();
            fields.put("Name", accountName);
            String id = accountDao.addAccount(fields);
            return new ExtensionResponse(ExtensionResponse.Result.SUCCESS, Map.of("Account", AccountCatalogType.fromSFAccount(accountDao.getAccountById(id))), null, null, Map.of());
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return null;
        }
    }

    private void updateAccountDetails(FreeForm eventData, Map<String, Object> accountMap) {
        String accountId = eventData.get("Id") != null ? eventData.get("Id").toString() : "";
        String query = "SELECT Fields(ALL) FROM AccountHistory WHERE AccountId = '" + soqlEscape(accountId)
                + "' ORDER BY CreatedDate DESC LIMIT 20";

        Map<String, Object> fetched = accountDao.getAccountUpdatedFields(query);
        if (fetched.size() != 1 || !fetched.containsKey(Constants.CREATED)) {
            upsertAccountDetails(fetched, accountMap);
        }
    }

}
