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

package app.krista.extensions.crm.sales.salesforce_sales.impl;

import app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes.ProductCatalogType;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFProduct;
import app.krista.extensions.crm.sales.salesforce_sales.service.Product;
import app.krista.extensions.crm.sales.salesforce_sales.service.ProductDao;
import app.krista.extensions.crm.sales.salesforce_sales.util.SalesforceListResponse;
import app.krista.extensions.crm.sales.salesforce_sales.util.SFHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of ProductDao interface.
 * Handles all Product2 CRUD operations with Salesforce.
 */
@Service
public class ProductDaoImpl implements ProductDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductDaoImpl.class);
    private static final Gson GSON = new Gson();
    private static final String PRODUCT = "Product2";

    // Define all fields to query from Salesforce Product2 object
    public static final String QUERY_FIELDS = "Id, Name, ProductCode, Description, IsActive, Family, " +
            "CreatedDate, CreatedById, LastModifiedDate, LastModifiedById";

    private final SalesforceManager salesforceManager;

    @Inject
    public ProductDaoImpl(SalesforceManager salesforceManager) {
        this.salesforceManager = salesforceManager;
    }

    @Override
    public Product getProductById(String productId) {
        String response = salesforceManager.getRecordById(productId, PRODUCT);
        if (response == null || response.isEmpty()) {
            LOGGER.warn("No product found with ID: {}", productId);
            return null;
        }
        SFProduct sfProduct = GSON.fromJson(response, SFProduct.class);
        return new ProductImpl(sfProduct);
    }

    @Override
    public Product getProductByProductCode(String productCode) {
        String query = String.format("SELECT %s FROM Product2 WHERE ProductCode = '%s' LIMIT 1",
                QUERY_FIELDS, SFHelper.soqlEscape(productCode));
        List<Product> products = getProductsByQuery(query);
        return products.isEmpty() ? null : products.get(0);
    }

    @Override
    public List<Product> getProductsByFamily(String family, long size, long page) {
        String query = String.format("SELECT %s FROM Product2 WHERE Family = '%s' ORDER BY Name ASC LIMIT %d OFFSET %d",
                QUERY_FIELDS, SFHelper.soqlEscape(family), size, Math.max((page - 1) * size, 0));
        return getProductsByQuery(query);
    }

    @Override
    public List<Product> getActiveProducts(long size, long page) {
        String query = String.format("SELECT %s FROM Product2 WHERE IsActive = true ORDER BY Name ASC LIMIT %d OFFSET %d",
                QUERY_FIELDS, size, Math.max((page - 1) * size, 0));
        return getProductsByQuery(query);
    }

    @Override
    public List<Product> getProducts(long size, long page) {
        String query = String.format("SELECT %s FROM Product2 ORDER BY Name ASC LIMIT %d OFFSET %d",
                QUERY_FIELDS, size, Math.max((page - 1) * size, 0));
        return getProductsByQuery(query);
    }

    @Override
    public String addProduct(Map<String, Object> entityAttributeField) {
        try {
            if (entityAttributeField == null || entityAttributeField.isEmpty()) {
                throw new IllegalArgumentException("Product fields cannot be null or empty");
            }

            // Validate required fields
            if (!entityAttributeField.containsKey("Name") || entityAttributeField.get("Name") == null) {
                throw new IllegalArgumentException("Name is required to create a product");
            }

            SFProduct product = SFProduct.from(entityAttributeField);
            String response = salesforceManager.createRecord(GSON.toJson(product), PRODUCT);
            Map<String, Object> responseMap = GSON.fromJson(response, Map.class);
            String productId = String.valueOf(responseMap.get("id"));

            LOGGER.info("Successfully created product with ID: {}", productId);
            return productId;
        } catch (IllegalArgumentException e) {
            LOGGER.error("Validation error creating product: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error creating product: {}", e.getMessage(), e);
            throw new IllegalArgumentException("Failed to create product: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateProduct(Product product) {
        try {
            if (product.getId() == null || product.getId().isEmpty()) {
                throw new IllegalArgumentException("Product ID is required for update");
            }

            // Get current product state
            SFProduct updatedProduct = SFProduct.from(ProductCatalogType.fromSFProduct(product));
            SFProduct oldProduct = SFProduct.from(ProductCatalogType.fromSFProduct(getProductById(updatedProduct.id)));

            // Calculate difference to send only changed fields
            Map<String, Object> difference = salesforceManager.getDifference(oldProduct, updatedProduct);

            if (difference.isEmpty()) {
                LOGGER.info("No changes detected for product ID: {}", updatedProduct.id);
                return;
            }

            salesforceManager.updateRecord(updatedProduct.id, GSON.toJson(difference), PRODUCT);
            LOGGER.info("Successfully updated product with ID: {}", updatedProduct.id);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Validation error updating product: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error updating product: {}", e.getMessage(), e);
            throw new IllegalArgumentException("Failed to update product: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteProduct(String productId) {
        try {
            if (productId == null || productId.isEmpty()) {
                throw new IllegalArgumentException("Product ID is required for deletion");
            }

            salesforceManager.deleteRecordById(productId, PRODUCT);
            LOGGER.info("Successfully deleted product with ID: {}", productId);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Validation error deleting product: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error deleting product: {}", e.getMessage(), e);
            throw new IllegalArgumentException("Failed to delete product: " + e.getMessage(), e);
        }
    }

    /**
     * Helper method to execute SOQL query and return list of products.
     *
     * @param query SOQL query string
     * @return List of products
     */
    @Override
    public List<Product> getProductsByQuery(String query) {
        String response = salesforceManager.getRecordByQuery(query);
        SalesforceListResponse<SFProduct> listResponse = GSON.fromJson(response,
                new TypeToken<SalesforceListResponse<SFProduct>>() {}.getType());

        if (listResponse == null || listResponse.records == null) {
            return List.of();
        }
        return listResponse.records.stream()
                .map(ProductImpl::new)
                .collect(Collectors.toList());
    }
}

