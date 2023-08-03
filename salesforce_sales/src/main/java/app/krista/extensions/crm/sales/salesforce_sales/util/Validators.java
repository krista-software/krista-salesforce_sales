package app.krista.extensions.crm.sales.salesforce_sales.util;

public class Validators {
    public static Object getValidatedString(Object value) {
        return value != null ? value : "";
    }
    public static Object getValidatedDouble(Object value) {
        return value != null ? value : 0;
    }
}
