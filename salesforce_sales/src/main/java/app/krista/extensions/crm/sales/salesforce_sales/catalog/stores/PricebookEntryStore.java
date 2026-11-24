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

package app.krista.extensions.crm.sales.salesforce_sales.catalog.stores;

import app.krista.extension.executor.QueryClause;
import app.krista.extension.executor.SearchCondition;
import app.krista.extension.executor.SearchQuery;
import app.krista.extension.util.EntityStore;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes.PricebookEntryCatalogType;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.PricebookEntry;
import app.krista.extensions.crm.sales.salesforce_sales.impl.PriceBookEntryDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.impl.PriceBookEntryImpl;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFPricebookEntry;
import app.krista.extensions.crm.sales.salesforce_sales.util.SoqlQueryHelper;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static app.krista.extensions.crm.sales.salesforce_sales.impl.PriceBookEntryDaoImpl.QUERY_FIELDS;

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
            return priceBookEntryDao.getPriceBookEntries(pageSize, pageIndex).stream().map(PricebookEntryCatalogType::fromSFPriceBookEntry).collect(Collectors.toList());
        }
        List<SearchCondition> conditions = searchQuery.getSearchConditions();
        List<QueryClause> clauses = searchQuery.getQueryClauses();
        try {
            // Single Id lookup optimization
            for (SearchCondition sc : conditions) {
                if ("Id".equalsIgnoreCase(sc.getFieldName()) && sc.getOperand() != null) {
                    String id = String.valueOf(sc.getOperand()).trim();
                    if (!id.isEmpty()) {
                        PricebookEntry entity = get(id);
                        return entity != null ? List.of(entity) : List.of();
                    }
                }
            }
            String where = SoqlQueryHelper.buildWhereClause("pricebookentry", conditions);
            String orderBy = SoqlQueryHelper.buildOrderByClause("pricebookentry", clauses);
            String pagination = SoqlQueryHelper.buildPaginationClause(pageSize, pageIndex);

            if (where.isEmpty()) {
                return priceBookEntryDao.getPriceBookEntries(pageSize, pageIndex).stream().map(PricebookEntryCatalogType::fromSFPriceBookEntry).collect(Collectors.toList());
            }
            String query = "SELECT " + QUERY_FIELDS + " FROM PricebookEntry WHERE " + where + orderBy + pagination;
            return priceBookEntryDao.getPriceBookEntryByQuery(query).stream().map(PricebookEntryCatalogType::fromSFPriceBookEntry).collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
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
