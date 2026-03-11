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

package app.krista.extensions.crm.sales.salesforce_sales.service;

import java.util.List;
import java.util.Map;

public interface QuoteDao {

    /**
     * Returns {@link Quote} object of salesforce from given quote id provided.
     *
     * @param id id of quote to be searched
     * @return {@link Quote} object of salesforce
     */
    Quote getQuoteById(String id);

    /**
     * Returns Map of a {@link Quote} object of salesforce from given quote id provided.
     *
     * @param id id of quote to be searched
     * @return Map of a {@link Quote} object of salesforce
     */
    Map getQuoteMapById(String id);

    /**
     * Returns {@link Quote} object of salesforce from given quote's opportunity id provided.
     *
     * @param opportunityId opportunity id of quote to be searched
     * @return {@link Quote} object of salesforce
     */
    Quote getQuoteByOpportunityId(String opportunityId);

    /**
     * Returns {@link List<Quote>} list of object of salesforce.
     *
     * @param query query to be searched
     * @return {@link List<Quote>} list of object of salesforce
     * @return empty list if does not find any records
     */
    List<Quote> getQuotesByQuery(String query);

    /**
     * Returns {@link List<Quote>} list of object of salesforce.
     *
     * @param size number of quote to be fetched
     * @param page page number to be fetched
     * @return {@link List<Quote>} list of object of salesforce
     * @return empty list if does not find any records
     */
    List<Quote> getQuotes(long size, long page);

    /**
     * Returns List of statues of the quote.
     *
     * @return {@link List<String>} list of statues of the quote.
     */
    List<String> getQuoteStatuses();

    /**
     * Returns list of line items of quote of salesforce from given quote id provided.
     *
     * @param quoteId quote id of quote to be searched
     * @return {@link List<Object>} list of object of salesforce
     */
    List<Object> getQuoteLineItems(String quoteId);

    /**
     * Returns list of line items of quote of salesforce from given quote id and filters provided.
     *
     * @param quoteId quote id of quote to be searched
     * @param filters filters to be applied on searched result
     * @return {@link List<Object>} list of object of salesforce
     */
    List<Object> getQuoteLineItems(String quoteId, List<Object> filters);

    /**
     * Add {@link Quote} to salesforce from provided input fields
     *
     * @param entityAttributeField fields which are added in the {@link Quote} object of salesforce to create
     */
    String addQuote(Map<String, Object> entityAttributeField);

    /**
     * Add {@link PriceBookEntry} to the quote of salesforce from provided input fields
     *
     * @param quote          quote to which pricebook will get added
     * @param priceBookEntry pricebook which are added in the {@link Quote} object of salesforce
     * @param discount       discount field which get add in pricebook
     */
    void addProductToQuote(Quote quote, PriceBookEntry priceBookEntry, Double discount);

    /**
     * Update {@link Quote} to salesforce from provided input fields
     *
     * @param quote {@link Quote} object of salesforce to be updated
     */
    void updateQuote(Quote quote);

}
