package app.krista.extensions.crm.sales.salesforce_sales.catalog;

import app.krista.extension.impl.anno.CatalogRequest;
import app.krista.extension.impl.anno.Domain;
import app.krista.extension.impl.anno.Field;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes.AssetCatalogType;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Asset;
import app.krista.extensions.crm.sales.salesforce_sales.impl.AssetDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.impl.AssetImpl;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFAsset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

@Domain(id = "catEntryDomain_086f33da-aaea-4d0c-816f-3d5f20b4a273",
        name = "Sales",
        ecosystemId = "catEntryEcosystem_8a0e8ce7-6289-492f-9202-52284024280c",
        ecosystemName = "CRM",
        ecosystemVersion = "ad741807-a1da-4c8a-8bbe-6eeab572dcd5")
public final class AssetsManagementArea {

    private final AssetDaoImpl assetDao;
    private static final Logger LOGGER = LoggerFactory.getLogger(AssetsManagementArea.class);


    @Inject
    public AssetsManagementArea(AssetDaoImpl assetDao) {
        this.assetDao = assetDao;
    }

    @CatalogRequest(
            id = "localDomainRequest_f77ec339-c143-46b8-97cd-e7a3a651bde5",
            name = "Delete An Asset",
            description = "This request will delete the Asset",
            area = "Assets Management",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field(name = "Status", type = "Switch", required = false)
    public Boolean deleteAnAsset(
            @Field(name = "Id", type = "Text") String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Asset ID is required. Please provide the Salesforce ID of the asset you want to delete.");
        }
        try {
            assetDao.deleteAssetById(id);
            return true;
        } catch (IllegalArgumentException cause) {
            LOGGER.error("Provided asset ID does not exist or is not accessible");
            LOGGER.error(cause.getMessage(), cause);
            return false;
        }
    }

    @CatalogRequest(
            id = "localDomainRequest_26538e58-225f-4961-a868-8d36bacadbdb",
            name = "Update An Asset",
            description = "This request will modify the Asset data with the given input parameters",
            area = "Assets Management",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field.Desc(name = "Asset", type = "Entity(Asset)", required = false)
    public Asset updateAnAsset(
            @Field(name = "Id", type = "Text") String id,
            @Field(name = "Contact Id", type = "Text") String contactId,
            @Field(name = "Account Id", type = "Text") String accountId,
            @Field(name = "Is Competitor Product", type = "Switch") Boolean isCompetitorProduct,
            @Field(name = "Name", type = "Text") String name) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Asset ID is required. Please provide the Salesforce ID of the asset you want to update.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Asset Name is required. Please provide a name for the asset.");
        }
        try {
            Asset updateAsset = new Asset();
            updateAsset.id = id;
            updateAsset.contactId = contactId;
            updateAsset.accountId = accountId;
            updateAsset.isCompetitorProduct = isCompetitorProduct;
            updateAsset.name = name;
            assetDao.updateAsset(new AssetImpl(SFAsset.from(updateAsset)));
            return AssetCatalogType.fromSFAsset(assetDao.getAssetById(id));
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return null;
        }
    }

}
