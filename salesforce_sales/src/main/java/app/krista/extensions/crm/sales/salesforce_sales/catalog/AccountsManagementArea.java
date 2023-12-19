package app.krista.extensions.crm.sales.salesforce_sales.catalog;

import app.krista.extension.impl.anno.CatalogRequest;
import app.krista.extension.impl.anno.Domain;
import app.krista.extension.impl.anno.Field;
import app.krista.extensions.crm.sales.salesforce_sales.impl.AccountDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.util.Constants;
import app.krista.model.base.FreeForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

import static app.krista.extensions.crm.sales.salesforce_sales.util.SFHelper.upsertAccountDetails;

@Domain(id = "catEntryDomain_086f33da-aaea-4d0c-816f-3d5f20b4a273", name = "Sales", ecosystemId = "catEntryEcosystem_8a0e8ce7-6289-492f-9202-52284024280c", ecosystemName = "CRM", ecosystemVersion = "c93e81ed-93eb-4f29-b36c-4186cabee3d1")
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

    private void updateAccountDetails(FreeForm eventData, Map<String, Object> accountMap) {
        String query = "SELECT Fields(ALL) FROM AccountHistory WHERE AccountId = '" + eventData.get("Id")
                + "' ORDER BY CreatedDate DESC LIMIT 20";

        Map<String, Object> fetched = accountDao.getAccountUpdatedFields(query);
        if (fetched.size() != 1 || !fetched.containsKey(Constants.CREATED)) {
            upsertAccountDetails(fetched, accountMap);
        }
    }

}
