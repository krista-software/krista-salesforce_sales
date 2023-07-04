package app.krista.extensions.crm.sales.salesforce_sales.catalog.stores;

import app.krista.extension.executor.SearchCondition;
import app.krista.extension.executor.SearchQuery;
import app.krista.extension.util.EntityStore;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.CatalogTypes;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Event;
import app.krista.extensions.crm.sales.salesforce_sales.impl.EventDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.impl.EventImpl;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFEvent;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EventStore implements EntityStore<Event> {

    private final EventDaoImpl eventDao;

    @Inject
    private EventStore(EventDaoImpl eventDao) {
        this.eventDao = eventDao;
    }

    @Override
    public Event create(Map<String, Object> entityAttributeField) {
        if (entityAttributeField == null || entityAttributeField.isEmpty()) {
            System.err.println("Entity attribute field map cannot be null or empty");
        }
        try {
            eventDao.addEvent(entityAttributeField);
            return Event.fromFields(entityAttributeField);
        } catch (IllegalArgumentException cause) {
            System.err.println(cause.getMessage());
            cause.printStackTrace();
            return new Event();
        }
    }

    @Override
    public Event get(String primaryKey) {
        if (primaryKey == null || primaryKey.isEmpty()) {
            System.err.println("Primary key cannot be null or empty");
        }
        try {
            return CatalogTypes.fromSFEvent(eventDao.getEventById(primaryKey));
        } catch (IllegalArgumentException cause) {
            System.err.println(cause.getMessage());
            cause.printStackTrace();
            return null;
        }
    }

    @Override
    public Event update(Event event) {
        if (event == null) {
            System.err.println("Event cannot be null");
        }
        try {
            eventDao.updateEvent(new EventImpl(SFEvent.from(event)));
            return event;
        } catch (IllegalArgumentException cause) {
            System.err.println(cause.getMessage());
            cause.printStackTrace();
            return null;
        }
    }

    @Override
    public void delete(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Delete identifier cannot be null or empty");
        }
        eventDao.deleteEvent(key);
        // not supported from API side
    }

    @Override
    public boolean contains(String s) {
        return false;
    }

    @Override
    public List<Event> search(List<SearchCondition> conditions, long pageIndex, int pageSize) {
        return List.of();
    }

    @Override
    public List<Event> search(SearchQuery searchQuery, long pageIndex, int pageSize) throws IOException {
        if (searchQuery == null || searchQuery.getSearchConditions() == null || searchQuery.getSearchConditions().isEmpty()) {
            System.err.println("Search conditions cannot be null or empty");
        }
        List<SearchCondition> conditions = searchQuery.getSearchConditions();
        try {
            List<Event> events = new ArrayList<>();
            String leadId = getConditionValue(conditions, "Lead Id");
            for (SearchCondition condition : conditions) {
                String matchPattern = condition.getOperand() != null ? String.valueOf(condition.getOperand()).trim() : "";
                String selector = condition.getFieldName();
                if ("Subject".equalsIgnoreCase(selector)) {
                    events = eventDao.getEventBySubject(matchPattern, leadId, pageSize, pageIndex).stream().map(CatalogTypes::fromSFEvent).collect(Collectors.toList());
                } else if ("Lead Id".equalsIgnoreCase(selector)) {
                    events = eventDao.getEventByLeadId(matchPattern, pageSize, pageIndex).stream().map(CatalogTypes::fromSFEvent).collect(Collectors.toList());
                }
            }
            if (conditions.size() == 1 && events.isEmpty()) {
                events = eventDao.getEvents(pageSize, pageIndex).stream().map(CatalogTypes::fromSFEvent).collect(Collectors.toList());
            }
            return events;
        } catch (IllegalArgumentException cause) {
            System.err.println(cause.getMessage());
            cause.printStackTrace();
            return List.of();
        }
    }

    @Override
    public long count(List<SearchCondition> list) {
        return 0;
    }

    @Override
    public List<String> lookup(Map<String, Object> map, List<String> list) {
        return null;
    }

    private String getConditionValue(List<SearchCondition> conditions, String fieldName) {
        for (SearchCondition con : conditions) {
            if (fieldName.equalsIgnoreCase(con.getFieldName())) {
                return con.getOperand() != null ? String.valueOf(con.getOperand()).trim() : "";
            }
        }
        return "";
    }
}
