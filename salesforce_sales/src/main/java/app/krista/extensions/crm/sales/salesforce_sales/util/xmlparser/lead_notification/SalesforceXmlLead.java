package app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser.lead_notification;

import app.krista.model.base.FreeForm;

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.*;

public final class SalesforceXmlLead {

    @XmlElement(name = "FirstName", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String FirstName;
    @XmlElement(name = "LastName", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String LastName;
    @XmlElement(name = "Name", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String Name;
    @XmlElement(name = "Id", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String Id;
    @XmlElement(name = "Title", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String Title;
    @XmlElement(name = "Company", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String Company;
    @XmlElement(name = "Website", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String Website;
    @XmlElement(name = "OwnerId", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String OwnerId;
    @XmlElement(name = "IsConverted", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String IsConverted;
    @XmlElement(name = "ConvertedAccountId", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String ConvertedAccountId;
    @XmlElement(name = "ConvertedContactId", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String ConvertedContactId;
    @XmlElement(name = "ConvertedOpportunityId", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String ConvertedOpportunityId;
    @XmlElement(name = "CleanStatus", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String CleanStatus;
    @XmlElement(name = "Phone", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String Phone;
    @XmlElement(name = "MobilePhone", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String MobilePhone;
    @XmlElement(name = "Email", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String Email;
    @XmlElement(name = "Description", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String Description;
    @XmlElement(name = "Status", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String Status;
    @XmlElement(name = "LeadSource", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String LeadSource;
    @XmlElement(name = "Industry", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String Industry;
    @XmlElement(name = "Rating", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String Rating;
    @XmlElement(name = "AnnualRevenue", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public Double AnnualRevenue;
    @XmlElement(name = "ConvertedDate", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String ConvertedDate;
    @XmlElement(name = "CreatedDate", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public Long CreatedDate;

    @XmlElement(name = "LastModifiedDate", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public Long LastModifiedDate;

    public FreeForm toLeadFreeForm() {
        FreeForm freeForm = new FreeForm();
        if (Name != null && !Name.isEmpty()) {
            List<String> name = new ArrayList<>(Arrays.asList(Name.split("\\s+")));
            if (name.size() == 1) LastName = Name;
            else {
                FirstName = name.get(0);
                name.remove(0);
                LastName = String.join(" ", name);
            }
        }
        String leadName = FirstName == null ? LastName : FirstName + " " + LastName;
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
