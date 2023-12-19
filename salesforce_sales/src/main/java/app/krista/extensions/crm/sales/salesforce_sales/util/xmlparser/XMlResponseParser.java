package app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser;

import app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser.account_notification.AccountNotifications;
import app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser.lead_notification.LeadNotifications;
import app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser.opportunity_notification.OpportunityNotifications;
import app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser.task_notification.TaskNotifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;

public final class XMlResponseParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(XMlResponseParser.class);


    private XMlResponseParser() {
    }

    // region Internal
    private static <T> JAXBElement<T> parseXmlResponse(String response, Class<T> notificationClass) {
        try (StringReader stringReader = new StringReader(response)) {
            XMLInputFactory xif = XMLInputFactory.newFactory();
            xif.setProperty(XMLInputFactory.SUPPORT_DTD, false); // Disable DTD support
            xif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false); // Disable external entities
            XMLStreamReader xsr = xif.createXMLStreamReader(stringReader);
            try {
                xsr.nextTag(); // Advance to Envelope tag
                xsr.nextTag(); // Advance to Body tag
                xsr.nextTag();

                JAXBContext jc = JAXBContext.newInstance(notificationClass);
                Unmarshaller unmarshaller = jc.createUnmarshaller();
                return unmarshaller.unmarshal(xsr, notificationClass);
            } finally {
                xsr.close();
            }

        } catch (XMLStreamException | JAXBException cause) {
            LOGGER.error("XML Parsing Failed for " + notificationClass.getSimpleName() + " with error " + cause.getMessage(), cause);
        }
        return null;
    }

    // endregion

    public static JAXBElement<OpportunityNotifications> parseOpportunityXmlResponse(String response) {
        return parseXmlResponse(response, OpportunityNotifications.class);
    }

    public static JAXBElement<AccountNotifications> parseAccountXmlResponse(String response) {
        return parseXmlResponse(response, AccountNotifications.class);
    }

    public static JAXBElement<LeadNotifications> parseLeadXmlResponse(String response) {
        return parseXmlResponse(response, LeadNotifications.class);
    }

    public static JAXBElement<TaskNotifications> parseTaskXmlResponse(String response) {
        return parseXmlResponse(response, TaskNotifications.class);
    }
}