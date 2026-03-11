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

package app.krista.extensions.crm.sales.salesforce_sales.catalog;

import app.krista.extension.impl.anno.CatalogRequest;
import app.krista.extension.impl.anno.Domain;
import app.krista.extension.impl.anno.Field;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes.PricebookEntryCatalogType;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes.ProductCatalogType;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.PricebookEntry;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Product;
import app.krista.extensions.crm.sales.salesforce_sales.impl.ContentDocumentLinkDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.impl.ContentVersionDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.impl.PriceBookEntryDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.impl.ProductDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.service.PriceBookEntry;
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
    private final ProductDaoImpl productDao;
    private final PriceBookEntryDaoImpl priceBookEntryDao;

    @Inject
    private OrderManagementArea(ContentVersionDaoImpl contentVersionDao,
                                ContentDocumentLinkDaoImpl contentDocumentLinkDao,
                                FileRepository fileRepository,
                                ProductDaoImpl productDao,
                                PriceBookEntryDaoImpl priceBookEntryDao) {
        this.contentVersionDao = contentVersionDao;
        this.contentDocumentLinkDao = contentDocumentLinkDao;
        this.fileRepository = fileRepository;
        this.productDao = productDao;
        this.priceBookEntryDao = priceBookEntryDao;
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

    @CatalogRequest(
            id = "localDomainRequest_b2c3d4e5-6f7a-8901-bcde-f12345678901",
            name = "Find Or Create Product",
            description = "Searches for a product by product code. If found, returns it. If not found, creates a new product with the given name and code and returns it.",
            area = "Order Management",
            type = CatalogRequest.Type.QUERY_SYSTEM)
    @Field.Desc(name = "Product", type = "Entity(Product)", required = false)
    public Product findOrCreateProduct(
            @Field(name = "Product Name", type = "Text") String productName,
            @Field(name = "Product Code", type = "Text") String productCode) {
        try {
            app.krista.extensions.crm.sales.salesforce_sales.service.Product existing = productDao.getProductByProductCode(productCode);
            if (existing != null) {
                return ProductCatalogType.fromSFProduct(existing);
            }
            Map<String, Object> fields = new HashMap<>();
            fields.put("Name", productName);
            fields.put("Product Code", productCode);
            String id = productDao.addProduct(fields);
            return ProductCatalogType.fromSFProduct(productDao.getProductById(id));
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return null;
        }
    }

    @CatalogRequest(
            id = "localDomainRequest_c3d4e5f6-7a8b-9012-cdef-123456789012",
            name = "Find Or Create Pricebook Entry",
            description = "Searches for a pricebook entry by product and pricebook. If found, returns it. If not found, creates a new pricebook entry and returns it.",
            area = "Order Management",
            type = CatalogRequest.Type.QUERY_SYSTEM)
    @Field.Desc(name = "Pricebook Entry", type = "Entity(Pricebook Entry)", required = false)
    public PricebookEntry findOrCreatePricebookEntry(
            @Field(name = "Pricebook2 Id", type = "Text") String pricebook2Id,
            @Field(name = "Product2 Id", type = "Text") String product2Id,
            @Field(name = "Unit Price", type = "Number") Double unitPrice) {
        try {
            List<PriceBookEntry> existing = priceBookEntryDao.getPriceBookEntryByProductAndPricebook(product2Id, pricebook2Id);

            if (!existing.isEmpty()) {
                PriceBookEntry found = existing.get(0);
                return PricebookEntryCatalogType.fromSFPriceBookEntry(found);
            }

            Map<String, Object> fields = new HashMap<>();
            fields.put("Pricebook Id", pricebook2Id);
            fields.put("Product Id", product2Id);
            fields.put("Unit Price", unitPrice);

            String id = priceBookEntryDao.addPriceBookEntry(fields);

            PricebookEntry created = PricebookEntryCatalogType.fromSFPriceBookEntry(priceBookEntryDao.getPriceBookEntryById(id));
            return created;
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return null;
        }
    }

}

