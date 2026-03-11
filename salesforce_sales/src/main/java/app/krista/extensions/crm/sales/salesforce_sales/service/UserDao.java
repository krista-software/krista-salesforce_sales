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
    String addUser(Map<String, Object> entityAttributeField);
}
