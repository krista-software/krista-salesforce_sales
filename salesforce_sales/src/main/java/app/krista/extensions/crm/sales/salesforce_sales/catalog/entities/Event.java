package app.krista.extensions.crm.sales.salesforce_sales.catalog.entities;


import app.krista.extension.impl.anno.*;

import java.util.Map;

@Domain(id = "catEntryDomain_086f33da-aaea-4d0c-816f-3d5f20b4a273",
        name = "Sales",
        ecosystemId = "catEntryEcosystem_8a0e8ce7-6289-492f-9202-52284024280c",
        ecosystemName = "CRM",
        ecosystemVersion = "db320c64-6f6f-4c9e-8daa-41d3f796d6a6")
@Entity(name = "Event", id = "localDomainEntity_92016d5b-8398-4daa-9263-3fd93a0f0c37", primaryKey = "Id", supportStore = false)
public class Event {

    @Field(name = "Id", type = "Text", required = false)
    public String id;

    @Searchable
    @ToString
    @Field(name = "Lead Id", type = "Text", required = false)
    public String leadId;

    @Searchable
    @ToString
    @Field(name = "Subject", type = "Text")
    public String subject;

    @Field(name = "Owner Id", type = "Text", required = false)
    public String ownerId;

    @Field(name = "Account Id", type = "Text", required = false)
    public String accountId;

    @Field(name = "Created Date", type = "Date", required = false)
    public Long createdDate;

    @Field(name = "Is Private", type = "Switch", required = false)
    public Boolean isPrivate;

    @Field(name = "Is Group Event", type = "Switch", required = false)
    public Boolean isGroupEvent;

    @Field(name = "Last Modified Date", type = "Date", required = false)
    public Long lastModifiedDate;

    @Field(name = "Location", type = "Text", required = false)
    public String location;

    @Field(name = "Start Date Time", type = "Date")
    public Long startDateTime;

    @Field(name = "End Date Time", type = "Date")
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

}