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

import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.PricebookEntry;
import app.krista.extensions.crm.sales.salesforce_sales.service.PriceBookEntry;

public final class PricebookEntryCatalogType {

    private PricebookEntryCatalogType() {
    }

    public static PricebookEntry fromSFPriceBookEntry(PriceBookEntry priceBookEntry) {
        PricebookEntry pricebookEntry = new PricebookEntry();
        pricebookEntry.id = priceBookEntry.getId();
        pricebookEntry.name = priceBookEntry.getName();
        pricebookEntry.pricebookId = priceBookEntry.getPriceBookId();
        pricebookEntry.productId = priceBookEntry.getProductId();
        pricebookEntry.productCode = priceBookEntry.getProductCode();
        pricebookEntry.createdDate = priceBookEntry.getCreatedDate();
        pricebookEntry.lastModifiedDate = priceBookEntry.getLastModifiedDate();
        pricebookEntry.unitPrice = priceBookEntry.getUnitPrice();
        return pricebookEntry;
    }
}
