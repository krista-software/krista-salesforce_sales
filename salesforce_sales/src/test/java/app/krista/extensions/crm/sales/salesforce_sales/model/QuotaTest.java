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
 * Unit tests for Quota class.
 * Tests quota data model and conversion methods.
 */
public class QuotaTest {

    @Test
    public void testQuota_WithValidData() {
        // Arrange
        Quota quota = new Quota();
        Quota.QuotaOwner owner = new Quota.QuotaOwner();
        owner.Name = "John Doe";
        quota.QuotaOwner = owner;
        quota.quotaAmount = 100000L;

        // Assert
        assertNotNull("Quota owner should not be null", quota.QuotaOwner);
        assertEquals("Owner name should match", "John Doe", quota.QuotaOwner.Name);
        assertEquals("Quota amount should match", 100000L, quota.quotaAmount);
    }

    @Test
    public void testQuota_ToMap() {
        // Arrange
        Quota quota = new Quota();
        Quota.QuotaOwner owner = new Quota.QuotaOwner();
        owner.Name = "Jane Smith";
        quota.QuotaOwner = owner;
        quota.quotaAmount = 250000L;

        // Act
        Map<String, Object> result = quota.to();

        // Assert
        assertNotNull("Result map should not be null", result);
        assertEquals("Should have 2 entries", 2, result.size());
        assertEquals("Owner Name should match", "Jane Smith", result.get("Owner Name"));
        assertTrue("Quota Amount should be a number", result.get("Quota Amount") instanceof Number);
        assertEquals("Quota Amount should match", 250000L, ((Number) result.get("Quota Amount")).longValue());
    }

    @Test
    public void testQuota_ToMap_WithZeroAmount() {
        // Arrange
        Quota quota = new Quota();
        Quota.QuotaOwner owner = new Quota.QuotaOwner();
        owner.Name = "Test User";
        quota.QuotaOwner = owner;
        quota.quotaAmount = 0L;

        // Act
        Map<String, Object> result = quota.to();

        // Assert
        assertNotNull("Result map should not be null", result);
        assertTrue("Quota Amount should be a number", result.get("Quota Amount") instanceof Number);
        assertEquals("Quota Amount should be 0", 0L, ((Number) result.get("Quota Amount")).longValue());
    }

    @Test
    public void testQuota_ToMap_WithNegativeAmount() {
        // Arrange
        Quota quota = new Quota();
        Quota.QuotaOwner owner = new Quota.QuotaOwner();
        owner.Name = "Test User";
        quota.QuotaOwner = owner;
        quota.quotaAmount = -1000L;

        // Act
        Map<String, Object> result = quota.to();

        // Assert
        assertNotNull("Result map should not be null", result);
        assertTrue("Quota Amount should be a number", result.get("Quota Amount") instanceof Number);
        assertEquals("Quota Amount should be -1000", -1000L, ((Number) result.get("Quota Amount")).longValue());
    }

    @Test
    public void testQuota_ToMap_WithLargeAmount() {
        // Arrange
        Quota quota = new Quota();
        Quota.QuotaOwner owner = new Quota.QuotaOwner();
        owner.Name = "Big Spender";
        quota.QuotaOwner = owner;
        quota.quotaAmount = 100000000L;

        // Act
        Map<String, Object> result = quota.to();

        // Assert
        assertNotNull("Result map should not be null", result);
        assertTrue("Quota Amount should be a number", result.get("Quota Amount") instanceof Number);
        assertEquals("Quota Amount should match", 100000000L, ((Number) result.get("Quota Amount")).longValue());
    }

    @Test
    public void testQuotaOwner_WithName() {
        // Arrange & Act
        Quota.QuotaOwner owner = new Quota.QuotaOwner();
        owner.Name = "Test Owner";

        // Assert
        assertEquals("Owner name should match", "Test Owner", owner.Name);
    }

    @Test
    public void testQuotaOwner_WithNullName() {
        // Arrange & Act
        Quota.QuotaOwner owner = new Quota.QuotaOwner();

        // Assert
        assertNull("Owner name should be null by default", owner.Name);
    }

    @Test
    public void testQuota_DefaultValues() {
        // Arrange & Act
        Quota quota = new Quota();

        // Assert
        assertNull("Quota owner should be null by default", quota.QuotaOwner);
        assertEquals("Quota amount should be 0 by default", 0L, quota.quotaAmount);
    }

    @Test
    public void testQuota_ModifyFields() {
        // Arrange
        Quota quota = new Quota();
        Quota.QuotaOwner owner1 = new Quota.QuotaOwner();
        owner1.Name = "Owner 1";
        quota.QuotaOwner = owner1;
        quota.quotaAmount = 50000L;

        // Act
        Quota.QuotaOwner owner2 = new Quota.QuotaOwner();
        owner2.Name = "Owner 2";
        quota.QuotaOwner = owner2;
        quota.quotaAmount = 75000L;

        // Assert
        assertEquals("Owner name should be modified", "Owner 2", quota.QuotaOwner.Name);
        assertEquals("Quota amount should be modified", 75000L, quota.quotaAmount);
    }

    @Test
    public void testQuota_ToMap_WithEmptyOwnerName() {
        // Arrange
        Quota quota = new Quota();
        Quota.QuotaOwner owner = new Quota.QuotaOwner();
        owner.Name = "";
        quota.QuotaOwner = owner;
        quota.quotaAmount = 100000L;

        // Act
        Map<String, Object> result = quota.to();

        // Assert
        assertNotNull("Result map should not be null", result);
        assertEquals("Owner Name should be empty string", "", result.get("Owner Name"));
    }
}

