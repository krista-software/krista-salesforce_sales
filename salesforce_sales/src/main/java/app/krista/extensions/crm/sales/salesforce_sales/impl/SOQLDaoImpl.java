package app.krista.extensions.crm.sales.salesforce_sales.impl;

import app.krista.extensions.crm.sales.salesforce_sales.SalesforceSalesAttributes;
import app.krista.extensions.crm.sales.salesforce_sales.service.SOQLDao;
import app.krista.extensions.crm.sales.salesforce_sales.util.SalesforceListResponse;
import com.google.gson.internal.LinkedTreeMap;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.GSON;

@Service
public class SOQLDaoImpl implements SOQLDao {

    private final SalesforceManager salesforceManager;

    @Inject
    public SOQLDaoImpl(SalesforceManager salesforceManager) {
        this.salesforceManager = salesforceManager;
    }

    @Override
    public Object getSalesforceObjectByQuery(String query) {
        List results = new ArrayList();
        if (query == null || query.isEmpty()) {
            return new HashMap();
        }
        String queryResult = salesforceManager.getRecordByQuery(query);
        if (queryResult == null || queryResult.isEmpty()) {
            System.err.println("Invalid Query");
            return new HashMap();
        }
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
        try {
            salesforceManager.getTokenProvider().getSalesforceTokenForUser();
        } catch (IOException cause) {
            System.err.println(cause.getMessage());
        }
    }
}
