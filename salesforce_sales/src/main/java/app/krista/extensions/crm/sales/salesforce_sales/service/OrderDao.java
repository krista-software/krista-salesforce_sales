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
 * Data Access Object interface for Order operations.
 * Defines CRUD operations for Salesforce Order object.
 */
public interface OrderDao {
    
    /**
     * Returns {@link Order} object from Salesforce by order ID.
     *
     * @param id Salesforce ID of the order to retrieve
     * @return {@link Order} object from Salesforce
     * @throws IllegalArgumentException if order not found
     */
    Order getOrderById(String id);
    
    /**
     * Returns {@link Order} object from Salesforce by order number.
     *
     * @param orderNumber Order number to search for
     * @return {@link Order} object from Salesforce
     * @throws IllegalArgumentException if order not found
     */
    Order getOrderByOrderNumber(String orderNumber);
    
    /**
     * Returns list of {@link Order} objects from Salesforce by account ID.
     *
     * @param accountId Account ID to filter orders
     * @param size Number of orders to fetch
     * @param page Page number for pagination (1-based)
     * @return {@link List<Order>} list of orders
     * @return empty list if no records found
     */
    List<Order> getOrdersByAccountId(String accountId, long size, long page);
    
    /**
     * Returns list of {@link Order} objects from Salesforce by status.
     *
     * @param status Order status to filter (Draft, Activated, etc.)
     * @param size Number of orders to fetch
     * @param page Page number for pagination (1-based)
     * @return {@link List<Order>} list of orders
     * @return empty list if no records found
     */
    List<Order> getOrdersByStatus(String status, long size, long page);
    
    /**
     * Returns list of all {@link Order} objects from Salesforce with pagination.
     *
     * @param size Number of orders to fetch
     * @param page Page number for pagination (1-based)
     * @return {@link List<Order>} list of orders
     * @return empty list if no records found
     */
    List<Order> getOrders(long size, long page);
    
    /**
     * Creates a new {@link Order} in Salesforce.
     *
     * @param entityAttributeField Map containing order field values (supports standard and custom fields)
     * @return Salesforce ID of the created order
     * @throws IllegalArgumentException if required fields are missing
     */
    String addOrder(Map<String, Object> entityAttributeField);
    
    /**
     * Updates an existing {@link Order} in Salesforce.
     * Only sends changed fields to optimize API usage.
     *
     * @param order {@link Order} object with updated values
     * @throws IllegalArgumentException if Order ID is null or empty
     */
    void updateOrder(Order order);
    
    /**
     * Deletes an {@link Order} from Salesforce.
     *
     * @param id Salesforce ID of the order to delete
     * @throws IllegalArgumentException if order not found
     */
    void deleteOrder(String id);
}

