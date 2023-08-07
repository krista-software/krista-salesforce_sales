package app.krista.extensions.crm.sales.salesforce_sales.util;

public class Validators {
    public static String getStringOrEmpty(Object value) {
        return value != null ? value.toString() : "";
    }

    public static Double getDoubleOrEmpty(Object value) {
        return value != null ? (Double) value : 0;
    }

    private Validators() {
    }
}
