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

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class OrderEntityTest {

    @Test
    public void testFromFields_AllFields() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("Id", "801ORD");
        fields.put("Order Number", "ORD-0001");
        fields.put("Account Id", "001ACC");
        fields.put("Effective Date", 1700000000000L);
        fields.put("Status", "Draft");
        fields.put("Description", "Test order");
        fields.put("Customer Authorized By Id", "005CUST");
        fields.put("Company Authorized By Id", "005COMP");
        fields.put("Type", "Standard");
        fields.put("Billing Street", "123 Main");
        fields.put("Billing City", "Springfield");
        fields.put("Billing State", "IL");
        fields.put("Billing Postal Code", "62701");
        fields.put("Billing Country", "US");
        fields.put("Shipping Street", "456 Oak");
        fields.put("Shipping City", "Shelbyville");
        fields.put("Shipping State", "IL");
        fields.put("Shipping Postal Code", "62565");
        fields.put("Shipping Country", "US");
        fields.put("Total Amount", 10000.0);
        fields.put("Created Date", 1600000000000L);
        fields.put("Created By Id", "005CRE");
        fields.put("Owner Id", "005OWN");
        Map<String, Object> custom = Map.of("PO_Number__c", "PO-123");
        fields.put("Custom Fields", custom);

        Order order = Order.fromFields(fields);

        assertEquals("801ORD", order.id);
        assertEquals("ORD-0001", order.orderNumber);
        assertEquals("001ACC", order.accountId);
        assertEquals(Long.valueOf(1700000000000L), order.effectiveDate);
        assertEquals("Draft", order.status);
        assertEquals("Test order", order.description);
        assertEquals("005CUST", order.customerAuthorizedById);
        assertEquals("005COMP", order.companyAuthorizedById);
        assertEquals("Standard", order.type);
        assertEquals("123 Main", order.billingStreet);
        assertEquals("Springfield", order.billingCity);
        assertEquals("IL", order.billingState);
        assertEquals("62701", order.billingPostalCode);
        assertEquals("US", order.billingCountry);
        assertEquals("456 Oak", order.shippingStreet);
        assertEquals("Shelbyville", order.shippingCity);
        assertEquals("IL", order.shippingState);
        assertEquals("62565", order.shippingPostalCode);
        assertEquals("US", order.shippingCountry);
        assertEquals(Double.valueOf(10000.0), order.totalAmount);
        assertEquals(Long.valueOf(1600000000000L), order.createdDate);
        assertEquals("005CRE", order.createdById);
        assertEquals("005OWN", order.ownerId);
        assertEquals("PO-123", order.customFields.get("PO_Number__c"));
    }

    @Test
    public void testFromFields_NullCustomFields() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("Id", "801ORD");
        Order order = Order.fromFields(fields);
        assertNull(order.customFields);
    }

    @Test
    public void testFromFields_NonMapCustomFields() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("Id", "801ORD");
        fields.put("Custom Fields", "not a map");
        Order order = Order.fromFields(fields);
        assertNull(order.customFields);
    }

    @Test
    public void testToFields_AllFields() {
        Order order = new Order();
        order.id = "801ORD";
        order.orderNumber = "ORD-0001";
        order.accountId = "001ACC";
        order.effectiveDate = 1700000000000L;
        order.status = "Activated";
        order.description = "Active order";
        order.customerAuthorizedById = "005CUST";
        order.companyAuthorizedById = "005COMP";
        order.type = "Standard";
        order.billingStreet = "123 Main";
        order.billingCity = "Springfield";
        order.billingState = "IL";
        order.billingPostalCode = "62701";
        order.billingCountry = "US";
        order.shippingStreet = "456 Oak";
        order.shippingCity = "Shelbyville";
        order.shippingState = "IL";
        order.shippingPostalCode = "62565";
        order.shippingCountry = "US";
        order.totalAmount = 10000.0;
        order.createdDate = 1600000000000L;
        order.createdById = "005CRE";
        order.ownerId = "005OWN";
        order.customFields = Map.of("Custom__c", "val");

        Map<String, Object> result = order.toFields();

        assertEquals("801ORD", result.get("Id"));
        assertEquals("ORD-0001", result.get("Order Number"));
        assertEquals("001ACC", result.get("Account Id"));
        assertEquals(1700000000000L, result.get("Effective Date"));
        assertEquals("Activated", result.get("Status"));
        assertEquals("Active order", result.get("Description"));
        assertEquals("005CUST", result.get("Customer Authorized By Id"));
        assertEquals("005COMP", result.get("Company Authorized By Id"));
        assertEquals("Standard", result.get("Type"));
        assertEquals("123 Main", result.get("Billing Street"));
        assertEquals("Springfield", result.get("Billing City"));
        assertEquals("IL", result.get("Billing State"));
        assertEquals("62701", result.get("Billing Postal Code"));
        assertEquals("US", result.get("Billing Country"));
        assertEquals("456 Oak", result.get("Shipping Street"));
        assertEquals("Shelbyville", result.get("Shipping City"));
        assertEquals("IL", result.get("Shipping State"));
        assertEquals("62565", result.get("Shipping Postal Code"));
        assertEquals("US", result.get("Shipping Country"));
        assertEquals(10000.0, result.get("Total Amount"));
        assertEquals(1600000000000L, result.get("Created Date"));
        assertEquals("005CRE", result.get("Created By Id"));
        assertEquals("005OWN", result.get("Owner Id"));
        assertNotNull(result.get("Custom Fields"));
    }

    @Test
    public void testRoundTrip() {
        Map<String, Object> original = new HashMap<>();
        original.put("Id", "801RT");
        original.put("Order Number", "ORD-RT");
        original.put("Account Id", "001RT");
        original.put("Effective Date", 1700000000000L);
        original.put("Status", "Draft");
        original.put("Description", null);
        original.put("Customer Authorized By Id", null);
        original.put("Company Authorized By Id", null);
        original.put("Type", null);
        original.put("Billing Street", null);
        original.put("Billing City", null);
        original.put("Billing State", null);
        original.put("Billing Postal Code", null);
        original.put("Billing Country", null);
        original.put("Shipping Street", null);
        original.put("Shipping City", null);
        original.put("Shipping State", null);
        original.put("Shipping Postal Code", null);
        original.put("Shipping Country", null);
        original.put("Total Amount", null);
        original.put("Created Date", null);
        original.put("Created By Id", null);
        original.put("Owner Id", null);

        Order order = Order.fromFields(original);
        Map<String, Object> result = order.toFields();

        assertEquals(original.get("Id"), result.get("Id"));
        assertEquals(original.get("Order Number"), result.get("Order Number"));
        assertEquals(original.get("Account Id"), result.get("Account Id"));
        assertEquals(original.get("Status"), result.get("Status"));
    }
}
