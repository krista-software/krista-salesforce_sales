package app.krista.extensions.crm.sales.salesforce_sales.impl;

import app.krista.extensions.crm.sales.salesforce_sales.model.SFProduct;
import app.krista.extensions.crm.sales.salesforce_sales.service.Product;

/**
 * Implementation of Product service interface.
 * Wraps SFProduct model and provides getter methods.
 */
public class ProductImpl implements Product {
    
    private final SFProduct sfProduct;
    
    public ProductImpl(SFProduct sfProduct) {
        this.sfProduct = sfProduct;
    }
    
    @Override
    public String getId() {
        return sfProduct.id;
    }
    
    @Override
    public String getName() {
        return sfProduct.name;
    }
    
    @Override
    public String getProductCode() {
        return sfProduct.productCode;
    }
    
    @Override
    public String getDescription() {
        return sfProduct.description;
    }
    
    @Override
    public Boolean getIsActive() {
        return sfProduct.isActive;
    }
    
    @Override
    public String getFamily() {
        return sfProduct.family;
    }
    
    @Override
    public Long getCreatedDate() {
        return sfProduct.createdDate != null ? sfProduct.createdDate.getTime() : null;
    }
    
    @Override
    public String getCreatedById() {
        return sfProduct.createdById;
    }
    
    @Override
    public Long getLastModifiedDate() {
        return sfProduct.lastModifiedDate != null ? sfProduct.lastModifiedDate.getTime() : null;
    }
    
    @Override
    public String getLastModifiedById() {
        return sfProduct.lastModifiedById;
    }
}

