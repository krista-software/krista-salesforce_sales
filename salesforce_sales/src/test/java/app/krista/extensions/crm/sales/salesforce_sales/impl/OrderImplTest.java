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

import app.krista.extensions.crm.sales.salesforce_sales.model.SFOrder;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Unit tests for OrderImpl class.
 * Tests order implementation wrapper.
 */
public class OrderImplTest {
    
    private SFOrder sfOrder;
    private OrderImpl order;
    
    @Before
    public void setUp() {
        sfOrder = new SFOrder();
    }
    
    @Test
    public void testConstructor() {
        // Arrange & Act
        order = new OrderImpl(sfOrder);
        
        // Assert
        assertNotNull("Order should not be null", order);
    }
    
    @Test
    public void testGetId() {
        // Arrange
        sfOrder.id = "801xx000000001AAAQ";
        order = new OrderImpl(sfOrder);
        
        // Act
        String result = order.getId();
        
        // Assert
        assertEquals("ID should match", "801xx000000001AAAQ", result);
    }
    
    @Test
    public void testGetId_WithNull() {
        // Arrange
        sfOrder.id = null;
        order = new OrderImpl(sfOrder);
        
        // Act
        String result = order.getId();
        
        // Assert
        assertNull("ID should be null", result);
    }
    
    @Test
    public void testGetOrderNumber() {
        // Arrange
        sfOrder.orderNumber = "ORD-00001234";
        order = new OrderImpl(sfOrder);
        
        // Act
        String result = order.getOrderNumber();
        
        // Assert
        assertEquals("Order Number should match", "ORD-00001234", result);
    }
    
    @Test
    public void testGetAccountId() {
        // Arrange
        sfOrder.accountId = "001xx000000001AAAQ";
        order = new OrderImpl(sfOrder);
        
        // Act
        String result = order.getAccountId();
        
        // Assert
        assertEquals("Account ID should match", "001xx000000001AAAQ", result);
    }
    
    @Test
    public void testGetEffectiveDate() {
        // Arrange
        sfOrder.effectiveDate = "2024-01-15";
        order = new OrderImpl(sfOrder);
        
        // Act
        Long result = order.getEffectiveDate();
        
        // Assert
        assertNotNull("Effective Date should not be null", result);
    }
    
    @Test
    public void testGetEffectiveDate_WithNull() {
        // Arrange
        sfOrder.effectiveDate = null;
        order = new OrderImpl(sfOrder);
        
        // Act
        Long result = order.getEffectiveDate();
        
        // Assert
        assertNull("Effective Date should be null", result);
    }
    
    @Test
    public void testGetStatus() {
        // Arrange
        sfOrder.status = "Draft";
        order = new OrderImpl(sfOrder);
        
        // Act
        String result = order.getStatus();
        
        // Assert
        assertEquals("Status should match", "Draft", result);
    }
    
    @Test
    public void testGetDescription() {
        // Arrange
        sfOrder.description = "Test order for PO-12345";
        order = new OrderImpl(sfOrder);
        
        // Act
        String result = order.getDescription();
        
        // Assert
        assertEquals("Description should match", "Test order for PO-12345", result);
    }
    
    @Test
    public void testGetCustomerAuthorizedById() {
        // Arrange
        sfOrder.customerAuthorizedById = "005xx000000001AAAQ";
        order = new OrderImpl(sfOrder);
        
        // Act
        String result = order.getCustomerAuthorizedById();
        
        // Assert
        assertEquals("Customer Authorized By ID should match", "005xx000000001AAAQ", result);
    }
    
    @Test
    public void testGetCompanyAuthorizedById() {
        // Arrange
        sfOrder.companyAuthorizedById = "005xx000000002AAAQ";
        order = new OrderImpl(sfOrder);

        // Act
        String result = order.getCompanyAuthorizedById();

        // Assert
        assertEquals("Company Authorized By ID should match", "005xx000000002AAAQ", result);
    }

    @Test
    public void testGetType() {
        // Arrange
        sfOrder.type = "Standard";
        order = new OrderImpl(sfOrder);

        // Act
        String result = order.getType();

        // Assert
        assertEquals("Type should match", "Standard", result);
    }

    @Test
    public void testGetBillingAddress() {
        // Arrange
        sfOrder.billingStreet = "123 Main St";
        sfOrder.billingCity = "San Francisco";
        sfOrder.billingState = "CA";
        sfOrder.billingPostalCode = "94105";
        sfOrder.billingCountry = "USA";
        order = new OrderImpl(sfOrder);

        // Assert
        assertEquals("Billing Street should match", "123 Main St", order.getBillingStreet());
        assertEquals("Billing City should match", "San Francisco", order.getBillingCity());
        assertEquals("Billing State should match", "CA", order.getBillingState());
        assertEquals("Billing Postal Code should match", "94105", order.getBillingPostalCode());
        assertEquals("Billing Country should match", "USA", order.getBillingCountry());
    }

    @Test
    public void testGetShippingAddress() {
        // Arrange
        sfOrder.shippingStreet = "456 Oak Ave";
        sfOrder.shippingCity = "Los Angeles";
        sfOrder.shippingState = "CA";
        sfOrder.shippingPostalCode = "90001";
        sfOrder.shippingCountry = "USA";
        order = new OrderImpl(sfOrder);

        // Assert
        assertEquals("Shipping Street should match", "456 Oak Ave", order.getShippingStreet());
        assertEquals("Shipping City should match", "Los Angeles", order.getShippingCity());
        assertEquals("Shipping State should match", "CA", order.getShippingState());
        assertEquals("Shipping Postal Code should match", "90001", order.getShippingPostalCode());
        assertEquals("Shipping Country should match", "USA", order.getShippingCountry());
    }

    @Test
    public void testGetTotalAmount() {
        // Arrange
        sfOrder.totalAmount = 1500.50;
        order = new OrderImpl(sfOrder);

        // Act
        Double result = order.getTotalAmount();

        // Assert
        assertEquals("Total Amount should match", Double.valueOf(1500.50), result);
    }

    @Test
    public void testGetTotalAmount_WithNull() {
        // Arrange
        sfOrder.totalAmount = null;
        order = new OrderImpl(sfOrder);

        // Act
        Double result = order.getTotalAmount();

        // Assert
        assertNull("Total Amount should be null", result);
    }

    @Test
    public void testGetCreatedDate() {
        // Arrange
        sfOrder.createdDate = new Date();
        order = new OrderImpl(sfOrder);

        // Act
        Long result = order.getCreatedDate();

        // Assert
        assertNotNull("Created Date should not be null", result);
        assertTrue("Created Date should be positive", result > 0);
    }

    @Test
    public void testGetCreatedDate_WithNull() {
        // Arrange
        sfOrder.createdDate = null;
        order = new OrderImpl(sfOrder);

        // Act
        Long result = order.getCreatedDate();

        // Assert
        assertNull("Created Date should be null", result);
    }

    @Test
    public void testGetCreatedById() {
        // Arrange
        sfOrder.createdById = "005xx000000003AAAQ";
        order = new OrderImpl(sfOrder);

        // Act
        String result = order.getCreatedById();

        // Assert
        assertEquals("Created By ID should match", "005xx000000003AAAQ", result);
    }

    @Test
    public void testGetOwnerId() {
        // Arrange
        sfOrder.ownerId = "005xx000000004AAAQ";
        order = new OrderImpl(sfOrder);

        // Act
        String result = order.getOwnerId();

        // Assert
        assertEquals("Owner ID should match", "005xx000000004AAAQ", result);
    }

    @Test
    public void testGetPoNumber() {
        // Arrange
        sfOrder.customFields = new HashMap<>();
        sfOrder.customFields.put("PO_Number__c", "PO-2024-001");
        order = new OrderImpl(sfOrder);

        // Act
        String result = order.getPoNumber();

        // Assert
        assertEquals("PO Number should match", "PO-2024-001", result);
    }

    @Test
    public void testGetExtractionStatus() {
        // Arrange
        sfOrder.customFields = new HashMap<>();
        sfOrder.customFields.put("Extraction_Status__c", "Completed");
        order = new OrderImpl(sfOrder);

        // Act
        String result = order.getExtractionStatus();

        // Assert
        assertEquals("Extraction Status should match", "Completed", result);
    }

    @Test
    public void testGetConfidence() {
        // Arrange
        sfOrder.customFields = new HashMap<>();
        sfOrder.customFields.put("Confidence__c", 95.5);
        order = new OrderImpl(sfOrder);

        // Act
        Double result = order.getConfidence();

        // Assert
        assertEquals("Confidence should match", Double.valueOf(95.5), result);
    }

    @Test
    public void testGetConfidence_WithNull() {
        // Arrange
        sfOrder.customFields = new HashMap<>();
        order = new OrderImpl(sfOrder);

        // Act
        Double result = order.getConfidence();

        // Assert
        assertNull("Confidence should be null", result);
    }

    @Test
    public void testAllFields() {
        // Arrange
        sfOrder.id = "801xx000000001AAAQ";
        sfOrder.orderNumber = "ORD-00001234";
        sfOrder.accountId = "001xx000000001AAAQ";
        sfOrder.effectiveDate = "2024-01-15";
        sfOrder.status = "Activated";
        sfOrder.description = "Complete order test";
        sfOrder.totalAmount = 2500.75;

        // Set custom fields using the new customFields map
        sfOrder.customFields = new HashMap<>();
        sfOrder.customFields.put("PO_Number__c", "PO-2024-001");
        sfOrder.customFields.put("Extraction_Status__c", "Completed");
        sfOrder.customFields.put("Confidence__c", 98.5);

        order = new OrderImpl(sfOrder);

        // Assert
        assertEquals("ID should match", "801xx000000001AAAQ", order.getId());
        assertEquals("Order Number should match", "ORD-00001234", order.getOrderNumber());
        assertEquals("Account ID should match", "001xx000000001AAAQ", order.getAccountId());
        assertNotNull("Effective Date should not be null", order.getEffectiveDate());
        assertEquals("Status should match", "Activated", order.getStatus());
        assertEquals("Description should match", "Complete order test", order.getDescription());
        assertEquals("Total Amount should match", Double.valueOf(2500.75), order.getTotalAmount());
        assertEquals("PO Number should match", "PO-2024-001", order.getPoNumber());
        assertEquals("Extraction Status should match", "Completed", order.getExtractionStatus());
        assertEquals("Confidence should match", Double.valueOf(98.5), order.getConfidence());
    }

    @Test
    public void testGetCustomFields() {
        // Arrange
        sfOrder.customFields = new HashMap<>();
        sfOrder.customFields.put("PO_Number__c", "PO-2024-001");
        sfOrder.customFields.put("Extraction_Status__c", "Completed");
        sfOrder.customFields.put("Confidence__c", 95.5);
        sfOrder.customFields.put("Custom_Field_1__c", "Value1");
        order = new OrderImpl(sfOrder);

        // Act
        Map<String, Object> result = order.getCustomFields();

        // Assert
        assertNotNull("Custom fields should not be null", result);
        assertEquals("Should have 4 custom fields", 4, result.size());
        assertEquals("PO Number should match", "PO-2024-001", result.get("PO_Number__c"));
        assertEquals("Extraction Status should match", "Completed", result.get("Extraction_Status__c"));
        assertEquals("Confidence should match", 95.5, result.get("Confidence__c"));
        assertEquals("Custom Field 1 should match", "Value1", result.get("Custom_Field_1__c"));
    }

    @Test
    public void testGetCustomFields_WithNull() {
        // Arrange
        sfOrder.customFields = null;
        order = new OrderImpl(sfOrder);

        // Act
        Map<String, Object> result = order.getCustomFields();

        // Assert
        assertNull("Custom fields should be null", result);
    }

    @Test
    public void testGetCustomFields_WithEmpty() {
        // Arrange
        sfOrder.customFields = new HashMap<>();
        order = new OrderImpl(sfOrder);

        // Act
        Map<String, Object> result = order.getCustomFields();

        // Assert
        assertNotNull("Custom fields should not be null", result);
        assertTrue("Custom fields should be empty", result.isEmpty());
    }
}

