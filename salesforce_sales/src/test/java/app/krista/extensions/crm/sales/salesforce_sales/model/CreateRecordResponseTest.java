package app.krista.extensions.crm.sales.salesforce_sales.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Unit tests for CreateRecordResponse class.
 * Tests response object creation and toString method.
 */
public class CreateRecordResponseTest {

    @Test
    public void testCreateRecordResponse_WithAllFields() {
        // Arrange
        CreateRecordResponse response = new CreateRecordResponse();
        response.id = "001xx000003DGb2AAG";
        response.success = "true";
        response.errors = new ArrayList<>();

        // Assert
        assertEquals("ID should match", "001xx000003DGb2AAG", response.id);
        assertEquals("Success should match", "true", response.success);
        assertNotNull("Errors should not be null", response.errors);
        assertTrue("Errors should be empty", response.errors.isEmpty());
    }

    @Test
    public void testCreateRecordResponse_WithErrors() {
        // Arrange
        CreateRecordResponse response = new CreateRecordResponse();
        response.id = null;
        response.success = "false";
        response.errors = Arrays.asList("Error 1", "Error 2");

        // Assert
        assertNull("ID should be null", response.id);
        assertEquals("Success should be false", "false", response.success);
        assertNotNull("Errors should not be null", response.errors);
        assertEquals("Should have 2 errors", 2, response.errors.size());
    }

    @Test
    public void testCreateRecordResponse_DefaultValues() {
        // Arrange & Act
        CreateRecordResponse response = new CreateRecordResponse();

        // Assert
        assertNull("ID should be null by default", response.id);
        assertNull("Success should be null by default", response.success);
        assertNull("Errors should be null by default", response.errors);
    }

    @Test
    public void testToString_WithAllFields() {
        // Arrange
        CreateRecordResponse response = new CreateRecordResponse();
        response.id = "001xx000003DGb2AAG";
        response.success = "true";
        response.errors = new ArrayList<>();

        // Act
        String result = response.toString();

        // Assert
        assertNotNull("toString should not return null", result);
        assertTrue("toString should contain id", result.contains("id='001xx000003DGb2AAG'"));
        assertTrue("toString should contain success", result.contains("success='true'"));
        assertTrue("toString should contain errors", result.contains("errors=[]"));
    }

    @Test
    public void testToString_WithNullFields() {
        // Arrange
        CreateRecordResponse response = new CreateRecordResponse();

        // Act
        String result = response.toString();

        // Assert
        assertNotNull("toString should not return null", result);
        assertTrue("toString should contain null id", result.contains("id='null'"));
        assertTrue("toString should contain null success", result.contains("success='null'"));
        assertTrue("toString should contain null errors", result.contains("errors=null"));
    }

    @Test
    public void testToString_WithErrors() {
        // Arrange
        CreateRecordResponse response = new CreateRecordResponse();
        response.id = null;
        response.success = "false";
        response.errors = Arrays.asList("Error 1", "Error 2");

        // Act
        String result = response.toString();

        // Assert
        assertNotNull("toString should not return null", result);
        assertTrue("toString should contain errors list", result.contains("errors=[Error 1, Error 2]"));
    }

    @Test
    public void testSetFields() {
        // Arrange
        CreateRecordResponse response = new CreateRecordResponse();

        // Act
        response.id = "test-id";
        response.success = "true";
        response.errors = new ArrayList<>();

        // Assert
        assertEquals("ID should be set", "test-id", response.id);
        assertEquals("Success should be set", "true", response.success);
        assertNotNull("Errors should be set", response.errors);
    }

    @Test
    public void testModifyFields() {
        // Arrange
        CreateRecordResponse response = new CreateRecordResponse();
        response.id = "initial-id";
        response.success = "true";

        // Act
        response.id = "modified-id";
        response.success = "false";

        // Assert
        assertEquals("ID should be modified", "modified-id", response.id);
        assertEquals("Success should be modified", "false", response.success);
    }
}

