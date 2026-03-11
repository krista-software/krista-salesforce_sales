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

package app.krista.extensions.crm.sales.salesforce_sales.service;

/**
 * Service interface for Salesforce ContentVersion.
 * Provides getter methods for all ContentVersion fields.
 */
public interface ContentVersion {
    
    /**
     * @return Unique identifier for the ContentVersion
     */
    String getId();
    
    /**
     * @return Title of the file
     */
    String getTitle();
    
    /**
     * @return Original file path/name on client
     */
    String getPathOnClient();
    
    /**
     * @return Base64 encoded file content
     */
    String getVersionData();
    
    /**
     * @return ID of the parent ContentDocument
     */
    String getContentDocumentId();
    
    /**
     * @return File type (e.g., PDF, PNG, JPEG)
     */
    String getFileType();
    
    /**
     * @return File extension (e.g., pdf, png, jpg)
     */
    String getFileExtension();
    
    /**
     * @return File size in bytes
     */
    Long getContentSize();
    
    /**
     * @return Description of the file
     */
    String getDescription();
    
    /**
     * @return Created date as timestamp
     */
    Long getCreatedDate();
    
    /**
     * @return ID of the user who created the file
     */
    String getCreatedById();
    
    /**
     * @return Last modified date as timestamp
     */
    Long getLastModifiedDate();
    
    /**
     * @return ID of the user who last modified the file
     */
    String getLastModifiedById();
}

