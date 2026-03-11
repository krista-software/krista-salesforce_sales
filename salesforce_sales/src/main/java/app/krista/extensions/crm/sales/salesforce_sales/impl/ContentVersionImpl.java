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

package app.krista.extensions.crm.sales.salesforce_sales.impl;

import app.krista.extensions.crm.sales.salesforce_sales.model.SFContentVersion;
import app.krista.extensions.crm.sales.salesforce_sales.service.ContentVersion;

/**
 * Implementation of {@link ContentVersion} service interface.
 * Wraps the SFContentVersion model and provides getter methods.
 */
public final class ContentVersionImpl implements ContentVersion {
    
    private final SFContentVersion sfContentVersion;
    
    /**
     * Constructor that wraps a SFContentVersion model.
     *
     * @param sfContentVersion Salesforce ContentVersion model
     */
    public ContentVersionImpl(SFContentVersion sfContentVersion) {
        this.sfContentVersion = sfContentVersion;
    }
    
    @Override
    public String getId() {
        return sfContentVersion.id;
    }
    
    @Override
    public String getTitle() {
        return sfContentVersion.title;
    }
    
    @Override
    public String getPathOnClient() {
        return sfContentVersion.pathOnClient;
    }
    
    @Override
    public String getVersionData() {
        return sfContentVersion.versionData;
    }
    
    @Override
    public String getContentDocumentId() {
        return sfContentVersion.contentDocumentId;
    }
    
    @Override
    public String getFileType() {
        return sfContentVersion.fileType;
    }
    
    @Override
    public String getFileExtension() {
        return sfContentVersion.fileExtension;
    }
    
    @Override
    public Long getContentSize() {
        return sfContentVersion.contentSize;
    }
    
    @Override
    public String getDescription() {
        return sfContentVersion.description;
    }
    
    @Override
    public Long getCreatedDate() {
        return sfContentVersion.createdDate != null ? sfContentVersion.createdDate.getTime() : null;
    }
    
    @Override
    public String getCreatedById() {
        return sfContentVersion.createdById;
    }
    
    @Override
    public Long getLastModifiedDate() {
        return sfContentVersion.lastModifiedDate != null ? sfContentVersion.lastModifiedDate.getTime() : null;
    }
    
    @Override
    public String getLastModifiedById() {
        return sfContentVersion.lastModifiedById;
    }
}

