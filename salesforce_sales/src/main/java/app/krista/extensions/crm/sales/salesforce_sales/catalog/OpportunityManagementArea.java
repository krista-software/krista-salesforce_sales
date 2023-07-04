package app.krista.extensions.crm.sales.salesforce_sales.catalog;

import app.krista.extension.impl.anno.CatalogRequest;
import app.krista.extension.impl.anno.Domain;
import app.krista.extension.impl.anno.Field;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.LatestPosts;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Opportunity;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.User;
import app.krista.extensions.crm.sales.salesforce_sales.impl.OpportunityDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.impl.OpportunityImpl;
import app.krista.extensions.crm.sales.salesforce_sales.impl.UserDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFOpportunity;
import app.krista.extensions.crm.sales.salesforce_sales.model.WhenAnImportantOpportunityStatusChangesResponse;
import app.krista.model.field.util.FieldValues;
import com.fasterxml.jackson.core.JsonProcessingException;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Domain(id = "catEntryDomain_086f33da-aaea-4d0c-816f-3d5f20b4a273",
        name = "Sales",
        ecosystemId = "catEntryEcosystem_8a0e8ce7-6289-492f-9202-52284024280c",
        ecosystemName = "CRM",
        ecosystemVersion = "db320c64-6f6f-4c9e-8daa-41d3f796d6a6")
public final class OpportunityManagementArea {

    private final OpportunityDaoImpl opportunityDao;
    private final UserDaoImpl userDao;

    @Inject
    private OpportunityManagementArea(OpportunityDaoImpl opportunityDao, UserDaoImpl userDao) {
        this.opportunityDao = opportunityDao;
        this.userDao = userDao;
    }

    @CatalogRequest(
            id = "localDomainRequest_04141a55-ba74-48eb-b6ea-56036263683c",
            name = "Get Opportunities",
            description = "This request will return the list of Opportunities filtered by the given SOQL query or the last day's",
            area = "Opportunity Management",
            type = CatalogRequest.Type.QUERY_SYSTEM)
    @Field.Desc(name = "Opportunities", type = "[ Entity(Opportunity) ]", required = false)
    public List<Opportunity> getOpportunities(
            @Field(name = "SOQL Query", type = "Text", required = false) String sOQLQuery,
            @Field(name = "Last Days", type = "Number", required = false) Double lastDays) {
        try {
            if (sOQLQuery != null && !sOQLQuery.isEmpty()) {
                return opportunityDao.getOpportunitiesByQuery(sOQLQuery).stream().map(CatalogTypes::fromSFOpportunity).collect(Collectors.toList());
            } else if (lastDays != null && lastDays >= 0) {
                return opportunityDao.getNewOpportunitiesByLastDays(lastDays).stream().map(CatalogTypes::fromSFOpportunity).collect(Collectors.toList());
            } else {
                throw new IllegalArgumentException("Input is missing / wrong. Please provide correct input");
            }
        } catch (IllegalArgumentException cause) {
            System.err.println(cause.getMessage());
            cause.printStackTrace();
            return List.of();
        }
    }

    @CatalogRequest(
            id = "localDomainRequest_a0e737a6-d5f2-441d-a28c-b3adb54e15a5",
            name = "Update Opportunity Status By Name",
            description = "This request will update the status of all the Opportunities filtered by the given name",
            area = "Opportunity Management",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field(name = "Status", type = "Switch", required = false)
    public Boolean updateOpportunityStatusByName(
            @Field(name = "Opportunity Name", type = "Text") String opportunityName,
            @Field(name = "Status", type = "Text") String status) {
        if (opportunityName == null || opportunityName.isBlank()) {
            throw new IllegalArgumentException("Opportunity Name cannot be empty");
        }
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("Status cannot be empty");
        }
        try {
            opportunityDao.updateOpportunityStatus(opportunityName, status);
            return true;
        } catch (IllegalArgumentException cause) {
            System.err.println(cause.getMessage());
            cause.printStackTrace();
            return false;
        }
    }

    @CatalogRequest(
            id = "localDomainRequest_321cc51b-c4f2-4c2f-bdb7-c2b2a725035d",
            name = "Update Status Of Opportunities",
            description = "This request will change the status of given Opportunities",
            area = "Opportunity Management",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field.Desc(name = "Updated Opportunities", type = "[ Entity(Opportunity) ]", required = false)
    public List<Opportunity> updateStatusOfOpportunities(
            @Field.Desc(name = "Opportunities", type = "[ Entity(Opportunity) ]") List<Opportunity> opportunities,
            @Field(name = "Status Or Stage Name", type = "Text") String statusOrStageName) {
        try {
            List<Opportunity> updatedOpportunity = new ArrayList<>();
            for (Opportunity o : opportunities) {
                Opportunity updateOpportunity = new Opportunity();
                updateOpportunity.id = o.id;
                updateOpportunity.stageName = statusOrStageName;
                opportunityDao.updateOpportunity(new OpportunityImpl(SFOpportunity.from(updateOpportunity)));
                Opportunity opportunity = CatalogTypes.fromSFOpportunity(opportunityDao.getOpportunityById(updateOpportunity.id));
                updatedOpportunity.add(opportunity);
            }
            return updatedOpportunity;
        } catch (IllegalArgumentException cause) {
            System.err.println(cause.getMessage());
            cause.printStackTrace();
            return List.of();
        }
    }

    @CatalogRequest(
            id = "localDomainRequest_361c25b2-fee0-480c-b80d-71633a099a28",
            name = "Update An Opportunity",
            description = "This request will modify the Opportunity with the given input parameters",
            area = "Opportunity Management",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field.Desc(name = "Updated Opportunity", type = "Entity(Opportunity)", required = false)
    public Opportunity updateAnOpportunity(
            @Field(name = "Opportunity Id", type = "Text") String opportunityId,
            @Field(name = "Opportunity Name", type = "Text", required = false) String opportunityName,
            @Field(name = "Close Date", type = "Date", required = false) Long closeDate,
            @Field(name = "Stage Name", type = "Text", required = false) String stageName,
            @Field(name = "Amount", type = "Number", required = false) Double amount,
            @Field(name = "Description", type = "Paragraph", required = false) String description) {

        try {
            Opportunity updateOpportunity = new Opportunity();
            updateOpportunity.id = opportunityId;
            if (opportunityName != null) {
                updateOpportunity.name = opportunityName;
            }
            if (closeDate != null) {
                updateOpportunity.closeDate = closeDate;
            }
            if (stageName != null) {
                updateOpportunity.stageName = stageName;
            }
            if (amount != null) {
                updateOpportunity.amount = amount;
            }
            if (description != null) {
                updateOpportunity.description = description;
            }
            opportunityDao.updateOpportunity(new OpportunityImpl(SFOpportunity.from(updateOpportunity)));
            return CatalogTypes.fromSFOpportunity(opportunityDao.getOpportunityById(opportunityId));
        } catch (IllegalArgumentException cause) {
            System.err.println(cause.getMessage());
            cause.printStackTrace();
            return null;
        }
    }

    @CatalogRequest(
            id = "localDomainRequest_1dd3643c-e07d-4c9c-b5db-f417b0b50370",
            name = "Get My Details",
            description = "This request will return the currently logged-in user's profile details",
            area = "Opportunity Management",
            type = CatalogRequest.Type.QUERY_SYSTEM)
    @Field.Desc(name = "User", type = "Entity(User)", required = false)
    public User getMyDetails() {
        try {
            return CatalogTypes.fromSFUser(userDao.getMe());
        } catch (IllegalArgumentException cause) {
            System.err.println(cause.getMessage());
            cause.printStackTrace();
            return null;
        }
    }

    @CatalogRequest(
            id = "localDomainRequest_bc43c130-62ae-4384-9ece-f5f6b0a9136d",
            name = "Opportunity Change Alert",
            description = "This request will get triggered for any changes made in the Opportunities",
            area = "Opportunity Management",
            type = CatalogRequest.Type.WAIT_FOR_EVENT)
    @Field.Desc(name = "Response", type = "{ Opportunity Id: Identifier, Previous Status: Text, New Status: Text, Amount: Currency }", required = false)
    public Map<String, Object> opportunityChangeAlert(
            @Field(name = "eventName", type = "Text") String eventName,
            @Field(name = "eventData", type = "Composite") Map<String, Object> eventData) {
        try {
            if (eventName.equalsIgnoreCase("opportunityChanged")) {
                WhenAnImportantOpportunityStatusChangesResponse whenAnImportantOpportunityStatusChangesResponse =
                        new WhenAnImportantOpportunityStatusChangesResponse();
                whenAnImportantOpportunityStatusChangesResponse.opportunityId = (String) eventData.get("Id");
                whenAnImportantOpportunityStatusChangesResponse.amount = ((Double) eventData.get("Amount")).longValue();
                whenAnImportantOpportunityStatusChangesResponse.newStatus = (String) eventData.get("StageName");
                whenAnImportantOpportunityStatusChangesResponse.previousStatus = (String) eventData.get("Name");
                String query = "Select StageName, Amount FROM OpportunityHistory where Opportunity.Id = '" + whenAnImportantOpportunityStatusChangesResponse.opportunityId + "' LIMIT 2";
                List<Opportunity> opp = opportunityDao.getOpportunitiesByQuery(query).stream().map(CatalogTypes::fromSFOpportunity).collect(Collectors.toList());
                if (opp.size() == 2) {
                    if (!opp.get(0).stageName.equals(opp.get(1).stageName)) {
                        whenAnImportantOpportunityStatusChangesResponse.previousStatus = opp.get(1).stageName;
                        return whenAnImportantOpportunityStatusChangesResponse.toFields();
                    }
                }
                whenAnImportantOpportunityStatusChangesResponse.opportunityId = "";
                whenAnImportantOpportunityStatusChangesResponse.amount = 0L;
                whenAnImportantOpportunityStatusChangesResponse.newStatus = "";
                whenAnImportantOpportunityStatusChangesResponse.previousStatus = "";
                return whenAnImportantOpportunityStatusChangesResponse.toFields();
            }
            return Map.of();
        } catch (IllegalArgumentException cause) {
            System.err.println(cause.getMessage());
            cause.printStackTrace();
            return Map.of();
        }
    }

    @CatalogRequest(
            id = "localDomainRequest_e4b6fee9-1510-4d9d-8486-12ea92d3f6fa",
            name = "Get Latest Posts For Opportunity",
            description = "This request will retrieve the latest posts from Salesforce Sales Opportunity feed.",
            area = "Opportunity Management",
            type = CatalogRequest.Type.QUERY_SYSTEM)
    @Field.Desc(name = "Posts", type = "[ Entity(Latest Posts) ]", required = false)
    public List<LatestPosts> getLatestPostsForOpportunity(
            @Field.Text(name = "Opportunity Id", required = true, attributes = {}, options = {}) String opportunityId,
            @Field(name = "Number Of Posts", type = "Number", required = false, attributes = {}, options = {}) Double numberOfPosts) {
        try {
            int maxNumberOfPosts = 100;
            if (numberOfPosts != null && Double.isFinite(numberOfPosts)) {
                maxNumberOfPosts = Math.min(numberOfPosts.intValue(), maxNumberOfPosts);
                if (maxNumberOfPosts <= 0) {
                    throw new IllegalArgumentException("Bad Request. Number of posts must be greater than 0.");
                }
            }
            return opportunityDao.getLatestPosts(opportunityId, maxNumberOfPosts);
        } catch (IllegalArgumentException | IOException cause) {
            System.err.println("Failed to get Latest Posts" + cause.getMessage());
        }
        return List.of();
    }
}
