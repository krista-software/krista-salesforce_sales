package app.krista.extensions.crm.sales.salesforce_sales.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Unit tests for OwnerResponse class and its nested classes.
 * Tests owner response data model.
 */
public class OwnerResponseTest {

    @Test
    public void testOwnerResponse_WithValidData() {
        // Arrange
        OwnerResponse response = new OwnerResponse();
        response.totalSize = 5;
        response.done = true;
        response.records = new ArrayList<>();

        // Assert
        assertEquals("Total size should match", Integer.valueOf(5), response.totalSize);
        assertTrue("Done should be true", response.done);
        assertNotNull("Records should not be null", response.records);
        assertTrue("Records should be empty", response.records.isEmpty());
    }

    @Test
    public void testOwnerResponse_DefaultValues() {
        // Arrange & Act
        OwnerResponse response = new OwnerResponse();

        // Assert
        assertNull("Total size should be null by default", response.totalSize);
        assertNull("Done should be null by default", response.done);
        assertNull("Records should be null by default", response.records);
    }

    @Test
    public void testOwnerResponse_WithRecords() {
        // Arrange
        OwnerResponse response = new OwnerResponse();
        OwnerResponse.Record record = response.new Record();
        record.name = "John Doe";
        response.records = Arrays.asList(record);
        response.totalSize = 1;
        response.done = true;

        // Assert
        assertEquals("Should have 1 record", 1, response.records.size());
        assertEquals("Record name should match", "John Doe", response.records.get(0).name);
    }

    @Test
    public void testRecord_WithAttributes() {
        // Arrange
        OwnerResponse response = new OwnerResponse();
        OwnerResponse.Record record = response.new Record();
        OwnerResponse.Attributes attributes = response.new Attributes();
        attributes.type = "Account";
        attributes.url = "/services/data/v58.0/sobjects/Account/001xx000003DGb2AAG";
        record.attributes = attributes;
        record.name = "Test Account";

        // Assert
        assertNotNull("Attributes should not be null", record.attributes);
        assertEquals("Attribute type should match", "Account", record.attributes.type);
        assertEquals("Attribute URL should match", "/services/data/v58.0/sobjects/Account/001xx000003DGb2AAG", record.attributes.url);
        assertEquals("Record name should match", "Test Account", record.name);
    }

    @Test
    public void testRecord_WithAddress() {
        // Arrange
        OwnerResponse response = new OwnerResponse();
        OwnerResponse.Record record = response.new Record();
        OwnerResponse.Address address = response.new Address();
        address.street = "123 Main St";
        address.city = "San Francisco";
        address.state = "CA";
        address.postalCode = "94105";
        address.country = "USA";
        record.address = address;

        // Assert
        assertNotNull("Address should not be null", record.address);
        assertEquals("Street should match", "123 Main St", record.address.street);
        assertEquals("City should match", "San Francisco", record.address.city);
        assertEquals("State should match", "CA", record.address.state);
        assertEquals("Postal code should match", "94105", record.address.postalCode);
        assertEquals("Country should match", "USA", record.address.country);
    }

    @Test
    public void testAddress_WithGeocode() {
        // Arrange
        OwnerResponse response = new OwnerResponse();
        OwnerResponse.Address address = response.new Address();
        address.latitude = 37.7749;
        address.longitude = -122.4194;
        address.geocodeAccuracy = "Address";

        // Assert
        assertEquals("Latitude should match", 37.7749, address.latitude);
        assertEquals("Longitude should match", -122.4194, address.longitude);
        assertEquals("Geocode accuracy should match", "Address", address.geocodeAccuracy);
    }

    @Test
    public void testAddress_DefaultValues() {
        // Arrange
        OwnerResponse response = new OwnerResponse();
        OwnerResponse.Address address = response.new Address();

        // Assert
        assertNull("Street should be null by default", address.street);
        assertNull("City should be null by default", address.city);
        assertNull("State should be null by default", address.state);
        assertNull("Postal code should be null by default", address.postalCode);
        assertNull("Country should be null by default", address.country);
        assertNull("Latitude should be null by default", address.latitude);
        assertNull("Longitude should be null by default", address.longitude);
        assertNull("Geocode accuracy should be null by default", address.geocodeAccuracy);
    }

    @Test
    public void testAttributes_DefaultValues() {
        // Arrange
        OwnerResponse response = new OwnerResponse();
        OwnerResponse.Attributes attributes = response.new Attributes();

        // Assert
        assertNull("Type should be null by default", attributes.type);
        assertNull("URL should be null by default", attributes.url);
    }

    @Test
    public void testRecord_DefaultValues() {
        // Arrange
        OwnerResponse response = new OwnerResponse();
        OwnerResponse.Record record = response.new Record();

        // Assert
        assertNull("Attributes should be null by default", record.attributes);
        assertNull("Name should be null by default", record.name);
        assertNull("Address should be null by default", record.address);
    }

    @Test
    public void testOwnerResponse_WithMultipleRecords() {
        // Arrange
        OwnerResponse response = new OwnerResponse();
        OwnerResponse.Record record1 = response.new Record();
        record1.name = "Record 1";
        OwnerResponse.Record record2 = response.new Record();
        record2.name = "Record 2";
        response.records = Arrays.asList(record1, record2);
        response.totalSize = 2;
        response.done = true;

        // Assert
        assertEquals("Should have 2 records", 2, response.records.size());
        assertEquals("First record name should match", "Record 1", response.records.get(0).name);
        assertEquals("Second record name should match", "Record 2", response.records.get(1).name);
    }

    @Test
    public void testOwnerResponse_ModifyFields() {
        // Arrange
        OwnerResponse response = new OwnerResponse();
        response.totalSize = 5;
        response.done = false;

        // Act
        response.totalSize = 10;
        response.done = true;

        // Assert
        assertEquals("Total size should be modified", Integer.valueOf(10), response.totalSize);
        assertTrue("Done should be modified to true", response.done);
    }

    @Test
    public void testAddress_WithPartialData() {
        // Arrange
        OwnerResponse response = new OwnerResponse();
        OwnerResponse.Address address = response.new Address();
        address.city = "New York";
        address.country = "USA";

        // Assert
        assertNull("Street should be null", address.street);
        assertEquals("City should match", "New York", address.city);
        assertNull("State should be null", address.state);
        assertNull("Postal code should be null", address.postalCode);
        assertEquals("Country should match", "USA", address.country);
    }

    @Test
    public void testOwnerResponse_WithEmptyRecordsList() {
        // Arrange
        OwnerResponse response = new OwnerResponse();
        response.totalSize = 0;
        response.done = true;
        response.records = new ArrayList<>();

        // Assert
        assertEquals("Total size should be 0", Integer.valueOf(0), response.totalSize);
        assertTrue("Done should be true", response.done);
        assertNotNull("Records should not be null", response.records);
        assertTrue("Records should be empty", response.records.isEmpty());
    }
}

