package app.krista.extensions.crm.sales.salesforce_sales.impl;

import app.krista.extensions.crm.sales.salesforce_sales.model.SFOrder;
import app.krista.extensions.crm.sales.salesforce_sales.service.Order;
import app.krista.extensions.crm.sales.salesforce_sales.util.SalesforceListResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.GSON;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for OrderDaoImpl class.
 * Tests all CRUD operations with mocked SalesforceManager.
 */
@RunWith(MockitoJUnitRunner.class)
public class OrderDaoImplTest {
    
    @Mock
    private SalesforceManager salesforceManager;
    
    @InjectMocks
    private OrderDaoImpl orderDao;
    
    private SFOrder mockSFOrder;
    private Map<String, Object> mockOrderFields;
    
    @Before
    public void setUp() {
        // Setup mock SFOrder
        mockSFOrder = new SFOrder();
        mockSFOrder.id = "801xx000000001AAAQ";
        mockSFOrder.orderNumber = "ORD-00001234";
        mockSFOrder.accountId = "001xx000000001AAAQ";
        mockSFOrder.effectiveDate = "2024-01-15";
        mockSFOrder.status = "Draft";
        mockSFOrder.description = "Test Order";
        mockSFOrder.totalAmount = 1500.00;

        // Setup custom fields using the new customFields map
        mockSFOrder.customFields = new HashMap<>();
        mockSFOrder.customFields.put("PO_Number__c", "PO-2024-001");
        mockSFOrder.customFields.put("Extraction_Status__c", "Pending");
        mockSFOrder.customFields.put("Confidence__c", 95.5);
        
        // Setup mock order fields
        mockOrderFields = new HashMap<>();
        mockOrderFields.put("Account Id", "001xx000000001AAAQ");
        mockOrderFields.put("Effective Date", 1705276800000L); // 2024-01-15
        mockOrderFields.put("Status", "Draft");
        mockOrderFields.put("Description", "Test Order");
        mockOrderFields.put("Total Amount", 1500.00);
        mockOrderFields.put("PO Number", "PO-2024-001");
    }
    
    @Test
    public void testGetOrderById_Success() {
        // Arrange
        String orderId = "801xx000000001AAAQ";
        when(salesforceManager.getRecordById(orderId, "Order"))
                .thenReturn(GSON.toJson(mockSFOrder));
        
        // Act
        Order result = orderDao.getOrderById(orderId);
        
        // Assert
        assertNotNull("Order should not be null", result);
        assertEquals("Order ID should match", orderId, result.getId());
        assertEquals("Order Number should match", "ORD-00001234", result.getOrderNumber());
        assertEquals("Account ID should match", "001xx000000001AAAQ", result.getAccountId());
        assertEquals("Status should match", "Draft", result.getStatus());
        verify(salesforceManager, times(1)).getRecordById(orderId, "Order");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetOrderById_NotFound() {
        // Arrange
        String orderId = "INVALID_ID";
        when(salesforceManager.getRecordById(orderId, "Order"))
                .thenThrow(new RuntimeException("Order not found"));
        
        // Act
        orderDao.getOrderById(orderId);
        
        // Assert - exception expected
    }
    
    @Test
    public void testGetOrderByOrderNumber_Success() {
        // Arrange
        String orderNumber = "ORD-00001234";
        SalesforceListResponse<SFOrder> response = new SalesforceListResponse<>();
        response.records = List.of(mockSFOrder);
        
        when(salesforceManager.getRecordByQuery(anyString()))
                .thenReturn(GSON.toJson(response));
        
        // Act
        Order result = orderDao.getOrderByOrderNumber(orderNumber);
        
        // Assert
        assertNotNull("Order should not be null", result);
        assertEquals("Order Number should match", orderNumber, result.getOrderNumber());
        verify(salesforceManager, times(1)).getRecordByQuery(anyString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetOrderByOrderNumber_NotFound() {
        // Arrange
        String orderNumber = "INVALID_ORDER_NUMBER";
        SalesforceListResponse<SFOrder> response = new SalesforceListResponse<>();
        response.records = List.of();
        
        when(salesforceManager.getRecordByQuery(anyString()))
                .thenReturn(GSON.toJson(response));
        
        // Act
        orderDao.getOrderByOrderNumber(orderNumber);
        
        // Assert - exception expected
    }
    
    @Test
    public void testGetOrdersByAccountId_Success() {
        // Arrange
        String accountId = "001xx000000001AAAQ";
        SalesforceListResponse<SFOrder> response = new SalesforceListResponse<>();
        response.records = List.of(mockSFOrder);
        
        when(salesforceManager.getRecordByQuery(anyString()))
                .thenReturn(GSON.toJson(response));
        
        // Act
        List<Order> results = orderDao.getOrdersByAccountId(accountId, 10, 1);
        
        // Assert
        assertNotNull("Results should not be null", results);
        assertEquals("Should return 1 order", 1, results.size());
        assertEquals("Account ID should match", accountId, results.get(0).getAccountId());
        verify(salesforceManager, times(1)).getRecordByQuery(anyString());
    }
    
    @Test
    public void testGetOrdersByAccountId_EmptyResult() {
        // Arrange
        String accountId = "001xx000000002AAAQ";
        SalesforceListResponse<SFOrder> response = new SalesforceListResponse<>();
        response.records = List.of();

        when(salesforceManager.getRecordByQuery(anyString()))
                .thenReturn(GSON.toJson(response));

        // Act
        List<Order> results = orderDao.getOrdersByAccountId(accountId, 10, 1);

        // Assert
        assertNotNull("Results should not be null", results);
        assertTrue("Results should be empty", results.isEmpty());
    }

    @Test
    public void testGetOrdersByStatus_Success() {
        // Arrange
        String status = "Draft";
        SalesforceListResponse<SFOrder> response = new SalesforceListResponse<>();
        response.records = List.of(mockSFOrder);

        when(salesforceManager.getRecordByQuery(anyString()))
                .thenReturn(GSON.toJson(response));

        // Act
        List<Order> results = orderDao.getOrdersByStatus(status, 10, 1);

        // Assert
        assertNotNull("Results should not be null", results);
        assertEquals("Should return 1 order", 1, results.size());
        assertEquals("Status should match", status, results.get(0).getStatus());
    }

    @Test
    public void testGetOrders_Success() {
        // Arrange
        SalesforceListResponse<SFOrder> response = new SalesforceListResponse<>();
        response.records = List.of(mockSFOrder);

        when(salesforceManager.getRecordByQuery(anyString()))
                .thenReturn(GSON.toJson(response));

        // Act
        List<Order> results = orderDao.getOrders(10, 1);

        // Assert
        assertNotNull("Results should not be null", results);
        assertEquals("Should return 1 order", 1, results.size());
    }

    @Test
    public void testGetOrders_WithPagination() {
        // Arrange
        SalesforceListResponse<SFOrder> response = new SalesforceListResponse<>();
        response.records = List.of(mockSFOrder);

        when(salesforceManager.getRecordByQuery(anyString()))
                .thenReturn(GSON.toJson(response));

        // Act
        List<Order> results = orderDao.getOrders(20, 2);

        // Assert
        assertNotNull("Results should not be null", results);
        verify(salesforceManager, times(1)).getRecordByQuery(contains("OFFSET 20"));
    }

    @Test
    public void testAddOrder_Success() {
        // Arrange
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("id", "801xx000000001AAAQ");
        responseMap.put("success", true);

        when(salesforceManager.createRecord(anyString(), eq("Order")))
                .thenReturn(GSON.toJson(responseMap));

        // Act
        String orderId = orderDao.addOrder(mockOrderFields);

        // Assert
        assertNotNull("Order ID should not be null", orderId);
        assertEquals("Order ID should match", "801xx000000001AAAQ", orderId);
        verify(salesforceManager, times(1)).createRecord(anyString(), eq("Order"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOrder_NullFields() {
        // Act
        orderDao.addOrder(null);

        // Assert - exception expected
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOrder_EmptyFields() {
        // Act
        orderDao.addOrder(new HashMap<>());

        // Assert - exception expected
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOrder_MissingAccountId() {
        // Arrange
        Map<String, Object> fields = new HashMap<>();
        fields.put("Effective Date", 1705276800000L);
        fields.put("Status", "Draft");

        // Act
        orderDao.addOrder(fields);

        // Assert - exception expected
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOrder_MissingEffectiveDate() {
        // Arrange
        Map<String, Object> fields = new HashMap<>();
        fields.put("Account Id", "001xx000000001AAAQ");
        fields.put("Status", "Draft");

        // Act
        orderDao.addOrder(fields);

        // Assert - exception expected
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOrder_MissingStatus() {
        // Arrange
        Map<String, Object> fields = new HashMap<>();
        fields.put("Account Id", "001xx000000001AAAQ");
        fields.put("Effective Date", 1705276800000L);

        // Act
        orderDao.addOrder(fields);

        // Assert - exception expected
    }

    @Test
    public void testAddOrder_WithCustomFields() {
        // Arrange
        mockOrderFields.put("PO Number", "PO-2024-001");
        mockOrderFields.put("Extraction Status", "Completed");
        mockOrderFields.put("Confidence", 98.5);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("id", "801xx000000001AAAQ");

        when(salesforceManager.createRecord(anyString(), eq("Order")))
                .thenReturn(GSON.toJson(responseMap));

        // Act
        String orderId = orderDao.addOrder(mockOrderFields);

        // Assert
        assertNotNull("Order ID should not be null", orderId);
        verify(salesforceManager, times(1)).createRecord(anyString(), eq("Order"));
    }

    @Test
    public void testUpdateOrder_Success() {
        // Arrange
        Order mockOrder = new OrderImpl(mockSFOrder);

        when(salesforceManager.getRecordById(anyString(), eq("Order")))
                .thenReturn(GSON.toJson(mockSFOrder));
        when(salesforceManager.getDifference(any(), any()))
                .thenReturn(Map.of("Status", "Activated"));
        when(salesforceManager.updateRecord(anyString(), anyString(), eq("Order")))
                .thenReturn("{}");

        // Act
        orderDao.updateOrder(mockOrder);

        // Assert
        verify(salesforceManager, times(1)).updateRecord(anyString(), anyString(), eq("Order"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateOrder_NullId() {
        // Arrange
        mockSFOrder.id = null;
        Order mockOrder = new OrderImpl(mockSFOrder);

        // Act
        orderDao.updateOrder(mockOrder);

        // Assert - exception expected
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateOrder_EmptyId() {
        // Arrange
        mockSFOrder.id = "";
        Order mockOrder = new OrderImpl(mockSFOrder);

        // Act
        orderDao.updateOrder(mockOrder);

        // Assert - exception expected
    }

    @Test
    public void testUpdateOrder_NoChanges() {
        // Arrange
        Order mockOrder = new OrderImpl(mockSFOrder);

        when(salesforceManager.getRecordById(anyString(), eq("Order")))
                .thenReturn(GSON.toJson(mockSFOrder));
        when(salesforceManager.getDifference(any(), any()))
                .thenReturn(new HashMap<>());

        // Act
        orderDao.updateOrder(mockOrder);

        // Assert
        verify(salesforceManager, never()).updateRecord(anyString(), anyString(), eq("Order"));
    }

    @Test
    public void testDeleteOrder_Success() {
        // Arrange
        String orderId = "801xx000000001AAAQ";
        doNothing().when(salesforceManager).deleteRecordById(orderId, "Order");

        // Act
        orderDao.deleteOrder(orderId);

        // Assert
        verify(salesforceManager, times(1)).deleteRecordById(orderId, "Order");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteOrder_NullId() {
        // Act
        orderDao.deleteOrder(null);

        // Assert - exception expected
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteOrder_EmptyId() {
        // Act
        orderDao.deleteOrder("");

        // Assert - exception expected
    }
}

