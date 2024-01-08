package app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser;

import javax.xml.bind.annotation.XmlElement;

public class ResponseAck {

    public static final ResponseAck SUCCESS = new ResponseAck().setAck(true);

    @XmlElement(name = "Ack")
    public boolean ack;

    public ResponseAck() {
    }

    public ResponseAck setAck(boolean ack) {
        this.ack = ack;
        return this;
    }

}