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

import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Order;
import app.krista.extensions.crm.sales.salesforce_sales.util.CustomFieldsExtractor;
import app.krista.extensions.crm.sales.salesforce_sales.util.DatePlus;
import com.google.gson.annotations.SerializedName;

import java.util.*;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Validators.returnNullOrValue;

/**
 * Salesforce Order API response model.
 * Maps to the Order standard object in Salesforce.
 */
public final class SFOrder {
    
    @SerializedName("Id")
    public String id;
    
    @SerializedName("OrderNumber")
    public String orderNumber;
    
    @SerializedName("AccountId")
    public String accountId;
    
    @SerializedName("EffectiveDate")
    public String effectiveDate;
    
    @SerializedName("Status")
    public String status;
    
    @SerializedName("Description")
    public String description;
    
    @SerializedName("CustomerAuthorizedById")
    public String customerAuthorizedById;
    
    @SerializedName("CompanyAuthorizedById")
    public String companyAuthorizedById;
    
    @SerializedName("Type")
    public String type;
    
    @SerializedName("BillingStreet")
    public String billingStreet;
    
    @SerializedName("BillingCity")
    public String billingCity;
    
    @SerializedName("BillingState")
    public String billingState;
    
    @SerializedName("BillingPostalCode")
    public String billingPostalCode;
    
    @SerializedName("BillingCountry")
    public String billingCountry;
    
    @SerializedName("ShippingStreet")
    public String shippingStreet;
    
    @SerializedName("ShippingCity")
    public String shippingCity;
    
    @SerializedName("ShippingState")
    public String shippingState;
    
    @SerializedName("ShippingPostalCode")
    public String shippingPostalCode;
    
    @SerializedName("ShippingCountry")
    public String shippingCountry;
    
    @SerializedName("TotalAmount")
    public Double totalAmount;
    
    @SerializedName("CreatedDate")
    public Date createdDate;
    
    @SerializedName("CreatedById")
    public String createdById;
    
    @SerializedName("OwnerId")
    public String ownerId;

    /**
     * Dynamic custom fields map.
     * Stores any Salesforce custom fields (ending with __c) that are not explicitly defined above.
     * This allows passing ANY custom field without hardcoding them in the model.
     */
    public Map<String, Object> customFields;
    
    /**
     * Converts a Map of entity attributes to SFOrder model.
     * Used for creating/updating orders from catalog requests.
     * Supports dynamic custom fields - any field ending with __c will be automatically included.
     *
     * @param entityAttributeField Map containing order field values
     * @return SFOrder instance populated with field values
     */
    public static SFOrder from(Map<String, Object> entityAttributeField) {
        SFOrder order = new SFOrder();
        order.orderNumber = returnNullOrValue((String) entityAttributeField.get("Order Number"));
        order.accountId = returnNullOrValue((String) entityAttributeField.get("Account Id"));

        Long effectiveDate = (Long) entityAttributeField.get("Effective Date");
        order.effectiveDate = effectiveDate != null && effectiveDate > 0 ? DatePlus.getYYYYMMDD(effectiveDate) : null;

        order.status = returnNullOrValue((String) entityAttributeField.get("Status"));
        order.description = returnNullOrValue((String) entityAttributeField.get("Description"));
        order.customerAuthorizedById = returnNullOrValue((String) entityAttributeField.get("Customer Authorized By Id"));
        order.companyAuthorizedById = returnNullOrValue((String) entityAttributeField.get("Company Authorized By Id"));
        order.type = returnNullOrValue((String) entityAttributeField.get("Type"));

        order.billingStreet = returnNullOrValue((String) entityAttributeField.get("Billing Street"));
        order.billingCity = returnNullOrValue((String) entityAttributeField.get("Billing City"));
        order.billingState = returnNullOrValue((String) entityAttributeField.get("Billing State"));
        order.billingPostalCode = returnNullOrValue((String) entityAttributeField.get("Billing Postal Code"));
        order.billingCountry = returnNullOrValue((String) entityAttributeField.get("Billing Country"));

        order.shippingStreet = returnNullOrValue((String) entityAttributeField.get("Shipping Street"));
        order.shippingCity = returnNullOrValue((String) entityAttributeField.get("Shipping City"));
        order.shippingState = returnNullOrValue((String) entityAttributeField.get("Shipping State"));
        order.shippingPostalCode = returnNullOrValue((String) entityAttributeField.get("Shipping Postal Code"));
        order.shippingCountry = returnNullOrValue((String) entityAttributeField.get("Shipping Country"));

        order.totalAmount = (Double) entityAttributeField.get("Total Amount");

        Long createdDate = (Long) entityAttributeField.get("Created Date");
        order.createdDate = createdDate != null ? new Date(createdDate) : null;

        order.createdById = returnNullOrValue((String) entityAttributeField.get("Created By Id"));
        order.ownerId = returnNullOrValue((String) entityAttributeField.get("Owner Id"));

        // Extract dynamic custom fields (any field ending with __c)
        order.customFields = extractCustomFields(entityAttributeField);

        return order;
    }
    
    /**
     * Converts a catalog Order entity to SFOrder model.
     * Used for update operations.
     *
     * @param order Catalog Order entity
     * @return SFOrder instance
     */
    public static SFOrder from(Order order) {
        SFOrder sfOrder = new SFOrder();
        sfOrder.id = order.id;
        sfOrder.orderNumber = order.orderNumber;
        sfOrder.accountId = order.accountId;

        if (order.effectiveDate != null && order.effectiveDate > 0)
            sfOrder.effectiveDate = DatePlus.getYYYYMMDD(order.effectiveDate);

        sfOrder.status = order.status;
        sfOrder.description = order.description;
        sfOrder.customerAuthorizedById = order.customerAuthorizedById;
        sfOrder.companyAuthorizedById = order.companyAuthorizedById;
        sfOrder.type = order.type;

        sfOrder.billingStreet = order.billingStreet;
        sfOrder.billingCity = order.billingCity;
        sfOrder.billingState = order.billingState;
        sfOrder.billingPostalCode = order.billingPostalCode;
        sfOrder.billingCountry = order.billingCountry;

        sfOrder.shippingStreet = order.shippingStreet;
        sfOrder.shippingCity = order.shippingCity;
        sfOrder.shippingState = order.shippingState;
        sfOrder.shippingPostalCode = order.shippingPostalCode;
        sfOrder.shippingCountry = order.shippingCountry;

        sfOrder.totalAmount = order.totalAmount;

        if (order.createdDate != null)
            sfOrder.createdDate = new Date(order.createdDate);

        sfOrder.createdById = order.createdById;
        sfOrder.ownerId = order.ownerId;

        // Copy custom fields from Order entity
        sfOrder.customFields = order.customFields != null ? new HashMap<>(order.customFields) : new HashMap<>();

        return sfOrder;
    }

    /**
     * Extracts custom fields from the entity attribute map.
     * Delegates to CustomFieldsExtractor utility for reusable logic.
     *
     * @param entityAttributeField Map containing all field values
     * @return Map containing only custom fields with their Salesforce API names
     */
    private static Map<String, Object> extractCustomFields(Map<String, Object> entityAttributeField) {
        // Define standard Order fields that should NOT be treated as custom fields
        Set<String> standardFields = new HashSet<>(Arrays.asList(
            "Id", "Order Number", "Account Id", "Effective Date", "Status", "Description",
            "Customer Authorized By Id", "Company Authorized By Id", "Type",
            "Billing Street", "Billing City", "Billing State", "Billing Postal Code", "Billing Country",
            "Shipping Street", "Shipping City", "Shipping State", "Shipping Postal Code", "Shipping Country",
            "Total Amount", "Created Date", "Created By Id", "Owner Id", "Custom Fields"
        ));

        return CustomFieldsExtractor.extractCustomFields(entityAttributeField, standardFields, "SFOrder");
    }
}

