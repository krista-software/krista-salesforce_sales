package app.krista.extensions.crm.sales.salesforce_sales.catalog.entities;


import app.krista.extension.impl.anno.*;

import java.util.Map;

@Domain(id = "catEntryDomain_086f33da-aaea-4d0c-816f-3d5f20b4a273",
        name = "Sales",
        ecosystemId = "catEntryEcosystem_8a0e8ce7-6289-492f-9202-52284024280c",
        ecosystemName = "CRM",
        ecosystemVersion = "25d0e050-2ab9-4f60-80f3-3b04dc83230a")
@Entity(name = "Asset", id = "localDomainEntity_e69ebb84-dd77-4ca5-8df2-a4f2bd209fc5", primaryKey = "Id")
public class Asset {

    @Searchable
    @Field(name = "Id", type = "Text", required = false)
    public String id;

    @Field(name = "Contact Id", type = "Text", required = false)
    public String contactId;

    @Field(name = "Account Id", type = "Text", required = false)
    public String accountId;

    @Field(name = "Root Asset Id", type = "Text", required = false)
    public String rootAssetId;

    @Field(name = "Is Competitor Product", type = "Switch", required = false)
    public Boolean isCompetitorProduct;

    @Field(name = "Created Date", type = "Date", required = false)
    public Long createdDate;

    @Field(name = "Created By Id", type = "Text", required = false)
    public String createdById;

    @Field(name = "Last Modified Date", type = "Date", required = false)
    public Long lastModifiedDate;

    @Field(name = "Is Deleted", type = "Switch", required = false)
    public Boolean isDeleted;

    @Searchable
    @ToString
    @Field(name = "Name", type = "Text")
    public String name;

    @Field(name = "Owner Id", type = "Text", required = false)
    public String ownerId;

    public static Asset fromFields(Map<String, Object> attributes) {
        Asset asset = new Asset();
        asset.id = (String) attributes.get("Id");
        asset.contactId = (String) attributes.get("Contact Id");
        asset.accountId = (String) attributes.get("Account Id");
        asset.rootAssetId = (String) attributes.get("Root Asset Id");
        asset.isCompetitorProduct = (Boolean) attributes.get("Is Competitor Product");
        asset.createdDate = (Long) attributes.get("Created Date");
        asset.createdById = (String) attributes.get("Created By Id");
        asset.lastModifiedDate = (Long) attributes.get("Last Modified Date");
        asset.isDeleted = (Boolean) attributes.get("Is Deleted");
        asset.name = (String) attributes.get("Name");
        asset.ownerId = (String) attributes.get("Owner Id");
        return asset;
    }

}