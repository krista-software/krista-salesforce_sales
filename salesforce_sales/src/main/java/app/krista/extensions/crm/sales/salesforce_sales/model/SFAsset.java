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

import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Asset;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.Map;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Validators.returnNullOrValue;

public final class SFAsset {

    @SerializedName("Id")
    public String id;

    @SerializedName("ContactId")
    public String contactId;

    @SerializedName("AccountId")
    public String accountId;

    @SerializedName("RootAssetId")
    public String rootAssetId;

    @SerializedName("IsCompetitorProduct")
    public Boolean isCompetitorProduct;

    @SerializedName("CreatedDate")
    public Date createdDate;

    @SerializedName("CreatedById")
    public String createdById;

    @SerializedName("LastModifiedDate")
    public Date lastModifiedDate;

    @SerializedName("IsDeleted")
    public Boolean isDeleted;

    @SerializedName("Name")
    public String name;

    @SerializedName("OwnerId")
    public String ownerId;

    public static SFAsset from(Asset asset) {
        SFAsset sfAsset = new SFAsset();
        sfAsset.id = asset.id;
        sfAsset.contactId = asset.contactId;
        sfAsset.accountId = asset.accountId;
        sfAsset.rootAssetId = asset.rootAssetId;
        sfAsset.isCompetitorProduct = asset.isCompetitorProduct;
        if (asset.createdDate != null)
            sfAsset.createdDate = new Date(asset.createdDate);
        sfAsset.createdById = asset.createdById;
        if (asset.lastModifiedDate != null)
            sfAsset.lastModifiedDate = new Date(asset.lastModifiedDate);
        sfAsset.isDeleted = asset.isDeleted;
        sfAsset.name = asset.name;
        sfAsset.ownerId = asset.ownerId;
        return sfAsset;
    }

    public static SFAsset fromField(Map<String, Object> entityAttributeField) {
        SFAsset asset = new SFAsset();
        asset.contactId = returnNullOrValue((String) entityAttributeField.get("Contact Id"));
        asset.accountId = returnNullOrValue((String) entityAttributeField.get("Account Id"));
        if (asset.contactId == null && asset.accountId == null) {
            throw new IllegalArgumentException("Provide an account, a contact, or both");
        }
        asset.rootAssetId = returnNullOrValue((String) entityAttributeField.get("Root Asset Id"));
        asset.isCompetitorProduct = (Boolean) entityAttributeField.get("Is Competitor Product");
        asset.createdDate = (Date) entityAttributeField.get("Created Date");
        asset.createdById = returnNullOrValue((String) entityAttributeField.get("Created By Id"));
        asset.lastModifiedDate = (Date) entityAttributeField.get("Last Modified Date");
        asset.name = returnNullOrValue((String) entityAttributeField.get("Name"));
        asset.ownerId = returnNullOrValue((String) entityAttributeField.get("Owner Id"));
        return asset;
    }
}
