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

import app.krista.extensions.crm.sales.salesforce_sales.SalesforceSalesAttributes;
import app.krista.extensions.crm.sales.salesforce_sales.service.SOQLDao;
import app.krista.extensions.crm.sales.salesforce_sales.util.SalesforceListResponse;
import com.google.gson.internal.LinkedTreeMap;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.*;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.GSON;

@Service
public class SOQLDaoImpl implements SOQLDao {

    private final SalesforceManager salesforceManager;
    private static final Logger LOGGER = LoggerFactory.getLogger(SOQLDaoImpl.class);

    @Inject
    public SOQLDaoImpl(SalesforceManager salesforceManager) {
        this.salesforceManager = salesforceManager;
    }

    @Override
    public Object getSalesforceObjectByQuery(String query) {
        List results = new ArrayList();
        if (query == null || query.isEmpty()) {
            return Collections.emptyMap();
        }
        String queryResult = salesforceManager.getRecordByQuery(query);
        return parseQueryResult(results, queryResult);
    }

    public SalesforceSalesAttributes getSalesAttributes() {
        return salesforceManager.getSalesAttributes();
    }

    public static Map parseQueryResult(List results, String res) {
        SalesforceListResponse response = GSON.fromJson(res, SalesforceListResponse.class);
        if (response.records.size() > 0) {
            for (Object map : response.records) {
                Map<String, Object> fieldMap = new LinkedTreeMap<>();
                if (map instanceof LinkedTreeMap) {
                    for (Object key : ((LinkedTreeMap<?, ?>) map).keySet()) {
                        if (!key.equals("attributes")) {
                            if (((LinkedTreeMap<?, ?>) map).get(key) == null) {
                                fieldMap.put((String) key, "");
                            } else {
                                fieldMap.put((String) key, ((LinkedTreeMap<?, ?>) map).get(key));
                            }
                        }
                    }
                    results.add(fieldMap);
                }
            }
            Map responses = new HashMap<>();
            responses.put("Responses", results);
            return responses;
        } else {
            return new HashMap<>();
        }
    }

    @Override
    public String getSalesforceObjectByQuery(String query, boolean invokeAsUser, String accountId) {
        if (query == null || query.isEmpty()) {
            return null;
        }
        return salesforceManager.getRecordByQuery(query, invokeAsUser, accountId);
    }

    @Override
    public void validateRefreshToken() {
        salesforceManager.getTokenProvider().getSalesforceTokenForUser();
    }
}
