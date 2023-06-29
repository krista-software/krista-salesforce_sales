package app.krista.extensions.crm.sales.salesforce_sales.impl;

import app.krista.extensions.crm.sales.salesforce_sales.catalog.CatalogTypes;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFLead;
import app.krista.extensions.crm.sales.salesforce_sales.service.Lead;
import app.krista.extensions.crm.sales.salesforce_sales.service.LeadDao;
import app.krista.extensions.crm.sales.salesforce_sales.util.DatePlus;
import app.krista.extensions.crm.sales.salesforce_sales.util.SalesforceListResponse;
import com.google.gson.reflect.TypeToken;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.GSON;

@Service
public class LeadDaoImpl implements LeadDao {

    public static final String QUERY_FIELDS = "Id, LastName, FirstName, Salutation, Name, Title, Company, Address, Phone, MobilePhone, Fax, Email, Website, Description, LeadSource, Status, Industry, Rating, AnnualRevenue, NumberOfEmployees, OwnerId, IsConverted, ConvertedDate, ConvertedAccountId, ConvertedContactId, ConvertedOpportunityId, IsUnreadByOwner, CreatedDate, LastModifiedDate, CleanStatus";
    public static final String LEAD = "Lead";
    private final SalesforceManager salesforceManager;

    @Inject
    public LeadDaoImpl(SalesforceManager salesforceManager) {
        this.salesforceManager = salesforceManager;
    }

    @Override
    public Lead getLeadById(String id) {
        return getLeadsByQuery("Select " + QUERY_FIELDS + " from Lead where id = '" + id + "'").stream().findFirst().orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public List<Lead> getLeadsByOwnerId(String ownerId) {
        return getLeadsByQuery("Select " + QUERY_FIELDS + " from Lead where ownerId = '" + ownerId + "'");
    }

    @Override
    public List<Lead> getLeadByDate(long date) {
        return getLeadsByQuery("Select " + QUERY_FIELDS + " from Lead WHERE CreatedDate >" + DatePlus.getISODate(new Date(date)));
    }

    @Override
    public List<Lead> getLeads(long size, long page) {
        return getLeadsByQuery("Select " + QUERY_FIELDS + " from Lead ORDER BY LastModifiedDate DESC LIMIT " + size + " OFFSET " + Math.max((page - 1) * size, 0));
    }

    @Override
    public List<Lead> getLeadsByQuery(String query) {
        String response = salesforceManager.getRecordByQuery(query);
        SalesforceListResponse<SFLead> oppList =
                GSON.fromJson(response, new TypeToken<SalesforceListResponse<SFLead>>() {
                }.getType());
        if (oppList.records.size() > 0) {
            return oppList.records.stream().map(LeadImpl::new).collect(Collectors.toList());
        } else {
            return List.of();
        }
    }

    @Override
    public void addLead(Map<String, Object> entityAttributeField) {
        salesforceManager.createRecord(GSON.toJson(SFLead.from(entityAttributeField)), LEAD);
    }

    @Override
    public void updateLead(Lead lead) {
        if (lead.getId() == null || lead.getId().isEmpty())
            throw new IllegalArgumentException("Lead Id cant be empty");
        SFLead updatedLead = SFLead.from(CatalogTypes.fromSFLead(lead));
        SFLead oldLead = SFLead.from(CatalogTypes.fromSFLead(getLeadById(updatedLead.id)));
        Map<String, Object> diff = salesforceManager.getDifference(oldLead, updatedLead);
        salesforceManager.updateRecord(updatedLead.id, GSON.toJson(diff), LEAD);
    }

}
