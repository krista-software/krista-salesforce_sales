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
@Entity(name = "Order Item", id = "localDomainEntity_b2c3d4e5-6f7a-8b9c-0d1e-2f3a4b5c6d7e", primaryKey = "Id", supportStore = true)
public class OrderItem {
    
    @Field(name = "Id", type = "Text", required = false)
    @Searchable
    public String id;

    @Field(name = "Order Id", type = "Text", required = false)
    @Searchable
    public String orderId;

    @Field(name = "Pricebook Entry Id", type = "Text", required = false)
    @Searchable
    public String pricebookEntryId;

    @Field(name = "Product Id", type = "Text", required = false)
    @Searchable
    public String product2Id;

    @Field(name = "Quantity", type = "Number", required = true)
    public Double quantity;

    @Field(name = "Unit Price", type = "Number", required = true)
    public Double unitPrice;

    @Field(name = "Total Price", type = "Number", required = false)
    public Double totalPrice;

    @Field(name = "List Price", type = "Number", required = false)
    public Double listPrice;

    @Field(name = "Description", type = "Text", required = false)
    public String description;

    @Field(name = "Order Item Number", type = "Text", required = false)
    @ToString
    public String orderItemNumber;

    @Field(name = "Service Date", type = "Date", required = false)
    public Long serviceDate;

    @Field(name = "End Date", type = "Date", required = false)
    public Long endDate;

    @Field(name = "Created Date", type = "Date", required = false)
    public Long createdDate;

    @Field(name = "Created By Id", type = "Text", required = false)
    public String createdById;

    @Field(name = "Last Modified Date", type = "Date", required = false)
    public Long lastModifiedDate;

    @Field(name = "Last Modified By Id", type = "Text", required = false)
    public String lastModifiedById;

    @Field(name = "Custom Fields", type = "FreeForm", required = false)
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
}

