package app.krista.extensions.crm.sales.salesforce_sales.util;

import app.krista.extension.impl.anno.Field;

import java.io.IOException;
import java.util.List;

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
        List<String> timeZones = List.of("Etc/GMT+12", "Etc/GMT+11", "Pacific/Honolulu", "America/Anchorage", "America/Santa_Isabel",
                "America/Los_Angeles", "America/Phoenix", "America/Chihuahua", "America/Denver", "America/Guatemala", "America/Chicago",
                "America/Mexico_City", "America/Regina", "America/Bogota", "America/New_York", "America/Indiana/Indianapolis", "America/Caracas",
                "America/Asuncion", "America/Halifax", "America/Cuiaba", "America/La_Paz", "America/Santiago", "America/St_Johns", "America/Sao_Paulo",
                "America/Argentina/Buenos_Aires", "America/Cayenne", "America/Godthab", "America/Montevideo", "America/Bahia", "Etc/GMT+2",
                "Atlantic/Azores", "Atlantic/Cape_Verde", "Africa/Casablanca", "Etc/GMT", "Europe/London", "Atlantic/Reykjavik", "Europe/Berlin",
                "Europe/Budapest", "Europe/Paris", "Europe/Warsaw", "Africa/Lagos", "Africa/Windhoek", "Europe/Bucharest", "Asia/Beirut",
                "Africa/Cairo", "Asia/Damascus", "Africa/Johannesburg", "Europe/Kyiv (Kiev)", "Europe/Istanbul", "Asia/Jerusalem", "Asia/Amman",
                "Asia/Baghdad", "Europe/Kaliningrad", "Asia/Riyadh", "Africa/Nairobi", "Asia/Tehran", "Asia/Dubai", "Asia/Baku",
                "Europe/Moscow", "Indian/Mauritius", "Asia/Tbilisi", "Asia/Yerevan", "Asia/Kabul", "Asia/Karachi", "Asia/Toshkent (Tashkent)", "Asia/Kolkata",
                "Asia/Colombo", "Asia/Kathmandu", "Asia/Astana (Almaty)", "Asia/Dhaka", "Asia/Yekaterinburg", "Asia/Yangon (Rangoon)", "Asia/Bangkok", "Asia/Novosibirsk",
                "Asia/Shanghai", "Asia/Krasnoyarsk", "Asia/Singapore", "Australia/Perth", "Asia/Taipei", "Asia/Ulaanbaatar", "Asia/Irkutsk",
                "Asia/Tokyo", "Asia/Seoul", "Australia/Adelaide", "Australia/Darwin", "Australia/Brisbane", "Australia/Sydney", "Pacific/Port_Moresby", "Australia/Hobart",
                "Asia/Yakutsk", "Pacific/Guadalcanal", "Asia/Vladivostok", "Pacific/Auckland", "Etc/GMT-12", "Pacific/Fiji", "Asia/Magadan", "Pacific/Tongatapu",
                "Pacific/Apia", "Pacific/Kiritimati");
        if (timeZone != null && !timeZones.contains(timeZone)) {
            throw new IOException(Constants.PLEASE_PROVIDE_SUPPORTED_TIME_ZONE);
        }
    }

    private Validators() {
    }
}
