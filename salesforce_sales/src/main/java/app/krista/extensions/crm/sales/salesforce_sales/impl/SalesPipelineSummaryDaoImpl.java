package app.krista.extensions.crm.sales.salesforce_sales.impl;

import app.krista.extensions.crm.sales.salesforce_sales.model.SFSalesPipelineSummary;
import app.krista.extensions.crm.sales.salesforce_sales.service.SalesPipelineSummary;
import app.krista.extensions.crm.sales.salesforce_sales.service.SalesPipelineSummaryDao;
import app.krista.extensions.crm.sales.salesforce_sales.util.SalesforceListResponse;
import com.google.gson.reflect.TypeToken;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.GSON;

@Service
public class SalesPipelineSummaryDaoImpl implements SalesPipelineSummaryDao {

    private final SalesforceManager salesforceManager;

    @Inject
    public SalesPipelineSummaryDaoImpl(SalesforceManager salesforceManager) {
        this.salesforceManager = salesforceManager;
    }

    @Override
    public List<SalesPipelineSummary> getSalesPipelinedSummaries() {
        String response = salesforceManager.getRecordByQuery("SELECT  StageName, AVG(Amount)  Average, SUM(AMOUNT)  Total from Opportunity GROUP BY StageName");
        SalesforceListResponse<SFSalesPipelineSummary> oppList =
                GSON.fromJson(response, new TypeToken<SalesforceListResponse<SFSalesPipelineSummary>>() {
                }.getType());
        if (!oppList.records.isEmpty()) {
            return oppList.records.stream().map(SalesPipelineSummaryImpl::new).collect(Collectors.toList());
        } else {
            return List.of();
        }
    }
}
