package app.krista.extensions.crm.sales.salesforce_sales.catalog;

import app.krista.extension.impl.anno.CatalogRequest;
import app.krista.extension.impl.anno.Domain;
import app.krista.extension.impl.anno.Field;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Quote;
import app.krista.extensions.crm.sales.salesforce_sales.impl.PriceBookEntryDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.impl.QuoteDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.impl.QuoteImpl;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFQuote;
import app.krista.model.base.FreeForm;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@Domain(id = "catEntryDomain_086f33da-aaea-4d0c-816f-3d5f20b4a273",
        name = "Sales",
        ecosystemId = "catEntryEcosystem_8a0e8ce7-6289-492f-9202-52284024280c",
        ecosystemName = "CRM",
        ecosystemVersion = "8d29698c-f92a-4cc5-9ec2-16c2e85fe7ca")
public final class QuoteManagementArea {

    private final QuoteDaoImpl quoteDao;
    private final PriceBookEntryDaoImpl priceBookEntryDao;

    @Inject
    private QuoteManagementArea(QuoteDaoImpl quoteDao, PriceBookEntryDaoImpl priceBookEntryDao) {
        this.quoteDao = quoteDao;
        this.priceBookEntryDao = priceBookEntryDao;
    }

    @CatalogRequest(
            id = "localDomainRequest_e8451d33-24ce-46ee-a837-a1eb0f6ef309",
            name = "Get All Quote Status",
            description = "This request returns a list of status for the Quote",
            area = "Quote Management",
            type = CatalogRequest.Type.QUERY_SYSTEM)
    @Field.Desc(name = "Statuses", type = "[ Text ]", required = false)
    public List<String> getAllQuoteStatus() {
        try {
            return quoteDao.getQuoteStatuses();
        } catch (IllegalArgumentException cause) {
            System.err.println(cause.getMessage());
            cause.printStackTrace();
            return List.of();
        }
    }

    @CatalogRequest(
            id = "localDomainRequest_36a83ec0-fd48-4279-bb2c-b86ce8849c8a",
            name = "Update Quote By Quote Id",
            description = "This request will update the quote record with modified fields",
            area = "Quote Management",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field.Desc(name = "Updated Quote", type = "Entity(Quote)", required = false)
    public Quote updateQuoteByQuoteId(
            @Field(name = "Quote Id", type = "Identifier") String quoteId,
            @Field.Desc(name = "Quote Fields", type = "[ Composite ]") List<Object> quoteFields) {
        try {
            Map<String, Object> fieldMap = CatalogTypes.getFieldMapFromList(quoteFields);
            SFQuote sfQuote = SFQuote.from(fieldMap);
            sfQuote.id = quoteId;
            quoteDao.updateQuote(new QuoteImpl(sfQuote));
            return CatalogTypes.fromSFQuote(quoteDao.getQuoteById(quoteId));
        } catch (IllegalArgumentException cause) {
            System.err.println(cause.getMessage());
            cause.printStackTrace();
            return null;
        }
    }

    @CatalogRequest(
            id = "localDomainRequest_4fd823bb-13a6-452e-ba60-27f0919819dc",
            name = "Update Quote Status By Id",
            description = "This request will modify the status of the Quote",
            area = "Quote Management",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field(name = "New Status", type = "Text", required = false)
    public String updateQuoteStatusById(
            @Field(name = "Quote Id", type = "Identifier") String quoteId,
            @Field(name = "Status", type = "Text") String status) {
        try {
            Quote quote = CatalogTypes.fromSFQuote(quoteDao.getQuoteById(quoteId));
            quote.status = status;
            quoteDao.updateQuote(new QuoteImpl(SFQuote.from(quote)));
            return status;
        } catch (IllegalArgumentException cause) {
            System.err.println(cause.getMessage());
            cause.printStackTrace();
            return null;
        }
    }

    @CatalogRequest(
            id = "localDomainRequest_43f4ee2f-e787-4653-89bb-971c98cf2096",
            name = "Get The Latest Quote From Opportunity",
            description = "This request will return the Quote for the given Opportunity Id",
            area = "Quote Management",
            type = CatalogRequest.Type.QUERY_SYSTEM)
    @Field.Desc(name = "Quote", type = "Entity(Quote)", required = false)
    public Quote getTheLatestQuoteFromOpportunity(
            @Field(name = "Opportunity Id", type = "Identifier") String opportunityId) {
        try {
            return CatalogTypes.fromSFQuote(quoteDao.getQuoteByOpportunityId(opportunityId));
        } catch (IllegalArgumentException cause) {
            System.err.println(cause.getMessage());
            cause.printStackTrace();
            return null;
        }
    }

    @CatalogRequest(
            id = "localDomainRequest_5622790c-55dd-4653-9bde-5df3305fdcc2",
            name = "Add Product To A Quote",
            description = "This request will update the Quote by adding a Product with quantity and discount percent",
            area = "Quote Management",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field.Desc(name = "All Products In Quote", type = "[ Composite ]", required = false)
    public List<Object> addProductToAQuote(
            @Field(name = "Quote Id", type = "Text") String quoteId,
            @Field(name = "Pricebook Entry id", type = "Text") String pricebookEntryId,
            @Field(name = "Quantity", type = "Number") Double quantity,
            @Field(name = "Discount Percent", type = "Number", required = false) Double discountPercent) {
        try {
            quoteDao.addProductToQuote(quoteDao.getQuoteById(quoteId), priceBookEntryDao.getPriceBookEntryById(pricebookEntryId), discountPercent);
            return quoteDao.getQuoteLineItems(quoteId);
        } catch (IllegalArgumentException cause) {
            System.err.println(cause.getMessage());
            cause.printStackTrace();
            return List.of();
        }
    }

    @CatalogRequest(
            id = "localDomainRequest_0c407535-fc7b-4a03-9601-81d3e2c92945",
            name = "Get Quote Details",
            description = "This request returns a Quote for the given Id. In case of invalid input, this will return an empty Quote.",
            area = "Quote Management",
            type = CatalogRequest.Type.QUERY_SYSTEM)
    @Field(name = "Quote Details", type = "Composite", required = false)
    public FreeForm getQuoteDetails(
            @Field(name = "Quote Id", type = "Identifier") String quoteId) {
        try {
            return quoteDao.getQuoteMapById(quoteId);
        } catch (IllegalArgumentException cause) {
            System.err.println(cause.getMessage());
            cause.printStackTrace();
            return new FreeForm();
        }
    }

    @CatalogRequest(
            id = "localDomainRequest_a89d4aa4-4147-48c4-aa19-519bdc39328e",
            name = "Get Quote line Item By Quote Id",
            description = "This request will return the quote line item with important fields like quantity, price, discount, etc",
            area = "Quote Management",
            type = CatalogRequest.Type.QUERY_SYSTEM)
    @Field.Desc(name = "Quote Line Item", type = "[ Composite ]", required = false)
    public List<Object> getQuoteLineItemByQuoteId(
            @Field(name = "Quote Id", type = "Identifier") String quoteId,
            @Field.Desc(name = "Filters", type = "[ Composite ]", required = false) List<Object> filters) {
        try {
            return quoteDao.getQuoteLineItems(quoteId, filters);
        } catch (IllegalArgumentException cause) {
            System.err.println(cause.getMessage());
            cause.printStackTrace();
            return List.of();
        }
    }

}
