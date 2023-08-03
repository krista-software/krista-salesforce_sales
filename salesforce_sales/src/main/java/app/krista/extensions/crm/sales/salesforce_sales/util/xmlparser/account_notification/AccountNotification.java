package app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser.account_notification;

import app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser.sObject;

import javax.xml.bind.annotation.XmlElement;

public final class AccountNotification {


    @XmlElement(name = "sObject", namespace = "http://soap.sforce.com/2005/09/outbound")
    public AccountObject accountObject;

    @XmlElement(name = "Id", namespace = "http://soap.sforce.com/2005/09/outbound")

    public String Id;

}
