package app.krista.extensions.crm.sales.salesforce_sales.catalog;

import app.krista.extension.impl.anno.Attribute;
import app.krista.extension.impl.anno.CatalogRequest;
import app.krista.extension.impl.anno.Domain;
import app.krista.extension.impl.anno.Field;
import app.krista.extensions.crm.sales.salesforce_sales.impl.SalesforceManager;
import app.krista.extensions.crm.sales.salesforce_sales.impl.stores.ContentDaoImpl;
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
        ecosystemVersion = "ed97ad0a-c01c-4cfd-8855-ce14af770527")
public class ContentArea {
    private final SalesforceManager salesforceManager;
    private final ContentDaoImpl contentDao;


    @Inject
    public ContentArea(SalesforceManager salesforceManager, ContentDaoImpl contentDao) {
        this.salesforceManager = salesforceManager;
        this.contentDao = contentDao;
    }

    @CatalogRequest(
            id = "localDomainRequest_b6f197c8-1886-44ac-b938-02cdb3ee922d",
            name = "Retrieve Custom Fields For Given Object",
            description = "Retrieve Custom Fields For Given Object",
            area = "Content",
            type = CatalogRequest.Type.QUERY_SYSTEM)
    @Field(name = "Field Details", type = "FreeForm", required = false, attributes = {}, options = {})
    public FreeForm retrieveCustomFieldsForGivenObject(
            @Field.Text(name = "Object Id", required = true, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {}) String objectId,
            @Field.PickOne(name = "Object Type", values = {"Account","Asset","Event","Lead","Opportunity","PricebookEntry","Quote","Task","User"}, required = true, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {}) String objectType,
            @Field.Desc(name = "Custom Fields", type = "[ Text ]",required = true) List<String> customFields) {
        StringBuilder customField = new StringBuilder();
        for (String str : customFields) {
            customField.append(str).append(",");
        }
        if (!customFields.isEmpty()) {
            customField.deleteCharAt(customField.length() - 1);
        }
        String queryFields = customField.toString();
        String query = "Select " + queryFields + " from " + objectType + " where id = '" + objectId + "'";
        String recordByQuery = salesforceManager.getRecordByQuery(query);
        if (recordByQuery != null) {
            return contentDao.parseJSONToMap(recordByQuery, customFields);
        }
        return new FreeForm();
    }

}
