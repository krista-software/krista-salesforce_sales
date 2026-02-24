package app.krista.extensions.crm.sales.salesforce_sales.catalog.stores;

import app.krista.extension.executor.SearchCondition;
import app.krista.extension.executor.SearchQuery;
import app.krista.extension.util.EntityStore;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes.PricebookEntryCatalogType;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.PricebookEntry;
import app.krista.extensions.crm.sales.salesforce_sales.impl.PriceBookEntryDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.impl.PriceBookEntryImpl;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFPricebookEntry;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PricebookEntryStore implements EntityStore<PricebookEntry> {

    private final PriceBookEntryDaoImpl priceBookEntryDao;
    private static final Logger LOGGER = LoggerFactory.getLogger(PricebookEntryStore.class);


    @Inject
    public PricebookEntryStore(PriceBookEntryDaoImpl priceBookEntryDao) {
        this.priceBookEntryDao = priceBookEntryDao;
    }

    @Override
    public PricebookEntry create(Map<String, Object> entityAttributeField) {
        if (entityAttributeField == null || entityAttributeField.isEmpty()) {
            LOGGER.info("Entity attribute field map cannot be null or empty");
        }
        try {
            String priceBookEntryId = priceBookEntryDao.addPriceBookEntry(entityAttributeField);
            return get(priceBookEntryId);
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return new PricebookEntry();
        }
    }

    @Override
    public PricebookEntry get(String primaryKey) {
        if (primaryKey == null || primaryKey.isEmpty()) {
            LOGGER.info("Primary key cannot be null or empty");
        }
        try {
            return PricebookEntryCatalogType.fromSFPriceBookEntry(priceBookEntryDao.getPriceBookEntryById(primaryKey));
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return null;
        }
    }

    @Override
    public PricebookEntry update(PricebookEntry pricebookEntry) {
        if (pricebookEntry == null) {
            LOGGER.info("Price book Entry cannot be null");
        }
        try {
            priceBookEntryDao.updatePriceBookEntry(new PriceBookEntryImpl(SFPricebookEntry.from(pricebookEntry)));
            return get(pricebookEntry.id);
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return null;
        }
    }

    @Override
    public void delete(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Pricebook Entry ID is required for deletion. Please provide the Salesforce ID of the pricebook entry you want to delete.");
        }
        priceBookEntryDao.deletePriceBook(key);
    }

    @Override
    public boolean contains(String s) {
        return false;
    }

    @Override
    public List<PricebookEntry> search(List<SearchCondition> conditions, long pageIndex, int pageSize) {
        return List.of();
    }

    @Override
    public List<PricebookEntry> search(SearchQuery searchQuery, long pageIndex, int pageSize) throws IOException {
        if (searchQuery == null || searchQuery.getSearchConditions() == null || searchQuery.getSearchConditions().isEmpty()) {
            LOGGER.info("Search conditions cannot be null or empty");
        }
        List<SearchCondition> conditions = searchQuery.getSearchConditions();
        try {
            List<PricebookEntry> pricebookEntries = new ArrayList<>();
            for (SearchCondition condition : conditions) {
                String matchPattern = condition.getOperand() != null ? String.valueOf(condition.getOperand()).trim() : "";
                String selector = condition.getFieldName();
                if ("Name".equalsIgnoreCase(selector)) {
                    pricebookEntries = priceBookEntryDao.getPriceBookEntryByName(matchPattern, pageSize, pageIndex).stream().map(PricebookEntryCatalogType::fromSFPriceBookEntry).collect(Collectors.toList());
                }
            }
            if (conditions.size() == 1 && pricebookEntries.isEmpty()) {
                pricebookEntries = priceBookEntryDao.getPriceBookEntries(pageSize, pageIndex).stream().map(PricebookEntryCatalogType::fromSFPriceBookEntry).collect(Collectors.toList());
            }
            return pricebookEntries;
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
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
