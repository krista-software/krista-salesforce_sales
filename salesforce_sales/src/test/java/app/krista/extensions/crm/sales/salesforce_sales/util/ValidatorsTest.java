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

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Comprehensive real-world test cases for Validators utility class.
 * Tests all public static methods without using Mockito.
 */
public class ValidatorsTest {

    // ========== Tests for getDoubleOrZero() ==========

    @Test
    public void testGetDoubleOrZero_WithValidPositiveDouble() {
        // Real-world scenario: Processing opportunity amount
        Double opportunityAmount = 150000.50;
        Double result = Validators.getDoubleOrZero(opportunityAmount);
        assertEquals("Should return the exact opportunity amount", opportunityAmount, result, 0.0);
    }

    @Test
    public void testGetDoubleOrZero_WithValidNegativeDouble() {
        // Real-world scenario: Processing refund or credit amount
        Double creditAmount = -5000.75;
        Double result = Validators.getDoubleOrZero(creditAmount);
        assertEquals("Should return the exact negative amount", creditAmount, result, 0.0);
    }

    @Test
    public void testGetDoubleOrZero_WithZero() {
        // Real-world scenario: Processing zero-value opportunity
        Double zeroAmount = 0.0;
        Double result = Validators.getDoubleOrZero(zeroAmount);
        assertEquals("Should return zero", 0.0, result, 0.0);
    }

    @Test
    public void testGetDoubleOrZero_WithNull() {
        // Real-world scenario: Missing annual revenue field from Salesforce
        Double result = Validators.getDoubleOrZero(null);
        assertEquals("Should return zero when value is null", 0.0, result, 0.0);
    }

    @Test
    public void testGetDoubleOrZero_WithVeryLargeNumber() {
        // Real-world scenario: Processing enterprise deal worth millions
        Double largeAmount = 99999999.99;
        Double result = Validators.getDoubleOrZero(largeAmount);
        assertEquals("Should handle large amounts correctly", largeAmount, result, 0.0);
    }

    @Test
    public void testGetDoubleOrZero_WithVerySmallDecimal() {
        // Real-world scenario: Processing fractional currency amounts
        Double smallAmount = 0.01;
        Double result = Validators.getDoubleOrZero(smallAmount);
        assertEquals("Should handle small decimal amounts correctly", smallAmount, result, 0.0);
    }

    // ========== Tests for getStringOrEmpty() ==========

    @Test
    public void testGetStringOrEmpty_WithValidString() {
        // Real-world scenario: Processing account name
        String accountName = "Acme Corporation";
        String result = Validators.getStringOrEmpty(accountName);
        assertEquals("Should return the exact account name", accountName, result);
    }

    @Test
    public void testGetStringOrEmpty_WithNull() {
        // Real-world scenario: Missing optional field like fax number
        String result = Validators.getStringOrEmpty(null);
        assertEquals("Should return empty string when value is null", "", result);
    }

    @Test
    public void testGetStringOrEmpty_WithEmptyString() {
        // Real-world scenario: Empty description field
        String emptyString = "";
        String result = Validators.getStringOrEmpty(emptyString);
        assertEquals("Should return empty string as-is", "", result);
    }

    @Test
    public void testGetStringOrEmpty_WithWhitespaceString() {
        // Real-world scenario: Field with only spaces
        String whitespace = "   ";
        String result = Validators.getStringOrEmpty(whitespace);
        assertEquals("Should preserve whitespace", "   ", result);
    }

    @Test
    public void testGetStringOrEmpty_WithInteger() {
        // Real-world scenario: Converting numeric ID to string
        Integer numericId = 12345;
        String result = Validators.getStringOrEmpty(numericId);
        assertEquals("Should convert integer to string", "12345", result);
    }

    @Test
    public void testGetStringOrEmpty_WithBoolean() {
        // Real-world scenario: Converting boolean flag to string
        Boolean isActive = true;
        String result = Validators.getStringOrEmpty(isActive);
        assertEquals("Should convert boolean to string", "true", result);
    }

    @Test
    public void testGetStringOrEmpty_WithSpecialCharacters() {
        // Real-world scenario: Processing company name with special characters
        String specialName = "O'Reilly & Associates, Inc.";
        String result = Validators.getStringOrEmpty(specialName);
        assertEquals("Should handle special characters correctly", specialName, result);
    }

    @Test
    public void testGetStringOrEmpty_WithUnicodeCharacters() {
        // Real-world scenario: Processing international company names
        String unicodeName = "北京科技有限公司";
        String result = Validators.getStringOrEmpty(unicodeName);
        assertEquals("Should handle Unicode characters correctly", unicodeName, result);
    }

    // ========== Tests for validation() ==========

    @Test
    public void testValidation_WithAllValidInputs() {
        // Real-world scenario: Sending a complete email
        String emailAddresses = "john.doe@example.com";
        String subject = "Q4 Sales Report";
        String body = "Please find attached the Q4 sales report for your review.";

        // Should not throw any exception
        Validators.validation(emailAddresses, subject, body);
    }

    @Test
    public void testValidation_WithMultipleEmailAddresses() {
        // Real-world scenario: Sending email to multiple recipients
        String emailAddresses = "john@example.com, jane@example.com, bob@example.com";
        String subject = "Team Meeting";
        String body = "Meeting scheduled for tomorrow at 10 AM.";

        // Should not throw any exception
        Validators.validation(emailAddresses, subject, body);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidation_WithNullEmailAddresses() {
        // Real-world scenario: Missing recipient email
        Validators.validation(null, "Subject", "Body content");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidation_WithEmptyEmailAddresses() {
        // Real-world scenario: Empty recipient field
        Validators.validation("", "Subject", "Body content");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidation_WithWhitespaceOnlyEmailAddresses() {
        // Real-world scenario: Email field with only spaces
        Validators.validation("   ", "Subject", "Body content");
    }

    @Test
    public void testValidation_WithNullEmailAddresses_VerifyErrorMessage() {
        // Real-world scenario: Verify user-friendly error message
        try {
            Validators.validation(null, "Subject", "Body");
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Email address is required. Please provide at least one recipient email address.",
                    e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidation_WithNullSubject() {
        // Real-world scenario: Missing email subject
        Validators.validation("john@example.com", null, "Body content");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidation_WithEmptySubject() {
        // Real-world scenario: Empty subject field
        Validators.validation("john@example.com", "", "Body content");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidation_WithWhitespaceOnlySubject() {
        // Real-world scenario: Subject with only spaces
        Validators.validation("john@example.com", "   ", "Body content");
    }

    @Test
    public void testValidation_WithNullSubject_VerifyErrorMessage() {
        // Real-world scenario: Verify user-friendly error message
        try {
            Validators.validation("john@example.com", null, "Body");
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Email subject is required. Please provide a subject for the email.",
                    e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidation_WithNullBody() {
        // Real-world scenario: Missing email body
        Validators.validation("john@example.com", "Subject", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidation_WithEmptyBody() {
        // Real-world scenario: Empty email body
        Validators.validation("john@example.com", "Subject", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidation_WithWhitespaceOnlyBody() {
        // Real-world scenario: Email body with only spaces
        Validators.validation("john@example.com", "Subject", "   ");
    }

    @Test
    public void testValidation_WithNullBody_VerifyErrorMessage() {
        // Real-world scenario: Verify user-friendly error message
        try {
            Validators.validation("john@example.com", "Subject", null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Email body is required. Please provide content for the email message.",
                    e.getMessage());
        }
    }

    @Test
    public void testValidation_WithLongEmailBody() {
        // Real-world scenario: Sending detailed email with long content
        String emailAddresses = "manager@example.com";
        String subject = "Quarterly Sales Analysis";
        StringBuilder longBody = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longBody.append("This is line ").append(i).append(" of the detailed sales report. ");
        }

        // Should not throw any exception
        Validators.validation(emailAddresses, subject, longBody.toString());
    }

    // ========== Tests for validateTimeZone() ==========

    @Test
    public void testValidateTimeZone_WithValidTimeZone_UTC() throws IOException {
        // Real-world scenario: Using UTC timezone
        Validators.validateTimeZone("UTC");
        // Should not throw any exception
    }

    @Test
    public void testValidateTimeZone_WithValidTimeZone_AmericaNewYork() throws IOException {
        // Real-world scenario: Using Eastern Time
        Validators.validateTimeZone("America/New_York");
        // Should not throw any exception
    }

    @Test
    public void testValidateTimeZone_WithValidTimeZone_AsiaTokyo() throws IOException {
        // Real-world scenario: Using Japan Standard Time
        Validators.validateTimeZone("Asia/Tokyo");
        // Should not throw any exception
    }

    @Test
    public void testValidateTimeZone_WithValidTimeZone_EuropeLondon() throws IOException {
        // Real-world scenario: Using British Time
        Validators.validateTimeZone("Europe/London");
        // Should not throw any exception
    }

    @Test
    public void testValidateTimeZone_WithNullTimeZone() throws IOException {
        // Real-world scenario: No timezone specified (should be allowed)
        Validators.validateTimeZone(null);
        // Should not throw any exception
    }

    @Test(expected = IOException.class)
    public void testValidateTimeZone_WithInvalidTimeZone() throws IOException {
        // Real-world scenario: Invalid timezone string
        Validators.validateTimeZone("Invalid/Timezone");
    }

    @Test(expected = IOException.class)
    public void testValidateTimeZone_WithEmptyString() throws IOException {
        // Real-world scenario: Empty timezone string
        Validators.validateTimeZone("XYZ/Invalid");
    }

    @Test
    public void testValidateTimeZone_WithInvalidTimeZone_VerifyErrorMessage() {
        // Real-world scenario: Verify user-friendly error message
        try {
            Validators.validateTimeZone("BadTimeZone");
            fail("Should have thrown IOException");
        } catch (IOException e) {
            assertEquals("The time zone you provided is not supported. Please select a valid time zone from the supported list and try again.",
                    e.getMessage());
        }
    }

    // ========== Tests for returnNullOrValue() ==========

    @Test
    public void testReturnNullOrValue_WithValidString() {
        // Real-world scenario: Processing non-empty field value
        String value = "Acme Corporation";
        String result = Validators.returnNullOrValue(value);
        assertEquals("Should return the value as-is", "Acme Corporation", result);
    }

    @Test
    public void testReturnNullOrValue_WithNull() {
        // Real-world scenario: Processing null field
        String result = Validators.returnNullOrValue(null);
        assertNull("Should return null when input is null", result);
    }

    @Test
    public void testReturnNullOrValue_WithEmptyString() {
        // Real-world scenario: Processing empty optional field
        String emptyString = "";
        String result = Validators.returnNullOrValue(emptyString);
        assertNull("Should return null when input is empty string", result);
    }

    @Test
    public void testReturnNullOrValue_WithWhitespaceString() {
        // Real-world scenario: Processing field with whitespace
        String whitespace = "   ";
        String result = Validators.returnNullOrValue(whitespace);
        assertEquals("Should return whitespace string as-is (not empty)", "   ", result);
    }

    @Test
    public void testReturnNullOrValue_WithSingleCharacter() {
        // Real-world scenario: Processing single character code
        String singleChar = "A";
        String result = Validators.returnNullOrValue(singleChar);
        assertEquals("Should return single character as-is", "A", result);
    }

    @Test
    public void testReturnNullOrValue_WithSpecialCharacters() {
        // Real-world scenario: Processing field with special characters
        String specialChars = "@#$%";
        String result = Validators.returnNullOrValue(specialChars);
        assertEquals("Should return special characters as-is", "@#$%", result);
    }
}

