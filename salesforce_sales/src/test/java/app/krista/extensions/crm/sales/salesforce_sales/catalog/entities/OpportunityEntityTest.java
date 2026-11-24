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

public class OpportunityEntityTest {

    @Test
    public void testFromFields_AllFields() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("Id", "006OPP");
        fields.put("Name", "Big Deal");
        fields.put("Contact Id", "003CON");
        fields.put("Owner Id", "005OWN");
        fields.put("Account Id", "001ACC");
        fields.put("Pricebook Id", "01sPB");
        fields.put("Stage Name", "Closed Won");
        fields.put("Amount", 50000.0);
        fields.put("Probability", 90.0);
        fields.put("Description", "Large enterprise deal");
        fields.put("Expected Revenue", 45000.0);
        fields.put("Last Modified Date", 1700000000000L);
        fields.put("Created Date", 1600000000000L);
        fields.put("Close Date", 1750000000000L);

        Opportunity opp = Opportunity.fromFields(fields);

        assertEquals("006OPP", opp.id);
        assertEquals("Big Deal", opp.name);
        assertEquals("003CON", opp.contactId);
        assertEquals("005OWN", opp.ownerId);
        assertEquals("001ACC", opp.accountId);
        assertEquals("01sPB", opp.pricebookId);
        assertEquals("Closed Won", opp.stageName);
        assertEquals(Double.valueOf(50000.0), opp.amount);
        assertEquals(Double.valueOf(90.0), opp.probability);
        assertEquals("Large enterprise deal", opp.description);
        assertEquals(Double.valueOf(45000.0), opp.expectedRevenue);
        assertEquals(Long.valueOf(1700000000000L), opp.lastModifiedDate);
        assertEquals(Long.valueOf(1600000000000L), opp.createdDate);
        assertEquals(Long.valueOf(1750000000000L), opp.closeDate);
    }

    @Test
    public void testFromFields_NullCloseDate() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("Id", "006OPP");
        fields.put("Close Date", null);
        Opportunity opp = Opportunity.fromFields(fields);
        assertNull(opp.closeDate);
    }

    @Test
    public void testFromFields_ZeroCloseDate() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("Id", "006OPP");
        fields.put("Close Date", 0L);
        Opportunity opp = Opportunity.fromFields(fields);
        assertNull(opp.closeDate);
    }

    @Test
    public void testToFields_AllFields() {
        Opportunity opp = new Opportunity();
        opp.id = "006OPP";
        opp.name = "Big Deal";
        opp.contactId = "003CON";
        opp.ownerId = "005OWN";
        opp.ownerName = "John Owner";
        opp.accountId = "001ACC";
        opp.accountName = "Acme Corp";
        opp.type = "New Business";
        opp.leadSource = "Web";
        opp.pricebookId = "01sPB";
        opp.stageName = "Prospecting";
        opp.nextStep = "Demo";
        opp.amount = 50000.0;
        opp.acv = 48000.0;
        opp.partner = "Partner Co";
        opp.competition = "Competitor Inc";
        opp.probability = 75.0;
        opp.description = "Enterprise deal";
        opp.expectedRevenue = 37500.0;
        opp.lastModifiedDate = 1700000000000L;
        opp.createdDate = 1600000000000L;
        opp.closeDate = 1750000000000L;

        Map<String, Object> result = opp.toFields();

        assertEquals("006OPP", result.get("Id"));
        assertEquals("Big Deal", result.get("Name"));
        assertEquals("003CON", result.get("Contact Id"));
        assertEquals("005OWN", result.get("Owner Id"));
        assertEquals("John Owner", result.get("Owner Name"));
        assertEquals("001ACC", result.get("Account Id"));
        assertEquals("Acme Corp", result.get("Account Name"));
        assertEquals("New Business", result.get("Type"));
        assertEquals("Web", result.get("Lead Source"));
        assertEquals("01sPB", result.get("Pricebook Id"));
        assertEquals("Prospecting", result.get("Stage Name"));
        assertEquals("Demo", result.get("Next Step"));
        assertEquals(50000.0, result.get("Amount"));
        assertEquals(48000.0, result.get("Acv"));
        assertEquals("Partner Co", result.get("Partner"));
        assertEquals("Competitor Inc", result.get("Competition"));
        assertEquals(75.0, result.get("Probability"));
        assertEquals("Enterprise deal", result.get("Description"));
        assertEquals(37500.0, result.get("Expected Revenue"));
        assertEquals(1700000000000L, result.get("Last Modified Date"));
        assertEquals(1600000000000L, result.get("Created Date"));
        assertEquals(1750000000000L, result.get("Close Date"));
    }

    @Test
    public void testRoundTrip() {
        Map<String, Object> original = new HashMap<>();
        original.put("Id", "006RT");
        original.put("Name", "RoundTrip Opp");
        original.put("Contact Id", null);
        original.put("Owner Id", "005X");
        original.put("Account Id", "001X");
        original.put("Pricebook Id", null);
        original.put("Stage Name", "Qualification");
        original.put("Amount", 10000.0);
        original.put("Probability", 50.0);
        original.put("Description", null);
        original.put("Expected Revenue", 5000.0);
        original.put("Last Modified Date", 1700000000000L);
        original.put("Created Date", 1600000000000L);
        original.put("Close Date", 1800000000000L);

        Opportunity opp = Opportunity.fromFields(original);
        Map<String, Object> result = opp.toFields();

        assertEquals(original.get("Id"), result.get("Id"));
        assertEquals(original.get("Name"), result.get("Name"));
        assertEquals(original.get("Stage Name"), result.get("Stage Name"));
        assertEquals(original.get("Amount"), result.get("Amount"));
    }
}
