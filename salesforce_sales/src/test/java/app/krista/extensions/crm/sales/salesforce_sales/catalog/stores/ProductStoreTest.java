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

package app.krista.extensions.crm.sales.salesforce_sales.catalog.stores;

import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Product;
import app.krista.extensions.crm.sales.salesforce_sales.impl.ProductDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.impl.ProductImpl;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFProduct;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ProductStore class.
 * Tests all EntityStore operations with mocked ProductDaoImpl.
 */
@RunWith(MockitoJUnitRunner.class)
public class ProductStoreTest {

    @Mock
    private ProductDaoImpl productDao;

    @InjectMocks
    private ProductStore productStore;

    private Map<String, Object> mockProductFields;
    private SFProduct mockSFProduct;
    private ProductImpl mockProductImpl;

    @Before
    public void setUp() {
        // Setup mock product fields
        mockProductFields = new HashMap<>();
        mockProductFields.put("Name", "Test Product");
        mockProductFields.put("Product Code", "PROD-001");
        mockProductFields.put("Description", "Test product description");
        mockProductFields.put("Is Active", true);
        mockProductFields.put("Family", "Hardware");

        // Setup mock SFProduct
        mockSFProduct = new SFProduct();
        mockSFProduct.id = "01txx000000001AAAQ";
        mockSFProduct.name = "Test Product";
        mockSFProduct.productCode = "PROD-001";
        mockSFProduct.description = "Test product description";
        mockSFProduct.isActive = true;
        mockSFProduct.family = "Hardware";

        // Setup mock ProductImpl
        mockProductImpl = new ProductImpl(mockSFProduct);
    }

    @Test
    public void testCreate_Success() {
        // Arrange
        String productId = "01txx000000001AAAQ";
        when(productDao.addProduct(mockProductFields)).thenReturn(productId);
        when(productDao.getProductById(productId)).thenReturn(mockProductImpl);

        // Act
        Product result = productStore.create(mockProductFields);

        // Assert
        assertNotNull("Product should not be null", result);
        assertEquals("Product ID should match", productId, result.id);
        verify(productDao, times(1)).addProduct(mockProductFields);
        verify(productDao, times(1)).getProductById(productId);
    }

    @Test
    public void testCreate_NullFields() {
        // Act
        Product result = productStore.create(null);

        // Assert - should return empty Product, not throw exception
        assertNotNull("Product should not be null", result);
        assertNull("Product ID should be null for empty product", result.id);
    }

    @Test
    public void testCreate_EmptyFields() {
        // Act
        Product result = productStore.create(new HashMap<>());

        // Assert - should return empty Product, not throw exception
        assertNotNull("Product should not be null", result);
        assertNull("Product ID should be null for empty product", result.id);
    }

    @Test
    public void testGet_Success() {
        // Arrange
        String productId = "01txx000000001AAAQ";
        when(productDao.getProductById(productId)).thenReturn(mockProductImpl);

        // Act
        Product result = productStore.get(productId);

        // Assert
        assertNotNull("Product should not be null", result);
        assertEquals("Product ID should match", productId, result.id);
        verify(productDao, times(1)).getProductById(productId);
    }

    @Test
    public void testGet_NotFound() {
        // Arrange
        String productId = "INVALID_ID";
        when(productDao.getProductById(productId)).thenThrow(new IllegalArgumentException("Product not found"));

        // Act
        Product result = productStore.get(productId);

        // Assert
        assertNull("Product should be null when not found", result);
    }

    @Test
    public void testGet_NullId() {
        // Act
        Product result = productStore.get(null);

        // Assert - should return null, not throw exception
        assertNull("Product should be null for null ID", result);
    }

    @Test
    public void testGet_EmptyId() {
        // Act
        Product result = productStore.get("");

        // Assert - should return null, not throw exception
        assertNull("Product should be null for empty ID", result);
    }

    @Test
    public void testUpdate_Success() {
        // Arrange
        Product product = new Product();
        product.id = "01txx000000001AAAQ";
        product.name = "Updated Product";

        doNothing().when(productDao).updateProduct(any(ProductImpl.class));
        when(productDao.getProductById("01txx000000001AAAQ")).thenReturn(mockProductImpl);

        // Act
        Product result = productStore.update(product);

        // Assert
        assertNotNull("Product should not be null", result);
        assertEquals("Product ID should match", product.id, result.id);
        verify(productDao, times(1)).updateProduct(any(ProductImpl.class));
    }

    @Test
    public void testUpdate_NullProduct() {
        // Act
        Product result = productStore.update(null);

        // Assert - should return null, not throw exception
        assertNull("Product should be null for null input", result);
    }

    @Test
    public void testDelete_Success() {
        // Arrange
        String productId = "01txx000000001AAAQ";
        doNothing().when(productDao).deleteProduct(productId);

        // Act
        productStore.delete(productId);

        // Assert
        verify(productDao, times(1)).deleteProduct(productId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDelete_NullId() {
        // Act
        productStore.delete(null);

        // Assert - exception expected
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDelete_EmptyId() {
        // Act
        productStore.delete("");

        // Assert - exception expected
    }

    @Test
    public void testSearch_DeprecatedMethod() {
        // Act
        List<Product> results = productStore.search(List.of(), 1, 10);

        // Assert
        assertNotNull("Results should not be null", results);
        assertTrue("Results should be empty for deprecated method", results.isEmpty());
    }

    @Test
    public void testContains() {
        // Act
        boolean result = productStore.contains("01txx000000001AAAQ");

        // Assert
        assertFalse("Contains should return false", result);
    }

    @Test
    public void testCount() {
        // Act
        long result = productStore.count(List.of());

        // Assert
        assertEquals("Count should return 0", 0, result);
    }

    @Test
    public void testLookup() {
        // Act
        List<String> result = productStore.lookup(new HashMap<>(), List.of());

        // Assert
        assertNotNull("Lookup should return empty list, not null", result);
        assertTrue("Lookup should return empty list", result.isEmpty());
    }
}
