package app.krista.extensions.crm.sales.salesforce_sales.catalog;

import app.krista.extension.impl.anno.CatalogRequest;
import app.krista.extension.impl.anno.Domain;
import app.krista.extension.impl.anno.Field;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes.LeadCatalogType;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Lead;
import app.krista.extensions.crm.sales.salesforce_sales.impl.LeadDaoImpl;
import app.krista.model.base.FreeForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.CREATED;
import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.NAME;
import static app.krista.extensions.crm.sales.salesforce_sales.util.SFHelper.upsertLeadDetails;


@Domain(id = "catEntryDomain_086f33da-aaea-4d0c-816f-3d5f20b4a273",
        name = "Sales",
        ecosystemId = "catEntryEcosystem_8a0e8ce7-6289-492f-9202-52284024280c",
        ecosystemName = "CRM",
        ecosystemVersion = "db320c64-6f6f-4c9e-8daa-41d3f796d6a6")
public final class LeadsManagementArea {
    private final LeadDaoImpl leadDao;
    private static final Logger LOGGER = LoggerFactory.getLogger(LeadsManagementArea.class);


    @Inject
    private LeadsManagementArea(LeadDaoImpl leadDao) {
        this.leadDao = leadDao;
    }

    @CatalogRequest(
            id = "localDomainRequest_48c208ec-65c5-4b2f-9ce4-52a6c7e26d4a",
            name = "Get Leads",
            description = "This request will return a list of Leads filtered by field 'User' with the given 'Owner Id' or by 'New Leads' with 'Start Date Time'",
            area = "Leads Management",
            type = CatalogRequest.Type.QUERY_SYSTEM)
    @Field.Desc(name = "Leads", type = "[ Entity(Lead) ]", required = false)
    public List<Lead> getLeads(
            @Field.Desc(name = "Field", type = "PickOne(By User|New Leads)") String field,
            @Field(name = "Owner Id", type = "Text", required = false) String ownerId,
            @Field(name = "Start Date Time", type = "Date", required = false) Long startDateTime) {
        if (field == null || field.isBlank()) {
            throw new IllegalArgumentException("Field cannot be empty");
        }
        try {
            if (field.equalsIgnoreCase("By User")) {
                if (ownerId == null || ownerId.isBlank()) {
                    throw new IllegalArgumentException("Please provide ownerId");
                }
                return leadDao.getLeadsByOwnerId(ownerId).stream().map(LeadCatalogType::fromSFLead).collect(Collectors.toList());
            }
            if (field.equalsIgnoreCase("New Leads")) {
                if (startDateTime == null || startDateTime.toString().isBlank()) {
                    throw new IllegalArgumentException("Please provide startDateTime");
                }
                return leadDao.getLeadByDate(startDateTime).stream().map(LeadCatalogType::fromSFLead).collect(Collectors.toList());
            }
            return List.of();
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return List.of();
        }
    }

    @CatalogRequest(
            id = "localDomainRequest_190ec6ca-0e61-41ec-bce6-d04f9976f352",
            name = "Lead Change Alert",
            description = "This request will alert the user whenever you create a lead or update any lead.",
            area = "Leads Management",
            type = CatalogRequest.Type.WAIT_FOR_EVENT)
    @Field.Desc(name = "LeadResponse", type = "{ Lead Name: Text, Lead Id: Text, Lead Title: Text, Lead Company: Text, Lead Website: Text, Lead Owner Id: Text, Lead Is Converted: Text, Lead Converted Account Id: Text, Lead Converted Contact Id: Text, Lead Converted Opportunity Id: Text, Lead Clean Status: Text, Lead Phone: Text, Lead MobilePhone: Text, Lead Email: Email, Lead Description: Paragraph, Lead Status: Text, Lead LeadSource: Text, Lead Industry: Text, Lead Rating: Text, Lead Annual Revenue: Number, Lead Converted Date: Date, Lead Created Date: Date, Lead Last Modified Date: Date }", required = false)
    public Map<String, Object> leadChangeAlert(
            @Field(name = "eventName", type = "Text") String eventName,
            @Field(name = "eventData", type = "FreeForm") FreeForm eventData) {
        Map<String, Object> leadMap = new HashMap<>();
        try {
            if (eventName.equals("Lead Change Alert")) {
                leadMap.put("Lead Id", eventData.get("Id"));
                upsertLeadDetails(eventData, leadMap);
                updateLeadDetails(eventData, leadMap);
            }
        } catch (IllegalArgumentException cause) {
            LOGGER.error("Unable to create alert for lead change due to " + cause.getMessage(), cause);
        }
        return leadMap;
    }

    private void updateLeadDetails(FreeForm eventData, Map<String, Object> leadMap) {
        String query = "SELECT Fields(ALL) FROM LeadHistory WHERE LeadId = '" +
                eventData.get("Id") + "' ORDER BY CreatedDate DESC LIMIT 20";

        Map<String, Object> fetched = leadDao.getLeadsUpdatedFields(query);
        if (fetched.size() != 1 || !fetched.containsKey(CREATED)) {
            if (fetched.containsKey("FirstName") || fetched.containsKey("LastName")) {
                fetched.put(NAME, eventData.get(NAME));
            } else {
                fetched.put(NAME, "");
            }
            upsertLeadDetails(fetched, leadMap);
        }
    }
}
