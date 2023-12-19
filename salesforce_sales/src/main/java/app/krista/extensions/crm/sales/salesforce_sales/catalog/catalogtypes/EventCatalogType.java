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
