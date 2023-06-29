package app.krista.extensions.crm.sales.salesforce_sales.model;

import java.util.HashMap;
import java.util.Map;

public final class SFAggOpportunityOwnerTotal {

    public String OwnerId;
    public Double Total;
    public String Name;

    public String getOwnerId() {
        return OwnerId;
    }

    public Double getTotal() {
        return Total;
    }

    public String getName() {
        return Name;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> sFAggOpportunityOwnerTotal = new HashMap<>();
        sFAggOpportunityOwnerTotal.put("Owner Id", getOwnerId());
        sFAggOpportunityOwnerTotal.put("Total", String.valueOf(getTotal()));
        sFAggOpportunityOwnerTotal.put("Name", String.valueOf(getName()));
        return sFAggOpportunityOwnerTotal;
    }
}