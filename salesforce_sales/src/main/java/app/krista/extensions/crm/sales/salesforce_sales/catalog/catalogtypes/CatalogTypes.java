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