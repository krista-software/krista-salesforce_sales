package app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser;

import javax.xml.bind.annotation.XmlElement;

public final class Notification {
    @XmlElement(name = "sObject", namespace = "http://soap.sforce.com/2005/09/outbound")
    private sObject sObject;

    @XmlElement(name = "Id", namespace = "http://soap.sforce.com/2005/09/outbound")
    private String Id;

    public sObject getSObject() {
        return sObject;
    }

    public void setSObject(sObject sObject) {
        this.sObject = sObject;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }
}