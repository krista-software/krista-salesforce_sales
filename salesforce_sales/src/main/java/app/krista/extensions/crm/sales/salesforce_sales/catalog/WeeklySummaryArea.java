package app.krista.extensions.crm.sales.salesforce_sales.catalog;

import app.krista.extension.impl.anno.CatalogRequest;
import app.krista.extension.impl.anno.Domain;
import app.krista.extension.impl.anno.Field;
import app.krista.extensions.crm.sales.salesforce_sales.impl.OpportunityDaoImpl;

import javax.inject.Inject;

@Domain(id = "catEntryDomain_086f33da-aaea-4d0c-816f-3d5f20b4a273",
        name = "Sales",
        ecosystemId = "catEntryEcosystem_8a0e8ce7-6289-492f-9202-52284024280c",
        ecosystemName = "CRM",
        ecosystemVersion = "db320c64-6f6f-4c9e-8daa-41d3f796d6a6")
public final class WeeklySummaryArea {

    private final OpportunityDaoImpl opportunityDao;

    @Inject
    public WeeklySummaryArea(OpportunityDaoImpl opportunityDao) {
        this.opportunityDao = opportunityDao;
    }

    @CatalogRequest(
            id = "localDomainRequest_8e91880b-7695-454a-8044-1a6f02c41c59",
            name = "Get Revenue Closed In Current Quarter",
            description = "This request will return the 'Revenue closed' amount in the current quarter",
            area = "Weekly Summary",
            type = CatalogRequest.Type.QUERY_SYSTEM)
    @Field(name = "Closed Revenue", type = "Number", required = false)
    public Double getRevenueClosedInCurrentQuarter() {
        try {
            return opportunityDao.getRevenue(true);
        } catch (IllegalArgumentException cause) {
            System.err.println(cause.getMessage());
            cause.printStackTrace();
            return null;
        }
    }

    @CatalogRequest(
            id = "localDomainRequest_acf7b729-294e-4300-a5e4-d79a832819d9",
            name = "Get Revenue Expected To Close In Current Quarter",
            description = "This request will return the 'Revenue Expected To Close' amount in the current quarter",
            area = "Weekly Summary",
            type = CatalogRequest.Type.QUERY_SYSTEM)
    @Field(name = "Expected Revenue", type = "Number", required = false)
    public Double getRevenueExpectedToCloseInCurrentQuarter() {
        try {
            return opportunityDao.getRevenue(false);
        } catch (IllegalArgumentException cause) {
            System.err.println(cause.getMessage());
            cause.printStackTrace();
            return null;
        }
    }

}
