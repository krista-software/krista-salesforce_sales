package app.krista.extensions.crm.sales.salesforce_sales.catalog.stores;

import app.krista.extension.executor.SearchCondition;
import app.krista.extension.executor.SearchQuery;
import app.krista.extension.util.EntityStore;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes.OrderCatalogType;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Order;
import app.krista.extensions.crm.sales.salesforce_sales.impl.OrderDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.impl.OrderImpl;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFOrder;
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
        System.out.println("========================================");
        System.out.println("OrderStore INITIALIZED");
        System.out.println("OrderStore instance: " + this);
        System.out.println("OrderDao instance: " + orderDao);
        System.out.println("========================================");
        LOGGER.info("OrderStore initialized successfully with OrderDao: {}", orderDao);
    }
    
    @Override
    public Order create(Map<String, Object> entityAttributeField) {
        System.out.println("========================================");
        System.out.println("OrderStore.create() CALLED");
        System.out.println("Input fields: " + entityAttributeField);
        System.out.println("========================================");
        LOGGER.info("OrderStore.create() called with fields: {}", entityAttributeField);

        if (entityAttributeField == null || entityAttributeField.isEmpty()) {
            System.out.println("ERROR: Entity attribute field map is null or empty");
            LOGGER.info("Entity attribute field map cannot be null or empty");
            return new Order();
        }

        // Debug: Check what "Custom Fields" contains
        Object customFieldsObj = entityAttributeField.get("Custom Fields");
        System.out.println("========================================");
        System.out.println("DEBUG: Custom Fields value");
        System.out.println("Custom Fields object: " + customFieldsObj);
        System.out.println("Custom Fields type: " + (customFieldsObj != null ? customFieldsObj.getClass().getName() : "null"));
        if (customFieldsObj != null) {
            System.out.println("Custom Fields toString: " + customFieldsObj.toString());
        }
        System.out.println("========================================");

        try {
            System.out.println("Calling orderDao.addOrder()...");
            String id = orderDao.addOrder(entityAttributeField);
            System.out.println("Order created with ID: " + id);
            return get(id);
        } catch (IllegalArgumentException cause) {
            System.out.println("ERROR creating order: " + cause.getMessage());
            cause.printStackTrace();
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
        System.out.println("========================================");
        System.out.println("OrderStore.search() CALLED");
        System.out.println("SearchQuery: " + searchQuery);
        System.out.println("PageIndex: " + pageIndex + ", PageSize: " + pageSize);
        System.out.println("========================================");
        LOGGER.info("OrderStore.search() called with query: {}, pageIndex: {}, pageSize: {}", searchQuery, pageIndex, pageSize);

        if (searchQuery == null || searchQuery.getSearchConditions() == null || searchQuery.getSearchConditions().isEmpty()) {
            System.out.println("WARNING: Search conditions are null or empty, returning all orders");
            LOGGER.info("Search conditions are null or empty, returning all orders");
            try {
                return orderDao.getOrders(pageSize, pageIndex).stream()
                        .map(OrderCatalogType::fromSFOrder)
                        .collect(Collectors.toList());
            } catch (IllegalArgumentException cause) {
                LOGGER.error(cause.getMessage(), cause);
                return List.of();
            }
        }
        List<SearchCondition> conditions = searchQuery.getSearchConditions();
        System.out.println("Search conditions: " + conditions);
        try {
            List<Order> orders = new ArrayList<>();
            boolean hasNonEmptyCondition = false;
            for (SearchCondition condition : conditions) {
                String matchPattern = condition.getOperand() != null ? String.valueOf(condition.getOperand()).trim() : "";
                String selector = condition.getFieldName();

                if (matchPattern.isEmpty()) {
                    continue;
                }

                if ("Order Number".equalsIgnoreCase(selector)) {
                    hasNonEmptyCondition = true;
                    app.krista.extensions.crm.sales.salesforce_sales.service.Order order = orderDao.getOrderByOrderNumber(matchPattern);
                    if (order != null) {
                        orders.add(OrderCatalogType.fromSFOrder(order));
                    }
                } else if ("Account Id".equalsIgnoreCase(selector)) {
                    hasNonEmptyCondition = true;
                    orders = orderDao.getOrdersByAccountId(matchPattern, pageSize, pageIndex).stream()
                            .map(OrderCatalogType::fromSFOrder)
                            .collect(Collectors.toList());
                } else if ("Status".equalsIgnoreCase(selector)) {
                    hasNonEmptyCondition = true;
                    orders = orderDao.getOrdersByStatus(matchPattern, pageSize, pageIndex).stream()
                            .map(OrderCatalogType::fromSFOrder)
                            .collect(Collectors.toList());
                }
                // Unknown conditions (e.g. logicalName injected by framework) are intentionally ignored
            }
            if (!hasNonEmptyCondition) {
                LOGGER.info("All search conditions are empty, returning all orders");
                return orderDao.getOrders(pageSize, pageIndex).stream()
                        .map(OrderCatalogType::fromSFOrder)
                        .collect(Collectors.toList());
            }
            return orders;
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
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

