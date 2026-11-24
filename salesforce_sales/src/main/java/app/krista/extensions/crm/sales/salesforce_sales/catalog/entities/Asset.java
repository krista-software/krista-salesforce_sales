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

@Searchable
@Domain(id = "catEntryDomain_086f33da-aaea-4d0c-816f-3d5f20b4a273",
        name = "Sales",
        ecosystemId = "catEntryEcosystem_8a0e8ce7-6289-492f-9202-52284024280c",
        ecosystemName = "CRM",
        ecosystemVersion = "25d0e050-2ab9-4f60-80f3-3b04dc83230a")
@Entity(name = "Asset", id = "localDomainEntity_e69ebb84-dd77-4ca5-8df2-a4f2bd209fc5", primaryKey = "Id", supportStore = true, description = "Represents a purchased product (asset) in Salesforce. Belongs to an Account (via Account Id referencing Account.Id). Owner Id references User.Id. Contact Id references a Contact.")
public class Asset {

    @Searchable
    @Field(name = "Id", type = "Text", required = false, description = "Unique Salesforce identifier for the asset.")
    public String id;

    @Field(name = "Contact Id", type = "Text", required = false, description = "Id of the associated contact.")
    public String contactId;

    @Field(name = "Account Id", type = "Text", required = false, description = "Id of the associated account. References Account.Id.")
    public String accountId;

    @Field(name = "Root Asset Id", type = "Text", required = false, description = "Id of the root asset in the asset hierarchy.")
    public String rootAssetId;

    @Field(name = "Is Competitor Product", type = "Switch", required = false, description = "Whether this is a competitor's product.")
    public Boolean isCompetitorProduct;

    @Field(name = "Created Date", type = "Date", required = false, description = "Date when the asset was created.")
    public Long createdDate;

    @Field(name = "Created By Id", type = "Text", required = false, description = "User who created the asset. References User.Id.")
    public String createdById;

    @Field(name = "Last Modified Date", type = "Date", required = false, description = "Date when the asset was last modified.")
    public Long lastModifiedDate;

    @Field(name = "Is Deleted", type = "Switch", required = false, description = "Whether the asset has been soft-deleted.")
    public Boolean isDeleted;

    @Searchable
    @ToString
    @Field(name = "Name", type = "Text", description = "Name of the asset.")
    public String name;

    @Field(name = "Owner Id", type = "Text", required = false, description = "Salesforce User Id of the asset owner. References User.Id.")
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

    public Map<String, Object> toFields() {
        Map<String, Object> fieldMap = new LinkedHashMap<>();
        fieldMap.put("Id", id);
        fieldMap.put("Contact Id", contactId);
        fieldMap.put("Account Id", accountId);
        fieldMap.put("Root Asset Id", rootAssetId);
        fieldMap.put("Is Competitor Product", isCompetitorProduct);
        fieldMap.put("Created Date", createdDate);
        fieldMap.put("Created By Id", createdById);
        fieldMap.put("Last Modified Date", lastModifiedDate);
        fieldMap.put("Is Deleted", isDeleted);
        fieldMap.put("Name", name);
        fieldMap.put("Owner Id", ownerId);
        return fieldMap;
    }

}
