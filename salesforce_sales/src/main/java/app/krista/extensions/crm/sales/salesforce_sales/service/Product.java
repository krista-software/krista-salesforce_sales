package app.krista.extensions.crm.sales.salesforce_sales.service;

/**
 * Service interface for Product operations.
 * Provides getter methods for all Product fields.
 */
public interface Product {
    
    /**
     * Gets the unique identifier for the product.
     *
     * @return Product ID
     */
    String getId();
    
    /**
     * Gets the product name.
     *
     * @return Product name
     */
    String getName();
    
    /**
     * Gets the product code.
     *
     * @return Product code
     */
    String getProductCode();
    
    /**
     * Gets the product description.
     *
     * @return Product description
     */
    String getDescription();
    
    /**
     * Gets the active status of the product.
     *
     * @return True if product is active, false otherwise
     */
    Boolean getIsActive();
    
    /**
     * Gets the product family.
     *
     * @return Product family
     */
    String getFamily();
    
    /**
     * Gets the creation date of the product.
     *
     * @return Created date as epoch milliseconds
     */
    Long getCreatedDate();
    
    /**
     * Gets the ID of the user who created the product.
     *
     * @return Created by user ID
     */
    String getCreatedById();
    
    /**
     * Gets the last modified date of the product.
     *
     * @return Last modified date as epoch milliseconds
     */
    Long getLastModifiedDate();
    
    /**
     * Gets the ID of the user who last modified the product.
     *
     * @return Last modified by user ID
     */
    String getLastModifiedById();
}

