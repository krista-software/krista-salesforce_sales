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

import app.krista.extensions.crm.sales.salesforce_sales.service.ContentDocumentLink;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ContentDocumentLinkDaoImpl.
 * Tests all DAO operations with mocked SalesforceManager.
 */
public class ContentDocumentLinkDaoImplTest {

    private static final Gson GSON = new Gson();

    @Mock
    private SalesforceManager salesforceManager;

    private ContentDocumentLinkDaoImpl contentDocumentLinkDao;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        contentDocumentLinkDao = new ContentDocumentLinkDaoImpl(salesforceManager);
    }

    @Test
    public void testGetContentDocumentLinkById() {
        String linkId = "06Axx000000001AAAQ";
        String mockResponse = "{"
                + "\"Id\":\"06Axx000000001AAAQ\","
                + "\"ContentDocumentId\":\"069xx000000002BAAQ\","
                + "\"LinkedEntityId\":\"801xx000000003CAAQ\","
                + "\"ShareType\":\"V\","
                + "\"Visibility\":\"AllUsers\""
                + "}";

        when(salesforceManager.getRecordById(linkId, "ContentDocumentLink")).thenReturn(mockResponse);

        ContentDocumentLink result = contentDocumentLinkDao.getContentDocumentLinkById(linkId);

        assertNotNull(result);
        assertEquals("06Axx000000001AAAQ", result.getId());
        assertEquals("069xx000000002BAAQ", result.getContentDocumentId());
        assertEquals("801xx000000003CAAQ", result.getLinkedEntityId());
        assertEquals("V", result.getShareType());
        assertEquals("AllUsers", result.getVisibility());

        verify(salesforceManager, times(1)).getRecordById(linkId, "ContentDocumentLink");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetContentDocumentLinkByIdNull() {
        contentDocumentLinkDao.getContentDocumentLinkById(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetContentDocumentLinkByIdEmpty() {
        contentDocumentLinkDao.getContentDocumentLinkById("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetContentDocumentLinkByIdNotFound() {
        when(salesforceManager.getRecordById(anyString(), eq("ContentDocumentLink"))).thenReturn(null);
        contentDocumentLinkDao.getContentDocumentLinkById("06Axx000000001AAAQ");
    }

    @Test
    public void testLinkFileToRecordWithDefaults() {
        String contentDocumentId = "069xx000000002BAAQ";
        String linkedEntityId = "801xx000000003CAAQ";
        String mockResponse = "{\"id\":\"06Axx000000001AAAQ\",\"success\":true}";

        when(salesforceManager.createRecord(anyString(), eq("ContentDocumentLink"))).thenReturn(mockResponse);

        String result = contentDocumentLinkDao.linkFileToRecord(contentDocumentId, linkedEntityId);

        assertEquals("06Axx000000001AAAQ", result);
        verify(salesforceManager, times(1)).createRecord(anyString(), eq("ContentDocumentLink"));
    }

    @Test
    public void testLinkFileToRecordWithCustomSettings() {
        String contentDocumentId = "069xx000000002BAAQ";
        String linkedEntityId = "801xx000000003CAAQ";
        String shareType = "C";
        String visibility = "InternalUsers";
        String mockResponse = "{\"id\":\"06Axx000000001AAAQ\",\"success\":true}";

        when(salesforceManager.createRecord(anyString(), eq("ContentDocumentLink"))).thenReturn(mockResponse);

        String result = contentDocumentLinkDao.linkFileToRecord(contentDocumentId, linkedEntityId, shareType, visibility);

        assertEquals("06Axx000000001AAAQ", result);
        verify(salesforceManager, times(1)).createRecord(anyString(), eq("ContentDocumentLink"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLinkFileToRecordNullContentDocumentId() {
        contentDocumentLinkDao.linkFileToRecord(null, "801xx000000003CAAQ", "V", "AllUsers");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLinkFileToRecordEmptyContentDocumentId() {
        contentDocumentLinkDao.linkFileToRecord("", "801xx000000003CAAQ", "V", "AllUsers");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLinkFileToRecordNullLinkedEntityId() {
        contentDocumentLinkDao.linkFileToRecord("069xx000000002BAAQ", null, "V", "AllUsers");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLinkFileToRecordEmptyLinkedEntityId() {
        contentDocumentLinkDao.linkFileToRecord("069xx000000002BAAQ", "", "V", "AllUsers");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLinkFileToRecordInvalidShareType() {
        contentDocumentLinkDao.linkFileToRecord("069xx000000002BAAQ", "801xx000000003CAAQ", "X", "AllUsers");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLinkFileToRecordInvalidVisibility() {
        contentDocumentLinkDao.linkFileToRecord("069xx000000002BAAQ", "801xx000000003CAAQ", "V", "InvalidVisibility");
    }

    @Test
    public void testAddContentDocumentLink() {
        Map<String, Object> entityAttributeField = new HashMap<>();
        entityAttributeField.put("Content Document Id", "069xx000000002BAAQ");
        entityAttributeField.put("Linked Entity Id", "801xx000000003CAAQ");
        entityAttributeField.put("Share Type", "V");
        entityAttributeField.put("Visibility", "AllUsers");

        String mockResponse = "{\"id\":\"06Axx000000001AAAQ\",\"success\":true}";
        when(salesforceManager.createRecord(anyString(), eq("ContentDocumentLink"))).thenReturn(mockResponse);

        String result = contentDocumentLinkDao.addContentDocumentLink(entityAttributeField);

        assertEquals("06Axx000000001AAAQ", result);
        verify(salesforceManager, times(1)).createRecord(anyString(), eq("ContentDocumentLink"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddContentDocumentLinkMissingContentDocumentId() {
        Map<String, Object> entityAttributeField = new HashMap<>();
        entityAttributeField.put("Linked Entity Id", "801xx000000003CAAQ");

        contentDocumentLinkDao.addContentDocumentLink(entityAttributeField);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddContentDocumentLinkMissingLinkedEntityId() {
        Map<String, Object> entityAttributeField = new HashMap<>();
        entityAttributeField.put("Content Document Id", "069xx000000002BAAQ");

        contentDocumentLinkDao.addContentDocumentLink(entityAttributeField);
    }

    @Test
    public void testGetLinksForRecord() {
        String linkedEntityId = "801xx000000003CAAQ";
        String mockResponse = "{"
                + "\"totalSize\":2,"
                + "\"done\":true,"
                + "\"records\":["
                + "{\"Id\":\"06Axx000000001AAAQ\",\"ContentDocumentId\":\"069xx000000002BAAQ\",\"LinkedEntityId\":\"801xx000000003CAAQ\",\"ShareType\":\"V\",\"Visibility\":\"AllUsers\"},"
                + "{\"Id\":\"06Axx000000002AAAQ\",\"ContentDocumentId\":\"069xx000000003BAAQ\",\"LinkedEntityId\":\"801xx000000003CAAQ\",\"ShareType\":\"V\",\"Visibility\":\"AllUsers\"}"
                + "]"
                + "}";

        when(salesforceManager.getRecordByQuery(anyString())).thenReturn(mockResponse);

        List<ContentDocumentLink> result = contentDocumentLinkDao.getLinksForRecord(linkedEntityId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("06Axx000000001AAAQ", result.get(0).getId());
        assertEquals("06Axx000000002AAAQ", result.get(1).getId());

        verify(salesforceManager, times(1)).getRecordByQuery(anyString());
    }

    @Test
    public void testGetLinksForRecordEmpty() {
        String linkedEntityId = "801xx000000003CAAQ";
        String mockResponse = "{\"totalSize\":0,\"done\":true,\"records\":[]}";

        when(salesforceManager.getRecordByQuery(anyString())).thenReturn(mockResponse);

        List<ContentDocumentLink> result = contentDocumentLinkDao.getLinksForRecord(linkedEntityId);

        assertNotNull(result);
        assertEquals(0, result.size());

        verify(salesforceManager, times(1)).getRecordByQuery(anyString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLinksForRecordNullId() {
        contentDocumentLinkDao.getLinksForRecord(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLinksForRecordEmptyId() {
        contentDocumentLinkDao.getLinksForRecord("");
    }

    @Test
    public void testGetLinksForDocument() {
        String contentDocumentId = "069xx000000002BAAQ";
        String mockResponse = "{"
                + "\"totalSize\":2,"
                + "\"done\":true,"
                + "\"records\":["
                + "{\"Id\":\"06Axx000000001AAAQ\",\"ContentDocumentId\":\"069xx000000002BAAQ\",\"LinkedEntityId\":\"801xx000000003CAAQ\",\"ShareType\":\"V\",\"Visibility\":\"AllUsers\"},"
                + "{\"Id\":\"06Axx000000002AAAQ\",\"ContentDocumentId\":\"069xx000000002BAAQ\",\"LinkedEntityId\":\"801xx000000004CAAQ\",\"ShareType\":\"V\",\"Visibility\":\"AllUsers\"}"
                + "]"
                + "}";

        when(salesforceManager.getRecordByQuery(anyString())).thenReturn(mockResponse);

        List<ContentDocumentLink> result = contentDocumentLinkDao.getLinksForDocument(contentDocumentId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("801xx000000003CAAQ", result.get(0).getLinkedEntityId());
        assertEquals("801xx000000004CAAQ", result.get(1).getLinkedEntityId());

        verify(salesforceManager, times(1)).getRecordByQuery(anyString());
    }

    @Test
    public void testGetLinksForDocumentEmpty() {
        String contentDocumentId = "069xx000000002BAAQ";
        String mockResponse = "{\"totalSize\":0,\"done\":true,\"records\":[]}";

        when(salesforceManager.getRecordByQuery(anyString())).thenReturn(mockResponse);

        List<ContentDocumentLink> result = contentDocumentLinkDao.getLinksForDocument(contentDocumentId);

        assertNotNull(result);
        assertEquals(0, result.size());

        verify(salesforceManager, times(1)).getRecordByQuery(anyString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLinksForDocumentNullId() {
        contentDocumentLinkDao.getLinksForDocument(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLinksForDocumentEmptyId() {
        contentDocumentLinkDao.getLinksForDocument("");
    }

    @Test
    public void testDeleteLink() {
        String linkId = "06Axx000000001AAAQ";

        doNothing().when(salesforceManager).deleteRecordById(linkId, "ContentDocumentLink");

        contentDocumentLinkDao.deleteLink(linkId);

        verify(salesforceManager, times(1)).deleteRecordById(linkId, "ContentDocumentLink");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteLinkNullId() {
        contentDocumentLinkDao.deleteLink(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteLinkEmptyId() {
        contentDocumentLinkDao.deleteLink("");
    }
}

