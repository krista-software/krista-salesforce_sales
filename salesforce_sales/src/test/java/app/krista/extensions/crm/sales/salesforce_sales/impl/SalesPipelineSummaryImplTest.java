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

package app.krista.extensions.crm.sales.salesforce_sales.impl;

import app.krista.extensions.crm.sales.salesforce_sales.model.SFSalesPipelineSummary;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for SalesPipelineSummaryImpl class.
 * Tests sales pipeline summary implementation.
 */
public class SalesPipelineSummaryImplTest {

    private SFSalesPipelineSummary sfSummary;
    private SalesPipelineSummaryImpl summary;

    @Before
    public void setUp() {
        sfSummary = new SFSalesPipelineSummary();
    }

    @Test
    public void testConstructor() {
        // Arrange & Act
        summary = new SalesPipelineSummaryImpl(sfSummary);

        // Assert
        assertNotNull("Summary should not be null", summary);
    }

    @Test
    public void testGetStatus() {
        // Arrange
        sfSummary.StageName = "Prospecting";
        summary = new SalesPipelineSummaryImpl(sfSummary);

        // Act
        String result = summary.getStatus();

        // Assert
        assertEquals("Status should match", "Prospecting", result);
    }

    @Test
    public void testGetStatus_WithNull() {
        // Arrange
        sfSummary.StageName = null;
        summary = new SalesPipelineSummaryImpl(sfSummary);

        // Act
        String result = summary.getStatus();

        // Assert
        assertNull("Status should be null", result);
    }

    @Test
    public void testGetAverage() {
        // Arrange
        sfSummary.Average = 50000.50;
        summary = new SalesPipelineSummaryImpl(sfSummary);

        // Act
        Double result = summary.getAverage();

        // Assert
        assertEquals("Average should match", 50000.50, result, 0.01);
    }

    @Test
    public void testGetTotal() {
        // Arrange
        sfSummary.Total = 250000.75;
        summary = new SalesPipelineSummaryImpl(sfSummary);

        // Act
        Double result = summary.getTotal();

        // Assert
        assertEquals("Total should match", 250000.75, result, 0.01);
    }

    @Test
    public void testGetTotal_WithZero() {
        // Arrange
        sfSummary.Total = 0.0;
        summary = new SalesPipelineSummaryImpl(sfSummary);

        // Act
        Double result = summary.getTotal();

        // Assert
        assertEquals("Total should be 0", 0.0, result, 0.01);
    }

    @Test
    public void testGetAverage_WithZero() {
        // Arrange
        sfSummary.Average = 0.0;
        summary = new SalesPipelineSummaryImpl(sfSummary);

        // Act
        Double result = summary.getAverage();

        // Assert
        assertEquals("Average should be 0", 0.0, result, 0.01);
    }

    @Test
    public void testAllFields() {
        // Arrange
        sfSummary.StageName = "Closed Won";
        sfSummary.Average = 75000.0;
        sfSummary.Total = 300000.0;
        summary = new SalesPipelineSummaryImpl(sfSummary);

        // Assert
        assertEquals("Status should match", "Closed Won", summary.getStatus());
        assertEquals("Average should match", 75000.0, summary.getAverage(), 0.01);
        assertEquals("Total should match", 300000.0, summary.getTotal(), 0.01);
    }

    @Test
    public void testAllFields_WithDefaultValues() {
        // Arrange
        sfSummary.StageName = null;
        sfSummary.Average = 0.0;
        sfSummary.Total = 0.0;
        summary = new SalesPipelineSummaryImpl(sfSummary);

        // Assert
        assertNull("Status should be null", summary.getStatus());
        assertEquals("Average should be 0", 0.0, summary.getAverage(), 0.01);
        assertEquals("Total should be 0", 0.0, summary.getTotal(), 0.01);
    }

    @Test
    public void testGetStatus_WithEmptyString() {
        // Arrange
        sfSummary.StageName = "";
        summary = new SalesPipelineSummaryImpl(sfSummary);

        // Act
        String result = summary.getStatus();

        // Assert
        assertEquals("Status should be empty string", "", result);
    }

    @Test
    public void testGetTotal_WithNegativeValue() {
        // Arrange
        sfSummary.Total = -1000.0;
        summary = new SalesPipelineSummaryImpl(sfSummary);

        // Act
        Double result = summary.getTotal();

        // Assert
        assertEquals("Total should be negative", -1000.0, result, 0.01);
    }

    @Test
    public void testGetAverage_WithLargeValue() {
        // Arrange
        sfSummary.Average = 999999999.99;
        summary = new SalesPipelineSummaryImpl(sfSummary);

        // Act
        Double result = summary.getAverage();

        // Assert
        assertEquals("Average should match large value", 999999999.99, result, 0.01);
    }
}

