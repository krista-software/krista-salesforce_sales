package app.krista.extensions.crm.sales.salesforce_sales.impl;

import app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes.EventCatalogType;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFEvent;
import app.krista.extensions.crm.sales.salesforce_sales.service.Event;
import app.krista.extensions.crm.sales.salesforce_sales.service.EventDao;
import app.krista.extensions.crm.sales.salesforce_sales.util.SalesforceListResponse;
import com.google.gson.reflect.TypeToken;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.GSON;

@Service
public class EventDaoImpl implements EventDao {

    private static final String EVENT = "Event";
    private static final String QUERY_FIELDS = "Id, WhoId, Subject, Location, StartDateTime, EndDateTime, Description, AccountId, OwnerId, IsPrivate, ShowAs, IsGroupEvent, CreatedDate, LastModifiedDate";
    private final SalesforceManager salesforceManager;

    @Inject
    public EventDaoImpl(SalesforceManager salesforceManager) {
        this.salesforceManager = salesforceManager;
    }

    @Override
    public Event getEventById(String id) {
        return getEventByQuery("Select " + QUERY_FIELDS + "  from Event where id = '" + id + "'").stream().findFirst().orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public List<Event> getEventBySubject(String subject, String leadId, long size, long page) {
        return leadId == null || leadId.isEmpty() ? getEventByQuery("Select " + QUERY_FIELDS + "  from Event where Subject LIKE '%" + subject + "%' ORDER BY LastModifiedDate DESC  LIMIT " + size + " OFFSET " + Math.max((page - 1) * size, 0)) :
                getEventByQuery("Select " + QUERY_FIELDS + "  from Event where Subject LIKE '%" + subject + "%' AND whoId = '" + leadId + "'  ORDER BY LastModifiedDate DESC  LIMIT " + size + " OFFSET " + Math.max((page - 1) * size, 0));
    }

    @Override
    public List<Event> getEventByLeadId(String leadId, long size, long page) {
        return getEventByQuery("Select " + QUERY_FIELDS + " from Event where WhoId = '" + leadId + "' ORDER BY LastModifiedDate DESC  LIMIT " + size + " OFFSET " + Math.max((page - 1) * size, 0));
    }

    @Override
    public List<Event> getEvents(long size, long page) {
        return getEventByQuery("Select " + QUERY_FIELDS + " from Event ORDER BY LastModifiedDate DESC LIMIT " + size + " OFFSET " + Math.max((page - 1) * size, 0));
    }

    @Override
    public void addEvent(Map<String, Object> entityAttributeField) {
        salesforceManager.createRecord(GSON.toJson(SFEvent.from(entityAttributeField)), EVENT);
    }

    @Override
    public void updateEvent(Event event) {
        if (event.getId() == null || event.getId().isEmpty())
            throw new IllegalArgumentException("Event Id cant be empty");
        SFEvent updatedEvent = SFEvent.from(EventCatalogType.fromSFEvent(event));
        SFEvent oldEvent = SFEvent.from(EventCatalogType.fromSFEvent(getEventById(updatedEvent.id)));
        Map<String, Object> diff = salesforceManager.getDifference(oldEvent, updatedEvent);
        salesforceManager.updateRecord(updatedEvent.id, GSON.toJson(diff), EVENT);
    }

    private List<Event> getEventByQuery(String query) {
        String res = salesforceManager.getRecordByQuery(query);
        SalesforceListResponse<SFEvent> oppList =
                GSON.fromJson(res, new TypeToken<SalesforceListResponse<SFEvent>>() {
                }.getType());
        if (oppList.records.size() > 0) {
            return oppList.records.stream().map(EventImpl::new).collect(Collectors.toList());
        } else {
            return List.of();
        }
    }

    public void deleteEvent(String key) {
        salesforceManager.deleteRecordById(key, EVENT);
    }
}
