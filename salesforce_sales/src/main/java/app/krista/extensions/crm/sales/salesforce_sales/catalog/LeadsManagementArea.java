package app.krista.extensions.crm.sales.salesforce_sales.catalog;

import app.krista.extension.impl.anno.CatalogRequest;
import app.krista.extension.impl.anno.Domain;
import app.krista.extension.impl.anno.Field;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Lead;
import app.krista.extensions.crm.sales.salesforce_sales.impl.LeadDaoImpl;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Domain(id = "catEntryDomain_086f33da-aaea-4d0c-816f-3d5f20b4a273",
        name = "Sales",
        ecosystemId = "catEntryEcosystem_8a0e8ce7-6289-492f-9202-52284024280c",
        ecosystemName = "CRM",
        ecosystemVersion = "db320c64-6f6f-4c9e-8daa-41d3f796d6a6")
public final class LeadsManagementArea {

    private final LeadDaoImpl leadDao;

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
}
