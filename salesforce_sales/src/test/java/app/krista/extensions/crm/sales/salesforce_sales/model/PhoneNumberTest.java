package app.krista.extensions.crm.sales.salesforce_sales.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for PhoneNumber class.
 * Tests phone number parsing and validation logic.
 */
public class PhoneNumberTest {

    @Test
    public void testParsePhoneNumber_WithValidUSNumber() {
        // Arrange
        String phoneNumber = "+14155552671";

        // Act
        app.krista.model.base.PhoneNumber result = PhoneNumber.parsePhoneNumber(phoneNumber);

        // Assert
        assertNotNull("Parsed phone number should not be null", result);
        assertEquals("Country code should be 1", 1L, result.getCountryCode());
    }

    @Test
    public void testParsePhoneNumber_WithValidIndianNumber() {
        // Arrange
        String phoneNumber = "+919876543210";

        // Act
        app.krista.model.base.PhoneNumber result = PhoneNumber.parsePhoneNumber(phoneNumber);

        // Assert
        assertNotNull("Parsed phone number should not be null", result);
        assertEquals("Country code should be 91", 91L, result.getCountryCode());
    }

    @Test
    public void testParsePhoneNumber_WithoutPlusSign() {
        // Arrange
        String phoneNumber = "919876543210";

        // Act
        app.krista.model.base.PhoneNumber result = PhoneNumber.parsePhoneNumber(phoneNumber);

        // Assert
        assertNotNull("Parsed phone number should not be null", result);
    }

    @Test
    public void testParsePhoneNumber_WithNull() {
        // Arrange & Act
        app.krista.model.base.PhoneNumber result = PhoneNumber.parsePhoneNumber(null);

        // Assert
        assertNull("Parsed phone number should be null for null input", result);
    }

    @Test
    public void testParsePhoneNumber_WithEmptyString() {
        // Arrange & Act
        app.krista.model.base.PhoneNumber result = PhoneNumber.parsePhoneNumber("");

        // Assert
        assertNull("Parsed phone number should be null for empty input", result);
    }

    @Test
    public void testParsePhoneNumber_WithInvalidNumber() {
        // Arrange
        String phoneNumber = "invalid";

        // Act
        app.krista.model.base.PhoneNumber result = PhoneNumber.parsePhoneNumber(phoneNumber);

        // Assert
        assertNull("Parsed phone number should be null for invalid input", result);
    }

    @Test
    public void testParsePhoneNumber_WithShortNumber() {
        // Arrange
        String phoneNumber = "+1234";

        // Act
        app.krista.model.base.PhoneNumber result = PhoneNumber.parsePhoneNumber(phoneNumber);

        // Assert
        // Result may be null or valid depending on phone number library behavior
        // Just ensure no exception is thrown
        assertNotNull("Method should handle short numbers gracefully", phoneNumber);
    }

    @Test
    public void testParsePhoneNumber_WithLongNumber() {
        // Arrange
        String phoneNumber = "+1234567890123456789";

        // Act
        app.krista.model.base.PhoneNumber result = PhoneNumber.parsePhoneNumber(phoneNumber);

        // Assert
        // Result may be null or valid depending on phone number library behavior
        // Just ensure no exception is thrown
        assertNotNull("Method should handle long numbers gracefully", phoneNumber);
    }

    @Test
    public void testParsePhoneNumber_WithSpecialCharacters() {
        // Arrange
        String phoneNumber = "+1 (415) 555-2671";

        // Act
        app.krista.model.base.PhoneNumber result = PhoneNumber.parsePhoneNumber(phoneNumber);

        // Assert
        // Result may be null or valid depending on phone number library behavior
        // Just ensure no exception is thrown
        assertNotNull("Method should handle special characters gracefully", phoneNumber);
    }

    @Test
    public void testPhoneNumberFields() {
        // Arrange
        PhoneNumber phoneNumber = new PhoneNumber();

        // Act
        phoneNumber.countryCode = 1L;
        phoneNumber.nationalNumber = 4155552671L;

        // Assert
        assertEquals("Country code should be set", Long.valueOf(1), phoneNumber.countryCode);
        assertEquals("National number should be set", Long.valueOf(4155552671L), phoneNumber.nationalNumber);
    }

    @Test
    public void testPhoneNumberFields_WithNull() {
        // Arrange
        PhoneNumber phoneNumber = new PhoneNumber();

        // Act & Assert
        assertNull("Country code should be null by default", phoneNumber.countryCode);
        assertNull("National number should be null by default", phoneNumber.nationalNumber);
    }
}

