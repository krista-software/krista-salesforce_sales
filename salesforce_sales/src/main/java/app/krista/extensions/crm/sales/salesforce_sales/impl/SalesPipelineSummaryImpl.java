package app.krista.extensions.crm.sales.salesforce_sales.impl;

import app.krista.extensions.crm.sales.salesforce_sales.model.SFSalesPipelineSummary;
import app.krista.extensions.crm.sales.salesforce_sales.service.SalesPipelineSummary;

public class SalesPipelineSummaryImpl implements SalesPipelineSummary {

    private final SFSalesPipelineSummary sfSalesPipelineSummary;

    public SalesPipelineSummaryImpl(SFSalesPipelineSummary sfSalesPipelineSummary) {
        this.sfSalesPipelineSummary = sfSalesPipelineSummary;
    }

    @Override
    public String getStatus() {
        return sfSalesPipelineSummary.StageName;
    }

    @Override
    public Double getAverage() {
        return sfSalesPipelineSummary.Average;
    }

    @Override
    public Double getTotal() {
        return sfSalesPipelineSummary.Total;
    }
}
