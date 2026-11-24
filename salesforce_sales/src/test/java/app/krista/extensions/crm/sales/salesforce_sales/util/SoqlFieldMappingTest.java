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

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for SoqlFieldMapping — centralized field name and type registry
 * for all Salesforce Sales entities.
 */
public class SoqlFieldMappingTest {

    // ========== forEntity() — basic lookup ==========

    @Test
    public void testForEntity_AccountName_ReturnsTextType() {
        SoqlFieldMapping mapping = SoqlFieldMapping.forEntity("account", "name");
        assertNotNull("Account.name should be mapped", mapping);
        assertEquals("Name", mapping.getSoqlFieldName());
        assertEquals(SoqlFieldMapping.FieldType.TEXT, mapping.getFieldType());
    }

    @Test
    public void testForEntity_AccountId_ReturnsIdentityType() {
        SoqlFieldMapping mapping = SoqlFieldMapping.forEntity("account", "id");
        assertNotNull(mapping);
        assertEquals("Id", mapping.getSoqlFieldName());
        assertEquals(SoqlFieldMapping.FieldType.IDENTITY, mapping.getFieldType());
    }

    @Test
    public void testForEntity_AccountIndustry_ReturnsExactType() {
        SoqlFieldMapping mapping = SoqlFieldMapping.forEntity("account", "industry");
        assertNotNull(mapping);
        assertEquals("Industry", mapping.getSoqlFieldName());
        assertEquals(SoqlFieldMapping.FieldType.EXACT, mapping.getFieldType());
    }

    @Test
    public void testForEntity_AccountAnnualRevenue_ReturnsComparableType() {
        SoqlFieldMapping mapping = SoqlFieldMapping.forEntity("account", "annual revenue");
        assertNotNull(mapping);
        assertEquals("AnnualRevenue", mapping.getSoqlFieldName());
        assertEquals(SoqlFieldMapping.FieldType.COMPARABLE, mapping.getFieldType());
    }

    @Test
    public void testForEntity_AccountCreatedDate_ReturnsDateType() {
        SoqlFieldMapping mapping = SoqlFieldMapping.forEntity("account", "created date");
        assertNotNull(mapping);
        assertEquals("CreatedDate", mapping.getSoqlFieldName());
        assertEquals(SoqlFieldMapping.FieldType.DATE, mapping.getFieldType());
    }

    // ========== forEntity() — case insensitivity ==========

    @Test
    public void testForEntity_CaseInsensitiveEntityName() {
        SoqlFieldMapping lower = SoqlFieldMapping.forEntity("account", "name");
        SoqlFieldMapping upper = SoqlFieldMapping.forEntity("Account", "name");
        SoqlFieldMapping mixed = SoqlFieldMapping.forEntity("ACCOUNT", "name");
        assertNotNull(lower);
        assertNotNull(upper);
        assertNotNull(mixed);
        assertEquals(lower.getSoqlFieldName(), upper.getSoqlFieldName());
        assertEquals(lower.getSoqlFieldName(), mixed.getSoqlFieldName());
    }

    @Test
    public void testForEntity_CaseInsensitiveFieldName() {
        SoqlFieldMapping lower = SoqlFieldMapping.forEntity("account", "name");
        SoqlFieldMapping upper = SoqlFieldMapping.forEntity("account", "Name");
        SoqlFieldMapping mixed = SoqlFieldMapping.forEntity("account", "NAME");
        assertNotNull(lower);
        assertNotNull(upper);
        assertNotNull(mixed);
        assertEquals(lower.getSoqlFieldName(), upper.getSoqlFieldName());
        assertEquals(lower.getSoqlFieldName(), mixed.getSoqlFieldName());
    }

    // ========== forEntity() — null/unknown handling ==========

    @Test
    public void testForEntity_NullEntityName_ReturnsNull() {
        assertNull(SoqlFieldMapping.forEntity(null, "name"));
    }

    @Test
    public void testForEntity_NullFieldName_ReturnsNull() {
        assertNull(SoqlFieldMapping.forEntity("account", null));
    }

    @Test
    public void testForEntity_UnknownEntity_ReturnsNull() {
        assertNull(SoqlFieldMapping.forEntity("nonexistent", "name"));
    }

    @Test
    public void testForEntity_UnknownField_ReturnsNull() {
        assertNull(SoqlFieldMapping.forEntity("account", "nonexistent_field"));
    }

    // ========== forEntity() — all 12 entities have mappings ==========

    @Test
    public void testForEntity_AllEntitiesHaveIdMapping() {
        String[] entities = {"account", "lead", "opportunity", "order", "orderitem",
                "product", "pricebookentry", "task", "event", "quote", "asset", "user"};
        for (String entity : entities) {
            SoqlFieldMapping mapping = SoqlFieldMapping.forEntity(entity, "id");
            assertNotNull(entity + " should have Id mapping", mapping);
            assertEquals(entity + ".Id should map to 'Id'", "Id", mapping.getSoqlFieldName());
            assertEquals(entity + ".Id should be IDENTITY type",
                    SoqlFieldMapping.FieldType.IDENTITY, mapping.getFieldType());
        }
    }

    // ========== forEntity() — entity-specific field checks ==========

    @Test
    public void testForEntity_OpportunityStageName_ExactType() {
        SoqlFieldMapping mapping = SoqlFieldMapping.forEntity("opportunity", "stage name");
        assertNotNull(mapping);
        assertEquals("StageName", mapping.getSoqlFieldName());
        assertEquals(SoqlFieldMapping.FieldType.EXACT, mapping.getFieldType());
    }

    @Test
    public void testForEntity_OpportunityAmount_ComparableType() {
        SoqlFieldMapping mapping = SoqlFieldMapping.forEntity("opportunity", "amount");
        assertNotNull(mapping);
        assertEquals("Amount", mapping.getSoqlFieldName());
        assertEquals(SoqlFieldMapping.FieldType.COMPARABLE, mapping.getFieldType());
    }

    @Test
    public void testForEntity_OpportunityCloseDate_DateType() {
        SoqlFieldMapping mapping = SoqlFieldMapping.forEntity("opportunity", "close date");
        assertNotNull(mapping);
        assertEquals("CloseDate", mapping.getSoqlFieldName());
        assertEquals(SoqlFieldMapping.FieldType.DATE, mapping.getFieldType());
    }

    @Test
    public void testForEntity_LeadStatus_ExactType() {
        SoqlFieldMapping mapping = SoqlFieldMapping.forEntity("lead", "status");
        assertNotNull(mapping);
        assertEquals("Status", mapping.getSoqlFieldName());
        assertEquals(SoqlFieldMapping.FieldType.EXACT, mapping.getFieldType());
    }

    @Test
    public void testForEntity_TaskTitle_MapsToSubject() {
        SoqlFieldMapping mapping = SoqlFieldMapping.forEntity("task", "title");
        assertNotNull(mapping);
        assertEquals("Subject", mapping.getSoqlFieldName());
        assertEquals(SoqlFieldMapping.FieldType.TEXT, mapping.getFieldType());
    }

    @Test
    public void testForEntity_OrderItemQuantity_ComparableType() {
        SoqlFieldMapping mapping = SoqlFieldMapping.forEntity("orderitem", "quantity");
        assertNotNull(mapping);
        assertEquals("Quantity", mapping.getSoqlFieldName());
        assertEquals(SoqlFieldMapping.FieldType.COMPARABLE, mapping.getFieldType());
    }

    @Test
    public void testForEntity_UserEmail_TextType() {
        SoqlFieldMapping mapping = SoqlFieldMapping.forEntity("user", "email");
        assertNotNull(mapping);
        assertEquals("Email", mapping.getSoqlFieldName());
        assertEquals(SoqlFieldMapping.FieldType.TEXT, mapping.getFieldType());
    }

    @Test
    public void testForEntity_QuoteTotalPrice_ComparableType() {
        SoqlFieldMapping mapping = SoqlFieldMapping.forEntity("quote", "total price");
        assertNotNull(mapping);
        assertEquals("TotalPrice", mapping.getSoqlFieldName());
        assertEquals(SoqlFieldMapping.FieldType.COMPARABLE, mapping.getFieldType());
    }

    @Test
    public void testForEntity_EventSubject_TextType() {
        SoqlFieldMapping mapping = SoqlFieldMapping.forEntity("event", "subject");
        assertNotNull(mapping);
        assertEquals("Subject", mapping.getSoqlFieldName());
        assertEquals(SoqlFieldMapping.FieldType.TEXT, mapping.getFieldType());
    }

    @Test
    public void testForEntity_AssetName_TextType() {
        SoqlFieldMapping mapping = SoqlFieldMapping.forEntity("asset", "name");
        assertNotNull(mapping);
        assertEquals("Name", mapping.getSoqlFieldName());
        assertEquals(SoqlFieldMapping.FieldType.TEXT, mapping.getFieldType());
    }

    @Test
    public void testForEntity_ProductFamily_ExactType() {
        SoqlFieldMapping mapping = SoqlFieldMapping.forEntity("product", "family");
        assertNotNull(mapping);
        assertEquals("Family", mapping.getSoqlFieldName());
        assertEquals(SoqlFieldMapping.FieldType.EXACT, mapping.getFieldType());
    }

    @Test
    public void testForEntity_PricebookEntryUnitPrice_ComparableType() {
        SoqlFieldMapping mapping = SoqlFieldMapping.forEntity("pricebookentry", "unit price");
        assertNotNull(mapping);
        assertEquals("UnitPrice", mapping.getSoqlFieldName());
        assertEquals(SoqlFieldMapping.FieldType.COMPARABLE, mapping.getFieldType());
    }

    // ========== M5/M6: previously missing field mappings ==========

    @Test
    public void testForEntity_TaskCreatedDate_DateType() {
        SoqlFieldMapping mapping = SoqlFieldMapping.forEntity("task", "created date");
        assertNotNull("Task should have 'created date' in WHERE mapping", mapping);
        assertEquals("CreatedDate", mapping.getSoqlFieldName());
        assertEquals(SoqlFieldMapping.FieldType.DATE, mapping.getFieldType());
    }

    @Test
    public void testForEntity_PricebookEntryIsActive_ExactType() {
        SoqlFieldMapping mapping = SoqlFieldMapping.forEntity("pricebookentry", "is active");
        assertNotNull("PricebookEntry should have 'is active' in WHERE mapping", mapping);
        assertEquals("IsActive", mapping.getSoqlFieldName());
        assertEquals(SoqlFieldMapping.FieldType.EXACT, mapping.getFieldType());
    }

    // ========== orderByField() ==========

    @Test
    public void testOrderByField_AccountName_ReturnsName() {
        String field = SoqlFieldMapping.orderByField("account", "name");
        assertEquals("Name", field);
    }

    @Test
    public void testOrderByField_OpportunityAmount_ReturnsAmount() {
        String field = SoqlFieldMapping.orderByField("opportunity", "amount");
        assertEquals("Amount", field);
    }

    @Test
    public void testOrderByField_TaskDueDate_ReturnsActivityDate() {
        String field = SoqlFieldMapping.orderByField("task", "due date");
        assertEquals("ActivityDate", field);
    }

    @Test
    public void testOrderByField_CaseInsensitive() {
        String lower = SoqlFieldMapping.orderByField("account", "name");
        String upper = SoqlFieldMapping.orderByField("Account", "Name");
        assertEquals(lower, upper);
    }

    @Test
    public void testOrderByField_NullEntity_ReturnsNull() {
        assertNull(SoqlFieldMapping.orderByField(null, "name"));
    }

    @Test
    public void testOrderByField_NullField_ReturnsNull() {
        assertNull(SoqlFieldMapping.orderByField("account", null));
    }

    @Test
    public void testOrderByField_UnknownField_ReturnsNull() {
        assertNull(SoqlFieldMapping.orderByField("account", "nonexistent"));
    }

    @Test
    public void testOrderByField_UnknownEntity_ReturnsNull() {
        assertNull(SoqlFieldMapping.orderByField("nonexistent", "name"));
    }
}
