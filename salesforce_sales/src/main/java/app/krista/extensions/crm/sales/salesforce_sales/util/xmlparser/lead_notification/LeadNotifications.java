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
    private String ActionId;
    private String PartnerUrl;
    private String EnterpriseUrl;
    private String OrganizationId;
    private String SessionId;

    @XmlElement(name = "Notification", namespace = SFORCE_COM_2005_09_OUTBOUND)
    private LeadNotification leadNotification;

    public String getActionId() {
        return ActionId;
    }

    public void setActionId(String actionId) {
        ActionId = actionId;
    }

    public String getPartnerUrl() {
        return PartnerUrl;
    }

    public void setPartnerUrl(String partnerUrl) {
        PartnerUrl = partnerUrl;
    }

    public String getEnterpriseUrl() {
        return EnterpriseUrl;
    }

    public void setEnterpriseUrl(String enterpriseUrl) {
        EnterpriseUrl = enterpriseUrl;
    }

    public String getOrganizationId() {
        return OrganizationId;
    }

    public void setOrganizationId(String organizationId) {
        OrganizationId = organizationId;
    }

    public String getSessionId() {
        return SessionId;
    }

    public void setSessionId(String sessionId) {
        SessionId = sessionId;
    }

    public LeadNotification getLeadNotification() {
        return leadNotification;
    }

    public void setLeadNotification(LeadNotification leadNotification) {
        this.leadNotification = leadNotification;
    }
}
