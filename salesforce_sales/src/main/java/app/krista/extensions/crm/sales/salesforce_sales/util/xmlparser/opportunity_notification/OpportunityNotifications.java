package app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser.opportunity_notification;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.SFORCE_COM_2005_09_OUTBOUND;

@XmlRootElement(name = "Notifications", namespace = SFORCE_COM_2005_09_OUTBOUND)
@XmlAccessorType(XmlAccessType.FIELD)
public final class OpportunityNotifications {
    @XmlElement(name = "ActionId", namespace = SFORCE_COM_2005_09_OUTBOUND)
    private String ActionId;
    private String PartnerUrl;
    private String EnterpriseUrl;
    private String OrganizationId;
    private String SessionId;

    @XmlElement(name = "Notification", namespace = SFORCE_COM_2005_09_OUTBOUND)
    private OpportunityNotification OpportunityNotification;

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

    public OpportunityNotification getNotification() {
        return OpportunityNotification;
    }

    public void setNotification(OpportunityNotification OpportunityNotification) {
        this.OpportunityNotification = OpportunityNotification;
    }
}