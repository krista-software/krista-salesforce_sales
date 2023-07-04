package app.krista.extensions.crm.sales.salesforce_sales.model;


import java.util.List;

public final class CreateRecordResponse {

    public String id;
    public String success;
    public List errors;

    @Override
    public String toString() {
        return "CreateRecordResponse{" +
                "id='" + id + '\'' +
                ", success='" + success + '\'' +
                ", errors=" + errors +
                '}';
    }

}
