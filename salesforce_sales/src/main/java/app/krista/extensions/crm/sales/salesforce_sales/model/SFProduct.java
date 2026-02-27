package app.krista.extensions.crm.sales.salesforce_sales.model;

import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Product;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.Map;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Validators.returnNullOrValue;

/**
 * Salesforce Product2 API response model.
 * Maps to the Product2 standard object in Salesforce.
 */
public final class SFProduct {
    
    @SerializedName("Id")
    public String id;
    
    @SerializedName("Name")
    public String name;
    
    @SerializedName("ProductCode")
    public String productCode;
    
    @SerializedName("Description")
    public String description;
    
    @SerializedName("IsActive")
    public Boolean isActive;
    
    @SerializedName("Family")
    public String family;
    
    @SerializedName("CreatedDate")
    public Date createdDate;
    
    @SerializedName("CreatedById")
    public String createdById;
    
    @SerializedName("LastModifiedDate")
    public Date lastModifiedDate;
    
    @SerializedName("LastModifiedById")
    public String lastModifiedById;
    
    /**
     * Converts a Map of entity attributes to SFProduct model.
     * Used for creating/updating products from catalog requests.
     * Note: CreatedDate, CreatedById, LastModifiedDate, LastModifiedById are read-only system fields
     * and should not be sent to Salesforce during create operations.
     *
     * @param entityAttributeField Map containing product field values
     * @return SFProduct instance populated with field values
     */
    public static SFProduct from(Map<String, Object> entityAttributeField) {
        SFProduct product = new SFProduct();
        product.name = returnNullOrValue((String) entityAttributeField.get("Name"));
        product.productCode = returnNullOrValue((String) entityAttributeField.get("Product Code"));
        product.description = returnNullOrValue((String) entityAttributeField.get("Description"));
        product.isActive = (Boolean) entityAttributeField.get("Is Active");
        product.family = returnNullOrValue((String) entityAttributeField.get("Family"));

        // Note: CreatedDate, CreatedById, LastModifiedDate, LastModifiedById are NOT set here
        // because they are read-only system fields managed by Salesforce

        return product;
    }
    
    /**
     * Converts a catalog Product entity to SFProduct model.
     * Used for update operations.
     *
     * @param product Catalog Product entity
     * @return SFProduct instance
     */
    public static SFProduct from(Product product) {
        SFProduct sfProduct = new SFProduct();
        sfProduct.id = product.id;
        sfProduct.name = product.name;
        sfProduct.productCode = product.productCode;
        sfProduct.description = product.description;
        sfProduct.isActive = product.isActive;
        sfProduct.family = product.family;
        
        if (product.createdDate != null)
            sfProduct.createdDate = new Date(product.createdDate);
        
        sfProduct.createdById = product.createdById;
        
        if (product.lastModifiedDate != null)
            sfProduct.lastModifiedDate = new Date(product.lastModifiedDate);
        
        sfProduct.lastModifiedById = product.lastModifiedById;
        
        return sfProduct;
    }
}

