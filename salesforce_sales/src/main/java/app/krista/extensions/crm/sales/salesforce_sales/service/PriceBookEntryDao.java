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

public interface PriceBookEntryDao {

    /**
     * Returns {@link PriceBookEntry} object of salesforce from given pricebook id provided.
     *
     * @param id id of pricebook to be searched
     * @return {@link PriceBookEntry} object of salesforce
     */
    PriceBookEntry getPriceBookEntryById(String id);

    /**
     * Returns {@link List<PriceBookEntry>} list of object of salesforce from given pricebook name provided.
     *
     * @param name name of pricebook to be searched
     * @param size number of pricebook to be fetched
     * @param page page number to be fetched
     * @return {@link List<PriceBookEntry>} list of object of salesforce
     * @return empty list if does not find any records
     */
    List<PriceBookEntry> getPriceBookEntryByName(String name, long size, long page);

    /**
     * Returns {@link List<PriceBookEntry>} list of object of salesforce from given pricebook name provided.
     *
     * @param size number of pricebook to be fetched
     * @param page page number to be fetched
     * @return {@link List<PriceBookEntry>} list of object of salesforce
     * @return empty list if does not find any records
     */
    List<PriceBookEntry> getPriceBookEntries(long size, long page);

    /**
     * Add {@link PriceBookEntry} to salesforce from provided input fields
     *
     * @param entityAttributeField fields which are added in the {@link PriceBookEntry} object of salesforce to create
     */
    String addPriceBookEntry(Map<String, Object> entityAttributeField);

    /**
     * Returns matching {@link PriceBookEntry} for a given Product2Id and Pricebook2Id combination.
     *
     * @param product2Id   Salesforce ID of the product
     * @param pricebook2Id Salesforce ID of the pricebook
     * @return list with the matching entry, or empty list if not found
     */
    List<PriceBookEntry> getPriceBookEntryByProductAndPricebook(String product2Id, String pricebook2Id);

    /**
     * Returns list of {@link PriceBookEntry} objects from Salesforce by executing the given SOQL query.
     *
     * @param query SOQL query string
     * @return list of pricebook entries
     */
    List<PriceBookEntry> getPriceBookEntryByQuery(String query);

    /**
     * Update {@link PriceBookEntry} to salesforce from provided input fields
     *
     * @param priceBookEntry {@link PriceBookEntry} object of salesforce to be updated
     */
    void updatePriceBookEntry(PriceBookEntry priceBookEntry);
}
