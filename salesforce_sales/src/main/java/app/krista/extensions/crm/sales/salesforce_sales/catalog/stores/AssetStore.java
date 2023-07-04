package app.krista.extensions.crm.sales.salesforce_sales.catalog.stores;

import app.krista.extension.executor.SearchCondition;
import app.krista.extension.executor.SearchQuery;
import app.krista.extension.util.EntityStore;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.CatalogTypes;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Asset;
import app.krista.extensions.crm.sales.salesforce_sales.impl.AssetDaoImpl;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AssetStore implements EntityStore<Asset> {

    private final AssetDaoImpl assetDao;

    @Inject
    public AssetStore(AssetDaoImpl assetDao) {
        this.assetDao = assetDao;
    }

    @Override
    public Asset create(Map<String, Object> entityAttributeField) {
        if (entityAttributeField == null || entityAttributeField.isEmpty()) {
            System.err.println("Entity attribute field map cannot be null or empty");
        }
        try {
            assetDao.addAsset(entityAttributeField);
            return Asset.fromFields(entityAttributeField);
        } catch (IllegalArgumentException cause) {
            System.err.println(cause.getMessage());
            cause.printStackTrace();
            return new Asset();
        }
    }

    @Override
    public Asset get(String primaryKey) {
        if (primaryKey == null || primaryKey.isEmpty()) {
            System.err.println("Primary key cannot be null or empty");
        }
        try {
            return CatalogTypes.fromSFAsset(assetDao.getAssetById(primaryKey));
        } catch (IllegalArgumentException cause) {
            System.err.println(cause.getMessage());
            cause.printStackTrace();
            return null;
        }
    }

    @Override
    public Asset update(Asset asset) {
        return null;
    }

    @Override
    public void delete(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Delete identifier cannot be null or empty");
        }
        assetDao.deleteAssetById(key);
    }

    @Override
    public boolean contains(String s) {
        return false;
    }

    @Override
    public List<Asset> search(List<SearchCondition> conditions, long pageIndex, int pageSize) {
        return List.of();
    }

    @Override
    public List<Asset> search(SearchQuery searchQuery, long pageIndex, int pageSize) throws IOException {
        if (searchQuery == null || searchQuery.getSearchConditions() == null || searchQuery.getSearchConditions().isEmpty()) {
            System.err.println("Search conditions cannot be null or empty");
        }
        List<SearchCondition> conditions = searchQuery.getSearchConditions();
        try {
            List<Asset> assets = new ArrayList<>();
            for (SearchCondition condition : conditions) {
                String matchPattern = condition.getOperand() != null ? String.valueOf(condition.getOperand()).trim() : "";
                String selector = condition.getFieldName();
                if ("Id".equalsIgnoreCase(selector)) {
                    assets = assetDao.getAssetById(matchPattern, pageSize, pageIndex).stream().map(CatalogTypes::fromSFAsset).collect(Collectors.toList());
                }
            }
            if (conditions.size() == 1 && assets.isEmpty()) {
                assets = assetDao.getAssets(pageSize, pageIndex).stream().map(CatalogTypes::fromSFAsset).collect(Collectors.toList());
            }
            return assets;
        } catch (IllegalArgumentException cause) {
            System.err.println(cause.getMessage());
            cause.printStackTrace();
            return List.of();
        }
    }

    @Override
    public long count(List<SearchCondition> list) {
        return 0;
    }

    @Override
    public List<String> lookup(Map<String, Object> map, List<String> list) {
        return null;
    }
}
