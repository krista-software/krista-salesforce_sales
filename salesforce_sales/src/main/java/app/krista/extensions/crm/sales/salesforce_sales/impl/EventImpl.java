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

package app.krista.extensions.crm.sales.salesforce_sales.impl;

import app.krista.extensions.crm.sales.salesforce_sales.model.SFEvent;
import app.krista.extensions.crm.sales.salesforce_sales.service.Event;
import app.krista.extensions.crm.sales.salesforce_sales.util.DatePlus;

public class EventImpl implements Event {

    private final SFEvent sfEvent;

    public EventImpl(SFEvent sfEvent) {
        this.sfEvent = sfEvent;
    }

    @Override
    public String getId() {
        return sfEvent.id;
    }

    @Override
    public String getLeadId() {
        return sfEvent.leadId;
    }

    @Override
    public String getSubject() {
        return sfEvent.subject;
    }

    @Override
    public String getOwnerId() {
        return sfEvent.ownerId;
    }

    @Override
    public String getAccountId() {
        return sfEvent.accountId;
    }

    @Override
    public Long getCreatedDate() {
        return sfEvent.createdDate != null ? sfEvent.createdDate.getTime() : null;
    }

    @Override
    public Boolean getIsPrivate() {
        return sfEvent.isPrivate;
    }

    @Override
    public Boolean getIsGroupEvent() {
        return sfEvent.isGroupEvent;
    }

    @Override
    public Long getLastModifiedDate() {
        return sfEvent.lastModifiedDate != null ? sfEvent.lastModifiedDate.getTime() : null;
    }

    @Override
    public String getLocation() {
        return sfEvent.location;
    }

    @Override
    public Long getStartDateTime() {
        return sfEvent.startDateTime != null ? DatePlus.fromYYYYMMDD(sfEvent.startDateTime).getTime() : null;
    }

    @Override
    public Long getEndDateTime() {
        return sfEvent.endDateTime != null ? DatePlus.fromYYYYMMDD(sfEvent.endDateTime).getTime() : null;
    }
}
