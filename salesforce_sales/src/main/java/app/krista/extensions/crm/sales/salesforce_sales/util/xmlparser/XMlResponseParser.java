package app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser;

import app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser.account_notification.AccountNotifications;
import app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser.lead_notification.LeadNotifications;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;

public final class XMlResponseParser {

    // region Internal
    private static <T> JAXBElement<T> parseXmlResponse(String response, Class<T> notificationClass) {
        try {
            XMLInputFactory xif = XMLInputFactory.newFactory();
            XMLStreamReader xsr = xif.createXMLStreamReader(new StringReader(response));
            xsr.nextTag(); // Advance to Envelope tag
            xsr.nextTag(); // Advance to Body tag
            xsr.nextTag();

            JAXBContext jc = JAXBContext.newInstance(notificationClass);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            return unmarshaller.unmarshal(xsr, notificationClass);

        } catch (XMLStreamException | JAXBException cause) {
            cause.printStackTrace();
        }
        return null;
    }

    // endregion

    public static JAXBElement<Notifications> parseOpportunityXmlResponse(String response) throws Exception {
        return parseXmlResponse(response, Notifications.class);
    }

    public static JAXBElement<AccountNotifications> parseAccountXmlResponse(String response) throws Exception {
        return parseXmlResponse(response, AccountNotifications.class);
    }

    public static JAXBElement<LeadNotifications> parseLeadXmlResponse(String response) throws Exception {
        return parseXmlResponse(response, LeadNotifications.class);
    }

}