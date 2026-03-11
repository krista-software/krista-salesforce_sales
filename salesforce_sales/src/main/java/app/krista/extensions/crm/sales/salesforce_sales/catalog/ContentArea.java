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

package app.krista.extensions.crm.sales.salesforce_sales.catalog;

import app.krista.extension.impl.anno.Attribute;
import app.krista.extension.impl.anno.CatalogRequest;
import app.krista.extension.impl.anno.Domain;
import app.krista.extension.impl.anno.Field;
import app.krista.extensions.crm.sales.salesforce_sales.impl.SalesforceManager;
import app.krista.extensions.crm.sales.salesforce_sales.impl.stores.ContentDaoImpl;
import app.krista.model.base.FreeForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger LOGGER = LoggerFactory.getLogger(ContentArea.class);

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
            @Field.PickOne(name = "Object Type", values = {"Account", "Asset", "Event", "Lead", "Opportunity", "PricebookEntry", "Quote", "Task", "User"}, required = true, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {}) String objectType,
            @Field.Desc(name = "Custom Fields", type = "[ Text ]", required = true) List<String> customFields) {
        StringBuilder customField = new StringBuilder();
        for (String str : customFields) {
            if (!str.endsWith("__c")) { // custom fields ends with __c
                LOGGER.info("{} is not a custom field", str);
                return new FreeForm();
            }
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
