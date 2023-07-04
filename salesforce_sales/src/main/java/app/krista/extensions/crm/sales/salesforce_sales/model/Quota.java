package app.krista.extensions.crm.sales.salesforce_sales.model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public final class Quota {

    public QuotaOwner QuotaOwner;

    @SerializedName("QuotaAmount")
    public long quotaAmount;

    public static class QuotaOwner {
        public String Name;
    }

    public Map<String, Object> to() {
        return Map.of("Owner Name", QuotaOwner.Name, "Quota Amount", Math.round(quotaAmount));
    }

}