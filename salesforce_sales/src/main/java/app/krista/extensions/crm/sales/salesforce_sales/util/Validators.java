package app.krista.extensions.crm.sales.salesforce_sales.util;

import app.krista.extension.impl.anno.Field;

public class Validators {
    public static String getStringOrEmpty(Object value) {
        return value != null ? value.toString() : "";
    }

    public static Double getDoubleOrZero(Object value) {
        return value != null ? (Double) value : 0;
    }

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

    private Validators() {
    }
}
