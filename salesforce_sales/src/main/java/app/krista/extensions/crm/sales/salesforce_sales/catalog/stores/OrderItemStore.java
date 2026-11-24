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
import app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes.OrderItemCatalogType;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.OrderItem;
import app.krista.extensions.crm.sales.salesforce_sales.impl.OrderItemDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.util.SoqlQueryHelper;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static app.krista.extensions.crm.sales.salesforce_sales.impl.OrderItemDaoImpl.QUERY_FIELDS;

/**
 * Entity store implementation for OrderItem catalog entity.
 * Handles CRUD operations and search functionality.
 */
@Service
public class OrderItemStore implements EntityStore<OrderItem> {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderItemStore.class);

    private final OrderItemDaoImpl orderItemDao;

    @Inject
    public OrderItemStore(OrderItemDaoImpl orderItemDao) {
        this.orderItemDao = orderItemDao;
    }

    @Override
    public OrderItem create(Map<String, Object> entityAttributeField) {
        if (entityAttributeField == null || entityAttributeField.isEmpty()) {
            LOGGER.info("Entity attribute field map cannot be null or empty");
            return new OrderItem();
        }
        try {
            String id = orderItemDao.addOrderItem(entityAttributeField);
            return get(id);
        } catch (IllegalArgumentException | IOException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return new OrderItem();
        }
    }

    public OrderItem create(OrderItem orderItem) throws IOException {
        try {
            LOGGER.info("Creating order item for Order ID: {}", orderItem.orderId);
            Map<String, Object> orderItemMap = OrderItemCatalogType.toMap(orderItem);
            String id = orderItemDao.addOrderItem(orderItemMap);
            orderItem.id = id;
            LOGGER.info("Successfully created order item with ID: {}", id);
            return orderItem;
        } catch (Exception e) {
            LOGGER.error("Error creating order item: {}", e.getMessage(), e);
            throw new IOException("Failed to create order item: " + e.getMessage(), e);
        }
    }
    
    @Override
    public OrderItem get(String id) throws IOException {
        if (id == null || id.isEmpty()) {
            LOGGER.info("Primary key cannot be null or empty");
            return null;
        }
        try {
            return OrderItemCatalogType.fromSFOrderItem(orderItemDao.getOrderItemById(id));
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return null;
        }
    }
    
    @Override
    public OrderItem update(OrderItem orderItem) throws IOException {
        if (orderItem == null) {
            LOGGER.info("Order item cannot be null");
            return null;
        }
        try {
            app.krista.extensions.crm.sales.salesforce_sales.service.OrderItem sfOrderItem =
                    orderItemDao.getOrderItemById(orderItem.id);
            OrderItem catalogOrderItem = OrderItemCatalogType.fromSFOrderItem(sfOrderItem);

            // Update fields
            if (orderItem.quantity != null) catalogOrderItem.quantity = orderItem.quantity;
            if (orderItem.unitPrice != null) catalogOrderItem.unitPrice = orderItem.unitPrice;
            if (orderItem.description != null) catalogOrderItem.description = orderItem.description;
            if (orderItem.serviceDate != null) catalogOrderItem.serviceDate = orderItem.serviceDate;
            if (orderItem.endDate != null) catalogOrderItem.endDate = orderItem.endDate;

            // Convert catalog entity to SFOrderItem model and wrap in OrderItemImpl
            app.krista.extensions.crm.sales.salesforce_sales.model.SFOrderItem sfModel =
                    app.krista.extensions.crm.sales.salesforce_sales.model.SFOrderItem.from(catalogOrderItem);
            app.krista.extensions.crm.sales.salesforce_sales.impl.OrderItemImpl orderItemImpl =
                    new app.krista.extensions.crm.sales.salesforce_sales.impl.OrderItemImpl(sfModel);

            orderItemDao.updateOrderItem(orderItemImpl);
            return catalogOrderItem;
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return null;
        }
    }
    
    @Override
    public void delete(String id) throws IOException {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Order Item ID is required for deletion. Please provide the Salesforce ID of the order item you want to delete.");
        }
        orderItemDao.deleteOrderItem(id);
    }
    
    @Override
    public List<OrderItem> search(SearchQuery searchQuery, long pageIndex, int pageSize) throws IOException {
        if (searchQuery == null || searchQuery.getSearchConditions() == null || searchQuery.getSearchConditions().isEmpty()) {
            return orderItemDao.getOrderItems(pageSize, pageIndex).stream().map(OrderItemCatalogType::fromSFOrderItem).collect(Collectors.toList());
        }
        List<SearchCondition> conditions = searchQuery.getSearchConditions();
        List<QueryClause> clauses = searchQuery.getQueryClauses();
        try {
            // Single Id lookup optimization
            for (SearchCondition sc : conditions) {
                if ("Id".equalsIgnoreCase(sc.getFieldName()) && sc.getOperand() != null) {
                    String id = String.valueOf(sc.getOperand()).trim();
                    if (!id.isEmpty()) {
                        OrderItem entity = get(id);
                        return entity != null ? List.of(entity) : List.of();
                    }
                }
            }
            String where = SoqlQueryHelper.buildWhereClause("orderitem", conditions);
            String orderBy = SoqlQueryHelper.buildOrderByClause("orderitem", clauses);
            String pagination = SoqlQueryHelper.buildPaginationClause(pageSize, pageIndex);

            if (where.isEmpty()) {
                return orderItemDao.getOrderItems(pageSize, pageIndex).stream().map(OrderItemCatalogType::fromSFOrderItem).collect(Collectors.toList());
            }
            String query = "SELECT " + QUERY_FIELDS + " FROM OrderItem WHERE " + where + orderBy + pagination;
            return orderItemDao.getOrderItemsByQuery(query).stream().map(OrderItemCatalogType::fromSFOrderItem).collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return List.of();
        }
    }

    @Override
    @Deprecated
    public List<OrderItem> search(List<SearchCondition> conditions, long pageIndex, int pageSize) {
        return List.of();
    }

    @Override
    public boolean contains(String s) {
        return false;
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

