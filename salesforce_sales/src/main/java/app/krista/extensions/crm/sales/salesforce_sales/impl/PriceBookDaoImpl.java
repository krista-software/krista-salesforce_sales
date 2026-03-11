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
import app.krista.extensions.crm.sales.salesforce_sales.service.PriceBookDao;
import app.krista.extensions.crm.sales.salesforce_sales.util.SalesforceListResponse;
import com.google.gson.reflect.TypeToken;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.util.stream.Collectors;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.GSON;

@Service
public class PriceBookDaoImpl implements PriceBookDao {

    private static final String QUERY_FIELDS = " Id,Name ";
    private final SalesforceManager salesforceManager;

    @Inject
    public PriceBookDaoImpl(SalesforceManager salesforceManager) {
        this.salesforceManager = salesforceManager;
    }

    @Override
    public PriceBook getPriceBook() {
        String res = salesforceManager.getRecordByQuery(
                "Select " + QUERY_FIELDS + " from Pricebook2 where Name='Standard'");
        SalesforceListResponse<SFPriceBook> oppList =
                GSON.fromJson(res, new TypeToken<SalesforceListResponse<SFPriceBook>>() {
                }.getType());
        if (oppList.records.size() > 0) {
            return oppList.records.stream().map(PriceBookImpl::new).collect(Collectors.toList()).stream().findFirst().orElseThrow();
        } else {
            return null;
        }
    }
}
