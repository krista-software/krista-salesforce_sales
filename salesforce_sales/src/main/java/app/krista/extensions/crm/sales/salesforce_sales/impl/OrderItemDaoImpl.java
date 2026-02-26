package app.krista.extensions.crm.sales.salesforce_sales.impl;

import app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes.OrderItemCatalogType;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFOrderItem;
import app.krista.extensions.crm.sales.salesforce_sales.service.OrderItem;
import app.krista.extensions.crm.sales.salesforce_sales.service.OrderItemDao;
import app.krista.extensions.crm.sales.salesforce_sales.util.CustomFieldsSerializer;
import app.krista.extensions.crm.sales.salesforce_sales.util.SFOrderItemDeserializer;
import app.krista.extensions.crm.sales.salesforce_sales.util.SalesforceListResponse;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.GSON;

/**
 * Implementation of OrderItemDao interface.
 * Handles all CRUD operations for Salesforce OrderItem (Order Product) object.
 */
@Service
public class OrderItemDaoImpl implements OrderItemDao {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderItemDaoImpl.class);
    private static final String ORDER_ITEM = "OrderItem";
    
    // Define all fields to query from Salesforce OrderItem object
    // Note: FIELDS(CUSTOM) retrieves ALL custom fields, so we don't need to list them explicitly
    private static final String QUERY_FIELDS = "Id, OrderId, PricebookEntryId, Product2Id, " +
            "Quantity, UnitPrice, TotalPrice, ListPrice, Description, OrderItemNumber, " +
            "ServiceDate, EndDate, CreatedDate, CreatedById, LastModifiedDate, LastModifiedById, FIELDS(CUSTOM)";

    // Gson instance with custom deserializer for SFOrderItem to handle dynamic custom fields
    private static final com.google.gson.Gson ORDERITEM_GSON = new com.google.gson.GsonBuilder()
            .registerTypeAdapter(SFOrderItem.class, new SFOrderItemDeserializer())
            .create();

    private final SalesforceManager salesforceManager;
    
    @Inject
    public OrderItemDaoImpl(SalesforceManager salesforceManager) {
        this.salesforceManager = salesforceManager;
    }
    
    @Override
    public OrderItem getOrderItemById(String id) {
        try {
            String response = salesforceManager.getRecordById(id, ORDER_ITEM);
            return new OrderItemImpl(ORDERITEM_GSON.fromJson(response, SFOrderItem.class));
        } catch (Exception e) {
            LOGGER.error("Error retrieving order item by ID {}: {}", id, e.getMessage(), e);
            throw new IllegalArgumentException("Failed to retrieve order item with ID: " + id + ". " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<OrderItem> getOrderItemsByOrderId(String orderId, long size, long page) {
        String query = String.format("SELECT %s FROM OrderItem WHERE OrderId = '%s' ORDER BY CreatedDate DESC LIMIT %d OFFSET %d",
                QUERY_FIELDS, orderId, size, Math.max((page - 1) * size, 0));
        return getOrderItemsByQuery(query);
    }
    
    @Override
    public List<OrderItem> getOrderItemsByProductId(String productId, long size, long page) {
        String query = String.format("SELECT %s FROM OrderItem WHERE Product2Id = '%s' ORDER BY CreatedDate DESC LIMIT %d OFFSET %d",
                QUERY_FIELDS, productId, size, Math.max((page - 1) * size, 0));
        return getOrderItemsByQuery(query);
    }
    
    @Override
    public List<OrderItem> getOrderItems(long size, long page) {
        String query = String.format("SELECT %s FROM OrderItem ORDER BY CreatedDate DESC LIMIT %d OFFSET %d",
                QUERY_FIELDS, size, Math.max((page - 1) * size, 0));
        return getOrderItemsByQuery(query);
    }
    
    @Override
    public String addOrderItem(Map<String, Object> entityAttributeField) {
        try {
            if (entityAttributeField == null || entityAttributeField.isEmpty()) {
                throw new IllegalArgumentException("Order item fields cannot be null or empty");
            }
            
            // Validate required fields
            if (!entityAttributeField.containsKey("Order Id") || entityAttributeField.get("Order Id") == null) {
                throw new IllegalArgumentException("Order Id is required to create an order item");
            }
            if (!entityAttributeField.containsKey("Pricebook Entry Id") || entityAttributeField.get("Pricebook Entry Id") == null) {
                throw new IllegalArgumentException("Pricebook Entry Id is required to create an order item");
            }
            if (!entityAttributeField.containsKey("Quantity") || entityAttributeField.get("Quantity") == null) {
                throw new IllegalArgumentException("Quantity is required to create an order item");
            }

            SFOrderItem orderItem = SFOrderItem.from(entityAttributeField);

            // Serialize order item with custom fields flattened into root JSON
            String body = serializeOrderItemWithCustomFields(orderItem);

            String response = salesforceManager.createRecord(body, ORDER_ITEM);

            Map<String, Object> responseMap = GSON.fromJson(response, Map.class);
            String id = (String) responseMap.get("id");

            LOGGER.info("Successfully created order item with ID: {}", id);
            return id;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error creating order item: {}", e.getMessage(), e);
            throw new IllegalArgumentException("Failed to create order item. " + e.getMessage(), e);
        }
    }
    
    @Override
    public void updateOrderItem(OrderItem orderItem) {
        try {
            if (orderItem.getId() == null || orderItem.getId().isEmpty()) {
                throw new IllegalArgumentException("Order Item ID is required. Please provide the Salesforce ID of the order item you want to update.");
            }
            
            // Get current order item state
            SFOrderItem updatedOrderItem = SFOrderItem.from(OrderItemCatalogType.fromSFOrderItem(orderItem));
            SFOrderItem oldOrderItem = SFOrderItem.from(OrderItemCatalogType.fromSFOrderItem(getOrderItemById(updatedOrderItem.id)));
            
            // Calculate difference to send only changed fields
            Map<String, Object> diff = salesforceManager.getDifference(oldOrderItem, updatedOrderItem);
            
            if (diff.isEmpty()) {
                LOGGER.info("No changes detected for order item ID: {}", updatedOrderItem.id);
                return;
            }
            
            String body = GSON.toJson(diff);
            salesforceManager.updateRecord(updatedOrderItem.id, body, ORDER_ITEM);
            LOGGER.info("Successfully updated order item with ID: {}", updatedOrderItem.id);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error updating order item: {}", e.getMessage(), e);
            throw new IllegalArgumentException("Failed to update order item. " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteOrderItem(String id) {
        try {
            if (id == null || id.isEmpty()) {
                throw new IllegalArgumentException("Order Item ID is required for deletion");
            }

            salesforceManager.deleteRecordById(id, ORDER_ITEM);
            LOGGER.info("Successfully deleted order item with ID: {}", id);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error deleting order item with ID {}: {}", id, e.getMessage(), e);
            throw new IllegalArgumentException("Failed to delete order item with ID: " + id + ". " + e.getMessage(), e);
        }
    }

    /**
     * Helper method to execute SOQL query and return list of OrderItems.
     *
     * @param query SOQL query string
     * @return List of OrderItem objects
     */
    private List<OrderItem> getOrderItemsByQuery(String query) {
        try {
            String response = salesforceManager.getRecordByQuery(query);
            Type listType = new TypeToken<SalesforceListResponse<SFOrderItem>>() {}.getType();
            SalesforceListResponse<SFOrderItem> listResponse = ORDERITEM_GSON.fromJson(response, listType);

            List<OrderItem> orderItems = new ArrayList<>();
            if (listResponse.records != null) {
                for (SFOrderItem sfOrderItem : listResponse.records) {
                    orderItems.add(new OrderItemImpl(sfOrderItem));
                }
            }

            LOGGER.info("Retrieved {} order items from Salesforce", orderItems.size());
            return orderItems;
        } catch (Exception e) {
            LOGGER.error("Error executing query {}: {}", query, e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * Serializes an SFOrderItem with custom fields flattened into the root JSON object.
     * Delegates to CustomFieldsSerializer utility for reusable logic.
     *
     * @param orderItem SFOrderItem with customFields map
     * @return JSON string with custom fields flattened to root level
     */
    private String serializeOrderItemWithCustomFields(SFOrderItem orderItem) {
        return CustomFieldsSerializer.serializeWithCustomFields(orderItem, orderItem.customFields, "OrderItem");
    }
}

