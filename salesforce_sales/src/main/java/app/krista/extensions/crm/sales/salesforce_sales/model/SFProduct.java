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

