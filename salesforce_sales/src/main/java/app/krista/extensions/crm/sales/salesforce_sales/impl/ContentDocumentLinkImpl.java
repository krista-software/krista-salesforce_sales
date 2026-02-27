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

