package app.krista.extensions.crm.sales.salesforce_sales.catalog;

import app.krista.extension.impl.anno.CatalogRequest;
import app.krista.extension.impl.anno.Domain;
import app.krista.extension.impl.anno.Field;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Opportunity;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.SalesPipelineSummary;
import app.krista.extensions.crm.sales.salesforce_sales.impl.OpportunityDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.impl.SOQLDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.impl.SalesPipelineSummaryDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.util.DatePlus;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Domain(id = "catEntryDomain_086f33da-aaea-4d0c-816f-3d5f20b4a273",
        name = "Sales",
        ecosystemId = "catEntryEcosystem_8a0e8ce7-6289-492f-9202-52284024280c",
        ecosystemName = "CRM",
        ecosystemVersion = "db320c64-6f6f-4c9e-8daa-41d3f796d6a6")
public final class ReportingArea {

    private final OpportunityDaoImpl opportunityDao;
    private final SalesPipelineSummaryDaoImpl salesPipelineSummaryDao;
    private final SOQLDaoImpl soqlDao;

    @Inject
    private ReportingArea(OpportunityDaoImpl opportunityDao, SalesPipelineSummaryDaoImpl salesPipelineSummaryDao, SOQLDaoImpl soqlDao) {
        this.opportunityDao = opportunityDao;
        this.salesPipelineSummaryDao = salesPipelineSummaryDao;
        this.soqlDao = soqlDao;
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
            System.err.println(cause.getMessage());
            cause.printStackTrace();
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
            System.err.println(cause.getMessage());
            cause.printStackTrace();
            return null;
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
            System.err.println(cause.getMessage());
            cause.printStackTrace();
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
            System.err.println(cause.getMessage());
            cause.printStackTrace();
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
            return opportunityDao.getCurrentQuaterQuota();
        } catch (IllegalArgumentException cause) {
            System.err.println(cause.getMessage());
            cause.printStackTrace();
            return List.of();
        }
    }

    @CatalogRequest(
            id = "localDomainRequest_80a52f28-d2e8-4d21-9631-83762b99300e",
            name = "Value Of Opportunities Closed For Current Quarter",
            description = "This request will return the values of Opportunities closed for the current quarter filtered by owner Id if provided",
            area = "Reporting",
            type = CatalogRequest.Type.QUERY_SYSTEM)
    @Field.Desc(name = "Quarter Revenue", type = "[ Entity(Opportunity) ]", required = false)
    public List<Opportunity> valueOfOpportunitiesClosedForCurrentQuarter(
            @Field(name = "Owner Id", type = "Text", required = false) String ownerId) {
        try {
            if (ownerId != null && !ownerId.isEmpty() && !ownerId.isBlank()) {
                return opportunityDao.getQuaterClosedSumByOwnerId(ownerId);
            } else {
                Date today = new Date();

                String startDate = DatePlus.getISODate(new Date(today.getTime() - 600 * 1000).getTime());
                String endDate = DatePlus.getISODate(today.getTime());

                String queryLimited = "Name, StageName, Amount, closeDate, probability, Description, " +
                        "ExpectedRevenue, LastModifiedDate, createdDate";
                String query = "Select " + queryLimited + " from Opportunity where lastModifiedDate >= "
                        + startDate + " and lastModifiedDate <= " + endDate + " and stageName LIKE 'Closed won%'";

                return opportunityDao.getOpportunitiesByQuery(query).stream().map(CatalogTypes::fromSFOpportunity).collect(Collectors.toList());
            }
        } catch (IllegalArgumentException cause) {
            System.err.println(cause.getMessage());
            cause.printStackTrace();
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
            System.err.println(cause.getMessage());
            cause.printStackTrace();
            return null;
        }
    }

}
