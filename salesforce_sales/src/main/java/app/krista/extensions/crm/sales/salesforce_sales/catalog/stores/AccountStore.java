/*
 * Salesforce Sales Extension for Krista
 * Copyright (C) 2025 Krista Software
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package app.krista.extensions.crm.sales.salesforce_sales.catalog.stores;

import app.krista.extension.executor.SearchCondition;
import app.krista.extension.executor.SearchQuery;
import app.krista.extension.util.EntityStore;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes.AccountCatalogType;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Account;
import app.krista.extensions.crm.sales.salesforce_sales.impl.AccountDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.impl.AccountImpl;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFAccount;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AccountStore implements EntityStore<Account> {

    private final AccountDaoImpl accountDao;
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountStore.class);


    @Inject
    public AccountStore(AccountDaoImpl accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public Account create(Map<String, Object> entityAttributeField) {
        if (entityAttributeField == null || entityAttributeField.isEmpty()) {
            LOGGER.warn("Entity attribute field map cannot be null or empty");
            return new Account();
        }
        try {
            String id = accountDao.addAccount(entityAttributeField);
            return get(id);
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return new Account();
        }
    }

    @Override
    public Account get(String primaryKey) {
        if (primaryKey == null || primaryKey.isEmpty()) {
            LOGGER.warn("Primary key cannot be null or empty");
            return null;
        }
        try {
            return AccountCatalogType.fromSFAccount(accountDao.getAccountById(primaryKey));
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return null;
        }
    }

    @Override
    public Account update(Account account) {
        if (account == null) {
            LOGGER.warn("Account cannot be null");
            return null;
        }
        try {
            accountDao.updateAccount(new AccountImpl(SFAccount.from(account)));
            return get(account.id);
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return null;
        }
    }

    @Override
    public void delete(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Account ID is required for deletion. Please provide the Salesforce ID of the account you want to delete.");
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
            LOGGER.info("Search conditions are null or empty, returning all accounts");
            try {
                return accountDao.getAccounts(Math.max(pageSize, 200), pageIndex).stream()
                        .map(AccountCatalogType::fromSFAccount)
                        .collect(Collectors.toList());
            } catch (IllegalArgumentException cause) {
                LOGGER.error(cause.getMessage(), cause);
                return List.of();
            }
        }
        List<SearchCondition> conditions = searchQuery.getSearchConditions();
        try {
            // If an explicit Account Id is provided, use it as the primary identifier.
            // This is the correct path for update-by-Id (e.g. rename "vtt" → "vtt123").
            for (SearchCondition condition : conditions) {
                String matchPattern = condition.getOperand() != null ? String.valueOf(condition.getOperand()).trim() : "";
                if ("Id".equalsIgnoreCase(condition.getFieldName()) && !matchPattern.isEmpty()) {
                    Account account = get(matchPattern);
                    return account != null ? List.of(account) : List.of();
                }
            }
            // Otherwise search by Name
            List<Account> accounts = new ArrayList<>();
            boolean nameConditionFound = false;
            for (SearchCondition condition : conditions) {
                String matchPattern = condition.getOperand() != null ? String.valueOf(condition.getOperand()).trim() : "";
                String selector = condition.getFieldName();
                if ("Name".equalsIgnoreCase(selector)) {
                    nameConditionFound = true;
                    if (matchPattern.isEmpty()) {
                        accounts = accountDao.getAccounts(Math.max(pageSize, 200), pageIndex).stream().map(AccountCatalogType::fromSFAccount).collect(Collectors.toList());
                    } else {
                        accounts = accountDao.getAccountByName(matchPattern, pageSize, pageIndex).stream().map(AccountCatalogType::fromSFAccount).collect(Collectors.toList());
                    }
                }
                // Unknown conditions (e.g. logicalName injected by framework) are intentionally ignored
            }
            if (!nameConditionFound) {
                return accountDao.getAccounts(Math.max(pageSize, 200), pageIndex).stream().map(AccountCatalogType::fromSFAccount).collect(Collectors.toList());
            }
            return accounts;
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return List.of();
        }
    }

    @Override
    public long count(List<SearchCondition> list) {
        return 0;
    }

    @Override
    public List<String> lookup(Map<String, Object> map, List<String> list) {
        return List.of();
    }
}
