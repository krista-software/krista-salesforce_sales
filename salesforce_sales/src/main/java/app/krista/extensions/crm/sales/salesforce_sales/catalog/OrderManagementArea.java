package app.krista.extensions.crm.sales.salesforce_sales.catalog;

import app.krista.extension.impl.anno.CatalogRequest;
import app.krista.extension.impl.anno.Domain;
import app.krista.extension.impl.anno.Field;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes.CatalogTypes;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Order;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.stores.OrderStore;
import app.krista.extensions.crm.sales.salesforce_sales.impl.ContentDocumentLinkDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.impl.ContentVersionDaoImpl;
import app.krista.ksdk.files.FileHandle;
import app.krista.ksdk.files.FileRepository;
import app.krista.model.base.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Catalog area for Order Management operations.
 * Provides catalog requests for file operations (upload and link).
 * Create and Update operations are handled by OrderStore and OrderItemStore (EntityStore pattern).
 */
@Domain(id = "catEntryDomain_086f33da-aaea-4d0c-816f-3d5f20b4a273",
        name = "Sales",
        ecosystemId = "catEntryEcosystem_8a0e8ce7-6289-492f-9202-52284024280c",
        ecosystemName = "CRM",
        ecosystemVersion = "db320c64-6f6f-4c9e-8daa-41d3f796d6a6")
public final class OrderManagementArea {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderManagementArea.class);

    private final ContentVersionDaoImpl contentVersionDao;
    private final ContentDocumentLinkDaoImpl contentDocumentLinkDao;
    private final FileRepository fileRepository;
    private final OrderStore orderStore;

    @Inject
    private OrderManagementArea(ContentVersionDaoImpl contentVersionDao,
                                ContentDocumentLinkDaoImpl contentDocumentLinkDao,
                                FileRepository fileRepository,
                                OrderStore orderStore) {
        this.contentVersionDao = contentVersionDao;
        this.contentDocumentLinkDao = contentDocumentLinkDao;
        this.fileRepository = fileRepository;
        this.orderStore = orderStore;
    }

    @CatalogRequest(
            id = "localDomainRequest_file_upload_001",
            name = "Upload PO File",
            description = "Upload a Purchase Order file (PDF, Word, Excel, etc.) to Salesforce. Returns the Content Document ID for linking.",
            area = "Order Management",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field.Desc(name = "Content Document Id", type = "Text", required = false)
    public String uploadPOFile(
            @Field.File(name = "Upload File", multipleFileUpload = false, required = true) File uploadFile,
            @Field(name = "Description", type = "Text", required = false) String description) {
        try {
            if (uploadFile == null) {
                throw new IllegalArgumentException("Upload File is required to upload a file to Salesforce.");
            }

            // Get file details from the File object
            String fileName = uploadFile.getFileName();
            if (fileName == null || fileName.isEmpty()) {
                throw new IllegalArgumentException("File name cannot be empty.");
            }

            // Extract file title (name without extension) and extension
            String fileTitle;
            String fileExtension;
            int lastDotIndex = fileName.lastIndexOf('.');
            if (lastDotIndex > 0) {
                fileTitle = fileName.substring(0, lastDotIndex);
                fileExtension = fileName.substring(lastDotIndex + 1);
            } else {
                fileTitle = fileName;
                fileExtension = "pdf"; // Default extension
            }

            // Read file content and convert to Base64
            String fileDataBase64;
            try (FileHandle fileHandle = fileRepository.getFile(uploadFile);
                 InputStream inputStream = fileHandle.getContent()) {
                byte[] fileBytes = inputStream.readAllBytes();
                fileDataBase64 = Base64.getEncoder().encodeToString(fileBytes);
            } catch (Exception e) {
                LOGGER.error("Error reading file content: {}", e.getMessage(), e);
                throw new IllegalArgumentException("Failed to read file content: " + e.getMessage(), e);
            }

            // Determine path on client (filename with extension)
            String pathOnClient = fileTitle + "." + fileExtension;

            // Upload file to Salesforce using the DAO
            String contentVersionId = contentVersionDao.uploadFile(fileTitle, pathOnClient, fileDataBase64, description);
            LOGGER.info("Successfully uploaded file '{}' with ContentVersion ID: {}", fileName, contentVersionId);

            // Get the ContentDocumentId from the ContentVersion
            app.krista.extensions.crm.sales.salesforce_sales.service.ContentVersion contentVersion =
                    contentVersionDao.getContentVersionById(contentVersionId);
            String contentDocumentId = contentVersion.getContentDocumentId();

            LOGGER.info("File '{}' uploaded with ContentDocument ID: {}", fileName, contentDocumentId);
            return contentDocumentId;

        } catch (IllegalArgumentException cause) {
            LOGGER.error("Error uploading file: {}", cause.getMessage(), cause);
            throw cause;
        }
    }

    @CatalogRequest(
            id = "localDomainRequest_file_link_001",
            name = "Link File to Order",
            description = "Link an uploaded file (ContentDocument) to an Order. Creates a ContentDocumentLink relationship.",
            area = "Order Management",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field.Desc(name = "Link Id", type = "Text", required = false)
    public String linkFileToOrder(
            @Field(name = "Content Document Id", type = "Text") String contentDocumentId,
            @Field(name = "Order Id", type = "Text") String orderId,
            @Field(name = "Share Type", type = "Text", required = false) String shareType,
            @Field(name = "Visibility", type = "Text", required = false) String visibility) {
        try {
            if (contentDocumentId == null || contentDocumentId.isEmpty()) {
                throw new IllegalArgumentException("Content Document Id is required to link a file.");
            }

            if (orderId == null || orderId.isEmpty()) {
                throw new IllegalArgumentException("Order Id is required to link a file.");
            }

            // Use default values if not provided (ShareType=V, Visibility=AllUsers)
            String linkId;
            if (shareType != null && !shareType.isEmpty() && visibility != null && !visibility.isEmpty()) {
                linkId = contentDocumentLinkDao.linkFileToRecord(contentDocumentId, orderId, shareType, visibility);
            } else {
                linkId = contentDocumentLinkDao.linkFileToRecord(contentDocumentId, orderId);
            }

            LOGGER.info("Successfully linked file {} to order {} with link ID: {}", contentDocumentId, orderId, linkId);
            return linkId;

        } catch (IllegalArgumentException cause) {
            LOGGER.error("Error linking file to order: {}", cause.getMessage(), cause);
            throw cause;
        }
    }

    /**
     * Create an Order with custom fields.
     * This catalog request allows you to create an Order with standard fields and any custom fields.
     * Custom fields should be provided as key-value pairs in the "Custom Fields" composite list.
     *
     * Example custom fields:
     * - custom_field__c: 2024-02-15
     * - PO_Number__c: PO-12345
     * - Extraction_Status__c: Completed
     */
    @CatalogRequest(
            id = "localDomainRequest_create_order_with_custom_fields",
            name = "Create Order With Custom Fields",
            description = "Create an Order with standard and custom fields. Custom fields ending with __c are supported dynamically.",
            area = "Order Management",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field.Desc(name = "Order", type = "Entity(Order)", required = false)
    public Order createOrderWithCustomFields(
            @Field(name = "Account Id", type = "Text", required = true) String accountId,
            @Field(name = "Effective Date", type = "Date", required = true) Long effectiveDate,
            @Field(name = "Status", type = "PickOne", required = true) String status,
            @Field(name = "Description", type = "Text", required = false) String description,
            @Field.Desc(name = "Custom Fields", type = "[ Composite ]", required = false) List<Object> customFields) {
        try {
            System.out.println("========================================");
            System.out.println("createOrderWithCustomFields() CALLED");
            System.out.println("Account Id: " + accountId);
            System.out.println("Effective Date: " + effectiveDate);
            System.out.println("Status: " + status);
            System.out.println("Description: " + description);
            System.out.println("Custom Fields: " + customFields);
            System.out.println("========================================");

            // Build the order fields map with standard fields
            Map<String, Object> orderFields = new HashMap<>();
            orderFields.put("Account Id", accountId);
            orderFields.put("Effective Date", effectiveDate);
            orderFields.put("Status", status);
            if (description != null && !description.isEmpty()) {
                orderFields.put("Description", description);
            }

            // Add custom fields from the composite list
            if (customFields != null && !customFields.isEmpty()) {
                Map<String, Object> customFieldsMap = CatalogTypes.getFieldMapFromList(customFields);
                System.out.println("Parsed custom fields map: " + customFieldsMap);
                orderFields.putAll(customFieldsMap);
            }

            System.out.println("Final order fields map: " + orderFields);
            LOGGER.info("Creating order with fields: {}", orderFields);

            // Create the order using OrderStore
            Order createdOrder = orderStore.create(orderFields);

            System.out.println("Order created successfully: " + createdOrder);
            LOGGER.info("Successfully created order with ID: {}", createdOrder != null ? createdOrder.id : "null");

            return createdOrder;

        } catch (IllegalArgumentException cause) {
            System.out.println("ERROR creating order with custom fields: " + cause.getMessage());
            cause.printStackTrace();
            LOGGER.error("Error creating order with custom fields: {}", cause.getMessage(), cause);
            throw cause;
        }
    }
}

