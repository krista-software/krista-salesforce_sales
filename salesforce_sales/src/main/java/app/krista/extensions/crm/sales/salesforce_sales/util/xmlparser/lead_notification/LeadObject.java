package app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser.lead_notification;

import app.krista.model.base.FreeForm;

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.*;
import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.LONG;

public final class LeadObject {

    @XmlElement(name = "FirstName", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String FirstName;
    @XmlElement(name = "LastName", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String LastName;
    @XmlElement(name = "Name", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String Name;
    @XmlElement(name = "Id", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String Id;
    @XmlElement(name = "Title", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String Title;
    @XmlElement(name = "Company", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String Company;
    @XmlElement(name = "Website", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String Website;
    @XmlElement(name = "OwnerId", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String OwnerId;
    @XmlElement(name = "IsConverted", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String IsConverted;
    @XmlElement(name = "ConvertedAccountId", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String ConvertedAccountId;
    @XmlElement(name = "ConvertedContactId", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String ConvertedContactId;
    @XmlElement(name = "ConvertedOpportunityId", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String ConvertedOpportunityId;
    @XmlElement(name = "CleanStatus", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String CleanStatus;
    @XmlElement(name = "Phone", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String Phone;
    @XmlElement(name = "MobilePhone", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String MobilePhone;
    @XmlElement(name = "Email", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String Email;
    @XmlElement(name = "Description", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String Description;
    @XmlElement(name = "Status", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String Status;
    @XmlElement(name = "LeadSource", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String LeadSource;
    @XmlElement(name = "Industry", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String Industry;
    @XmlElement(name = "Rating", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String Rating;
    @XmlElement(name = "AnnualRevenue", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public Double AnnualRevenue;
    @XmlElement(name = "ConvertedDate", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String ConvertedDate;
    @XmlElement(name = "CreatedDate", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public Long CreatedDate;

    @XmlElement(name = "LastModifiedDate", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public Long LastModifiedDate;

    public FreeForm toLeadFreeForm() {
        FreeForm freeForm = new FreeForm();
        if (Name != null) {
            List<String> name = new ArrayList<>(Arrays.asList(Name.split("\\s+")));
            if (name.size() == 1)
                LastName = Name;
            else {
                FirstName = name.get(0);
                name.remove(0);
                LastName = String.join(" ", name);
            }
        }
        String leadName = FirstName == null ? LastName :
                FirstName + " " + LastName;
        freeForm.put("Name", TEXT, leadName);
        freeForm.put("Id", TEXT, Id);
        freeForm.put("Title", TEXT, Title);
        freeForm.put("Company", TEXT, Company);
        freeForm.put("Website", TEXT, Website);
        freeForm.put("OwnerId", TEXT, OwnerId);
        freeForm.put("IsConverted", TEXT, IsConverted);
        freeForm.put("ConvertedAccountId", TEXT, ConvertedAccountId);
        freeForm.put("ConvertedContactId", TEXT, ConvertedContactId);
        freeForm.put("ConvertedOpportunityId", TEXT, ConvertedOpportunityId);
        freeForm.put("CleanStatus", TEXT, CleanStatus);
        freeForm.put("Phone", TEXT, Phone);
        freeForm.put("MobilePhone", TEXT, MobilePhone);
        freeForm.put("Email", TEXT, Email);
        freeForm.put("Description", TEXT, Description);
        freeForm.put("Status", TEXT, Status);
        freeForm.put("LeadSource", TEXT, LeadSource);
        freeForm.put("Industry", TEXT, Industry);
        freeForm.put("Rating", TEXT, Rating);
        freeForm.put("AnnualRevenue", DOUBLE, AnnualRevenue);
        freeForm.put("ConvertedDate", TEXT, ConvertedDate);
        freeForm.put("CreatedDate", LONG, CreatedDate);
        freeForm.put("LastModifiedDate", LONG, LastModifiedDate);
        return freeForm;
    }
}
