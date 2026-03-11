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

package app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes;

import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Product;

import java.util.HashMap;
import java.util.Map;

/**
 * Converter between Product service implementation and Product catalog entity.
 * Provides bidirectional transformation methods.
 */
public final class ProductCatalogType {
    
    private ProductCatalogType() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Converts Product service interface to Product catalog entity.
     *
     * @param product Product service implementation
     * @return Product catalog entity
     */
    public static Product fromSFProduct(app.krista.extensions.crm.sales.salesforce_sales.service.Product product) {
        Product catalogProduct = new Product();
        catalogProduct.id = product.getId();
        catalogProduct.name = product.getName();
        catalogProduct.productCode = product.getProductCode();
        catalogProduct.description = product.getDescription();
        catalogProduct.isActive = product.getIsActive();
        catalogProduct.family = product.getFamily();
        catalogProduct.createdDate = product.getCreatedDate();
        catalogProduct.createdById = product.getCreatedById();
        catalogProduct.lastModifiedDate = product.getLastModifiedDate();
        catalogProduct.lastModifiedById = product.getLastModifiedById();
        return catalogProduct;
    }
    
    /**
     * Converts Product catalog entity to Map for API operations.
     *
     * @param product Product catalog entity
     * @return Map containing product field values
     */
    public static Map<String, Object> toMap(Product product) {
        Map<String, Object> productMap = new HashMap<>();
        if (product.id != null) productMap.put("Id", product.id);
        if (product.name != null) productMap.put("Name", product.name);
        if (product.productCode != null) productMap.put("Product Code", product.productCode);
        if (product.description != null) productMap.put("Description", product.description);
        if (product.isActive != null) productMap.put("Is Active", product.isActive);
        if (product.family != null) productMap.put("Family", product.family);
        if (product.createdDate != null) productMap.put("Created Date", product.createdDate);
        if (product.createdById != null) productMap.put("Created By Id", product.createdById);
        if (product.lastModifiedDate != null) productMap.put("Last Modified Date", product.lastModifiedDate);
        if (product.lastModifiedById != null) productMap.put("Last Modified By Id", product.lastModifiedById);
        return productMap;
    }
}

