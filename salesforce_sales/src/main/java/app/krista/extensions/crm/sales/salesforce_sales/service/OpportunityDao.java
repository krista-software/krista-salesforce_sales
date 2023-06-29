package app.krista.extensions.crm.sales.salesforce_sales.service;

import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.LatestPosts;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface OpportunityDao {

    /**
     * Returns {@link Opportunity} object of salesforce from given opportunity id provided.
     * @param id id of opportunity to be searched
     * @return {@link Opportunity} object of salesforce
     */
    Opportunity getOpportunityById(String id);

    /**
     * Returns {@link List<Opportunity>} list of object of salesforce.
     * @param size number of opportunity to be fetched
     * @param page page number to be fetched
     * @return {@link List<Opportunity>} list of object of salesforce
     * @return empty list if does not find any records
     */
    List<Opportunity> getOpportunities(long size, long page);

    /**
     * Returns {@link List<Opportunity>} list of object of salesforce.
     * @param query query to be searched
     * @return {@link List<Opportunity>} list of object of salesforce
     * @return empty list if does not find any records
     */
    List<Opportunity> getOpportunitiesByQuery(String query);

    /**
     * Returns {@link List<Opportunity>} list of object of salesforce.
     * @param days days from which opportunity to be searched
     * @return {@link List<Opportunity>} list of object of salesforce
     * @return empty list if does not find any records
     */
    List<Opportunity> getNewOpportunitiesByLastDays(Double days);

    /**
     * Returns List of map of a RegionWiseSummary object of salesforce.
     * @return List of map of a RegionWiseSummary object of salesforce
     * @return empty list when exception occurred
     */
    List<Map<String, Object>> getOpportunitiesRegionWise();

    /**
     * Update {@link Opportunity} to salesforce from provided input fields
     * @param opportunity {@link Opportunity} object of salesforce to be updated
     */
    void updateOpportunity(Opportunity opportunity);

    /**
     * Update status of {@link Opportunity} to salesforce from provided opportunity name
     * @param name name of the opportunity which status want to change
     * @param status new status of opportunity
     */
    void updateOpportunityStatus(String name, String status);

    /**
     * Add {@link Opportunity} to salesforce from provided input fields
     * @param entityAttributeField fields which are added in the {@link Opportunity} object of salesforce to create
     */
    void addOpportunity(Map<String, Object> entityAttributeField);

    /**
     * Returns sum of amount of closed opportunities in current quarter
     * @return Total amount
     */
    Double getTotalAmountClosedThisQuarter();

    /**
     * Returns the sum of expected revenue of opportunity
     * @param isClosed isClosed status of opportunity
     * @return expected revenue of opportunity
     */
    Double getRevenue(boolean isClosed);

    /**
     * Returns list of map of quarter and expected revenue
     * @param quarters quarters of the year
     * @param query query to be searched
     * @return list of map of quarter and expected revenue
     */
    List<Object> getExpectedRevenueOfQuarters(List<String> quarters, String query);

    /**
     * Returns list of map of owner and expected amount
     * @param ownerId owner id of opportunity
     * @return list of map of owner and expected amount
     */
    List<Object> getYTDClosedSumByOwnerId(String ownerId);

    /**
     * Returns list of map of owner and expected amount
     * @return list of map of owner and expected amount
     */
    List<Object> getYTDClosedSum();

    /**
     * Returns sum of amount of opportunities fom given owner id provided
     * @param ownerId owner id of opportunity
     * @return sum of amount of opportunities
     */
    List getQuaterClosedSumByOwnerId(String ownerId);

    /**
     * Returns sum of amount of current quarter quota
     * @return sum of amount of current quarter quota
     */
    List<Map<String, Object>> getCurrentQuaterQuota();

    List<LatestPosts> getLatestPosts(String opportunityId, int numberOfPosts) throws IOException;
}
