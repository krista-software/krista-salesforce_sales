package app.krista.extensions.crm.sales.salesforce_sales.catalog.stores;

import app.krista.extension.executor.SearchCondition;
import app.krista.extension.executor.SearchQuery;
import app.krista.extension.util.EntityStore;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes.UserCatalogType;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.User;
import app.krista.extensions.crm.sales.salesforce_sales.impl.UserDaoImpl;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.AND;

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
            throw new IllegalArgumentException("Delete identifier cannot be null or empty");
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
            LOGGER.info("Search conditions cannot be null or empty");
        }
        List<SearchCondition> conditions = searchQuery.getSearchConditions();
        try {
            String name = getStringConditionValue(conditions, "Name");
            String userName = getStringConditionValue(conditions, "User Name");
            String email = getStringConditionValue(conditions, "Email");
            List<User> users;
            if (conditions.size() == 1) {
                users = userDaoImpl.getUsers(pageSize, pageIndex).stream().map(UserCatalogType::fromSFUser).collect(Collectors.toList());
            } else {
                String query = createQuery(name, userName, email, pageSize, pageIndex);
                users = userDaoImpl.getUsersByQuery(query).stream().map(UserCatalogType::fromSFUser).collect(Collectors.toList());
            }
            return users;
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return null;
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

    private String getStringConditionValue(List<SearchCondition> conditions, String fieldName) {
        for (SearchCondition con : conditions) {
            if (fieldName.equalsIgnoreCase(con.getFieldName())) {
                return con.getOperand() != null ? String.valueOf(con.getOperand()).trim() : "";
            }
        }
        return "";
    }

    private String createQuery(String name, String userName, String email, long size, long page) {
        StringBuilder builder = new StringBuilder();
        builder.append("Select " + QUERY_FIELDS + " from User WHERE").
                append((isAvailable(name) ? " name LIKE '%" + name + "%'" + AND : "")).
                append((isAvailable(userName) ? " userName LIKE '%" + userName + "%'" + AND : "")).
                append((isAvailable(email) ? " email LIKE '%" + email + "%'" + AND : ""));
        if (builder.substring(builder.length() - "WHERE".length()).equals("WHERE"))
            builder.setLength(builder.lastIndexOf("WHERE"));
        if (builder.substring(builder.length() - AND.length()).equals(AND))
            builder.setLength(builder.lastIndexOf(AND));
        builder.append(" ORDER BY LastModifiedDate LIMIT ").append(size).append(" OFFSET ").append(Math.max((page - 1) * size, 0));
        return builder.toString();
    }

    private static boolean isAvailable(String s) {
        return s != null && !s.isBlank();
    }
}
