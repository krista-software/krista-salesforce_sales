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

public interface EventDao {

    /**
     * Returns {@link Event} object of salesforce from given event id provided.
     *
     * @param id id of event to be searched
     * @return {@link Event} object of salesforce
     */
    Event getEventById(String id);

    /**
     * Returns {@link List<Event>} list of object of salesforce from given event subject provided.
     *
     * @param subject subject of event to be searched
     * @param leadId  lead id of event to be searched
     * @param size    number of event to be fetched
     * @param page    page number to be fetched
     * @return {@link List<Event>} list of object of salesforce
     * @return empty list if does not find any records
     */
    List<Event> getEventBySubject(String subject, String leadId, long size, long page);

    /**
     * Returns {@link List<Event>} list of object of salesforce from given event lead id provided.
     *
     * @param leadId lead id of event to be searched
     * @param size   number of event to be fetched
     * @param page   page number to be fetched
     * @return {@link List<Event>} list of object of salesforce
     * @return empty list if does not find any records
     */
    List<Event> getEventByLeadId(String leadId, long size, long page);

    /**
     * Returns {@link List<Event>} list of object of salesforce.
     *
     * @param size number of event to be fetched
     * @param page page number to be fetched
     * @return {@link List<Event>} list of object of salesforce
     * @return empty list if does not find any records
     */
    List<Event> getEvents(long size, long page);

    /**
     * Add {@link Event} to salesforce from provided input fields
     *
     * @param entityAttributeField fields which are added in the {@link Event} object of salesforce to create
     */
    String addEvent(Map<String, Object> entityAttributeField);

    /**
     * Update {@link Event} to salesforce from provided input fields
     *
     * @param event {@link Event} object of salesforce to be updated
     */
    void updateEvent(Event event);
}
