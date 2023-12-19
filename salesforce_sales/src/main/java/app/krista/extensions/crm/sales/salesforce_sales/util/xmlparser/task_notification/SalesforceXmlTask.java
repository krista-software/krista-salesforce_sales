package app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser.task_notification;

import app.krista.model.base.FreeForm;

import javax.xml.bind.annotation.XmlElement;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.ENTERPRISE_SOAP_SFORCE_COM;
import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.TEXT;

public final class SalesforceXmlTask {
    @XmlElement(name = "Id", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String Id;
    @XmlElement(name = "Subject", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String Subject;
    @XmlElement(name = "Priority", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String Priority;
    @XmlElement(name = "ActivityDate", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String ActivityDate;
    @XmlElement(name = "Description", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String Description;
    @XmlElement(name = "Status", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String Status;

    @XmlElement(name = "OwnerId", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String OwnerId;
    @XmlElement(name = "WhatId", namespace = ENTERPRISE_SOAP_SFORCE_COM)
    public String WhatId;

    public FreeForm toTaskFreeForm() {
        FreeForm freeForm = new FreeForm();
        freeForm.put("Id", TEXT, Id);
        freeForm.put("Title", TEXT, Subject);
        freeForm.put("Priority", TEXT, Priority);
        freeForm.put("Due Date", TEXT, ActivityDate);
        freeForm.put("Description", TEXT, Description);
        freeForm.put("Status", TEXT, Status);
        freeForm.put("Assigned To Id", TEXT, OwnerId);
        freeForm.put("Associated Entity Id", TEXT, WhatId);
        return freeForm;
    }
}
