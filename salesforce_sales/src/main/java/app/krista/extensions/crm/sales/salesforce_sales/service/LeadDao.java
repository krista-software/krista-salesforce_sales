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

public interface LeadDao {

    /**
     * Returns {@link Lead} object of salesforce from given lead id provided.
     *
     * @param id id of lead to be searched
     * @return {@link Lead} object of salesforce
     */
    Lead getLeadById(String id);

    /**
     * Returns {@link List<Lead>} list of object of salesforce from given lead owner id provided.
     *
     * @param ownerId owner id of lead to be searched
     * @return {@link List<Lead>} list of object of salesforce
     * @return empty list if does not find any records
     */
    List<Lead> getLeadsByOwnerId(String ownerId);

    /**
     * Returns {@link List<Lead>} list of object of salesforce from given date provided.
     *
     * @param date date from which leads are created
     * @return {@link List<Lead>} list of object of salesforce
     * @return empty list if does not find any records
     */
    List<Lead> getLeadByDate(long date);

    /**
     * Returns {@link List<Lead>} list of object of salesforce.
     *
     * @param size number of lead to be fetched
     * @param page page number to be fetched
     * @return {@link List<Lead>} list of object of salesforce
     * @return empty list if does not find any records
     */
    List<Lead> getLeads(long size, long page);

    /**
     * Returns {@link List<Lead>} list of object of salesforce.
     *
     * @param query query to be searched
     * @return {@link List<Lead>} list of object of salesforce
     * @return empty list if does not find any records
     */
    List<Lead> getLeadsByQuery(String query);

    /**
     * Add {@link Lead} to salesforce from provided input fields
     *
     * @param entityAttributeField fields which are added in the {@link Lead} object of salesforce to create
     */
    String addLead(Map<String, Object> entityAttributeField);

    /**
     * Update {@link Lead} to salesforce from provided input fields
     *
     * @param lead {@link Lead} object of salesforce to be updated
     */
    void updateLead(Lead lead);

    /**
     * This function will return updated fields for lead object.
     *
     * @param query
     * @return
     */
    Map<String, Object> getLeadsUpdatedFields(String query);

    /**
     * This function will delete lead from lead object.
     *
     * @param key
     */
    void deleteLead(String key);
}
