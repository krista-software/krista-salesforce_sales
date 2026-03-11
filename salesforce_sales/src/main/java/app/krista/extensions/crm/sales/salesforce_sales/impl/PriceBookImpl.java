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

import app.krista.extensions.crm.sales.salesforce_sales.model.SFPriceBook;
import app.krista.extensions.crm.sales.salesforce_sales.service.PriceBook;

public class PriceBookImpl implements PriceBook {

    private final SFPriceBook sfPriceBook;

    public PriceBookImpl(SFPriceBook sfPriceBook) {
        this.sfPriceBook = sfPriceBook;
    }

    @Override
    public String getId() {
        return sfPriceBook.Id;
    }

    @Override
    public String getName() {
        return sfPriceBook.Name;
    }
}
