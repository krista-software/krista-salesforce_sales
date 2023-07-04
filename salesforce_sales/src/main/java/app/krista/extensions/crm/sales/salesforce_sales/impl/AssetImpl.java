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
