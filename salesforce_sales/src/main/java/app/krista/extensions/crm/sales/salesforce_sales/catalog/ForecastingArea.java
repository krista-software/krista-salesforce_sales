package app.krista.extensions.crm.sales.salesforce_sales.catalog;

import app.krista.extension.impl.anno.CatalogRequest;
import app.krista.extension.impl.anno.Domain;
import app.krista.extension.impl.anno.Field;
import app.krista.extensions.crm.sales.salesforce_sales.impl.OpportunityDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;

@Domain(id = "catEntryDomain_086f33da-aaea-4d0c-816f-3d5f20b4a273",
        name = "Sales",
        ecosystemId = "catEntryEcosystem_8a0e8ce7-6289-492f-9202-52284024280c",
        ecosystemName = "CRM",
        ecosystemVersion = "d937af1f-f3e1-4e41-98f1-5154270068ec")
public final class ForecastingArea {

    private final OpportunityDaoImpl opportunityDao;
    private static final Logger LOGGER = LoggerFactory.getLogger(ForecastingArea.class);


    @Inject
    public ForecastingArea(OpportunityDaoImpl opportunityDao) {
        this.opportunityDao = opportunityDao;
    }

    @CatalogRequest(
            id = "localDomainRequest_bc6f7b99-e9cf-43ff-b1be-54a8ddd2dc6a",
            name = "Get Sales Forecast For A Specific Quarter",
            description = "This request will return the sales forecast amount for the given Quarter filtered by a query if provided",
            area = "Forecasting",
            type = CatalogRequest.Type.QUERY_SYSTEM)
    @Field.Desc(name = "Forecast", type = "[ Composite ]", required = false)
    public List<Object> getSalesForecastForASpecificQuarter(
            @Field(name = "Quarters", type = "Checkboxes") List<String> quarters,
            @Field(name = "Query", type = "Text", required = false) String query) {
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("Query is required. Please provide a SOQL query to retrieve the sales forecast data.");
        }
        try {
            return opportunityDao.getExpectedRevenueOfQuarters(quarters, query);
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return List.of();
        }
    }

}
