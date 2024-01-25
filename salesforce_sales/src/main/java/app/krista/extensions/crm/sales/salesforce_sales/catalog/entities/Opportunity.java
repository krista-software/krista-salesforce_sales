package app.krista.extensions.crm.sales.salesforce_sales.catalog.entities;


import app.krista.extension.impl.anno.*;

import java.util.Map;

@Domain(id = "catEntryDomain_086f33da-aaea-4d0c-816f-3d5f20b4a273",
        name = "Sales",
        ecosystemId = "catEntryEcosystem_8a0e8ce7-6289-492f-9202-52284024280c",
        ecosystemName = "CRM",
        ecosystemVersion = "db320c64-6f6f-4c9e-8daa-41d3f796d6a6")
@Entity(name = "Opportunity", id = "localDomainEntity_011cea38-c7d1-40c1-99e3-295ab76af557", primaryKey = "Id")
public class Opportunity {

    @Searchable
    @ToString
    @Field(name = "Name", type = "Text")
    public String name;

    @Searchable
    @Field(name = "Id", type = "Text", required = false)
    public String id;

    @Field(name = "Contact Id", type = "Text", required = false)
    public String contactId;

    @Searchable
    @Field(name = "Owner Id", type = "Text", required = false)
    public String ownerId;

    @Custom
    @Searchable
    @Field(name = "Owner Name", type = "Text", required = false)
    public String ownerName;

    @Searchable
    @Field(name = "Account Id", type = "Text", required = false)
    public String accountId;

    @Custom
    @Searchable
    @Field(name = "Account Name", type = "Text", required = false)
    public String accountName;

    @Custom
    @Searchable
    @Field(name = "Type", type = "Text", required = false)
    public String type;

    @Custom
    @Searchable
    @Field(name = "Lead Source", type = "Text", required = false)
    public String leadSource;

    @Field(name = "Pricebook Id", type = "Text", required = false)
    public String pricebookId;

    @Searchable
    @ToString
    @Field.PickOne(name = "Stage Name", values = {"Appointment scheduled","Qualified to buy","Presentation scheduled","Decision maker bought-in","Contract sent","Closed won","Closed lost"}, required = true, attributes = {}, options = {})
    public String stageName;

    @Custom
    @Searchable
    @Field(name = "Next Step", type = "Text", required = false)
    public String nextStep;

    @Searchable
    @ToString
    @Field(name = "Amount", type = "Currency", required = false)
    public Double amount;

    @Custom
    @Searchable
    @Field(name = "Acv", type = "Currency", required = false)
    public Double acv;

    @Custom
    @Searchable
    @Field(name = "Partner", type = "Text", required = false)
    public String partner;

    @Custom
    @Searchable
    @Field(name = "Competition", type = "Text", required = false)
    public String competition;

    @Searchable
    @Field(name = "Probability", type = "Percentage", required = false)
    public Double probability;

    @Field(name = "Description", type = "Paragraph", required = false)
    public String description;

    @Field(name = "Expected Revenue", type = "Currency", required = false)
    public Double expectedRevenue;

    @Searchable
    @Field(name = "Last Modified Date", type = "Date", required = false, attributes = @Attribute(name = "includeTimeOfDay", value = "true"))
    public Long lastModifiedDate;

    @Searchable
    @Field(name = "Created Date", type = "Date", required = false, attributes = @Attribute(name = "includeTimeOfDay", value = "true"))
    public Long createdDate;

    @Searchable
    @ToString
    @Field(name = "Close Date", type = "Date")
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

}