package app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser.lead_notification;

import app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser.account_notification.AccountObject;

import javax.xml.bind.annotation.XmlElement;

public final class LeadNotification {
    @XmlElement(name = "sObject", namespace = "http://soap.sforce.com/2005/09/outbound")
    public LeadObject leadObject;

    @XmlElement(name = "Id", namespace = "http://soap.sforce.com/2005/09/outbound")

    public String Id;
}
