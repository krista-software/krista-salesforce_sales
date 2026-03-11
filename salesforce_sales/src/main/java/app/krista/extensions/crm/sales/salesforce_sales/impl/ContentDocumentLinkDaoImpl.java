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
import app.krista.extensions.crm.sales.salesforce_sales.service.ContentDocumentLinkDao;
import app.krista.extensions.crm.sales.salesforce_sales.util.SalesforceListResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of ContentDocumentLinkDao interface.
 * Handles all ContentDocumentLink operations with Salesforce.
 */
@Service
public class ContentDocumentLinkDaoImpl implements ContentDocumentLinkDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentDocumentLinkDaoImpl.class);
    private static final Gson GSON = new Gson();
    private static final String CONTENT_DOCUMENT_LINK = "ContentDocumentLink";

    // Define all fields to query from Salesforce ContentDocumentLink object
    private static final String QUERY_FIELDS = "Id, ContentDocumentId, LinkedEntityId, ShareType, Visibility";

    private final SalesforceManager salesforceManager;

    @Inject
    public ContentDocumentLinkDaoImpl(SalesforceManager salesforceManager) {
        this.salesforceManager = salesforceManager;
    }

    @Override
    public ContentDocumentLink getContentDocumentLinkById(String id) {
        try {
            if (id == null || id.isEmpty()) {
                throw new IllegalArgumentException("ContentDocumentLink ID is required.");
            }
            
            String response = salesforceManager.getRecordById(id, CONTENT_DOCUMENT_LINK);
            if (response == null || response.isEmpty()) {
                LOGGER.warn("No content document link found with ID: {}", id);
                throw new IllegalArgumentException("ContentDocumentLink not found with ID: " + id);
            }
            
            SFContentDocumentLink sfLink = GSON.fromJson(response, SFContentDocumentLink.class);
            return new ContentDocumentLinkImpl(sfLink);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error getting content document link: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error getting content document link by ID: {}", e.getMessage(), e);
            throw new IllegalArgumentException("Failed to get content document link: " + e.getMessage(), e);
        }
    }

    @Override
    public String linkFileToRecord(String contentDocumentId, String linkedEntityId, String shareType, String visibility) {
        try {
            // Validate required fields
            if (contentDocumentId == null || contentDocumentId.isEmpty()) {
                throw new IllegalArgumentException("ContentDocumentId is required to link a file.");
            }
            if (linkedEntityId == null || linkedEntityId.isEmpty()) {
                throw new IllegalArgumentException("LinkedEntityId is required to link a file.");
            }
            
            // Set defaults if not provided
            String finalShareType = (shareType == null || shareType.isEmpty()) ? "V" : shareType;
            String finalVisibility = (visibility == null || visibility.isEmpty()) ? "AllUsers" : visibility;
            
            // Validate ShareType
            if (!finalShareType.equals("V") && !finalShareType.equals("C") && !finalShareType.equals("I")) {
                throw new IllegalArgumentException("ShareType must be V (Viewer), C (Collaborator), or I (Inferred).");
            }
            
            // Validate Visibility
            if (!finalVisibility.equals("AllUsers") && !finalVisibility.equals("InternalUsers") && !finalVisibility.equals("SharedUsers")) {
                throw new IllegalArgumentException("Visibility must be AllUsers, InternalUsers, or SharedUsers.");
            }
            
            // Create ContentDocumentLink
            Map<String, Object> linkData = new HashMap<>();
            linkData.put("ContentDocumentId", contentDocumentId);
            linkData.put("LinkedEntityId", linkedEntityId);
            linkData.put("ShareType", finalShareType);
            linkData.put("Visibility", finalVisibility);
            
            String response = salesforceManager.createRecord(GSON.toJson(linkData), CONTENT_DOCUMENT_LINK);
            Map<String, Object> responseMap = GSON.fromJson(response, Map.class);
            String linkId = String.valueOf(responseMap.get("id"));
            
            LOGGER.info("Successfully linked file {} to record {} with link ID: {}", contentDocumentId, linkedEntityId, linkId);
            return linkId;
        } catch (IllegalArgumentException e) {
            LOGGER.error("Validation error linking file to record: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error linking file to record: {}", e.getMessage(), e);
            throw new IllegalArgumentException("Failed to link file to record: " + e.getMessage(), e);
        }
    }

    @Override
    public String linkFileToRecord(String contentDocumentId, String linkedEntityId) {
        return linkFileToRecord(contentDocumentId, linkedEntityId, "V", "AllUsers");
    }

    @Override
    public String addContentDocumentLink(Map<String, Object> entityAttributeField) {
        try {
            SFContentDocumentLink link = SFContentDocumentLink.from(entityAttributeField);

            if (link.contentDocumentId == null || link.contentDocumentId.isEmpty()) {
                throw new IllegalArgumentException("ContentDocumentId is required.");
            }
            if (link.linkedEntityId == null || link.linkedEntityId.isEmpty()) {
                throw new IllegalArgumentException("LinkedEntityId is required.");
            }

            return linkFileToRecord(link.contentDocumentId, link.linkedEntityId, link.shareType, link.visibility);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Validation error adding content document link: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error adding content document link: {}", e.getMessage(), e);
            throw new IllegalArgumentException("Failed to add content document link: " + e.getMessage(), e);
        }
    }

    @Override
    public List<ContentDocumentLink> getLinksForRecord(String linkedEntityId) {
        try {
            if (linkedEntityId == null || linkedEntityId.isEmpty()) {
                throw new IllegalArgumentException("LinkedEntityId is required.");
            }

            String query = String.format("SELECT %s FROM ContentDocumentLink WHERE LinkedEntityId = '%s'",
                                       QUERY_FIELDS, linkedEntityId);
            String response = salesforceManager.getRecordByQuery(query);

            if (response == null || response.isEmpty()) {
                LOGGER.info("No files linked to record: {}", linkedEntityId);
                return List.of();
            }

            SalesforceListResponse<SFContentDocumentLink> listResponse = GSON.fromJson(
                response,
                new TypeToken<SalesforceListResponse<SFContentDocumentLink>>(){}.getType()
            );

            List<ContentDocumentLink> links = listResponse.records.stream()
                .map(ContentDocumentLinkImpl::new)
                .collect(Collectors.toList());

            LOGGER.info("Found {} file(s) linked to record: {}", links.size(), linkedEntityId);
            return links;
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error getting links for record: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error getting links for record: {}", e.getMessage(), e);
            throw new IllegalArgumentException("Failed to get links for record: " + e.getMessage(), e);
        }
    }

    @Override
    public List<ContentDocumentLink> getLinksForDocument(String contentDocumentId) {
        try {
            if (contentDocumentId == null || contentDocumentId.isEmpty()) {
                throw new IllegalArgumentException("ContentDocumentId is required.");
            }

            String query = String.format("SELECT %s FROM ContentDocumentLink WHERE ContentDocumentId = '%s'",
                                       QUERY_FIELDS, contentDocumentId);
            String response = salesforceManager.getRecordByQuery(query);

            if (response == null || response.isEmpty()) {
                LOGGER.info("No records linked to file: {}", contentDocumentId);
                return List.of();
            }

            SalesforceListResponse<SFContentDocumentLink> listResponse = GSON.fromJson(
                response,
                new TypeToken<SalesforceListResponse<SFContentDocumentLink>>(){}.getType()
            );

            List<ContentDocumentLink> links = listResponse.records.stream()
                .map(ContentDocumentLinkImpl::new)
                .collect(Collectors.toList());

            LOGGER.info("Found {} record(s) linked to file: {}", links.size(), contentDocumentId);
            return links;
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error getting links for document: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error getting links for document: {}", e.getMessage(), e);
            throw new IllegalArgumentException("Failed to get links for document: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteLink(String id) {
        try {
            if (id == null || id.isEmpty()) {
                throw new IllegalArgumentException("ContentDocumentLink ID is required for deletion.");
            }

            salesforceManager.deleteRecordById(id, CONTENT_DOCUMENT_LINK);
            LOGGER.info("Successfully deleted ContentDocumentLink with ID: {}", id);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Validation error deleting link: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error deleting link: {}", e.getMessage(), e);
            throw new IllegalArgumentException("Failed to delete link: " + e.getMessage(), e);
        }
    }
}

