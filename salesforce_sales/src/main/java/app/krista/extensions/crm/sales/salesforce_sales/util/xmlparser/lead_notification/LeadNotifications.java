package app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser.lead_notification;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.SFORCE_COM_2005_09_OUTBOUND;


@XmlRootElement(name = "Lead Notifications", namespace = SFORCE_COM_2005_09_OUTBOUND)
@XmlAccessorType(XmlAccessType.FIELD)
public final class LeadNotifications {
    @XmlElement(name = "ActionId", namespace = SFORCE_COM_2005_09_OUTBOUND)
    public String ActionId;
    public String PartnerUrl;
    public String EnterpriseUrl;
    public String OrganizationId;
    public String SessionId;

    @XmlElement(name = "Notification", namespace = SFORCE_COM_2005_09_OUTBOUND)
    public LeadNotification leadNotification;
}
