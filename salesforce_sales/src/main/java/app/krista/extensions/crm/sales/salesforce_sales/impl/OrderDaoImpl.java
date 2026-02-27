package app.krista.extensions.crm.sales.salesforce_sales.impl;

import app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes.OrderCatalogType;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFOrder;
import app.krista.extensions.crm.sales.salesforce_sales.service.Order;
import app.krista.extensions.crm.sales.salesforce_sales.service.OrderDao;
import app.krista.extensions.crm.sales.salesforce_sales.util.CustomFieldsSerializer;
import app.krista.extensions.crm.sales.salesforce_sales.util.SFOrderDeserializer;
import app.krista.extensions.crm.sales.salesforce_sales.util.SalesforceListResponse;
import app.krista.extensions.crm.sales.salesforce_sales.util.SFHelper;
import com.google.gson.reflect.TypeToken;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.GSON;

/**
 * Implementation of OrderDao interface.
 * Handles all CRUD operations for Salesforce Order object.
 */
@Service
public class OrderDaoImpl implements OrderDao {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderDaoImpl.class);
    
    public static final String ORDER = "Order";
    
    // Define all fields to query from Salesforce Order object
    // Note: FIELDS(CUSTOM) retrieves ALL custom fields, so we don't need to list them explicitly
    private static final String QUERY_FIELDS = "Id, OrderNumber, AccountId, EffectiveDate, Status, Description, " +
            "CustomerAuthorizedById, CompanyAuthorizedById, Type, " +
            "BillingStreet, BillingCity, BillingState, BillingPostalCode, BillingCountry, " +
            "ShippingStreet, ShippingCity, ShippingState, ShippingPostalCode, ShippingCountry, " +
            "TotalAmount, CreatedDate, CreatedById, OwnerId, FIELDS(CUSTOM)";

    // Gson instance with custom deserializer for SFOrder to handle dynamic custom fields
    private static final com.google.gson.Gson ORDER_GSON = new com.google.gson.GsonBuilder()
            .registerTypeAdapter(SFOrder.class, new SFOrderDeserializer())
            .create();
    
    private final SalesforceManager salesforceManager;
    
    @Inject
    public OrderDaoImpl(SalesforceManager salesforceManager) {
        this.salesforceManager = salesforceManager;
    }

    /**
     * Serializes SFOrder to JSON with custom fields flattened into the root object.
     * Delegates to CustomFieldsSerializer utility for reusable logic.
     *
     * @param order SFOrder instance to serialize
     * @return JSON string with custom fields flattened
     */
    private String serializeOrderWithCustomFields(SFOrder order) {
        return CustomFieldsSerializer.serializeWithCustomFields(order, order.customFields, "Order");
    }
    
    @Override
    public Order getOrderById(String id) {
        try {
            String response = salesforceManager.getRecordById(id, ORDER);
            return new OrderImpl(ORDER_GSON.fromJson(response, SFOrder.class));
        } catch (Exception e) {
            LOGGER.error("Error retrieving order by ID {}: {}", id, e.getMessage(), e);
            throw new IllegalArgumentException("Failed to retrieve order with ID: " + id + ". " + e.getMessage(), e);
        }
    }
    
    @Override
    public Order getOrderByOrderNumber(String orderNumber) {
        try {
            String query = String.format("SELECT %s FROM Order WHERE OrderNumber = '%s' LIMIT 1",
                    QUERY_FIELDS, SFHelper.soqlEscape(orderNumber));
            List<Order> orders = getOrdersByQuery(query);
            if (orders.isEmpty()) {
                throw new IllegalArgumentException("Order not found with Order Number: " + orderNumber);
            }
            return orders.get(0);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error retrieving order by order number {}: {}", orderNumber, e.getMessage(), e);
            throw new IllegalArgumentException("Failed to retrieve order with Order Number: " + orderNumber, e);
        }
    }
    
    @Override
    public List<Order> getOrdersByAccountId(String accountId, long size, long page) {
        String query = String.format("SELECT %s FROM Order WHERE AccountId = '%s' ORDER BY CreatedDate DESC LIMIT %d OFFSET %d",
                QUERY_FIELDS, SFHelper.soqlEscape(accountId), size, Math.max((page - 1) * size, 0));
        return getOrdersByQuery(query);
    }
    
    @Override
    public List<Order> getOrdersByStatus(String status, long size, long page) {
        String query = String.format("SELECT %s FROM Order WHERE Status = '%s' ORDER BY CreatedDate DESC LIMIT %d OFFSET %d",
                QUERY_FIELDS, SFHelper.soqlEscape(status), size, Math.max((page - 1) * size, 0));
        return getOrdersByQuery(query);
    }
    
    @Override
    public List<Order> getOrders(long size, long page) {
        String query = String.format("SELECT %s FROM Order ORDER BY CreatedDate DESC LIMIT %d OFFSET %d",
                QUERY_FIELDS, size, Math.max((page - 1) * size, 0));
        return getOrdersByQuery(query);
    }
    
    @Override
    public String addOrder(Map<String, Object> entityAttributeField) {
        try {
            if (entityAttributeField == null || entityAttributeField.isEmpty()) {
                throw new IllegalArgumentException("Order fields cannot be null or empty");
            }
            
            // Validate required fields
            if (!entityAttributeField.containsKey("Account Id") || entityAttributeField.get("Account Id") == null) {
                throw new IllegalArgumentException("Account Id is required to create an order");
            }
            if (!entityAttributeField.containsKey("Effective Date") || entityAttributeField.get("Effective Date") == null) {
                throw new IllegalArgumentException("Effective Date is required to create an order");
            }
            if (!entityAttributeField.containsKey("Status") || entityAttributeField.get("Status") == null) {
                throw new IllegalArgumentException("Status is required to create an order");
            }
            
            SFOrder order = SFOrder.from(entityAttributeField);

            // Serialize order with custom fields flattened into root JSON
            String orderJson = serializeOrderWithCustomFields(order);
            LOGGER.info("Creating order with JSON: {}", orderJson);

            String response = salesforceManager.createRecord(orderJson, ORDER);
            Map<String, Object> responseMap = GSON.fromJson(response, Map.class);
            String orderId = String.valueOf(responseMap.get("id"));

            LOGGER.info("Successfully created order with ID: {}", orderId);
            return orderId;
        } catch (IllegalArgumentException e) {
            LOGGER.error("Validation error creating order: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error creating order: {}", e.getMessage(), e);
            throw new IllegalArgumentException("Failed to create order: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void updateOrder(Order order) {
        try {
            if (order.getId() == null || order.getId().isEmpty()) {
                throw new IllegalArgumentException("Order ID is required. Please provide the Salesforce ID of the order you want to update.");
            }
            
            // Get current order state
            SFOrder updatedOrder = SFOrder.from(OrderCatalogType.fromSFOrder(order));
            SFOrder oldOrder = SFOrder.from(OrderCatalogType.fromSFOrder(getOrderById(updatedOrder.id)));

            // Calculate difference to send only changed fields
            Map<String, Object> diff = salesforceManager.getDifference(oldOrder, updatedOrder);

            // Add custom fields to diff if they've changed
            if (updatedOrder.customFields != null && !updatedOrder.customFields.isEmpty()) {
                for (Map.Entry<String, Object> entry : updatedOrder.customFields.entrySet()) {
                    Object oldValue = oldOrder.customFields != null ? oldOrder.customFields.get(entry.getKey()) : null;
                    Object newValue = entry.getValue();

                    // Only include if the value has changed
                    if ((oldValue == null && newValue != null) ||
                        (oldValue != null && !oldValue.equals(newValue))) {
                        diff.put(entry.getKey(), newValue);
                    }
                }
            }

            if (diff.isEmpty()) {
                LOGGER.info("No changes detected for order ID: {}", updatedOrder.id);
                return;
            }

            LOGGER.info("Updating order {} with changes: {}", updatedOrder.id, diff);
            salesforceManager.updateRecord(updatedOrder.id, GSON.toJson(diff), ORDER);
            LOGGER.info("Successfully updated order with ID: {}", updatedOrder.id);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Validation error updating order: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error updating order: {}", e.getMessage(), e);
            throw new IllegalArgumentException("Failed to update order: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteOrder(String id) {
        try {
            if (id == null || id.isEmpty()) {
                throw new IllegalArgumentException("Order ID is required for deletion");
            }
            salesforceManager.deleteRecordById(id, ORDER);
            LOGGER.info("Successfully deleted order with ID: {}", id);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error deleting order {}: {}", id, e.getMessage(), e);
            throw new IllegalArgumentException("Failed to delete order with ID: " + id, e);
        }
    }

    /**
     * Helper method to execute SOQL query and parse results.
     *
     * @param query SOQL query string
     * @return List of Order objects
     */
    private List<Order> getOrdersByQuery(String query) {
        try {
            String response = salesforceManager.getRecordByQuery(query);
            SalesforceListResponse<SFOrder> orderList = ORDER_GSON.fromJson(response,
                    new TypeToken<SalesforceListResponse<SFOrder>>() {}.getType());

            if (orderList.records != null && !orderList.records.isEmpty()) {
                return orderList.records.stream()
                        .map(OrderImpl::new)
                        .collect(Collectors.toList());
            } else {
                return List.of();
            }
        } catch (Exception e) {
            LOGGER.error("Error executing query: {}", query, e);
            return List.of();
        }
    }
}

