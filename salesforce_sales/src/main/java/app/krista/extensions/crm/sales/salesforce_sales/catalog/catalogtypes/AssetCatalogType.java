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

package app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes;

import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Asset;

public final class AssetCatalogType {

    private AssetCatalogType() {
    }

    public static Asset fromSFAsset(app.krista.extensions.crm.sales.salesforce_sales.service.Asset asset) {
        Asset newAsset = new Asset();
        newAsset.id = asset.getId();
        newAsset.contactId = asset.getContactId();
        newAsset.accountId = asset.getAccountId();
        newAsset.rootAssetId = asset.getRootAssetId();
        newAsset.isCompetitorProduct = asset.getIsCompetitorProduct();
        newAsset.createdDate = asset.getCreatedDate();
        newAsset.createdById = asset.getCreatedById();
        newAsset.lastModifiedDate = asset.getLastModifiedDate();
        newAsset.isDeleted = asset.getIsDeleted();
        newAsset.name = asset.getName();
        newAsset.ownerId = asset.getOwnerId();
        return newAsset;
    }
}
