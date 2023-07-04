package app.krista.extensions.crm.sales.salesforce_sales.catalog.stores;

import app.krista.extension.executor.SearchCondition;
import app.krista.extension.executor.SearchQuery;
import app.krista.extension.util.EntityStore;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.CatalogTypes;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Account;
import app.krista.extensions.crm.sales.salesforce_sales.impl.AccountDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.impl.AccountImpl;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFAccount;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AccountStore implements EntityStore<Account> {

    private final AccountDaoImpl accountDao;

    @Inject
    public AccountStore(AccountDaoImpl accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public Account create(Map<String, Object> entityAttributeField) {
        if (entityAttributeField == null || entityAttributeField.isEmpty()) {
            System.err.println("Entity attribute field map cannot be null or empty");
        }
        try {
            accountDao.addAccount(entityAttributeField);
            return Account.fromFields(entityAttributeField);
        } catch (IllegalArgumentException cause) {
            System.err.println(cause.getMessage());
            cause.printStackTrace();
            return new Account();
        }
    }

    @Override
    public Account get(String primaryKey) {
        if (primaryKey == null || primaryKey.isEmpty()) {
            System.err.println("Primary key cannot be null or empty");
        }
        try {
            return CatalogTypes.fromSFAccount(accountDao.getAccountById(primaryKey));
        } catch (IllegalArgumentException cause) {
            System.err.println(cause.getMessage());
            cause.printStackTrace();
            return null;
        }
    }

    @Override
    public Account update(Account account) {
        if (account == null) {
            System.err.println("Account cannot be null");
        }
        try {
            accountDao.updateAccount(new AccountImpl(SFAccount.from(account)));
            return account;
        } catch (IllegalArgumentException cause) {
            System.err.println(cause.getMessage());
            cause.printStackTrace();
            return null;
        }
    }

    @Override
    public void delete(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Delete identifier cannot be null or empty");
        }
        accountDao.deleteAccount(key);
    }

    @Override
    public boolean contains(String s) {
        return false;
    }

    @Override
    public List<Account> search(List<SearchCondition> conditions, long pageIndex, int pageSize) {
        return List.of();
    }

    @Override
    public List<Account> search(SearchQuery searchQuery, long pageIndex, int pageSize) throws IOException {
        if (searchQuery == null || searchQuery.getSearchConditions() == null || searchQuery.getSearchConditions().isEmpty()) {
            System.err.println("Search conditions cannot be null or empty");
        }
        List<SearchCondition> conditions = searchQuery.getSearchConditions();
        try {
            List<Account> accounts = new ArrayList<>();
            for (SearchCondition condition : conditions) {
                String matchPattern = condition.getOperand() != null ? String.valueOf(condition.getOperand()).trim() : "";
                String selector = condition.getFieldName();
                if ("Name".equalsIgnoreCase(selector)) {
                    accounts = accountDao.getAccountByName(matchPattern, pageSize, pageIndex).stream().map(CatalogTypes::fromSFAccount).collect(Collectors.toList());
                }
            }
            if (conditions.size() == 1 && accounts.isEmpty()) {
                accounts = accountDao.getAccounts(pageSize, pageIndex).stream().map(CatalogTypes::fromSFAccount).collect(Collectors.toList());
            }
            return accounts;
        } catch (IllegalArgumentException cause) {
            System.err.println(cause.getMessage());
            cause.printStackTrace();
            return List.of();
        }
    }

    @Override
    public long count(List<SearchCondition> list) {
        return 0;
    }

    @Override
    public List<String> lookup(Map<String, Object> map, List<String> list) {
        return null;
    }
}
