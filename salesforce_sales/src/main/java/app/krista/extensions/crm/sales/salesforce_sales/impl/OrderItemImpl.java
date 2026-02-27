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
