/*
 * Salesforce Sales Extension for Krista
 * Copyright (C) 2025 Krista Software
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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

