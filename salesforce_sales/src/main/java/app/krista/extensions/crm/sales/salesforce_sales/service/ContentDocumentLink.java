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
 * Service interface for Salesforce ContentDocumentLink.
 * Provides getter methods for all ContentDocumentLink fields.
 */
public interface ContentDocumentLink {
    
    /**
     * @return Unique identifier for the ContentDocumentLink
     */
    String getId();
    
    /**
     * @return ID of the ContentDocument (file)
     */
    String getContentDocumentId();
    
    /**
     * @return ID of the linked entity (e.g., Order, Account, Opportunity)
     */
    String getLinkedEntityId();
    
    /**
     * @return Share type (V=Viewer, C=Collaborator, I=Inferred)
     */
    String getShareType();
    
    /**
     * @return Visibility (AllUsers, InternalUsers, SharedUsers)
     */
    String getVisibility();
}

