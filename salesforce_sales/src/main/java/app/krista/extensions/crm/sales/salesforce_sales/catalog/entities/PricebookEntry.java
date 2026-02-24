package app.krista.extensions.crm.sales.salesforce_sales.catalog.entities;


import app.krista.extension.impl.anno.*;

import java.util.Map;

@Domain(id = "catEntryDomain_086f33da-aaea-4d0c-816f-3d5f20b4a273",
        name = "Sales",
        ecosystemId = "catEntryEcosystem_8a0e8ce7-6289-492f-9202-52284024280c",
        ecosystemName = "CRM",
        ecosystemVersion = "db320c64-6f6f-4c9e-8daa-41d3f796d6a6")
@Entity(name = "Pricebook Entry", id = "localDomainEntity_cad67c19-86fd-4220-9768-c190a70f9fa5", primaryKey = "Id", supportStore = true)
public class PricebookEntry {

    @Field(name = "Id", type = "Text", required = false)
    public String id;

    @ToString
    @Field(name = "Name", type = "Text", required = false)
    public String name;

    @Field(name = "Pricebook Id", type = "Text", required = true)
    public String pricebookId;

    @Field(name = "Product Id", type = "Text", required = true)
    public String productId;

    @Field(name = "Product Code", type = "Text", required = false)
    public String productCode;

    @Field(name = "Created Date", type = "Date", required = false, attributes = @Attribute(name = "includeTimeOfDay", value = "true"))
    public Long createdDate;

    @Field(name = "Last Modified Date", type = "Date", required = false, attributes = @Attribute(name = "includeTimeOfDay", value = "true"))
    public Long lastModifiedDate;

    @Field(name = "Unit Price", type = "Currency", required = true)
    public Double unitPrice;

    public static PricebookEntry fromFields(Map<String, Object> fields) {
        PricebookEntry pricebookEntry = new PricebookEntry();
        pricebookEntry.id = (String) fields.get("Id");
        pricebookEntry.name = (String) fields.get("Name");
        pricebookEntry.pricebookId = (String) fields.get("Pricebook Id");
        pricebookEntry.productId = (String) fields.get("Product Id");
        pricebookEntry.productCode = (String) fields.get("Product Code");
        pricebookEntry.createdDate = (Long) fields.get("Created Date");
        pricebookEntry.lastModifiedDate = (Long) fields.get("Last Modified Date");
        pricebookEntry.unitPrice = (Double) fields.get("Unit Price");
        return pricebookEntry;
    }

}
