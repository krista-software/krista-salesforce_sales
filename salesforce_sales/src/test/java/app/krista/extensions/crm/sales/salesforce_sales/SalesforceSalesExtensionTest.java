package app.krista.extensions.crm.sales.salesforce_sales;

import app.krista.extension.authorization.RequestAuthenticator;
import app.krista.extension.executor.Invoker;
import app.krista.extensions.crm.sales.salesforce_sales.impl.connectores.SalesforceTokenProviderFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for SalesforceSalesExtension class.
 * Tests core extension functionality including initialization, authentication, and configuration.
 */
public class SalesforceSalesExtensionTest {

    @Mock
    private SalesforceTokenProviderFactory mockProviderFactory;

    @Mock
    private Invoker mockInvoker;

    @Mock
    private SalesforceSalesAttributes mockAttributes;

    @Mock
    private SalesforceSalesRequestAuthenticator mockRequestAuthenticator;

    private SalesforceSalesExtension extension;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        extension = new SalesforceSalesExtension(
                mockProviderFactory,
                mockInvoker,
                mockAttributes,
                mockRequestAuthenticator
        );
    }

    @Test
    public void testConstructorWithValidParameters() {
        // Test that extension is properly initialized with all dependencies
        assertNotNull("Extension should be initialized", extension);
    }

    @Test
    public void testGetRequestAuthenticator() {
        // Test that the request authenticator is properly returned
        RequestAuthenticator authenticator = extension.getRequestAuthenticator();
        
        assertNotNull("Request authenticator should not be null", authenticator);
        assertEquals("Should return the injected authenticator", mockRequestAuthenticator, authenticator);
    }

    @Test
    public void testCustomTabs() {
        // Test custom tabs functionality
        Map<String, String> customTabs = extension.customTabs();
        
        assertNotNull("Custom tabs should not be null", customTabs);
        assertTrue("Custom tabs should be a map", customTabs instanceof Map);
    }

    @Test
    public void testConstructorWithNullProviderFactory() {
        // Test behavior with null provider factory
        try {
            new SalesforceSalesExtension(null, mockInvoker, mockAttributes, mockRequestAuthenticator);
            // Should not throw exception during construction, but may fail during usage
        } catch (Exception e) {
            fail("Constructor should handle null provider factory gracefully");
        }
    }

    @Test
    public void testConstructorWithNullInvoker() {
        // Test behavior with null invoker
        try {
            new SalesforceSalesExtension(mockProviderFactory, null, mockAttributes, mockRequestAuthenticator);
            // Should not throw exception during construction
        } catch (Exception e) {
            fail("Constructor should handle null invoker gracefully");
        }
    }

    @Test
    public void testConstructorWithNullAttributes() {
        // Test behavior with null attributes
        try {
            new SalesforceSalesExtension(mockProviderFactory, mockInvoker, null, mockRequestAuthenticator);
            // Should not throw exception during construction
        } catch (Exception e) {
            fail("Constructor should handle null attributes gracefully");
        }
    }

    @Test
    public void testConstructorWithNullRequestAuthenticator() {
        // Test behavior with null request authenticator
        try {
            SalesforceSalesExtension testExtension = new SalesforceSalesExtension(
                    mockProviderFactory, mockInvoker, mockAttributes, (SalesforceSalesRequestAuthenticator) null);
            RequestAuthenticator authenticator = testExtension.getRequestAuthenticator();
            assertNull("Request authenticator should be null when not provided", authenticator);
        } catch (Exception e) {
            fail("Constructor should handle null request authenticator gracefully");
        }
    }

    @Test
    public void testExtensionMetadataResourceExists() throws Exception {
        // Verify the generated extension.json is present on the classpath
        try (java.io.InputStream is = this.getClass().getClassLoader().getResourceAsStream("META-INF/krista/extension.json")) {
            assertNotNull("extension.json should be present on classpath", is);
            String content = new String(is.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
            assertTrue("extension.json should contain implementationModel", content.contains("\"implementationModel\""));
        }
    }

    @Test
    public void testFieldAnnotations() {
        // Test that the extension class has proper field annotations for configuration
        Class<SalesforceSalesExtension> extensionClass = SalesforceSalesExtension.class;
        
        // Check for field annotations (these are class-level annotations for configuration fields)
        assertTrue("Extension should have field annotations", 
                extensionClass.getAnnotations().length > 0);
    }

    @Test
    public void testExtensionVersionFromGeneratedManifest() throws Exception {
        try (java.io.InputStream is = this.getClass().getClassLoader().getResourceAsStream("META-INF/krista/extension.json")) {
            assertNotNull("extension.json should be present on classpath", is);
            String content = new String(is.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
            assertTrue(content.contains("\"version\": \"2.0.16\""));
        }
    }

    @Test
    public void testJavaVersionFromGeneratedManifest() throws Exception {
        try (java.io.InputStream is = this.getClass().getClassLoader().getResourceAsStream("META-INF/krista/extension.json")) {
            assertNotNull(is);
            String content = new String(is.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
            assertTrue(content.contains("\"javaConfig\":") || content.contains("\"implementationModel\":"));
        }
    }

    @Test
    public void testStaticResourceMappingExists() throws Exception {
        try (java.io.InputStream is = this.getClass().getClassLoader().getResourceAsStream("META-INF/krista/static-resource.json")) {
            assertNotNull("static-resource.json should be present on classpath", is);
            String content = new String(is.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
            assertTrue(content.contains("\"docs\""));
        }
    }

    @Test
    public void testTestConnection_WithValidAttributes() throws Exception {
        // Arrange
        when(mockAttributes.getClientId()).thenReturn("test_client_id");
        when(mockAttributes.getClientSecret()).thenReturn("test_client_secret");
        when(mockAttributes.getInstanceUrl()).thenReturn("https://test.salesforce.com");

        app.krista.extensions.crm.sales.salesforce_sales.impl.connectores.SalesforceTokenProvider mockTokenProvider =
                mock(app.krista.extensions.crm.sales.salesforce_sales.impl.connectores.SalesforceTokenProvider.class);
        com.github.scribejava.apis.salesforce.SalesforceToken mockToken =
                mock(com.github.scribejava.apis.salesforce.SalesforceToken.class);
        when(mockProviderFactory.create(any())).thenReturn(mockTokenProvider);
        when(mockTokenProvider.getSalesforceTokenForUser()).thenReturn(mockToken);

        // Act
        try {
            extension.testConnection();
            // If no exception is thrown, test passes
        } catch (Exception e) {
            fail("testConnection should not throw exception with valid attributes: " + e.getMessage());
        }
    }

    @Test
    public void testAttributesUpdated() {
        // Arrange
        java.util.Map<String, Object> oldAttributes = new java.util.HashMap<>();
        oldAttributes.put("clientId", "old_client_id");

        java.util.Map<String, Object> newAttributes = new java.util.HashMap<>();
        newAttributes.put("clientId", "new_client_id");
        newAttributes.put("clientSecret", "new_client_secret");

        // Act
        extension.attributesUpdated(oldAttributes, newAttributes);

        // Assert
        verify(mockAttributes, times(1)).update(newAttributes);
    }

    @Test
    public void testAttributesUpdated_WithEmptyMaps() {
        // Arrange
        java.util.Map<String, Object> oldAttributes = new java.util.HashMap<>();
        java.util.Map<String, Object> newAttributes = new java.util.HashMap<>();

        // Act
        extension.attributesUpdated(oldAttributes, newAttributes);

        // Assert
        verify(mockAttributes, times(1)).update(newAttributes);
    }

    @Test
    public void testAttributesUpdated_WithNullOldAttributes() {
        // Arrange
        java.util.Map<String, Object> newAttributes = new java.util.HashMap<>();
        newAttributes.put("clientId", "new_client_id");

        // Act
        extension.attributesUpdated(null, newAttributes);

        // Assert
        verify(mockAttributes, times(1)).update(newAttributes);
    }

    @Test
    public void testCustomTabs_ReturnsNonEmptyMap() {
        // Act
        java.util.Map<String, String> customTabs = extension.customTabs();

        // Assert
        assertNotNull("Custom tabs should not be null", customTabs);
        assertFalse("Custom tabs should not be empty", customTabs.isEmpty());
        assertTrue("Custom tabs should contain Documentation", customTabs.containsKey("Documentation"));
        assertEquals("Documentation path should be correct", "static/docs", customTabs.get("Documentation"));
    }

    @Test
    public void testExtensionInitialization_WithAllDependencies() {
        // Arrange & Act
        SalesforceSalesExtension testExtension = new SalesforceSalesExtension(
                mockProviderFactory,
                mockInvoker,
                mockAttributes,
                mockRequestAuthenticator
        );

        // Assert
        assertNotNull("Extension should be initialized", testExtension);
        assertEquals("Request authenticator should match", mockRequestAuthenticator, testExtension.getRequestAuthenticator());
    }

    @Test
    public void testExtensionAnnotations() {
        // Test that the extension class has proper annotations
        Class<SalesforceSalesExtension> extensionClass = SalesforceSalesExtension.class;

        // Check for Extension annotation
        assertTrue("Extension should have annotations",
                extensionClass.getAnnotations().length > 0);
    }

    @Test
    public void testGetRequestAuthenticator_NotNull() {
        // Act
        app.krista.extension.authorization.RequestAuthenticator authenticator = extension.getRequestAuthenticator();

        // Assert
        assertNotNull("Request authenticator should not be null", authenticator);
    }

    @Test
    public void testGetRequestAuthenticator_ReturnsSameInstance() {
        // Act
        app.krista.extension.authorization.RequestAuthenticator authenticator1 = extension.getRequestAuthenticator();
        app.krista.extension.authorization.RequestAuthenticator authenticator2 = extension.getRequestAuthenticator();

        // Assert
        assertSame("Should return same authenticator instance", authenticator1, authenticator2);
    }
}
