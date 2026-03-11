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

package app.krista.extensions.crm.sales.salesforce_sales.util;

import app.krista.extension.impl.anno.Field;

import java.io.IOException;
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
            throw new IllegalArgumentException("Email address is required. Please provide at least one recipient email address.");
        }
        if (subject == null || subject.trim().isEmpty()) {
            throw new IllegalArgumentException("Email subject is required. Please provide a subject for the email.");
        }
        if (body == null || body.trim().isEmpty()) {
            throw new IllegalArgumentException("Email body is required. Please provide content for the email message.");
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
