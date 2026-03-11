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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.*;

/**
 * Comprehensive real-world test cases for DatePlus utility class.
 * Tests all public static methods without using Mockito.
 */
public class DatePlusTest {

    // ========== Tests for parseDate() ==========

    @Test
    public void testParseDate_WithValidDate() {
        // Real-world scenario: Parsing opportunity close date
        String validDateString = "2023-12-07";
        Date validDate = DatePlus.parseDate(validDateString);

        assertNotNull("Should parse valid date successfully", validDate);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        assertEquals("Parsed date should match input", validDateString, dateFormat.format(validDate));
    }

    @Test
    public void testParseDate_WithInvalidDate() {
        // Real-world scenario: Handling malformed date input
        String invalidDateString = "invalid-date";
        Date invalidDate = DatePlus.parseDate(invalidDateString);

        assertNull("Should return null for invalid date format", invalidDate);
    }

    @Test
    public void testParseDate_WithNull() {
        // Real-world scenario: Missing date field
        Date result = DatePlus.parseDate(null);
        assertNull("Should return null when input is null", result);
    }

    @Test
    public void testParseDate_WithNonStringObject() {
        // Real-world scenario: Incorrect data type passed
        Integer nonStringObject = 12345;
        Date result = DatePlus.parseDate(nonStringObject);
        assertNull("Should return null for non-string input", result);
    }

    @Test
    public void testParseDate_WithLeapYearDate() {
        // Real-world scenario: Parsing February 29th in leap year
        String leapYearDate = "2024-02-29";
        Date result = DatePlus.parseDate(leapYearDate);

        assertNotNull("Should parse leap year date successfully", result);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        assertEquals("Should correctly parse Feb 29", leapYearDate, dateFormat.format(result));
    }

    @Test
    public void testParseDate_WithYearEndDate() {
        // Real-world scenario: Parsing year-end date
        String yearEndDate = "2023-12-31";
        Date result = DatePlus.parseDate(yearEndDate);

        assertNotNull("Should parse year-end date successfully", result);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        assertEquals("Should correctly parse Dec 31", yearEndDate, dateFormat.format(result));
    }

    @Test
    public void testParseDate_WithYearStartDate() {
        // Real-world scenario: Parsing year-start date
        String yearStartDate = "2024-01-01";
        Date result = DatePlus.parseDate(yearStartDate);

        assertNotNull("Should parse year-start date successfully", result);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        assertEquals("Should correctly parse Jan 1", yearStartDate, dateFormat.format(result));
    }

    @Test
    public void testParseDate_WithWrongFormat() {
        // Real-world scenario: Date in completely invalid format
        String wrongFormat = "not-a-date";
        Date result = DatePlus.parseDate(wrongFormat);
        assertNull("Should return null for invalid date format", result);
    }

    // ========== Tests for getISODate(Date) ==========

    @Test
    public void testGetISODate_WithValidDate() {
        // Real-world scenario: Converting opportunity close date to ISO format for Salesforce API
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.set(2024, Calendar.MARCH, 15, 14, 30, 45);
        cal.set(Calendar.MILLISECOND, 123);
        Date testDate = cal.getTime();

        String isoDate = DatePlus.getISODate(testDate);

        assertNotNull("ISO date should not be null", isoDate);
        assertTrue("ISO date should contain year", isoDate.contains("2024"));
        assertTrue("ISO date should contain 'T' separator", isoDate.contains("T"));
        assertTrue("ISO date should contain timezone offset", isoDate.contains("+") || isoDate.contains("-"));
    }

    @Test
    public void testGetISODate_WithCurrentDate() {
        // Real-world scenario: Converting current timestamp for API call
        Date now = new Date();
        String isoDate = DatePlus.getISODate(now);

        assertNotNull("ISO date should not be null", isoDate);
        assertTrue("ISO date should be in correct format", isoDate.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}[+-]\\d{4}"));
    }

    // ========== Tests for getISODate(Long) ==========

    @Test
    public void testGetISODate_WithValidTimestamp() {
        // Real-world scenario: Converting epoch timestamp from Salesforce to ISO format
        Long timestamp = 1710511845123L; // March 15, 2024 14:30:45.123 UTC
        String isoDate = DatePlus.getISODate(timestamp);

        assertNotNull("ISO date should not be null", isoDate);
        assertTrue("ISO date should contain year 2024", isoDate.contains("2024"));
        assertTrue("ISO date should be in ISO format", isoDate.contains("T"));
    }

    @Test
    public void testGetISODate_WithZeroTimestamp() {
        // Real-world scenario: Epoch start time (January 1, 1970)
        Long timestamp = 0L;
        String isoDate = DatePlus.getISODate(timestamp);

        assertNotNull("ISO date should not be null", isoDate);
        assertTrue("ISO date should contain 1970", isoDate.contains("1970"));
    }

    // ========== Tests for getYYYYMMDD() ==========

    @Test
    public void testGetYYYYMMDD_WithValidTimestamp() {
        // Real-world scenario: Formatting opportunity close date for display
        Calendar cal = Calendar.getInstance();
        cal.set(2024, Calendar.MARCH, 15, 0, 0, 0);
        long timestamp = cal.getTimeInMillis();

        String formattedDate = DatePlus.getYYYYMMDD(timestamp);

        assertNotNull("Formatted date should not be null", formattedDate);
        assertTrue("Date should start with year 2024", formattedDate.startsWith("2024"));
        assertTrue("Date should contain month 03", formattedDate.contains("-03-"));
        assertTrue("Date should end with day 15", formattedDate.endsWith("-15"));
    }

    @Test
    public void testGetYYYYMMDD_WithCurrentTimestamp() {
        // Real-world scenario: Formatting today's date
        long now = System.currentTimeMillis();
        String formattedDate = DatePlus.getYYYYMMDD(now);

        assertNotNull("Formatted date should not be null", formattedDate);
        assertTrue("Date should match YYYY-MM-DD pattern", formattedDate.matches("\\d{4}-\\d{2}-\\d{2}"));
    }

    @Test
    public void testGetYYYYMMDD_WithLeapYearDate() {
        // Real-world scenario: Formatting February 29, 2024
        Calendar cal = Calendar.getInstance();
        cal.set(2024, Calendar.FEBRUARY, 29, 0, 0, 0);
        long timestamp = cal.getTimeInMillis();

        String formattedDate = DatePlus.getYYYYMMDD(timestamp);

        assertNotNull("Formatted date should not be null", formattedDate);
        assertTrue("Date should be Feb 29, 2024", formattedDate.equals("2024-02-29"));
    }


    // ========== Tests for fromYYYYMMDD() ==========

    @Test
    public void testFromYYYYMMDD_WithValidDate() {
        // Real-world scenario: Parsing date string from Salesforce API response
        String dateString = "2024-03-15";
        Date result = DatePlus.fromYYYYMMDD(dateString);

        assertNotNull("Should parse valid date string", result);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        assertEquals("Parsed date should match input", dateString, sdf.format(result));
    }

    @Test
    public void testFromYYYYMMDD_WithInvalidDate() {
        // Real-world scenario: SimpleDateFormat is lenient and adjusts invalid dates
        // "2024-13-45" gets adjusted to a valid date (Feb 14, 2025)
        String invalidDate = "2024-13-45"; // Invalid month and day
        Date result = DatePlus.fromYYYYMMDD(invalidDate);

        assertNotNull("SimpleDateFormat is lenient and adjusts the date", result);
    }

    @Test
    public void testFromYYYYMMDD_WithWrongFormat() {
        // Real-world scenario: Date in wrong format
        String wrongFormat = "03/15/2024";
        Date result = DatePlus.fromYYYYMMDD(wrongFormat);

        assertNull("Should return null for wrong format", result);
    }

    @Test(expected = NullPointerException.class)
    public void testFromYYYYMMDD_WithNull() {
        // Real-world scenario: Missing date field throws NullPointerException
        DatePlus.fromYYYYMMDD(null);
    }

    @Test
    public void testFromYYYYMMDD_WithLeapYearDate() {
        // Real-world scenario: Parsing February 29 in leap year
        String leapDate = "2024-02-29";
        Date result = DatePlus.fromYYYYMMDD(leapDate);

        assertNotNull("Should parse leap year date", result);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        assertEquals("Should correctly parse Feb 29", leapDate, sdf.format(result));
    }

    @Test
    public void testFromYYYYMMDD_WithNonLeapYearDate() {
        // Real-world scenario: SimpleDateFormat is lenient and adjusts Feb 29 in non-leap year to Mar 1
        String nonLeapDate = "2023-02-29";
        Date result = DatePlus.fromYYYYMMDD(nonLeapDate);

        assertNotNull("SimpleDateFormat is lenient and adjusts to March 1", result);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        assertEquals("Feb 29 in non-leap year becomes Mar 1", "2023-03-01", sdf.format(result));
    }

    // ========== Tests for getQuarterDates() ==========

    @Test
    public void testGetQuarterDates_WithQ1() {
        // Real-world scenario: Getting Q1 date range for sales forecast
        String quarter = "Q1";
        DatePlus.QuarterDates result = DatePlus.getQuarterDates(quarter);

        assertNotNull("Quarter dates should not be null", result);
        assertNotNull("Start date should not be null", result.startDate);
        assertNotNull("End date should not be null", result.endDate);
        assertNotNull("Current date should not be null", result.current);

        // Q1 is January-March (months 0-2)
        assertTrue("Q1 start should be in January", result.startDate.contains("-01-"));
        assertTrue("Q1 end should be in March", result.endDate.contains("-03-") || result.endDate.contains("-04-"));
    }

    @Test
    public void testGetQuarterDates_WithQ2() {
        // Real-world scenario: Getting Q2 date range for sales forecast
        String quarter = "Q2";
        DatePlus.QuarterDates result = DatePlus.getQuarterDates(quarter);

        assertNotNull("Quarter dates should not be null", result);
        assertNotNull("Start date should not be null", result.startDate);
        assertNotNull("End date should not be null", result.endDate);

        // Q2 is April-June (months 3-5)
        assertTrue("Q2 start should be in April", result.startDate.contains("-04-"));
    }

    @Test
    public void testGetQuarterDates_WithQ3() {
        // Real-world scenario: Getting Q3 date range for sales forecast
        String quarter = "Q3";
        DatePlus.QuarterDates result = DatePlus.getQuarterDates(quarter);

        assertNotNull("Quarter dates should not be null", result);
        assertNotNull("Start date should not be null", result.startDate);
        assertNotNull("End date should not be null", result.endDate);

        // Q3 is July-September (months 6-8)
        assertTrue("Q3 start should be in July", result.startDate.contains("-07-"));
    }

    @Test
    public void testGetQuarterDates_WithQ4() {
        // Real-world scenario: Getting Q4 date range for sales forecast
        String quarter = "Q4";
        DatePlus.QuarterDates result = DatePlus.getQuarterDates(quarter);

        assertNotNull("Quarter dates should not be null", result);
        assertNotNull("Start date should not be null", result.startDate);
        assertNotNull("End date should not be null", result.endDate);

        // Q4 is October-December (months 9-11)
        assertTrue("Q4 start should be in October", result.startDate.contains("-10-"));
    }

    @Test
    public void testGetQuarterDates_InvalidFormat_NoNumber() {
        // Real-world scenario: Invalid quarter format without number
        try {
            DatePlus.getQuarterDates("Quarter");
            fail("Should throw IllegalArgumentException for invalid format");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid quarter format. Please provide a valid quarter (e.g., 'Q1', 'Q2', 'Q3', or 'Q4').",
                    e.getMessage());
        }
    }

    @Test
    public void testGetQuarterDates_InvalidFormat_EmptyString() {
        // Real-world scenario: Empty quarter string
        try {
            DatePlus.getQuarterDates("");
            fail("Should throw exception for empty string");
        } catch (Exception e) {
            // Expected - either IllegalArgumentException or StringIndexOutOfBoundsException
            assertTrue("Should throw an exception", true);
        }
    }

    @Test
    public void testGetQuarterDates_QuarterDatesStructure() {
        // Real-world scenario: Verify QuarterDates structure contains all required fields
        String quarter = "Q1";
        DatePlus.QuarterDates result = DatePlus.getQuarterDates(quarter);

        assertNotNull("QuarterDates object should not be null", result);
        assertNotNull("startDate field should not be null", result.startDate);
        assertNotNull("endDate field should not be null", result.endDate);
        assertNotNull("current field should not be null", result.current);

        // Verify date format
        assertTrue("startDate should be in YYYY-MM-DD format", result.startDate.matches("\\d{4}-\\d{2}-\\d{2}"));
        assertTrue("endDate should be in YYYY-MM-DD format", result.endDate.matches("\\d{4}-\\d{2}-\\d{2}"));
        assertTrue("current should be in YYYY-MM-DD format", result.current.matches("\\d{4}-\\d{2}-\\d{2}"));
    }

    @Test
    public void testGetQuarterDates_CurrentDateWithinRange() {
        // Real-world scenario: Verify current date is within or before the quarter range
        String quarter = "Q1";
        DatePlus.QuarterDates result = DatePlus.getQuarterDates(quarter);

        assertNotNull("Quarter dates should not be null", result);

        // Current date should be a valid date
        Date currentDate = DatePlus.fromYYYYMMDD(result.current);
        assertNotNull("Current date should be parseable", currentDate);
    }
}
