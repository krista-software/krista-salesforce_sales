package app.krista.extensions.crm.sales.salesforce_sales.service;

import java.util.List;
import java.util.Map;

public interface AssetDao {

    /**
     * Returns {@link Asset} object of salesforce from given asset id provided.
     *
     * @param id id of asset to be searched
     * @return {@link Asset} object of salesforce
     */
    Asset getAssetById(String id);

    /**
     * Returns {@link List<Asset>} list of object of salesforce from given asset id provided.
     *
     * @param id   id of asset to be searched
     * @param size number of assets to be fetched
     * @param page page number to be fetched
     * @return {@link List<Asset>} list of object of salesforce
     * @return empty list if does not find any records
     */
    List<Asset> getAssetById(String id, long size, long page);

    /**
     * Returns {@link List<Asset>} list of object of salesforce.
     *
     * @param size number of assets to be fetched
     * @param page page number to be fetched
     * @return {@link List<Asset>} list of object of salesforce
     * @return empty list if does not find any records
     */
    List<Asset> getAssets(long size, long page);

    /**
     * Add {@link Asset} to salesforce from provided input fields
     *
     * @param entityAttributeField fields which are added in the {@link Asset} object of salesforce to create
     */
    void addAsset(Map<String, Object> entityAttributeField);

    /**
     * Update {@link Asset} to salesforce from provided input fields
     *
     * @param asset {@link Asset} object of salesforce to be updated
     */
    void updateAsset(Asset asset);

    /**
     * Delete {@link Asset} object from the salesforce.
     *
     * @param id id of asset to be deleted
     */
    void deleteAssetById(String id);
}
