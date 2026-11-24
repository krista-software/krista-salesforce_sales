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

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Centralized registry mapping entity field names to Salesforce SOQL field names and field types.
 * Each entity has its own set of field mappings used for WHERE clause and ORDER BY generation.
 */
public final class SoqlFieldMapping {

    public enum FieldType {
        /** Supports LIKE '%val%', LIKE 'val%', LIKE '%val' — for Name, Subject, Email, etc. */
        TEXT,
        /** Supports = and IN — for Status, Type, OwnerId, AccountId, etc. */
        EXACT,
        /** Supports =, >, <, >=, <= — for Amount, Probability, Quantity, UnitPrice */
        COMPARABLE,
        /** Supports =, >, <, >=, <= with date format — for CreatedDate, CloseDate, etc. */
        DATE,
        /** Supports = and IN — for Id fields (primary keys) */
        IDENTITY
    }

    private final String soqlFieldName;
    private final FieldType fieldType;

    private SoqlFieldMapping(String soqlFieldName, FieldType fieldType) {
        this.soqlFieldName = soqlFieldName;
        this.fieldType = fieldType;
    }

    public String getSoqlFieldName() {
        return soqlFieldName;
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    // ── Per-entity field mappings ──────────────────────────────────────────

    private static final Map<String, Map<String, SoqlFieldMapping>> ENTITY_FIELDS = new LinkedHashMap<>();
    private static final Map<String, Map<String, String>> ENTITY_ORDERBY = new LinkedHashMap<>();

    static {
        // Account
        Map<String, SoqlFieldMapping> account = new LinkedHashMap<>();
        account.put("name", new SoqlFieldMapping("Name", FieldType.TEXT));
        account.put("id", new SoqlFieldMapping("Id", FieldType.IDENTITY));
        account.put("industry", new SoqlFieldMapping("Industry", FieldType.EXACT));
        account.put("type", new SoqlFieldMapping("Type", FieldType.EXACT));
        account.put("owner id", new SoqlFieldMapping("OwnerId", FieldType.EXACT));
        account.put("phone", new SoqlFieldMapping("Phone", FieldType.TEXT));
        account.put("annual revenue", new SoqlFieldMapping("AnnualRevenue", FieldType.COMPARABLE));
        account.put("number of employees", new SoqlFieldMapping("NumberOfEmployees", FieldType.COMPARABLE));
        account.put("created date", new SoqlFieldMapping("CreatedDate", FieldType.DATE));
        account.put("ownership", new SoqlFieldMapping("Ownership", FieldType.EXACT));
        account.put("account number", new SoqlFieldMapping("AccountNumber", FieldType.TEXT));
        account.put("website", new SoqlFieldMapping("Website", FieldType.TEXT));
        account.put("fax", new SoqlFieldMapping("Fax", FieldType.TEXT));
        ENTITY_FIELDS.put("account", account);

        Map<String, String> accountOrder = new LinkedHashMap<>();
        accountOrder.put("name", "Name");
        accountOrder.put("created date", "CreatedDate");
        accountOrder.put("industry", "Industry");
        accountOrder.put("type", "Type");
        accountOrder.put("annual revenue", "AnnualRevenue");
        ENTITY_ORDERBY.put("account", accountOrder);

        // Lead
        Map<String, SoqlFieldMapping> lead = new LinkedHashMap<>();
        lead.put("name", new SoqlFieldMapping("Name", FieldType.TEXT));
        lead.put("id", new SoqlFieldMapping("Id", FieldType.IDENTITY));
        lead.put("status", new SoqlFieldMapping("Status", FieldType.EXACT));
        lead.put("owner id", new SoqlFieldMapping("OwnerId", FieldType.EXACT));
        lead.put("email", new SoqlFieldMapping("Email", FieldType.TEXT));
        lead.put("lead source", new SoqlFieldMapping("LeadSource", FieldType.EXACT));
        lead.put("industry", new SoqlFieldMapping("Industry", FieldType.EXACT));
        lead.put("rating", new SoqlFieldMapping("Rating", FieldType.EXACT));
        lead.put("company", new SoqlFieldMapping("Company", FieldType.TEXT));
        lead.put("title", new SoqlFieldMapping("Title", FieldType.TEXT));
        lead.put("phone", new SoqlFieldMapping("Phone", FieldType.TEXT));
        lead.put("created date", new SoqlFieldMapping("CreatedDate", FieldType.DATE));
        lead.put("last modified date", new SoqlFieldMapping("LastModifiedDate", FieldType.DATE));
        lead.put("annual revenue", new SoqlFieldMapping("AnnualRevenue", FieldType.COMPARABLE));
        ENTITY_FIELDS.put("lead", lead);

        Map<String, String> leadOrder = new LinkedHashMap<>();
        leadOrder.put("name", "Name");
        leadOrder.put("status", "Status");
        leadOrder.put("created date", "CreatedDate");
        leadOrder.put("last modified date", "LastModifiedDate");
        leadOrder.put("company", "Company");
        leadOrder.put("lead source", "LeadSource");
        ENTITY_ORDERBY.put("lead", leadOrder);

        // Opportunity
        Map<String, SoqlFieldMapping> opportunity = new LinkedHashMap<>();
        opportunity.put("name", new SoqlFieldMapping("Name", FieldType.TEXT));
        opportunity.put("id", new SoqlFieldMapping("Id", FieldType.IDENTITY));
        opportunity.put("stage name", new SoqlFieldMapping("StageName", FieldType.EXACT));
        opportunity.put("amount", new SoqlFieldMapping("Amount", FieldType.COMPARABLE));
        opportunity.put("probability", new SoqlFieldMapping("Probability", FieldType.COMPARABLE));
        opportunity.put("owner id", new SoqlFieldMapping("OwnerId", FieldType.EXACT));
        opportunity.put("owner name", new SoqlFieldMapping("Owner.Name", FieldType.TEXT));
        opportunity.put("account id", new SoqlFieldMapping("AccountId", FieldType.EXACT));
        opportunity.put("account name", new SoqlFieldMapping("Account.Name", FieldType.TEXT));
        opportunity.put("close date", new SoqlFieldMapping("CloseDate", FieldType.DATE));
        opportunity.put("created date", new SoqlFieldMapping("CreatedDate", FieldType.DATE));
        opportunity.put("last modified date", new SoqlFieldMapping("LastModifiedDate", FieldType.DATE));
        opportunity.put("type", new SoqlFieldMapping("Type", FieldType.EXACT));
        opportunity.put("lead source", new SoqlFieldMapping("LeadSource", FieldType.EXACT));
        opportunity.put("next step", new SoqlFieldMapping("NextStep", FieldType.TEXT));
        opportunity.put("expected revenue", new SoqlFieldMapping("ExpectedRevenue", FieldType.COMPARABLE));
        opportunity.put("acv", new SoqlFieldMapping("ACV__c", FieldType.COMPARABLE));
        opportunity.put("partner", new SoqlFieldMapping("Partner__c", FieldType.TEXT));
        opportunity.put("competition", new SoqlFieldMapping("Competition__c", FieldType.TEXT));
        opportunity.put("contact id", new SoqlFieldMapping("ContactId", FieldType.EXACT));
        opportunity.put("pricebook id", new SoqlFieldMapping("Pricebook2Id", FieldType.EXACT));
        ENTITY_FIELDS.put("opportunity", opportunity);

        Map<String, String> opportunityOrder = new LinkedHashMap<>();
        opportunityOrder.put("name", "Name");
        opportunityOrder.put("stage name", "StageName");
        opportunityOrder.put("amount", "Amount");
        opportunityOrder.put("close date", "CloseDate");
        opportunityOrder.put("created date", "CreatedDate");
        opportunityOrder.put("last modified date", "LastModifiedDate");
        opportunityOrder.put("probability", "Probability");
        opportunityOrder.put("type", "Type");
        ENTITY_ORDERBY.put("opportunity", opportunityOrder);

        // Order
        Map<String, SoqlFieldMapping> order = new LinkedHashMap<>();
        order.put("id", new SoqlFieldMapping("Id", FieldType.IDENTITY));
        order.put("order number", new SoqlFieldMapping("OrderNumber", FieldType.TEXT));
        order.put("account id", new SoqlFieldMapping("AccountId", FieldType.EXACT));
        order.put("status", new SoqlFieldMapping("Status", FieldType.EXACT));
        order.put("owner id", new SoqlFieldMapping("OwnerId", FieldType.EXACT));
        order.put("total amount", new SoqlFieldMapping("TotalAmount", FieldType.COMPARABLE));
        order.put("created date", new SoqlFieldMapping("CreatedDate", FieldType.DATE));
        order.put("type", new SoqlFieldMapping("Type", FieldType.EXACT));
        order.put("effective date", new SoqlFieldMapping("EffectiveDate", FieldType.DATE));
        order.put("created by id", new SoqlFieldMapping("CreatedById", FieldType.EXACT));
        ENTITY_FIELDS.put("order", order);

        Map<String, String> orderOrder = new LinkedHashMap<>();
        orderOrder.put("order number", "OrderNumber");
        orderOrder.put("status", "Status");
        orderOrder.put("total amount", "TotalAmount");
        orderOrder.put("created date", "CreatedDate");
        orderOrder.put("effective date", "EffectiveDate");
        ENTITY_ORDERBY.put("order", orderOrder);

        // OrderItem
        Map<String, SoqlFieldMapping> orderItem = new LinkedHashMap<>();
        orderItem.put("id", new SoqlFieldMapping("Id", FieldType.IDENTITY));
        orderItem.put("order id", new SoqlFieldMapping("OrderId", FieldType.EXACT));
        orderItem.put("product id", new SoqlFieldMapping("Product2Id", FieldType.EXACT));
        orderItem.put("pricebook entry id", new SoqlFieldMapping("PricebookEntryId", FieldType.EXACT));
        orderItem.put("quantity", new SoqlFieldMapping("Quantity", FieldType.COMPARABLE));
        orderItem.put("unit price", new SoqlFieldMapping("UnitPrice", FieldType.COMPARABLE));
        orderItem.put("total price", new SoqlFieldMapping("TotalPrice", FieldType.COMPARABLE));
        orderItem.put("list price", new SoqlFieldMapping("ListPrice", FieldType.COMPARABLE));
        orderItem.put("created date", new SoqlFieldMapping("CreatedDate", FieldType.DATE));
        orderItem.put("last modified date", new SoqlFieldMapping("LastModifiedDate", FieldType.DATE));
        orderItem.put("order item number", new SoqlFieldMapping("OrderItemNumber", FieldType.TEXT));
        ENTITY_FIELDS.put("orderitem", orderItem);

        Map<String, String> orderItemOrder = new LinkedHashMap<>();
        orderItemOrder.put("order item number", "OrderItemNumber");
        orderItemOrder.put("quantity", "Quantity");
        orderItemOrder.put("unit price", "UnitPrice");
        orderItemOrder.put("total price", "TotalPrice");
        orderItemOrder.put("created date", "CreatedDate");
        orderItemOrder.put("last modified date", "LastModifiedDate");
        ENTITY_ORDERBY.put("orderitem", orderItemOrder);

        // Product
        Map<String, SoqlFieldMapping> product = new LinkedHashMap<>();
        product.put("id", new SoqlFieldMapping("Id", FieldType.IDENTITY));
        product.put("name", new SoqlFieldMapping("Name", FieldType.TEXT));
        product.put("product code", new SoqlFieldMapping("ProductCode", FieldType.TEXT));
        product.put("family", new SoqlFieldMapping("Family", FieldType.EXACT));
        product.put("is active", new SoqlFieldMapping("IsActive", FieldType.EXACT));
        product.put("created date", new SoqlFieldMapping("CreatedDate", FieldType.DATE));
        product.put("last modified date", new SoqlFieldMapping("LastModifiedDate", FieldType.DATE));
        ENTITY_FIELDS.put("product", product);

        Map<String, String> productOrder = new LinkedHashMap<>();
        productOrder.put("name", "Name");
        productOrder.put("product code", "ProductCode");
        productOrder.put("family", "Family");
        productOrder.put("created date", "CreatedDate");
        productOrder.put("last modified date", "LastModifiedDate");
        ENTITY_ORDERBY.put("product", productOrder);

        // PricebookEntry
        Map<String, SoqlFieldMapping> pricebookEntry = new LinkedHashMap<>();
        pricebookEntry.put("id", new SoqlFieldMapping("Id", FieldType.IDENTITY));
        pricebookEntry.put("name", new SoqlFieldMapping("Name", FieldType.TEXT));
        pricebookEntry.put("product id", new SoqlFieldMapping("Product2Id", FieldType.EXACT));
        pricebookEntry.put("pricebook id", new SoqlFieldMapping("Pricebook2Id", FieldType.EXACT));
        pricebookEntry.put("unit price", new SoqlFieldMapping("UnitPrice", FieldType.COMPARABLE));
        pricebookEntry.put("product code", new SoqlFieldMapping("ProductCode", FieldType.TEXT));
        pricebookEntry.put("created date", new SoqlFieldMapping("CreatedDate", FieldType.DATE));
        pricebookEntry.put("last modified date", new SoqlFieldMapping("LastModifiedDate", FieldType.DATE));
        pricebookEntry.put("is active", new SoqlFieldMapping("IsActive", FieldType.EXACT));
        ENTITY_FIELDS.put("pricebookentry", pricebookEntry);

        Map<String, String> pricebookEntryOrder = new LinkedHashMap<>();
        pricebookEntryOrder.put("name", "Name");
        pricebookEntryOrder.put("unit price", "UnitPrice");
        pricebookEntryOrder.put("product code", "ProductCode");
        pricebookEntryOrder.put("created date", "CreatedDate");
        pricebookEntryOrder.put("last modified date", "LastModifiedDate");
        ENTITY_ORDERBY.put("pricebookentry", pricebookEntryOrder);

        // Task
        Map<String, SoqlFieldMapping> task = new LinkedHashMap<>();
        task.put("id", new SoqlFieldMapping("Id", FieldType.IDENTITY));
        task.put("title", new SoqlFieldMapping("Subject", FieldType.TEXT));
        task.put("priority", new SoqlFieldMapping("Priority", FieldType.EXACT));
        task.put("status", new SoqlFieldMapping("Status", FieldType.EXACT));
        task.put("assigned to name", new SoqlFieldMapping("Owner.Name", FieldType.TEXT));
        task.put("assigned to id", new SoqlFieldMapping("OwnerId", FieldType.EXACT));
        task.put("due date", new SoqlFieldMapping("ActivityDate", FieldType.DATE));
        task.put("associated entity id", new SoqlFieldMapping("WhatId", FieldType.EXACT));
        task.put("task type", new SoqlFieldMapping("TaskSubtype", FieldType.EXACT));
        task.put("description", new SoqlFieldMapping("Description", FieldType.TEXT));
        task.put("created date", new SoqlFieldMapping("CreatedDate", FieldType.DATE));
        ENTITY_FIELDS.put("task", task);

        Map<String, String> taskOrder = new LinkedHashMap<>();
        taskOrder.put("title", "Subject");
        taskOrder.put("priority", "Priority");
        taskOrder.put("status", "Status");
        taskOrder.put("due date", "ActivityDate");
        taskOrder.put("created date", "CreatedDate");
        ENTITY_ORDERBY.put("task", taskOrder);

        // Event
        Map<String, SoqlFieldMapping> event = new LinkedHashMap<>();
        event.put("id", new SoqlFieldMapping("Id", FieldType.IDENTITY));
        event.put("subject", new SoqlFieldMapping("Subject", FieldType.TEXT));
        event.put("lead id", new SoqlFieldMapping("WhoId", FieldType.EXACT));
        event.put("owner id", new SoqlFieldMapping("OwnerId", FieldType.EXACT));
        event.put("account id", new SoqlFieldMapping("AccountId", FieldType.EXACT));
        event.put("start date time", new SoqlFieldMapping("StartDateTime", FieldType.DATE));
        event.put("end date time", new SoqlFieldMapping("EndDateTime", FieldType.DATE));
        event.put("created date", new SoqlFieldMapping("CreatedDate", FieldType.DATE));
        event.put("last modified date", new SoqlFieldMapping("LastModifiedDate", FieldType.DATE));
        event.put("location", new SoqlFieldMapping("Location", FieldType.TEXT));
        ENTITY_FIELDS.put("event", event);

        Map<String, String> eventOrder = new LinkedHashMap<>();
        eventOrder.put("subject", "Subject");
        eventOrder.put("start date time", "StartDateTime");
        eventOrder.put("end date time", "EndDateTime");
        eventOrder.put("created date", "CreatedDate");
        eventOrder.put("last modified date", "LastModifiedDate");
        ENTITY_ORDERBY.put("event", eventOrder);

        // Quote
        Map<String, SoqlFieldMapping> quote = new LinkedHashMap<>();
        quote.put("id", new SoqlFieldMapping("Id", FieldType.IDENTITY));
        quote.put("name", new SoqlFieldMapping("Name", FieldType.TEXT));
        quote.put("status", new SoqlFieldMapping("Status", FieldType.EXACT));
        quote.put("opportunity id", new SoqlFieldMapping("OpportunityId", FieldType.EXACT));
        quote.put("account id", new SoqlFieldMapping("AccountId", FieldType.EXACT));
        quote.put("contact id", new SoqlFieldMapping("ContactId", FieldType.EXACT));
        quote.put("total price", new SoqlFieldMapping("TotalPrice", FieldType.COMPARABLE));
        quote.put("grand total", new SoqlFieldMapping("GrandTotal", FieldType.COMPARABLE));
        quote.put("subtotal", new SoqlFieldMapping("Subtotal", FieldType.COMPARABLE));
        quote.put("discount", new SoqlFieldMapping("Discount", FieldType.COMPARABLE));
        quote.put("created date", new SoqlFieldMapping("CreatedDate", FieldType.DATE));
        quote.put("expiration date", new SoqlFieldMapping("ExpirationDate", FieldType.DATE));
        quote.put("quote number", new SoqlFieldMapping("QuoteNumber", FieldType.TEXT));
        ENTITY_FIELDS.put("quote", quote);

        Map<String, String> quoteOrder = new LinkedHashMap<>();
        quoteOrder.put("name", "Name");
        quoteOrder.put("status", "Status");
        quoteOrder.put("total price", "TotalPrice");
        quoteOrder.put("grand total", "GrandTotal");
        quoteOrder.put("created date", "CreatedDate");
        quoteOrder.put("expiration date", "ExpirationDate");
        quoteOrder.put("quote number", "QuoteNumber");
        ENTITY_ORDERBY.put("quote", quoteOrder);

        // Asset
        Map<String, SoqlFieldMapping> asset = new LinkedHashMap<>();
        asset.put("id", new SoqlFieldMapping("Id", FieldType.IDENTITY));
        asset.put("name", new SoqlFieldMapping("Name", FieldType.TEXT));
        asset.put("account id", new SoqlFieldMapping("AccountId", FieldType.EXACT));
        asset.put("owner id", new SoqlFieldMapping("OwnerId", FieldType.EXACT));
        asset.put("contact id", new SoqlFieldMapping("ContactId", FieldType.EXACT));
        asset.put("created date", new SoqlFieldMapping("CreatedDate", FieldType.DATE));
        asset.put("last modified date", new SoqlFieldMapping("LastModifiedDate", FieldType.DATE));
        asset.put("root asset id", new SoqlFieldMapping("RootAssetId", FieldType.EXACT));
        ENTITY_FIELDS.put("asset", asset);

        Map<String, String> assetOrder = new LinkedHashMap<>();
        assetOrder.put("name", "Name");
        assetOrder.put("created date", "CreatedDate");
        assetOrder.put("last modified date", "LastModifiedDate");
        ENTITY_ORDERBY.put("asset", assetOrder);

        // User
        Map<String, SoqlFieldMapping> user = new LinkedHashMap<>();
        user.put("id", new SoqlFieldMapping("Id", FieldType.IDENTITY));
        user.put("name", new SoqlFieldMapping("Name", FieldType.TEXT));
        user.put("user name", new SoqlFieldMapping("Username", FieldType.TEXT));
        user.put("email", new SoqlFieldMapping("Email", FieldType.TEXT));
        user.put("first name", new SoqlFieldMapping("FirstName", FieldType.TEXT));
        user.put("last name", new SoqlFieldMapping("LastName", FieldType.TEXT));
        user.put("alias", new SoqlFieldMapping("Alias", FieldType.TEXT));
        user.put("mobile number", new SoqlFieldMapping("MobilePhone", FieldType.TEXT));
        ENTITY_FIELDS.put("user", user);

        Map<String, String> userOrder = new LinkedHashMap<>();
        userOrder.put("name", "Name");
        userOrder.put("user name", "Username");
        userOrder.put("email", "Email");
        userOrder.put("last name", "LastName");
        ENTITY_ORDERBY.put("user", userOrder);
    }

    /**
     * Look up the SOQL field mapping for a given entity and field name.
     *
     * @param entityName the entity name (e.g. "Account", "Opportunity")
     * @param fieldName  the entity field name (e.g. "Name", "Stage Name")
     * @return the mapping, or null if not found
     */
    public static SoqlFieldMapping forEntity(String entityName, String fieldName) {
        if (entityName == null || fieldName == null) return null;
        Map<String, SoqlFieldMapping> fields = ENTITY_FIELDS.get(entityName.toLowerCase(Locale.ROOT));
        if (fields == null) return null;
        return fields.get(fieldName.toLowerCase(Locale.ROOT));
    }

    /**
     * Look up the SOQL ORDER BY field name for a given entity and field name.
     *
     * @param entityName the entity name (e.g. "Account", "Opportunity")
     * @param fieldName  the entity field name (e.g. "Name", "Created Date")
     * @return the SOQL field name for ORDER BY, or null if not found
     */
    public static String orderByField(String entityName, String fieldName) {
        if (entityName == null || fieldName == null) return null;
        Map<String, String> fields = ENTITY_ORDERBY.get(entityName.toLowerCase(Locale.ROOT));
        if (fields == null) return null;
        return fields.get(fieldName.toLowerCase(Locale.ROOT));
    }
}
