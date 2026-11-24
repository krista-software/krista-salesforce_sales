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

package app.krista.extensions.crm.sales.salesforce_sales.service;

import java.util.List;
import java.util.Map;

/**
 * Data Access Object interface for OrderItem operations.
 * Defines CRUD operations for Salesforce OrderItem (Order Product) object.
 */
public interface OrderItemDao {
    
    /**
     * Returns {@link OrderItem} object from Salesforce by order item ID.
     *
     * @param id Salesforce ID of the order item to retrieve
     * @return {@link OrderItem} object from Salesforce
     * @throws IllegalArgumentException if order item not found
     */
    OrderItem getOrderItemById(String id);
    
    /**
     * Returns list of {@link OrderItem} objects from Salesforce by order ID.
     *
     * @param orderId Order ID to filter order items
     * @param size Number of order items to fetch
     * @param page Page number for pagination (1-based)
     * @return {@link List<OrderItem>} list of order items
     * @return empty list if no records found
     */
    List<OrderItem> getOrderItemsByOrderId(String orderId, long size, long page);
    
    /**
     * Returns list of {@link OrderItem} objects from Salesforce by product ID.
     *
     * @param productId Product ID (Product2Id) to filter order items
     * @param size Number of order items to fetch
     * @param page Page number for pagination (1-based)
     * @return {@link List<OrderItem>} list of order items
     * @return empty list if no records found
     */
    List<OrderItem> getOrderItemsByProductId(String productId, long size, long page);
    
    /**
     * Returns list of all {@link OrderItem} objects from Salesforce with pagination.
     *
     * @param size Number of order items to fetch
     * @param page Page number for pagination (1-based)
     * @return {@link List<OrderItem>} list of order items
     * @return empty list if no records found
     */
    List<OrderItem> getOrderItems(long size, long page);
    
    /**
     * Creates a new {@link OrderItem} in Salesforce.
     * Requires OrderId and PricebookEntryId.
     *
     * @param entityAttributeField Map containing order item field values
     * @return Salesforce ID of the created order item
     * @throws IllegalArgumentException if required fields are missing
     */
    String addOrderItem(Map<String, Object> entityAttributeField);
    
    /**
     * Updates an existing {@link OrderItem} in Salesforce.
     * Only sends changed fields to optimize API usage.
     *
     * @param orderItem {@link OrderItem} object with updated values
     * @throws IllegalArgumentException if OrderItem ID is null or empty
     */
    void updateOrderItem(OrderItem orderItem);
    
    /**
     * Returns list of {@link OrderItem} objects from Salesforce by executing the given SOQL query.
     *
     * @param query SOQL query string
     * @return {@link List<OrderItem>} list of order items
     */
    List<OrderItem> getOrderItemsByQuery(String query);

    /**
     * Deletes an {@link OrderItem} from Salesforce.
     *
     * @param id Salesforce ID of the order item to delete
     * @throws IllegalArgumentException if order item not found
     */
    void deleteOrderItem(String id);
}

