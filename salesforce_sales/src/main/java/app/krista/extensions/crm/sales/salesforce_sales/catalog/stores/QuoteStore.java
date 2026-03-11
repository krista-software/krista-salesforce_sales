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

import app.krista.extension.executor.SearchCondition;
import app.krista.extension.executor.SearchQuery;
import app.krista.extension.util.EntityStore;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes.QuoteCatalogType;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Quote;
import app.krista.extensions.crm.sales.salesforce_sales.impl.QuoteDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.impl.QuoteImpl;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFQuote;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static app.krista.extensions.crm.sales.salesforce_sales.impl.QuoteDaoImpl.QUERY_FIELDS;
import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.AND;

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
            LOGGER.info("Search conditions cannot be null or empty");
        }
        List<SearchCondition> conditions = searchQuery.getSearchConditions();
        try {
            String name = getConditionValue(conditions, "Name");
            String accountId = getConditionValue(conditions, "Account Id");
            String status = getConditionValue(conditions, "Status");
            String opportunityId = getConditionValue(conditions, "Opportunity Id");
            List<Quote> quotes;
            if (conditions.size() == 1) {
                quotes = quoteDao.getQuotes(pageSize, pageIndex).stream().map(QuoteCatalogType::fromSFQuote).collect(Collectors.toList());
            } else {
                String query = createQuery(name, opportunityId, accountId, status, pageSize, pageIndex);
                quotes = quoteDao.getQuotesByQuery(query).stream().map(QuoteCatalogType::fromSFQuote).collect(Collectors.toList());
            }
            return quotes;
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
    public List<String> lookup(Map<String, Object> map, List<String> variableName) {
        if (variableName.contains("Status")) {
            return quoteDao.getQuoteStatuses();
        }
        return List.of("error");
    }

    private String getConditionValue(List<SearchCondition> conditions, String fieldName) {
        for (SearchCondition con : conditions) {
            if (fieldName.equalsIgnoreCase(con.getFieldName())) {
                return con.getOperand() != null ? String.valueOf(con.getOperand()).trim() : "";
            }
        }
        return "";
    }

    private String createQuery(String name, String opportunityId, String accountId, String status, long size, long page) {
        StringBuilder builder = new StringBuilder();
        builder.append("Select " + QUERY_FIELDS + " from Quote WHERE").
                append((isAvailable(name) ? " name LIKE '%" + name + "%'" + AND : "")).
                append((isAvailable(opportunityId) ? " opportunityId = '" + opportunityId + "'" + AND : "")).
                append((isAvailable(accountId) ? " accountId LIKE '%" + accountId + "%'" + AND : "")).
                append((isAvailable(status) ? " status LIKE '%" + status + "%'" + AND : ""));
        if (builder.substring(builder.length() - "WHERE".length()).equals("WHERE"))
            builder.setLength(builder.lastIndexOf("WHERE"));
        if (builder.substring(builder.length() - AND.length()).equals(AND))
            builder.setLength(builder.lastIndexOf(AND));
        builder.append(" ORDER BY LastModifiedDate LIMIT ").append(size).append(" OFFSET ").append(Math.max((page - 1) * size, 0));
        return builder.toString();
    }

    private static boolean isAvailable(String s) {
        return s != null && !s.isBlank();
    }
}
