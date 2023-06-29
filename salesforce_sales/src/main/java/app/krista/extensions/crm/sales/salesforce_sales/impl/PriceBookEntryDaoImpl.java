package app.krista.extensions.crm.sales.salesforce_sales.impl;

import app.krista.extensions.crm.sales.salesforce_sales.catalog.CatalogTypes;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFPricebookEntry;
import app.krista.extensions.crm.sales.salesforce_sales.service.PriceBookEntry;
import app.krista.extensions.crm.sales.salesforce_sales.service.PriceBookEntryDao;
import app.krista.extensions.crm.sales.salesforce_sales.util.SalesforceListResponse;
import com.google.gson.reflect.TypeToken;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.GSON;

@Service
public class PriceBookEntryDaoImpl implements PriceBookEntryDao {

    public static final String QUERY_FIELDS = "Id, Name, Pricebook2Id, Product2Id, UnitPrice, IsActive, CreatedDate, LastModifiedDate, ProductCode";
    public static final String PRICEBOOK_ENTRY = "PricebookEntry";
    private final SalesforceManager salesforceManager;

    @Inject
    public PriceBookEntryDaoImpl(SalesforceManager salesforceManager) {
        this.salesforceManager = salesforceManager;
    }

    @Override
    public PriceBookEntry getPriceBookEntryById(String id) {
        return getPriceBookEntryByQuery("Select " + QUERY_FIELDS + "  from PricebookEntry where id = '" + id + "'").stream().findFirst().orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public List<PriceBookEntry> getPriceBookEntryByName(String name, long size, long page) {
        return getPriceBookEntryByQuery("Select " + QUERY_FIELDS + "  from PricebookEntry where name LIKE '%" +
                name + "%' ORDER BY LastModifiedDate DESC  LIMIT " + size + " OFFSET " + Math.max((page - 1) * size, 0));
    }

    @Override
    public List<PriceBookEntry> getPriceBookEntries(long size, long page) {
        return getPriceBookEntryByQuery("Select " + QUERY_FIELDS + " from PricebookEntry ORDER BY LastModifiedDate DESC LIMIT " + size + " OFFSET " + Math.max((page - 1) * size, 0));
    }

    @Override
    public void addPriceBookEntry(Map<String, Object> entityAttributeField) {
        salesforceManager.createRecord(GSON.toJson(SFPricebookEntry.from(entityAttributeField)), PRICEBOOK_ENTRY);
    }

    @Override
    public void updatePriceBookEntry(PriceBookEntry priceBookEntry) {
        if (priceBookEntry.getId() == null || priceBookEntry.getId().isEmpty())
            throw new IllegalArgumentException("PricebookEntry Id cant be empty");
        SFPricebookEntry updatedPricebookEntry = SFPricebookEntry.from(CatalogTypes.fromSFPriceBookEntry(priceBookEntry));
        SFPricebookEntry oldPricebookEntry = SFPricebookEntry.from(CatalogTypes.fromSFPriceBookEntry(getPriceBookEntryById(updatedPricebookEntry.id)));
        Map<String, Object> diff = salesforceManager.getDifference(oldPricebookEntry, updatedPricebookEntry);
        salesforceManager.updateRecord(updatedPricebookEntry.id, GSON.toJson(diff), PRICEBOOK_ENTRY);
    }

    private List<PriceBookEntry> getPriceBookEntryByQuery(String query) {
        String response = salesforceManager.getRecordByQuery(query);
        SalesforceListResponse<SFPricebookEntry> oppList =
                GSON.fromJson(response, new TypeToken<SalesforceListResponse<SFPricebookEntry>>() {
                }.getType());
        if (oppList.records.size() > 0) {
            return oppList.records.stream().map(PriceBookEntryImpl::new).collect(Collectors.toList());
        } else {
            return List.of();
        }
    }
}
