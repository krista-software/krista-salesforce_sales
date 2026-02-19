package app.krista.extensions.crm.sales.salesforce_sales.service;

import java.util.Map;

/**
 * Data Access Object interface for Salesforce ContentVersion operations.
 * Handles file upload, retrieval, and deletion operations.
 */
public interface ContentVersionDao {
    
    /**
     * Returns {@link ContentVersion} object from Salesforce by ID.
     *
     * @param id ContentVersion ID
     * @return {@link ContentVersion} object
     * @throws IllegalArgumentException if content version not found
     */
    ContentVersion getContentVersionById(String id);
    
    /**
     * Uploads a file to Salesforce using base64 encoded data.
     *
     * @param title Title of the file
     * @param pathOnClient Original file path/name (e.g., "document.pdf")
     * @param base64Data Base64 encoded file content
     * @param description Optional description of the file
     * @return ContentVersion ID of the uploaded file
     * @throws IllegalArgumentException if required fields are missing or invalid
     */
    String uploadFile(String title, String pathOnClient, String base64Data, String description);
    
    /**
     * Uploads a file to Salesforce using byte array.
     * Automatically converts byte array to base64.
     *
     * @param title Title of the file
     * @param pathOnClient Original file path/name (e.g., "document.pdf")
     * @param fileData File content as byte array
     * @param description Optional description of the file
     * @return ContentVersion ID of the uploaded file
     * @throws IllegalArgumentException if required fields are missing or invalid
     */
    String uploadFileFromBytes(String title, String pathOnClient, byte[] fileData, String description);
    
    /**
     * Uploads a file using a Map of field values.
     * Supports dynamic field mapping.
     *
     * @param entityAttributeField Map containing file field values
     * @return ContentVersion ID of the uploaded file
     * @throws IllegalArgumentException if required fields are missing
     */
    String addContentVersion(Map<String, Object> entityAttributeField);
    
    /**
     * Deletes a ContentVersion from Salesforce.
     *
     * @param id ContentVersion ID to delete
     * @throws IllegalArgumentException if ID is null or empty
     */
    void deleteContentVersion(String id);
    
    /**
     * Gets the ContentDocument ID for a given ContentVersion ID.
     * Useful for linking files to records.
     *
     * @param contentVersionId ContentVersion ID
     * @return ContentDocument ID
     * @throws IllegalArgumentException if content version not found
     */
    String getContentDocumentId(String contentVersionId);
}

