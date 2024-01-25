package app.krista.extensions.crm.sales.salesforce_sales.util;

import app.krista.extension.impl.anno.Field;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

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

    public static void validateTimeZone(String timeZone) throws IOException {
        List<String> timeZones = Arrays.asList(TimeZone.getAvailableIDs());
        if (timeZone != null && !timeZones.contains(timeZone)) {
            throw new IOException(Constants.PLEASE_PROVIDE_SUPPORTED_TIME_ZONE);
        }
    }

    public static String returnNullOrValue(String value) {
        return value != null && value.isEmpty() ? null : value;
    }

    private Validators() {
    }
}
