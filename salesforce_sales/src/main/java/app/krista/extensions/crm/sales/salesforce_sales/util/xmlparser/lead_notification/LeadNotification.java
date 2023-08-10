package app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser.lead_notification;

import javax.xml.bind.annotation.XmlElement;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.SFORCE_COM_2005_09_OUTBOUND;

public final class LeadNotification {

    @XmlElement(name = "sObject", namespace = SFORCE_COM_2005_09_OUTBOUND)
    private SalesforceXmlLead SalesforceXmlLead;

    @XmlElement(name = "Id", namespace = SFORCE_COM_2005_09_OUTBOUND)
    private String Id;

    public SalesforceXmlLead getSalesforceXmlLead() {
        return SalesforceXmlLead;
    }

    public void setSalesforceXmlLead(SalesforceXmlLead salesforceXmlLead) {
        this.SalesforceXmlLead = salesforceXmlLead;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
