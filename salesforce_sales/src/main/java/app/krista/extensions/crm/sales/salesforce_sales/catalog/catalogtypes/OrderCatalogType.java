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

import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Order;

import java.util.HashMap;
import java.util.Map;

/**
 * Converter between Order service implementation and Order catalog entity.
 * Provides bidirectional transformation methods.
 */
public final class OrderCatalogType {
    
    private OrderCatalogType() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Converts Order service interface to Order catalog entity.
     *
     * @param order Order service implementation
     * @return Order catalog entity
     */
    public static Order fromSFOrder(app.krista.extensions.crm.sales.salesforce_sales.service.Order order) {
        Order catalogOrder = new Order();
        catalogOrder.id = order.getId();
        catalogOrder.orderNumber = order.getOrderNumber();
        catalogOrder.accountId = order.getAccountId();
        catalogOrder.effectiveDate = order.getEffectiveDate();
        catalogOrder.status = order.getStatus();
        catalogOrder.description = order.getDescription();
        catalogOrder.customerAuthorizedById = order.getCustomerAuthorizedById();
        catalogOrder.companyAuthorizedById = order.getCompanyAuthorizedById();
        catalogOrder.type = order.getType();
        catalogOrder.billingStreet = order.getBillingStreet();
        catalogOrder.billingCity = order.getBillingCity();
        catalogOrder.billingState = order.getBillingState();
        catalogOrder.billingPostalCode = order.getBillingPostalCode();
        catalogOrder.billingCountry = order.getBillingCountry();
        catalogOrder.shippingStreet = order.getShippingStreet();
        catalogOrder.shippingCity = order.getShippingCity();
        catalogOrder.shippingState = order.getShippingState();
        catalogOrder.shippingPostalCode = order.getShippingPostalCode();
        catalogOrder.shippingCountry = order.getShippingCountry();
        catalogOrder.totalAmount = order.getTotalAmount();
        catalogOrder.createdDate = order.getCreatedDate();
        catalogOrder.createdById = order.getCreatedById();
        catalogOrder.ownerId = order.getOwnerId();

        // Copy custom fields from service Order to catalog Order
        catalogOrder.customFields = order.getCustomFields();

        return catalogOrder;
    }
    
    /**
     * Converts Order catalog entity to Map for API operations.
     *
     * @param order Order catalog entity
     * @return Map containing order field values
     */
    public static Map<String, Object> toMap(Order order) {
        Map<String, Object> orderMap = new HashMap<>();
        if (order.id != null) orderMap.put("Id", order.id);
        if (order.orderNumber != null) orderMap.put("Order Number", order.orderNumber);
        if (order.accountId != null) orderMap.put("Account Id", order.accountId);
        if (order.effectiveDate != null) orderMap.put("Effective Date", order.effectiveDate);
        if (order.status != null) orderMap.put("Status", order.status);
        if (order.description != null) orderMap.put("Description", order.description);
        if (order.customerAuthorizedById != null) orderMap.put("Customer Authorized By Id", order.customerAuthorizedById);
        if (order.companyAuthorizedById != null) orderMap.put("Company Authorized By Id", order.companyAuthorizedById);
        if (order.type != null) orderMap.put("Type", order.type);
        if (order.billingStreet != null) orderMap.put("Billing Street", order.billingStreet);
        if (order.billingCity != null) orderMap.put("Billing City", order.billingCity);
        if (order.billingState != null) orderMap.put("Billing State", order.billingState);
        if (order.billingPostalCode != null) orderMap.put("Billing Postal Code", order.billingPostalCode);
        if (order.billingCountry != null) orderMap.put("Billing Country", order.billingCountry);
        if (order.shippingStreet != null) orderMap.put("Shipping Street", order.shippingStreet);
        if (order.shippingCity != null) orderMap.put("Shipping City", order.shippingCity);
        if (order.shippingState != null) orderMap.put("Shipping State", order.shippingState);
        if (order.shippingPostalCode != null) orderMap.put("Shipping Postal Code", order.shippingPostalCode);
        if (order.shippingCountry != null) orderMap.put("Shipping Country", order.shippingCountry);
        if (order.totalAmount != null) orderMap.put("Total Amount", order.totalAmount);
        if (order.createdDate != null) orderMap.put("Created Date", order.createdDate);
        if (order.createdById != null) orderMap.put("Created By Id", order.createdById);
        if (order.ownerId != null) orderMap.put("Owner Id", order.ownerId);

        // Include dynamic custom fields in the map
        if (order.customFields != null && !order.customFields.isEmpty()) {
            orderMap.putAll(order.customFields);
        }

        return orderMap;
    }
}

