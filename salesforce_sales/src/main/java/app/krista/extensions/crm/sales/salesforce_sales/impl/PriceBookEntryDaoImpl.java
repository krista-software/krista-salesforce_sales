package app.krista.extensions.crm.sales.salesforce_sales.impl;

import app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes.PricebookEntryCatalogType;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFPricebookEntry;
import app.krista.extensions.crm.sales.salesforce_sales.service.PriceBookEntry;
import app.krista.extensions.crm.sales.salesforce_sales.service.PriceBookEntryDao;
import app.krista.extensions.crm.sales.salesforce_sales.util.Constants;
import app.krista.extensions.crm.sales.salesforce_sales.util.SalesforceListResponse;
import com.google.gson.reflect.TypeToken;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.GSON;

@Service
public class PriceBookEntryDaoImpl implements PriceBookEntryDao {

    private static final String QUERY_FIELDS = "Id, Name, Pricebook2Id, Product2Id, UnitPrice, IsActive, CreatedDate, LastModifiedDate, ProductCode";
    private static final String PRICEBOOK_ENTRY = "PricebookEntry";
    private final SalesforceManager salesforceManager;
    private static final Logger LOGGER = LoggerFactory.getLogger(PriceBookEntryDaoImpl.class);


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
    public String addPriceBookEntry(Map<String, Object> entityAttributeField) {
        if (entityAttributeField == null || entityAttributeField.isEmpty()) {
            LOGGER.info("Failed create pricebookentry request. " + Constants.ENTITY_MAP_IS_NULL_OR_EMPTY);
        }
        var response = GSON.fromJson(salesforceManager.createRecord(GSON.toJson(SFPricebookEntry.from(entityAttributeField)), PRICEBOOK_ENTRY), Map.class);
        return String.valueOf(response.get("id"));
    }

    @Override
    public void updatePriceBookEntry(PriceBookEntry priceBookEntry) {
        if (priceBookEntry.getId() == null || priceBookEntry.getId().isEmpty())
            throw new IllegalArgumentException("PricebookEntry Id cant be empty");
        SFPricebookEntry updatedPricebookEntry = SFPricebookEntry.from(PricebookEntryCatalogType.fromSFPriceBookEntry(priceBookEntry));
        SFPricebookEntry oldPricebookEntry = SFPricebookEntry.from(PricebookEntryCatalogType.fromSFPriceBookEntry(getPriceBookEntryById(updatedPricebookEntry.id)));
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

    public void deletePriceBook(String key) {
        salesforceManager.deleteRecordById(key, PRICEBOOK_ENTRY);
    }
}
