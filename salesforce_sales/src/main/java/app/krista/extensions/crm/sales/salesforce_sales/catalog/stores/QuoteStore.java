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
import app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes.QuoteCatalogType;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Quote;
import app.krista.extensions.crm.sales.salesforce_sales.impl.QuoteDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.impl.QuoteImpl;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFQuote;
import app.krista.extensions.crm.sales.salesforce_sales.util.SoqlQueryHelper;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static app.krista.extensions.crm.sales.salesforce_sales.impl.QuoteDaoImpl.QUERY_FIELDS;

@Service
public class QuoteStore implements EntityStore<Quote> {
    private final QuoteDaoImpl quoteDao;
    private static final Logger LOGGER = LoggerFactory.getLogger(QuoteStore.class);


    @Inject
    public QuoteStore(QuoteDaoImpl quoteDao) {
        this.quoteDao = quoteDao;
    }

    @Override
    public Quote create(Map<String, Object> entityAttributeField) {
        if (entityAttributeField == null || entityAttributeField.isEmpty()) {
            LOGGER.info("Entity attribute field map cannot be null or empty");
        }
        try {
            String quoteId = quoteDao.addQuote(entityAttributeField);
            return get(quoteId);
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return new Quote();
        }
    }

    @Override
    public Quote get(String primaryKey) {
        if (primaryKey == null || primaryKey.isEmpty()) {
            LOGGER.info("Primary key cannot be null or empty");
        }
        try {
            return QuoteCatalogType.fromSFQuote(quoteDao.getQuoteById(primaryKey));
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return null;
        }
    }

    @Override
    public Quote update(Quote quote) {
        if (quote == null) {
            LOGGER.info("Quote cannot be null");
        }
        try {
            quoteDao.updateQuote(new QuoteImpl(SFQuote.from(quote)));
            return quote;
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return null;
        }
    }

    @Override
    public void delete(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Quote ID is required for deletion. Please provide the Salesforce ID of the quote you want to delete.");
        }
        quoteDao.deleteQuote(key);
    }

    @Override
    public boolean contains(String s) {
        return false;
    }

    @Override
    public List<Quote> search(List<SearchCondition> conditions, long pageIndex, int pageSize) {
        return List.of();
    }

    @Override
    public List<Quote> search(SearchQuery searchQuery, long pageIndex, int pageSize) throws IOException {
        if (searchQuery == null || searchQuery.getSearchConditions() == null || searchQuery.getSearchConditions().isEmpty()) {
            return quoteDao.getQuotes(pageSize, pageIndex).stream().map(QuoteCatalogType::fromSFQuote).collect(Collectors.toList());
        }
        List<SearchCondition> conditions = searchQuery.getSearchConditions();
        List<QueryClause> clauses = searchQuery.getQueryClauses();
        try {
            // Single Id lookup optimization
            for (SearchCondition sc : conditions) {
                if ("Id".equalsIgnoreCase(sc.getFieldName()) && sc.getOperand() != null) {
                    String id = String.valueOf(sc.getOperand()).trim();
                    if (!id.isEmpty()) {
                        Quote entity = get(id);
                        return entity != null ? List.of(entity) : List.of();
                    }
                }
            }
            String where = SoqlQueryHelper.buildWhereClause("quote", conditions);
            String orderBy = SoqlQueryHelper.buildOrderByClause("quote", clauses);
            String pagination = SoqlQueryHelper.buildPaginationClause(pageSize, pageIndex);

            if (where.isEmpty()) {
                return quoteDao.getQuotes(pageSize, pageIndex).stream().map(QuoteCatalogType::fromSFQuote).collect(Collectors.toList());
            }
            String query = "SELECT " + QUERY_FIELDS + " FROM Quote WHERE " + where + orderBy + pagination;
            return quoteDao.getQuotesByQuery(query).stream().map(QuoteCatalogType::fromSFQuote).collect(Collectors.toList());
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
    public List<String> lookup(Map<String, Object> map, List<String> variableName) {
        if (variableName.contains("Status")) {
            return quoteDao.getQuoteStatuses();
        }
        return List.of("error");
    }
}
