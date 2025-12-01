package app.krista.extensions.crm.sales.salesforce_sales;

import app.krista.extension.executor.Invoker;
import app.krista.extension.request.RoutingInfo;
import app.krista.extension.request.protos.http.HttpProtocol;
import com.github.scribejava.core.oauth.OAuth20Service;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for SalesforceSalesAttributes class.
 * Tests configuration management, OAuth service creation, and validation logic.
 */
public class SalesforceSalesAttributesTest {

    @Mock
    private Invoker mockInvoker;

    @Mock
    private RoutingInfo mockRoutingInfo;

    private Map<String, Object> testAttributes;
    private AutoCloseable closeable;

    private static final String TEST_ROUTING_URL = "https://test.krista.app";
    private static final String TEST_CLIENT_ID = "test_client_id";
    private static final String TEST_CLIENT_SECRET = "test_client_secret";
    private static final String TEST_INSTANCE_URL = "https://test.salesforce.com";
    private static final String TEST_USERNAME = "test@example.com";
    private static final String TEST_PASSWORD = "testPassword";
    private static final String TEST_SECURITY_TOKEN = "testToken123";
    private static final String TEST_TIMEZONE = "America/New_York";

    @Before
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        testAttributes = new HashMap<>();
        testAttributes.put(SalesforceSalesAttributes.CLIENT_ID, TEST_CLIENT_ID);
        testAttributes.put(SalesforceSalesAttributes.CLIENT_SECRET, TEST_CLIENT_SECRET);
        testAttributes.put(SalesforceSalesAttributes.INSTANCE_URL, TEST_INSTANCE_URL);
        testAttributes.put(SalesforceSalesAttributes.USER_NAME, TEST_USERNAME);
        testAttributes.put(SalesforceSalesAttributes.PASS_WORD, TEST_PASSWORD);
        testAttributes.put(SalesforceSalesAttributes.SECURITY_TOKEN, TEST_SECURITY_TOKEN);
        testAttributes.put(SalesforceSalesAttributes.SANDBOX, false);
        testAttributes.put(SalesforceSalesAttributes.TIME_ZONE, TEST_TIMEZONE);

        when(mockInvoker.getRoutingInfo()).thenReturn(mockRoutingInfo);
        when(mockRoutingInfo.getRoutingURL(HttpProtocol.PROTOCOL_NAME, RoutingInfo.Type.APPLIANCE))
                .thenReturn(TEST_ROUTING_URL);
        when(mockInvoker.getAttributes()).thenReturn(testAttributes);
    }

    @After
    public void tearDown() throws Exception {
        if (closeable != null) {
            closeable.close();
        }
    }

    // ========== Constructor Tests ==========

    @Test
    public void testConstructorWithInvoker() {
        // Arrange & Act
        SalesforceSalesAttributes attributes = new SalesforceSalesAttributes(mockInvoker);

        // Assert
        assertNotNull("Attributes should be initialized", attributes);
        assertEquals("Client ID should match", TEST_CLIENT_ID, attributes.getClientId());
        assertEquals("Client Secret should match", TEST_CLIENT_SECRET, attributes.getClientSecret());
        assertEquals("Instance URL should match", TEST_INSTANCE_URL, attributes.getInstanceUrl());
        assertEquals("Username should match", TEST_USERNAME, attributes.getUserName());
        assertEquals("Password should match", TEST_PASSWORD, attributes.getPassword());
        assertEquals("Security Token should match", TEST_SECURITY_TOKEN, attributes.getSecurityToken());
        assertEquals("Timezone should match", TEST_TIMEZONE, attributes.getTimezone());
        assertFalse("Sandbox should be false", attributes.isSandbox());
    }

    @Test
    public void testConstructorWithRoutingUrlAndAttributes() {
        // Arrange & Act
        SalesforceSalesAttributes attributes = new SalesforceSalesAttributes(TEST_ROUTING_URL, testAttributes);

        // Assert
        assertNotNull("Attributes should be initialized", attributes);
        assertEquals("Client ID should match", TEST_CLIENT_ID, attributes.getClientId());
        assertEquals("Client Secret should match", TEST_CLIENT_SECRET, attributes.getClientSecret());
    }

    @Test
    public void testConstructorWithIndividualParameters() {
        // Arrange & Act
        SalesforceSalesAttributes attributes = new SalesforceSalesAttributes(
                TEST_CLIENT_ID, TEST_CLIENT_SECRET, TEST_INSTANCE_URL,
                TEST_USERNAME, TEST_PASSWORD, TEST_SECURITY_TOKEN,
                false, TEST_ROUTING_URL, TEST_TIMEZONE
        );

        // Assert
        assertNotNull("Attributes should be initialized", attributes);
        assertEquals("Client ID should match", TEST_CLIENT_ID, attributes.getClientId());
        assertEquals("Client Secret should match", TEST_CLIENT_SECRET, attributes.getClientSecret());
        assertEquals("Instance URL should match", TEST_INSTANCE_URL, attributes.getInstanceUrl());
        assertEquals("Username should match", TEST_USERNAME, attributes.getUserName());
        assertEquals("Password should match", TEST_PASSWORD, attributes.getPassword());
        assertEquals("Security Token should match", TEST_SECURITY_TOKEN, attributes.getSecurityToken());
        assertEquals("Timezone should match", TEST_TIMEZONE, attributes.getTimezone());
        assertFalse("Sandbox should be false", attributes.isSandbox());
    }

    @Test
    public void testConstructorWithSandboxTrue() {
        // Arrange & Act
        SalesforceSalesAttributes attributes = new SalesforceSalesAttributes(
                TEST_CLIENT_ID, TEST_CLIENT_SECRET, TEST_INSTANCE_URL,
                TEST_USERNAME, TEST_PASSWORD, TEST_SECURITY_TOKEN,
                true, TEST_ROUTING_URL, TEST_TIMEZONE
        );

        // Assert
        assertTrue("Sandbox should be true", attributes.isSandbox());
    }

    @Test
    public void testConstructorWithNullSandbox() {
        // Arrange & Act
        SalesforceSalesAttributes attributes = new SalesforceSalesAttributes(
                TEST_CLIENT_ID, TEST_CLIENT_SECRET, TEST_INSTANCE_URL,
                TEST_USERNAME, TEST_PASSWORD, TEST_SECURITY_TOKEN,
                null, TEST_ROUTING_URL, TEST_TIMEZONE
        );

        // Assert
        assertFalse("Sandbox should default to false when null", attributes.isSandbox());
    }

    // ========== Getter Tests ==========

    @Test
    public void testGetters() {
        // Arrange
        SalesforceSalesAttributes attributes = new SalesforceSalesAttributes(TEST_ROUTING_URL, testAttributes);

        // Act & Assert
        assertEquals("Client ID getter should work", TEST_CLIENT_ID, attributes.getClientId());
        assertEquals("Client Secret getter should work", TEST_CLIENT_SECRET, attributes.getClientSecret());
        assertEquals("Instance URL getter should work", TEST_INSTANCE_URL, attributes.getInstanceUrl());
        assertEquals("Username getter should work", TEST_USERNAME, attributes.getUserName());
        assertEquals("Password getter should work", TEST_PASSWORD, attributes.getPassword());
        assertEquals("Security Token getter should work", TEST_SECURITY_TOKEN, attributes.getSecurityToken());
        assertEquals("Timezone getter should work", TEST_TIMEZONE, attributes.getTimezone());
        assertFalse("Sandbox getter should work", attributes.isSandbox());
    }

    // ========== OAuth Service Tests ==========

    @Test
    public void testGetOauthService_Production() {
        // Arrange
        SalesforceSalesAttributes attributes = new SalesforceSalesAttributes(
                TEST_CLIENT_ID, TEST_CLIENT_SECRET, TEST_INSTANCE_URL,
                TEST_USERNAME, TEST_PASSWORD, TEST_SECURITY_TOKEN,
                false, TEST_ROUTING_URL, TEST_TIMEZONE
        );

        // Act
        OAuth20Service service = attributes.getOauthService();

        // Assert
        assertNotNull("OAuth service should be created", service);
    }

    @Test
    public void testGetOauthService_Sandbox() {
        // Arrange
        SalesforceSalesAttributes attributes = new SalesforceSalesAttributes(
                TEST_CLIENT_ID, TEST_CLIENT_SECRET, TEST_INSTANCE_URL,
                TEST_USERNAME, TEST_PASSWORD, TEST_SECURITY_TOKEN,
                true, TEST_ROUTING_URL, TEST_TIMEZONE
        );

        // Act
        OAuth20Service service = attributes.getOauthService();

        // Assert
        assertNotNull("OAuth service should be created for sandbox", service);
    }

    @Test
    public void testGetOauthService_Cached() {
        // Arrange
        SalesforceSalesAttributes attributes = new SalesforceSalesAttributes(
                TEST_CLIENT_ID, TEST_CLIENT_SECRET, TEST_INSTANCE_URL,
                TEST_USERNAME, TEST_PASSWORD, TEST_SECURITY_TOKEN,
                false, TEST_ROUTING_URL, TEST_TIMEZONE
        );

        // Act
        OAuth20Service service1 = attributes.getOauthService();
        OAuth20Service service2 = attributes.getOauthService();

        // Assert
        assertSame("OAuth service should be cached", service1, service2);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetOauthService_MissingClientId() {
        // Arrange
        SalesforceSalesAttributes attributes = new SalesforceSalesAttributes(
                null, TEST_CLIENT_SECRET, TEST_INSTANCE_URL,
                TEST_USERNAME, TEST_PASSWORD, TEST_SECURITY_TOKEN,
                false, TEST_ROUTING_URL, TEST_TIMEZONE
        );

        // Act
        attributes.getOauthService();

        // Assert - exception expected
    }

    @Test(expected = IllegalStateException.class)
    public void testGetOauthService_MissingClientSecret() {
        // Arrange
        SalesforceSalesAttributes attributes = new SalesforceSalesAttributes(
                TEST_CLIENT_ID, null, TEST_INSTANCE_URL,
                TEST_USERNAME, TEST_PASSWORD, TEST_SECURITY_TOKEN,
                false, TEST_ROUTING_URL, TEST_TIMEZONE
        );

        // Act
        attributes.getOauthService();

        // Assert - exception expected
    }

    @Test(expected = IllegalStateException.class)
    public void testGetOauthService_EmptyClientId() {
        // Arrange
        SalesforceSalesAttributes attributes = new SalesforceSalesAttributes(
                "   ", TEST_CLIENT_SECRET, TEST_INSTANCE_URL,
                TEST_USERNAME, TEST_PASSWORD, TEST_SECURITY_TOKEN,
                false, TEST_ROUTING_URL, TEST_TIMEZONE
        );

        // Act
        attributes.getOauthService();

        // Assert - exception expected
    }

    @Test(expected = IllegalStateException.class)
    public void testGetOauthService_EmptyClientSecret() {
        // Arrange
        SalesforceSalesAttributes attributes = new SalesforceSalesAttributes(
                TEST_CLIENT_ID, "   ", TEST_INSTANCE_URL,
                TEST_USERNAME, TEST_PASSWORD, TEST_SECURITY_TOKEN,
                false, TEST_ROUTING_URL, TEST_TIMEZONE
        );

        // Act
        attributes.getOauthService();

        // Assert - exception expected
    }

    // ========== OAuth Service with Custom Domain Tests ==========

    @Test
    public void testGetOauthService_WithCustomDomain() {
        // Arrange
        String customDomain = "https://custom.my.salesforce.com";
        SalesforceSalesAttributes attributes = new SalesforceSalesAttributes(
                TEST_CLIENT_ID, TEST_CLIENT_SECRET, TEST_INSTANCE_URL,
                TEST_USERNAME, TEST_PASSWORD, TEST_SECURITY_TOKEN,
                false, TEST_ROUTING_URL, TEST_TIMEZONE
        );

        // Act
        OAuth20Service service = attributes.getOauthService(customDomain);

        // Assert
        assertNotNull("OAuth service should be created with custom domain", service);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOauthService_WithNullDomain() {
        // Arrange
        SalesforceSalesAttributes attributes = new SalesforceSalesAttributes(
                TEST_CLIENT_ID, TEST_CLIENT_SECRET, TEST_INSTANCE_URL,
                TEST_USERNAME, TEST_PASSWORD, TEST_SECURITY_TOKEN,
                false, TEST_ROUTING_URL, TEST_TIMEZONE
        );

        // Act
        attributes.getOauthService(null);

        // Assert - exception expected
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOauthService_WithEmptyDomain() {
        // Arrange
        SalesforceSalesAttributes attributes = new SalesforceSalesAttributes(
                TEST_CLIENT_ID, TEST_CLIENT_SECRET, TEST_INSTANCE_URL,
                TEST_USERNAME, TEST_PASSWORD, TEST_SECURITY_TOKEN,
                false, TEST_ROUTING_URL, TEST_TIMEZONE
        );

        // Act
        attributes.getOauthService("   ");

        // Assert - exception expected
    }

    // ========== Callback URL Tests ==========

    @Test
    public void testGetCallbackUrl_Standard() {
        // Arrange
        SalesforceSalesAttributes attributes = new SalesforceSalesAttributes(
                TEST_CLIENT_ID, TEST_CLIENT_SECRET, TEST_INSTANCE_URL,
                TEST_USERNAME, TEST_PASSWORD, TEST_SECURITY_TOKEN,
                false, TEST_ROUTING_URL, TEST_TIMEZONE
        );

        // Act
        String callbackUrl = attributes.getCallbackUrl();

        // Assert
        assertNotNull("Callback URL should not be null", callbackUrl);
        assertTrue("Callback URL should end with /rest/salesforcesales/callback",
                callbackUrl.endsWith("/rest/salesforcesales/callback"));
    }

    @Test
    public void testGetCallbackUrl_LocalDevelopment() {
        // Arrange
        String localRoutingUrl = "https://extension.local.eng.krista.app";
        SalesforceSalesAttributes attributes = new SalesforceSalesAttributes(
                TEST_CLIENT_ID, TEST_CLIENT_SECRET, TEST_INSTANCE_URL,
                TEST_USERNAME, TEST_PASSWORD, TEST_SECURITY_TOKEN,
                false, localRoutingUrl, TEST_TIMEZONE
        );

        // Act
        String callbackUrl = attributes.getCallbackUrl();

        // Assert
        assertNotNull("Callback URL should not be null", callbackUrl);
        assertTrue("Callback URL should use localhost", callbackUrl.contains("localhost:8765"));
        assertTrue("Callback URL should use http for localhost", callbackUrl.startsWith("http://"));
    }

    // ========== Update Tests ==========

    @Test
    public void testUpdate() {
        // Arrange
        SalesforceSalesAttributes attributes = new SalesforceSalesAttributes(TEST_ROUTING_URL, testAttributes);

        Map<String, Object> newAttributes = new HashMap<>();
        newAttributes.put(SalesforceSalesAttributes.CLIENT_ID, "new_client_id");
        newAttributes.put(SalesforceSalesAttributes.CLIENT_SECRET, "new_client_secret");
        newAttributes.put(SalesforceSalesAttributes.INSTANCE_URL, "https://new.salesforce.com");
        newAttributes.put(SalesforceSalesAttributes.USER_NAME, "new@example.com");
        newAttributes.put(SalesforceSalesAttributes.PASS_WORD, "newPassword");
        newAttributes.put(SalesforceSalesAttributes.SECURITY_TOKEN, "newToken");
        newAttributes.put(SalesforceSalesAttributes.SANDBOX, true);
        newAttributes.put(SalesforceSalesAttributes.TIME_ZONE, "America/Los_Angeles");

        // Act
        attributes.update(newAttributes);

        // Assert
        assertEquals("Client ID should be updated", "new_client_id", attributes.getClientId());
        assertEquals("Client Secret should be updated", "new_client_secret", attributes.getClientSecret());
        assertEquals("Instance URL should be updated", "https://new.salesforce.com", attributes.getInstanceUrl());
        assertEquals("Username should be updated", "new@example.com", attributes.getUserName());
        assertEquals("Password should be updated", "newPassword", attributes.getPassword());
        assertEquals("Security Token should be updated", "newToken", attributes.getSecurityToken());
        assertTrue("Sandbox should be updated to true", attributes.isSandbox());
        assertEquals("Timezone should be updated", "America/Los_Angeles", attributes.getTimezone());
    }

    @Test
    public void testUpdate_WithNullValues() {
        // Arrange
        SalesforceSalesAttributes attributes = new SalesforceSalesAttributes(TEST_ROUTING_URL, testAttributes);

        Map<String, Object> newAttributes = new HashMap<>();
        newAttributes.put(SalesforceSalesAttributes.CLIENT_ID, null);
        newAttributes.put(SalesforceSalesAttributes.CLIENT_SECRET, null);

        // Act
        attributes.update(newAttributes);

        // Assert
        assertNull("Client ID should be null after update", attributes.getClientId());
        assertNull("Client Secret should be null after update", attributes.getClientSecret());
    }

    // ========== Instance URL Validation Tests ==========

    @Test
    public void testValidInstanceUrl_Https() {
        // Arrange & Act
        SalesforceSalesAttributes attributes = new SalesforceSalesAttributes(
                TEST_CLIENT_ID, TEST_CLIENT_SECRET, "https://test.salesforce.com",
                TEST_USERNAME, TEST_PASSWORD, TEST_SECURITY_TOKEN,
                false, TEST_ROUTING_URL, TEST_TIMEZONE
        );

        // Assert
        assertEquals("HTTPS instance URL should be valid", "https://test.salesforce.com", attributes.getInstanceUrl());
    }

    @Test
    public void testValidInstanceUrl_Http() {
        // Arrange & Act
        SalesforceSalesAttributes attributes = new SalesforceSalesAttributes(
                TEST_CLIENT_ID, TEST_CLIENT_SECRET, "http://test.salesforce.com",
                TEST_USERNAME, TEST_PASSWORD, TEST_SECURITY_TOKEN,
                false, TEST_ROUTING_URL, TEST_TIMEZONE
        );

        // Assert
        assertEquals("HTTP instance URL should be valid", "http://test.salesforce.com", attributes.getInstanceUrl());
    }

    @Test
    public void testValidInstanceUrl_Null() {
        // Arrange & Act
        SalesforceSalesAttributes attributes = new SalesforceSalesAttributes(
                TEST_CLIENT_ID, TEST_CLIENT_SECRET, null,
                TEST_USERNAME, TEST_PASSWORD, TEST_SECURITY_TOKEN,
                false, TEST_ROUTING_URL, TEST_TIMEZONE
        );

        // Assert
        assertNull("Null instance URL should be allowed", attributes.getInstanceUrl());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInstanceUrl_InvalidProtocol() {
        // Arrange & Act
        new SalesforceSalesAttributes(
                TEST_CLIENT_ID, TEST_CLIENT_SECRET, "ftp://test.salesforce.com",
                TEST_USERNAME, TEST_PASSWORD, TEST_SECURITY_TOKEN,
                false, TEST_ROUTING_URL, TEST_TIMEZONE
        );

        // Assert - exception expected
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInstanceUrl_MalformedUrl() {
        // Arrange & Act
        new SalesforceSalesAttributes(
                TEST_CLIENT_ID, TEST_CLIENT_SECRET, "not-a-valid-url",
                TEST_USERNAME, TEST_PASSWORD, TEST_SECURITY_TOKEN,
                false, TEST_ROUTING_URL, TEST_TIMEZONE
        );

        // Assert - exception expected
    }

    // ========== Static Factory Method Tests ==========

    @Test
    public void testCreate_WithInvokerAndAttributes() {
        // Arrange & Act
        SalesforceSalesAttributes attributes = SalesforceSalesAttributes.create(mockInvoker, testAttributes);

        // Assert
        assertNotNull("Attributes should be created", attributes);
        assertEquals("Client ID should match", TEST_CLIENT_ID, attributes.getClientId());
        assertEquals("Client Secret should match", TEST_CLIENT_SECRET, attributes.getClientSecret());
    }

    // ========== Validation Tests ==========

    @Test
    public void testValidateConfiguration_ClientCredentials_Valid() {
        // Arrange
        SalesforceSalesAttributes attributes = new SalesforceSalesAttributes(
                TEST_CLIENT_ID, TEST_CLIENT_SECRET, TEST_INSTANCE_URL,
                null, null, null,
                false, TEST_ROUTING_URL, TEST_TIMEZONE
        );

        // Act
        SalesforceSalesAttributes.ValidationResult result =
                attributes.validateConfiguration(SalesforceSalesAttributes.AuthenticationFlowType.CLIENT_CREDENTIALS);

        // Assert
        assertNotNull("Validation result should not be null", result);
        assertTrue("Validation should be valid", result.isValid());
        assertTrue("Errors should be empty", result.getErrors().isEmpty());
    }

    @Test
    public void testValidateConfiguration_ClientCredentials_MissingClientId() {
        // Arrange
        SalesforceSalesAttributes attributes = new SalesforceSalesAttributes(
                null, TEST_CLIENT_SECRET, TEST_INSTANCE_URL,
                null, null, null,
                false, TEST_ROUTING_URL, TEST_TIMEZONE
        );

        // Act
        SalesforceSalesAttributes.ValidationResult result =
                attributes.validateConfiguration(SalesforceSalesAttributes.AuthenticationFlowType.CLIENT_CREDENTIALS);

        // Assert
        assertNotNull("Validation result should not be null", result);
        assertFalse("Validation should be invalid", result.isValid());
        assertFalse("Errors should not be empty", result.getErrors().isEmpty());
    }

    @Test
    public void testValidateConfiguration_PasswordGrant_Valid() {
        // Arrange
        SalesforceSalesAttributes attributes = new SalesforceSalesAttributes(
                TEST_CLIENT_ID, TEST_CLIENT_SECRET, null,
                TEST_USERNAME, TEST_PASSWORD, TEST_SECURITY_TOKEN,
                false, TEST_ROUTING_URL, TEST_TIMEZONE
        );

        // Act
        SalesforceSalesAttributes.ValidationResult result =
                attributes.validateConfiguration(SalesforceSalesAttributes.AuthenticationFlowType.PASSWORD_GRANT);

        // Assert
        assertNotNull("Validation result should not be null", result);
        assertTrue("Validation should be valid", result.isValid());
        assertTrue("Errors should be empty", result.getErrors().isEmpty());
    }

    @Test
    public void testValidateConfiguration_PasswordGrant_MissingUsername() {
        // Arrange
        SalesforceSalesAttributes attributes = new SalesforceSalesAttributes(
                TEST_CLIENT_ID, TEST_CLIENT_SECRET, null,
                null, TEST_PASSWORD, TEST_SECURITY_TOKEN,
                false, TEST_ROUTING_URL, TEST_TIMEZONE
        );

        // Act
        SalesforceSalesAttributes.ValidationResult result =
                attributes.validateConfiguration(SalesforceSalesAttributes.AuthenticationFlowType.PASSWORD_GRANT);

        // Assert
        assertNotNull("Validation result should not be null", result);
        assertFalse("Validation should be invalid", result.isValid());
        assertFalse("Errors should not be empty", result.getErrors().isEmpty());
    }

    @Test
    public void testValidateConfiguration_AuthorizationCode_Valid() {
        // Arrange
        SalesforceSalesAttributes attributes = new SalesforceSalesAttributes(
                TEST_CLIENT_ID, TEST_CLIENT_SECRET, null,
                null, null, null,
                false, TEST_ROUTING_URL, TEST_TIMEZONE
        );

        // Act
        SalesforceSalesAttributes.ValidationResult result =
                attributes.validateConfiguration(SalesforceSalesAttributes.AuthenticationFlowType.AUTHORIZATION_CODE);

        // Assert
        assertNotNull("Validation result should not be null", result);
        assertTrue("Validation should be valid", result.isValid());
        assertTrue("Errors should be empty", result.getErrors().isEmpty());
    }

    @Test
    public void testValidateConfiguration_AutoDetect() {
        // Arrange
        SalesforceSalesAttributes attributes = new SalesforceSalesAttributes(
                TEST_CLIENT_ID, TEST_CLIENT_SECRET, TEST_INSTANCE_URL,
                TEST_USERNAME, TEST_PASSWORD, TEST_SECURITY_TOKEN,
                false, TEST_ROUTING_URL, TEST_TIMEZONE
        );

        // Act
        SalesforceSalesAttributes.ValidationResult result =
                attributes.validateConfiguration(SalesforceSalesAttributes.AuthenticationFlowType.AUTO_DETECT);

        // Assert
        assertNotNull("Validation result should not be null", result);
        assertTrue("Validation should be valid", result.isValid());
    }

    @Test
    public void testValidationResult_AddError() {
        // Arrange
        SalesforceSalesAttributes.ValidationResult result = new SalesforceSalesAttributes.ValidationResult();

        // Act
        result.addError("Test error");

        // Assert
        assertFalse("Validation should be invalid after adding error", result.isValid());
        assertEquals("Should have 1 error", 1, result.getErrors().size());
        assertTrue("Error list should contain the error", result.getErrors().contains("Test error"));
    }

    @Test
    public void testValidationResult_AddWarning() {
        // Arrange
        SalesforceSalesAttributes.ValidationResult result = new SalesforceSalesAttributes.ValidationResult();

        // Act
        result.addWarning("Test warning");

        // Assert
        assertTrue("Validation should still be valid after adding warning", result.isValid());
        assertEquals("Should have 1 warning", 1, result.getWarnings().size());
        assertTrue("Warning list should contain the warning", result.getWarnings().contains("Test warning"));
    }

    @Test
    public void testValidationResult_AddInfo() {
        // Arrange
        SalesforceSalesAttributes.ValidationResult result = new SalesforceSalesAttributes.ValidationResult();

        // Act
        result.addInfo("Test info");

        // Assert
        assertTrue("Validation should still be valid after adding info", result.isValid());
        assertEquals("Should have 1 info", 1, result.getInfo().size());
        assertTrue("Info list should contain the info", result.getInfo().contains("Test info"));
    }
}
