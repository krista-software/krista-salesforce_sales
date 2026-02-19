package app.krista.extensions.crm.sales.salesforce_sales.service;

import java.util.List;
import java.util.Map;

/**
 * Data Access Object interface for Salesforce ContentDocumentLink operations.
 * Handles linking files to records and managing file-record relationships.
 */
public interface ContentDocumentLinkDao {
    
    /**
     * Returns {@link ContentDocumentLink} object from Salesforce by ID.
     *
     * @param id ContentDocumentLink ID
     * @return {@link ContentDocumentLink} object
     * @throws IllegalArgumentException if link not found
     */
    ContentDocumentLink getContentDocumentLinkById(String id);
    
    /**
     * Links a file (ContentDocument) to a record (e.g., Order, Account).
     *
     * @param contentDocumentId ID of the file to link
     * @param linkedEntityId ID of the record to link the file to
     * @param shareType Share type (V=Viewer, C=Collaborator, I=Inferred). Default: V
     * @param visibility Visibility (AllUsers, InternalUsers, SharedUsers). Default: AllUsers
     * @return ContentDocumentLink ID
     * @throws IllegalArgumentException if required fields are missing
     */
    String linkFileToRecord(String contentDocumentId, String linkedEntityId, String shareType, String visibility);
    
    /**
     * Links a file to a record using default settings (ShareType=V, Visibility=AllUsers).
     *
     * @param contentDocumentId ID of the file to link
     * @param linkedEntityId ID of the record to link the file to
     * @return ContentDocumentLink ID
     * @throws IllegalArgumentException if required fields are missing
     */
    String linkFileToRecord(String contentDocumentId, String linkedEntityId);
    
    /**
     * Creates a ContentDocumentLink using a Map of field values.
     * Supports dynamic field mapping.
     *
     * @param entityAttributeField Map containing link field values
     * @return ContentDocumentLink ID
     * @throws IllegalArgumentException if required fields are missing
     */
    String addContentDocumentLink(Map<String, Object> entityAttributeField);
    
    /**
     * Gets all files linked to a specific record.
     *
     * @param linkedEntityId ID of the record (e.g., Order ID)
     * @return List of ContentDocumentLink objects
     */
    List<ContentDocumentLink> getLinksForRecord(String linkedEntityId);
    
    /**
     * Gets all records linked to a specific file.
     *
     * @param contentDocumentId ID of the file (ContentDocument)
     * @return List of ContentDocumentLink objects
     */
    List<ContentDocumentLink> getLinksForDocument(String contentDocumentId);
    
    /**
     * Deletes a ContentDocumentLink (unlinks a file from a record).
     *
     * @param id ContentDocumentLink ID to delete
     * @throws IllegalArgumentException if ID is null or empty
     */
    void deleteLink(String id);
}

