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

public class LeadEntityTest {

    @Test
    public void testFromFields_AllFields() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("Id", "00QLEAD");
        fields.put("Title", "VP Sales");
        fields.put("Company", "Acme Corp");
        fields.put("Website", "https://acme.com");
        fields.put("Owner Id", "005OWN");
        fields.put("Is Converted", true);
        fields.put("Converted Account Id", "001ACC");
        fields.put("Converted Contact Id", "003CON");
        fields.put("Converted Opportunity Id", "006OPP");
        fields.put("Clean Status", "Matched");
        PhoneNumber phone = new PhoneNumber(1, 5551234567L);
        PhoneNumber mobile = new PhoneNumber(1, 5559876543L);
        fields.put("Phone", phone);
        fields.put("MobilePhone", mobile);
        fields.put("Email", "john@acme.com");
        fields.put("Description", "Hot lead");
        fields.put("Status", "Qualified");
        fields.put("LeadSource", "Web");
        fields.put("Industry", "Technology");
        fields.put("Rating", "Hot");
        fields.put("Annual Revenue", 5000000.0);
        fields.put("Converted Date", 1700000000000L);
        fields.put("Created Date", 1600000000000L);
        fields.put("Last Modified Date", 1650000000000L);
        fields.put("Name", "John Doe");

        Lead lead = Lead.fromFields(fields);

        assertEquals("00QLEAD", lead.id);
        assertEquals("VP Sales", lead.title);
        assertEquals("Acme Corp", lead.company);
        assertEquals("https://acme.com", lead.website);
        assertEquals("005OWN", lead.ownerId);
        assertTrue(lead.isConverted);
        assertEquals("001ACC", lead.convertedAccountId);
        assertEquals("003CON", lead.convertedContactId);
        assertEquals("006OPP", lead.convertedOpportunityId);
        assertEquals("Matched", lead.cleanStatus);
        assertSame(phone, lead.phone);
        assertSame(mobile, lead.mobilePhone);
        assertEquals("john@acme.com", lead.email);
        assertEquals("Hot lead", lead.description);
        assertEquals("Qualified", lead.status);
        assertEquals("Web", lead.leadSource);
        assertEquals("Technology", lead.industry);
        assertEquals("Hot", lead.rating);
        assertEquals(Double.valueOf(5000000.0), lead.annualRevenue);
        assertEquals(Long.valueOf(1700000000000L), lead.convertedDate);
        assertEquals(Long.valueOf(1600000000000L), lead.createdDate);
        assertEquals(Long.valueOf(1650000000000L), lead.lastModifiedDate);
        assertEquals("John Doe", lead.name);
    }

    @Test
    public void testFromFields_NullName() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("Id", "00QLEAD");
        Lead lead = Lead.fromFields(fields);
        assertNull(lead.name);
    }

    @Test
    public void testToFields_AllFields() {
        Lead lead = new Lead();
        lead.id = "00QLEAD";
        lead.title = "CEO";
        lead.company = "Beta Inc";
        lead.website = "https://beta.com";
        lead.ownerId = "005OWN";
        lead.isConverted = false;
        lead.status = "Open";
        lead.email = "ceo@beta.com";
        lead.industry = "Finance";
        lead.rating = "Warm";
        lead.annualRevenue = 2000000.0;
        lead.createdDate = 1600000000000L;
        lead.lastModifiedDate = 1650000000000L;
        lead.name = "Jane Smith";

        Map<String, Object> result = lead.toFields();

        assertEquals("00QLEAD", result.get("Id"));
        assertEquals("CEO", result.get("Title"));
        assertEquals("Beta Inc", result.get("Company"));
        assertEquals("https://beta.com", result.get("Website"));
        assertEquals("005OWN", result.get("Owner Id"));
        assertEquals(false, result.get("Is Converted"));
        assertEquals("Open", result.get("Status"));
        assertEquals("ceo@beta.com", result.get("Email"));
        assertEquals("Finance", result.get("Industry"));
        assertEquals("Warm", result.get("Rating"));
        assertEquals(2000000.0, result.get("Annual Revenue"));
        assertEquals(1600000000000L, result.get("Created Date"));
        assertEquals(1650000000000L, result.get("Last Modified Date"));
        assertEquals("Jane Smith", result.get("Name"));
    }

    @Test
    public void testRoundTrip() {
        Map<String, Object> original = new HashMap<>();
        original.put("Id", "00QRT");
        original.put("Name", "Round Trip");
        original.put("Company", "RT Corp");
        original.put("Status", "Contacted");
        original.put("Title", null);
        original.put("Website", null);
        original.put("Owner Id", "005X");
        original.put("Is Converted", false);
        original.put("Converted Account Id", null);
        original.put("Converted Contact Id", null);
        original.put("Converted Opportunity Id", null);
        original.put("Clean Status", null);
        original.put("Phone", null);
        original.put("MobilePhone", null);
        original.put("Email", "rt@test.com");
        original.put("Description", null);
        original.put("LeadSource", "Phone");
        original.put("Industry", null);
        original.put("Rating", null);
        original.put("Annual Revenue", null);
        original.put("Converted Date", null);
        original.put("Created Date", null);
        original.put("Last Modified Date", null);

        Lead lead = Lead.fromFields(original);
        Map<String, Object> result = lead.toFields();

        assertEquals(original.get("Id"), result.get("Id"));
        assertEquals(original.get("Name"), result.get("Name"));
        assertEquals(original.get("Company"), result.get("Company"));
        assertEquals(original.get("Status"), result.get("Status"));
    }
}
