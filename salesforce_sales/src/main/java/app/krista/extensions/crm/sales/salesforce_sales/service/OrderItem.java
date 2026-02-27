package app.krista.extensions.crm.sales.salesforce_sales.service;

import java.util.Map;

/**
 * Service interface for OrderItem (Order Product).
 * Defines getter methods for all OrderItem fields.
 */
public interface OrderItem {
    
    /**
     * Returns the Salesforce ID of the order item.
     *
     * @return Order item ID
     */
    String getId();
    
    /**
     * Returns the Order ID this item belongs to.
     *
     * @return Order ID
     */
    String getOrderId();
    
    /**
     * Returns the Pricebook Entry ID.
     *
     * @return Pricebook Entry ID
     */
    String getPricebookEntryId();
    
    /**
     * Returns the Product ID (Product2Id).
     *
     * @return Product ID
     */
    String getProduct2Id();
    
    /**
     * Returns the quantity of the product.
     *
     * @return Quantity
     */
    Double getQuantity();
    
    /**
     * Returns the unit price.
     *
     * @return Unit price
     */
    Double getUnitPrice();
    
    /**
     * Returns the total price (Quantity * UnitPrice).
     *
     * @return Total price
     */
    Double getTotalPrice();
    
    /**
     * Returns the list price.
     *
     * @return List price
     */
    Double getListPrice();
    
    /**
     * Returns the description.
     *
     * @return Description
     */
    String getDescription();
    
    /**
     * Returns the order item number.
     *
     * @return Order item number
     */
    String getOrderItemNumber();
    
    /**
     * Returns the service date as epoch milliseconds.
     *
     * @return Service date in milliseconds
     */
    Long getServiceDate();
    
    /**
     * Returns the end date as epoch milliseconds.
     *
     * @return End date in milliseconds
     */
    Long getEndDate();
    
    /**
     * Returns the created date as epoch milliseconds.
     *
     * @return Created date in milliseconds
     */
    Long getCreatedDate();
    
    /**
     * Returns the created by user ID.
     *
     * @return Created by ID
     */
    String getCreatedById();
    
    /**
     * Returns the last modified date as epoch milliseconds.
     *
     * @return Last modified date in milliseconds
     */
    Long getLastModifiedDate();
    
    /**
     * Returns the last modified by user ID.
     *
     * @return Last modified by ID
     */
    String getLastModifiedById();

    /**
     * Returns the map of dynamic custom fields (fields ending with __c in Salesforce).
     *
     * @return Map of custom fields
     */
    Map<String, Object> getCustomFields();
}

