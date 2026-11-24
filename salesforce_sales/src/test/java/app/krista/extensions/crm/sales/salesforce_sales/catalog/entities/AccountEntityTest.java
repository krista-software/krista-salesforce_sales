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

import app.krista.model.base.PhoneNumber;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class AccountEntityTest {

    @Test
    public void testFromFields_AllFields() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("Id", "001ABC");
        fields.put("Name", "Acme Corp");
        fields.put("Fax", "555-1234");
        fields.put("Account Number", "AN-001");
        fields.put("Website", "https://acme.com");
        fields.put("Sic", "1234");
        fields.put("Owner Id", "005OWN");
        fields.put("Created By Id", "005CRE");
        fields.put("Clean Status", "Matched");
        fields.put("SLA Serial Number", "SLA-001");
        fields.put("Created Date", 1700000000000L);
        fields.put("Type", "Customer");
        fields.put("Billing Street", "123 Main St");
        fields.put("Billing City", "Springfield");
        fields.put("Billing State", "IL");
        fields.put("Billing Postal Code", "62701");
        fields.put("Billing Country", "US");
        fields.put("Shipping Street", "456 Oak Ave");
        fields.put("Shipping City", "Shelbyville");
        fields.put("Shipping State", "IL");
        fields.put("Shipping Postal Code", "62565");
        fields.put("Shipping Country", "US");
        PhoneNumber phone = new PhoneNumber(1, 5551234567L);
        fields.put("Phone", phone);
        fields.put("Industry", "Technology");
        fields.put("Number Of Employees", 500.0);
        fields.put("Ownership", "Public");
        fields.put("CustomerPriority", "High");
        fields.put("SLA Expiration Date", 1800000000000L);
        fields.put(" Annual Revenue", 1000000.0);
        Map<String, Object> customFields = Map.of("Custom__c", "val");
        fields.put("Custom Fields", customFields);

        Account account = Account.fromFields(fields);

        assertEquals("001ABC", account.id);
        assertEquals("Acme Corp", account.name);
        assertEquals("555-1234", account.fax);
        assertEquals("AN-001", account.accountNumber);
        assertEquals("https://acme.com", account.website);
        assertEquals("1234", account.sic);
        assertEquals("005OWN", account.ownerId);
        assertEquals("005CRE", account.createdById);
        assertEquals("Matched", account.cleanStatus);
        assertEquals("SLA-001", account.sLASerialNumber);
        assertEquals(Long.valueOf(1700000000000L), account.createdDate);
        assertEquals("Customer", account.type);
        assertEquals("123 Main St", account.billingStreet);
        assertEquals("Springfield", account.billingCity);
        assertEquals("IL", account.billingState);
        assertEquals("62701", account.billingPostalCode);
        assertEquals("US", account.billingCountry);
        assertEquals("456 Oak Ave", account.shippingStreet);
        assertEquals("Shelbyville", account.shippingCity);
        assertEquals("IL", account.shippingState);
        assertEquals("62565", account.shippingPostalCode);
        assertEquals("US", account.shippingCountry);
        assertSame(phone, account.phone);
        assertEquals("Technology", account.industry);
        assertEquals(Double.valueOf(500.0), account.numberOfEmployees);
        assertEquals("Public", account.ownership);
        assertEquals("High", account.customerPriority);
        assertEquals(Long.valueOf(1800000000000L), account.sLAExpirationDate);
        assertEquals(Double.valueOf(1000000.0), account.annualRevenue);
        assertEquals("val", account.customFields.get("Custom__c"));
    }

    @Test
    public void testFromFields_NullCustomFields() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("Id", "001ABC");
        fields.put("Name", "Test");
        Account account = Account.fromFields(fields);
        assertNull(account.customFields);
    }

    @Test
    public void testToFields_AllFields() {
        Account account = new Account();
        account.id = "001ABC";
        account.name = "Acme Corp";
        account.fax = "555-1234";
        account.accountNumber = "AN-001";
        account.website = "https://acme.com";
        account.sic = "1234";
        account.ownerId = "005OWN";
        account.createdById = "005CRE";
        account.cleanStatus = "Matched";
        account.sLASerialNumber = "SLA-001";
        account.createdDate = 1700000000000L;
        account.type = "Customer";
        account.billingStreet = "123 Main St";
        account.billingCity = "Springfield";
        account.billingState = "IL";
        account.billingPostalCode = "62701";
        account.billingCountry = "US";
        account.shippingStreet = "456 Oak Ave";
        account.shippingCity = "Shelbyville";
        account.shippingState = "IL";
        account.shippingPostalCode = "62565";
        account.shippingCountry = "US";
        account.industry = "Technology";
        account.numberOfEmployees = 500.0;
        account.ownership = "Public";
        account.customerPriority = "High";
        account.sLAExpirationDate = 1800000000000L;
        account.annualRevenue = 1000000.0;

        Map<String, Object> result = account.toFields();

        assertEquals("001ABC", result.get("Id"));
        assertEquals("Acme Corp", result.get("Name"));
        assertEquals("555-1234", result.get("Fax"));
        assertEquals("AN-001", result.get("Account Number"));
        assertEquals("https://acme.com", result.get("Website"));
        assertEquals("1234", result.get("Sic"));
        assertEquals("005OWN", result.get("Owner Id"));
        assertEquals("005CRE", result.get("Created By Id"));
        assertEquals("Matched", result.get("Clean Status"));
        assertEquals("SLA-001", result.get("SLA Serial Number"));
        assertEquals(1700000000000L, result.get("Created Date"));
        assertEquals("Customer", result.get("Type"));
        assertEquals("123 Main St", result.get("Billing Street"));
        assertEquals("Springfield", result.get("Billing City"));
        assertEquals("IL", result.get("Billing State"));
        assertEquals("62701", result.get("Billing Postal Code"));
        assertEquals("US", result.get("Billing Country"));
        assertEquals("456 Oak Ave", result.get("Shipping Street"));
        assertEquals("Shelbyville", result.get("Shipping City"));
        assertEquals("IL", result.get("Shipping State"));
        assertEquals("62565", result.get("Shipping Postal Code"));
        assertEquals("US", result.get("Shipping Country"));
        assertEquals("Technology", result.get("Industry"));
        assertEquals(500.0, result.get("Number Of Employees"));
        assertEquals("Public", result.get("Ownership"));
        assertEquals("High", result.get("CustomerPriority"));
        assertEquals(1800000000000L, result.get("SLA Expiration Date"));
        assertEquals(1000000.0, result.get(" Annual Revenue"));
    }

    @Test
    public void testRoundTrip_fromFieldsThenToFields() {
        Map<String, Object> original = new HashMap<>();
        original.put("Id", "001XYZ");
        original.put("Name", "RoundTrip Inc");
        original.put("Fax", "555-9999");
        original.put("Account Number", "RT-100");
        original.put("Website", "https://roundtrip.com");
        original.put("Sic", "5678");
        original.put("Owner Id", "005RT");
        original.put("Created By Id", "005CR");
        original.put("Clean Status", "Pending");
        original.put("SLA Serial Number", "SLA-RT");
        original.put("Created Date", 1600000000000L);
        original.put("Type", "Partner");
        original.put("Billing Street", "789 Elm");
        original.put("Billing City", "Capital");
        original.put("Billing State", "CA");
        original.put("Billing Postal Code", "90001");
        original.put("Billing Country", "US");
        original.put("Shipping Street", "101 Pine");
        original.put("Shipping City", "Harbor");
        original.put("Shipping State", "CA");
        original.put("Shipping Postal Code", "90002");
        original.put("Shipping Country", "US");
        original.put("Phone", null);
        original.put("Industry", "Finance");
        original.put("Number Of Employees", 100.0);
        original.put("Ownership", "Private");
        original.put("CustomerPriority", "Medium");
        original.put("SLA Expiration Date", 1700000000000L);
        original.put(" Annual Revenue", 500000.0);

        Account account = Account.fromFields(original);
        Map<String, Object> result = account.toFields();

        assertEquals(original.get("Id"), result.get("Id"));
        assertEquals(original.get("Name"), result.get("Name"));
        assertEquals(original.get("Industry"), result.get("Industry"));
        assertEquals(original.get("Type"), result.get("Type"));
        assertEquals(original.get("Owner Id"), result.get("Owner Id"));
    }
}
