package app.krista.extensions.crm.sales.salesforce_sales.impl;

import app.krista.extensions.crm.sales.salesforce_sales.model.SFOrderItem;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Unit tests for OrderItemImpl class.
 * Tests all getter methods and data transformations.
 */
public class OrderItemImplTest {
    
    private SFOrderItem sfOrderItem;
    private OrderItemImpl orderItemImpl;

    @Before
    public void setUp() {
        sfOrderItem = new SFOrderItem();
        sfOrderItem.id = "802xx000000001AAAQ";
        sfOrderItem.orderId = "801xx000000001AAAQ";
        sfOrderItem.pricebookEntryId = "01uxx000000001AAAQ";
        sfOrderItem.product2Id = "01txx000000001AAAQ";
        sfOrderItem.quantity = 5.0;
        sfOrderItem.unitPrice = 100.0;
        sfOrderItem.totalPrice = 500.0;
        sfOrderItem.listPrice = 120.0;
        sfOrderItem.description = "Test Order Item Description";
        sfOrderItem.orderItemNumber = "00000101";
        sfOrderItem.serviceDate = "2024-01-15";
        sfOrderItem.endDate = "2024-12-31";
        sfOrderItem.createdDate = new Date();
        sfOrderItem.createdById = "005xx000000001AAAQ";
        sfOrderItem.lastModifiedDate = new Date();
        sfOrderItem.lastModifiedById = "005xx000000002AAAQ";
        
        orderItemImpl = new OrderItemImpl(sfOrderItem);
    }
    
    @Test
    public void testGetId() {
        assertEquals("802xx000000001AAAQ", orderItemImpl.getId());
    }
    
    @Test
    public void testGetOrderId() {
        assertEquals("801xx000000001AAAQ", orderItemImpl.getOrderId());
    }
    
    @Test
    public void testGetPricebookEntryId() {
        assertEquals("01uxx000000001AAAQ", orderItemImpl.getPricebookEntryId());
    }
    
    @Test
    public void testGetProduct2Id() {
        assertEquals("01txx000000001AAAQ", orderItemImpl.getProduct2Id());
    }
    
    @Test
    public void testGetQuantity() {
        assertEquals(5.0, orderItemImpl.getQuantity(), 0.001);
    }

    @Test
    public void testGetUnitPrice() {
        assertEquals(100.0, orderItemImpl.getUnitPrice(), 0.001);
    }

    @Test
    public void testGetTotalPrice() {
        assertEquals(500.0, orderItemImpl.getTotalPrice(), 0.001);
    }

    @Test
    public void testGetListPrice() {
        assertEquals(120.0, orderItemImpl.getListPrice(), 0.001);
    }
    
    @Test
    public void testGetDescription() {
        assertEquals("Test Order Item Description", orderItemImpl.getDescription());
    }
    
    @Test
    public void testGetOrderItemNumber() {
        assertEquals("00000101", orderItemImpl.getOrderItemNumber());
    }
    
    @Test
    public void testGetServiceDate() {
        assertNotNull(orderItemImpl.getServiceDate());
    }
    
    @Test
    public void testGetEndDate() {
        assertNotNull(orderItemImpl.getEndDate());
    }
    
    @Test
    public void testGetCreatedDate() {
        assertNotNull(orderItemImpl.getCreatedDate());
    }
    
    @Test
    public void testGetCreatedById() {
        assertEquals("005xx000000001AAAQ", orderItemImpl.getCreatedById());
    }
    
    @Test
    public void testGetLastModifiedDate() {
        assertNotNull(orderItemImpl.getLastModifiedDate());
    }
    
    @Test
    public void testGetLastModifiedById() {
        assertEquals("005xx000000002AAAQ", orderItemImpl.getLastModifiedById());
    }
    
    @Test
    public void testGetId_Null() {
        sfOrderItem.id = null;
        orderItemImpl = new OrderItemImpl(sfOrderItem);
        assertNull(orderItemImpl.getId());
    }
    
    @Test
    public void testGetQuantity_Null() {
        sfOrderItem.quantity = null;
        orderItemImpl = new OrderItemImpl(sfOrderItem);
        assertNull(orderItemImpl.getQuantity());
    }
    
    @Test
    public void testGetUnitPrice_Null() {
        sfOrderItem.unitPrice = null;
        orderItemImpl = new OrderItemImpl(sfOrderItem);
        assertNull(orderItemImpl.getUnitPrice());
    }
    
    @Test
    public void testGetDescription_Null() {
        sfOrderItem.description = null;
        orderItemImpl = new OrderItemImpl(sfOrderItem);
        assertNull(orderItemImpl.getDescription());
    }
    
    @Test
    public void testGetServiceDate_Null() {
        sfOrderItem.serviceDate = null;
        orderItemImpl = new OrderItemImpl(sfOrderItem);
        assertNull(orderItemImpl.getServiceDate());
    }
    
    @Test
    public void testGetCreatedDate_Null() {
        sfOrderItem.createdDate = null;
        orderItemImpl = new OrderItemImpl(sfOrderItem);
        assertNull(orderItemImpl.getCreatedDate());
    }
}

