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
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for ContentDocumentLinkImpl.
 * Tests all getter methods with valid and null data.
 */
public class ContentDocumentLinkImplTest {

    private SFContentDocumentLink sfContentDocumentLink;
    private ContentDocumentLink contentDocumentLink;

    @Before
    public void setUp() {
        sfContentDocumentLink = new SFContentDocumentLink();
        sfContentDocumentLink.id = "06Axx000000001AAAQ";
        sfContentDocumentLink.contentDocumentId = "069xx000000002BAAQ";
        sfContentDocumentLink.linkedEntityId = "801xx000000003CAAQ";
        sfContentDocumentLink.shareType = "V";
        sfContentDocumentLink.visibility = "AllUsers";

        contentDocumentLink = new ContentDocumentLinkImpl(sfContentDocumentLink);
    }

    @Test
    public void testGetId() {
        assertEquals("06Axx000000001AAAQ", contentDocumentLink.getId());
    }

    @Test
    public void testGetIdNull() {
        sfContentDocumentLink.id = null;
        contentDocumentLink = new ContentDocumentLinkImpl(sfContentDocumentLink);
        assertNull(contentDocumentLink.getId());
    }

    @Test
    public void testGetContentDocumentId() {
        assertEquals("069xx000000002BAAQ", contentDocumentLink.getContentDocumentId());
    }

    @Test
    public void testGetContentDocumentIdNull() {
        sfContentDocumentLink.contentDocumentId = null;
        contentDocumentLink = new ContentDocumentLinkImpl(sfContentDocumentLink);
        assertNull(contentDocumentLink.getContentDocumentId());
    }

    @Test
    public void testGetLinkedEntityId() {
        assertEquals("801xx000000003CAAQ", contentDocumentLink.getLinkedEntityId());
    }

    @Test
    public void testGetLinkedEntityIdNull() {
        sfContentDocumentLink.linkedEntityId = null;
        contentDocumentLink = new ContentDocumentLinkImpl(sfContentDocumentLink);
        assertNull(contentDocumentLink.getLinkedEntityId());
    }

    @Test
    public void testGetShareType() {
        assertEquals("V", contentDocumentLink.getShareType());
    }

    @Test
    public void testGetShareTypeNull() {
        sfContentDocumentLink.shareType = null;
        contentDocumentLink = new ContentDocumentLinkImpl(sfContentDocumentLink);
        assertNull(contentDocumentLink.getShareType());
    }

    @Test
    public void testGetShareTypeCollaborator() {
        sfContentDocumentLink.shareType = "C";
        contentDocumentLink = new ContentDocumentLinkImpl(sfContentDocumentLink);
        assertEquals("C", contentDocumentLink.getShareType());
    }

    @Test
    public void testGetShareTypeInferred() {
        sfContentDocumentLink.shareType = "I";
        contentDocumentLink = new ContentDocumentLinkImpl(sfContentDocumentLink);
        assertEquals("I", contentDocumentLink.getShareType());
    }

    @Test
    public void testGetVisibility() {
        assertEquals("AllUsers", contentDocumentLink.getVisibility());
    }

    @Test
    public void testGetVisibilityNull() {
        sfContentDocumentLink.visibility = null;
        contentDocumentLink = new ContentDocumentLinkImpl(sfContentDocumentLink);
        assertNull(contentDocumentLink.getVisibility());
    }

    @Test
    public void testGetVisibilityInternalUsers() {
        sfContentDocumentLink.visibility = "InternalUsers";
        contentDocumentLink = new ContentDocumentLinkImpl(sfContentDocumentLink);
        assertEquals("InternalUsers", contentDocumentLink.getVisibility());
    }

    @Test
    public void testGetVisibilitySharedUsers() {
        sfContentDocumentLink.visibility = "SharedUsers";
        contentDocumentLink = new ContentDocumentLinkImpl(sfContentDocumentLink);
        assertEquals("SharedUsers", contentDocumentLink.getVisibility());
    }
}

