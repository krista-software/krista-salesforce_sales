package app.krista.extensions.crm.sales.salesforce_sales.catalog.entities;

import app.krista.extension.impl.anno.*;

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
@Entity(name = "Product", id = "localDomainEntity_c3d4e5f6-7a8b-9c0d-1e2f-3a4b5c6d7e8f", primaryKey = "Id", supportStore = true)
public class Product {
    
    @Field(name = "Id", type = "Text", required = false)
    @Searchable
    public String id;
    
    @Field(name = "Name", type = "Text", required = true)
    @ToString
    public String name;
    
    @Field(name = "Product Code", type = "Text", required = true)
    @Searchable
    public String productCode;
    
    @Field(name = "Description", type = "Text", required = false)
    public String description;
    
    @Field.Boolean(name = "Is Active", required = false)
    public Boolean isActive;
    
    @Field(name = "Family", type = "Text", required = false)
    @Searchable
    public String family;
    
    @Field(name = "Created Date", type = "Date", required = false)
    public Long createdDate;
    
    @Field(name = "Created By Id", type = "Text", required = false)
    public String createdById;
    
    @Field(name = "Last Modified Date", type = "Date", required = false)
    public Long lastModifiedDate;
    
    @Field(name = "Last Modified By Id", type = "Text", required = false)
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
}

