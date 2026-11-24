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

package app.krista.extensions.crm.sales.salesforce_sales.catalog.entities;

import app.krista.extension.impl.anno.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Catalog entity for Salesforce Product (Product2).
 * Represents a product that the company sells.
 */
@Domain(id = "catEntryDomain_086f33da-aaea-4d0c-816f-3d5f20b4a273",
        name = "Sales",
        ecosystemId = "catEntryEcosystem_8a0e8ce7-6289-492f-9202-52284024280c",
        ecosystemName = "CRM",
        ecosystemVersion = "db320c64-6f6f-4c9e-8daa-41d3f796d6a6")
@Entity(name = "Product", id = "localDomainEntity_c3d4e5f6-7a8b-9c0d-1e2f-3a4b5c6d7e8f", primaryKey = "Id", supportStore = true,
        description = "Represents a product in the Salesforce product catalog (Product2). Referenced by OrderItem.Product Id and PricebookEntry.Product Id. Created By Id and Last Modified By Id reference User.Id.")
@Searchable
public class Product {

    @Field(name = "Id", type = "Text", required = false, description = "Unique Salesforce identifier for the product. Referenced by OrderItem.Product Id and PricebookEntry.Product Id.")
    public String id;

    @Field(name = "Name", type = "Text", required = true, description = "Name of the product.")
    @ToString
    public String name;

    @Field(name = "Product Code", type = "Text", required = true, description = "Unique product code identifier.")
    @Searchable
    public String productCode;

    @Field(name = "Description", type = "Text", required = false, description = "Description of the product.")
    public String description;

    @Field.Boolean(name = "Is Active", required = false, description = "Whether the product is currently active.")
    public Boolean isActive;

    @Field(name = "Family", type = "Text", required = false, description = "Product family or category.")
    @Searchable
    public String family;

    @Field(name = "Created Date", type = "Date", required = false, description = "Date when the product was created.")
    public Long createdDate;

    @Field(name = "Created By Id", type = "Text", required = false, description = "User who created the product. References User.Id.")
    public String createdById;

    @Field(name = "Last Modified Date", type = "Date", required = false, description = "Date when the product was last modified.")
    public Long lastModifiedDate;

    @Field(name = "Last Modified By Id", type = "Text", required = false, description = "User who last modified the product. References User.Id.")
    public String lastModifiedById;

    /**
     * Converts a Map of field values to Product entity.
     * Used for creating products from catalog requests.
     *
     * @param fields Map containing product field values
     * @return Product entity instance
     */
    public static Product fromFields(Map<String, Object> fields) {
        Product product = new Product();
        product.id = (String) fields.get("Id");
        product.name = (String) fields.get("Name");
        product.productCode = (String) fields.get("Product Code");
        product.description = (String) fields.get("Description");
        product.isActive = (Boolean) fields.get("Is Active");
        product.family = (String) fields.get("Family");
        product.createdDate = (Long) fields.get("Created Date");
        product.createdById = (String) fields.get("Created By Id");
        product.lastModifiedDate = (Long) fields.get("Last Modified Date");
        product.lastModifiedById = (String) fields.get("Last Modified By Id");
        return product;
    }

    /**
     * Converts this Product entity to a Map of field values.
     *
     * @return Map containing product field values in declaration order
     */
    public Map<String, Object> toFields() {
        Map<String, Object> fields = new LinkedHashMap<>();
        fields.put("Id", id);
        fields.put("Name", name);
        fields.put("Product Code", productCode);
        fields.put("Description", description);
        fields.put("Is Active", isActive);
        fields.put("Family", family);
        fields.put("Created Date", createdDate);
        fields.put("Created By Id", createdById);
        fields.put("Last Modified Date", lastModifiedDate);
        fields.put("Last Modified By Id", lastModifiedById);
        return fields;
    }
}
