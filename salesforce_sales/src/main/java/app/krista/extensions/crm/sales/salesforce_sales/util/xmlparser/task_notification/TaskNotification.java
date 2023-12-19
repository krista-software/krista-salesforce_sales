package app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser.task_notification;

import javax.xml.bind.annotation.XmlElement;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.SFORCE_COM_2005_09_OUTBOUND;

public final class TaskNotification {
    @XmlElement(name = "sObject", namespace = SFORCE_COM_2005_09_OUTBOUND)
    private SalesforceXmlTask SalesforceXmlTask;

    @XmlElement(name = "Id", namespace = SFORCE_COM_2005_09_OUTBOUND)
    private String Id;

    public SalesforceXmlTask getSalesforceXmlTask() {
        return SalesforceXmlTask;
    }

    public void setSalesforceXmlTask(SalesforceXmlTask salesforceXmlTask) {
        this.SalesforceXmlTask = salesforceXmlTask;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
