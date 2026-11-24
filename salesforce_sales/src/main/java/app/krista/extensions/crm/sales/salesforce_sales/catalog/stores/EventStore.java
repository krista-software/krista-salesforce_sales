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

package app.krista.extensions.crm.sales.salesforce_sales.catalog.stores;

import app.krista.extension.executor.QueryClause;
import app.krista.extension.executor.SearchCondition;
import app.krista.extension.executor.SearchQuery;
import app.krista.extension.util.EntityStore;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes.EventCatalogType;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Event;
import app.krista.extensions.crm.sales.salesforce_sales.impl.EventDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.impl.EventImpl;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFEvent;
import app.krista.extensions.crm.sales.salesforce_sales.util.SoqlQueryHelper;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static app.krista.extensions.crm.sales.salesforce_sales.impl.EventDaoImpl.QUERY_FIELDS;

@Service
public class EventStore implements EntityStore<Event> {

    private final EventDaoImpl eventDao;
    private static final Logger LOGGER = LoggerFactory.getLogger(EventStore.class);


    @Inject
    private EventStore(EventDaoImpl eventDao) {
        this.eventDao = eventDao;
    }

    @Override
    public Event create(Map<String, Object> entityAttributeField) {
        if (entityAttributeField == null || entityAttributeField.isEmpty()) {
            LOGGER.info("Entity attribute field map cannot be null or empty");
        }
        try {
            String eventId = eventDao.addEvent(entityAttributeField);
            return get(eventId);
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return new Event();
        }
    }

    @Override
    public Event get(String primaryKey) {
        if (primaryKey == null || primaryKey.isEmpty()) {
            LOGGER.info("Primary key cannot be null or empty");
        }
        try {
            return EventCatalogType.fromSFEvent(eventDao.getEventById(primaryKey));
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return null;
        }
    }

    @Override
    public Event update(Event event) {
        if (event == null) {
            LOGGER.info("Event cannot be null");
        }
        try {
            eventDao.updateEvent(new EventImpl(SFEvent.from(event)));
            return event;
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return null;
        }
    }

    @Override
    public void delete(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Event ID is required for deletion. Please provide the Salesforce ID of the event you want to delete.");
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
            return eventDao.getEvents(pageSize, pageIndex).stream()
                    .map(EventCatalogType::fromSFEvent)
                    .collect(Collectors.toList());
        }
        List<SearchCondition> conditions = searchQuery.getSearchConditions();
        List<QueryClause> clauses = searchQuery.getQueryClauses();
        try {
            // Single Id lookup optimization
            for (SearchCondition sc : conditions) {
                if ("Id".equalsIgnoreCase(sc.getFieldName()) && sc.getOperand() != null) {
                    String id = String.valueOf(sc.getOperand()).trim();
                    if (!id.isEmpty()) {
                        Event event = get(id);
                        return event != null ? List.of(event) : List.of();
                    }
                }
            }
            // Build SOQL using helpers
            String where = SoqlQueryHelper.buildWhereClause("event", conditions);
            String orderBy = SoqlQueryHelper.buildOrderByClause("event", clauses);
            String pagination = SoqlQueryHelper.buildPaginationClause(pageSize, pageIndex);

            if (where.isEmpty()) {
                return eventDao.getEvents(pageSize, pageIndex).stream()
                        .map(EventCatalogType::fromSFEvent)
                        .collect(Collectors.toList());
            }

            String query = "SELECT " + QUERY_FIELDS + " FROM Event WHERE " + where + orderBy + pagination;
            return eventDao.getEventByQuery(query).stream()
                    .map(EventCatalogType::fromSFEvent)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
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
}
