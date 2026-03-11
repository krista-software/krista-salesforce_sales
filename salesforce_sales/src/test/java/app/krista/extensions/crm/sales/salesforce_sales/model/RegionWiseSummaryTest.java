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

package app.krista.extensions.crm.sales.salesforce_sales.model;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Unit tests for RegionWiseSummary class.
 * Tests region-wise summary data model and conversion methods.
 */
public class RegionWiseSummaryTest {

    @Test
    public void testRegionWiseSummary_WithValidData() {
        // Arrange
        RegionWiseSummary summary = new RegionWiseSummary();
        summary.amount = 150000.50;
        summary.region = "North America";

        // Assert
        assertEquals("Amount should match", 150000.50, summary.amount, 0.01);
        assertEquals("Region should match", "North America", summary.region);
    }

    @Test
    public void testRegionWiseSummary_GetAmount() {
        // Arrange
        RegionWiseSummary summary = new RegionWiseSummary();
        summary.amount = 250000.75;

        // Act
        Object result = summary.getAmount();

        // Assert
        assertNotNull("Amount should not be null", result);
        assertEquals("Amount should match", 250000.75, (Double) result, 0.01);
    }

    @Test
    public void testRegionWiseSummary_GetRegion() {
        // Arrange
        RegionWiseSummary summary = new RegionWiseSummary();
        summary.region = "Europe";

        // Act
        Object result = summary.getRegion();

        // Assert
        assertNotNull("Region should not be null", result);
        assertEquals("Region should match", "Europe", result);
    }

    @Test
    public void testRegionWiseSummary_ToMap() {
        // Arrange
        RegionWiseSummary summary = new RegionWiseSummary();
        summary.amount = 100000.0;
        summary.region = "Asia";

        // Act
        Map<String, Object> result = summary.toMap();

        // Assert
        assertNotNull("Result map should not be null", result);
        assertEquals("Should have 2 entries", 2, result.size());
        assertEquals("Amount should match", 100000.0, (Double) result.get("Amount"), 0.01);
        assertEquals("Region should match", "Asia", result.get("Region"));
    }

    @Test
    public void testRegionWiseSummary_ToMap_WithNullValues() {
        // Arrange
        RegionWiseSummary summary = new RegionWiseSummary();

        // Act
        Map<String, Object> result = summary.toMap();

        // Assert
        assertNotNull("Result map should not be null", result);
        assertEquals("Should have 2 entries", 2, result.size());
        assertNull("Amount should be null", result.get("Amount"));
        assertNull("Region should be null", result.get("Region"));
    }

    @Test
    public void testRegionWiseSummary_ToMap_WithZeroAmount() {
        // Arrange
        RegionWiseSummary summary = new RegionWiseSummary();
        summary.amount = 0.0;
        summary.region = "Antarctica";

        // Act
        Map<String, Object> result = summary.toMap();

        // Assert
        assertNotNull("Result map should not be null", result);
        assertEquals("Amount should be 0", 0.0, (Double) result.get("Amount"), 0.01);
        assertEquals("Region should match", "Antarctica", result.get("Region"));
    }

    @Test
    public void testRegionWiseSummary_ToMap_WithNegativeAmount() {
        // Arrange
        RegionWiseSummary summary = new RegionWiseSummary();
        summary.amount = -5000.0;
        summary.region = "South America";

        // Act
        Map<String, Object> result = summary.toMap();

        // Assert
        assertNotNull("Result map should not be null", result);
        assertEquals("Amount should be negative", -5000.0, (Double) result.get("Amount"), 0.01);
        assertEquals("Region should match", "South America", result.get("Region"));
    }

    @Test
    public void testRegionWiseSummary_DefaultValues() {
        // Arrange & Act
        RegionWiseSummary summary = new RegionWiseSummary();

        // Assert
        assertNull("Amount should be null by default", summary.amount);
        assertNull("Region should be null by default", summary.region);
    }

    @Test
    public void testRegionWiseSummary_ModifyFields() {
        // Arrange
        RegionWiseSummary summary = new RegionWiseSummary();
        summary.amount = 50000.0;
        summary.region = "Region 1";

        // Act
        summary.amount = 75000.0;
        summary.region = "Region 2";

        // Assert
        assertEquals("Amount should be modified", 75000.0, summary.amount, 0.01);
        assertEquals("Region should be modified", "Region 2", summary.region);
    }

    @Test
    public void testRegionWiseSummary_ToMap_WithEmptyRegion() {
        // Arrange
        RegionWiseSummary summary = new RegionWiseSummary();
        summary.amount = 100000.0;
        summary.region = "";

        // Act
        Map<String, Object> result = summary.toMap();

        // Assert
        assertNotNull("Result map should not be null", result);
        assertEquals("Region should be empty string", "", result.get("Region"));
    }

    @Test
    public void testRegionWiseSummary_ToMap_WithLargeAmount() {
        // Arrange
        RegionWiseSummary summary = new RegionWiseSummary();
        summary.amount = 999999999.99;
        summary.region = "Global";

        // Act
        Map<String, Object> result = summary.toMap();

        // Assert
        assertNotNull("Result map should not be null", result);
        assertEquals("Amount should match", 999999999.99, (Double) result.get("Amount"), 0.01);
    }

    @Test
    public void testRegionWiseSummary_ToMap_PreservesOrder() {
        // Arrange
        RegionWiseSummary summary = new RegionWiseSummary();
        summary.amount = 50000.0;
        summary.region = "Test Region";

        // Act
        Map<String, Object> result = summary.toMap();

        // Assert
        assertNotNull("Result map should not be null", result);
        // LinkedHashMap preserves insertion order
        Object[] keys = result.keySet().toArray();
        assertEquals("First key should be Amount", "Amount", keys[0]);
        assertEquals("Second key should be Region", "Region", keys[1]);
    }
}

