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
