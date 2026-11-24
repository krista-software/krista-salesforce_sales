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

public class OrderItemEntityTest {

    @Test
    public void testFromFields_AllFields() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("Id", "802OI");
        fields.put("Order Id", "801ORD");
        fields.put("Pricebook Entry Id", "01uPBE");
        fields.put("Product Id", "01tPROD");
        fields.put("Quantity", 10.0);
        fields.put("Unit Price", 99.99);
        fields.put("Total Price", 999.90);
        fields.put("List Price", 109.99);
        fields.put("Description", "Line item 1");
        fields.put("Order Item Number", "OI-0001");
        fields.put("Service Date", 1700000000000L);
        fields.put("End Date", 1800000000000L);
        fields.put("Created Date", 1600000000000L);
        fields.put("Created By Id", "005CRE");
        fields.put("Last Modified Date", 1650000000000L);
        fields.put("Last Modified By Id", "005MOD");
        Map<String, Object> custom = Map.of("Custom__c", "val");
        fields.put("Custom Fields", custom);

        OrderItem item = OrderItem.fromFields(fields);

        assertEquals("802OI", item.id);
        assertEquals("801ORD", item.orderId);
        assertEquals("01uPBE", item.pricebookEntryId);
        assertEquals("01tPROD", item.product2Id);
        assertEquals(Double.valueOf(10.0), item.quantity);
        assertEquals(Double.valueOf(99.99), item.unitPrice);
        assertEquals(Double.valueOf(999.90), item.totalPrice);
        assertEquals(Double.valueOf(109.99), item.listPrice);
        assertEquals("Line item 1", item.description);
        assertEquals("OI-0001", item.orderItemNumber);
        assertEquals(Long.valueOf(1700000000000L), item.serviceDate);
        assertEquals(Long.valueOf(1800000000000L), item.endDate);
        assertEquals(Long.valueOf(1600000000000L), item.createdDate);
        assertEquals("005CRE", item.createdById);
        assertEquals(Long.valueOf(1650000000000L), item.lastModifiedDate);
        assertEquals("005MOD", item.lastModifiedById);
        assertEquals("val", item.customFields.get("Custom__c"));
    }

    @Test
    public void testFromFields_NullCustomFields() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("Id", "802OI");
        fields.put("Quantity", 1.0);
        fields.put("Unit Price", 10.0);
        OrderItem item = OrderItem.fromFields(fields);
        assertNull(item.customFields);
    }

    @Test
    public void testToFields_AllFields() {
        OrderItem item = new OrderItem();
        item.id = "802OI";
        item.orderId = "801ORD";
        item.pricebookEntryId = "01uPBE";
        item.product2Id = "01tPROD";
        item.quantity = 5.0;
        item.unitPrice = 200.0;
        item.totalPrice = 1000.0;
        item.listPrice = 220.0;
        item.description = "Test item";
        item.orderItemNumber = "OI-0002";
        item.serviceDate = 1700000000000L;
        item.endDate = 1800000000000L;
        item.createdDate = 1600000000000L;
        item.createdById = "005CRE";
        item.lastModifiedDate = 1650000000000L;
        item.lastModifiedById = "005MOD";

        Map<String, Object> result = item.toFields();

        assertEquals("802OI", result.get("Id"));
        assertEquals("801ORD", result.get("Order Id"));
        assertEquals("01uPBE", result.get("Pricebook Entry Id"));
        assertEquals("01tPROD", result.get("Product Id"));
        assertEquals(5.0, result.get("Quantity"));
        assertEquals(200.0, result.get("Unit Price"));
        assertEquals(1000.0, result.get("Total Price"));
        assertEquals(220.0, result.get("List Price"));
        assertEquals("Test item", result.get("Description"));
        assertEquals("OI-0002", result.get("Order Item Number"));
        assertEquals(1700000000000L, result.get("Service Date"));
        assertEquals(1800000000000L, result.get("End Date"));
        assertEquals(1600000000000L, result.get("Created Date"));
        assertEquals("005CRE", result.get("Created By Id"));
        assertEquals(1650000000000L, result.get("Last Modified Date"));
        assertEquals("005MOD", result.get("Last Modified By Id"));
    }
}
