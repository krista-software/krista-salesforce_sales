package app.krista.extensions.crm.sales.salesforce_sales.service;

import java.util.List;
import java.util.Map;

/**
 * Data Access Object interface for Product operations.
 * Defines CRUD operations for Salesforce Product2 object.
 */
public interface ProductDao {
    
    /**
     * Retrieves a product by its ID.
     *
     * @param productId Salesforce Product ID
     * @return Product instance or null if not found
     */
    Product getProductById(String productId);
    
    /**
     * Retrieves a product by its product code.
     *
     * @param productCode Product code
     * @return Product instance or null if not found
     */
    Product getProductByProductCode(String productCode);
    
    /**
     * Retrieves products by family.
     *
     * @param family Product family
     * @param size Number of records to retrieve
     * @param page Page number (1-based)
     * @return List of products
     */
    List<Product> getProductsByFamily(String family, long size, long page);
    
    /**
     * Retrieves active products.
     *
     * @param size Number of records to retrieve
     * @param page Page number (1-based)
     * @return List of active products
     */
    List<Product> getActiveProducts(long size, long page);
    
    /**
     * Retrieves all products with pagination.
     *
     * @param size Number of records to retrieve
     * @param page Page number (1-based)
     * @return List of products
     */
    List<Product> getProducts(long size, long page);
    
    /**
     * Creates a new product.
     *
     * @param entityAttributeField Map containing product field values
     * @return Created product ID
     */
    String addProduct(Map<String, Object> entityAttributeField);
    
    /**
     * Updates an existing product.
     *
     * @param product Product instance with updated values
     */
    void updateProduct(Product product);
    
    /**
     * Deletes a product by its ID.
     *
     * @param productId Salesforce Product ID
     */
    void deleteProduct(String productId);
}

