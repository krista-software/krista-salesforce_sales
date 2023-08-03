package app.krista.extensions.crm.sales.salesforce_sales.catalog;

import app.krista.extension.impl.anno.CatalogRequest;
import app.krista.extension.impl.anno.Domain;
import app.krista.extension.impl.anno.Field;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Lead;
import app.krista.extensions.crm.sales.salesforce_sales.impl.LeadDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.impl.UserDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.util.DatePlus;
import app.krista.model.base.FreeForm;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.CREATED;
import static app.krista.extensions.crm.sales.salesforce_sales.util.Validators.getValidatedDouble;
import static app.krista.extensions.crm.sales.salesforce_sales.util.Validators.getValidatedString;

@Domain(id = "catEntryDomain_086f33da-aaea-4d0c-816f-3d5f20b4a273",
        name = "Sales",
        ecosystemId = "catEntryEcosystem_8a0e8ce7-6289-492f-9202-52284024280c",
        ecosystemName = "CRM",
        ecosystemVersion = "db320c64-6f6f-4c9e-8daa-41d3f796d6a6")
public final class LeadsManagementArea {

    private final LeadDaoImpl leadDao;
    private final UserDaoImpl userDao;

    @Inject
    private LeadsManagementArea(LeadDaoImpl leadDao, UserDaoImpl userDao) {

        this.leadDao = leadDao;
        this.userDao = userDao;

    }
//    java.util.concurrent.ExecutionException:
//    ServerInternal:java.util.NoSuchElementException No area found for: Accounts Management

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
                return leadDao.getLeadsByOwnerId(ownerId).stream().map(CatalogTypes::fromSFLead).collect(Collectors.toList());
            }
            if (field.equalsIgnoreCase("New Leads")) {
                if (startDateTime == null || startDateTime.toString().isBlank()) {
                    throw new IllegalArgumentException("Please provide startDateTime");
                }
                return leadDao.getLeadByDate(startDateTime).stream().map(CatalogTypes::fromSFLead).collect(Collectors.toList());
            }
            return List.of();
        } catch (IllegalArgumentException cause) {
            System.err.println(cause.getMessage());
            cause.printStackTrace();
            return List.of();
        }
    }

    @CatalogRequest(
            id = "localDomainRequest_190ec6ca-0e61-41ec-bce6-d04f9976f352",
            name = "Lead Change Alert",
            description = "Gives Warning Message",
            area = "Leads Management",
            type = CatalogRequest.Type.WAIT_FOR_EVENT)
    @Field.Desc(name = "LeadResponse", type = "{ Lead Name: Text, Lead Id: Text, Lead Title: Text, Lead Company: Text, Lead Website: Text, Lead Owner Id: Text, Lead Is Converted: Text, Lead Converted Account Id: Text, Lead Converted Contact Id: Text, Lead Converted Opportunity Id: Text, Lead Clean Status: Text, Lead Phone: Text, Lead MobilePhone: Text, Lead Email: Email, Lead Description: Paragraph, Lead Status: Text, Lead LeadSource: Text, Lead Industry: Text, Lead Rating: Text, Lead Annual Revenue: Number, Lead Converted Date: Date, Lead Created Date: Date, Lead Last Modified Date: Date }", required = false)
    public Map<String, Object> leadChangeAlert(
            @Field(name = "eventName", type = "Text") String eventName,
            @Field(name = "eventData", type = "FreeForm") FreeForm eventData) {
        try {
            Map<String, Object> leadMap = new HashMap<>();
            if (eventName.equals("Lead Change Alert")) {
                leadMap.put("Lead Id", eventData.get("Id"));
                setLeadDetails(eventData, leadMap);

                String query = "SELECT Fields(ALL) FROM LeadHistory WHERE LeadId = '" +
                        eventData.get("Id") + "' ORDER BY CreatedDate DESC LIMIT 20";
                Map<String, Object> fetched = leadDao.getLeadsUpdatedFields(query); //only uodated values

                if (fetched.size() != 1 || !fetched.containsKey(CREATED)) {
                    System.out.println("In Update");
                    if (fetched.containsKey("FirstName") || fetched.containsKey("LastName")) {
                        System.out.println("In name update");
                        fetched.put("Name", eventData.get("Name"));
                    } else {
                        fetched.put("Name", "");
                    }
                    setLeadDetails(fetched, leadMap);

                }
                return leadMap;
            }
            return leadMap;
        } catch (IllegalArgumentException cause) {
            System.err.println(cause.getMessage());
            cause.printStackTrace();
            return Map.of();
        }
    }


    private static void setLeadDetails(Map<String, Object> leadData, Map<String, Object> leadMap) {
        leadMap.put("Lead Name", leadData.get("Name"));
        leadMap.put("Lead Title", getValidatedString(leadData.get("Title")));
        leadMap.put("Lead Company", getValidatedString(leadData.get("Company")));
        leadMap.put("Lead Website", getValidatedString(leadData.get("Website")));
        leadMap.put("Lead Owner Id", getValidatedString(leadData.get("OwnerId")));
        leadMap.put("Lead Is Converted", getValidatedString(leadData.get("IsConverted")));
        leadMap.put("Lead Converted Account Id", getValidatedString(leadData.get("ConvertedAccountId")));
        leadMap.put("Lead Converted Contact Id", getValidatedString(leadData.get("ConvertedContactId")));
        leadMap.put("Lead Converted Opportunity Id", getValidatedString(leadData.get("ConvertedOpportunityId")));
        leadMap.put("Lead Clean Status", getValidatedString(leadData.get("CleanStatus")));
        leadMap.put("Lead Phone", getValidatedString(leadData.get("Phone")));
        leadMap.put("Lead MobilePhone", getValidatedString(leadData.get("MobilePhone")));
        leadMap.put("Lead Email", getValidatedString(leadData.get("Email")));
        leadMap.put("Lead Description", getValidatedString(leadData.get("Description")));
        leadMap.put("Lead Status", getValidatedString(leadData.get("Status")));
        leadMap.put("Lead LeadSource", getValidatedString(leadData.get("LeadSource")));
        leadMap.put("Lead Industry", getValidatedString(leadData.get("Industry")));
        leadMap.put("Lead Rating", getValidatedString(leadData.get("Rating")));
        leadMap.put("Lead Annual Revenue", getValidatedDouble(leadData.get("AnnualRevenue")));
        leadMap.put("Lead Created Date", leadData.get("CreatedDate") != null
                ? Long.parseLong((String) leadData.get("CreatedDate")) : null);
        leadMap.put("Lead Converted Date", leadData.get("ConvertedDate") != null
                ? Objects.requireNonNull(DatePlus.fromYYYYMMDD(leadData.get("ConvertedDate").toString())).getTime()
                : null);
        leadMap.put("Lead Last Modified Date", leadData.get("LastModifiedDate") != null
                ? Long.parseLong((String) leadData.get("LastModifiedDate")) : null);

    }


}
