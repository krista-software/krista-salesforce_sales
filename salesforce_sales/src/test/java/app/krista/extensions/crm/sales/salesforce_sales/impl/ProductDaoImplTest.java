package app.krista.extensions.crm.sales.salesforce_sales.impl;

import app.krista.extensions.crm.sales.salesforce_sales.model.SFProduct;
import app.krista.extensions.crm.sales.salesforce_sales.service.Product;
import app.krista.extensions.crm.sales.salesforce_sales.util.SalesforceListResponse;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ProductDaoImpl class.
 * Tests all CRUD operations and query methods.
 */
public class ProductDaoImplTest {

    private static final Gson GSON = new Gson();

    @Mock
    private SalesforceManager salesforceManager;

    private ProductDaoImpl productDao;
    private SFProduct testProduct;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        productDao = new ProductDaoImpl(salesforceManager);

        testProduct = new SFProduct();
        testProduct.id = "01t5g000001ABCD";
        testProduct.name = "Test Product";
        testProduct.productCode = "PROD-001";
        testProduct.description = "Test product description";
        testProduct.isActive = true;
        testProduct.family = "Electronics";
        testProduct.createdDate = new Date(1609459200000L);
        testProduct.createdById = "0055g000001USER";
        testProduct.lastModifiedDate = new Date(1640995200000L);
        testProduct.lastModifiedById = "0055g000001USER";
    }

    @Test
    public void testGetProductById() {
        when(salesforceManager.getRecordById(anyString(), eq("Product2")))
                .thenReturn(GSON.toJson(testProduct));

        Product product = productDao.getProductById("01t5g000001ABCD");

        assertNotNull(product);
        assertEquals("01t5g000001ABCD", product.getId());
        assertEquals("Test Product", product.getName());
        assertEquals("PROD-001", product.getProductCode());
        verify(salesforceManager, times(1)).getRecordById("01t5g000001ABCD", "Product2");
    }

    @Test
    public void testGetProductByIdNotFound() {
        when(salesforceManager.getRecordById(anyString(), eq("Product2")))
                .thenReturn(null);

        Product product = productDao.getProductById("INVALID_ID");

        assertNull(product);
        verify(salesforceManager, times(1)).getRecordById("INVALID_ID", "Product2");
    }

    @Test
    public void testGetProductByProductCode() {
        SalesforceListResponse<SFProduct> response = new SalesforceListResponse<>();
        response.records = Collections.singletonList(testProduct);

        when(salesforceManager.getRecordByQuery(anyString()))
                .thenReturn(GSON.toJson(response));

        Product product = productDao.getProductByProductCode("PROD-001");

        assertNotNull(product);
        assertEquals("PROD-001", product.getProductCode());
        verify(salesforceManager, times(1)).getRecordByQuery(anyString());
    }

    @Test
    public void testGetProductByProductCodeNotFound() {
        SalesforceListResponse<SFProduct> response = new SalesforceListResponse<>();
        response.records = Collections.emptyList();

        when(salesforceManager.getRecordByQuery(anyString()))
                .thenReturn(GSON.toJson(response));

        Product product = productDao.getProductByProductCode("INVALID_CODE");

        assertNull(product);
        verify(salesforceManager, times(1)).getRecordByQuery(anyString());
    }

    @Test
    public void testGetProductsByFamily() {
        SalesforceListResponse<SFProduct> response = new SalesforceListResponse<>();
        response.records = Arrays.asList(testProduct);

        when(salesforceManager.getRecordByQuery(anyString()))
                .thenReturn(GSON.toJson(response));

        List<Product> products = productDao.getProductsByFamily("Electronics", 10, 1);

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("Electronics", products.get(0).getFamily());
        verify(salesforceManager, times(1)).getRecordByQuery(anyString());
    }

    @Test
    public void testGetActiveProducts() {
        SalesforceListResponse<SFProduct> response = new SalesforceListResponse<>();
        response.records = Arrays.asList(testProduct);

        when(salesforceManager.getRecordByQuery(anyString()))
                .thenReturn(GSON.toJson(response));

        List<Product> products = productDao.getActiveProducts(10, 1);

        assertNotNull(products);
        assertEquals(1, products.size());
        assertTrue(products.get(0).getIsActive());
        verify(salesforceManager, times(1)).getRecordByQuery(anyString());
    }

    @Test
    public void testGetProducts() {
        SalesforceListResponse<SFProduct> response = new SalesforceListResponse<>();
        response.records = Arrays.asList(testProduct);

        when(salesforceManager.getRecordByQuery(anyString()))
                .thenReturn(GSON.toJson(response));

        List<Product> products = productDao.getProducts(10, 1);

        assertNotNull(products);
        assertEquals(1, products.size());
        verify(salesforceManager, times(1)).getRecordByQuery(anyString());
    }

    @Test
    public void testAddProduct() {
        Map<String, Object> productFields = new HashMap<>();
        productFields.put("Name", "New Product");
        productFields.put("Product Code", "PROD-002");
        productFields.put("Description", "New product description");
        productFields.put("Is Active", true);
        productFields.put("Family", "Software");

        Map<String, Object> createResponse = new HashMap<>();
        createResponse.put("id", "01t5g000001NEWID");
        createResponse.put("success", true);

        when(salesforceManager.createRecord(anyString(), eq("Product2")))
                .thenReturn(GSON.toJson(createResponse));

        String productId = productDao.addProduct(productFields);

        assertNotNull(productId);
        assertEquals("01t5g000001NEWID", productId);
        verify(salesforceManager, times(1)).createRecord(anyString(), eq("Product2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddProductNullFields() {
        productDao.addProduct(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddProductEmptyFields() {
        productDao.addProduct(new HashMap<>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddProductMissingName() {
        Map<String, Object> productFields = new HashMap<>();
        productFields.put("Product Code", "PROD-002");
        productDao.addProduct(productFields);
    }

    @Test
    public void testUpdateProduct() {
        when(salesforceManager.getRecordById(anyString(), eq("Product2")))
                .thenReturn(GSON.toJson(testProduct));

        Map<String, Object> difference = new HashMap<>();
        difference.put("Name", "Updated Product");

        when(salesforceManager.getDifference(any(), any()))
                .thenReturn(difference);

        when(salesforceManager.updateRecord(anyString(), anyString(), eq("Product2")))
                .thenReturn("{}");

        Product product = productDao.getProductById("01t5g000001ABCD");
        productDao.updateProduct(product);

        verify(salesforceManager, times(1)).updateRecord(anyString(), anyString(), eq("Product2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateProductNullId() {
        testProduct.id = null;
        Product product = new ProductImpl(testProduct);
        productDao.updateProduct(product);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateProductEmptyId() {
        testProduct.id = "";
        Product product = new ProductImpl(testProduct);
        productDao.updateProduct(product);
    }

    @Test
    public void testDeleteProduct() {
        doNothing().when(salesforceManager).deleteRecordById(anyString(), eq("Product2"));

        productDao.deleteProduct("01t5g000001ABCD");

        verify(salesforceManager, times(1)).deleteRecordById("01t5g000001ABCD", "Product2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteProductNullId() {
        productDao.deleteProduct(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteProductEmptyId() {
        productDao.deleteProduct("");
    }
}

