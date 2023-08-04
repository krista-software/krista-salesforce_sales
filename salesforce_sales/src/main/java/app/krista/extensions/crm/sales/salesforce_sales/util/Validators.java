package app.krista.extensions.crm.sales.salesforce_sales.util;

public class Validators {
    public static String getValidatedString(Object value) {
        return value != null ? value.toString() : "";
    }
    public static Double getValidatedDouble(Object value) {
        return value != null ? (Double) value : 0;
    }
}
