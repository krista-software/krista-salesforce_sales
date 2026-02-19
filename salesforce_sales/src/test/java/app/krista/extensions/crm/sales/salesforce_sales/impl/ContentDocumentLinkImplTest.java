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

