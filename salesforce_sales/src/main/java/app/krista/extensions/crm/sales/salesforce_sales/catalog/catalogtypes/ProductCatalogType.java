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

