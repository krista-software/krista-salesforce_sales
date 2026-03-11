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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for SalesforceListResponse class.
 * Tests generic list response handling.
 */
public class SalesforceListResponseTest {

    @Test
    public void testSalesforceListResponse_WithStringRecords() {
        // Arrange
        SalesforceListResponse<String> response = new SalesforceListResponse<>();
        response.totalSize = 3;
        response.done = true;
        response.records = Arrays.asList("Record1", "Record2", "Record3");

        // Assert
        assertEquals("Total size should be 3", 3, response.totalSize);
        assertTrue("Done should be true", response.done);
        assertNotNull("Records should not be null", response.records);
        assertEquals("Should have 3 records", 3, response.records.size());
    }

    @Test
    public void testSalesforceListResponse_WithEmptyRecords() {
        // Arrange
        SalesforceListResponse<String> response = new SalesforceListResponse<>();
        response.totalSize = 0;
        response.done = true;
        response.records = new ArrayList<>();

        // Assert
        assertEquals("Total size should be 0", 0, response.totalSize);
        assertTrue("Done should be true", response.done);
        assertNotNull("Records should not be null", response.records);
        assertTrue("Records should be empty", response.records.isEmpty());
    }

    @Test
    public void testSalesforceListResponse_WithNullRecords() {
        // Arrange
        SalesforceListResponse<String> response = new SalesforceListResponse<>();
        response.totalSize = 0;
        response.done = false;
        response.records = null;

        // Assert
        assertEquals("Total size should be 0", 0, response.totalSize);
        assertFalse("Done should be false", response.done);
        assertNull("Records should be null", response.records);
    }

    @Test
    public void testSalesforceListResponse_WithIntegerRecords() {
        // Arrange
        SalesforceListResponse<Integer> response = new SalesforceListResponse<>();
        response.totalSize = 5;
        response.done = true;
        response.records = Arrays.asList(1, 2, 3, 4, 5);

        // Assert
        assertEquals("Total size should be 5", 5, response.totalSize);
        assertTrue("Done should be true", response.done);
        assertEquals("Should have 5 records", 5, response.records.size());
        assertEquals("First record should be 1", Integer.valueOf(1), response.records.get(0));
    }

    @Test
    public void testSalesforceListResponse_DefaultValues() {
        // Arrange & Act
        SalesforceListResponse<String> response = new SalesforceListResponse<>();

        // Assert
        assertEquals("Total size should be 0 by default", 0, response.totalSize);
        assertFalse("Done should be false by default", response.done);
        assertNull("Records should be null by default", response.records);
    }

    @Test
    public void testSalesforceListResponse_WithLargeRecordSet() {
        // Arrange
        SalesforceListResponse<String> response = new SalesforceListResponse<>();
        List<String> largeList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            largeList.add("Record" + i);
        }
        response.totalSize = 1000;
        response.done = true;
        response.records = largeList;

        // Assert
        assertEquals("Total size should be 1000", 1000, response.totalSize);
        assertTrue("Done should be true", response.done);
        assertEquals("Should have 1000 records", 1000, response.records.size());
    }

    @Test
    public void testSalesforceListResponse_WithPartialResults() {
        // Arrange
        SalesforceListResponse<String> response = new SalesforceListResponse<>();
        response.totalSize = 100;
        response.done = false;
        response.records = Arrays.asList("Record1", "Record2", "Record3");

        // Assert
        assertEquals("Total size should be 100", 100, response.totalSize);
        assertFalse("Done should be false for partial results", response.done);
        assertEquals("Should have 3 records", 3, response.records.size());
    }

    @Test
    public void testSalesforceListResponse_ModifyFields() {
        // Arrange
        SalesforceListResponse<String> response = new SalesforceListResponse<>();
        response.totalSize = 5;
        response.done = false;
        response.records = Arrays.asList("Record1");

        // Act
        response.totalSize = 10;
        response.done = true;
        response.records = Arrays.asList("Record1", "Record2");

        // Assert
        assertEquals("Total size should be modified to 10", 10, response.totalSize);
        assertTrue("Done should be modified to true", response.done);
        assertEquals("Should have 2 records after modification", 2, response.records.size());
    }

    @Test
    public void testSalesforceListResponse_WithCustomObject() {
        // Arrange
        SalesforceListResponse<TestObject> response = new SalesforceListResponse<>();
        TestObject obj1 = new TestObject("Test1");
        TestObject obj2 = new TestObject("Test2");
        response.totalSize = 2;
        response.done = true;
        response.records = Arrays.asList(obj1, obj2);

        // Assert
        assertEquals("Total size should be 2", 2, response.totalSize);
        assertTrue("Done should be true", response.done);
        assertEquals("Should have 2 records", 2, response.records.size());
        assertEquals("First record name should match", "Test1", response.records.get(0).name);
    }

    // Helper class for testing
    private static class TestObject {
        String name;

        TestObject(String name) {
            this.name = name;
        }
    }
}

