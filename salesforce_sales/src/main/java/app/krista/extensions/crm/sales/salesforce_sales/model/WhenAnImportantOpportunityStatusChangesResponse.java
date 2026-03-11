/*
 * Salesforce Sales Extension for Krista
 * Copyright (C) 2025 Krista Software
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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