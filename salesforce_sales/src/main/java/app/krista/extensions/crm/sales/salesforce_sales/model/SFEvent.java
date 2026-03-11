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

package app.krista.extensions.crm.sales.salesforce_sales.model;

import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Event;
import app.krista.extensions.crm.sales.salesforce_sales.util.DatePlus;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.Map;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Validators.returnNullOrValue;

public final class SFEvent {
    @SerializedName("AccountId")
    public String accountId;

    @SerializedName("LastModifiedDate")
    public Date lastModifiedDate;

    @SerializedName("OwnerId")
    public String ownerId;

    @SerializedName("IsPrivate")
    public Boolean isPrivate;

    @SerializedName("CreatedDate")
    public Date createdDate;

    @SerializedName("WhoId")
    public String leadId;

    @SerializedName("StartDateTime")
    public String startDateTime;

    @SerializedName("EndDateTime")
    public String endDateTime;

    @SerializedName("Id")
    public String id;

    @SerializedName("IsGroupEvent")
    public Boolean isGroupEvent;

    @SerializedName("Subject")
    public String subject;

    @SerializedName("Location")
    public String location;

    public static SFEvent from(Event sfEvent) {
        SFEvent event = new SFEvent();
        event.id = sfEvent.id;
        event.ownerId = sfEvent.ownerId;
        event.location = sfEvent.location;
        event.leadId = sfEvent.leadId;
        event.subject = sfEvent.subject;
        event.accountId = sfEvent.accountId;
        if (sfEvent.lastModifiedDate != null)
            event.lastModifiedDate = new Date(sfEvent.lastModifiedDate);
        if (sfEvent.createdDate != null)
            event.createdDate = new Date(sfEvent.createdDate);
        if (sfEvent.startDateTime != null && sfEvent.startDateTime > 0)
            event.startDateTime = DatePlus.getISODate(sfEvent.startDateTime);
        if (sfEvent.endDateTime != null && sfEvent.endDateTime > 0)
            event.endDateTime = DatePlus.getISODate(sfEvent.endDateTime);
        return event;
    }

    public static SFEvent from(Map<String, Object> entityAttributeField) {
        SFEvent event = new SFEvent();
        event.ownerId = returnNullOrValue((String) entityAttributeField.get("Owner Id"));
        event.location = returnNullOrValue((String) entityAttributeField.get("Location"));
        event.leadId = returnNullOrValue((String) entityAttributeField.get("Lead Id"));
        event.subject = returnNullOrValue((String) entityAttributeField.get("Subject"));
        event.accountId = returnNullOrValue((String) entityAttributeField.get("Account Id"));
        if (entityAttributeField.get("Last Modified Date") != null)
            event.lastModifiedDate = new Date((Long) entityAttributeField.get("Last Modified Date"));
        if (entityAttributeField.get("Created Date") != null)
            event.createdDate = new Date((Long) entityAttributeField.get("Created Date"));
        Long startDateTime = (Long) entityAttributeField.get("Start Date Time");
        if (startDateTime != null && startDateTime > 0)
            event.startDateTime = DatePlus.getISODate(startDateTime);
        Long endDateTime = (Long) entityAttributeField.get("End Date Time");
        if (endDateTime != null && endDateTime > 0)
            event.endDateTime = DatePlus.getISODate(endDateTime);
        return event;
    }

}
