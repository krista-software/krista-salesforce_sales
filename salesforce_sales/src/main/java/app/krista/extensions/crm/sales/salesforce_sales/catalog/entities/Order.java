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

/**
 * Catalog entity for Salesforce Order.
 * Represents an order in the Krista catalog system.
 */
@Searchable
@Domain(id = "catEntryDomain_086f33da-aaea-4d0c-816f-3d5f20b4a273",
        name = "Sales",
        ecosystemId = "catEntryEcosystem_8a0e8ce7-6289-492f-9202-52284024280c",
        ecosystemName = "CRM",
        ecosystemVersion = "db320c64-6f6f-4c9e-8daa-41d3f796d6a6")
@Entity(name = "Order", id = "localDomainEntity_a1b2c3d4-5e6f-7a8b-9c0d-1e2f3a4b5c6d", primaryKey = "Id", supportStore = true, description = "Represents an order in Salesforce. Belongs to an Account (via Account Id referencing Account.Id). Contains OrderItems where OrderItem.Order Id matches Order.Id. Owner Id references User.Id. Customer Authorized By Id and Company Authorized By Id reference User.Id.")
public class Order {

    @Field(name = "Id", type = "Text", required = false, description = "Unique Salesforce identifier for the order. Referenced by OrderItem.Order Id.")
    public String id;

    @Searchable
    @ToString
    @Field(name = "Order Number", type = "Text", required = false, description = "Auto-generated order number.")
    public String orderNumber;

    @Searchable
    @Field(name = "Account Id", type = "Text", required = true, description = "Id of the associated account. References Account.Id.")
    public String accountId;

    @Field(name = "Effective Date", type = "Date", required = true, description = "Date when the order takes effect.")
    public Long effectiveDate;

    @Field.PickOne(name = "Status", values = {"Draft", "Activated"}, required = true, description = "Current status of the order.", attributes = {}, options = {})
    public String status;

    @Field(name = "Description", type = "Text", required = false, description = "Description of the order.")
    public String description;

    @Field(name = "Customer Authorized By Id", type = "Text", required = false, description = "User who authorized the order on customer side. References User.Id.")
    public String customerAuthorizedById;

    @Field(name = "Company Authorized By Id", type = "Text", required = false, description = "User who authorized the order on company side. References User.Id.")
    public String companyAuthorizedById;

    @Field(name = "Type", type = "PickOne", required = false, description = "Type of order.")
    public String type;

    @Field(name = "Billing Street", type = "Street", required = false, description = "Billing address street for the order.")
    public String billingStreet;

    @Field(name = "Billing City", type = "City", required = false, description = "Billing address city for the order.")
    public String billingCity;

    @Field(name = "Billing State", type = "State", required = false, description = "Billing address state for the order.")
    public String billingState;

    @Field(name = "Billing Postal Code", type = "PostalCode", required = false, description = "Billing address postal code for the order.")
    public String billingPostalCode;

    @Field(name = "Billing Country", type = "Country", required = false, description = "Billing address country for the order.")
    public String billingCountry;

    @Field(name = "Shipping Street", type = "Street", required = false, description = "Shipping address street for the order.")
    public String shippingStreet;

    @Field(name = "Shipping City", type = "City", required = false, description = "Shipping address city for the order.")
    public String shippingCity;

    @Field(name = "Shipping State", type = "State", required = false, description = "Shipping address state for the order.")
    public String shippingState;

    @Field(name = "Shipping Postal Code", type = "PostalCode", required = false, description = "Shipping address postal code for the order.")
    public String shippingPostalCode;

    @Field(name = "Shipping Country", type = "Country", required = false, description = "Shipping address country for the order.")
    public String shippingCountry;

    @Field(name = "Total Amount", type = "Number", required = false, description = "Total amount of the order.")
    public Double totalAmount;

    @Field(name = "Created Date", type = "Date", required = false, description = "Date when the order was created.")
    public Long createdDate;

    @Field(name = "Created By Id", type = "Text", required = false, description = "User who created the order. References User.Id.")
    public String createdById;

    @Field(name = "Owner Id", type = "Text", required = false, description = "Salesforce User Id of the order owner. References User.Id.")
    public String ownerId;

    /**
     * Dynamic custom fields map.
     * Allows passing ANY Salesforce custom field (ending with __c) without hardcoding them.
     * This supports the PO workflow and any other custom fields that may be added to Salesforce.
     *
     * Example usage:
     * - PO_Number__c
     * - Extraction_Status__c
     * - Confidence__c
     * - custom_field__c
     * - Any other custom field
     *
     * In Krista Studio, use the "Custom Fields" FreeForm field to enter custom fields as key-value pairs.
     */
    @Field(name = "Custom Fields", type = "FreeForm", required = false, description = "Dynamic custom fields from Salesforce (fields ending with __c).")
    public Map<String, Object> customFields;

    /**
     * Creates an Order entity from a Map of field values.
     * Supports dynamic custom fields - any field ending with __c will be automatically included.
     *
     * @param orderFields Map containing order field values
     * @return Order entity populated with field values
     */
    public static Order fromFields(Map<String, Object> orderFields) {
        Order order = new Order();
        order.id = (String) orderFields.get("Id");
        order.orderNumber = (String) orderFields.get("Order Number");
        order.accountId = (String) orderFields.get("Account Id");
        order.effectiveDate = (Long) orderFields.get("Effective Date");
        order.status = (String) orderFields.get("Status");
        order.description = (String) orderFields.get("Description");
        order.customerAuthorizedById = (String) orderFields.get("Customer Authorized By Id");
        order.companyAuthorizedById = (String) orderFields.get("Company Authorized By Id");
        order.type = (String) orderFields.get("Type");
        order.billingStreet = (String) orderFields.get("Billing Street");
        order.billingCity = (String) orderFields.get("Billing City");
        order.billingState = (String) orderFields.get("Billing State");
        order.billingPostalCode = (String) orderFields.get("Billing Postal Code");
        order.billingCountry = (String) orderFields.get("Billing Country");
        order.shippingStreet = (String) orderFields.get("Shipping Street");
        order.shippingCity = (String) orderFields.get("Shipping City");
        order.shippingState = (String) orderFields.get("Shipping State");
        order.shippingPostalCode = (String) orderFields.get("Shipping Postal Code");
        order.shippingCountry = (String) orderFields.get("Shipping Country");
        order.totalAmount = (Double) orderFields.get("Total Amount");
        order.createdDate = (Long) orderFields.get("Created Date");
        order.createdById = (String) orderFields.get("Created By Id");
        order.ownerId = (String) orderFields.get("Owner Id");

        // Extract custom fields from the "Custom Fields" FreeForm
        Object customFieldsObj = orderFields.get("Custom Fields");
        if (customFieldsObj instanceof Map) {
            order.customFields = (Map<String, Object>) customFieldsObj;
        }

        return order;
    }

    public Map<String, Object> toFields() {
        Map<String, Object> fieldMap = new LinkedHashMap<>();
        fieldMap.put("Id", id);
        fieldMap.put("Order Number", orderNumber);
        fieldMap.put("Account Id", accountId);
        fieldMap.put("Effective Date", effectiveDate);
        fieldMap.put("Status", status);
        fieldMap.put("Description", description);
        fieldMap.put("Customer Authorized By Id", customerAuthorizedById);
        fieldMap.put("Company Authorized By Id", companyAuthorizedById);
        fieldMap.put("Type", type);
        fieldMap.put("Billing Street", billingStreet);
        fieldMap.put("Billing City", billingCity);
        fieldMap.put("Billing State", billingState);
        fieldMap.put("Billing Postal Code", billingPostalCode);
        fieldMap.put("Billing Country", billingCountry);
        fieldMap.put("Shipping Street", shippingStreet);
        fieldMap.put("Shipping City", shippingCity);
        fieldMap.put("Shipping State", shippingState);
        fieldMap.put("Shipping Postal Code", shippingPostalCode);
        fieldMap.put("Shipping Country", shippingCountry);
        fieldMap.put("Total Amount", totalAmount);
        fieldMap.put("Created Date", createdDate);
        fieldMap.put("Created By Id", createdById);
        fieldMap.put("Owner Id", ownerId);
        fieldMap.put("Custom Fields", customFields);
        return fieldMap;
    }
}
