package app.krista.extensions.crm.sales.salesforce_sales.impl;

import app.krista.extensions.crm.sales.salesforce_sales.model.SFPriceBook;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for PriceBookImpl class.
 * Tests price book implementation.
 */
public class PriceBookImplTest {

    private SFPriceBook sfPriceBook;
    private PriceBookImpl priceBook;

    @Before
    public void setUp() {
        sfPriceBook = new SFPriceBook();
    }

    @Test
    public void testConstructor() {
        // Arrange & Act
        priceBook = new PriceBookImpl(sfPriceBook);

        // Assert
        assertNotNull("PriceBook should not be null", priceBook);
    }

    @Test
    public void testGetId() {
        // Arrange
        sfPriceBook.Id = "01sxx000000001AAAQ";
        priceBook = new PriceBookImpl(sfPriceBook);

        // Act
        String result = priceBook.getId();

        // Assert
        assertEquals("ID should match", "01sxx000000001AAAQ", result);
    }

    @Test
    public void testGetId_WithNull() {
        // Arrange
        sfPriceBook.Id = null;
        priceBook = new PriceBookImpl(sfPriceBook);

        // Act
        String result = priceBook.getId();

        // Assert
        assertNull("ID should be null", result);
    }

    @Test
    public void testGetName() {
        // Arrange
        sfPriceBook.Name = "Standard Price Book";
        priceBook = new PriceBookImpl(sfPriceBook);

        // Act
        String result = priceBook.getName();

        // Assert
        assertEquals("Name should match", "Standard Price Book", result);
    }

    @Test
    public void testGetName_WithNull() {
        // Arrange
        sfPriceBook.Name = null;
        priceBook = new PriceBookImpl(sfPriceBook);

        // Act
        String result = priceBook.getName();

        // Assert
        assertNull("Name should be null", result);
    }

    @Test
    public void testGetName_WithEmptyString() {
        // Arrange
        sfPriceBook.Name = "";
        priceBook = new PriceBookImpl(sfPriceBook);

        // Act
        String result = priceBook.getName();

        // Assert
        assertEquals("Name should be empty string", "", result);
    }

    @Test
    public void testAllFields() {
        // Arrange
        sfPriceBook.Id = "01sxx000000001AAAQ";
        sfPriceBook.Name = "Enterprise Price Book";
        priceBook = new PriceBookImpl(sfPriceBook);

        // Assert
        assertEquals("ID should match", "01sxx000000001AAAQ", priceBook.getId());
        assertEquals("Name should match", "Enterprise Price Book", priceBook.getName());
    }

    @Test
    public void testAllFields_WithNullValues() {
        // Arrange
        sfPriceBook.Id = null;
        sfPriceBook.Name = null;
        priceBook = new PriceBookImpl(sfPriceBook);

        // Assert
        assertNull("ID should be null", priceBook.getId());
        assertNull("Name should be null", priceBook.getName());
    }

    @Test
    public void testGetId_WithLongId() {
        // Arrange
        sfPriceBook.Id = "01sxx000000001AAAQ123456789012345";
        priceBook = new PriceBookImpl(sfPriceBook);

        // Act
        String result = priceBook.getId();

        // Assert
        assertEquals("ID should match long ID", "01sxx000000001AAAQ123456789012345", result);
    }

    @Test
    public void testGetName_WithSpecialCharacters() {
        // Arrange
        sfPriceBook.Name = "Price Book - 2024 (Q1)";
        priceBook = new PriceBookImpl(sfPriceBook);

        // Act
        String result = priceBook.getName();

        // Assert
        assertEquals("Name should match with special characters", "Price Book - 2024 (Q1)", result);
    }

    @Test
    public void testGetName_WithUnicodeCharacters() {
        // Arrange
        sfPriceBook.Name = "价格表 2024";
        priceBook = new PriceBookImpl(sfPriceBook);

        // Act
        String result = priceBook.getName();

        // Assert
        assertEquals("Name should match with unicode characters", "价格表 2024", result);
    }
}

