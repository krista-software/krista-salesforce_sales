package app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser;

import app.krista.model.base.FreeForm;

import javax.xml.bind.annotation.XmlElement;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.TEXT;

public final class sObject {
    @XmlElement(name = "Id", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String Id;
    @XmlElement(name = "Amount", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public double Amount;
    @XmlElement(name = "StageName", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String StageName;
    @XmlElement(name = "Name", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String Name;


    public FreeForm toOpportunityFreeForm() {
        FreeForm freeForm = new FreeForm();
        freeForm.put("Id", TEXT, Id);
        freeForm.put("Amount", TEXT, Amount);
        freeForm.put("StageName", TEXT, StageName);
        freeForm.put("Name", TEXT, Name);
        return freeForm;
    }
}