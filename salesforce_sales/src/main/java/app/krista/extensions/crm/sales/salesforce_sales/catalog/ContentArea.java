package app.krista.extensions.crm.sales.salesforce_sales.catalog;

import app.krista.extension.impl.anno.Attribute;
import app.krista.extension.impl.anno.CatalogRequest;
import app.krista.extension.impl.anno.Domain;
import app.krista.extension.impl.anno.Field;
import app.krista.extensions.crm.sales.salesforce_sales.impl.SalesforceManager;
import app.krista.model.base.FreeForm;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.List;

@Domain(id = "catEntryDomain_086f33da-aaea-4d0c-816f-3d5f20b4a273",
        name = "Sales",
        ecosystemId = "catEntryEcosystem_8a0e8ce7-6289-492f-9202-52284024280c",
        ecosystemName = "CRM",
        ecosystemVersion = "d2511b1f-4e1f-4ef7-80d8-52c6300a365c")
public class ContentArea {
    private final SalesforceManager salesforceManager;

    @Inject
    public ContentArea(SalesforceManager salesforceManager) {
        this.salesforceManager = salesforceManager;
    }

    @CatalogRequest(
            id = "localDomainRequest_db413776-e8f0-47f5-bf3a-d868b7fe5634",
            name = "Retrieve Custom Fields For Given Object",
            description = "Retrieve Custom Fields For Given Object",
            area = "Content",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field(name = "Field Details", type = "FreeForm", required = false, attributes = {}, options = {})
    public FreeForm retrieveCustomFieldsForGivenObject(
            @Field.Text(name = "Object Id", required = true, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {}) String objectId,
            @Field.PickOne(name = "Object Type", values = {"Account", "Asset", "Event", "Lead", "Opportunity", "PricebookEntry", "Quote", "Task", "User"}, required = true, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {}) String objectType,
            @Field.Desc(name = "Custom Fields", type = "[ Text ]", required = true) List<String> customFields) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : customFields) {
            stringBuilder.append(str).append(",");
        }
        if (!customFields.isEmpty()) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        String queryFields = stringBuilder.toString();
        String query = "Select " + queryFields + " from " + objectType + " where id = '" + objectId + "'";
        String recordByQuery = salesforceManager.getRecordByQuery(query);
        if (recordByQuery != null) {
            return parseJSONToMap(recordByQuery, customFields);
        }
        return new FreeForm();
    }

    public static FreeForm parseJSONToMap(String jsonResponse, List<String> customFields) {
        FreeForm resultMap = new FreeForm();
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray records = jsonObject.getJSONArray("records");
            JSONObject record = records.getJSONObject(0);

            for (String key : customFields) {
                if (record.has(key)) {
                    Object value = record.get(key);
                    if (value.equals(null)) {
                        resultMap.put(key, "Text", "");
                    } else {
                        resultMap.put(key, "Text", value);
                    }

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultMap;
    }
}
