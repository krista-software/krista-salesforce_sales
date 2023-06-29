package app.krista.extensions.crm.sales.salesforce_sales.impl;

import app.krista.extensions.crm.sales.salesforce_sales.model.SFPriceBook;
import app.krista.extensions.crm.sales.salesforce_sales.service.PriceBook;

public class PriceBookImpl implements PriceBook {

    private final SFPriceBook sfPriceBook;

    public PriceBookImpl(SFPriceBook sfPriceBook) {
        this.sfPriceBook = sfPriceBook;
    }

    @Override
    public String getId() {
        return sfPriceBook.Id;
    }

    @Override
    public String getName() {
        return sfPriceBook.Name;
    }
}
