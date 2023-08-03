package app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser.lead_notification;

import javax.xml.bind.annotation.XmlElement;

public final class LeadObject {

    @XmlElement(name = "FirstName", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String FirstName;
    @XmlElement(name = "LastName", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String LastName;
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
    public String  CleanStatus;
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

}
