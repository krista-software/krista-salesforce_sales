package app.krista.extensions.crm.sales.salesforce_sales.catalog;

import app.krista.extensions.crm.sales.salesforce_sales.impl.ContentDocumentLinkDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.impl.ContentVersionDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.impl.PriceBookEntryDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.impl.ProductDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.service.ContentVersion;
import app.krista.ksdk.files.FileHandle;
import app.krista.ksdk.files.FileRepository;
import app.krista.model.base.File;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for OrderManagementArea.
 * Tests file operation catalog request methods with mocked DAOs.
 */
public class OrderManagementAreaTest {

    private ContentVersionDaoImpl contentVersionDao;
    private ContentDocumentLinkDaoImpl contentDocumentLinkDao;
    private FileRepository fileRepository;
    private ProductDaoImpl productDao;
    private PriceBookEntryDaoImpl priceBookEntryDao;
    private OrderManagementArea orderManagementArea;

    @Before
    public void setUp() throws Exception {
        // Create mocks
        contentVersionDao = mock(ContentVersionDaoImpl.class);
        contentDocumentLinkDao = mock(ContentDocumentLinkDaoImpl.class);
        fileRepository = mock(FileRepository.class);
        productDao = mock(ProductDaoImpl.class);
        priceBookEntryDao = mock(PriceBookEntryDaoImpl.class);

        // Use reflection to access private constructor
        var ctor = OrderManagementArea.class.getDeclaredConstructor(
                ContentVersionDaoImpl.class,
                ContentDocumentLinkDaoImpl.class,
                FileRepository.class,
                ProductDaoImpl.class,
                PriceBookEntryDaoImpl.class
        );
        ctor.setAccessible(true);
        orderManagementArea = ctor.newInstance(
                contentVersionDao,
                contentDocumentLinkDao,
                fileRepository,
                productDao,
                priceBookEntryDao
        );
    }

    // ========== Upload PO File Tests ==========

    @Test
    public void testUploadPOFile_Success() throws Exception {
        String fileName = "PO-12345.pdf";
        byte[] fileBytes = "test file content".getBytes();
        String contentVersionId = "068xx000000001AAAQ";
        String contentDocumentId = "069xx000000002BAAQ";

        // Mock File object
        File mockFile = mock(File.class);
        when(mockFile.getFileName()).thenReturn(fileName);

        // Mock FileHandle and InputStream
        FileHandle mockFileHandle = mock(FileHandle.class);
        InputStream mockInputStream = new ByteArrayInputStream(fileBytes);
        when(mockFileHandle.getContent()).thenReturn(mockInputStream);
        when(fileRepository.getFile(mockFile)).thenReturn(mockFileHandle);

        // Mock ContentVersion
        ContentVersion mockContentVersion = mock(ContentVersion.class);
        when(mockContentVersion.getContentDocumentId()).thenReturn(contentDocumentId);

        when(contentVersionDao.uploadFile(anyString(), anyString(), anyString(), isNull())).thenReturn(contentVersionId);
        when(contentVersionDao.getContentVersionById(contentVersionId)).thenReturn(mockContentVersion);

        String result = orderManagementArea.uploadPOFile(mockFile, null);

        assertEquals(contentDocumentId, result);
        verify(fileRepository, times(1)).getFile(mockFile);
        verify(contentVersionDao, times(1)).uploadFile(eq("PO-12345"), eq("PO-12345.pdf"), anyString(), isNull());
        verify(contentVersionDao, times(1)).getContentVersionById(contentVersionId);
    }

    @Test
    public void testUploadPOFile_WithDescription() throws Exception {
        String fileName = "PO-12345.docx";
        String description = "Purchase Order Document";
        byte[] fileBytes = "test file content".getBytes();
        String contentVersionId = "068xx000000001AAAQ";
        String contentDocumentId = "069xx000000002BAAQ";

        // Mock File object
        File mockFile = mock(File.class);
        when(mockFile.getFileName()).thenReturn(fileName);

        // Mock FileHandle and InputStream
        FileHandle mockFileHandle = mock(FileHandle.class);
        InputStream mockInputStream = new ByteArrayInputStream(fileBytes);
        when(mockFileHandle.getContent()).thenReturn(mockInputStream);
        when(fileRepository.getFile(mockFile)).thenReturn(mockFileHandle);

        // Mock ContentVersion
        ContentVersion mockContentVersion = mock(ContentVersion.class);
        when(mockContentVersion.getContentDocumentId()).thenReturn(contentDocumentId);

        when(contentVersionDao.uploadFile(anyString(), anyString(), anyString(), eq(description))).thenReturn(contentVersionId);
        when(contentVersionDao.getContentVersionById(contentVersionId)).thenReturn(mockContentVersion);

        String result = orderManagementArea.uploadPOFile(mockFile, description);

        assertEquals(contentDocumentId, result);
        verify(contentVersionDao, times(1)).uploadFile(eq("PO-12345"), eq("PO-12345.docx"), anyString(), eq(description));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUploadPOFile_NullFile() {
        orderManagementArea.uploadPOFile(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUploadPOFile_EmptyFileName() {
        File mockFile = mock(File.class);
        when(mockFile.getFileName()).thenReturn("");
        orderManagementArea.uploadPOFile(mockFile, null);
    }

    // ========== Link File to Order Tests ==========

    @Test
    public void testLinkFileToOrder_Success() {
        String contentDocumentId = "069xx000000002BAAQ";
        String orderId = "801xx000000001AAAQ";
        String linkId = "06Axx000000001AAAQ";

        when(contentDocumentLinkDao.linkFileToRecord(contentDocumentId, orderId)).thenReturn(linkId);

        String result = orderManagementArea.linkFileToOrder(contentDocumentId, orderId, null, null);

        assertEquals(linkId, result);
        verify(contentDocumentLinkDao, times(1)).linkFileToRecord(contentDocumentId, orderId);
    }

    @Test
    public void testLinkFileToOrder_WithShareTypeAndVisibility() {
        String contentDocumentId = "069xx000000002BAAQ";
        String orderId = "801xx000000001AAAQ";
        String shareType = "C";
        String visibility = "InternalUsers";
        String linkId = "06Axx000000001AAAQ";

        when(contentDocumentLinkDao.linkFileToRecord(contentDocumentId, orderId, shareType, visibility)).thenReturn(linkId);

        String result = orderManagementArea.linkFileToOrder(contentDocumentId, orderId, shareType, visibility);

        assertEquals(linkId, result);
        verify(contentDocumentLinkDao, times(1)).linkFileToRecord(contentDocumentId, orderId, shareType, visibility);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLinkFileToOrder_NullContentDocumentId() {
        orderManagementArea.linkFileToOrder(null, "801xx000000001AAAQ", null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLinkFileToOrder_NullOrderId() {
        orderManagementArea.linkFileToOrder("069xx000000002BAAQ", null, null, null);
    }

}

