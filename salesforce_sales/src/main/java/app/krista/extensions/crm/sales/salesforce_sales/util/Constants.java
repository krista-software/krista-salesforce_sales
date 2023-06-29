package app.krista.extensions.crm.sales.salesforce_sales.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class Constants {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String AND = " AND ";
    public static final String MEDIA_URI = "http://platform:9991";

    private Constants() {
    }
}

