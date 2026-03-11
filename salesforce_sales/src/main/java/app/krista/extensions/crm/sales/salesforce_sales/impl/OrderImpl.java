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

import app.krista.extensions.crm.sales.salesforce_sales.model.SFOrder;
import app.krista.extensions.crm.sales.salesforce_sales.service.Order;
import app.krista.extensions.crm.sales.salesforce_sales.util.DatePlus;

import java.util.Map;

/**
 * Implementation of Order service interface.
 * Wraps SFOrder model and provides getter methods.
 */
public class OrderImpl implements Order {
    
    private final SFOrder sfOrder;
    
    public OrderImpl(SFOrder sfOrder) {
        this.sfOrder = sfOrder;
    }
    
    @Override
    public String getId() {
        return sfOrder.id;
    }
    
    @Override
    public String getOrderNumber() {
        return sfOrder.orderNumber;
    }
    
    @Override
    public String getAccountId() {
        return sfOrder.accountId;
    }
    
    @Override
    public Long getEffectiveDate() {
        return sfOrder.effectiveDate != null ? DatePlus.fromYYYYMMDD(sfOrder.effectiveDate).getTime() : null;
    }
    
    @Override
    public String getStatus() {
        return sfOrder.status;
    }
    
    @Override
    public String getDescription() {
        return sfOrder.description;
    }
    
    @Override
    public String getCustomerAuthorizedById() {
        return sfOrder.customerAuthorizedById;
    }
    
    @Override
    public String getCompanyAuthorizedById() {
        return sfOrder.companyAuthorizedById;
    }
    
    @Override
    public String getType() {
        return sfOrder.type;
    }
    
    @Override
    public String getBillingStreet() {
        return sfOrder.billingStreet;
    }
    
    @Override
    public String getBillingCity() {
        return sfOrder.billingCity;
    }
    
    @Override
    public String getBillingState() {
        return sfOrder.billingState;
    }
    
    @Override
    public String getBillingPostalCode() {
        return sfOrder.billingPostalCode;
    }
    
    @Override
    public String getBillingCountry() {
        return sfOrder.billingCountry;
    }
    
    @Override
    public String getShippingStreet() {
        return sfOrder.shippingStreet;
    }
    
    @Override
    public String getShippingCity() {
        return sfOrder.shippingCity;
    }
    
    @Override
    public String getShippingState() {
        return sfOrder.shippingState;
    }
    
    @Override
    public String getShippingPostalCode() {
        return sfOrder.shippingPostalCode;
    }
    
    @Override
    public String getShippingCountry() {
        return sfOrder.shippingCountry;
    }
    
    @Override
    public Double getTotalAmount() {
        return sfOrder.totalAmount;
    }
    
    @Override
    public Long getCreatedDate() {
        return sfOrder.createdDate != null ? sfOrder.createdDate.getTime() : null;
    }
    
    @Override
    public String getCreatedById() {
        return sfOrder.createdById;
    }
    
    @Override
    public String getOwnerId() {
        return sfOrder.ownerId;
    }
    
    @Override
    @Deprecated
    public String getPoNumber() {
        // For backward compatibility, try to get from customFields
        if (sfOrder.customFields != null && sfOrder.customFields.containsKey("PO_Number__c")) {
            return (String) sfOrder.customFields.get("PO_Number__c");
        }
        return null;
    }

    @Override
    @Deprecated
    public String getExtractionStatus() {
        // For backward compatibility, try to get from customFields
        if (sfOrder.customFields != null && sfOrder.customFields.containsKey("Extraction_Status__c")) {
            return (String) sfOrder.customFields.get("Extraction_Status__c");
        }
        return null;
    }

    @Override
    @Deprecated
    public Double getConfidence() {
        // For backward compatibility, try to get from customFields
        if (sfOrder.customFields != null && sfOrder.customFields.containsKey("Confidence__c")) {
            Object value = sfOrder.customFields.get("Confidence__c");
            if (value instanceof Number) {
                return ((Number) value).doubleValue();
            }
        }
        return null;
    }

    @Override
    public Map<String, Object> getCustomFields() {
        return sfOrder.customFields;
    }
}

