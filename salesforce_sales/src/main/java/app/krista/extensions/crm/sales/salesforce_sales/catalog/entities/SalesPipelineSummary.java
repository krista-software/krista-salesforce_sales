package app.krista.extensions.crm.sales.salesforce_sales.catalog.entities;


import app.krista.extension.impl.anno.Domain;
import app.krista.extension.impl.anno.Entity;
import app.krista.extension.impl.anno.Field;

@Domain(id = "catEntryDomain_086f33da-aaea-4d0c-816f-3d5f20b4a273",
        name = "Sales",
        ecosystemId = "catEntryEcosystem_8a0e8ce7-6289-492f-9202-52284024280c",
        ecosystemName = "CRM",
        ecosystemVersion = "db320c64-6f6f-4c9e-8daa-41d3f796d6a6")
@Entity(name = "Sales Pipeline Summary", id = "localDomainEntity_21cf61de-4c00-422e-8795-6c18ca84bd37", primaryKey = "Status", supportStore = false)
public class SalesPipelineSummary {

    @Field(name = "Status", type = "Text")
    public String status;

    @Field(name = "Average", type = "Currency")
    public Double average;

    @Field(name = "Total", type = "Currency")
    public Double total;

}