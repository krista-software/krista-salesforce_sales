package app.krista.extensions.crm.sales.salesforce_sales.catalog.stores;

import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Order;
import app.krista.extensions.crm.sales.salesforce_sales.impl.OrderDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.impl.OrderImpl;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFOrder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for OrderStore class.
 * Tests all EntityStore operations with mocked OrderDaoImpl.
 */
@RunWith(MockitoJUnitRunner.class)
public class OrderStoreTest {

    @Mock
    private OrderDaoImpl orderDao;

    @InjectMocks
    private OrderStore orderStore;

    private Map<String, Object> mockOrderFields;
    private SFOrder mockSFOrder;
    private OrderImpl mockOrderImpl;

    @Before
    public void setUp() {
        // Setup mock order fields
        mockOrderFields = new HashMap<>();
        mockOrderFields.put("Account Id", "001xx000000001AAAQ");
        mockOrderFields.put("Effective Date", 1705276800000L);
        mockOrderFields.put("Status", "Draft");
        mockOrderFields.put("Description", "Test Order");

        // Setup mock SFOrder
        mockSFOrder = new SFOrder();
        mockSFOrder.id = "801xx000000001AAAQ";
        mockSFOrder.orderNumber = "ORD-00001234";
        mockSFOrder.accountId = "001xx000000001AAAQ";
        mockSFOrder.effectiveDate = "2024-01-15";
        mockSFOrder.status = "Draft";
        mockSFOrder.description = "Test Order";

        // Setup mock OrderImpl
        mockOrderImpl = new OrderImpl(mockSFOrder);
    }

    @Test
    public void testCreate_Success() {
        // Arrange
        String orderId = "801xx000000001AAAQ";
        when(orderDao.addOrder(mockOrderFields)).thenReturn(orderId);
        when(orderDao.getOrderById(orderId)).thenReturn(mockOrderImpl);

        // Act
        Order result = orderStore.create(mockOrderFields);

        // Assert
        assertNotNull("Order should not be null", result);
        assertEquals("Order ID should match", orderId, result.id);
        verify(orderDao, times(1)).addOrder(mockOrderFields);
        verify(orderDao, times(1)).getOrderById(orderId);
    }

    @Test
    public void testCreate_NullFields() {
        // Act
        Order result = orderStore.create(null);

        // Assert - should return empty Order, not throw exception
        assertNotNull("Order should not be null", result);
        assertNull("Order ID should be null for empty order", result.id);
    }

    @Test
    public void testCreate_EmptyFields() {
        // Act
        Order result = orderStore.create(new HashMap<>());

        // Assert - should return empty Order, not throw exception
        assertNotNull("Order should not be null", result);
        assertNull("Order ID should be null for empty order", result.id);
    }

    @Test
    public void testGet_Success() {
        // Arrange
        String orderId = "801xx000000001AAAQ";
        when(orderDao.getOrderById(orderId)).thenReturn(mockOrderImpl);

        // Act
        Order result = orderStore.get(orderId);

        // Assert
        assertNotNull("Order should not be null", result);
        assertEquals("Order ID should match", orderId, result.id);
        verify(orderDao, times(1)).getOrderById(orderId);
    }

    @Test
    public void testGet_NullId() {
        // Act
        Order result = orderStore.get(null);

        // Assert - should return null, not throw exception
        assertNull("Order should be null for null ID", result);
    }

    @Test
    public void testGet_EmptyId() {
        // Act
        Order result = orderStore.get("");

        // Assert - should return null, not throw exception
        assertNull("Order should be null for empty ID", result);
    }

    @Test
    public void testGet_NotFound() {
        // Arrange
        String orderId = "INVALID_ID";
        when(orderDao.getOrderById(orderId)).thenThrow(new IllegalArgumentException("Order not found"));

        // Act
        Order result = orderStore.get(orderId);

        // Assert
        assertNull("Order should be null when not found", result);
    }

    @Test
    public void testUpdate_Success() {
        // Arrange
        Order order = new Order();
        order.id = "801xx000000001AAAQ";
        order.status = "Activated";

        doNothing().when(orderDao).updateOrder(any(OrderImpl.class));
        when(orderDao.getOrderById("801xx000000001AAAQ")).thenReturn(mockOrderImpl);

        // Act
        Order result = orderStore.update(order);

        // Assert
        assertNotNull("Order should not be null", result);
        assertEquals("Order ID should match", order.id, result.id);
        verify(orderDao, times(1)).updateOrder(any(OrderImpl.class));
    }

    @Test
    public void testUpdate_NullOrder() {
        // Act
        Order result = orderStore.update(null);

        // Assert - should return null, not throw exception
        assertNull("Order should be null for null input", result);
    }

    @Test
    public void testDelete_Success() {
        // Arrange
        String orderId = "801xx000000001AAAQ";
        doNothing().when(orderDao).deleteOrder(orderId);

        // Act
        orderStore.delete(orderId);

        // Assert
        verify(orderDao, times(1)).deleteOrder(orderId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDelete_NullId() {
        // Act
        orderStore.delete(null);

        // Assert - exception expected
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDelete_EmptyId() {
        // Act
        orderStore.delete("");

        // Assert - exception expected
    }

    @Test
    public void testSearch_DeprecatedMethod() {
        // Act
        List<Order> results = orderStore.search(List.of(), 1, 10);

        // Assert
        assertNotNull("Results should not be null", results);
        assertTrue("Results should be empty for deprecated method", results.isEmpty());
    }

    @Test
    public void testContains() {
        // Act
        boolean result = orderStore.contains("801xx000000001AAAQ");

        // Assert
        assertFalse("Contains should return false", result);
    }

    @Test
    public void testCount() {
        // Act
        long result = orderStore.count(List.of());

        // Assert
        assertEquals("Count should return 0", 0, result);
    }

    @Test
    public void testLookup() {
        // Act
        List<String> result = orderStore.lookup(new HashMap<>(), List.of());

        // Assert
        assertNull("Lookup should return null", result);
    }
}