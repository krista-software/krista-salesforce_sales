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

/**
 * Tests for Asset, Event, PricebookEntry, Product, Quote, Task, User entities.
 */
public class SmallEntitiesTest {

    // ========== Asset ==========

    @Test
    public void testAsset_FromFields() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("Id", "02iAST");
        fields.put("Contact Id", "003CON");
        fields.put("Account Id", "001ACC");
        fields.put("Root Asset Id", "02iROOT");
        fields.put("Is Competitor Product", true);
        fields.put("Created Date", 1600000000000L);
        fields.put("Created By Id", "005CRE");
        fields.put("Last Modified Date", 1650000000000L);
        fields.put("Is Deleted", false);
        fields.put("Name", "Server Rack");
        fields.put("Owner Id", "005OWN");

        Asset asset = Asset.fromFields(fields);

        assertEquals("02iAST", asset.id);
        assertEquals("003CON", asset.contactId);
        assertEquals("001ACC", asset.accountId);
        assertEquals("02iROOT", asset.rootAssetId);
        assertTrue(asset.isCompetitorProduct);
        assertEquals(Long.valueOf(1600000000000L), asset.createdDate);
        assertEquals("005CRE", asset.createdById);
        assertEquals(Long.valueOf(1650000000000L), asset.lastModifiedDate);
        assertFalse(asset.isDeleted);
        assertEquals("Server Rack", asset.name);
        assertEquals("005OWN", asset.ownerId);
    }

    @Test
    public void testAsset_ToFields() {
        Asset asset = new Asset();
        asset.id = "02iAST";
        asset.contactId = "003CON";
        asset.accountId = "001ACC";
        asset.name = "Laptop";
        asset.ownerId = "005OWN";
        asset.isCompetitorProduct = false;
        asset.isDeleted = false;

        Map<String, Object> result = asset.toFields();

        assertEquals("02iAST", result.get("Id"));
        assertEquals("003CON", result.get("Contact Id"));
        assertEquals("001ACC", result.get("Account Id"));
        assertEquals("Laptop", result.get("Name"));
        assertEquals("005OWN", result.get("Owner Id"));
        assertEquals(false, result.get("Is Competitor Product"));
        assertEquals(false, result.get("Is Deleted"));
    }

    @Test
    public void testAsset_RoundTrip() {
        Map<String, Object> original = new HashMap<>();
        original.put("Id", "02iRT");
        original.put("Contact Id", null);
        original.put("Account Id", "001X");
        original.put("Root Asset Id", null);
        original.put("Is Competitor Product", false);
        original.put("Created Date", 1600000000000L);
        original.put("Created By Id", "005X");
        original.put("Last Modified Date", null);
        original.put("Is Deleted", false);
        original.put("Name", "Widget");
        original.put("Owner Id", "005Y");

        Asset asset = Asset.fromFields(original);
        Map<String, Object> result = asset.toFields();

        assertEquals(original.get("Id"), result.get("Id"));
        assertEquals(original.get("Name"), result.get("Name"));
        assertEquals(original.get("Account Id"), result.get("Account Id"));
    }

    // ========== Event ==========

    @Test
    public void testEvent_FromFields() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("Id", "00UEVT");
        fields.put("Lead Id", "00QLEAD");
        fields.put("Subject", "Demo Call");
        fields.put("Owner Id", "005OWN");
        fields.put("Account Id", "001ACC");
        fields.put("Created Date", 1600000000000L);
        fields.put("Is Private", false);
        fields.put("Is Group Event", true);
        fields.put("Last Modified Date", 1650000000000L);
        fields.put("Location", "Zoom");
        fields.put("Start Date Time", 1700000000000L);
        fields.put("End Date Time", 1700003600000L);

        Event event = Event.fromFields(fields);

        assertEquals("00UEVT", event.id);
        assertEquals("00QLEAD", event.leadId);
        assertEquals("Demo Call", event.subject);
        assertEquals("005OWN", event.ownerId);
        assertEquals("001ACC", event.accountId);
        assertEquals(Long.valueOf(1600000000000L), event.createdDate);
        assertFalse(event.isPrivate);
        assertTrue(event.isGroupEvent);
        assertEquals(Long.valueOf(1650000000000L), event.lastModifiedDate);
        assertEquals("Zoom", event.location);
        assertEquals(Long.valueOf(1700000000000L), event.startDateTime);
        assertEquals(Long.valueOf(1700003600000L), event.endDateTime);
    }

    @Test
    public void testEvent_ToFields() {
        Event event = new Event();
        event.id = "00UEVT";
        event.leadId = "00QLEAD";
        event.subject = "Meeting";
        event.ownerId = "005OWN";
        event.accountId = "001ACC";
        event.createdDate = 1600000000000L;
        event.isPrivate = true;
        event.isGroupEvent = false;
        event.lastModifiedDate = 1650000000000L;
        event.location = "Office";
        event.startDateTime = 1700000000000L;
        event.endDateTime = 1700003600000L;

        Map<String, Object> result = event.toFields();

        assertEquals("00UEVT", result.get("Id"));
        assertEquals("00QLEAD", result.get("Lead Id"));
        assertEquals("Meeting", result.get("Subject"));
        assertEquals("005OWN", result.get("Owner Id"));
        assertEquals("001ACC", result.get("Account Id"));
        assertEquals(1600000000000L, result.get("Created Date"));
        assertEquals(true, result.get("Is Private"));
        assertEquals(false, result.get("Is Group Event"));
        assertEquals(1650000000000L, result.get("Last Modified Date"));
        assertEquals("Office", result.get("Location"));
        assertEquals(1700000000000L, result.get("Start Date Time"));
        assertEquals(1700003600000L, result.get("End Date Time"));
    }

    // ========== PricebookEntry ==========

    @Test
    public void testPricebookEntry_FromFields() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("Id", "01uPBE");
        fields.put("Name", "Standard Entry");
        fields.put("Pricebook Id", "01sPB");
        fields.put("Product Id", "01tPROD");
        fields.put("Product Code", "PROD-001");
        fields.put("Created Date", 1600000000000L);
        fields.put("Last Modified Date", 1650000000000L);
        fields.put("Unit Price", 99.99);

        PricebookEntry pbe = PricebookEntry.fromFields(fields);

        assertEquals("01uPBE", pbe.id);
        assertEquals("Standard Entry", pbe.name);
        assertEquals("01sPB", pbe.pricebookId);
        assertEquals("01tPROD", pbe.productId);
        assertEquals("PROD-001", pbe.productCode);
        assertEquals(Long.valueOf(1600000000000L), pbe.createdDate);
        assertEquals(Long.valueOf(1650000000000L), pbe.lastModifiedDate);
        assertEquals(Double.valueOf(99.99), pbe.unitPrice);
    }

    @Test
    public void testPricebookEntry_ToFields() {
        PricebookEntry pbe = new PricebookEntry();
        pbe.id = "01uPBE";
        pbe.name = "Entry";
        pbe.pricebookId = "01sPB";
        pbe.productId = "01tPROD";
        pbe.productCode = "CODE";
        pbe.createdDate = 1600000000000L;
        pbe.lastModifiedDate = 1650000000000L;
        pbe.unitPrice = 50.0;

        Map<String, Object> result = pbe.toFields();

        assertEquals("01uPBE", result.get("Id"));
        assertEquals("Entry", result.get("Name"));
        assertEquals("01sPB", result.get("Pricebook Id"));
        assertEquals("01tPROD", result.get("Product Id"));
        assertEquals("CODE", result.get("Product Code"));
        assertEquals(1600000000000L, result.get("Created Date"));
        assertEquals(1650000000000L, result.get("Last Modified Date"));
        assertEquals(50.0, result.get("Unit Price"));
    }

    // ========== Product ==========

    @Test
    public void testProduct_FromFields() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("Id", "01tPROD");
        fields.put("Name", "Widget Pro");
        fields.put("Product Code", "WP-001");
        fields.put("Description", "Premium widget");
        fields.put("Is Active", true);
        fields.put("Family", "Hardware");
        fields.put("Created Date", 1600000000000L);
        fields.put("Created By Id", "005CRE");
        fields.put("Last Modified Date", 1650000000000L);
        fields.put("Last Modified By Id", "005MOD");

        Product product = Product.fromFields(fields);

        assertEquals("01tPROD", product.id);
        assertEquals("Widget Pro", product.name);
        assertEquals("WP-001", product.productCode);
        assertEquals("Premium widget", product.description);
        assertTrue(product.isActive);
        assertEquals("Hardware", product.family);
        assertEquals(Long.valueOf(1600000000000L), product.createdDate);
        assertEquals("005CRE", product.createdById);
        assertEquals(Long.valueOf(1650000000000L), product.lastModifiedDate);
        assertEquals("005MOD", product.lastModifiedById);
    }

    @Test
    public void testProduct_ToFields() {
        Product product = new Product();
        product.id = "01tPROD";
        product.name = "Gadget";
        product.productCode = "GDG-001";
        product.description = "A gadget";
        product.isActive = false;
        product.family = "Software";
        product.createdDate = 1600000000000L;
        product.createdById = "005CRE";
        product.lastModifiedDate = 1650000000000L;
        product.lastModifiedById = "005MOD";

        Map<String, Object> result = product.toFields();

        assertEquals("01tPROD", result.get("Id"));
        assertEquals("Gadget", result.get("Name"));
        assertEquals("GDG-001", result.get("Product Code"));
        assertEquals("A gadget", result.get("Description"));
        assertEquals(false, result.get("Is Active"));
        assertEquals("Software", result.get("Family"));
        assertEquals(1600000000000L, result.get("Created Date"));
        assertEquals("005CRE", result.get("Created By Id"));
        assertEquals(1650000000000L, result.get("Last Modified Date"));
        assertEquals("005MOD", result.get("Last Modified By Id"));
    }

    // ========== Quote ==========

    @Test
    public void testQuote_FromFields() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("Id", "0Q0QUO");
        fields.put("Name", "Q-001");
        fields.put("Created Date", 1600000000000L);
        fields.put("Opportunity Id", "006OPP");
        fields.put("Pricebook Id", "01sPB");
        fields.put("Contact Id", "003CON");
        fields.put("Shipping Handling", 50.0);
        fields.put("Tax", 100.0);
        fields.put("Expiration Date", 1800000000000L);
        fields.put("Subtotal", 5000.0);
        fields.put("Line Item Count", 3.0);
        fields.put("Billing Name", "Acme Corp");
        fields.put("Account Id", "001ACC");
        fields.put("Quote Number", 1001.0);
        fields.put("Status", "Draft");
        fields.put("Description", "Initial quote");
        fields.put("Total Price", 5150.0);
        fields.put("Discount", 10.0);
        fields.put("GrandTotal", 5250.0);

        Quote quote = Quote.fromFields(fields);

        assertEquals("0Q0QUO", quote.id);
        assertEquals("Q-001", quote.name);
        assertEquals(Long.valueOf(1600000000000L), quote.createdDate);
        assertEquals("006OPP", quote.opportunityId);
        assertEquals("01sPB", quote.pricebookId);
        assertEquals("003CON", quote.contactId);
        assertEquals(Double.valueOf(50.0), quote.shippingHandling);
        assertEquals(Double.valueOf(100.0), quote.tax);
        assertEquals(Long.valueOf(1800000000000L), quote.expirationDate);
        assertEquals(Double.valueOf(5000.0), quote.subtotal);
        assertEquals(Double.valueOf(3.0), quote.lineItemCount);
        assertEquals("Acme Corp", quote.billingName);
        assertEquals("001ACC", quote.accountId);
        assertEquals(Double.valueOf(1001.0), quote.quoteNumber);
        assertEquals("Draft", quote.status);
        assertEquals("Initial quote", quote.description);
        assertEquals(Double.valueOf(5150.0), quote.totalPrice);
        assertEquals(Double.valueOf(10.0), quote.discount);
        assertEquals(Double.valueOf(5250.0), quote.grandTotal);
    }

    @Test
    public void testQuote_ToFields() {
        Quote quote = new Quote();
        quote.id = "0Q0QUO";
        quote.name = "Q-002";
        quote.createdDate = 1600000000000L;
        quote.opportunityId = "006OPP";
        quote.pricebookId = "01sPB";
        quote.contactId = "003CON";
        quote.shippingHandling = 25.0;
        quote.tax = 75.0;
        quote.expirationDate = 1800000000000L;
        quote.subtotal = 3000.0;
        quote.lineItemCount = 2.0;
        quote.billingName = "Beta Inc";
        quote.accountId = "001ACC";
        quote.quoteNumber = 1002.0;
        quote.status = "Approved";
        quote.description = "Final quote";
        quote.totalPrice = 3100.0;
        quote.discount = 5.0;
        quote.grandTotal = 3175.0;

        Map<String, Object> result = quote.toFields();

        assertEquals("0Q0QUO", result.get("Id"));
        assertEquals("Q-002", result.get("Name"));
        assertEquals(1600000000000L, result.get("Created Date"));
        assertEquals("006OPP", result.get("Opportunity Id"));
        assertEquals("01sPB", result.get("Pricebook Id"));
        assertEquals("003CON", result.get("Contact Id"));
        assertEquals(25.0, result.get("Shipping Handling"));
        assertEquals(75.0, result.get("Tax"));
        assertEquals(1800000000000L, result.get("Expiration Date"));
        assertEquals(3000.0, result.get("Subtotal"));
        assertEquals(2.0, result.get("Line Item Count"));
        assertEquals("Beta Inc", result.get("Billing Name"));
        assertEquals("001ACC", result.get("Account Id"));
        assertEquals(1002.0, result.get("Quote Number"));
        assertEquals("Approved", result.get("Status"));
        assertEquals("Final quote", result.get("Description"));
        assertEquals(3100.0, result.get("Total Price"));
        assertEquals(5.0, result.get("Discount"));
        assertEquals(3175.0, result.get("Grand Total"));
    }

    // ========== Task ==========

    @Test
    public void testTask_ToFields() {
        Task task = new Task();
        task.id = "00TTASK";
        task.title = "Follow up";
        task.priority = "High";
        task.dueDate = 1700000000000L;
        task.description = "Call client";
        task.status = "Not Started";
        task.taskType = "Call";
        task.assignedToName = "John Doe";
        task.assignedToId = "005USR";
        task.associatedEntityId = "001ACC";
        task.associatedEntityType = "Account";
        task.associatedEntityName = "Acme Corp";

        Map<String, Object> result = task.toFields();

        assertEquals("00TTASK", result.get("Id"));
        assertEquals("Follow up", result.get("Title"));
        assertEquals("High", result.get("Priority"));
        assertEquals(1700000000000L, result.get("Due Date"));
        assertEquals("Call client", result.get("Description"));
        assertEquals("Not Started", result.get("Status"));
        assertEquals("Call", result.get("Task Type"));
        assertEquals("John Doe", result.get("Assigned To Name"));
        assertEquals("005USR", result.get("Assigned To Id"));
        assertEquals("001ACC", result.get("Associated Entity Id"));
        assertEquals("Account", result.get("Associated Entity Type"));
        assertEquals("Acme Corp", result.get("Associated Entity Name"));
    }

    @Test
    public void testTask_ToFields_NullValues() {
        Task task = new Task();
        task.id = "00TTASK";
        Map<String, Object> result = task.toFields();
        assertEquals("00TTASK", result.get("Id"));
        assertNull(result.get("Title"));
        assertNull(result.get("Priority"));
        assertNull(result.get("Due Date"));
        assertEquals(12, result.size());
    }

    // ========== User ==========

    @Test
    public void testUser_FromFields() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("Id", "005USR");
        fields.put("User name", "john.doe@acme.com");
        fields.put("Last Name", "Doe");
        fields.put("First Name", "John");
        fields.put("Name", "John Doe");
        fields.put("Company Name", "Acme Corp");
        fields.put("Alias", "jdoe");
        fields.put("Community Nickname", "johndoe");
        fields.put("Email", "john@acme.com");
        PhoneNumber mobile = new PhoneNumber(1, 5551234567L);
        fields.put("Mobile Number", mobile);

        User user = User.fromFields(fields);

        assertEquals("005USR", user.id);
        assertEquals("john.doe@acme.com", user.userName);
        assertEquals("Doe", user.lastName);
        assertEquals("John", user.firstName);
        assertEquals("John Doe", user.name);
        assertEquals("Acme Corp", user.companyName);
        assertEquals("jdoe", user.alias);
        assertEquals("johndoe", user.communityNickname);
        assertEquals("john@acme.com", user.email);
        assertSame(mobile, user.mobileNumber);
    }

    @Test
    public void testUser_ToFields() {
        User user = new User();
        user.id = "005USR";
        user.userName = "jane.smith@beta.com";
        user.lastName = "Smith";
        user.firstName = "Jane";
        user.name = "Jane Smith";
        user.companyName = "Beta Inc";
        user.alias = "jsmith";
        user.communityNickname = "janesmith";
        user.email = "jane@beta.com";

        Map<String, Object> result = user.toFields();

        assertEquals("005USR", result.get("Id"));
        assertEquals("jane.smith@beta.com", result.get("User name"));
        assertEquals("Smith", result.get("Last Name"));
        assertEquals("Jane", result.get("First Name"));
        assertEquals("Jane Smith", result.get("Name"));
        assertEquals("Beta Inc", result.get("Company Name"));
        assertEquals("jsmith", result.get("Alias"));
        assertEquals("janesmith", result.get("Community Nickname"));
        assertEquals("jane@beta.com", result.get("Email"));
    }

    @Test
    public void testUser_RoundTrip() {
        Map<String, Object> original = new HashMap<>();
        original.put("Id", "005RT");
        original.put("User name", "rt@test.com");
        original.put("Last Name", "Test");
        original.put("First Name", "Round");
        original.put("Name", "Round Test");
        original.put("Company Name", null);
        original.put("Alias", "rtest");
        original.put("Community Nickname", null);
        original.put("Email", "rt@test.com");
        original.put("Mobile Number", null);

        User user = User.fromFields(original);
        Map<String, Object> result = user.toFields();

        assertEquals(original.get("Id"), result.get("Id"));
        assertEquals(original.get("User name"), result.get("User name"));
        assertEquals(original.get("Name"), result.get("Name"));
        assertEquals(original.get("Email"), result.get("Email"));
    }
}
