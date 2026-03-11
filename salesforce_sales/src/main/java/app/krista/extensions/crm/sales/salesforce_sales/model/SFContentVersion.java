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

import java.util.Date;
import java.util.Map;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Validators.returnNullOrValue;

/**
 * Salesforce ContentVersion API response model.
 * Maps to the ContentVersion standard object in Salesforce.
 * Used for file upload and management operations.
 */
public final class SFContentVersion {
    
    @SerializedName("Id")
    public String id;
    
    @SerializedName("Title")
    public String title;
    
    @SerializedName("PathOnClient")
    public String pathOnClient;
    
    @SerializedName("VersionData")
    public String versionData;  // Base64 encoded file content
    
    @SerializedName("ContentDocumentId")
    public String contentDocumentId;
    
    @SerializedName("FileType")
    public String fileType;
    
    @SerializedName("FileExtension")
    public String fileExtension;
    
    @SerializedName("ContentSize")
    public Long contentSize;
    
    @SerializedName("Description")
    public String description;
    
    @SerializedName("CreatedDate")
    public Date createdDate;
    
    @SerializedName("CreatedById")
    public String createdById;
    
    @SerializedName("LastModifiedDate")
    public Date lastModifiedDate;
    
    @SerializedName("LastModifiedById")
    public String lastModifiedById;
    
    /**
     * Converts a Map of entity attributes to SFContentVersion model.
     * Used for creating/updating content versions from catalog requests.
     *
     * @param entityAttributeField Map containing content version field values
     * @return SFContentVersion instance populated with field values
     */
    public static SFContentVersion from(Map<String, Object> entityAttributeField) {
        SFContentVersion contentVersion = new SFContentVersion();
        contentVersion.title = returnNullOrValue((String) entityAttributeField.get("Title"));
        contentVersion.pathOnClient = returnNullOrValue((String) entityAttributeField.get("Path On Client"));
        contentVersion.versionData = returnNullOrValue((String) entityAttributeField.get("Version Data"));
        contentVersion.description = returnNullOrValue((String) entityAttributeField.get("Description"));
        
        // Read-only fields (not set during creation)
        contentVersion.contentDocumentId = returnNullOrValue((String) entityAttributeField.get("Content Document Id"));
        contentVersion.fileType = returnNullOrValue((String) entityAttributeField.get("File Type"));
        contentVersion.fileExtension = returnNullOrValue((String) entityAttributeField.get("File Extension"));
        
        Object contentSize = entityAttributeField.get("Content Size");
        if (contentSize instanceof Number) {
            contentVersion.contentSize = ((Number) contentSize).longValue();
        }
        
        Long createdDate = (Long) entityAttributeField.get("Created Date");
        contentVersion.createdDate = createdDate != null ? new Date(createdDate) : null;
        
        contentVersion.createdById = returnNullOrValue((String) entityAttributeField.get("Created By Id"));
        
        Long lastModifiedDate = (Long) entityAttributeField.get("Last Modified Date");
        contentVersion.lastModifiedDate = lastModifiedDate != null ? new Date(lastModifiedDate) : null;
        
        contentVersion.lastModifiedById = returnNullOrValue((String) entityAttributeField.get("Last Modified By Id"));
        
        return contentVersion;
    }
}

