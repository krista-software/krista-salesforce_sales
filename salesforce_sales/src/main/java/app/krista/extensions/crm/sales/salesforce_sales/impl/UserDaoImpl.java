package app.krista.extensions.crm.sales.salesforce_sales.impl;

import app.krista.extensions.crm.sales.salesforce_sales.model.Me;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFUser;
import app.krista.extensions.crm.sales.salesforce_sales.service.User;
import app.krista.extensions.crm.sales.salesforce_sales.service.UserDao;
import app.krista.extensions.crm.sales.salesforce_sales.util.SalesforceListResponse;
import com.google.gson.reflect.TypeToken;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.GSON;

@Service
public class UserDaoImpl implements UserDao {
    private static final String USER = "User";
    private final SalesforceManager salesforceManager;

    @Inject
    public UserDaoImpl(SalesforceManager salesforceManager) {
        this.salesforceManager = salesforceManager;
    }

    @Override
    public User getUserById(String id) {
        return new UserImpl(GSON.fromJson(salesforceManager.getRecordById(id, USER), SFUser.class));
    }

    @Override
    public List<User> getUsers(long size, long page) {
        return getUsersByQuery("Select id, Name, Country, CompanyName, Email, Username, Lastname, Firstname, Alias, CommunityNickname, MobilePhone from User ORDER BY Name LIMIT " + size + " OFFSET " + Math.max((page - 1) * size, 0));
    }

    @Override
    public List<User> getUsersByQuery(String query) {
        String response = salesforceManager.getRecordByQuery(query);
        SalesforceListResponse<SFUser> oppList =
                GSON.fromJson(response, new TypeToken<SalesforceListResponse<SFUser>>() {
                }.getType());
        if (oppList.records.size() > 0) {
            return oppList.records.stream().map(UserImpl::new).collect(Collectors.toList());
        } else {
            return List.of();
        }
    }

    @Override
    public User getMe() {
        String me = salesforceManager.getUserDetails();
        return new UserImpl(Me.to(GSON.fromJson(me, Me.class)));
    }

    @Override
    public void addUser(Map<String, Object> entityAttributeField) {
        salesforceManager.createRecord(GSON.toJson(SFUser.from(entityAttributeField)), USER);
    }

    public void deleteUser(String key) {
        salesforceManager.deleteRecordById(key, USER);
    }
}
