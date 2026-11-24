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

package app.krista.extensions.crm.sales.salesforce_sales.util;

import app.krista.extensions.crm.sales.salesforce_sales.SalesforceSalesAttributes;
import app.krista.model.base.FreeForm;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Comprehensive real-world test cases for SFHelper utility class.
 * Tests all public static methods without using Mockito.
 */
public class SFHelperTest {

    // ========== Tests for validateUsingOAuth() ==========

    @Test
    public void testValidateUsingOAuth_AdminContext_AlwaysReturnsTrue() {
        // Real-world scenario: Admin user always uses OAuth regardless of credentials
        SalesforceSalesAttributes attrs = createAttributesWithAllCredentials();
        boolean result = SFHelper.validateUsingOAuth(true, attrs);
        assertTrue("Admin context should always use OAuth", result);
    }

    @Test
    public void testValidateUsingOAuth_AdminContext_EvenWithoutCredentials() {
        // Real-world scenario: Admin without service account credentials
        SalesforceSalesAttributes attrs = createAttributesWithoutCredentials();
        boolean result = SFHelper.validateUsingOAuth(true, attrs);
        assertTrue("Admin context should use OAuth even without credentials", result);
    }

    @Test
    public void testValidateUsingOAuth_NonAdmin_AllCredentialsPresent() {
        // Real-world scenario: Service account with complete credentials
        SalesforceSalesAttributes attrs = createAttributesWithAllCredentials();
        boolean result = SFHelper.validateUsingOAuth(false, attrs);
        assertFalse("Should use password flow when all credentials are present", result);
    }

    @Test
    public void testValidateUsingOAuth_NonAdmin_MissingUsername() {
        // Real-world scenario: Configuration missing username
        SalesforceSalesAttributes attrs = new SalesforceSalesAttributes(
                "testClientId", "testClientSecret", "https://test.salesforce.com",
                null, "password123", "REDACTED_SECRET",
                false, "https://test.krista.ai", "America/New_York"
        );
        boolean result = SFHelper.validateUsingOAuth(false, attrs);
        assertTrue("Should use OAuth when username is missing", result);
    }

    @Test
    public void testValidateUsingOAuth_NonAdmin_EmptyUsername() {
        // Real-world scenario: Configuration with empty username
        SalesforceSalesAttributes attrs = new SalesforceSalesAttributes(
                "testClientId", "testClientSecret", "https://test.salesforce.com",
                "", "password123", "REDACTED_SECRET",
                false, "https://test.krista.ai", "America/New_York"
        );
        boolean result = SFHelper.validateUsingOAuth(false, attrs);
        assertTrue("Should use OAuth when username is empty", result);
    }

    @Test
    public void testValidateUsingOAuth_NonAdmin_BlankUsername() {
        // Real-world scenario: Configuration with whitespace-only username
        SalesforceSalesAttributes attrs = new SalesforceSalesAttributes(
                "testClientId", "testClientSecret", "https://test.salesforce.com",
                "   ", "password123", "REDACTED_SECRET",
                false, "https://test.krista.ai", "America/New_York"
        );
        boolean result = SFHelper.validateUsingOAuth(false, attrs);
        assertTrue("Should use OAuth when username is blank", result);
    }

    @Test
    public void testValidateUsingOAuth_NonAdmin_MissingPassword() {
        // Real-world scenario: Configuration missing password
        SalesforceSalesAttributes attrs = new SalesforceSalesAttributes(
                "testClientId", "testClientSecret", "https://test.salesforce.com",
                "user@example.com", null, "securityToken456",
                false, "https://test.krista.ai", "America/New_York"
        );
        boolean result = SFHelper.validateUsingOAuth(false, attrs);
        assertTrue("Should use OAuth when password is missing", result);
    }

    @Test
    public void testValidateUsingOAuth_NonAdmin_EmptyPassword() {
        // Real-world scenario: Configuration with empty password
        SalesforceSalesAttributes attrs = new SalesforceSalesAttributes(
                "testClientId", "testClientSecret", "https://test.salesforce.com",
                "user@example.com", "", "securityToken456",
                false, "https://test.krista.ai", "America/New_York"
        );
        boolean result = SFHelper.validateUsingOAuth(false, attrs);
        assertTrue("Should use OAuth when password is empty", result);
    }

    @Test
    public void testValidateUsingOAuth_NonAdmin_MissingSecurityToken() {
        // Real-world scenario: Configuration missing security token
        SalesforceSalesAttributes attrs = new SalesforceSalesAttributes(
                "testClientId", "testClientSecret", "https://test.salesforce.com",
                "user@example.com", "password123", null,
                false, "https://test.krista.ai", "America/New_York"
        );
        boolean result = SFHelper.validateUsingOAuth(false, attrs);
        assertTrue("Should use OAuth when security token is missing", result);
    }

    @Test
    public void testValidateUsingOAuth_NonAdmin_EmptySecurityToken() {
        // Real-world scenario: Configuration with empty security token
        SalesforceSalesAttributes attrs = new SalesforceSalesAttributes(
                "testClientId", "testClientSecret", "https://test.salesforce.com",
                "user@example.com", "password123", "",
                false, "https://test.krista.ai", "America/New_York"
        );
        boolean result = SFHelper.validateUsingOAuth(false, attrs);
        assertTrue("Should use OAuth when security token is empty", result);
    }

    @Test
    public void testValidateUsingOAuth_NonAdmin_AllCredentialsMissing() {
        // Real-world scenario: Fresh configuration with no credentials
        SalesforceSalesAttributes attrs = createAttributesWithoutCredentials();
        boolean result = SFHelper.validateUsingOAuth(false, attrs);
        assertTrue("Should use OAuth when all credentials are missing", result);
    }

    // ========== Tests for upsertAccountDetails() ==========

    @Test
    public void testUpsertAccountDetails_WithCompleteData() {
        // Real-world scenario: Processing complete account data from Salesforce
        Map<String, Object> accountData = new HashMap<>();
        accountData.put("Name", "Acme Corporation");
        accountData.put("Fax", "555-0123");
        accountData.put("AccountNumber", "ACC-12345");
        accountData.put("Website", "https://www.acme.com");
        accountData.put("Phone", "555-0100");
        accountData.put("Industry", "Technology");
        accountData.put("NumberOfEmployees", 500.0);
        accountData.put("AnnualRevenue", 5000000.0);
        accountData.put("Type", "Customer - Direct");
        accountData.put("OwnerId", "00558000000yFyGAAU");
        accountData.put("CreatedDate", "1710511845123");

        Map<String, Object> accountMap = new HashMap<>();
        SFHelper.upsertAccountDetails(accountData, accountMap);

        assertEquals("Acme Corporation", accountMap.get("Account Name"));
        assertEquals("555-0123", accountMap.get("Account Fax"));
        assertEquals("ACC-12345", accountMap.get("Account Number"));
        assertEquals("https://www.acme.com", accountMap.get("Account Website"));
        assertEquals("555-0100", accountMap.get("Account Phone"));
        assertEquals("Technology", accountMap.get("Account Industry"));
        assertEquals(500.0, accountMap.get("Account Number Of Employees"));
        assertEquals(5000000.0, accountMap.get("Account Annual Revenue"));
        assertEquals("Customer - Direct", accountMap.get("Account Type"));
        assertEquals("00558000000yFyGAAU", accountMap.get("Account Owner Id"));
        assertEquals(1710511845123L, accountMap.get("Account Created Date"));
    }

    @Test
    public void testUpsertAccountDetails_WithTextNameField() {
        // Real-world scenario: Account data using TextName instead of Name
        Map<String, Object> accountData = new HashMap<>();
        accountData.put("TextName", "Beta Industries");
        accountData.put("Phone", "555-0200");

        Map<String, Object> accountMap = new HashMap<>();
        SFHelper.upsertAccountDetails(accountData, accountMap);

        assertEquals("Beta Industries", accountMap.get("Account Name"));
        assertEquals("555-0200", accountMap.get("Account Phone"));
    }


    @Test
    public void testUpsertAccountDetails_WithMissingOptionalFields() {
        // Real-world scenario: Account with only required fields
        Map<String, Object> accountData = new HashMap<>();
        accountData.put("Name", "Minimal Account");

        Map<String, Object> accountMap = new HashMap<>();
        SFHelper.upsertAccountDetails(accountData, accountMap);

        assertEquals("Minimal Account", accountMap.get("Account Name"));
        assertEquals("", accountMap.get("Account Fax"));
        assertEquals("", accountMap.get("Account Phone"));
        assertEquals(0.0, accountMap.get("Account Number Of Employees"));
        assertEquals(0.0, accountMap.get("Account Annual Revenue"));
    }

    @Test
    public void testUpsertAccountDetails_WithNullValues() {
        // Real-world scenario: Account data with explicit null values
        Map<String, Object> accountData = new HashMap<>();
        accountData.put("Name", "Test Account");
        accountData.put("Fax", null);
        accountData.put("Phone", null);
        accountData.put("NumberOfEmployees", null);
        accountData.put("AnnualRevenue", null);
        accountData.put("CreatedDate", null);

        Map<String, Object> accountMap = new HashMap<>();
        SFHelper.upsertAccountDetails(accountData, accountMap);

        assertEquals("Test Account", accountMap.get("Account Name"));
        assertEquals("", accountMap.get("Account Fax"));
        assertEquals("", accountMap.get("Account Phone"));
        assertEquals(0.0, accountMap.get("Account Number Of Employees"));
        assertEquals(0.0, accountMap.get("Account Annual Revenue"));
        assertNull(accountMap.get("Account Created Date"));
    }

    @Test
    public void testUpsertAccountDetails_WithBillingAddress() {
        // Real-world scenario: Account with complete billing address
        Map<String, Object> accountData = new HashMap<>();
        accountData.put("Name", "Global Corp");
        accountData.put("BillingStreet", "123 Main Street");
        accountData.put("BillingCity", "San Francisco");
        accountData.put("BillingState", "CA");
        accountData.put("BillingPostalCode", "94105");
        accountData.put("BillingCountry", "USA");

        Map<String, Object> accountMap = new HashMap<>();
        SFHelper.upsertAccountDetails(accountData, accountMap);

        assertEquals("Global Corp", accountMap.get("Account Name"));
        assertEquals("123 Main Street", accountMap.get("Account Billing Street"));
        assertEquals("San Francisco", accountMap.get("Account Billing City"));
        assertEquals("CA", accountMap.get("Account Billing State"));
        assertEquals("94105", accountMap.get("Account Billing Postal Code"));
        assertEquals("USA", accountMap.get("Account Billing Country"));
    }

    @Test
    public void testUpsertAccountDetails_WithShippingAddress() {
        // Real-world scenario: Account with shipping address different from billing
        Map<String, Object> accountData = new HashMap<>();
        accountData.put("Name", "Shipping Test Corp");
        accountData.put("ShippingStreet", "456 Oak Avenue");
        accountData.put("ShippingCity", "New York");
        accountData.put("ShippingState", "NY");
        accountData.put("ShippingPostalCode", "10001");
        accountData.put("ShippingCountry", "USA");

        Map<String, Object> accountMap = new HashMap<>();
        SFHelper.upsertAccountDetails(accountData, accountMap);

        assertEquals("Shipping Test Corp", accountMap.get("Account Name"));
        assertEquals("456 Oak Avenue", accountMap.get("Account Shipping Street"));
        assertEquals("New York", accountMap.get("Account Shipping City"));
        assertEquals("NY", accountMap.get("Account Shipping State"));
        assertEquals("10001", accountMap.get("Account Shipping Postal Code"));
        assertEquals("USA", accountMap.get("Account Shipping Country"));
    }

    // ========== Tests for upsertLeadDetails() ==========

    @Test
    public void testUpsertLeadDetails_WithCompleteData() {
        // Real-world scenario: Processing complete lead data from Salesforce
        Map<String, Object> leadData = new HashMap<>();
        leadData.put("Name", "John Doe");
        leadData.put("Title", "VP of Sales");
        leadData.put("Company", "Tech Innovations Inc");
        leadData.put("Website", "https://www.techinnovations.com");
        leadData.put("Phone", "555-0300");
        leadData.put("Email", "john.doe@techinnovations.com");
        leadData.put("Status", "Open - Not Contacted");
        leadData.put("LeadSource", "Web");
        leadData.put("Industry", "Technology");
        leadData.put("Rating", "Hot");
        leadData.put("AnnualRevenue", 1000000.0);
        leadData.put("OwnerId", "00558000000yFyGAAU");
        leadData.put("IsConverted", "false");
        leadData.put("CreatedDate", "1710511845123");

        Map<String, Object> leadMap = new HashMap<>();
        SFHelper.upsertLeadDetails(leadData, leadMap);

        assertEquals("John Doe", leadMap.get("Lead Name"));
        assertEquals("VP of Sales", leadMap.get("Lead Title"));
        assertEquals("Tech Innovations Inc", leadMap.get("Lead Company"));
        assertEquals("https://www.techinnovations.com", leadMap.get("Lead Website"));
        assertEquals("555-0300", leadMap.get("Lead Phone"));
        assertEquals("john.doe@techinnovations.com", leadMap.get("Lead Email"));
        assertEquals("Open - Not Contacted", leadMap.get("Lead Status"));
        assertEquals("Web", leadMap.get("Lead LeadSource"));
        assertEquals("Technology", leadMap.get("Lead Industry"));
        assertEquals("Hot", leadMap.get("Lead Rating"));
        assertEquals(1000000.0, leadMap.get("Lead Annual Revenue"));
        assertEquals("00558000000yFyGAAU", leadMap.get("Lead Owner Id"));
        assertEquals("false", leadMap.get("Lead Is Converted"));
        assertEquals(1710511845123L, leadMap.get("Lead Created Date"));
    }

    @Test
    public void testUpsertLeadDetails_WithConvertedLead() {
        // Real-world scenario: Lead that has been converted to opportunity
        Map<String, Object> leadData = new HashMap<>();
        leadData.put("Name", "Jane Smith");
        leadData.put("Company", "Enterprise Solutions");
        leadData.put("IsConverted", "true");
        leadData.put("ConvertedAccountId", "001xx000003DGb2AAG");
        leadData.put("ConvertedContactId", "003xx000004TmiQAAS");
        leadData.put("ConvertedOpportunityId", "006xx000001T2bQAAS");
        leadData.put("ConvertedDate", "2024-03-15");

        Map<String, Object> leadMap = new HashMap<>();
        SFHelper.upsertLeadDetails(leadData, leadMap);

        assertEquals("Jane Smith", leadMap.get("Lead Name"));
        assertEquals("Enterprise Solutions", leadMap.get("Lead Company"));
        assertEquals("true", leadMap.get("Lead Is Converted"));
        assertEquals("001xx000003DGb2AAG", leadMap.get("Lead Converted Account Id"));
        assertEquals("003xx000004TmiQAAS", leadMap.get("Lead Converted Contact Id"));
        assertEquals("006xx000001T2bQAAS", leadMap.get("Lead Converted Opportunity Id"));
        assertNotNull(leadMap.get("Lead Converted Date"));
    }

    @Test
    public void testUpsertLeadDetails_WithMissingOptionalFields() {
        // Real-world scenario: Lead with minimal information
        Map<String, Object> leadData = new HashMap<>();
        leadData.put("Name", "Minimal Lead");
        leadData.put("Company", "Unknown Company");

        Map<String, Object> leadMap = new HashMap<>();
        SFHelper.upsertLeadDetails(leadData, leadMap);

        assertEquals("Minimal Lead", leadMap.get("Lead Name"));
        assertEquals("Unknown Company", leadMap.get("Lead Company"));
        assertEquals("", leadMap.get("Lead Title"));
        assertEquals("", leadMap.get("Lead Phone"));
        assertEquals("", leadMap.get("Lead Email"));
        assertEquals(0.0, leadMap.get("Lead Annual Revenue"));
    }

    // ========== Tests for upsertTaskDetails() ==========

    @Test
    public void testUpsertTaskDetails_WithCompleteData() {
        // Real-world scenario: Processing complete task data
        FreeForm taskData = new FreeForm();
        taskData.put("Title", "Text", "Follow up with customer");
        taskData.put("Priority", "Text", "High");
        taskData.put("Due Date", "Text", "2024-03-20");
        taskData.put("Description", "Text", "Call customer to discuss proposal");
        taskData.put("Status", "Text", "Not Started");
        taskData.put("Assigned To Id", "Text", "00558000000yFyGAAU");
        taskData.put("Associated Entity Id", "Text", "006xx000001T2bQAAS");

        Map<String, Object> taskMap = new HashMap<>();
        SFHelper.upsertTaskDetails(taskData, taskMap);

        assertEquals("Follow up with customer", taskMap.get("Task Title"));
        assertEquals("High", taskMap.get("Task Priority"));
        assertEquals("Call customer to discuss proposal", taskMap.get("Task Description"));
        assertEquals("Not Started", taskMap.get("Task Status"));
        assertEquals("00558000000yFyGAAU", taskMap.get("Task Assigned To Id"));
        assertEquals("006xx000001T2bQAAS", taskMap.get("Task Associated Entity Id"));
        assertNotNull(taskMap.get("Task Due Date"));
    }

    @Test
    public void testUpsertTaskDetails_WithNullTaskMap() {
        // Real-world scenario: Creating new task map from scratch
        FreeForm taskData = new FreeForm();
        taskData.put("Title", "Text", "New Task");
        taskData.put("Priority", "Text", "Normal");

        Map<String, Object> taskMap = null;
        SFHelper.upsertTaskDetails(taskData, taskMap);

        // Method should handle null taskMap gracefully
        // Note: The method creates a new HashMap internally if taskMap is null
    }

    @Test
    public void testUpsertTaskDetails_WithMissingOptionalFields() {
        // Real-world scenario: Task with only required fields
        FreeForm taskData = new FreeForm();
        taskData.put("Title", "Text", "Simple Task");

        Map<String, Object> taskMap = new HashMap<>();
        SFHelper.upsertTaskDetails(taskData, taskMap);

        assertEquals("Simple Task", taskMap.get("Task Title"));
        assertEquals("", taskMap.get("Task Priority"));
        assertEquals("", taskMap.get("Task Description"));
        assertEquals("", taskMap.get("Task Status"));
    }

    @Test
    public void testUpsertTaskDetails_WithInvalidDueDate() {
        // Real-world scenario: Task with invalid date format
        FreeForm taskData = new FreeForm();
        taskData.put("Title", "Text", "Task with bad date");
        taskData.put("Due Date", "Text", "invalid-date-format");

        Map<String, Object> taskMap = new HashMap<>();
        SFHelper.upsertTaskDetails(taskData, taskMap);

        assertEquals("Task with bad date", taskMap.get("Task Title"));
        assertNull(taskMap.get("Task Due Date"));
    }

    // ========== Helper Methods and Test Classes ==========

    /**
     * Helper method to create a test SalesforceSalesAttributes instance with all credentials.
     * Uses the real SalesforceSalesAttributes constructor - no mocking!
     */
    private SalesforceSalesAttributes createAttributesWithAllCredentials() {
        return new SalesforceSalesAttributes(
                "testClientId",           // clientId
                "testClientSecret",       // clientSecret
                "https://test.salesforce.com", // instanceUrl
                "user@example.com",       // userName
                "password123",            // password
                "securityToken456",       // securityToken
                false,                    // isSandbox
                "https://test.krista.ai", // routingUrl
                "America/New_York"        // timeZone
        );
    }

    /**
     * Helper method to create a test SalesforceSalesAttributes instance with no credentials.
     * Uses the real SalesforceSalesAttributes constructor - no mocking!
     */
    private SalesforceSalesAttributes createAttributesWithoutCredentials() {
        return new SalesforceSalesAttributes(
                null,  // clientId
                null,  // clientSecret
                null,  // instanceUrl
                null,  // userName
                null,  // password
                null,  // securityToken
                false, // isSandbox
                "https://test.krista.ai", // routingUrl
                null   // timeZone
        );
    }
}

