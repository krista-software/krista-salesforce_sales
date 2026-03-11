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

