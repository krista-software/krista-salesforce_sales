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

package app.krista.extensions.crm.sales.salesforce_sales.catalog;

import app.krista.extensions.crm.sales.salesforce_sales.impl.OpportunityDaoImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ForecastingArea class.
 * Tests sales forecasting catalog requests and error handling.
 */
public class ForecastingAreaTest {

    @Mock
    private OpportunityDaoImpl mockOpportunityDao;

    private ForecastingArea forecastingArea;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        forecastingArea = new ForecastingArea(mockOpportunityDao);
    }

    // ========== Constructor Tests ==========

    @Test
    public void testConstructor_WithValidDao() {
        // Arrange & Act
        ForecastingArea area = new ForecastingArea(mockOpportunityDao);

        // Assert
        assertNotNull("ForecastingArea should be initialized", area);
    }

    @Test
    public void testConstructor_WithNullDao() {
        // Arrange & Act
        ForecastingArea area = new ForecastingArea(null);

        // Assert
        assertNotNull("ForecastingArea should be initialized even with null DAO", area);
    }

    // ========== Get Sales Forecast Tests ==========

    @Test
    public void testGetSalesForecastForASpecificQuarter_WithValidInput() {
        // Arrange
        List<String> quarters = Arrays.asList("Q1", "Q2");
        String query = "SELECT Id, Amount FROM Opportunity WHERE CloseDate = THIS_QUARTER";
        List<Object> expectedForecast = Arrays.asList(
                createForecastData("Q1", 100000.0),
                createForecastData("Q2", 150000.0)
        );
        
        when(mockOpportunityDao.getExpectedRevenueOfQuarters(quarters, query))
                .thenReturn(expectedForecast);

        // Act
        List<Object> result = forecastingArea.getSalesForecastForASpecificQuarter(quarters, query);

        // Assert
        assertNotNull("Result should not be null", result);
        assertEquals("Result size should match", 2, result.size());
        verify(mockOpportunityDao, times(1)).getExpectedRevenueOfQuarters(quarters, query);
    }

    @Test
    public void testGetSalesForecastForASpecificQuarter_WithSingleQuarter() {
        // Arrange
        List<String> quarters = Arrays.asList("Q1");
        String query = "SELECT Id, Amount FROM Opportunity WHERE CloseDate = THIS_QUARTER";
        List<Object> expectedForecast = Arrays.asList(createForecastData("Q1", 100000.0));
        
        when(mockOpportunityDao.getExpectedRevenueOfQuarters(quarters, query))
                .thenReturn(expectedForecast);

        // Act
        List<Object> result = forecastingArea.getSalesForecastForASpecificQuarter(quarters, query);

        // Assert
        assertNotNull("Result should not be null", result);
        assertEquals("Result size should be 1", 1, result.size());
        verify(mockOpportunityDao, times(1)).getExpectedRevenueOfQuarters(quarters, query);
    }

    @Test
    public void testGetSalesForecastForASpecificQuarter_WithMultipleQuarters() {
        // Arrange
        List<String> quarters = Arrays.asList("Q1", "Q2", "Q3", "Q4");
        String query = "SELECT Id, Amount FROM Opportunity WHERE CloseDate = THIS_YEAR";
        List<Object> expectedForecast = Arrays.asList(
                createForecastData("Q1", 100000.0),
                createForecastData("Q2", 150000.0),
                createForecastData("Q3", 120000.0),
                createForecastData("Q4", 180000.0)
        );
        
        when(mockOpportunityDao.getExpectedRevenueOfQuarters(quarters, query))
                .thenReturn(expectedForecast);

        // Act
        List<Object> result = forecastingArea.getSalesForecastForASpecificQuarter(quarters, query);

        // Assert
        assertNotNull("Result should not be null", result);
        assertEquals("Result size should be 4", 4, result.size());
        verify(mockOpportunityDao, times(1)).getExpectedRevenueOfQuarters(quarters, query);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSalesForecastForASpecificQuarter_WithNullQuery() {
        // Arrange
        List<String> quarters = Arrays.asList("Q1", "Q2");

        // Act
        forecastingArea.getSalesForecastForASpecificQuarter(quarters, null);

        // Assert - exception expected
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSalesForecastForASpecificQuarter_WithEmptyQuery() {
        // Arrange
        List<String> quarters = Arrays.asList("Q1", "Q2");

        // Act
        forecastingArea.getSalesForecastForASpecificQuarter(quarters, "");

        // Assert - exception expected
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSalesForecastForASpecificQuarter_WithWhitespaceQuery() {
        // Arrange
        List<String> quarters = Arrays.asList("Q1", "Q2");

        // Act
        forecastingArea.getSalesForecastForASpecificQuarter(quarters, "   ");

        // Assert - exception expected
    }

    @Test
    public void testGetSalesForecastForASpecificQuarter_WithEmptyQuartersList() {
        // Arrange
        List<String> quarters = new ArrayList<>();
        String query = "SELECT Id, Amount FROM Opportunity";
        List<Object> expectedForecast = new ArrayList<>();
        
        when(mockOpportunityDao.getExpectedRevenueOfQuarters(quarters, query))
                .thenReturn(expectedForecast);

        // Act
        List<Object> result = forecastingArea.getSalesForecastForASpecificQuarter(quarters, query);

        // Assert
        assertNotNull("Result should not be null", result);
        assertTrue("Result should be empty", result.isEmpty());
        verify(mockOpportunityDao, times(1)).getExpectedRevenueOfQuarters(quarters, query);
    }

    @Test
    public void testGetSalesForecastForASpecificQuarter_WithNullQuartersList() {
        // Arrange
        String query = "SELECT Id, Amount FROM Opportunity";
        List<Object> expectedForecast = new ArrayList<>();
        
        when(mockOpportunityDao.getExpectedRevenueOfQuarters(null, query))
                .thenReturn(expectedForecast);

        // Act
        List<Object> result = forecastingArea.getSalesForecastForASpecificQuarter(null, query);

        // Assert
        assertNotNull("Result should not be null", result);
        assertTrue("Result should be empty", result.isEmpty());
        verify(mockOpportunityDao, times(1)).getExpectedRevenueOfQuarters(null, query);
    }

    @Test
    public void testGetSalesForecastForASpecificQuarter_DaoThrowsException() {
        // Arrange
        List<String> quarters = Arrays.asList("Q1", "Q2");
        String query = "SELECT Id, Amount FROM Opportunity";
        
        when(mockOpportunityDao.getExpectedRevenueOfQuarters(quarters, query))
                .thenThrow(new IllegalArgumentException("Invalid query"));

        // Act
        List<Object> result = forecastingArea.getSalesForecastForASpecificQuarter(quarters, query);

        // Assert
        assertNotNull("Result should not be null", result);
        assertTrue("Result should be empty when exception occurs", result.isEmpty());
        verify(mockOpportunityDao, times(1)).getExpectedRevenueOfQuarters(quarters, query);
    }

    @Test
    public void testGetSalesForecastForASpecificQuarter_DaoReturnsEmptyList() {
        // Arrange
        List<String> quarters = Arrays.asList("Q1", "Q2");
        String query = "SELECT Id, Amount FROM Opportunity WHERE Amount > 1000000";
        
        when(mockOpportunityDao.getExpectedRevenueOfQuarters(quarters, query))
                .thenReturn(new ArrayList<>());

        // Act
        List<Object> result = forecastingArea.getSalesForecastForASpecificQuarter(quarters, query);

        // Assert
        assertNotNull("Result should not be null", result);
        assertTrue("Result should be empty", result.isEmpty());
        verify(mockOpportunityDao, times(1)).getExpectedRevenueOfQuarters(quarters, query);
    }

    @Test
    public void testGetSalesForecastForASpecificQuarter_WithComplexQuery() {
        // Arrange
        List<String> quarters = Arrays.asList("Q1", "Q2");
        String query = "SELECT Id, Amount, StageName FROM Opportunity WHERE StageName = 'Closed Won' AND CloseDate = THIS_QUARTER";
        List<Object> expectedForecast = Arrays.asList(createForecastData("Q1", 250000.0));
        
        when(mockOpportunityDao.getExpectedRevenueOfQuarters(quarters, query))
                .thenReturn(expectedForecast);

        // Act
        List<Object> result = forecastingArea.getSalesForecastForASpecificQuarter(quarters, query);

        // Assert
        assertNotNull("Result should not be null", result);
        assertEquals("Result size should be 1", 1, result.size());
        verify(mockOpportunityDao, times(1)).getExpectedRevenueOfQuarters(quarters, query);
    }

    @Test
    public void testGetSalesForecastForASpecificQuarter_VerifyDaoInteraction() {
        // Arrange
        List<String> quarters = Arrays.asList("Q1");
        String query = "SELECT Id FROM Opportunity";
        
        when(mockOpportunityDao.getExpectedRevenueOfQuarters(any(), any()))
                .thenReturn(new ArrayList<>());

        // Act
        forecastingArea.getSalesForecastForASpecificQuarter(quarters, query);

        // Assert
        verify(mockOpportunityDao, times(1)).getExpectedRevenueOfQuarters(eq(quarters), eq(query));
        verifyNoMoreInteractions(mockOpportunityDao);
    }

    @Test
    public void testGetSalesForecastForASpecificQuarter_WithLongQuery() {
        // Arrange
        List<String> quarters = Arrays.asList("Q1", "Q2");
        String query = "SELECT Id, Amount, Name, StageName, CloseDate, AccountId, OwnerId, " +
                "Probability, ExpectedRevenue, TotalOpportunityQuantity, NextStep, Description " +
                "FROM Opportunity WHERE CloseDate = THIS_QUARTER AND StageName IN ('Prospecting', 'Qualification')";
        List<Object> expectedForecast = Arrays.asList(createForecastData("Q1", 300000.0));
        
        when(mockOpportunityDao.getExpectedRevenueOfQuarters(quarters, query))
                .thenReturn(expectedForecast);

        // Act
        List<Object> result = forecastingArea.getSalesForecastForASpecificQuarter(quarters, query);

        // Assert
        assertNotNull("Result should not be null", result);
        assertEquals("Result size should be 1", 1, result.size());
        verify(mockOpportunityDao, times(1)).getExpectedRevenueOfQuarters(quarters, query);
    }

    // ========== Helper Methods ==========

    /**
     * Helper method to create forecast data for testing.
     */
    private Object createForecastData(String quarter, Double amount) {
        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("quarter", quarter);
        data.put("amount", amount);
        return data;
    }
}

