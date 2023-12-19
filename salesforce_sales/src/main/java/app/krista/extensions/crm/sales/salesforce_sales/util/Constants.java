package app.krista.extensions.crm.sales.salesforce_sales.util;

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
    public static final String ENTITY_MAP_IS_NULL_OR_EMPTY = "Entity map is null or empty. ";

    private Constants() {
    }

}

