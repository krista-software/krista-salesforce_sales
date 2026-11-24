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
import app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes.OrderCatalogType;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Order;
import app.krista.extensions.crm.sales.salesforce_sales.impl.OrderDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.impl.OrderImpl;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFOrder;
import app.krista.extensions.crm.sales.salesforce_sales.util.SoqlQueryHelper;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static app.krista.extensions.crm.sales.salesforce_sales.impl.OrderDaoImpl.QUERY_FIELDS;

/**
 * Entity Store implementation for Order catalog entity.
 * Provides CRUD operations and search functionality.
 */
@Service
public class OrderStore implements EntityStore<Order> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderStore.class);
    private final OrderDaoImpl orderDao;

    @Inject
    public OrderStore(OrderDaoImpl orderDao) {
        this.orderDao = orderDao;
    }
    
    @Override
    public Order create(Map<String, Object> entityAttributeField) {
        LOGGER.info("OrderStore.create() called with fields: {}", entityAttributeField);

        if (entityAttributeField == null || entityAttributeField.isEmpty()) {
            LOGGER.info("Entity attribute field map cannot be null or empty");
            return new Order();
        }

        try {
            String id = orderDao.addOrder(entityAttributeField);
            return get(id);
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return new Order();
        }
    }
    
    @Override
    public Order get(String primaryKey) {
        if (primaryKey == null || primaryKey.isEmpty()) {
            LOGGER.info("Primary key cannot be null or empty");
            return null;
        }
        try {
            return OrderCatalogType.fromSFOrder(orderDao.getOrderById(primaryKey));
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return null;
        }
    }
    
    @Override
    public Order update(Order order) {
        if (order == null) {
            LOGGER.info("Order cannot be null");
            return null;
        }
        try {
            orderDao.updateOrder(new OrderImpl(SFOrder.from(order)));
            return get(order.id);
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return null;
        }
    }
    
    @Override
    public void delete(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Order ID is required for deletion. Please provide the Salesforce ID of the order you want to delete.");
        }
        orderDao.deleteOrder(key);
    }
    
    @Override
    public boolean contains(String s) {
        return false;
    }
    
    @Override
    public List<Order> search(List<SearchCondition> conditions, long pageIndex, int pageSize) {
        return List.of();
    }
    
    @Override
    public List<Order> search(SearchQuery searchQuery, long pageIndex, int pageSize) throws IOException {
        if (searchQuery == null || searchQuery.getSearchConditions() == null || searchQuery.getSearchConditions().isEmpty()) {
            return orderDao.getOrders(pageSize, pageIndex).stream().map(OrderCatalogType::fromSFOrder).collect(Collectors.toList());
        }
        List<SearchCondition> conditions = searchQuery.getSearchConditions();
        List<QueryClause> clauses = searchQuery.getQueryClauses();
        try {
            // Single Id lookup optimization
            for (SearchCondition sc : conditions) {
                if ("Id".equalsIgnoreCase(sc.getFieldName()) && sc.getOperand() != null) {
                    String id = String.valueOf(sc.getOperand()).trim();
                    if (!id.isEmpty()) {
                        Order entity = get(id);
                        return entity != null ? List.of(entity) : List.of();
                    }
                }
            }
            String where = SoqlQueryHelper.buildWhereClause("order", conditions);
            String orderBy = SoqlQueryHelper.buildOrderByClause("order", clauses);
            String pagination = SoqlQueryHelper.buildPaginationClause(pageSize, pageIndex);

            if (where.isEmpty()) {
                return orderDao.getOrders(pageSize, pageIndex).stream().map(OrderCatalogType::fromSFOrder).collect(Collectors.toList());
            }
            String query = "SELECT " + QUERY_FIELDS + " FROM Order WHERE " + where + orderBy + pagination;
            return orderDao.getOrdersByQuery(query).stream().map(OrderCatalogType::fromSFOrder).collect(Collectors.toList());
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
        return List.of();
    }
}

