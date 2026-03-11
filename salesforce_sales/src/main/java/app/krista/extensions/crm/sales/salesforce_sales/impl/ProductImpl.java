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

