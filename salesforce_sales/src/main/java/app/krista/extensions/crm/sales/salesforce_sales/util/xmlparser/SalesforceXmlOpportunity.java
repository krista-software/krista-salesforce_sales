package app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser;

import app.krista.model.base.FreeForm;

import javax.xml.bind.annotation.XmlElement;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.ENTERPRISE_SOAP_SFORCE_COM;
import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.TEXT;

public final class SalesforceXmlOpportunity {
    @XmlElement(name = "Id", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String Id;
    @XmlElement(name = "Amount", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public double Amount;
    @XmlElement(name = "StageName", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String StageName;
    @XmlElement(name = "Name", namespace = ENTERPRISE_SOAP_SFORCE_COM)
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