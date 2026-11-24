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

package app.krista.extensions.crm.sales.salesforce_sales.catalog.entities;


import app.krista.extension.impl.anno.*;

import java.util.LinkedHashMap;
import java.util.Map;

@Searchable
@Domain(id = "catEntryDomain_086f33da-aaea-4d0c-816f-3d5f20b4a273",
        name = "Sales",
        ecosystemId = "catEntryEcosystem_8a0e8ce7-6289-492f-9202-52284024280c",
        ecosystemName = "CRM",
        ecosystemVersion = "db320c64-6f6f-4c9e-8daa-41d3f796d6a6")
@Entity(name = "Event", id = "localDomainEntity_92016d5b-8398-4daa-9263-3fd93a0f0c37", primaryKey = "Id", supportStore = true, description = "Represents a calendar event in Salesforce. Owner Id references User.Id. Lead Id references Lead.Id. Account Id references Account.Id.")
public class Event {

    @Field(name = "Id", type = "Text", required = false, description = "Unique Salesforce identifier for the event.")
    public String id;

    @Searchable
    @ToString
    @Field(name = "Lead Id", type = "Text", required = false, description = "Id of the associated lead. References Lead.Id.")
    public String leadId;

    @Searchable
    @ToString
    @Field(name = "Subject", type = "Text", description = "Subject or title of the event.")
    public String subject;

    @Field(name = "Owner Id", type = "Text", required = false, description = "Salesforce User Id of the event owner. References User.Id.")
    public String ownerId;

    @Field(name = "Account Id", type = "Text", required = false, description = "Id of the associated account. References Account.Id.")
    public String accountId;

    @Field(name = "Created Date", type = "Date", required = false, description = "Date when the event was created.")
    public Long createdDate;

    @Field(name = "Is Private", type = "Switch", required = false, description = "Whether the event is private.")
    public Boolean isPrivate;

    @Field(name = "Is Group Event", type = "Switch", required = false, description = "Whether this is a group event.")
    public Boolean isGroupEvent;

    @Field(name = "Last Modified Date", type = "Date", required = false, description = "Date when the event was last modified.")
    public Long lastModifiedDate;

    @Field(name = "Location", type = "Text", required = false, description = "Location of the event.")
    public String location;

    @Field(name = "Start Date Time", type = "Date", description = "Start date and time of the event.")
    public Long startDateTime;

    @Field(name = "End Date Time", type = "Date", description = "End date and time of the event.")
    public Long endDateTime;

    public static Event fromFields(Map<String, Object> attributes) {
        Event event = new Event();
        event.id = (String) attributes.get("Id");
        event.leadId = (String) attributes.get("Lead Id");
        event.subject = (String) attributes.get("Subject");
        event.ownerId = (String) attributes.get("Owner Id");
        event.accountId = (String) attributes.get("Account Id");
        event.createdDate = (Long) attributes.get("Created Date");
        event.isPrivate = (Boolean) attributes.get("Is Private");
        event.isGroupEvent = (Boolean) attributes.get("Is Group Event");
        event.lastModifiedDate = (Long) attributes.get("Last Modified Date");
        event.location = (String) attributes.get("Location");
        event.startDateTime = (Long) attributes.get("Start Date Time");
        event.endDateTime = (Long) attributes.get("End Date Time");
        return event;
    }

    public Map<String, Object> toFields() {
        Map<String, Object> fieldMap = new LinkedHashMap<>();
        fieldMap.put("Id", id);
        fieldMap.put("Lead Id", leadId);
        fieldMap.put("Subject", subject);
        fieldMap.put("Owner Id", ownerId);
        fieldMap.put("Account Id", accountId);
        fieldMap.put("Created Date", createdDate);
        fieldMap.put("Is Private", isPrivate);
        fieldMap.put("Is Group Event", isGroupEvent);
        fieldMap.put("Last Modified Date", lastModifiedDate);
        fieldMap.put("Location", location);
        fieldMap.put("Start Date Time", startDateTime);
        fieldMap.put("End Date Time", endDateTime);
        return fieldMap;
    }

}
