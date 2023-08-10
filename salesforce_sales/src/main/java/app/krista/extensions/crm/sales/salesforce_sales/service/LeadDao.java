package app.krista.extensions.crm.sales.salesforce_sales.service;

import java.util.List;
import java.util.Map;

public interface LeadDao {

    /**
     * Returns {@link Lead} object of salesforce from given lead id provided.
     * @param id id of lead to be searched
     * @return {@link Lead} object of salesforce
     */
    Lead getLeadById(String id);

    /**
     * Returns {@link List<Lead>} list of object of salesforce from given lead owner id provided.
     * @param ownerId owner id of lead to be searched
     * @return {@link List<Lead>} list of object of salesforce
     * @return empty list if does not find any records
     */
    List<Lead> getLeadsByOwnerId(String ownerId);

    /**
     * Returns {@link List<Lead>} list of object of salesforce from given date provided.
     * @param date date from which leads are created
     * @return {@link List<Lead>} list of object of salesforce
     * @return empty list if does not find any records
     */
    List<Lead> getLeadByDate(long date);

    /**
     * Returns {@link List<Lead>} list of object of salesforce.
     * @param size number of lead to be fetched
     * @param page page number to be fetched
     * @return {@link List<Lead>} list of object of salesforce
     * @return empty list if does not find any records
     */
    List<Lead> getLeads(long size, long page);

    /**
     * Returns {@link List<Lead>} list of object of salesforce.
     * @param query query to be searched
     * @return {@link List<Lead>} list of object of salesforce
     * @return empty list if does not find any records
     */
    List<Lead> getLeadsByQuery(String query);

    /**
     * Add {@link Lead} to salesforce from provided input fields
     * @param entityAttributeField fields which are added in the {@link Lead} object of salesforce to create
     */
    void addLead(Map<String, Object> entityAttributeField);

    /**
     * Update {@link Lead} to salesforce from provided input fields
     * @param lead {@link Lead} object of salesforce to be updated
     */
    void updateLead(Lead lead);

    /**
     * This function will return updated fields for lead object.
     * @param query
     * @return
     */
    Map<String, Object> getLeadsUpdatedFields(String query);

    /**
     * This function will delete lead from lead object.
     * @param key 
     */
    void deleteLead(String key);
}
