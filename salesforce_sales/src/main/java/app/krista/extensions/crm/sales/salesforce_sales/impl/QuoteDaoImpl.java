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

import app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes.QuoteCatalogType;
import app.krista.extensions.crm.sales.salesforce_sales.model.CreateRecordResponse;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFQuote;
import app.krista.extensions.crm.sales.salesforce_sales.service.PriceBook;
import app.krista.extensions.crm.sales.salesforce_sales.service.PriceBookEntry;
import app.krista.extensions.crm.sales.salesforce_sales.service.Quote;
import app.krista.extensions.crm.sales.salesforce_sales.service.QuoteDao;
import app.krista.extensions.crm.sales.salesforce_sales.util.Constants;
import app.krista.extensions.crm.sales.salesforce_sales.util.SalesforceListResponse;
import app.krista.model.base.FreeForm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.GSON;

@Service
public class QuoteDaoImpl implements QuoteDao {

    private static final String QUOTE = "Quote";
    public static final String QUERY_FIELDS = "Id, OwnerId, Name, CreatedDate, LastModifiedDate, OpportunityId, Pricebook2Id, ContactId, QuoteNumber, Tax, Status, ExpirationDate, Description, Subtotal, TotalPrice, LineItemCount, BillingName, ShippingName, ShippingHandling, QuoteToName, AdditionalName, Email, Phone, Fax, ContractId, AccountId, Discount, GrandTotal";
    private final SalesforceManager salesforceManager;
    private final PriceBookDaoImpl priceBookDao;
    private static final Logger LOGGER = LoggerFactory.getLogger(QuoteDaoImpl.class);


    @Inject
    public QuoteDaoImpl(SalesforceManager salesforceManager, PriceBookDaoImpl priceBookDao) {
        this.salesforceManager = salesforceManager;
        this.priceBookDao = priceBookDao;
    }

    @Override
    public Quote getQuoteById(String id) {
        return getQuotesByQuery("Select " + QUERY_FIELDS + "  from Quote where id = '" + id + "'").stream().findFirst().orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public FreeForm getQuoteMapById(String id) {
        try {
            String response = salesforceManager.getRecordById(id, QUOTE);
            SFQuote sfQuote = GSON.fromJson(response, SFQuote.class);
            HashMap result = new ObjectMapper().readValue(GSON.toJson(sfQuote), HashMap.class);
            FreeForm freeForm = new FreeForm();
            for (Object key : result.keySet()) {
                freeForm.put(key.toString().equalsIgnoreCase("Id") ? "QuoteId" : key.toString(), "Text", result.get(key));
            }
            return freeForm;
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public Quote getQuoteByOpportunityId(String opportunityId) {
        return getQuotesByQuery("Select " + QUERY_FIELDS + " from Quote where OpportunityId = '" + opportunityId + "' ORDER BY CreatedDate DESC LIMIT " + 1).stream().findFirst().orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public List<Quote> getQuotesByQuery(String query) {
        String response = salesforceManager.getRecordByQuery(query);
        SalesforceListResponse<SFQuote> oppList =
                GSON.fromJson(response, new TypeToken<SalesforceListResponse<SFQuote>>() {
                }.getType());
        if (oppList.records.size() > 0) {
            return oppList.records.stream().map(QuoteImpl::new).collect(Collectors.toList());
        } else {
            return List.of();
        }
    }

    @Override
    public List<Quote> getQuotes(long size, long page) {
        return getQuotesByQuery("Select " + QUERY_FIELDS + " from Quote ORDER BY LastModifiedDate DESC LIMIT " + size + " OFFSET " + Math.max((page - 1) * size, 0));
    }

    @Override
    public List<String> getQuoteStatuses() {
        String res = salesforceManager.getObjects("Quote/describe");
        JsonObject object = new JsonParser().parse(res).getAsJsonObject();
        JsonArray fields = object.get("fields").getAsJsonArray();
        JsonObject fieldStatus = null;
        for (JsonElement element : fields) {
            JsonObject json = (JsonObject) element;
            if (json.get("name").getAsString().equals("Status")) {
                fieldStatus = json;
                break;
            }
        }

        if (fieldStatus != null) {
            JsonArray status = fieldStatus.get("picklistValues").getAsJsonArray();
            if (status != null && status.size() > 0) {
                List<String> statusList = new ArrayList<>();
                for (JsonElement jsonElement : status) {
                    statusList.add(jsonElement.getAsJsonObject().get("value").getAsString());
                }
                return statusList;
            }
        }
        return List.of();
    }

    @Override
    public List<Object> getQuoteLineItems(String quoteId) {
        String res = salesforceManager.getRecordByQuery("Select LineNumber,QuoteId,PricebookEntryId,Quantity,UnitPrice,Discount,ListPrice,Subtotal,TotalPrice,Product2Id from QuoteLineItem WHERE QuoteId = '" + quoteId + "'");
        SalesforceListResponse<Map<String, Object>> oppList =
                GSON.fromJson(res, new TypeToken<SalesforceListResponse<Map<String, Object>>>() {
                }.getType());
        if (oppList.records.isEmpty()) {
            return List.of();
        } else {
            return oppList.records.stream().peek(x -> x.put("attributes", null)).collect(Collectors.toList());
        }
    }

    @Override
    public List<Object> getQuoteLineItems(String quoteId, List<Object> filters) {
        List<Object> quoteLineItems = getQuoteLineItems(quoteId);
        if (filters == null || filters.size() == 0) {
            return quoteLineItems;
        }

        for (Object filter : filters) {
            quoteLineItems = quoteLineItems.stream().filter(quoteLineItem -> filterQuoteLineItem((Map) filter, (Map) quoteLineItem)).collect(Collectors.toList());
        }
        return quoteLineItems;
    }

    @Override
    public String addQuote(Map<String, Object> entityAttributeField) {
        if (entityAttributeField == null || entityAttributeField.isEmpty()) {
            LOGGER.info("Failed create quote request. " + Constants.ENTITY_MAP_IS_NULL_OR_EMPTY);
        }
        SFQuote sfQuote = SFQuote.from(entityAttributeField);

        if (sfQuote.pricebookId == null) {
            PriceBook priceBook = priceBookDao.getPriceBook();
            if (priceBook.getId() != null)
                sfQuote.pricebookId = priceBook.getId();
        }
        var response = GSON.fromJson(salesforceManager.createRecord(GSON.toJson(sfQuote), QUOTE), Map.class);
        return String.valueOf(response.get("id"));
    }

    @Override
    public void addProductToQuote(Quote quote, PriceBookEntry priceBookEntry, Double discount) {
        Map<String, String> map = new HashMap<>();
        map.put("QuoteId", quote.getId());
        map.put("Quantity", "12");
        map.put("PricebookEntryId", priceBookEntry.getId());
        map.put("Product2Id", priceBookEntry.getProductId());
        map.put("UnitPrice", String.valueOf(priceBookEntry.getUnitPrice()));
        map.put("Discount", discount != null ? String.valueOf(discount) : null);
        GSON.fromJson(salesforceManager.createRecord(GSON.toJson(map), "QuoteLineItem"), CreateRecordResponse.class);
    }

    @Override
    public void updateQuote(Quote quote) {
        if (quote.getId() == null || quote.getId().isEmpty())
            throw new IllegalArgumentException("Quote ID is required. Please provide the Salesforce ID of the quote you want to update.");
        SFQuote updatedQuote = SFQuote.from(QuoteCatalogType.fromSFQuote(quote));
        SFQuote oldQuote = SFQuote.from(QuoteCatalogType.fromSFQuote(getQuoteById(updatedQuote.id)));
        Map<String, Object> diff = salesforceManager.getDifference(oldQuote, updatedQuote);
        salesforceManager.updateRecord(updatedQuote.id, GSON.toJson(diff), QUOTE);
    }

    private static boolean filterQuoteLineItem(Map filter, Map<String, Object> quoteLineItem) {
        for (Object key : filter.keySet()) {
            return quoteLineItem.get(key) != null && String.valueOf(quoteLineItem.get(key)).equals(((Map<?, ?>) filter).get(key));
        }
        return false;
    }

    public void deleteQuote(String key) {
        salesforceManager.deleteRecordById(key, QUOTE);
    }
}
