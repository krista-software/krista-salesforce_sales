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

package app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes;

import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.OrderItem;

import java.util.HashMap;
import java.util.Map;

/**
 * Converter between OrderItem service implementation and OrderItem catalog entity.
 * Provides bidirectional transformation methods.
 */
public final class OrderItemCatalogType {
    
    private OrderItemCatalogType() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Converts OrderItem service interface to OrderItem catalog entity.
     *
     * @param orderItem OrderItem service implementation
     * @return OrderItem catalog entity
     */
    public static OrderItem fromSFOrderItem(app.krista.extensions.crm.sales.salesforce_sales.service.OrderItem orderItem) {
        OrderItem catalogOrderItem = new OrderItem();
        catalogOrderItem.id = orderItem.getId();
        catalogOrderItem.orderId = orderItem.getOrderId();
        catalogOrderItem.pricebookEntryId = orderItem.getPricebookEntryId();
        catalogOrderItem.product2Id = orderItem.getProduct2Id();
        catalogOrderItem.quantity = orderItem.getQuantity();
        catalogOrderItem.unitPrice = orderItem.getUnitPrice();
        catalogOrderItem.totalPrice = orderItem.getTotalPrice();
        catalogOrderItem.listPrice = orderItem.getListPrice();
        catalogOrderItem.description = orderItem.getDescription();
        catalogOrderItem.orderItemNumber = orderItem.getOrderItemNumber();
        catalogOrderItem.serviceDate = orderItem.getServiceDate();
        catalogOrderItem.endDate = orderItem.getEndDate();
        catalogOrderItem.createdDate = orderItem.getCreatedDate();
        catalogOrderItem.createdById = orderItem.getCreatedById();
        catalogOrderItem.lastModifiedDate = orderItem.getLastModifiedDate();
        catalogOrderItem.lastModifiedById = orderItem.getLastModifiedById();

        // Copy custom fields from service OrderItem to catalog OrderItem
        catalogOrderItem.customFields = orderItem.getCustomFields();

        return catalogOrderItem;
    }
    
    /**
     * Converts OrderItem catalog entity to Map for API operations.
     *
     * @param orderItem OrderItem catalog entity
     * @return Map containing order item field values
     */
    public static Map<String, Object> toMap(OrderItem orderItem) {
        Map<String, Object> orderItemMap = new HashMap<>();
        if (orderItem.id != null) orderItemMap.put("Id", orderItem.id);
        if (orderItem.orderId != null) orderItemMap.put("Order Id", orderItem.orderId);
        if (orderItem.pricebookEntryId != null) orderItemMap.put("Pricebook Entry Id", orderItem.pricebookEntryId);
        if (orderItem.product2Id != null) orderItemMap.put("Product Id", orderItem.product2Id);
        if (orderItem.quantity != null) orderItemMap.put("Quantity", orderItem.quantity);
        if (orderItem.unitPrice != null) orderItemMap.put("Unit Price", orderItem.unitPrice);
        if (orderItem.totalPrice != null) orderItemMap.put("Total Price", orderItem.totalPrice);
        if (orderItem.listPrice != null) orderItemMap.put("List Price", orderItem.listPrice);
        if (orderItem.description != null) orderItemMap.put("Description", orderItem.description);
        if (orderItem.orderItemNumber != null) orderItemMap.put("Order Item Number", orderItem.orderItemNumber);
        if (orderItem.serviceDate != null) orderItemMap.put("Service Date", orderItem.serviceDate);
        if (orderItem.endDate != null) orderItemMap.put("End Date", orderItem.endDate);
        if (orderItem.createdDate != null) orderItemMap.put("Created Date", orderItem.createdDate);
        if (orderItem.createdById != null) orderItemMap.put("Created By Id", orderItem.createdById);
        if (orderItem.lastModifiedDate != null) orderItemMap.put("Last Modified Date", orderItem.lastModifiedDate);
        if (orderItem.lastModifiedById != null) orderItemMap.put("Last Modified By Id", orderItem.lastModifiedById);

        // Add custom fields to the map
        if (orderItem.customFields != null && !orderItem.customFields.isEmpty()) {
            orderItemMap.putAll(orderItem.customFields);
        }

        return orderItemMap;
    }
}

