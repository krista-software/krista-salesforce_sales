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
