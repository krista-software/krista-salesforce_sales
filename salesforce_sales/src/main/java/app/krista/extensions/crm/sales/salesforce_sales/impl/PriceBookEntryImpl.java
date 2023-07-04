package app.krista.extensions.crm.sales.salesforce_sales.impl;

import app.krista.extensions.crm.sales.salesforce_sales.model.SFPricebookEntry;
import app.krista.extensions.crm.sales.salesforce_sales.service.PriceBookEntry;

import javax.inject.Inject;

public class PriceBookEntryImpl implements PriceBookEntry {

    private final SFPricebookEntry sfPricebookEntry;

    @Inject
    public PriceBookEntryImpl(SFPricebookEntry sfPricebookEntry) {
        this.sfPricebookEntry = sfPricebookEntry;
    }

    @Override
    public String getId() {
        return sfPricebookEntry.id;
    }

    @Override
    public String getName() {
        return sfPricebookEntry.name;
    }

    @Override
    public String getPriceBookId() {
        return sfPricebookEntry.pricebookId;
    }

    @Override
    public String getProductId() {
        return sfPricebookEntry.productId;
    }

    @Override
    public String getProductCode() {
        return sfPricebookEntry.productCode;
    }

    @Override
    public Long getCreatedDate() {
        return sfPricebookEntry.createdDate != null ? sfPricebookEntry.createdDate.getTime() : null;
    }

    @Override
    public Long getLastModifiedDate() {
        return sfPricebookEntry.lastModifiedDate != null ? sfPricebookEntry.lastModifiedDate.getTime() : null;
    }

    @Override
    public Double getUnitPrice() {
        return sfPricebookEntry.unitPrice;
    }
}
