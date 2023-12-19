package app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes;

import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.SalesPipelineSummary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CatalogTypes {

    private CatalogTypes() {
    }

    public static SalesPipelineSummary fromSFSalesPipelineSummary(app.krista.extensions.crm.sales.salesforce_sales.service.SalesPipelineSummary salesPipelineSummary) {
        SalesPipelineSummary newSalesPipelineSummary = new SalesPipelineSummary();
        newSalesPipelineSummary.status = salesPipelineSummary.getStatus();
        newSalesPipelineSummary.average = salesPipelineSummary.getAverage();
        newSalesPipelineSummary.total = salesPipelineSummary.getTotal();
        return newSalesPipelineSummary;
    }

    public static Map<String, Object> getFieldMapFromList(List quoteFields) {
        Map<String, Object> fieldsMap = new HashMap<>();
        for (Object field : quoteFields) {
            fieldsMap.putAll((Map<? extends String, ?>) field);
        }
        return fieldsMap;
    }
}