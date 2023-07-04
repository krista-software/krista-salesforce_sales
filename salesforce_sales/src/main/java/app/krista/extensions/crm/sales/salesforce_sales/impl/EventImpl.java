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
