package app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser.account_notification;

import app.krista.model.base.FreeForm;

import javax.xml.bind.annotation.XmlElement;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.*;

public final class SalesforceXmlAccount {
    @XmlElement(name = "Id", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String Id;
    @XmlElement(name = "Name", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String Name;
    @XmlElement(name = "Fax", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String Fax;
    @XmlElement(name = "AccountNumber", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String AccountNumber;
    @XmlElement(name = "Website", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String Website;
    @XmlElement(name = "Sic", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String Sic;
    @XmlElement(name = "OwnerId", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String OwnerId;
    @XmlElement(name = "CreatedById", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String CreatedById;
    @XmlElement(name = "CleanStatus", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String CleanStatus;
    @XmlElement(name = "SLASerialNumber__c", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String SLASerialNumber__c;
    @XmlElement(name = "CreatedDate", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public Long CreatedDate;
    @XmlElement(name = "Type", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String Type;
    @XmlElement(name = "BillingStreet", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String BillingStreet;
    @XmlElement(name = "BillingCity", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String BillingCity;
    @XmlElement(name = "BillingState", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String BillingState;
    @XmlElement(name = "BillingPostalCode", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String BillingPostalCode;
    @XmlElement(name = "BillingCountry", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String BillingCountry;
    @XmlElement(name = "ShippingStreet", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String ShippingStreet;
    @XmlElement(name = "ShippingCity", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String ShippingCity;
    @XmlElement(name = "ShippingState", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String ShippingState;
    @XmlElement(name = "ShippingPostalCode", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String ShippingPostalCode;
    @XmlElement(name = "ShippingCountry", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String ShippingCountry;

    @XmlElement(name = "Phone", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String Phone;

    @XmlElement(name = "Industry", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String Industry;
    @XmlElement(name = "NumberOfEmployees", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public Double NumberOfEmployees;
    @XmlElement(name = "Ownership", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String Ownership;
    @XmlElement(name = "CustomerPriority__c", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String CustomerPriority__c;
    @XmlElement(name = "SLAExpirationDate__c", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String SLAExpirationDate__c;
    @XmlElement(name = "AnnualRevenue", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public Double AnnualRevenue;

    public FreeForm toAccountFreeForm() {

        FreeForm freeForm = new FreeForm();
        freeForm.put("Id", TEXT, Id);
        freeForm.put("Name", TEXT, Name);
        freeForm.put("Fax", TEXT, Fax);
        freeForm.put("AccountNumber", TEXT, AccountNumber);
        freeForm.put("Website", TEXT, Website);
        freeForm.put("Sic", TEXT, Sic);
        freeForm.put("OwnerId", TEXT, OwnerId);
        freeForm.put("CreatedById", TEXT, CreatedById);
        freeForm.put("CleanStatus", TEXT, CleanStatus);
        freeForm.put("SLASerialNumber__c", TEXT, SLASerialNumber__c);
        freeForm.put("CreatedDate", LONG, CreatedDate);
        freeForm.put("Type", TEXT, Type);
        freeForm.put("BillingStreet", TEXT, BillingStreet);
        freeForm.put("BillingCity", TEXT, BillingCity);
        freeForm.put("BillingState", TEXT, BillingState);
        freeForm.put("BillingPostalCode", TEXT, BillingPostalCode);
        freeForm.put("BillingCountry", TEXT, BillingCountry);
        freeForm.put("ShippingStreet", TEXT, ShippingStreet);
        freeForm.put("ShippingCity", TEXT, ShippingCity);
        freeForm.put("ShippingState", TEXT, ShippingState);
        freeForm.put("ShippingPostalCode", TEXT, ShippingPostalCode);
        freeForm.put("ShippingCountry", TEXT, ShippingCountry);
        freeForm.put("Phone", TEXT, Phone);
        freeForm.put("Industry", TEXT, Industry);
        freeForm.put("NumberOfEmployees", DOUBLE, NumberOfEmployees);
        freeForm.put("Ownership", TEXT, Ownership);
        freeForm.put("CustomerPriority__c", TEXT, CustomerPriority__c);
        freeForm.put("SLAExpirationDate__c", TEXT, SLAExpirationDate__c);
        freeForm.put("AnnualRevenue", DOUBLE, AnnualRevenue);
        return freeForm;
    }
}
