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

package app.krista.extensions.crm.sales.salesforce_sales.catalog.entities;

import app.krista.extension.impl.anno.*;

import java.util.LinkedHashMap;
import java.util.Map;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Validators.returnNullOrValue;

/**
 * Catalog entity for Salesforce OrderItem (Order Product).
 * Represents an order line item with product and pricing information.
 */
@Domain(id = "catEntryDomain_086f33da-aaea-4d0c-816f-3d5f20b4a273",
        name = "Sales",
        ecosystemId = "catEntryEcosystem_8a0e8ce7-6289-492f-9202-52284024280c",
        ecosystemName = "CRM",
        ecosystemVersion = "db320c64-6f6f-4c9e-8daa-41d3f796d6a6")
@Entity(name = "Order Item", id = "localDomainEntity_b2c3d4e5-6f7a-8b9c-0d1e-2f3a4b5c6d7e", primaryKey = "Id", supportStore = true,
        description = "Represents a line item within an Order in Salesforce. Belongs to an Order (via Order Id referencing Order.Id). Product Id references Product.Id. Pricebook Entry Id references PricebookEntry.Id. Created By Id and Last Modified By Id reference User.Id.")
@Searchable
public class OrderItem {

    @Field(name = "Id", type = "Text", required = false, description = "Unique Salesforce identifier for the order item.")
    @Searchable
    public String id;

    @Field(name = "Order Id", type = "Text", required = false, description = "Id of the parent order. References Order.Id.")
    @Searchable
    public String orderId;

    @Field(name = "Pricebook Entry Id", type = "Text", required = false, description = "Id of the pricebook entry. References PricebookEntry.Id.")
    @Searchable
    public String pricebookEntryId;

    @Field(name = "Product Id", type = "Text", required = false, description = "Id of the associated product. References Product.Id.")
    @Searchable
    public String product2Id;

    @Field(name = "Quantity", type = "Number", required = true, description = "Quantity of the product ordered.")
    public Double quantity;

    @Field(name = "Unit Price", type = "Number", required = true, description = "Price per unit.")
    public Double unitPrice;

    @Field(name = "Total Price", type = "Number", required = false, description = "Total price for this line item.")
    public Double totalPrice;

    @Field(name = "List Price", type = "Number", required = false, description = "List price from the pricebook.")
    public Double listPrice;

    @Field(name = "Description", type = "Text", required = false, description = "Description of the order item.")
    public String description;

    @Field(name = "Order Item Number", type = "Text", required = false, description = "Auto-generated line item number.")
    @ToString
    public String orderItemNumber;

    @Field(name = "Service Date", type = "Date", required = false, description = "Start date for the service.")
    public Long serviceDate;

    @Field(name = "End Date", type = "Date", required = false, description = "End date for the service.")
    public Long endDate;

    @Field(name = "Created Date", type = "Date", required = false, description = "Date when the order item was created.")
    public Long createdDate;

    @Field(name = "Created By Id", type = "Text", required = false, description = "User who created the order item. References User.Id.")
    public String createdById;

    @Field(name = "Last Modified Date", type = "Date", required = false, description = "Date when the order item was last modified.")
    public Long lastModifiedDate;

    @Field(name = "Last Modified By Id", type = "Text", required = false, description = "User who last modified the order item. References User.Id.")
    public String lastModifiedById;

    @Field(name = "Custom Fields", type = "FreeForm", required = false, description = "Dynamic custom fields from Salesforce (fields ending with __c).")
    public Map<String, Object> customFields;

    /**
     * Creates an OrderItem entity from a Map of field values.
     * Used for catalog request processing.
     *
     * @param fields Map containing order item field values
     * @return OrderItem entity instance
     */
    public static OrderItem fromFields(Map<String, Object> fields) {
        OrderItem orderItem = new OrderItem();
        orderItem.id = returnNullOrValue((String) fields.get("Id"));
        orderItem.orderId = returnNullOrValue((String) fields.get("Order Id"));
        orderItem.pricebookEntryId = returnNullOrValue((String) fields.get("Pricebook Entry Id"));
        orderItem.product2Id = returnNullOrValue((String) fields.get("Product Id"));

        orderItem.quantity = (Double) fields.get("Quantity");
        orderItem.unitPrice = (Double) fields.get("Unit Price");
        orderItem.totalPrice = (Double) fields.get("Total Price");
        orderItem.listPrice = (Double) fields.get("List Price");

        orderItem.description = returnNullOrValue((String) fields.get("Description"));
        orderItem.orderItemNumber = returnNullOrValue((String) fields.get("Order Item Number"));

        orderItem.serviceDate = (Long) fields.get("Service Date");
        orderItem.endDate = (Long) fields.get("End Date");
        orderItem.createdDate = (Long) fields.get("Created Date");
        orderItem.createdById = returnNullOrValue((String) fields.get("Created By Id"));
        orderItem.lastModifiedDate = (Long) fields.get("Last Modified Date");
        orderItem.lastModifiedById = returnNullOrValue((String) fields.get("Last Modified By Id"));

        // Extract custom fields from the "Custom Fields" FreeForm
        Object customFieldsObj = fields.get("Custom Fields");
        if (customFieldsObj instanceof Map) {
            orderItem.customFields = (Map<String, Object>) customFieldsObj;
        }

        return orderItem;
    }

    /**
     * Converts this OrderItem entity to a Map of field values.
     *
     * @return Map containing order item field values in declaration order
     */
    public Map<String, Object> toFields() {
        Map<String, Object> fields = new LinkedHashMap<>();
        fields.put("Id", id);
        fields.put("Order Id", orderId);
        fields.put("Pricebook Entry Id", pricebookEntryId);
        fields.put("Product Id", product2Id);
        fields.put("Quantity", quantity);
        fields.put("Unit Price", unitPrice);
        fields.put("Total Price", totalPrice);
        fields.put("List Price", listPrice);
        fields.put("Description", description);
        fields.put("Order Item Number", orderItemNumber);
        fields.put("Service Date", serviceDate);
        fields.put("End Date", endDate);
        fields.put("Created Date", createdDate);
        fields.put("Created By Id", createdById);
        fields.put("Last Modified Date", lastModifiedDate);
        fields.put("Last Modified By Id", lastModifiedById);
        fields.put("Custom Fields", customFields);
        return fields;
    }
}
