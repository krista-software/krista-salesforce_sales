package app.krista.extensions.crm.sales.salesforce_sales.catalog;

import app.krista.extension.impl.anno.Attribute;
import app.krista.extension.impl.anno.CatalogRequest;
import app.krista.extension.impl.anno.Domain;
import app.krista.extension.impl.anno.Field;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes.CatalogTypes;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.SalesPipelineSummary;
import app.krista.extensions.crm.sales.salesforce_sales.impl.OpportunityDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.impl.SOQLDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.impl.SalesPipelineSummaryDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.util.Constants;
import app.krista.extensions.crm.sales.salesforce_sales.util.SFHelper;
import app.krista.extensions.util.EventHandler;
import app.krista.ksdk.context.AuthorizationContext;
import app.krista.ksdk.context.RequestContext;
import app.krista.model.base.FreeForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static app.krista.extensions.crm.sales.salesforce_sales.impl.SOQLDaoImpl.parseQueryResult;

@Domain(id = "catEntryDomain_086f33da-aaea-4d0c-816f-3d5f20b4a273",
        name = "Sales",
        ecosystemId = "catEntryEcosystem_8a0e8ce7-6289-492f-9202-52284024280c",
        ecosystemName = "CRM",
        ecosystemVersion = "7a281d96-8440-4945-a07d-a1a49caab1f2")
public final class ReportingArea {

    private final OpportunityDaoImpl opportunityDao;
    private final SalesPipelineSummaryDaoImpl salesPipelineSummaryDao;
    private final SOQLDaoImpl soqlDao;
    private final RequestContext requestContext;
    private final AuthorizationContext authorizationContext;
    private final EventHandler eventHandler;
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportingArea.class);

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Inject
    private ReportingArea(OpportunityDaoImpl opportunityDao, SalesPipelineSummaryDaoImpl salesPipelineSummaryDao,
                          SOQLDaoImpl soqlDao, RequestContext requestContext,
                          AuthorizationContext authorizationContext, EventHandler eventHandler) {
        this.opportunityDao = opportunityDao;
        this.salesPipelineSummaryDao = salesPipelineSummaryDao;
        this.soqlDao = soqlDao;
        this.requestContext = requestContext;
        this.authorizationContext = authorizationContext;
        this.eventHandler = eventHandler;
    }

    @CatalogRequest(
            id = "localDomainRequest_3a7861dc-509e-465a-861f-b0ebcae77a58",
            name = "Get Opportunities Summary",
            description = "This request will return a list of regions with the total associated amount",
            area = "Reporting",
            type = CatalogRequest.Type.QUERY_SYSTEM)
    @Field.Desc(name = "Opportunities", type = "[ { Region: Text, Amount: Number } ]", required = false)
    public List<Map<String, Object>> getOpportunitiesSummary() {
        try {
            return opportunityDao.getOpportunitiesRegionWise();
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return List.of();
        }
    }

    @CatalogRequest(
            id = "localDomainRequest_99eb9be6-9368-43c2-8585-810e8532dc69",
            name = "Process Query Request",
            description = "This request takes the SOQL query as input and returns the response with details",
            area = "Reporting",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field(name = "Response", type = "Composite", required = false)
    public Object processQueryRequest(
            @Field(name = "Query", type = "Paragraph") String query) {
        try {
            return soqlDao.getSalesforceObjectByQuery(query);
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return Collections.emptyMap();
        }
    }

    @CatalogRequest(
            id = "localDomainRequest_1ce88eaa-0d6b-48eb-9642-20104aa43a67",
            name = "Process Query Request Async",
            description = "This request takes the SOQL query as input and returns Job Id.",
            area = "Reporting",
            type = CatalogRequest.Type.QUERY_SYSTEM)
    @Field.Text(name = "Job Id", required = false, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {})
    public String processQueryRequestAsync(
            @Field(name = "SOQL Query", type = "Paragraph", required = true, attributes = {@Attribute(name = "visualWidth", value = "L")}, options = {}) String sOQLQuery) {

        boolean invokeAsUser = requestContext.invokeAsUser();
        if (invokeAsUser) {
            soqlDao.validateRefreshToken();
        }

        final String jobId = UUID.randomUUID().toString();
        String accountId = SFHelper.validateUsingOAuth(invokeAsUser, soqlDao.getSalesAttributes()) ? authorizationContext.getAuthorizedAccount().getAccountId() : null;
        executorService.submit(() -> {
            try {
                String result = soqlDao.getSalesforceObjectByQuery(sOQLQuery, invokeAsUser, accountId);
                FreeForm freeForm = new FreeForm();
                if (result != null && !result.isEmpty()) {
                    freeForm.put(Constants.DATA, "Text", result);
                }
                eventHandler.handleEvent(jobId, freeForm);
            } catch (Exception cause) {
                throw new IllegalStateException(cause);
            }
        });
        return jobId;
    }

    @CatalogRequest(
            id = "localDomainRequest_2c883b8a-8027-4426-a7a7-d30ec264e8d9",
            name = "Get Results for Process Query Request Async",
            description = "This request takes the Job Id as input and returns the generic output.",
            area = "Reporting",
            type = CatalogRequest.Type.WAIT_FOR_EVENT)
    @Field(name = "Response", type = "FreeForm", required = false, attributes = {}, options = {})
    public Object getResults(
            @Field(name = "eventName", type = "Text") String eventName,
            @Field(name = "eventData", type = "FreeForm") FreeForm eventData,
            @Field.Text(name = "Job Id", required = true, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {}) String jobId) {
        if (eventName.equals(jobId)) {
            String data = (String) eventData.get(Constants.DATA);
            if (data != null) {
                List results = new ArrayList();
                return parseQueryResult(results, data);
            } else {
                return new FreeForm();
            }
        } else {
            throw new IllegalStateException(Constants.INVALID_TASK_ID);
        }
    }

    @CatalogRequest(
            id = "localDomainRequest_dda53b5a-2172-47f0-b550-a7440c8392d9",
            name = "YTD Sales Revenue Closed",
            description = "This request will return the total sum i.e. value of Opportunities closed for the current calendar year. The filter will be applied only if the optional 'Owner Id' input is provided",
            area = "Reporting",
            type = CatalogRequest.Type.QUERY_SYSTEM)
    @Field.Desc(name = "YTD Sales Revenue Closed Sum", type = "[ Composite ]", required = false)
    public List<Object> yTDSalesRevenueClosed(
            @Field(name = "Owner Id", type = "Text", required = false) String ownerId) {

        try {
            return ownerId != null && !ownerId.isEmpty() && !ownerId.isBlank() ?
                    opportunityDao.getYTDClosedSumByOwnerId(ownerId) :
                    opportunityDao.getYTDClosedSum();
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return List.of();
        }
    }

    @CatalogRequest(
            id = "localDomainRequest_376b22fa-2c12-4c01-9801-428ebbd18ab4",
            name = "Sales Pipeline Summary",
            description = "This request will return a list of the sales pipeline with details like a stage name, average, and total",
            area = "Reporting",
            type = CatalogRequest.Type.QUERY_SYSTEM)
    @Field.Desc(name = "Sales Pipeline Summary", type = "[ Entity(Sales Pipeline Summary) ]", required = false)
    public List<SalesPipelineSummary> salesPipelineSummary() {
        try {
            return salesPipelineSummaryDao.getSalesPipelinedSummaries().stream().map(CatalogTypes::fromSFSalesPipelineSummary).collect(Collectors.toList());
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return List.of();
        }
    }

    @CatalogRequest(
            id = "localDomainRequest_a7b550aa-ff5e-4a2e-97fd-940bc4b72e3b",
            name = "Sales Quota For The Quarter",
            description = "This request will return sales quota details with the owner's name, and amount for the current quarter",
            area = "Reporting",
            type = CatalogRequest.Type.QUERY_SYSTEM)
    @Field.Desc(name = "Sales Quota", type = "[ { Owner Name: Text, Quota Amount: Number } ]", required = false)
    public List<Map<String, Object>> salesQuotaForTheQuarter() {
        try {
            return opportunityDao.getCurrentQuarterQuota();
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return List.of();
        }
    }

    @CatalogRequest(
            id = "localDomainRequest_80a52f28-d2e8-4d21-9631-83762b99300e",
            name = "Value Of Opportunities Closed For Current Quarter",
            description = "This request will retrieve the  total closed opportunities revenue within the current quarter. If an owner ID is provided, it returns the revenue for that particular owner; otherwise, it returns total revenue grouped by owners.",
            area = "Reporting",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field.Desc(name = "Quarter Revenue", type = "[ { Owner Id: Text, Total: Text, Name: Text } ]", required = false)
    public List<Map<String, Object>> valueOfOpportunitiesClosedForCurrentQuarter(
            @Field.Text(name = "Owner Id", required = false, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {}) String ownerId) {
        try {
            return opportunityDao.getQuarterClosedSum(ownerId);
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return List.of();
        }
    }

    @CatalogRequest(
            id = "localDomainRequest_f62a0559-0894-4e10-ab01-b1aa983f5c8e",
            name = "Total Potential Opportunity Value For Quarter",
            description = "This request will return the potential Opportunity amount for the current quarter",
            area = "Reporting",
            type = CatalogRequest.Type.QUERY_SYSTEM)
    @Field(name = "Projected Sum", type = "Number", required = false)
    public Double totalPotentialOpportunityValueForQuarter() {
        try {
            return opportunityDao.getTotalAmountClosedThisQuarter();
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return null;
        }
    }

}
