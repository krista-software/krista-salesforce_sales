package app.krista.extensions.crm.sales.salesforce_sales.impl;

import app.krista.extensions.crm.sales.salesforce_sales.model.SFContentVersion;
import app.krista.extensions.crm.sales.salesforce_sales.service.ContentVersion;
import app.krista.extensions.crm.sales.salesforce_sales.service.ContentVersionDao;
import com.google.gson.Gson;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of ContentVersionDao interface.
 * Handles all ContentVersion file upload/download operations with Salesforce.
 */
@Service
public class ContentVersionDaoImpl implements ContentVersionDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentVersionDaoImpl.class);
    private static final Gson GSON = new Gson();
    private static final String CONTENT_VERSION = "ContentVersion";
    private static final long MAX_FILE_SIZE_BYTES = 37_500_000L;

    private final SalesforceManager salesforceManager;

    @Inject
    public ContentVersionDaoImpl(SalesforceManager salesforceManager) {
        this.salesforceManager = salesforceManager;
    }

    @Override
    public ContentVersion getContentVersionById(String id) {
        try {
            if (id == null || id.isEmpty()) {
                throw new IllegalArgumentException("ContentVersion ID is required.");
            }
            
            String response = salesforceManager.getRecordById(id, CONTENT_VERSION);
            if (response == null || response.isEmpty()) {
                LOGGER.warn("No content version found with ID: {}", id);
                throw new IllegalArgumentException("ContentVersion not found with ID: " + id);
            }
            
            SFContentVersion sfContentVersion = GSON.fromJson(response, SFContentVersion.class);
            return new ContentVersionImpl(sfContentVersion);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error getting content version: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error getting content version by ID: {}", e.getMessage(), e);
            throw new IllegalArgumentException("Failed to get content version: " + e.getMessage(), e);
        }
    }

    @Override
    public String uploadFile(String title, String pathOnClient, String base64Data, String description) {
        try {
            if (title == null || title.isEmpty()) {
                throw new IllegalArgumentException("Title is required for file upload.");
            }
            if (pathOnClient == null || pathOnClient.isEmpty()) {
                throw new IllegalArgumentException("PathOnClient is required for file upload.");
            }
            if (base64Data == null || base64Data.isEmpty()) {
                throw new IllegalArgumentException("File data (VersionData) is required for file upload.");
            }
            
            if (base64Data.length() > MAX_FILE_SIZE_BYTES * 1.33) {
                throw new IllegalArgumentException("File size exceeds the maximum limit of 37.5 MB.");
            }
            
            Map<String, Object> contentVersionData = new HashMap<>();
            contentVersionData.put("Title", title);
            contentVersionData.put("PathOnClient", pathOnClient);
            contentVersionData.put("VersionData", base64Data);
            if (description != null && !description.isEmpty()) {
                contentVersionData.put("Description", description);
            }
            
            String response = salesforceManager.createRecord(GSON.toJson(contentVersionData), CONTENT_VERSION);
            Map<String, Object> responseMap = GSON.fromJson(response, Map.class);
            String contentVersionId = String.valueOf(responseMap.get("id"));
            
            LOGGER.info("Successfully uploaded file '{}' with ContentVersion ID: {}", title, contentVersionId);
            return contentVersionId;
        } catch (IllegalArgumentException e) {
            LOGGER.error("Validation error uploading file: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error uploading file: {}", e.getMessage(), e);
            throw new IllegalArgumentException("Failed to upload file: " + e.getMessage(), e);
        }
    }

    @Override
    public String uploadFileFromBytes(String title, String pathOnClient, byte[] fileData, String description) {
        try {
            if (fileData == null || fileData.length == 0) {
                throw new IllegalArgumentException("File data cannot be null or empty.");
            }
            
            if (fileData.length > MAX_FILE_SIZE_BYTES) {
                throw new IllegalArgumentException("File size exceeds the maximum limit of 37.5 MB.");
            }
            
            String base64Data = Base64.getEncoder().encodeToString(fileData);
            return uploadFile(title, pathOnClient, base64Data, description);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Validation error uploading file from bytes: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error uploading file from bytes: {}", e.getMessage(), e);
            throw new IllegalArgumentException("Failed to upload file from bytes: " + e.getMessage(), e);
        }
    }

    @Override
    public String addContentVersion(Map<String, Object> entityAttributeField) {
        try {
            SFContentVersion contentVersion = SFContentVersion.from(entityAttributeField);

            if (contentVersion.title == null || contentVersion.title.isEmpty()) {
                throw new IllegalArgumentException("Title is required for file upload.");
            }
            if (contentVersion.pathOnClient == null || contentVersion.pathOnClient.isEmpty()) {
                throw new IllegalArgumentException("PathOnClient is required for file upload.");
            }
            if (contentVersion.versionData == null || contentVersion.versionData.isEmpty()) {
                throw new IllegalArgumentException("VersionData is required for file upload.");
            }

            return uploadFile(contentVersion.title, contentVersion.pathOnClient,
                            contentVersion.versionData, contentVersion.description);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Validation error adding content version: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error adding content version: {}", e.getMessage(), e);
            throw new IllegalArgumentException("Failed to add content version: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteContentVersion(String id) {
        try {
            if (id == null || id.isEmpty()) {
                throw new IllegalArgumentException("ContentVersion ID is required for deletion.");
            }

            salesforceManager.deleteRecordById(id, CONTENT_VERSION);
            LOGGER.info("Successfully deleted ContentVersion with ID: {}", id);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Validation error deleting content version: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error deleting content version: {}", e.getMessage(), e);
            throw new IllegalArgumentException("Failed to delete content version: " + e.getMessage(), e);
        }
    }

    @Override
    public String getContentDocumentId(String contentVersionId) {
        try {
            if (contentVersionId == null || contentVersionId.isEmpty()) {
                throw new IllegalArgumentException("ContentVersion ID is required.");
            }

            ContentVersion contentVersion = getContentVersionById(contentVersionId);
            String contentDocumentId = contentVersion.getContentDocumentId();

            if (contentDocumentId == null || contentDocumentId.isEmpty()) {
                throw new IllegalArgumentException("ContentDocument ID not found for ContentVersion: " + contentVersionId);
            }

            LOGGER.info("Retrieved ContentDocument ID: {} for ContentVersion: {}", contentDocumentId, contentVersionId);
            return contentDocumentId;
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error getting content document ID: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error getting content document ID: {}", e.getMessage(), e);
            throw new IllegalArgumentException("Failed to get content document ID: " + e.getMessage(), e);
        }
    }
}

