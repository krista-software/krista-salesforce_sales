package app.krista.extensions.crm.sales.salesforce_sales.util;

import java.util.List;

public final class SalesforceListResponse<T> {

    public int totalSize;
    public boolean done;
    public List<T> records;

}
