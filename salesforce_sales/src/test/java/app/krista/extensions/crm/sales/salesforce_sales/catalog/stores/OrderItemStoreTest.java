package app.krista.extensions.crm.sales.salesforce_sales.catalog.stores;

import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.OrderItem;
import app.krista.extensions.crm.sales.salesforce_sales.impl.OrderItemDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.impl.OrderItemImpl;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFOrderItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for OrderItemStore class.
 * Tests all EntityStore operations with mocked OrderItemDao.
 */
@RunWith(MockitoJUnitRunner.class)
public class OrderItemStoreTest {

    @Mock
    private OrderItemDaoImpl orderItemDao;

    @InjectMocks
    private OrderItemStore orderItemStore;

    private Map<String, Object> mockOrderItemFields;
    private SFOrderItem mockSFOrderItem;
    private OrderItemImpl mockOrderItemImpl;

    @Before
    public void setUp() {
        // Setup mock order item fields
        mockOrderItemFields = new HashMap<>();
        mockOrderItemFields.put("Order Id", "801xx000000001AAAQ");
        mockOrderItemFields.put("Pricebook Entry Id", "01uxx000000001AAAQ");
        mockOrderItemFields.put("Quantity", 10.0);
        mockOrderItemFields.put("Unit Price", 100.0);

        // Setup mock SFOrderItem
        mockSFOrderItem = new SFOrderItem();
        mockSFOrderItem.id = "802xx000000001AAAQ";
        mockSFOrderItem.orderId = "801xx000000001AAAQ";
        mockSFOrderItem.pricebookEntryId = "01uxx000000001AAAQ";
        mockSFOrderItem.quantity = 10.0;
        mockSFOrderItem.unitPrice = 100.0;

        // Setup mock OrderItemImpl
        mockOrderItemImpl = new OrderItemImpl(mockSFOrderItem);
    }

    @Test
    public void testCreate_Success() throws IOException {
        // Arrange
        String orderItemId = "802xx000000001AAAQ";
        when(orderItemDao.addOrderItem(mockOrderItemFields)).thenReturn(orderItemId);
        when(orderItemDao.getOrderItemById(orderItemId)).thenReturn(mockOrderItemImpl);

        // Act
        OrderItem result = orderItemStore.create(mockOrderItemFields);

        // Assert
        assertNotNull("OrderItem should not be null", result);
        assertEquals("OrderItem ID should match", orderItemId, result.id);
        verify(orderItemDao, times(1)).addOrderItem(mockOrderItemFields);
        verify(orderItemDao, times(1)).getOrderItemById(orderItemId);
    }

    @Test
    public void testCreate_NullFields() throws IOException {
        // Act
        OrderItem result = orderItemStore.create((Map<String, Object>) null);

        // Assert
        assertNotNull("OrderItem should not be null", result);
        assertNull("OrderItem ID should be null", result.id);
    }

    @Test
    public void testCreate_EmptyFields() throws IOException {
        // Act
        OrderItem result = orderItemStore.create(new HashMap<>());

        // Assert
        assertNotNull("OrderItem should not be null", result);
        assertNull("OrderItem ID should be null", result.id);
    }

    @Test
    public void testCreate_WithOrderItemEntity() throws IOException {
        // Arrange
        OrderItem orderItem = new OrderItem();
        orderItem.orderId = "801xx000000001AAAQ";
        orderItem.pricebookEntryId = "01uxx000000001AAAQ";
        orderItem.quantity = 10.0;
        orderItem.unitPrice = 100.0;

        String orderItemId = "802xx000000001AAAQ";
        when(orderItemDao.addOrderItem(anyMap())).thenReturn(orderItemId);

        // Act
        OrderItem result = orderItemStore.create(orderItem);

        // Assert
        assertNotNull("OrderItem should not be null", result);
        assertEquals("OrderItem ID should match", orderItemId, result.id);
        verify(orderItemDao, times(1)).addOrderItem(anyMap());
    }

    @Test
    public void testGet_Success() throws IOException {
        // Arrange
        String orderItemId = "802xx000000001AAAQ";
        when(orderItemDao.getOrderItemById(orderItemId)).thenReturn(mockOrderItemImpl);

        // Act
        OrderItem result = orderItemStore.get(orderItemId);

        // Assert
        assertNotNull("OrderItem should not be null", result);
        assertEquals("OrderItem ID should match", orderItemId, result.id);
        verify(orderItemDao, times(1)).getOrderItemById(orderItemId);
    }

    @Test
    public void testGet_NotFound() throws IOException {
        // Arrange
        String orderItemId = "INVALID_ID";
        when(orderItemDao.getOrderItemById(orderItemId)).thenThrow(new IllegalArgumentException("OrderItem not found"));

        // Act
        OrderItem result = orderItemStore.get(orderItemId);

        // Assert - should return null, not throw exception
        assertNull("OrderItem should be null when not found", result);
    }

    @Test
    public void testUpdate_Success() throws IOException {
        // Arrange
        OrderItem orderItem = new OrderItem();
        orderItem.id = "802xx000000001AAAQ";
        orderItem.quantity = 15.0;

        when(orderItemDao.getOrderItemById(orderItem.id)).thenReturn(mockOrderItemImpl);
        doNothing().when(orderItemDao).updateOrderItem(any(OrderItemImpl.class));

        // Act
        OrderItem result = orderItemStore.update(orderItem);

        // Assert
        assertNotNull("OrderItem should not be null", result);
        verify(orderItemDao, times(1)).getOrderItemById(orderItem.id);
        verify(orderItemDao, times(1)).updateOrderItem(any(OrderItemImpl.class));
    }

    @Test
    public void testUpdate_NotFound() throws IOException {
        // Arrange
        OrderItem orderItem = new OrderItem();
        orderItem.id = "INVALID_ID";

        when(orderItemDao.getOrderItemById(orderItem.id)).thenThrow(new IllegalArgumentException("OrderItem not found"));

        // Act
        OrderItem result = orderItemStore.update(orderItem);

        // Assert - should return null, not throw exception
        assertNull("OrderItem should be null when not found", result);
    }

    @Test
    public void testDelete_Success() throws IOException {
        // Arrange
        String orderItemId = "802xx000000001AAAQ";
        doNothing().when(orderItemDao).deleteOrderItem(orderItemId);

        // Act
        orderItemStore.delete(orderItemId);

        // Assert
        verify(orderItemDao, times(1)).deleteOrderItem(orderItemId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDelete_NotFound() throws IOException {
        // Arrange
        String orderItemId = "INVALID_ID";
        doThrow(new IllegalArgumentException("OrderItem not found")).when(orderItemDao).deleteOrderItem(orderItemId);

        // Act
        orderItemStore.delete(orderItemId);

        // Assert - exception expected
    }

    @Test
    public void testSearch_DeprecatedMethod() {
        // Act
        List<OrderItem> results = orderItemStore.search(List.of(), 1, 10);

        // Assert
        assertNotNull("Results should not be null", results);
        assertTrue("Results should be empty for deprecated method", results.isEmpty());
    }

    @Test
    public void testContains() {
        // Act
        boolean result = orderItemStore.contains("802xx000000001AAAQ");

        // Assert
        assertFalse("Contains should return false", result);
    }

    @Test
    public void testCount() {
        // Act
        long result = orderItemStore.count(List.of());

        // Assert
        assertEquals("Count should return 0", 0, result);
    }

    @Test
    public void testLookup() {
        // Act
        List<String> result = orderItemStore.lookup(new HashMap<>(), List.of());

        // Assert
        assertNotNull("Lookup should return empty list, not null", result);
        assertTrue("Lookup should return empty list", result.isEmpty());
    }
}