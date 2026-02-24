package app.krista.extensions.crm.sales.salesforce_sales.catalog.stores;

import app.krista.extension.executor.SearchCondition;
import app.krista.extension.executor.SearchQuery;
import app.krista.extension.util.EntityStore;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes.OrderItemCatalogType;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.OrderItem;
import app.krista.extensions.crm.sales.salesforce_sales.impl.OrderItemDaoImpl;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
            LOGGER.info("Search conditions are null or empty, returning all order items");
            try {
                return orderItemDao.getOrderItems(pageSize, pageIndex).stream()
                        .map(OrderItemCatalogType::fromSFOrderItem)
                        .collect(Collectors.toList());
            } catch (IllegalArgumentException cause) {
                LOGGER.error(cause.getMessage(), cause);
                return List.of();
            }
        }
        List<SearchCondition> conditions = searchQuery.getSearchConditions();
        try {
            List<OrderItem> orderItems = new ArrayList<>();
            boolean hasNonEmptyCondition = false;
            for (SearchCondition condition : conditions) {
                String matchPattern = condition.getOperand() != null ? String.valueOf(condition.getOperand()).trim() : "";
                String selector = condition.getFieldName();

                if (matchPattern.isEmpty()) {
                    continue;
                }

                hasNonEmptyCondition = true;

                if ("Order Id".equalsIgnoreCase(selector)) {
                    orderItems = orderItemDao.getOrderItemsByOrderId(matchPattern, pageSize, pageIndex).stream()
                            .map(OrderItemCatalogType::fromSFOrderItem)
                            .collect(Collectors.toList());
                } else if ("Product Id".equalsIgnoreCase(selector)) {
                    orderItems = orderItemDao.getOrderItemsByProductId(matchPattern, pageSize, pageIndex).stream()
                            .map(OrderItemCatalogType::fromSFOrderItem)
                            .collect(Collectors.toList());
                } else if ("Id".equalsIgnoreCase(selector)) {
                    app.krista.extensions.crm.sales.salesforce_sales.service.OrderItem orderItem = orderItemDao.getOrderItemById(matchPattern);
                    if (orderItem != null) {
                        orderItems.add(OrderItemCatalogType.fromSFOrderItem(orderItem));
                    }
                }
            }
            if (!hasNonEmptyCondition) {
                LOGGER.info("All search conditions are empty, returning all order items");
                return orderItemDao.getOrderItems(pageSize, pageIndex).stream()
                        .map(OrderItemCatalogType::fromSFOrderItem)
                        .collect(Collectors.toList());
            }
            return orderItems;
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return List.of();
        }
    }

    @Override
    @Deprecated
    public List<OrderItem> search(List<SearchCondition> conditions, long pageIndex, int pageSize) {
        return List.of();
    }
    
    public List<OrderItem> search(List<SearchCondition> conditions, long pageIndex, long pageSize) {
        return search(conditions, pageIndex, (int) pageSize);
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
        return null;
    }
}

