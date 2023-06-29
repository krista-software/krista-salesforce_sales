package app.krista.extensions.crm.sales.salesforce_sales.catalog.stores;

import app.krista.extension.executor.SearchCondition;
import app.krista.extension.executor.SearchQuery;
import app.krista.extension.util.EntityStore;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.CatalogTypes;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.PricebookEntry;
import app.krista.extensions.crm.sales.salesforce_sales.impl.PriceBookEntryDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.impl.PriceBookEntryImpl;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFPricebookEntry;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PricebookEntryStore implements EntityStore<PricebookEntry> {

    private final PriceBookEntryDaoImpl priceBookEntryDao;

    @Inject
    public PricebookEntryStore(PriceBookEntryDaoImpl priceBookEntryDao) {
        this.priceBookEntryDao = priceBookEntryDao;
    }

    @Override
    public PricebookEntry create(Map<String, Object> entityAttributeField) {
        try {
            priceBookEntryDao.addPriceBookEntry(entityAttributeField);
            return PricebookEntry.fromFields(entityAttributeField);
        } catch (IllegalArgumentException cause) {
            System.err.println(cause.getMessage());
            cause.printStackTrace();
            return new PricebookEntry();
        }
    }

    @Override
    public PricebookEntry get(String primaryKey) {
        try {
            return CatalogTypes.fromSFPriceBookEntry(priceBookEntryDao.getPriceBookEntryById(primaryKey));
        } catch (IllegalArgumentException cause) {
            System.err.println(cause.getMessage());
            cause.printStackTrace();
            return null;
        }
    }

    @Override
    public PricebookEntry update(PricebookEntry pricebookEntry) {
        try {
            priceBookEntryDao.updatePriceBookEntry(new PriceBookEntryImpl(SFPricebookEntry.from(pricebookEntry)));
            return pricebookEntry;
        } catch (IllegalArgumentException cause) {
            System.err.println(cause.getMessage());
            cause.printStackTrace();
            return null;
        }
    }

    @Override
    public void delete(String s) {

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
        List<SearchCondition> conditions = searchQuery.getSearchConditions();
        try {
            List<PricebookEntry> pricebookEntries = new ArrayList<>();
            for (SearchCondition condition : conditions) {
                String matchPattern = condition.getOperand() != null ? String.valueOf(condition.getOperand()).trim() : "";
                String selector = condition.getFieldName();
                if ("Name".equalsIgnoreCase(selector)) {
                    pricebookEntries = priceBookEntryDao.getPriceBookEntryByName(matchPattern, pageSize, pageIndex).stream().map(CatalogTypes::fromSFPriceBookEntry).collect(Collectors.toList());
                }
            }
            if (conditions.size() == 1 && pricebookEntries.isEmpty()) {
                pricebookEntries = priceBookEntryDao.getPriceBookEntries(pageSize, pageIndex).stream().map(CatalogTypes::fromSFPriceBookEntry).collect(Collectors.toList());
            }
            return pricebookEntries;
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
