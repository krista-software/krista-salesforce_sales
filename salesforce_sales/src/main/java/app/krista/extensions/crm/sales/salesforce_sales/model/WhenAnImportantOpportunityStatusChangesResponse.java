package app.krista.extensions.crm.sales.salesforce_sales.model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public final class WhenAnImportantOpportunityStatusChangesResponse {
    @SerializedName("Opportunity Id")
    public String opportunityId;

    @SerializedName("Previous Status")
    public String previousStatus;

    @SerializedName("New Status")
    public String newStatus;

    @SerializedName("Amount")
    public Long amount;

    public Map<String, Object> toFields() {
        WhenAnImportantOpportunityStatusChangesResponse whenAnImportantOpportunityStatusChangesResponse = this;
        return Map.of("Opportunity Id", whenAnImportantOpportunityStatusChangesResponse.opportunityId,
                "Previous Status", whenAnImportantOpportunityStatusChangesResponse.previousStatus,
                "New Status", whenAnImportantOpportunityStatusChangesResponse.newStatus,
                "Amount", whenAnImportantOpportunityStatusChangesResponse.amount);
    }
}