package app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes;

import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.PricebookEntry;
import app.krista.extensions.crm.sales.salesforce_sales.service.PriceBookEntry;

public final class PricebookEntryCatalogType {

    private PricebookEntryCatalogType() {
    }

    public static PricebookEntry fromSFPriceBookEntry(PriceBookEntry priceBookEntry) {
        PricebookEntry pricebookEntry = new PricebookEntry();
        pricebookEntry.id = priceBookEntry.getId();
        pricebookEntry.name = priceBookEntry.getName();
        pricebookEntry.pricebookId = priceBookEntry.getPriceBookId();
        pricebookEntry.productId = priceBookEntry.getProductId();
        pricebookEntry.productCode = priceBookEntry.getProductCode();
        pricebookEntry.createdDate = priceBookEntry.getCreatedDate();
        pricebookEntry.lastModifiedDate = priceBookEntry.getLastModifiedDate();
        pricebookEntry.unitPrice = priceBookEntry.getUnitPrice();
        return pricebookEntry;
    }
}
