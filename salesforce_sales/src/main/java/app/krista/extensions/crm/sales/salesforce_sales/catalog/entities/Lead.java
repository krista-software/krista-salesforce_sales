package app.krista.extensions.crm.sales.salesforce_sales.catalog.entities;


import app.krista.extension.impl.anno.*;
import app.krista.model.base.PhoneNumber;

import java.util.Map;

@Domain(id = "catEntryDomain_086f33da-aaea-4d0c-816f-3d5f20b4a273",
        name = "Sales",
        ecosystemId = "catEntryEcosystem_8a0e8ce7-6289-492f-9202-52284024280c",
        ecosystemName = "CRM",
        ecosystemVersion = "db320c64-6f6f-4c9e-8daa-41d3f796d6a6")
@Entity(name = "Lead", id = "localDomainEntity_8a95a02d-5afb-4fac-ae32-0976885c0446", primaryKey = "Id", supportStore = false)
public class Lead {

    @Field(name = "Id", type = "Text", required = false)
    public String id;

    @Field(name = "Title", type = "Text", required = false)
    public String title;

    @Field(name = "Company", type = "Text")
    public String company;

    @Field(name = "Website", type = "Text", required = false)
    public String website;

    @Searchable
    @Field(name = "Owner Id", type = "Text", required = false)
    public String ownerId;

    @Field(name = "Is Converted", type = "Switch", required = false)
    public Boolean isConverted;

    @Field(name = "Converted Account Id", type = "Text", required = false)
    public String convertedAccountId;

    @Field(name = "Converted Contact Id", type = "Text", required = false)
    public String convertedContactId;

    @Field(name = "Converted Opportunity Id", type = "Text", required = false)
    public String convertedOpportunityId;

    @Field(name = "Clean Status", type = "Text", required = false)
    public String cleanStatus;

    @Field(name = "Phone", type = "Phone", required = false)
    public PhoneNumber phone;

    @Field(name = "MobilePhone", type = "Phone", required = false)
    public PhoneNumber mobilePhone;

    @Field(name = "Email", type = "Email", required = false)
    public String email;

    @Field(name = "Description", type = "Paragraph", required = false)
    public String description;

    @Searchable
    @Field(name = "Status", type = "PickOne", required = false)
    public String status;

    @Field(name = "LeadSource", type = "PickOne", required = false)
    public String leadSource;

    @Field(name = "Industry", type = "PickOne", required = false)
    public String industry;

    @Field(name = "Rating", type = "PickOne", required = false)
    public String rating;

    @Field(name = "Annual Revenue", type = "Currency", required = false)
    public Double annualRevenue;

    @Field(name = "Converted Date", type = "Date", required = false)
    public Long convertedDate;

    @Field(name = "Created Date", type = "Date", required = false)
    public Long createdDate;

    @Field(name = "Last Modified Date", type = "Date", required = false)
    public Long lastModifiedDate;

    @Searchable
    @ToString
    @Field(name = "Name", type = "Text")
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

}