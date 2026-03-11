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

import app.krista.extensions.crm.sales.salesforce_sales.service.ContentVersion;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ContentVersionDaoImpl.
 * Tests all DAO operations with mocked SalesforceManager.
 */
public class ContentVersionDaoImplTest {

    private static final Gson GSON = new Gson();

    @Mock
    private SalesforceManager salesforceManager;

    private ContentVersionDaoImpl contentVersionDao;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        contentVersionDao = new ContentVersionDaoImpl(salesforceManager);
    }

    @Test
    public void testGetContentVersionById() {
        String contentVersionId = "068xx000000001AAAQ";
        String mockResponse = "{"
                + "\"Id\":\"068xx000000001AAAQ\","
                + "\"Title\":\"Test Document\","
                + "\"PathOnClient\":\"test.pdf\","
                + "\"ContentDocumentId\":\"069xx000000002BAAQ\","
                + "\"FileType\":\"PDF\","
                + "\"FileExtension\":\"pdf\","
                + "\"ContentSize\":1024,"
                + "\"Description\":\"Test description\""
                + "}";

        when(salesforceManager.getRecordById(contentVersionId, "ContentVersion")).thenReturn(mockResponse);

        ContentVersion result = contentVersionDao.getContentVersionById(contentVersionId);

        assertNotNull(result);
        assertEquals("068xx000000001AAAQ", result.getId());
        assertEquals("Test Document", result.getTitle());
        assertEquals("test.pdf", result.getPathOnClient());
        assertEquals("069xx000000002BAAQ", result.getContentDocumentId());
        assertEquals("PDF", result.getFileType());
        assertEquals("pdf", result.getFileExtension());
        assertEquals(Long.valueOf(1024L), result.getContentSize());
        assertEquals("Test description", result.getDescription());

        verify(salesforceManager, times(1)).getRecordById(contentVersionId, "ContentVersion");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetContentVersionByIdNull() {
        contentVersionDao.getContentVersionById(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetContentVersionByIdEmpty() {
        contentVersionDao.getContentVersionById("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetContentVersionByIdNotFound() {
        when(salesforceManager.getRecordById(anyString(), eq("ContentVersion"))).thenReturn(null);
        contentVersionDao.getContentVersionById("068xx000000001AAAQ");
    }

    @Test
    public void testUploadFile() {
        String title = "Test Document";
        String pathOnClient = "test.pdf";
        String base64Data = "JVBERi0xLjQKJeLjz9MK";
        String description = "Test description";
        String mockResponse = "{\"id\":\"068xx000000001AAAQ\",\"success\":true}";

        when(salesforceManager.createRecord(anyString(), eq("ContentVersion"))).thenReturn(mockResponse);

        String result = contentVersionDao.uploadFile(title, pathOnClient, base64Data, description);

        assertEquals("068xx000000001AAAQ", result);
        verify(salesforceManager, times(1)).createRecord(anyString(), eq("ContentVersion"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUploadFileNullTitle() {
        contentVersionDao.uploadFile(null, "test.pdf", "base64data", "description");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUploadFileEmptyTitle() {
        contentVersionDao.uploadFile("", "test.pdf", "base64data", "description");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUploadFileNullPathOnClient() {
        contentVersionDao.uploadFile("Test", null, "base64data", "description");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUploadFileEmptyPathOnClient() {
        contentVersionDao.uploadFile("Test", "", "base64data", "description");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUploadFileNullVersionData() {
        contentVersionDao.uploadFile("Test", "test.pdf", null, "description");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUploadFileEmptyVersionData() {
        contentVersionDao.uploadFile("Test", "test.pdf", "", "description");
    }

    @Test
    public void testUploadFileWithoutDescription() {
        String mockResponse = "{\"id\":\"068xx000000001AAAQ\",\"success\":true}";
        when(salesforceManager.createRecord(anyString(), eq("ContentVersion"))).thenReturn(mockResponse);

        String result = contentVersionDao.uploadFile("Test", "test.pdf", "base64data", null);

        assertEquals("068xx000000001AAAQ", result);
        verify(salesforceManager, times(1)).createRecord(anyString(), eq("ContentVersion"));
    }

    @Test
    public void testUploadFileFromBytes() {
        byte[] fileData = "Test file content".getBytes();
        String mockResponse = "{\"id\":\"068xx000000001AAAQ\",\"success\":true}";

        when(salesforceManager.createRecord(anyString(), eq("ContentVersion"))).thenReturn(mockResponse);

        String result = contentVersionDao.uploadFileFromBytes("Test", "test.txt", fileData, "description");

        assertEquals("068xx000000001AAAQ", result);
        verify(salesforceManager, times(1)).createRecord(anyString(), eq("ContentVersion"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUploadFileFromBytesNullData() {
        contentVersionDao.uploadFileFromBytes("Test", "test.txt", null, "description");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUploadFileFromBytesEmptyData() {
        contentVersionDao.uploadFileFromBytes("Test", "test.txt", new byte[0], "description");
    }

    @Test
    public void testAddContentVersion() {
        Map<String, Object> entityAttributeField = new HashMap<>();
        entityAttributeField.put("Title", "Test Document");
        entityAttributeField.put("Path On Client", "test.pdf");
        entityAttributeField.put("Version Data", "base64data");
        entityAttributeField.put("Description", "Test description");

        String mockResponse = "{\"id\":\"068xx000000001AAAQ\",\"success\":true}";
        when(salesforceManager.createRecord(anyString(), eq("ContentVersion"))).thenReturn(mockResponse);

        String result = contentVersionDao.addContentVersion(entityAttributeField);

        assertEquals("068xx000000001AAAQ", result);
        verify(salesforceManager, times(1)).createRecord(anyString(), eq("ContentVersion"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddContentVersionMissingTitle() {
        Map<String, Object> entityAttributeField = new HashMap<>();
        entityAttributeField.put("Path On Client", "test.pdf");
        entityAttributeField.put("Version Data", "base64data");

        contentVersionDao.addContentVersion(entityAttributeField);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddContentVersionMissingPathOnClient() {
        Map<String, Object> entityAttributeField = new HashMap<>();
        entityAttributeField.put("Title", "Test");
        entityAttributeField.put("Version Data", "base64data");

        contentVersionDao.addContentVersion(entityAttributeField);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddContentVersionMissingVersionData() {
        Map<String, Object> entityAttributeField = new HashMap<>();
        entityAttributeField.put("Title", "Test");
        entityAttributeField.put("Path On Client", "test.pdf");

        contentVersionDao.addContentVersion(entityAttributeField);
    }

    @Test
    public void testDeleteContentVersion() {
        String contentVersionId = "068xx000000001AAAQ";

        doNothing().when(salesforceManager).deleteRecordById(contentVersionId, "ContentVersion");

        contentVersionDao.deleteContentVersion(contentVersionId);

        verify(salesforceManager, times(1)).deleteRecordById(contentVersionId, "ContentVersion");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteContentVersionNullId() {
        contentVersionDao.deleteContentVersion(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteContentVersionEmptyId() {
        contentVersionDao.deleteContentVersion("");
    }

    @Test
    public void testGetContentDocumentId() {
        String contentVersionId = "068xx000000001AAAQ";
        String contentDocumentId = "069xx000000002BAAQ";
        String mockResponse = "{"
                + "\"Id\":\"068xx000000001AAAQ\","
                + "\"ContentDocumentId\":\"069xx000000002BAAQ\""
                + "}";

        when(salesforceManager.getRecordById(contentVersionId, "ContentVersion")).thenReturn(mockResponse);

        String result = contentVersionDao.getContentDocumentId(contentVersionId);

        assertEquals(contentDocumentId, result);
        verify(salesforceManager, times(1)).getRecordById(contentVersionId, "ContentVersion");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetContentDocumentIdNullId() {
        contentVersionDao.getContentDocumentId(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetContentDocumentIdEmptyId() {
        contentVersionDao.getContentDocumentId("");
    }
}

