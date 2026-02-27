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

