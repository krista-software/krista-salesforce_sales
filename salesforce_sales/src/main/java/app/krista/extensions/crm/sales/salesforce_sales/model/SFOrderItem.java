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

package app.krista.extensions.crm.sales.salesforce_sales.model;

import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.OrderItem;
import app.krista.extensions.crm.sales.salesforce_sales.util.CustomFieldsExtractor;
import app.krista.extensions.crm.sales.salesforce_sales.util.DatePlus;
import com.google.gson.annotations.SerializedName;

import java.util.*;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Validators.returnNullOrValue;

/**
 * Salesforce OrderItem API response model.
 * Maps to the OrderItem (Order Product) standard object in Salesforce.
 */
public final class SFOrderItem {
    
    @SerializedName("Id")
    public String id;
    
    @SerializedName("OrderId")
    public String orderId;
    
    @SerializedName("PricebookEntryId")
    public String pricebookEntryId;
    
    @SerializedName("Product2Id")
    public String product2Id;
    
    @SerializedName("Quantity")
    public Double quantity;
    
    @SerializedName("UnitPrice")
    public Double unitPrice;
    
    @SerializedName("TotalPrice")
    public Double totalPrice;
    
    @SerializedName("Description")
    public String description;
    
    @SerializedName("ServiceDate")
    public String serviceDate;
    
    @SerializedName("EndDate")
    public String endDate;
    
    @SerializedName("ListPrice")
    public Double listPrice;
    
    @SerializedName("OrderItemNumber")
    public String orderItemNumber;
    
    @SerializedName("CreatedDate")
    public Date createdDate;
    
    @SerializedName("CreatedById")
    public String createdById;
    
    @SerializedName("LastModifiedDate")
    public Date lastModifiedDate;
    
    @SerializedName("LastModifiedById")
    public String lastModifiedById;

    // Dynamic custom fields map (not serialized directly, handled separately)
    public Map<String, Object> customFields = new HashMap<>();

    /**
     * Converts a Map of entity attributes to SFOrderItem model.
     * Used for creating/updating order items from catalog requests.
     *
     * @param entityAttributeField Map containing order item field values
     * @return SFOrderItem instance populated with field values
     */
    public static SFOrderItem from(Map<String, Object> entityAttributeField) {
        SFOrderItem orderItem = new SFOrderItem();
        orderItem.orderId = returnNullOrValue((String) entityAttributeField.get("Order Id"));
        orderItem.pricebookEntryId = returnNullOrValue((String) entityAttributeField.get("Pricebook Entry Id"));
        orderItem.product2Id = returnNullOrValue((String) entityAttributeField.get("Product Id"));
        
        orderItem.quantity = (Double) entityAttributeField.get("Quantity");
        orderItem.unitPrice = (Double) entityAttributeField.get("Unit Price");
        orderItem.totalPrice = (Double) entityAttributeField.get("Total Price");
        orderItem.listPrice = (Double) entityAttributeField.get("List Price");
        
        orderItem.description = returnNullOrValue((String) entityAttributeField.get("Description"));
        orderItem.orderItemNumber = returnNullOrValue((String) entityAttributeField.get("Order Item Number"));
        
        Long serviceDate = (Long) entityAttributeField.get("Service Date");
        orderItem.serviceDate = serviceDate != null && serviceDate > 0 ? DatePlus.getYYYYMMDD(serviceDate) : null;
        
        Long endDate = (Long) entityAttributeField.get("End Date");
        orderItem.endDate = endDate != null && endDate > 0 ? DatePlus.getYYYYMMDD(endDate) : null;
        
        orderItem.createdById = returnNullOrValue((String) entityAttributeField.get("Created By Id"));
        orderItem.lastModifiedById = returnNullOrValue((String) entityAttributeField.get("Last Modified By Id"));

        // Extract custom fields
        orderItem.customFields = extractCustomFields(entityAttributeField);

        return orderItem;
    }
    
    /**
     * Converts a catalog OrderItem entity to SFOrderItem model.
     * Used for update operations.
     *
     * @param orderItem Catalog OrderItem entity
     * @return SFOrderItem instance
     */
    public static SFOrderItem from(OrderItem orderItem) {
        SFOrderItem sfOrderItem = new SFOrderItem();
        sfOrderItem.id = orderItem.id;
        sfOrderItem.orderId = orderItem.orderId;
        sfOrderItem.pricebookEntryId = orderItem.pricebookEntryId;
        sfOrderItem.product2Id = orderItem.product2Id;
        
        sfOrderItem.quantity = orderItem.quantity;
        sfOrderItem.unitPrice = orderItem.unitPrice;
        sfOrderItem.totalPrice = orderItem.totalPrice;
        sfOrderItem.listPrice = orderItem.listPrice;
        
        sfOrderItem.description = orderItem.description;
        sfOrderItem.orderItemNumber = orderItem.orderItemNumber;
        
        if (orderItem.serviceDate != null && orderItem.serviceDate > 0)
            sfOrderItem.serviceDate = DatePlus.getYYYYMMDD(orderItem.serviceDate);
        
        if (orderItem.endDate != null && orderItem.endDate > 0)
            sfOrderItem.endDate = DatePlus.getYYYYMMDD(orderItem.endDate);
        
        if (orderItem.createdDate != null)
            sfOrderItem.createdDate = new Date(orderItem.createdDate);
        
        if (orderItem.lastModifiedDate != null)
            sfOrderItem.lastModifiedDate = new Date(orderItem.lastModifiedDate);
        
        sfOrderItem.createdById = orderItem.createdById;
        sfOrderItem.lastModifiedById = orderItem.lastModifiedById;

        return sfOrderItem;
    }

    /**
     * Extracts custom fields from the entity attribute field map.
     * Delegates to CustomFieldsExtractor utility for reusable logic.
     *
     * @param entityAttributeField Map containing all field values
     * @return Map of custom fields with Salesforce API names and converted values
     */
    private static Map<String, Object> extractCustomFields(Map<String, Object> entityAttributeField) {
        Set<String> standardFields = new HashSet<>(Arrays.asList(
            "Id", "Order Id", "Pricebook Entry Id", "Product Id", "Quantity", "Unit Price",
            "Total Price", "List Price", "Description", "Order Item Number", "Service Date",
            "End Date", "Created Date", "Created By Id", "Last Modified Date", "Last Modified By Id"
        ));

        return CustomFieldsExtractor.extractCustomFields(entityAttributeField, standardFields, "SFOrderItem");
    }
}

