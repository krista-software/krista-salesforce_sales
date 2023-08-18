package app.krista.extensions.crm.sales.salesforce_sales.util;

import app.krista.extension.impl.anno.Field;
import com.google.gson.Gson;

public final class Constants {
    public static final Gson GSON = new Gson();
    public static final String CREATED = "created";
    public static final String TEXT = "Text";
    public static final String DOUBLE = "Double";
    public static final String LONG = "Long";
    public static final String AND = " AND ";
    public static final String MEDIA_URI = "http://platform:9991";
    public static final String SFORCE_COM_2005_09_OUTBOUND = "http://soap.sforce.com/2005/09/outbound";
    public static final String ENTERPRISE_SOAP_SFORCE_COM = "urn:sobject.enterprise.soap.sforce.com";
    public static final String NAME = "Name";
    public static final String INVALID_TASK_ID = "Invalid task ID";
    public static final String DATA = "Data";
    public static final String SERVICES_DATA_V_48_0_SOBJECTS = "/services/data/v48.0/sobjects/";






    public static void validation(@Field(name = "Email Addresses", type = "Text") String emailAddresses, @Field(name = "Subject", type = "Text") String subject, @Field(name = "Body", type = "Paragraph") String body) {
        if (emailAddresses == null || emailAddresses.trim().isEmpty()) {
            throw new IllegalArgumentException("Email Addresses must not be empty.");
        }
        if (subject == null || subject.trim().isEmpty()) {
            throw new IllegalArgumentException("Subject must not be empty.");
        }
        if (body == null || body.trim().isEmpty()) {
            throw new IllegalArgumentException("Body must not be empty.");
        }
    }

    private Constants() {
    }

}

