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
     * Update {@link PriceBookEntry} to salesforce from provided input fields
     *
     * @param priceBookEntry {@link PriceBookEntry} object of salesforce to be updated
     */
    void updatePriceBookEntry(PriceBookEntry priceBookEntry);
}
