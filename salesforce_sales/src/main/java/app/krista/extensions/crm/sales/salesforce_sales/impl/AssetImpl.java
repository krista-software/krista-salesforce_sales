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

import app.krista.extensions.crm.sales.salesforce_sales.model.SFAsset;
import app.krista.extensions.crm.sales.salesforce_sales.service.Asset;

public class AssetImpl implements Asset {

    private final SFAsset sfAsset;

    public AssetImpl(SFAsset sfAsset) {
        this.sfAsset = sfAsset;
    }

    @Override
    public String getId() {
        return sfAsset.id;
    }

    @Override
    public String getContactId() {
        return sfAsset.contactId;
    }

    @Override
    public String getAccountId() {
        return sfAsset.accountId;
    }

    @Override
    public String getRootAssetId() {
        return sfAsset.rootAssetId;
    }

    @Override
    public Boolean getIsCompetitorProduct() {
        return sfAsset.isCompetitorProduct;
    }

    @Override
    public Long getCreatedDate() {
        return sfAsset.createdDate != null ? sfAsset.createdDate.getTime() : null;
    }

    @Override
    public String getCreatedById() {
        return sfAsset.createdById;
    }

    @Override
    public Long getLastModifiedDate() {
        return sfAsset.lastModifiedDate != null ? sfAsset.lastModifiedDate.getTime() : null;
    }

    @Override
    public Boolean getIsDeleted() {
        return sfAsset.isDeleted;
    }

    @Override
    public String getName() {
        return sfAsset.name;
    }

    @Override
    public String getOwnerId() {
        return sfAsset.ownerId;
    }
}
