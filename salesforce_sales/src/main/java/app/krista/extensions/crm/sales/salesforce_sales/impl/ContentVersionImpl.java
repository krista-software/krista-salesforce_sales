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

