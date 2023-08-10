package app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser.opportunity_notification;

import javax.xml.bind.annotation.XmlElement;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.SFORCE_COM_2005_09_OUTBOUND;

public final class OpportunityNotification {
    @XmlElement(name = "sObject", namespace = SFORCE_COM_2005_09_OUTBOUND)
    private SalesforceXmlOpportunity SalesforceXmlOpportunity;

    @XmlElement(name = "Id", namespace = SFORCE_COM_2005_09_OUTBOUND)
    private String Id;

    public SalesforceXmlOpportunity getSObject() {
        return SalesforceXmlOpportunity;
    }

    public void setSObject(SalesforceXmlOpportunity SalesforceXmlOpportunity) {
        this.SalesforceXmlOpportunity = SalesforceXmlOpportunity;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }
}