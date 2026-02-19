package app.krista.extensions.crm.sales.salesforce_sales.impl;

import app.krista.extensions.crm.sales.salesforce_sales.service.OrderItem;
import app.krista.extensions.crm.sales.salesforce_sales.util.SalesforceListResponse;
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
 * Unit tests for OrderItemDaoImpl class.
 * Tests all CRUD operations and query methods.
 */
public class OrderItemDaoImplTest {
    
    @Mock
    private SalesforceManager salesforceManager;
    
    private OrderItemDaoImpl orderItemDao;
    private Gson gson;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        orderItemDao = new OrderItemDaoImpl(salesforceManager);
        gson = new Gson();
    }
    
    @Test
    public void testGetOrderItemById_Success() {
        String orderItemId = "802xx000000001AAAQ";
        String mockResponse = "{\"Id\":\"802xx000000001AAAQ\",\"OrderId\":\"801xx000000001AAAQ\",\"Quantity\":5.0}";
        
        when(salesforceManager.getRecordById(orderItemId, "OrderItem")).thenReturn(mockResponse);
        
        OrderItem result = orderItemDao.getOrderItemById(orderItemId);
        
        assertNotNull(result);
        assertEquals(orderItemId, result.getId());
        verify(salesforceManager, times(1)).getRecordById(orderItemId, "OrderItem");
    }
    
    @Test
    public void testGetOrderItemById_Exception() {
        String orderItemId = "invalid_id";
        
        when(salesforceManager.getRecordById(orderItemId, "OrderItem"))
                .thenThrow(new RuntimeException("Salesforce API error"));
        
        assertThrows(IllegalArgumentException.class, () -> orderItemDao.getOrderItemById(orderItemId));
    }
    
    @Test
    public void testGetOrderItemsByOrderId_Success() {
        String orderId = "801xx000000001AAAQ";
        String mockResponse = "{\"totalSize\":1,\"done\":true,\"records\":[{\"Id\":\"802xx000000001AAAQ\",\"OrderId\":\"801xx000000001AAAQ\"}]}";
        
        when(salesforceManager.getRecordByQuery(anyString())).thenReturn(mockResponse);
        
        List<OrderItem> results = orderItemDao.getOrderItemsByOrderId(orderId, 10, 1);
        
        assertNotNull(results);
        assertEquals(1, results.size());
        verify(salesforceManager, times(1)).getRecordByQuery(anyString());
    }
    
    @Test
    public void testGetOrderItemsByProductId_Success() {
        String productId = "01txx000000001AAAQ";
        String mockResponse = "{\"totalSize\":1,\"done\":true,\"records\":[{\"Id\":\"802xx000000001AAAQ\",\"Product2Id\":\"01txx000000001AAAQ\"}]}";
        
        when(salesforceManager.getRecordByQuery(anyString())).thenReturn(mockResponse);
        
        List<OrderItem> results = orderItemDao.getOrderItemsByProductId(productId, 10, 1);
        
        assertNotNull(results);
        assertEquals(1, results.size());
        verify(salesforceManager, times(1)).getRecordByQuery(anyString());
    }
    
    @Test
    public void testGetOrderItems_Success() {
        String mockResponse = "{\"totalSize\":2,\"done\":true,\"records\":[{\"Id\":\"802xx000000001AAAQ\"},{\"Id\":\"802xx000000002AAAQ\"}]}";
        
        when(salesforceManager.getRecordByQuery(anyString())).thenReturn(mockResponse);
        
        List<OrderItem> results = orderItemDao.getOrderItems(10, 1);
        
        assertNotNull(results);
        assertEquals(2, results.size());
        verify(salesforceManager, times(1)).getRecordByQuery(anyString());
    }
    
    @Test
    public void testAddOrderItem_Success() {
        Map<String, Object> orderItemFields = new HashMap<>();
        orderItemFields.put("Order Id", "801xx000000001AAAQ");
        orderItemFields.put("Pricebook Entry Id", "01uxx000000001AAAQ");
        orderItemFields.put("Quantity", 5.0);
        
        String mockResponse = "{\"id\":\"802xx000000001AAAQ\",\"success\":true}";
        
        when(salesforceManager.createRecord(anyString(), eq("OrderItem"))).thenReturn(mockResponse);
        
        String result = orderItemDao.addOrderItem(orderItemFields);
        
        assertNotNull(result);
        assertEquals("802xx000000001AAAQ", result);
        verify(salesforceManager, times(1)).createRecord(anyString(), eq("OrderItem"));
    }
    
    @Test
    public void testAddOrderItem_NullFields() {
        assertThrows(IllegalArgumentException.class, () -> orderItemDao.addOrderItem(null));
    }
    
    @Test
    public void testAddOrderItem_EmptyFields() {
        Map<String, Object> emptyFields = new HashMap<>();
        assertThrows(IllegalArgumentException.class, () -> orderItemDao.addOrderItem(emptyFields));
    }
    
    @Test
    public void testAddOrderItem_MissingOrderId() {
        Map<String, Object> orderItemFields = new HashMap<>();
        orderItemFields.put("Pricebook Entry Id", "01uxx000000001AAAQ");
        orderItemFields.put("Quantity", 5.0);
        
        assertThrows(IllegalArgumentException.class, () -> orderItemDao.addOrderItem(orderItemFields));
    }
    
    @Test
    public void testAddOrderItem_MissingPricebookEntryId() {
        Map<String, Object> orderItemFields = new HashMap<>();
        orderItemFields.put("Order Id", "801xx000000001AAAQ");
        orderItemFields.put("Quantity", 5.0);
        
        assertThrows(IllegalArgumentException.class, () -> orderItemDao.addOrderItem(orderItemFields));
    }

    @Test
    public void testAddOrderItem_MissingQuantity() {
        Map<String, Object> orderItemFields = new HashMap<>();
        orderItemFields.put("Order Id", "801xx000000001AAAQ");
        orderItemFields.put("Pricebook Entry Id", "01uxx000000001AAAQ");
        assertThrows(IllegalArgumentException.class, () -> orderItemDao.addOrderItem(orderItemFields));
    }

    @Test
    public void testUpdateOrderItem_Success() {
        String orderItemId = "802xx000000001AAAQ";
        String getResponse = "{\"Id\":\"802xx000000001AAAQ\",\"OrderId\":\"801xx000000001AAAQ\",\"Quantity\":5.0}";
        when(salesforceManager.getRecordById(orderItemId, "OrderItem")).thenReturn(getResponse);
        when(salesforceManager.getDifference(any(), any())).thenReturn(Map.of("Quantity", 10.0));
        when(salesforceManager.updateRecord(eq(orderItemId), anyString(), eq("OrderItem"))).thenReturn("");
        OrderItem orderItem = orderItemDao.getOrderItemById(orderItemId);
        orderItemDao.updateOrderItem(orderItem);
        verify(salesforceManager, times(1)).updateRecord(eq(orderItemId), anyString(), eq("OrderItem"));
    }

    @Test
    public void testUpdateOrderItem_NullId() {
        OrderItem orderItem = mock(OrderItem.class);
        when(orderItem.getId()).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> orderItemDao.updateOrderItem(orderItem));
    }

    @Test
    public void testUpdateOrderItem_EmptyId() {
        OrderItem orderItem = mock(OrderItem.class);
        when(orderItem.getId()).thenReturn("");
        assertThrows(IllegalArgumentException.class, () -> orderItemDao.updateOrderItem(orderItem));
    }

    @Test
    public void testDeleteOrderItem_Success() {
        String orderItemId = "802xx000000001AAAQ";
        doNothing().when(salesforceManager).deleteRecordById(orderItemId, "OrderItem");
        orderItemDao.deleteOrderItem(orderItemId);
        verify(salesforceManager, times(1)).deleteRecordById(orderItemId, "OrderItem");
    }

    @Test
    public void testDeleteOrderItem_NullId() {
        assertThrows(IllegalArgumentException.class, () -> orderItemDao.deleteOrderItem(null));
    }

    @Test
    public void testDeleteOrderItem_EmptyId() {
        assertThrows(IllegalArgumentException.class, () -> orderItemDao.deleteOrderItem(""));
    }
}
