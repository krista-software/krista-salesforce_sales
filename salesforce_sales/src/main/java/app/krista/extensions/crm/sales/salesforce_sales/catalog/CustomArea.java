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
import java.util.Map;

@Domain(id = "catEntryDomain_086f33da-aaea-4d0c-816f-3d5f20b4a273",
        name = "Sales",
        ecosystemId = "catEntryEcosystem_8a0e8ce7-6289-492f-9202-52284024280c",
        ecosystemName = "CRM",
        ecosystemVersion = "dc440ce1-a1c6-42eb-89b4-cfbfe1ca9f70")
public class CustomArea {
    private final SalesforceManager salesforceManager;
@Inject
    public CustomArea(SalesforceManager salesforceManager) {
        this.salesforceManager = salesforceManager;
    }


    @CatalogRequest(
            id = "localDomainRequest_db413776-e8f0-47f5-bf3a-d868b7fe5634",
            name = "Retrieve Custom Fields For Given Object",
            description = "Retrieve Custom Fields For Given Object",
            area = "Custom",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field(name = "Field Details", type = "FreeForm", required = false, attributes = {}, options = {})
    public FreeForm retrieveCustomFieldsForGivenObject(
            @Field.Text(name = "Object Id", required = true, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {}) String objectId,
            @Field.PickOne(name = "Object Type", values = {"Account","Asset","Event","Lead","Opportunity","PricebookEntry","Quote","Task","User"}, required = true, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {}) String objectType,
            @Field.Desc(name = "Custom Fields", type = "[ Text ]",required = true) List<String> customFields) {
        StringBuilder sb = new StringBuilder();

        // Iterate through the list of strings
        for (String str : customFields) {
            // Append each string followed by a comma
            sb.append(str).append(",");
        }

        // Remove the trailing comma if the list is not empty
        if (!customFields.isEmpty()) {
            sb.deleteCharAt(sb.length() - 1);
        }
        String s = sb.toString();
        String query = "Select "+s+" from " + objectType +" where id = '" + objectId+"'";
        String recordByQuery = salesforceManager.getRecordByQuery(query);
        //GSON.fromJson(recordByQuery, SFAccount.class);
        FreeForm resultMap = parseJSONToMap(recordByQuery,customFields);

        // Output the map
        System.out.println("Resulting Map:");
        for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
return resultMap;
        //return  new FreeForm();
        //throw new UnsupportedOperationException("This method is not yet implemented! If you don't want to support this method, please remove the complete method");
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
                    resultMap.put(key, value);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resultMap;
    }
}
//            for (String key : record.keySet()) {
//                Object value = record.get(key);
//                resultMap.put(key, value);
//            }