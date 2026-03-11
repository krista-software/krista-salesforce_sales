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
