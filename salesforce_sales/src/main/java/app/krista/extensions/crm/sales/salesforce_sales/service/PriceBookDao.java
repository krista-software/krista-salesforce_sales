package app.krista.extensions.crm.sales.salesforce_sales.service;

public interface PriceBookDao {

    /**
     * Returns {@link PriceBook} object of salesforce
     * @return {@link PriceBook} object of salesforce
     * @return null if does not found records
     */
    PriceBook getPriceBook();
}
