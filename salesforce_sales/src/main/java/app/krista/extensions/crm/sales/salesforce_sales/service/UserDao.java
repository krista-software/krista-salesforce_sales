package app.krista.extensions.crm.sales.salesforce_sales.service;

import java.util.List;
import java.util.Map;

public interface UserDao {

    /**
     * Returns {@link User} object of salesforce from given user id provided.
     *
     * @param id id of user to be searched
     * @return {@link User} object of salesforce
     */
    User getUserById(String id);

    /**
     * Returns {@link List<User>} list of object of salesforce.
     *
     * @param size number of user to be fetched
     * @param page page number to be fetched
     * @return {@link List<User>} list of object of salesforce
     * @return empty list if does not find any records
     */
    List<User> getUsers(long size, long page);

    /**
     * Returns {@link List<User>} list of object of salesforce.
     *
     * @param query query to be searched
     * @return {@link List<User>} list of object of salesforce
     * @return empty list if does not find any records
     */
    List<User> getUsersByQuery(String query);

    /**
     * Returns {@link User} object of salesforce
     *
     * @return {@link User} object of salesforce
     */
    User getMe();

    /**
     * Add {@link User} to salesforce from provided input fields
     *
     * @param entityAttributeField fields which are added in the {@link User} object of salesforce to create
     */
    void addUser(Map<String, Object> entityAttributeField);
}
