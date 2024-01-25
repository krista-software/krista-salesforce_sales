package app.krista.extensions.crm.sales.salesforce_sales.model;

import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.PricebookEntry;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.Map;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Validators.returnNullOrValue;

public final class SFPricebookEntry {
    @SerializedName("Product2Id")
    public String productId;

    @SerializedName("LastModifiedDate")
    public Date lastModifiedDate;

    @SerializedName("CreatedDate")
    public Date createdDate;

    @SerializedName("ProductCode")
    public String productCode;

    @SerializedName("Id")
    public String id;

    @SerializedName("Pricebook2Id")
    public String pricebookId;

    @SerializedName("UnitPrice")
    public Double unitPrice;

    @SerializedName("Name")
    public String name;


    public static SFPricebookEntry from(PricebookEntry sfPricebookEntry) {
        SFPricebookEntry entry = new SFPricebookEntry();
        entry.id = sfPricebookEntry.id;
        if (sfPricebookEntry.lastModifiedDate != null)
            entry.lastModifiedDate = new Date(sfPricebookEntry.lastModifiedDate);
        if (sfPricebookEntry.createdDate != null)
            entry.createdDate = new Date(sfPricebookEntry.createdDate);
        entry.name = sfPricebookEntry.name;
        entry.pricebookId = sfPricebookEntry.pricebookId;
        entry.productCode = sfPricebookEntry.productCode;
        entry.unitPrice = sfPricebookEntry.unitPrice;
        entry.productId = sfPricebookEntry.productId;
        return entry;
    }

    public static SFPricebookEntry from(Map<String, Object> entityAttributeField) {
        SFPricebookEntry entry = new SFPricebookEntry();
        Long lastModifiedDate = (Long) entityAttributeField.get("Last Modified Date");
        entry.lastModifiedDate = lastModifiedDate != null ? new Date(lastModifiedDate) : null;
        Long createdDate = (Long) entityAttributeField.get("Created Date");
        entry.createdDate = createdDate != null ? new Date(createdDate) : null;
        entry.name = returnNullOrValue((String) entityAttributeField.get("Name"));
        entry.pricebookId = returnNullOrValue((String) entityAttributeField.get("Pricebook Id"));
        entry.productCode = returnNullOrValue((String) entityAttributeField.get("Product Code"));
        entry.unitPrice = (Double) entityAttributeField.get("Unit Price");
        entry.productId = returnNullOrValue((String) entityAttributeField.get("Product Id"));
        return entry;
    }

}
