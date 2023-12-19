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
