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

import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Opportunity;
import app.krista.extensions.crm.sales.salesforce_sales.util.DatePlus;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.Map;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Validators.returnNullOrValue;

public final class SFOpportunity {

    public static class AttributeClass {
        public String Name;
    }

    public AttributeClass Owner;
    public AttributeClass Account;

    @SerializedName("LastModifiedDate")
    public Date lastModifiedDate;

    @SerializedName("Description")
    public String description;

    @SerializedName("ExpectedRevenue")
    public Double expectedRevenue;

    @SerializedName("OwnerId")
    public String ownerId;

    @SerializedName("NextStep")
    public String nextStep;

    @SerializedName("StageName")
    public String stageName;

    @SerializedName("Amount")
    public Double amount;

    @SerializedName("Probability")
    public Double probability;

    @SerializedName("CreatedDate")
    public Date createdDate;

    @SerializedName("Id")
    public String id;

    @SerializedName("ContactId")
    public String contactId;

    @SerializedName("AccountId")
    public String accountId;

    @SerializedName("LeadSource")
    public String leadSource;

    @SerializedName("Partner__c")
    public String partner;

    @SerializedName("Competition__c")
    public String competition;

    @SerializedName("Type")
    public String type;

    @SerializedName("CloseDate")
    public String closeDate;

    @SerializedName("Name")
    public String name;

    @SerializedName("ACV__c")
    public Double acv;

    @SerializedName("Pricebook2Id")
    public String Pricebook2Id;

    public static SFOpportunity from(Opportunity opportunity) {
        SFOpportunity sfOpportunity = new SFOpportunity();
        sfOpportunity.id = opportunity.id;
        if (opportunity.lastModifiedDate != null && opportunity.lastModifiedDate > 0)
            sfOpportunity.lastModifiedDate = new Date(opportunity.lastModifiedDate);
        sfOpportunity.description = opportunity.description;
        sfOpportunity.expectedRevenue = opportunity.expectedRevenue;
        sfOpportunity.ownerId = opportunity.ownerId;
        sfOpportunity.stageName = opportunity.stageName;
        sfOpportunity.amount = opportunity.amount;
        sfOpportunity.accountId = opportunity.accountId;
        sfOpportunity.Pricebook2Id = opportunity.pricebookId;
        sfOpportunity.probability = opportunity.probability;
        if (opportunity.createdDate != null && opportunity.createdDate > 0)
            sfOpportunity.createdDate = new Date(opportunity.createdDate);
        sfOpportunity.contactId = opportunity.contactId;
        if (opportunity.closeDate != null && opportunity.closeDate > 0)
            sfOpportunity.closeDate = DatePlus.getYYYYMMDD(opportunity.closeDate);
        sfOpportunity.name = opportunity.name;
        return sfOpportunity;
    }

    public static SFOpportunity from(Map<String, Object> entityAttributeField) {
        SFOpportunity opportunity = new SFOpportunity();
        Long lastModifiedDate = (Long) entityAttributeField.get("Last Modified Date");
        opportunity.lastModifiedDate = lastModifiedDate != null && lastModifiedDate > 0 ? new Date(lastModifiedDate) : null;
        opportunity.description = returnNullOrValue((String) entityAttributeField.get("Description"));
        opportunity.expectedRevenue = (Double) entityAttributeField.get("Expected Revenue");
        opportunity.ownerId = returnNullOrValue((String) entityAttributeField.get("Owner Id"));
        opportunity.stageName = returnNullOrValue((String) entityAttributeField.get("Stage Name"));
        opportunity.amount = (Double) entityAttributeField.get("Amount");
        opportunity.accountId = returnNullOrValue((String) entityAttributeField.get("Account Id"));
        opportunity.Pricebook2Id = returnNullOrValue((String) entityAttributeField.get("Pricebook Id"));
        opportunity.probability = (Double) entityAttributeField.get("Probability");
        Long createdDate = (Long) entityAttributeField.get("Created Date");
        opportunity.createdDate = createdDate != null && createdDate > 0 ? new Date(createdDate) : null;
        opportunity.contactId = returnNullOrValue((String) entityAttributeField.get("Contact Id"));
        Long closeDate = (Long) entityAttributeField.get("Close Date");
        opportunity.closeDate = closeDate != null && closeDate > 0 ? DatePlus.getYYYYMMDD(closeDate) : null;
        opportunity.name = returnNullOrValue((String) entityAttributeField.get("Name"));
        return opportunity;
    }
}