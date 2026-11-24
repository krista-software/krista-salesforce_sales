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

import app.krista.extension.executor.QueryClause;
import app.krista.extension.executor.SearchCondition;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for SoqlQueryHelper — builds SOQL WHERE, ORDER BY, and pagination
 * clauses from SearchCondition and QueryClause objects.
 */
public class SoqlQueryHelperTest {

    // ========== buildWhereClause() — TEXT fields ==========

    @Test
    public void testBuildWhereClause_TextContains() {
        List<SearchCondition> conditions = List.of(
                new SearchCondition(SearchCondition.Operator.contains, "Acme", "Name")
        );
        String where = SoqlQueryHelper.buildWhereClause("account", conditions);
        assertTrue("Should produce LIKE clause", where.contains("Name LIKE '%Acme%'"));
    }

    @Test
    public void testBuildWhereClause_TextStartsWith() {
        List<SearchCondition> conditions = List.of(
                new SearchCondition(SearchCondition.Operator.startswith, "Acme", "Name")
        );
        String where = SoqlQueryHelper.buildWhereClause("account", conditions);
        assertTrue(where.contains("Name LIKE 'Acme%'"));
    }

    @Test
    public void testBuildWhereClause_TextEndsWith() {
        List<SearchCondition> conditions = List.of(
                new SearchCondition(SearchCondition.Operator.endswith, "Corp", "Name")
        );
        String where = SoqlQueryHelper.buildWhereClause("account", conditions);
        assertTrue(where.contains("Name LIKE '%Corp'"));
    }

    @Test
    public void testBuildWhereClause_TextEquals() {
        List<SearchCondition> conditions = List.of(
                new SearchCondition(SearchCondition.Operator.equals, "Acme Inc", "Name")
        );
        String where = SoqlQueryHelper.buildWhereClause("account", conditions);
        assertTrue(where.contains("Name = 'Acme Inc'"));
    }

    @Test
    public void testBuildWhereClause_TextNotEquals() {
        List<SearchCondition> conditions = List.of(
                new SearchCondition(SearchCondition.Operator.notequals, "Acme Inc", "Name")
        );
        String where = SoqlQueryHelper.buildWhereClause("account", conditions);
        assertTrue(where.contains("Name != 'Acme Inc'"));
    }

    // ========== buildWhereClause() — EXACT fields ==========

    @Test
    public void testBuildWhereClause_ExactEquals_SingleValue() {
        List<SearchCondition> conditions = List.of(
                new SearchCondition(SearchCondition.Operator.equals, "Closed Won", "Stage Name")
        );
        String where = SoqlQueryHelper.buildWhereClause("opportunity", conditions);
        assertTrue("Should produce = clause", where.contains("StageName = 'Closed Won'"));
    }

    @Test
    public void testBuildWhereClause_ExactEquals_MultipleValues_ProducesInClause() {
        List<SearchCondition> conditions = List.of(
                new SearchCondition(SearchCondition.Operator.equals, "Closed Won", "Stage Name"),
                new SearchCondition(SearchCondition.Operator.equals, "Closed Lost", "Stage Name")
        );
        String where = SoqlQueryHelper.buildWhereClause("opportunity", conditions);
        assertTrue("Should produce IN clause", where.contains("StageName IN ("));
        assertTrue(where.contains("'Closed Won'"));
        assertTrue(where.contains("'Closed Lost'"));
    }

    @Test
    public void testBuildWhereClause_ExactNotEquals_SingleValue() {
        List<SearchCondition> conditions = List.of(
                new SearchCondition(SearchCondition.Operator.notequals, "Closed Lost", "Stage Name")
        );
        String where = SoqlQueryHelper.buildWhereClause("opportunity", conditions);
        assertTrue(where.contains("StageName != 'Closed Lost'"));
    }

    @Test
    public void testBuildWhereClause_ExactNotEquals_MultipleValues_ProducesNotInClause() {
        List<SearchCondition> conditions = List.of(
                new SearchCondition(SearchCondition.Operator.notequals, "Closed Won", "Stage Name"),
                new SearchCondition(SearchCondition.Operator.notequals, "Closed Lost", "Stage Name")
        );
        String where = SoqlQueryHelper.buildWhereClause("opportunity", conditions);
        assertTrue("Should produce NOT IN clause", where.contains("StageName NOT IN ("));
        assertTrue(where.contains("'Closed Won'"));
        assertTrue(where.contains("'Closed Lost'"));
    }

    // ========== buildWhereClause() — COMPARABLE fields ==========

    @Test
    public void testBuildWhereClause_ComparableEquals() {
        List<SearchCondition> conditions = List.of(
                new SearchCondition(SearchCondition.Operator.equals, "50000", "Amount")
        );
        String where = SoqlQueryHelper.buildWhereClause("opportunity", conditions);
        assertTrue(where.contains("Amount = 50000"));
        assertFalse("Comparable values should NOT be quoted", where.contains("'50000'"));
    }

    @Test
    public void testBuildWhereClause_ComparableGt() {
        List<SearchCondition> conditions = List.of(
                new SearchCondition(SearchCondition.Operator.gt, "100000", "Amount")
        );
        String where = SoqlQueryHelper.buildWhereClause("opportunity", conditions);
        assertTrue(where.contains("Amount > 100000"));
    }

    @Test
    public void testBuildWhereClause_ComparableLt() {
        List<SearchCondition> conditions = List.of(
                new SearchCondition(SearchCondition.Operator.lt, "5000", "Amount")
        );
        String where = SoqlQueryHelper.buildWhereClause("opportunity", conditions);
        assertTrue(where.contains("Amount < 5000"));
    }

    @Test
    public void testBuildWhereClause_ComparableGte() {
        List<SearchCondition> conditions = List.of(
                new SearchCondition(SearchCondition.Operator.gte, "10", "Quantity")
        );
        String where = SoqlQueryHelper.buildWhereClause("orderitem", conditions);
        assertTrue(where.contains("Quantity >= 10"));
    }

    @Test
    public void testBuildWhereClause_ComparableLte() {
        List<SearchCondition> conditions = List.of(
                new SearchCondition(SearchCondition.Operator.lte, "99.99", "Unit Price")
        );
        String where = SoqlQueryHelper.buildWhereClause("orderitem", conditions);
        assertTrue(where.contains("UnitPrice <= 99.99"));
    }

    // ========== buildWhereClause() — DATE fields ==========

    @Test
    public void testBuildWhereClause_DateEquals() {
        List<SearchCondition> conditions = List.of(
                new SearchCondition(SearchCondition.Operator.equals, "2025-01-01T00:00:00Z", "Created Date")
        );
        String where = SoqlQueryHelper.buildWhereClause("account", conditions);
        assertTrue(where.contains("CreatedDate = 2025-01-01T00:00:00Z"));
        assertFalse("Date values should NOT be quoted", where.contains("'2025-01-01"));
    }

    @Test
    public void testBuildWhereClause_DateGt() {
        List<SearchCondition> conditions = List.of(
                new SearchCondition(SearchCondition.Operator.gt, "2025-06-01T00:00:00Z", "Close Date")
        );
        String where = SoqlQueryHelper.buildWhereClause("opportunity", conditions);
        assertTrue(where.contains("CloseDate > 2025-06-01T00:00:00Z"));
    }

    // ========== buildWhereClause() — IDENTITY fields ==========

    @Test
    public void testBuildWhereClause_IdentityEquals_SingleValue() {
        List<SearchCondition> conditions = List.of(
                new SearchCondition(SearchCondition.Operator.equals, "001ABC123", "Id")
        );
        String where = SoqlQueryHelper.buildWhereClause("account", conditions);
        assertTrue(where.contains("Id = '001ABC123'"));
    }

    @Test
    public void testBuildWhereClause_IdentityEquals_MultipleValues_ProducesInClause() {
        List<SearchCondition> conditions = List.of(
                new SearchCondition(SearchCondition.Operator.equals, "001ABC", "Id"),
                new SearchCondition(SearchCondition.Operator.equals, "001DEF", "Id")
        );
        String where = SoqlQueryHelper.buildWhereClause("account", conditions);
        assertTrue(where.contains("Id IN ("));
        assertTrue(where.contains("'001ABC'"));
        assertTrue(where.contains("'001DEF'"));
    }

    // ========== buildWhereClause() — multiple conditions joined with AND ==========

    @Test
    public void testBuildWhereClause_MultipleConditions_JoinedWithAnd() {
        List<SearchCondition> conditions = List.of(
                new SearchCondition(SearchCondition.Operator.contains, "Acme", "Name"),
                new SearchCondition(SearchCondition.Operator.equals, "Technology", "Industry")
        );
        String where = SoqlQueryHelper.buildWhereClause("account", conditions);
        assertTrue(where.contains(" AND "));
        assertTrue(where.contains("Name LIKE '%Acme%'"));
        assertTrue(where.contains("Industry = 'Technology'"));
    }

    // ========== buildWhereClause() — edge cases ==========

    @Test
    public void testBuildWhereClause_NullConditions_ReturnsEmpty() {
        String where = SoqlQueryHelper.buildWhereClause("account", null);
        assertEquals("", where);
    }

    @Test
    public void testBuildWhereClause_EmptyConditions_ReturnsEmpty() {
        String where = SoqlQueryHelper.buildWhereClause("account", Collections.emptyList());
        assertEquals("", where);
    }

    @Test
    public void testBuildWhereClause_NullOperand_Skipped() {
        List<SearchCondition> conditions = List.of(
                new SearchCondition(SearchCondition.Operator.equals, null, "Name")
        );
        String where = SoqlQueryHelper.buildWhereClause("account", conditions);
        assertEquals("", where);
    }

    @Test
    public void testBuildWhereClause_EmptyOperand_Skipped() {
        List<SearchCondition> conditions = List.of(
                new SearchCondition(SearchCondition.Operator.equals, "  ", "Name")
        );
        String where = SoqlQueryHelper.buildWhereClause("account", conditions);
        assertEquals("", where);
    }

    @Test
    public void testBuildWhereClause_UnmappedField_Skipped() {
        List<SearchCondition> conditions = List.of(
                new SearchCondition(SearchCondition.Operator.equals, "value", "NonExistentField")
        );
        String where = SoqlQueryHelper.buildWhereClause("account", conditions);
        assertEquals("", where);
    }

    @Test
    public void testBuildWhereClause_OrderbyOperator_Skipped() {
        List<SearchCondition> conditions = List.of(
                new SearchCondition(SearchCondition.Operator.orderby, "ASC", "Name")
        );
        String where = SoqlQueryHelper.buildWhereClause("account", conditions);
        assertEquals("Orderby conditions should be skipped", "", where);
    }

    @Test
    public void testBuildWhereClause_SoqlEscaping_SingleQuotes() {
        List<SearchCondition> conditions = List.of(
                new SearchCondition(SearchCondition.Operator.contains, "O'Brien", "Name")
        );
        String where = SoqlQueryHelper.buildWhereClause("account", conditions);
        assertFalse("Single quotes in value should be escaped",
                where.contains("O'Brien%'"));
        assertTrue("Should contain escaped value", where.contains("LIKE '%"));
    }

    // ========== buildOrderByClause() ==========

    @Test
    public void testBuildOrderByClause_WithOrderByClause() {
        List<QueryClause> clauses = List.of(
                new QueryClause(QueryClause.Operator.orderby, "DESC", "Name")
        );
        String orderBy = SoqlQueryHelper.buildOrderByClause("account", clauses);
        assertTrue(orderBy.contains("ORDER BY Name DESC"));
    }

    @Test
    public void testBuildOrderByClause_AscDirection() {
        List<QueryClause> clauses = List.of(
                new QueryClause(QueryClause.Operator.orderby, "ASC", "Amount")
        );
        String orderBy = SoqlQueryHelper.buildOrderByClause("opportunity", clauses);
        assertTrue(orderBy.contains("ORDER BY Amount ASC"));
    }

    @Test
    public void testBuildOrderByClause_NullClauses_ReturnsDefault() {
        String orderBy = SoqlQueryHelper.buildOrderByClause("opportunity", null);
        assertTrue("Should have default ORDER BY", orderBy.contains("ORDER BY"));
        assertTrue(orderBy.contains("LastModifiedDate DESC"));
    }

    @Test
    public void testBuildOrderByClause_EmptyClauses_ReturnsDefault() {
        String orderBy = SoqlQueryHelper.buildOrderByClause("opportunity", Collections.emptyList());
        assertTrue(orderBy.contains("ORDER BY"));
        assertTrue(orderBy.contains("LastModifiedDate DESC"));
    }

    @Test
    public void testBuildOrderByClause_AccountDefault_UsesCreatedDate() {
        // Account is in ENTITIES_WITHOUT_LAST_MODIFIED set
        String orderBy = SoqlQueryHelper.buildOrderByClause("account", Collections.emptyList());
        assertTrue(orderBy.contains("CreatedDate DESC"));
    }

    @Test
    public void testBuildOrderByClause_OrderDefault_UsesCreatedDate() {
        // Order is in ENTITIES_WITHOUT_LAST_MODIFIED set
        String orderBy = SoqlQueryHelper.buildOrderByClause("order", Collections.emptyList());
        assertTrue(orderBy.contains("CreatedDate DESC"));
    }

    @Test
    public void testBuildOrderByClause_UnmappedOrderField_ReturnsDefault() {
        List<QueryClause> clauses = List.of(
                new QueryClause(QueryClause.Operator.orderby, "DESC", "NonExistentField")
        );
        String orderBy = SoqlQueryHelper.buildOrderByClause("account", clauses);
        // Should fall back to default since field is not in ORDER BY mapping
        assertTrue(orderBy.contains("ORDER BY"));
    }

    @Test
    public void testBuildOrderByClause_NullDirection_DefaultsToDesc() {
        List<QueryClause> clauses = List.of(
                new QueryClause(QueryClause.Operator.orderby, null, "Name")
        );
        String orderBy = SoqlQueryHelper.buildOrderByClause("account", clauses);
        assertTrue(orderBy.contains("Name DESC"));
    }

    // ========== buildPaginationClause() ==========

    @Test
    public void testBuildPaginationClause_FirstPage() {
        String pagination = SoqlQueryHelper.buildPaginationClause(20, 1);
        assertTrue(pagination.contains("LIMIT 20"));
        assertTrue(pagination.contains("OFFSET 0"));
    }

    @Test
    public void testBuildPaginationClause_SecondPage() {
        String pagination = SoqlQueryHelper.buildPaginationClause(20, 2);
        assertTrue(pagination.contains("LIMIT 20"));
        assertTrue(pagination.contains("OFFSET 20"));
    }

    @Test
    public void testBuildPaginationClause_ThirdPage() {
        String pagination = SoqlQueryHelper.buildPaginationClause(10, 3);
        assertTrue(pagination.contains("LIMIT 10"));
        assertTrue(pagination.contains("OFFSET 20"));
    }

    @Test
    public void testBuildPaginationClause_ZeroPageIndex_OffsetClampsToZero() {
        // pageIndex=0 → offset=(0-1)*pageSize = -pageSize → clamped to 0
        String pagination = SoqlQueryHelper.buildPaginationClause(20, 0);
        assertTrue(pagination.contains("LIMIT 20"));
        assertTrue(pagination.contains("OFFSET 0"));
    }

    // ========== End-to-end: realistic search scenarios ==========

    @Test
    public void testEndToEnd_AccountSearchByNameAndIndustry() {
        List<SearchCondition> conditions = List.of(
                new SearchCondition(SearchCondition.Operator.contains, "Acme", "Name"),
                new SearchCondition(SearchCondition.Operator.equals, "Technology", "Industry")
        );
        List<QueryClause> clauses = List.of(
                new QueryClause(QueryClause.Operator.orderby, "ASC", "Name")
        );

        String where = SoqlQueryHelper.buildWhereClause("account", conditions);
        String orderBy = SoqlQueryHelper.buildOrderByClause("account", clauses);
        String pagination = SoqlQueryHelper.buildPaginationClause(20, 1);

        String fullQuery = "SELECT Id, Name FROM Account WHERE " + where + orderBy + pagination;

        assertTrue(fullQuery.contains("WHERE"));
        assertTrue(fullQuery.contains("Name LIKE '%Acme%'"));
        assertTrue(fullQuery.contains("Industry = 'Technology'"));
        assertTrue(fullQuery.contains("ORDER BY Name ASC"));
        assertTrue(fullQuery.contains("LIMIT 20 OFFSET 0"));
    }

    @Test
    public void testEndToEnd_OpportunitySearchByAmountAndStage() {
        List<SearchCondition> conditions = List.of(
                new SearchCondition(SearchCondition.Operator.gt, "50000", "Amount"),
                new SearchCondition(SearchCondition.Operator.equals, "Closed Won", "Stage Name")
        );

        String where = SoqlQueryHelper.buildWhereClause("opportunity", conditions);

        assertTrue(where.contains("Amount > 50000"));
        assertTrue(where.contains("StageName = 'Closed Won'"));
        assertTrue(where.contains(" AND "));
    }

    @Test
    public void testEndToEnd_LeadSearchByStatus() {
        List<SearchCondition> conditions = List.of(
                new SearchCondition(SearchCondition.Operator.equals, "Open", "Status")
        );

        String where = SoqlQueryHelper.buildWhereClause("lead", conditions);
        assertTrue(where.contains("Status = 'Open'"));
    }

    @Test
    public void testEndToEnd_TaskSearchByTitle() {
        List<SearchCondition> conditions = List.of(
                new SearchCondition(SearchCondition.Operator.contains, "Follow up", "Title")
        );

        String where = SoqlQueryHelper.buildWhereClause("task", conditions);
        assertTrue("Task title should map to Subject", where.contains("Subject LIKE '%Follow up%'"));
    }

    // ========== COMPARABLE validation (SOQL injection prevention) ==========

    @Test
    public void testBuildWhereClause_ComparableInjection_Rejected() {
        List<SearchCondition> conditions = List.of(
                new SearchCondition(SearchCondition.Operator.equals, "0 OR 1=1", "Amount")
        );
        String where = SoqlQueryHelper.buildWhereClause("opportunity", conditions);
        assertEquals("Injection attempt should be rejected", "", where);
    }

    @Test
    public void testBuildWhereClause_ComparableValidInteger() {
        List<SearchCondition> conditions = List.of(
                new SearchCondition(SearchCondition.Operator.gt, "50000", "Amount")
        );
        String where = SoqlQueryHelper.buildWhereClause("opportunity", conditions);
        assertTrue(where.contains("Amount > 50000"));
    }

    @Test
    public void testBuildWhereClause_ComparableValidDecimal() {
        List<SearchCondition> conditions = List.of(
                new SearchCondition(SearchCondition.Operator.equals, "99.99", "Unit Price")
        );
        String where = SoqlQueryHelper.buildWhereClause("orderitem", conditions);
        assertTrue(where.contains("UnitPrice = 99.99"));
    }

    @Test
    public void testBuildWhereClause_ComparableNegativeNumber() {
        List<SearchCondition> conditions = List.of(
                new SearchCondition(SearchCondition.Operator.lt, "-500", "Amount")
        );
        String where = SoqlQueryHelper.buildWhereClause("opportunity", conditions);
        assertTrue(where.contains("Amount < -500"));
    }

    @Test
    public void testBuildWhereClause_ComparableInvalidLetters_Rejected() {
        List<SearchCondition> conditions = List.of(
                new SearchCondition(SearchCondition.Operator.equals, "abc", "Amount")
        );
        String where = SoqlQueryHelper.buildWhereClause("opportunity", conditions);
        assertEquals("", where);
    }

    // ========== DATE validation and epoch conversion ==========

    @Test
    public void testBuildWhereClause_DateInjection_Rejected() {
        List<SearchCondition> conditions = List.of(
                new SearchCondition(SearchCondition.Operator.gt, "2024-01-01 OR 1=1", "Created Date")
        );
        String where = SoqlQueryHelper.buildWhereClause("account", conditions);
        assertEquals("Date injection should be rejected", "", where);
    }

    @Test
    public void testBuildWhereClause_DateValidIsoFormat_PassesThrough() {
        List<SearchCondition> conditions = List.of(
                new SearchCondition(SearchCondition.Operator.gt, "2025-06-01", "Close Date")
        );
        String where = SoqlQueryHelper.buildWhereClause("opportunity", conditions);
        assertTrue(where.contains("CloseDate > 2025-06-01"));
    }

    @Test
    public void testBuildWhereClause_DateValidIsoDatetime_PassesThrough() {
        List<SearchCondition> conditions = List.of(
                new SearchCondition(SearchCondition.Operator.gt, "2025-06-01T00:00:00Z", "Created Date")
        );
        String where = SoqlQueryHelper.buildWhereClause("account", conditions);
        assertTrue(where.contains("CreatedDate > 2025-06-01T00:00:00Z"));
    }

    @Test
    public void testBuildWhereClause_DateEpochMillis_ConvertedWithTimezone() {
        // 1710511845000 = 2024-03-15 in UTC
        List<SearchCondition> conditions = List.of(
                new SearchCondition(SearchCondition.Operator.equals, "1710511845000", "Close Date")
        );
        String where = SoqlQueryHelper.buildWhereClause("opportunity", conditions, "UTC");
        assertTrue("Epoch millis should be converted to date",
                where.contains("CloseDate = 2024-03-15"));
    }

    @Test
    public void testBuildWhereClause_DateEpochMillis_NoTimezone_StillConverts() {
        // Should still convert epoch millis even without explicit timezone (uses JVM default)
        List<SearchCondition> conditions = List.of(
                new SearchCondition(SearchCondition.Operator.equals, "1710511845000", "Close Date")
        );
        String where = SoqlQueryHelper.buildWhereClause("opportunity", conditions, null);
        assertTrue("Epoch millis should be converted to date format",
                where.contains("CloseDate = 20"));
    }

    @Test
    public void testBuildWhereClause_DateScientificNotation_ConvertedCorrectly() {
        // Framework may send epoch as scientific notation (e.g., 1.710511845E12)
        List<SearchCondition> conditions = List.of(
                new SearchCondition(SearchCondition.Operator.equals, "1.710511845E12", "Close Date")
        );
        String where = SoqlQueryHelper.buildWhereClause("opportunity", conditions, "UTC");
        assertTrue("Scientific notation epoch should be converted",
                where.contains("CloseDate = 2024-03-15"));
    }

    // ========== parseDirection tightened ==========

    @Test
    public void testBuildOrderByClause_AscendingDirection() {
        List<QueryClause> clauses = List.of(
                new QueryClause(QueryClause.Operator.orderby, "ASCENDING", "Name")
        );
        String orderBy = SoqlQueryHelper.buildOrderByClause("account", clauses);
        assertTrue(orderBy.contains("Name ASC"));
    }

    @Test
    public void testBuildOrderByClause_GarbageDirection_DefaultsToDesc() {
        List<QueryClause> clauses = List.of(
                new QueryClause(QueryClause.Operator.orderby, "FIASCO", "Name")
        );
        String orderBy = SoqlQueryHelper.buildOrderByClause("account", clauses);
        assertTrue("FIASCO should not match ASC", orderBy.contains("Name DESC"));
    }
}
