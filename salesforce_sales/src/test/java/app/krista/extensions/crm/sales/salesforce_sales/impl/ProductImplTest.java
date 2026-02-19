package app.krista.extensions.crm.sales.salesforce_sales.impl;

import app.krista.extensions.crm.sales.salesforce_sales.model.SFProduct;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Unit tests for ProductImpl class.
 * Tests all getter methods and field mappings.
 */
public class ProductImplTest {
    
    private SFProduct sfProduct;
    private ProductImpl productImpl;
    
    @Before
    public void setUp() {
        sfProduct = new SFProduct();
        sfProduct.id = "01t5g000001ABCD";
        sfProduct.name = "Test Product";
        sfProduct.productCode = "PROD-001";
        sfProduct.description = "Test product description";
        sfProduct.isActive = true;
        sfProduct.family = "Electronics";
        sfProduct.createdDate = new Date(1609459200000L); // 2021-01-01
        sfProduct.createdById = "0055g000001USER";
        sfProduct.lastModifiedDate = new Date(1640995200000L); // 2022-01-01
        sfProduct.lastModifiedById = "0055g000001USER";
        
        productImpl = new ProductImpl(sfProduct);
    }
    
    @Test
    public void testGetId() {
        assertEquals("01t5g000001ABCD", productImpl.getId());
    }
    
    @Test
    public void testGetName() {
        assertEquals("Test Product", productImpl.getName());
    }
    
    @Test
    public void testGetProductCode() {
        assertEquals("PROD-001", productImpl.getProductCode());
    }
    
    @Test
    public void testGetDescription() {
        assertEquals("Test product description", productImpl.getDescription());
    }
    
    @Test
    public void testGetIsActive() {
        assertTrue(productImpl.getIsActive());
    }
    
    @Test
    public void testGetFamily() {
        assertEquals("Electronics", productImpl.getFamily());
    }
    
    @Test
    public void testGetCreatedDate() {
        assertEquals(Long.valueOf(1609459200000L), productImpl.getCreatedDate());
    }
    
    @Test
    public void testGetCreatedById() {
        assertEquals("0055g000001USER", productImpl.getCreatedById());
    }
    
    @Test
    public void testGetLastModifiedDate() {
        assertEquals(Long.valueOf(1640995200000L), productImpl.getLastModifiedDate());
    }
    
    @Test
    public void testGetLastModifiedById() {
        assertEquals("0055g000001USER", productImpl.getLastModifiedById());
    }
    
    @Test
    public void testGetIdNull() {
        sfProduct.id = null;
        productImpl = new ProductImpl(sfProduct);
        assertNull(productImpl.getId());
    }
    
    @Test
    public void testGetNameNull() {
        sfProduct.name = null;
        productImpl = new ProductImpl(sfProduct);
        assertNull(productImpl.getName());
    }
    
    @Test
    public void testGetProductCodeNull() {
        sfProduct.productCode = null;
        productImpl = new ProductImpl(sfProduct);
        assertNull(productImpl.getProductCode());
    }
    
    @Test
    public void testGetDescriptionNull() {
        sfProduct.description = null;
        productImpl = new ProductImpl(sfProduct);
        assertNull(productImpl.getDescription());
    }
    
    @Test
    public void testGetIsActiveNull() {
        sfProduct.isActive = null;
        productImpl = new ProductImpl(sfProduct);
        assertNull(productImpl.getIsActive());
    }
    
    @Test
    public void testGetFamilyNull() {
        sfProduct.family = null;
        productImpl = new ProductImpl(sfProduct);
        assertNull(productImpl.getFamily());
    }
    
    @Test
    public void testGetCreatedDateNull() {
        sfProduct.createdDate = null;
        productImpl = new ProductImpl(sfProduct);
        assertNull(productImpl.getCreatedDate());
    }
    
    @Test
    public void testGetCreatedByIdNull() {
        sfProduct.createdById = null;
        productImpl = new ProductImpl(sfProduct);
        assertNull(productImpl.getCreatedById());
    }
    
    @Test
    public void testGetLastModifiedDateNull() {
        sfProduct.lastModifiedDate = null;
        productImpl = new ProductImpl(sfProduct);
        assertNull(productImpl.getLastModifiedDate());
    }
    
    @Test
    public void testGetLastModifiedByIdNull() {
        sfProduct.lastModifiedById = null;
        productImpl = new ProductImpl(sfProduct);
        assertNull(productImpl.getLastModifiedById());
    }
}

