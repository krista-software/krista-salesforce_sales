package app.krista.extensions.crm.sales.salesforce_sales.service;

import java.util.List;
import java.util.Map;

public interface AccountDao {

    /**
     * Returns {@link Account} object of salesforce from the account id provided.
     * @param id id of account to be searched
     * @return {@link Account} object of salesforce
     */
    Account getAccountById(String id);

    /**
     * Returns {@link List<Account>} list of object of salesforce from the account name provided.
     * @param name name of the account
     * @param size number of accounts to be fetched
     * @param page page number to be fetched
     * @return {@link List<Account>} list of object of salesforce
     * @return empty list if does not find any records
     */
    List<Account> getAccountByName(String name, long size, long page);

    /**
     * Returns {@link List<Account>} list of object of salesforce.
     * @param size number of accounts to be fetched
     * @param page page number to be fetched
     * @return {@link List<Account>} list of object of salesforce
     * @return empty list if does not find any records
     */
    List<Account> getAccounts(long size, long page);

    /**
     * Add {@link Account} to salesforce from provided input fields
     * @param entityAttributeField fields which are added in the {@link Account} object of salesforce to create
     */
    void addAccount(Map<String, Object> entityAttributeField);

    /**
     * Update {@link Account} to salesforce from provided input fields
     * @param account {@link Account} object of salesforce to be updated
     */
    void updateAccount(Account account);
}
