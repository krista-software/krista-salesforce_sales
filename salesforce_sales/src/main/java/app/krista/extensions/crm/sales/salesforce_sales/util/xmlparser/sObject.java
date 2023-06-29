package app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser;

import javax.xml.bind.annotation.XmlElement;

public final class sObject {
    @XmlElement(name = "Id", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String Id;
    @XmlElement(name = "Amount", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public double Amount;
    @XmlElement(name = "StageName", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String StageName;
    @XmlElement(name = "Name", namespace = "urn:sobject.enterprise.soap.sforce.com")
    public String Name;
}