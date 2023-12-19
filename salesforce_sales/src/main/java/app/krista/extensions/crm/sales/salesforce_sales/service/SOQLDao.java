package app.krista.extensions.crm.sales.salesforce_sales.service;

public interface SOQLDao {

    /**
     * Returns object of salesforce from given query provided
     *
     * @param query query to be searched
     * @return object of salesforce
     */
    Object getSalesforceObjectByQuery(String query);

    String getSalesforceObjectByQuery(String query, boolean invokeAsUser, String accountId);

    void validateRefreshToken();
}
