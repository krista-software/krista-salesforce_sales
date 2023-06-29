package app.krista.extensions.crm.sales.salesforce_sales.model;

import com.google.gson.annotations.SerializedName;

import java.util.LinkedHashMap;
import java.util.Map;

public final class RegionWiseSummary {
    @SerializedName("Amount")
    public Double amount;

    @SerializedName("Region")
    public String region;

    public Object getAmount() {
        return amount;
    }

    public Object getRegion() {
        return region;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("Amount", getAmount());
        dto.put("Region", getRegion());
        return dto;
    }
}
