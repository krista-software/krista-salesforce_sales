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

package app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes;

import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Event;

public final class EventCatalogType {

    private EventCatalogType() {
    }

    public static Event fromSFEvent(app.krista.extensions.crm.sales.salesforce_sales.service.Event event) {
        Event newEvent = new Event();
        newEvent.id = event.getId();
        newEvent.leadId = event.getLeadId();
        newEvent.subject = event.getSubject();
        newEvent.ownerId = event.getOwnerId();
        newEvent.accountId = event.getAccountId();
        newEvent.createdDate = event.getCreatedDate();
        newEvent.isPrivate = event.getIsPrivate();
        newEvent.isGroupEvent = event.getIsGroupEvent();
        newEvent.lastModifiedDate = event.getLastModifiedDate();
        newEvent.location = event.getLocation();
        newEvent.startDateTime = event.getStartDateTime();
        newEvent.endDateTime = event.getEndDateTime();
        return newEvent;
    }

}
