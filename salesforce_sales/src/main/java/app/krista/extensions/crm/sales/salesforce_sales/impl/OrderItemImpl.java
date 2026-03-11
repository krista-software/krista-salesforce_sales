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

package app.krista.extensions.crm.sales.salesforce_sales.impl;

import app.krista.extensions.crm.sales.salesforce_sales.model.SFOrderItem;
import app.krista.extensions.crm.sales.salesforce_sales.service.OrderItem;
import app.krista.extensions.crm.sales.salesforce_sales.util.DatePlus;

import java.util.Map;

/**
 * Implementation of OrderItem service interface.
 * Wraps SFOrderItem model and provides getter methods.
 */
public class OrderItemImpl implements OrderItem {
    
    private final SFOrderItem sfOrderItem;
    
    public OrderItemImpl(SFOrderItem sfOrderItem) {
        this.sfOrderItem = sfOrderItem;
    }
    
    @Override
    public String getId() {
        return sfOrderItem.id;
    }
    
    @Override
    public String getOrderId() {
        return sfOrderItem.orderId;
    }
    
    @Override
    public String getPricebookEntryId() {
        return sfOrderItem.pricebookEntryId;
    }
    
    @Override
    public String getProduct2Id() {
        return sfOrderItem.product2Id;
    }
    
    @Override
    public Double getQuantity() {
        return sfOrderItem.quantity;
    }
    
    @Override
    public Double getUnitPrice() {
        return sfOrderItem.unitPrice;
    }
    
    @Override
    public Double getTotalPrice() {
        return sfOrderItem.totalPrice;
    }
    
    @Override
    public Double getListPrice() {
        return sfOrderItem.listPrice;
    }
    
    @Override
    public String getDescription() {
        return sfOrderItem.description;
    }
    
    @Override
    public String getOrderItemNumber() {
        return sfOrderItem.orderItemNumber;
    }
    
    @Override
    public Long getServiceDate() {
        return sfOrderItem.serviceDate != null ? DatePlus.fromYYYYMMDD(sfOrderItem.serviceDate).getTime() : null;
    }
    
    @Override
    public Long getEndDate() {
        return sfOrderItem.endDate != null ? DatePlus.fromYYYYMMDD(sfOrderItem.endDate).getTime() : null;
    }
    
    @Override
    public Long getCreatedDate() {
        return sfOrderItem.createdDate != null ? sfOrderItem.createdDate.getTime() : null;
    }
    
    @Override
    public String getCreatedById() {
        return sfOrderItem.createdById;
    }
    
    @Override
    public Long getLastModifiedDate() {
        return sfOrderItem.lastModifiedDate != null ? sfOrderItem.lastModifiedDate.getTime() : null;
    }
    
    @Override
    public String getLastModifiedById() {
        return sfOrderItem.lastModifiedById;
    }

    @Override
    public Map<String, Object> getCustomFields() {
        return sfOrderItem.customFields;
    }
}
