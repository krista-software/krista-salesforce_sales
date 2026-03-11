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

import app.krista.extensions.crm.sales.salesforce_sales.model.SFContentVersion;
import app.krista.extensions.crm.sales.salesforce_sales.service.ContentVersion;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Unit tests for ContentVersionImpl.
 * Tests all getter methods with valid and null data.
 */
public class ContentVersionImplTest {

    private SFContentVersion sfContentVersion;
    private ContentVersion contentVersion;

    @Before
    public void setUp() {
        sfContentVersion = new SFContentVersion();
        sfContentVersion.id = "068xx000000001AAAQ";
        sfContentVersion.title = "Test Document";
        sfContentVersion.pathOnClient = "test-document.pdf";
        sfContentVersion.versionData = "JVBERi0xLjQKJeLjz9MKMSAwIG9iago8PC9UeXBlL0NhdGFsb2cvUGFnZXMgMiAwIFI+PgplbmRvYmoKMiAwIG9iago8PC9UeXBlL1BhZ2VzL0NvdW50IDEvS2lkc1szIDAgUl0+PgplbmRvYmoKMyAwIG9iago8PC9UeXBlL1BhZ2UvTWVkaWFCb3hbMCAwIDYxMiA3OTJdL1BhcmVudCAyIDAgUi9SZXNvdXJjZXM8PC9Gb250PDwvRjEgNCAwIFI+Pj4+L0NvbnRlbnRzIDUgMCBSPj4KZW5kb2JqCjQgMCBvYmoKPDwvVHlwZS9Gb250L1N1YnR5cGUvVHlwZTEvQmFzZUZvbnQvVGltZXMtUm9tYW4+PgplbmRvYmoKNSAwIG9iago8PC9MZW5ndGggNDQ+PgpzdHJlYW0KQlQKL0YxIDI0IFRmCjEwMCA3MDAgVGQKKEhlbGxvIFdvcmxkKSBUagpFVAplbmRzdHJlYW0KZW5kb2JqCnhyZWYKMCA2CjAwMDAwMDAwMDAgNjU1MzUgZiAKMDAwMDAwMDAxNSAwMDAwMCBuIAowMDAwMDAwMDY0IDAwMDAwIG4gCjAwMDAwMDAxMjEgMDAwMDAgbiAKMDAwMDAwMDI0NyAwMDAwMCBuIAowMDAwMDAwMzI5IDAwMDAwIG4gCnRyYWlsZXIKPDwvU2l6ZSA2L1Jvb3QgMSAwIFI+PgpzdGFydHhyZWYKNDIxCiUlRU9GCg==";
        sfContentVersion.contentDocumentId = "069xx000000002BAAQ";
        sfContentVersion.fileType = "PDF";
        sfContentVersion.fileExtension = "pdf";
        sfContentVersion.contentSize = 1024L;
        sfContentVersion.description = "Test file description";
        sfContentVersion.createdDate = new Date(1609459200000L); // 2021-01-01
        sfContentVersion.createdById = "005xx000001X8UzAAK";
        sfContentVersion.lastModifiedDate = new Date(1609545600000L); // 2021-01-02
        sfContentVersion.lastModifiedById = "005xx000001X8UzAAK";

        contentVersion = new ContentVersionImpl(sfContentVersion);
    }

    @Test
    public void testGetId() {
        assertEquals("068xx000000001AAAQ", contentVersion.getId());
    }

    @Test
    public void testGetIdNull() {
        sfContentVersion.id = null;
        contentVersion = new ContentVersionImpl(sfContentVersion);
        assertNull(contentVersion.getId());
    }

    @Test
    public void testGetTitle() {
        assertEquals("Test Document", contentVersion.getTitle());
    }

    @Test
    public void testGetTitleNull() {
        sfContentVersion.title = null;
        contentVersion = new ContentVersionImpl(sfContentVersion);
        assertNull(contentVersion.getTitle());
    }

    @Test
    public void testGetPathOnClient() {
        assertEquals("test-document.pdf", contentVersion.getPathOnClient());
    }

    @Test
    public void testGetPathOnClientNull() {
        sfContentVersion.pathOnClient = null;
        contentVersion = new ContentVersionImpl(sfContentVersion);
        assertNull(contentVersion.getPathOnClient());
    }

    @Test
    public void testGetVersionData() {
        assertNotNull(contentVersion.getVersionData());
        assertTrue(contentVersion.getVersionData().startsWith("JVBERi0"));
    }

    @Test
    public void testGetVersionDataNull() {
        sfContentVersion.versionData = null;
        contentVersion = new ContentVersionImpl(sfContentVersion);
        assertNull(contentVersion.getVersionData());
    }

    @Test
    public void testGetContentDocumentId() {
        assertEquals("069xx000000002BAAQ", contentVersion.getContentDocumentId());
    }

    @Test
    public void testGetContentDocumentIdNull() {
        sfContentVersion.contentDocumentId = null;
        contentVersion = new ContentVersionImpl(sfContentVersion);
        assertNull(contentVersion.getContentDocumentId());
    }

    @Test
    public void testGetFileType() {
        assertEquals("PDF", contentVersion.getFileType());
    }

    @Test
    public void testGetFileTypeNull() {
        sfContentVersion.fileType = null;
        contentVersion = new ContentVersionImpl(sfContentVersion);
        assertNull(contentVersion.getFileType());
    }

    @Test
    public void testGetFileExtension() {
        assertEquals("pdf", contentVersion.getFileExtension());
    }

    @Test
    public void testGetFileExtensionNull() {
        sfContentVersion.fileExtension = null;
        contentVersion = new ContentVersionImpl(sfContentVersion);
        assertNull(contentVersion.getFileExtension());
    }

    @Test
    public void testGetContentSize() {
        assertEquals(Long.valueOf(1024L), contentVersion.getContentSize());
    }

    @Test
    public void testGetContentSizeNull() {
        sfContentVersion.contentSize = null;
        contentVersion = new ContentVersionImpl(sfContentVersion);
        assertNull(contentVersion.getContentSize());
    }

    @Test
    public void testGetDescription() {
        assertEquals("Test file description", contentVersion.getDescription());
    }

    @Test
    public void testGetDescriptionNull() {
        sfContentVersion.description = null;
        contentVersion = new ContentVersionImpl(sfContentVersion);
        assertNull(contentVersion.getDescription());
    }

    @Test
    public void testGetCreatedDate() {
        assertEquals(Long.valueOf(1609459200000L), contentVersion.getCreatedDate());
    }

    @Test
    public void testGetCreatedDateNull() {
        sfContentVersion.createdDate = null;
        contentVersion = new ContentVersionImpl(sfContentVersion);
        assertNull(contentVersion.getCreatedDate());
    }

    @Test
    public void testGetCreatedById() {
        assertEquals("005xx000001X8UzAAK", contentVersion.getCreatedById());
    }

    @Test
    public void testGetCreatedByIdNull() {
        sfContentVersion.createdById = null;
        contentVersion = new ContentVersionImpl(sfContentVersion);
        assertNull(contentVersion.getCreatedById());
    }

    @Test
    public void testGetLastModifiedDate() {
        assertEquals(Long.valueOf(1609545600000L), contentVersion.getLastModifiedDate());
    }

    @Test
    public void testGetLastModifiedDateNull() {
        sfContentVersion.lastModifiedDate = null;
        contentVersion = new ContentVersionImpl(sfContentVersion);
        assertNull(contentVersion.getLastModifiedDate());
    }

    @Test
    public void testGetLastModifiedById() {
        assertEquals("005xx000001X8UzAAK", contentVersion.getLastModifiedById());
    }

    @Test
    public void testGetLastModifiedByIdNull() {
        sfContentVersion.lastModifiedById = null;
        contentVersion = new ContentVersionImpl(sfContentVersion);
        assertNull(contentVersion.getLastModifiedById());
    }
}

