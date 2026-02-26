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
        LOGGER.info("OrderStore initialized successfully with OrderDao: {}", orderDao);
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
        LOGGER.info("OrderStore.search() called with query: {}, pageIndex: {}, pageSize: {}", searchQuery, pageIndex, pageSize);

        if (searchQuery == null || searchQuery.getSearchConditions() == null || searchQuery.getSearchConditions().isEmpty()) {
            LOGGER.info("Search conditions are null or empty, returning all orders");
            try {
                return orderDao.getOrders(Math.max(pageSize, 200), pageIndex).stream()
                        .map(OrderCatalogType::fromSFOrder)
                        .collect(Collectors.toList());
            } catch (IllegalArgumentException cause) {
                LOGGER.error(cause.getMessage(), cause);
                return List.of();
            }
        }
        List<SearchCondition> conditions = searchQuery.getSearchConditions();
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
                    orders = orderDao.getOrdersByAccountId(matchPattern, Math.max(pageSize, 200), pageIndex).stream()
                            .map(OrderCatalogType::fromSFOrder)
                            .collect(Collectors.toList());
                } else if ("Status".equalsIgnoreCase(selector)) {
                    hasNonEmptyCondition = true;
                    orders = orderDao.getOrdersByStatus(matchPattern, Math.max(pageSize, 200), pageIndex).stream()
                            .map(OrderCatalogType::fromSFOrder)
                            .collect(Collectors.toList());
                }
                // Unknown conditions (e.g. logicalName injected by framework) are intentionally ignored
            }
            if (!hasNonEmptyCondition) {
                LOGGER.info("All search conditions are empty, returning all orders");
                return orderDao.getOrders(Math.max(pageSize, 200), pageIndex).stream()
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

