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

import app.krista.extension.executor.QueryClause;
import app.krista.extension.executor.SearchCondition;
import app.krista.extension.executor.SearchQuery;
import app.krista.extension.util.EntityStore;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes.UserCatalogType;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.User;
import app.krista.extensions.crm.sales.salesforce_sales.impl.UserDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.util.SoqlQueryHelper;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserStore implements EntityStore<User> {

    private static final String QUERY_FIELDS = "id, Name, CompanyName, Email, Username, Lastname, Firstname, Alias, CommunityNickname, MobilePhone";
    private final UserDaoImpl userDaoImpl;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserStore.class);


    @Inject
    public UserStore(UserDaoImpl userDaoImpl) {
        this.userDaoImpl = userDaoImpl;
    }

    @Override
    public User create(Map<String, Object> entityAttributeField) {
        if (entityAttributeField == null || entityAttributeField.isEmpty()) {
            LOGGER.info("Entity attribute field map cannot be null or empty");
        }
        try {
            String userId = userDaoImpl.addUser(entityAttributeField);
            return get(userId);
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return new User();
        }
    }

    @Override
    public User get(String primaryKey) {
        if (primaryKey == null || primaryKey.isEmpty()) {
            LOGGER.info("Primary key cannot be null or empty");
        }
        try {
            return UserCatalogType.fromSFUser(userDaoImpl.getUserById(primaryKey));
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return null;
        }
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public void delete(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("User ID is required for deletion. Please provide the Salesforce ID of the user you want to delete.");
        }
        userDaoImpl.deleteUser(key);
    }

    @Override
    public boolean contains(String s) {
        return false;
    }

    @Override
    public List<User> search(List<SearchCondition> conditions, long pageIndex, int pageSize) {
        return List.of();
    }

    @Override
    public List<User> search(SearchQuery searchQuery, long pageIndex, int pageSize) throws IOException {
        if (searchQuery == null || searchQuery.getSearchConditions() == null || searchQuery.getSearchConditions().isEmpty()) {
            return userDaoImpl.getUsers(pageSize, pageIndex).stream().map(UserCatalogType::fromSFUser).collect(Collectors.toList());
        }
        List<SearchCondition> conditions = searchQuery.getSearchConditions();
        List<QueryClause> clauses = searchQuery.getQueryClauses();
        try {
            // Single Id lookup optimization
            for (SearchCondition sc : conditions) {
                if ("Id".equalsIgnoreCase(sc.getFieldName()) && sc.getOperand() != null) {
                    String id = String.valueOf(sc.getOperand()).trim();
                    if (!id.isEmpty()) {
                        User entity = get(id);
                        return entity != null ? List.of(entity) : List.of();
                    }
                }
            }
            String where = SoqlQueryHelper.buildWhereClause("user", conditions);
            String orderBy = SoqlQueryHelper.buildOrderByClause("user", clauses);
            String pagination = SoqlQueryHelper.buildPaginationClause(pageSize, pageIndex);

            if (where.isEmpty()) {
                return userDaoImpl.getUsers(pageSize, pageIndex).stream().map(UserCatalogType::fromSFUser).collect(Collectors.toList());
            }
            String query = "SELECT " + QUERY_FIELDS + " FROM User WHERE " + where + orderBy + pagination;
            return userDaoImpl.getUsersByQuery(query).stream().map(UserCatalogType::fromSFUser).collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
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
