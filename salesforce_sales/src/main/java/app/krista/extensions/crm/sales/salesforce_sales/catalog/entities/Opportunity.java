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

package app.krista.extensions.crm.sales.salesforce_sales.catalog.entities;


import app.krista.extension.impl.anno.*;

import java.util.LinkedHashMap;
import java.util.Map;

@Searchable
@Domain(id = "catEntryDomain_086f33da-aaea-4d0c-816f-3d5f20b4a273",
        name = "Sales",
        ecosystemId = "catEntryEcosystem_8a0e8ce7-6289-492f-9202-52284024280c",
        ecosystemName = "CRM",
        ecosystemVersion = "db320c64-6f6f-4c9e-8daa-41d3f796d6a6")
@Entity(name = "Opportunity", id = "localDomainEntity_011cea38-c7d1-40c1-99e3-295ab76af557", primaryKey = "Id", supportStore = true, description = "Represents a sales opportunity in Salesforce. Belongs to an Account (via Account Id referencing Account.Id). Owner Id references User.Id. Owner Name matches User.Name. May have Quotes where Quote.Opportunity Id matches Opportunity.Id. Stage Name represents the current sales pipeline stage.")
public class Opportunity {

    @Searchable
    @ToString
    @Field(name = "Name", type = "Text", description = "Name of the opportunity.")
    public String name;

    @Searchable
    @Field(name = "Id", type = "Text", required = false, description = "Unique Salesforce identifier for the opportunity. Referenced by Quote.Opportunity Id.")
    public String id;

    @Field(name = "Contact Id", type = "Text", required = false, description = "Primary contact for the opportunity.")
    public String contactId;

    @Searchable
    @Field(name = "Owner Id", type = "Text", required = false, description = "Salesforce User Id of the opportunity owner. References User.Id.")
    public String ownerId;

    @Custom
    @Searchable
    @Field(name = "Owner Name", type = "Text", required = false, description = "Display name of the opportunity owner. Matches User.Name.")
    public String ownerName;

    @Searchable
    @Field(name = "Account Id", type = "Text", required = false, description = "Id of the associated account. References Account.Id.")
    public String accountId;

    @Custom
    @Searchable
    @Field(name = "Account Name", type = "Text", required = false, description = "Name of the associated account. Matches Account.Name.")
    public String accountName;

    @Custom
    @Searchable
    @Field(name = "Type", type = "Text", required = false, description = "Type of opportunity.")
    public String type;

    @Custom
    @Searchable
    @Field(name = "Lead Source", type = "Text", required = false, description = "Source of the opportunity.")
    public String leadSource;

    @Field(name = "Pricebook Id", type = "Text", required = false, description = "Associated pricebook. References PricebookEntry.Pricebook Id.")
    public String pricebookId;

    @Searchable
    @ToString
    @Field.PickOne(name = "Stage Name", values = {"Appointment scheduled","Qualified to buy","Presentation scheduled","Decision maker bought-in","Contract sent","Closed won","Closed lost"}, required = true, description = "Current stage in the sales pipeline.", attributes = {}, options = {})
    public String stageName;

    @Custom
    @Searchable
    @Field(name = "Next Step", type = "Text", required = false, description = "Next step in the sales process.")
    public String nextStep;

    @Searchable
    @ToString
    @Field(name = "Amount", type = "Currency", required = false, description = "Total amount of the opportunity.")
    public Double amount;

    @Custom
    @Searchable
    @Field(name = "Acv", type = "Currency", required = false, description = "Annual contract value.")
    public Double acv;

    @Custom
    @Searchable
    @Field(name = "Partner", type = "Text", required = false, description = "Partner associated with the opportunity.")
    public String partner;

    @Custom
    @Searchable
    @Field(name = "Competition", type = "Text", required = false, description = "Competitor information.")
    public String competition;

    @Searchable
    @Field(name = "Probability", type = "Percentage", required = false, description = "Probability of closing the opportunity (0-100).")
    public Double probability;

    @Field(name = "Description", type = "Paragraph", required = false, description = "Detailed description of the opportunity.")
    public String description;

    @Field(name = "Expected Revenue", type = "Currency", required = false, description = "Expected revenue (Amount * Probability).")
    public Double expectedRevenue;

    @Searchable
    @Field(name = "Last Modified Date", type = "Date", required = false, description = "Date when the opportunity was last modified.", attributes = @Attribute(name = "includeTimeOfDay", value = "true"))
    public Long lastModifiedDate;

    @Searchable
    @Field(name = "Created Date", type = "Date", required = false, description = "Date when the opportunity was created.", attributes = @Attribute(name = "includeTimeOfDay", value = "true"))
    public Long createdDate;

    @Searchable
    @ToString
    @Field(name = "Close Date", type = "Date", description = "Expected close date of the opportunity.")
    public Long closeDate;

    public static Opportunity fromFields(Map<String, Object> opportunity) {
        Opportunity newOpportunity = new Opportunity();
        newOpportunity.id = (String) opportunity.get("Id");
        newOpportunity.lastModifiedDate = (Long) opportunity.get("Last Modified Date");
        newOpportunity.description = (String) opportunity.get("Description");
        newOpportunity.expectedRevenue = (Double) opportunity.get("Expected Revenue");
        newOpportunity.ownerId = (String) opportunity.get("Owner Id");
        newOpportunity.stageName = (String) opportunity.get("Stage Name");
        newOpportunity.amount = (Double) opportunity.get("Amount");
        newOpportunity.accountId = (String) opportunity.get("Account Id");
        newOpportunity.pricebookId = (String) opportunity.get("Pricebook Id");
        newOpportunity.probability = (Double) opportunity.get("Probability");
        newOpportunity.createdDate = (Long) opportunity.get("Created Date");
        newOpportunity.contactId = (String) opportunity.get("Contact Id");
        if (opportunity.get("Close Date") != null && (Long) opportunity.get("Close Date") > 0)
            newOpportunity.closeDate = (Long) opportunity.get("Close Date");
        newOpportunity.name = (String) opportunity.get("Name");
        return newOpportunity;
    }

    public Map<String, Object> toFields() {
        Map<String, Object> fieldMap = new LinkedHashMap<>();
        fieldMap.put("Name", name);
        fieldMap.put("Id", id);
        fieldMap.put("Contact Id", contactId);
        fieldMap.put("Owner Id", ownerId);
        fieldMap.put("Owner Name", ownerName);
        fieldMap.put("Account Id", accountId);
        fieldMap.put("Account Name", accountName);
        fieldMap.put("Type", type);
        fieldMap.put("Lead Source", leadSource);
        fieldMap.put("Pricebook Id", pricebookId);
        fieldMap.put("Stage Name", stageName);
        fieldMap.put("Next Step", nextStep);
        fieldMap.put("Amount", amount);
        fieldMap.put("Acv", acv);
        fieldMap.put("Partner", partner);
        fieldMap.put("Competition", competition);
        fieldMap.put("Probability", probability);
        fieldMap.put("Description", description);
        fieldMap.put("Expected Revenue", expectedRevenue);
        fieldMap.put("Last Modified Date", lastModifiedDate);
        fieldMap.put("Created Date", createdDate);
        fieldMap.put("Close Date", closeDate);
        return fieldMap;
    }

}
