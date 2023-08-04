package app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser.account_notification;

import app.krista.model.base.FreeForm;

import javax.xml.bind.annotation.XmlElement;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.*;

public final class AccountObject {
    @XmlElement(name = "Id", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String Id;
    @XmlElement(name = "Name", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String Name;
    @XmlElement(name = "Fax", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String Fax;
    @XmlElement(name = "AccountNumber", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String AccountNumber;
    @XmlElement(name = "Website", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String Website;
    @XmlElement(name = "Sic", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String Sic;
    @XmlElement(name = "OwnerId", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String OwnerId;
    @XmlElement(name = "CreatedById", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String CreatedById;
    @XmlElement(name = "CleanStatus", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String CleanStatus;
    @XmlElement(name = "SLASerialNumber__c", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String SLASerialNumber__c;
    @XmlElement(name = "CreatedDate", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public Long CreatedDate;
    @XmlElement(name = "Type", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String Type;
    @XmlElement(name = "BillingStreet", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String BillingStreet;
    @XmlElement(name = "BillingCity", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String BillingCity;
    @XmlElement(name = "BillingState", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String BillingState;
    @XmlElement(name = "BillingPostalCode", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String BillingPostalCode;
    @XmlElement(name = "BillingCountry", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String BillingCountry;
    @XmlElement(name = "ShippingStreet", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String ShippingStreet;
    @XmlElement(name = "ShippingCity", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String ShippingCity;
    @XmlElement(name = "ShippingState", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String ShippingState;
    @XmlElement(name = "ShippingPostalCode", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String ShippingPostalCode;
    @XmlElement(name = "ShippingCountry", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String ShippingCountry;

    @XmlElement(name = "Phone", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String Phone;

    @XmlElement(name = "Industry", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String Industry;
    @XmlElement(name = "NumberOfEmployees", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public Double NumberOfEmployees;
    @XmlElement(name = "Ownership", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String Ownership;
    @XmlElement(name = "CustomerPriority__c", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String CustomerPriority__c;
    @XmlElement(name = "SLAExpirationDate__c", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String SLAExpirationDate__c;
    @XmlElement(name = "AnnualRevenue", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public Double AnnualRevenue;

    public static FreeForm toAccountFreeForm(AccountObject accountNotification) {

        FreeForm freeForm = new FreeForm();
        freeForm.put("Id", TEXT, accountNotification.Id);
        freeForm.put("Name", TEXT, accountNotification.Name);
        freeForm.put("Fax", TEXT, accountNotification.Fax);
        freeForm.put("AccountNumber", TEXT, accountNotification.AccountNumber);
        freeForm.put("Website", TEXT, accountNotification.Website);
        freeForm.put("Sic", TEXT, accountNotification.Sic);
        freeForm.put("OwnerId", TEXT, accountNotification.OwnerId);
        freeForm.put("CreatedById", TEXT, accountNotification.CreatedById);
        freeForm.put("CleanStatus", TEXT, accountNotification.CleanStatus);
        freeForm.put("SLASerialNumber__c", TEXT, accountNotification.SLASerialNumber__c);
        freeForm.put("CreatedDate", LONG, accountNotification.CreatedDate);
        freeForm.put("Type", TEXT, accountNotification.Type);
        freeForm.put("BillingStreet", TEXT, accountNotification.BillingStreet);
        freeForm.put("BillingCity", TEXT, accountNotification.BillingCity);
        freeForm.put("BillingState", TEXT, accountNotification.BillingState);
        freeForm.put("BillingPostalCode", TEXT, accountNotification.BillingPostalCode);
        freeForm.put("BillingCountry", TEXT, accountNotification.BillingCountry);
        freeForm.put("ShippingStreet", TEXT, accountNotification.ShippingStreet);
        freeForm.put("ShippingCity", TEXT, accountNotification.ShippingCity);
        freeForm.put("ShippingState", TEXT, accountNotification.ShippingState);
        freeForm.put("ShippingPostalCode", TEXT, accountNotification.ShippingPostalCode);
        freeForm.put("ShippingCountry", TEXT, accountNotification.ShippingCountry);
        freeForm.put("Phone", TEXT, accountNotification.Phone);
        freeForm.put("Industry", TEXT, accountNotification.Industry);
        freeForm.put("NumberOfEmployees", DOUBLE, accountNotification.NumberOfEmployees);
        freeForm.put("Ownership", TEXT, accountNotification.Ownership);
        freeForm.put("CustomerPriority__c", TEXT, accountNotification.CustomerPriority__c);
        freeForm.put("SLAExpirationDate__c", TEXT, accountNotification.SLAExpirationDate__c);
        freeForm.put("AnnualRevenue", DOUBLE, accountNotification.AnnualRevenue);
        return freeForm;
    }
}
