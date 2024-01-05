package app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "soapenv:Envelope")
public class SOAPEnvelope {

    public static final SOAPEnvelope SUCCESS = new SOAPEnvelope().setBody(SOAPBody.SUCCESS);

    @XmlAttribute(name = "xmlns:soapenv")
    public String schema = "http://schemas.xmlsoap.org/soap/envelope/";

    @XmlElement(name = "soapenv:Body")
    public SOAPBody body;

    public SOAPEnvelope() {
    }

    public SOAPEnvelope setBody(SOAPBody body) {
        this.body = body;
        return this;
    }

    public static class SOAPBody {

        public static final SOAPBody SUCCESS = new SOAPBody().setNotificationsResponse(ResponseAck.SUCCESS);

        @XmlElement(name = "notificationsResponse")
        public ResponseAck notificationsResponse;

        public SOAPBody() {

        }

        public SOAPBody setNotificationsResponse(ResponseAck notificationsResponse) {
            this.notificationsResponse = notificationsResponse;
            return this;
        }

    }
}