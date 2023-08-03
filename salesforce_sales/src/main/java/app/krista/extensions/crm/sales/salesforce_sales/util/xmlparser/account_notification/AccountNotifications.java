package app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser.account_notification;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Account Notifications", namespace = "http://soap.sforce.com/2005/09/outbound")
@XmlAccessorType(XmlAccessType.FIELD)
public final class AccountNotifications {
    @XmlElement(name = "ActionId", namespace = "http://soap.sforce.com/2005/09/outbound")
    public String ActionId;

    //@XmlElement(name = "ActionId", namespace = "http://soap.sforce.com/2005/09/outbound")
    public String PartnerUrl;

    //@XmlElement(name = "ActionId", namespace = "http://soap.sforce.com/2005/09/outbound")
    public String EnterpriseUrl;

    //@XmlElement(name = "ActionId", namespace = "http://soap.sforce.com/2005/09/outbound")
    public String OrganizationId;

    //@XmlElement(name = "ActionId", namespace = "http://soap.sforce.com/2005/09/outbound")
    public String SessionId;

    @XmlElement(name = "Notification", namespace = "http://soap.sforce.com/2005/09/outbound")
    public AccountNotification accountNotification;



}
