/*
 * Salesforce Sales Extension for Krista
 * Copyright (C) 2025 Krista Software
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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