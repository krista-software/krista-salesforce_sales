/*
 * Salesforce Sales Extension for Krista
 * Copyright (C) 2025 Krista Software
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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

    public static final String QUERY_FIELDS = "Id, Name, Pricebook2Id, Product2Id, UnitPrice, IsActive, CreatedDate, LastModifiedDate, ProductCode";
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
    public List<PriceBookEntry> getPriceBookEntryByProductAndPricebook(String product2Id, String pricebook2Id) {
        return getPriceBookEntryByQuery("SELECT " + QUERY_FIELDS + " FROM PricebookEntry WHERE Product2Id = '" + product2Id + "' AND Pricebook2Id = '" + pricebook2Id + "' LIMIT 1");
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
            throw new IllegalArgumentException("Pricebook Entry ID is required. Please provide the Salesforce ID of the pricebook entry you want to update.");
        SFPricebookEntry updatedPricebookEntry = SFPricebookEntry.from(PricebookEntryCatalogType.fromSFPriceBookEntry(priceBookEntry));
        SFPricebookEntry oldPricebookEntry = SFPricebookEntry.from(PricebookEntryCatalogType.fromSFPriceBookEntry(getPriceBookEntryById(updatedPricebookEntry.id)));
        Map<String, Object> diff = salesforceManager.getDifference(oldPricebookEntry, updatedPricebookEntry);
        salesforceManager.updateRecord(updatedPricebookEntry.id, GSON.toJson(diff), PRICEBOOK_ENTRY);
    }

    @Override
    public List<PriceBookEntry> getPriceBookEntryByQuery(String query) {
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
