package app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser.account_notification;

import javax.xml.bind.annotation.XmlElement;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.SFORCE_COM_2005_09_OUTBOUND;


public final class AccountNotification {
    @XmlElement(name = "sObject", namespace = SFORCE_COM_2005_09_OUTBOUND)
    public SalesforceXmlAccount salesforceXmlAccount;

    @XmlElement(name = "Id", namespace = SFORCE_COM_2005_09_OUTBOUND)
    public String Id;

}
