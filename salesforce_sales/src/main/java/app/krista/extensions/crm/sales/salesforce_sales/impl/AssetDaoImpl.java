package app.krista.extensions.crm.sales.salesforce_sales.impl;

import app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes.AssetCatalogType;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFAsset;
import app.krista.extensions.crm.sales.salesforce_sales.service.Asset;
import app.krista.extensions.crm.sales.salesforce_sales.service.AssetDao;
import app.krista.extensions.crm.sales.salesforce_sales.util.Constants;
import app.krista.extensions.crm.sales.salesforce_sales.util.SalesforceListResponse;
import com.google.gson.reflect.TypeToken;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.GSON;

@Service
public class AssetDaoImpl implements AssetDao {

    private static final String ASSET = "Asset";
    private static final String QUERY_FIELDS = " Id,ContactId,AccountId,RootAssetId,IsCompetitorProduct,CreatedDate,CreatedById,LastModifiedDate,IsDeleted,Name,OwnerId ";
    private final SalesforceManager salesforceManager;
    private static final Logger LOGGER = LoggerFactory.getLogger(AssetDaoImpl.class);


    @Inject
    public AssetDaoImpl(SalesforceManager salesforceManager) {
        this.salesforceManager = salesforceManager;
    }

    @Override
    public Asset getAssetById(String id) {
        return getAssetByQuery("Select " + QUERY_FIELDS + " from Asset where id = '" + id + "'").stream().findFirst().orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public List<Asset> getAssetById(String id, long size, long page) {
        return getAssetByQuery("Select " + QUERY_FIELDS + "  from Asset where id = '" + id + "' ORDER BY Name LIMIT " + size + " OFFSET " + Math.max((page - 1) * size, 0));
    }

    @Override
    public List<Asset> getAssets(long size, long page) {
        return getAssetByQuery("Select " + QUERY_FIELDS + "  from Asset ORDER BY Name LIMIT " + size + " OFFSET " + Math.max((page - 1) * size, 0));
    }

    @Override
    public String addAsset(Map<String, Object> entityAttributeField) {
        if (entityAttributeField == null || entityAttributeField.isEmpty()) {
            LOGGER.info("Failed create asset request. " + Constants.ENTITY_MAP_IS_NULL_OR_EMPTY);
        }
        var response = GSON.fromJson(salesforceManager.createRecord(GSON.toJson(SFAsset.fromField(entityAttributeField)), ASSET), Map.class);
        return String.valueOf(response.get("id"));
    }

    @Override
    public void updateAsset(Asset asset) {
        if (asset.getId() == null || asset.getId().isEmpty())
            throw new RuntimeException("Asset Id cant be empty");
        SFAsset updatedSFAsset = SFAsset.from(AssetCatalogType.fromSFAsset(asset));
        SFAsset oldAsset = SFAsset.from(AssetCatalogType.fromSFAsset(getAssetById(asset.getId())));
        Map<String, Object> diff = salesforceManager.getDifference(oldAsset, updatedSFAsset);
        salesforceManager.updateRecord(updatedSFAsset.id, GSON.toJson(diff), ASSET);
    }

    @Override
    public void deleteAssetById(String id) {
        salesforceManager.deleteRecordById(id, ASSET);
    }

    private List<Asset> getAssetByQuery(String query) {
        String response = salesforceManager.getRecordByQuery(query);
        SalesforceListResponse<SFAsset> oppList = GSON.fromJson(response, new TypeToken<SalesforceListResponse<SFAsset>>() {
        }.getType());
        if (oppList.records.size() > 0) {
            return oppList.records.stream().map(AssetImpl::new).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }
}
