package app.krista.extensions.crm.sales.salesforce_sales.model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Validators.returnNullOrValue;

/**
 * Salesforce ContentDocumentLink API response model.
 * Maps to the ContentDocumentLink standard object in Salesforce.
 * Represents the link between a file (ContentDocument) and a record (e.g., Order, Account).
 */
public final class SFContentDocumentLink {
    
    @SerializedName("Id")
    public String id;
    
    @SerializedName("ContentDocumentId")
    public String contentDocumentId;
    
    @SerializedName("LinkedEntityId")
    public String linkedEntityId;
    
    @SerializedName("ShareType")
    public String shareType;  // V=Viewer, C=Collaborator, I=Inferred
    
    @SerializedName("Visibility")
    public String visibility;  // AllUsers, InternalUsers, SharedUsers
    
    /**
     * Converts a Map of entity attributes to SFContentDocumentLink model.
     * Used for creating content document links from catalog requests.
     *
     * @param entityAttributeField Map containing content document link field values
     * @return SFContentDocumentLink instance populated with field values
     */
    public static SFContentDocumentLink from(Map<String, Object> entityAttributeField) {
        SFContentDocumentLink link = new SFContentDocumentLink();
        link.contentDocumentId = returnNullOrValue((String) entityAttributeField.get("Content Document Id"));
        link.linkedEntityId = returnNullOrValue((String) entityAttributeField.get("Linked Entity Id"));
        link.shareType = returnNullOrValue((String) entityAttributeField.get("Share Type"));
        link.visibility = returnNullOrValue((String) entityAttributeField.get("Visibility"));
        return link;
    }
}

