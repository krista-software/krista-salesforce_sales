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
import app.krista.model.base.PhoneNumber;

import java.util.LinkedHashMap;
import java.util.Map;

@Searchable
@Domain(id = "catEntryDomain_086f33da-aaea-4d0c-816f-3d5f20b4a273",
        name = "Sales",
        ecosystemId = "catEntryEcosystem_8a0e8ce7-6289-492f-9202-52284024280c",
        ecosystemName = "CRM",
        ecosystemVersion = "db320c64-6f6f-4c9e-8daa-41d3f796d6a6")
@Entity(name = "Lead", id = "localDomainEntity_8a95a02d-5afb-4fac-ae32-0976885c0446", primaryKey = "Id", supportStore = true, description = "Represents a sales lead in Salesforce. Owner Id references User.Id. When converted: Converted Account Id references Account.Id, Converted Contact Id references a Contact, Converted Opportunity Id references Opportunity.Id.")
public class Lead {

    @Field(name = "Id", type = "Text", required = false, description = "Unique Salesforce identifier for the lead.")
    public String id;

    @Field(name = "Title", type = "Text", required = false, description = "Job title of the lead.")
    public String title;

    @Field(name = "Company", type = "Text", description = "Company name of the lead.")
    public String company;

    @Field(name = "Website", type = "Text", required = false, description = "URL of the lead's website.")
    public String website;

    @Searchable
    @Field(name = "Owner Id", type = "Text", required = false, description = "Salesforce User Id of the lead owner. References User.Id.")
    public String ownerId;

    @Field(name = "Is Converted", type = "Switch", required = false, description = "Whether the lead has been converted.")
    public Boolean isConverted;

    @Field(name = "Converted Account Id", type = "Text", required = false, description = "Account created from conversion. References Account.Id.")
    public String convertedAccountId;

    @Field(name = "Converted Contact Id", type = "Text", required = false, description = "Contact created from conversion.")
    public String convertedContactId;

    @Field(name = "Converted Opportunity Id", type = "Text", required = false, description = "Opportunity created from conversion. References Opportunity.Id.")
    public String convertedOpportunityId;

    @Field(name = "Clean Status", type = "Text", required = false, description = "Data.com clean status.")
    public String cleanStatus;

    @Field(name = "Phone", type = "Phone", required = false, description = "Primary phone number.")
    public PhoneNumber phone;

    @Field(name = "MobilePhone", type = "Phone", required = false, description = "Mobile phone number.")
    public PhoneNumber mobilePhone;

    @Field(name = "Email", type = "Email", required = false, description = "Email address of the lead.")
    public String email;

    @Field(name = "Description", type = "Paragraph", required = false, description = "Detailed description of the lead.")
    public String description;

    @Searchable
    @Field(name = "Status", type = "PickOne", required = false, description = "Current status of the lead (e.g., Open, Contacted, Qualified).")
    public String status;

    @Field(name = "LeadSource", type = "PickOne", required = false, description = "Source of the lead.")
    public String leadSource;

    @Field(name = "Industry", type = "PickOne", required = false, description = "Industry of the lead's company.")
    public String industry;

    @Field(name = "Rating", type = "PickOne", required = false, description = "Lead rating (Hot, Warm, Cold).")
    public String rating;

    @Field(name = "Annual Revenue", type = "Currency", required = false, description = "Annual revenue of the lead's company.")
    public Double annualRevenue;

    @Field(name = "Converted Date", type = "Date", required = false, description = "Date when the lead was converted.")
    public Long convertedDate;

    @Field(name = "Created Date", type = "Date", required = false, description = "Date when the lead was created.")
    public Long createdDate;

    @Field(name = "Last Modified Date", type = "Date", required = false, description = "Date when the lead was last modified.")
    public Long lastModifiedDate;

    @Searchable
    @ToString
    @Field(name = "Name", type = "Text", description = "Full name of the lead.")
    public
    String name;

    public static Lead fromFields(Map<String, Object> lead) {
        Lead newLead = new Lead();
        newLead.id = (String) lead.get("Id");
        newLead.lastModifiedDate = (Long) lead.get("Last Modified Date");
        newLead.description = (String) lead.get("Description");
        newLead.ownerId = (String) lead.get("Owner Id");
        newLead.createdDate = (Long) lead.get("Created Date");
        if (lead.get("Name") != null) {
            newLead.name = (String) lead.get("Name");
        }
        newLead.isConverted = (Boolean) lead.get("Is Converted");
        newLead.annualRevenue = (Double) lead.get("Annual Revenue");
        newLead.cleanStatus = (String) lead.get("Clean Status");
        newLead.company = (String) lead.get("Company");
        newLead.convertedAccountId = (String) lead.get("Converted Account Id");
        newLead.convertedContactId = (String) lead.get("Converted Contact Id");
        newLead.convertedDate = (Long) lead.get("Converted Date");
        newLead.convertedOpportunityId = (String) lead.get("Converted Opportunity Id");
        newLead.email = (String) lead.get("Email");
        newLead.industry = (String) lead.get("Industry");
        newLead.leadSource = (String) lead.get("LeadSource");
        newLead.mobilePhone = (PhoneNumber) lead.get("MobilePhone");
        newLead.phone = (PhoneNumber) lead.get("Phone");
        newLead.rating = (String) lead.get("Rating");
        newLead.title = (String) lead.get("Title");
        newLead.status = (String) lead.get("Status");
        newLead.website = (String) lead.get("Website");
        return newLead;
    }

    public Map<String, Object> toFields() {
        Map<String, Object> fieldMap = new LinkedHashMap<>();
        fieldMap.put("Id", id);
        fieldMap.put("Title", title);
        fieldMap.put("Company", company);
        fieldMap.put("Website", website);
        fieldMap.put("Owner Id", ownerId);
        fieldMap.put("Is Converted", isConverted);
        fieldMap.put("Converted Account Id", convertedAccountId);
        fieldMap.put("Converted Contact Id", convertedContactId);
        fieldMap.put("Converted Opportunity Id", convertedOpportunityId);
        fieldMap.put("Clean Status", cleanStatus);
        fieldMap.put("Phone", phone);
        fieldMap.put("MobilePhone", mobilePhone);
        fieldMap.put("Email", email);
        fieldMap.put("Description", description);
        fieldMap.put("Status", status);
        fieldMap.put("LeadSource", leadSource);
        fieldMap.put("Industry", industry);
        fieldMap.put("Rating", rating);
        fieldMap.put("Annual Revenue", annualRevenue);
        fieldMap.put("Converted Date", convertedDate);
        fieldMap.put("Created Date", createdDate);
        fieldMap.put("Last Modified Date", lastModifiedDate);
        fieldMap.put("Name", name);
        return fieldMap;
    }

}
