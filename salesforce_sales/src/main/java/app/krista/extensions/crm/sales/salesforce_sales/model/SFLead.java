package app.krista.extensions.crm.sales.salesforce_sales.model;

import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Lead;
import app.krista.extensions.crm.sales.salesforce_sales.util.DatePlus;
import com.google.gson.annotations.SerializedName;

import java.util.*;

public final class SFLead {
    @SerializedName("Status")
    public String status;

    @SerializedName("LastModifiedDate")
    public Date lastModifiedDate;

    @SerializedName("Company")
    public String company;

    @SerializedName("ConvertedContactId")
    public String convertedContactId;

    @SerializedName("Email")
    public String email;

    @SerializedName("Description")
    public String description;

    @SerializedName("CreatedDate")
    public Date createdDate;

    @SerializedName("Rating")
    public String rating;

    @SerializedName("Title")
    public String title;

    @SerializedName("Website")
    public String website;

    @SerializedName("AnnualRevenue")
    public Double annualRevenue;

    @SerializedName("ConvertedOpportunityId")
    public String convertedOpportunityId;

    @SerializedName("MobilePhone")
    public String mobilePhone;

    @SerializedName("Name")
    public String name;

    @SerializedName("LastName")
    public String lastName;

    @SerializedName("FirstName")
    public String firstName;

    @SerializedName("ConvertedDate")
    public String convertedDate;

    @SerializedName("ConvertedAccountId")
    public String convertedAccountId;

    @SerializedName("Industry")
    public String industry;

    @SerializedName("IsConverted")
    public Boolean isConverted;

    @SerializedName("OwnerId")
    public String ownerId;

    @SerializedName("LeadSource")
    public String leadSource;

    @SerializedName("CleanStatus")
    public String cleanStatus;

    @SerializedName("Phone")
    public String phone;

    @SerializedName("Id")
    public String id;

    public static SFLead from(Lead sfLead) {
        SFLead lead = new SFLead();
        lead.id = sfLead.id;
        if (sfLead.lastModifiedDate != null)
            lead.lastModifiedDate = new Date(sfLead.lastModifiedDate);
        lead.description = sfLead.description;
        lead.ownerId = sfLead.ownerId;
        if (sfLead.createdDate != null)
            lead.createdDate = new Date(sfLead.createdDate);
        lead.isConverted = sfLead.isConverted;
        lead.annualRevenue = sfLead.annualRevenue;
        lead.cleanStatus = sfLead.cleanStatus;
        lead.company = sfLead.company;
        lead.convertedAccountId = sfLead.convertedAccountId;
        lead.convertedContactId = sfLead.convertedContactId;
        if (sfLead.convertedDate != null && sfLead.convertedDate > 0)
            lead.convertedDate = DatePlus.getYYYYMMDD(sfLead.convertedDate);
        lead.convertedOpportunityId = sfLead.convertedOpportunityId;
        lead.email = sfLead.email;
        lead.industry = sfLead.industry;
        lead.leadSource = sfLead.leadSource;
        lead.mobilePhone = sfLead.mobilePhone.toString();
        lead.phone = sfLead.phone.toString();
        lead.rating = sfLead.rating;
        lead.title = sfLead.title;
        lead.status = sfLead.status;
        lead.website = sfLead.website;
        if (sfLead.name != null) {
            List<String> name = new ArrayList<>(Arrays.asList(sfLead.name.split("\\s+")));
            if (name.size() == 1)
                lead.lastName = sfLead.name;
            else {
                lead.firstName = name.get(0);
                name.remove(0);
                lead.lastName = String.join(" ", name);
            }
        }
        return lead;
    }

    public static SFLead from(Map<String, Object> entityAttributeField) {
        SFLead lead = new SFLead();
        lead.id = (String) entityAttributeField.get("Id");
        if (entityAttributeField.get("Last Modified Date") != null)
            lead.lastModifiedDate = new Date((Long) entityAttributeField.get("Last Modified Date"));
        lead.description = (String) entityAttributeField.get("Description");
        lead.ownerId = (String) entityAttributeField.get("Owner Id");
        if (entityAttributeField.get("Created Date") != null)
            lead.createdDate = new Date((Long) entityAttributeField.get("Created Date"));
        lead.isConverted = (Boolean) entityAttributeField.get("Is Converted");
        lead.annualRevenue = (Double) entityAttributeField.get("Annual Revenue");
        lead.cleanStatus = (String) entityAttributeField.get("Clean Status");
        lead.company = (String) entityAttributeField.get("Company");
        lead.convertedAccountId = (String) entityAttributeField.get("Converted Account Id");
        lead.convertedContactId = (String) entityAttributeField.get("Converted Contact Id");
        Long convertedDate = (Long) entityAttributeField.get("Converted Date");
        if (convertedDate != null && convertedDate > 0)
            lead.convertedDate = DatePlus.getYYYYMMDD(convertedDate);
        lead.convertedOpportunityId = (String) entityAttributeField.get("Converted Opportunity Id");
        lead.email = (String) entityAttributeField.get("Email");
        lead.industry = (String) entityAttributeField.get("Industry");
        lead.leadSource = (String) entityAttributeField.get("LeadSource");
        lead.mobilePhone = (String) entityAttributeField.get("MobilePhone");
        lead.phone = (String) entityAttributeField.get("Phone");
        lead.rating = (String) entityAttributeField.get("Rating");
        lead.title = (String) entityAttributeField.get("Title");
        lead.status = (String) entityAttributeField.get("Status");
        lead.website = (String) entityAttributeField.get("Website");
        String name = (String) entityAttributeField.get("Name");
        List<String> nameList = new ArrayList<>(Arrays.asList(name.split("\\s+")));
        if (nameList.size() == 1)
            lead.lastName = name;
        else {
            lead.firstName = nameList.get(0);
            nameList.remove(0);
            lead.lastName = String.join(" ", nameList);
        }
        return lead;
    }
}
