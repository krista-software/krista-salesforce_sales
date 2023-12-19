package app.krista.extensions.crm.sales.salesforce_sales.service;

import java.util.List;

public interface SalesPipelineSummaryDao {

    /**
     * Returns {@link List<SalesPipelineSummary>} object of salesforce
     *
     * @return {@link List<SalesPipelineSummary>} object of salesforce
     */
    List<SalesPipelineSummary> getSalesPipelinedSummaries();
}
