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

import app.krista.extensions.crm.sales.salesforce_sales.model.SFContentDocumentLink;
import app.krista.extensions.crm.sales.salesforce_sales.service.ContentDocumentLink;

/**
 * Implementation of {@link ContentDocumentLink} service interface.
 * Wraps the SFContentDocumentLink model and provides getter methods.
 */
public final class ContentDocumentLinkImpl implements ContentDocumentLink {
    
    private final SFContentDocumentLink sfContentDocumentLink;
    
    /**
     * Constructor that wraps a SFContentDocumentLink model.
     *
     * @param sfContentDocumentLink Salesforce ContentDocumentLink model
     */
    public ContentDocumentLinkImpl(SFContentDocumentLink sfContentDocumentLink) {
        this.sfContentDocumentLink = sfContentDocumentLink;
    }
    
    @Override
    public String getId() {
        return sfContentDocumentLink.id;
    }
    
    @Override
    public String getContentDocumentId() {
        return sfContentDocumentLink.contentDocumentId;
    }
    
    @Override
    public String getLinkedEntityId() {
        return sfContentDocumentLink.linkedEntityId;
    }
    
    @Override
    public String getShareType() {
        return sfContentDocumentLink.shareType;
    }
    
    @Override
    public String getVisibility() {
        return sfContentDocumentLink.visibility;
    }
}

