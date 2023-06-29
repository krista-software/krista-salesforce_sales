package app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Notifications", namespace = "http://soap.sforce.com/2005/09/outbound")
@XmlAccessorType(XmlAccessType.FIELD)
public final class Notifications {
    @XmlElement(name = "ActionId", namespace = "http://soap.sforce.com/2005/09/outbound")
    private String ActionId;

    //@XmlElement(name = "ActionId", namespace = "http://soap.sforce.com/2005/09/outbound")
    private String PartnerUrl;

    //@XmlElement(name = "ActionId", namespace = "http://soap.sforce.com/2005/09/outbound")
    private String EnterpriseUrl;

    //@XmlElement(name = "ActionId", namespace = "http://soap.sforce.com/2005/09/outbound")
    private String OrganizationId;

    //@XmlElement(name = "ActionId", namespace = "http://soap.sforce.com/2005/09/outbound")
    private String SessionId;

    @XmlElement(name = "Notification", namespace = "http://soap.sforce.com/2005/09/outbound")
    private Notification Notification;

    public String getActionId() {
        return ActionId;
    }

    public void setActionId(String ActionId) {
        this.ActionId = ActionId;
    }

    public String getPartnerUrl() {
        return PartnerUrl;
    }

    public void setPartnerUrl(String PartnerUrl) {
        this.PartnerUrl = PartnerUrl;
    }

    public String getEnterpriseUrl() {
        return EnterpriseUrl;
    }

    public void setEnterpriseUrl(String EnterpriseUrl) {
        this.EnterpriseUrl = EnterpriseUrl;
    }

    public String getOrganizationId() {
        return OrganizationId;
    }

    public void setOrganizationId(String OrganizationId) {
        this.OrganizationId = OrganizationId;
    }

    public String getSessionId() {
        return SessionId;
    }

    public void setSessionId(String SessionId) {
        this.SessionId = SessionId;
    }

    public Notification getNotification() {
        return Notification;
    }

    public void setNotification(Notification Notification) {
        this.Notification = Notification;
    }
}