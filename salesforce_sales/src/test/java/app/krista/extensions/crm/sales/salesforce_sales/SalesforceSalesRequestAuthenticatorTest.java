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

package app.krista.extensions.crm.sales.salesforce_sales;

import app.krista.extension.authorization.MustAuthorizeException;
import app.krista.extension.authorization.RequestAuthenticator;
import app.krista.extension.request.ProtoRequest;
import app.krista.extension.request.protos.http.HttpRequest;
import app.krista.extensions.crm.sales.salesforce_sales.impl.stores.SalesforceSaleAttributeStore;
import com.github.scribejava.core.oauth.OAuth20Service;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.net.URI;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SalesforceSalesRequestAuthenticatorTest {

    @Mock
    private SalesforceSalesAttributes mockAttributes;

    @Mock
    private SalesforceSaleAttributeStore mockStore;

    @Mock
    private OAuth20Service mockOAuthService;

    @Mock
    private HttpRequest mockHttpRequest;

    private SalesforceSalesRequestAuthenticator authenticator;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticator = new SalesforceSalesRequestAuthenticator(mockAttributes, mockStore);
    }

    @Test
    public void testGetAuthenticatedAccountId_FromCallbackPath() throws Exception {
        when(mockHttpRequest.getUri()).thenReturn(new URI("/salesforcesales/callback"));
        MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
        params.add("state", "user123&other");
        when(mockHttpRequest.getQueryParameters()).thenReturn(params);

        String accountId = authenticator.getAuthenticatedAccountId(mockHttpRequest);
        assertEquals("user123", accountId);
    }

    @Test
    public void testGetAuthenticatedAccountId_NonCallbackPathReturnsNull() throws Exception {
        when(mockHttpRequest.getUri()).thenReturn(new URI("/other"));
        assertNull(authenticator.getAuthenticatedAccountId(mockHttpRequest));
    }

    @Test
    public void testGetMustAuthorizeResponse_WithoutAuthContext() {
        when(mockAttributes.getOauthService()).thenReturn(mockOAuthService);
        when(mockOAuthService.getAuthorizationUrl("user1")).thenReturn("https://login.salesforce.com/auth?state=user1");

        java.util.Map<String, Object> meta = new HashMap<>();
        MustAuthorizeException ex = new MustAuthorizeException("Missing auth", List.of(
                new app.krista.model.field.NamedValuedField("userId", "userId", "user1", meta, java.util.Map.of())
        ));

        RequestAuthenticator.AuthorizationResponse resp = authenticator.getMustAuthorizeResponse(ex);
        assertNotNull(resp);
        // Verify that OAuth service was invoked with the expected state
        verify(mockOAuthService, times(1)).getAuthorizationUrl("user1");
    }

    @Test
    public void testGetMustAuthorizeResponse_WithAuthContext() {
        SalesforceSalesAttributes ctxAttrs = mock(SalesforceSalesAttributes.class);
        when(mockStore.load("ctx123")).thenReturn(ctxAttrs);
        when(ctxAttrs.getOauthService()).thenReturn(mockOAuthService);
        when(mockOAuthService.getAuthorizationUrl("user1#ctx123")).thenReturn("https://login.salesforce.com/auth?state=user1#ctx123");

        java.util.Map<String, Object> meta = new HashMap<>();
        MustAuthorizeException ex = new MustAuthorizeException("Missing auth", List.of(
                new app.krista.model.field.NamedValuedField("userId", "userId", "user1", meta, java.util.Map.of()),
                new app.krista.model.field.NamedValuedField("authContextId", "authContextId", "ctx123", meta, java.util.Map.of())
        ));

        RequestAuthenticator.AuthorizationResponse resp = authenticator.getMustAuthorizeResponse(ex);
        assertNotNull(resp);
        // Verify that OAuth service was invoked with the expected state
        verify(mockOAuthService, times(1)).getAuthorizationUrl("user1#ctx123");
    }

    @Test
    public void testGetAuthenticatedAccountId_WithNullState() throws Exception {
        when(mockHttpRequest.getUri()).thenReturn(new URI("/salesforcesales/callback"));
        MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
        params.add("state", null);
        when(mockHttpRequest.getQueryParameters()).thenReturn(params);

        String accountId = authenticator.getAuthenticatedAccountId(mockHttpRequest);
        assertNull("Account ID should be null when state is null", accountId);
    }

    @Test
    public void testGetAuthenticatedAccountId_WithNullQueryParameters() throws Exception {
        when(mockHttpRequest.getUri()).thenReturn(new URI("/salesforcesales/callback"));
        when(mockHttpRequest.getQueryParameters()).thenReturn(null);

        String accountId = authenticator.getAuthenticatedAccountId(mockHttpRequest);
        assertNull("Account ID should be null when query parameters are null", accountId);
    }

    @Test
    public void testGetAuthenticatedAccountId_WithEmptyState() throws Exception {
        when(mockHttpRequest.getUri()).thenReturn(new URI("/salesforcesales/callback"));
        MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
        params.add("state", "");
        when(mockHttpRequest.getQueryParameters()).thenReturn(params);

        String accountId = authenticator.getAuthenticatedAccountId(mockHttpRequest);
        assertEquals("Account ID should be empty string", "", accountId);
    }

    @Test
    public void testGetAuthenticatedAccountId_WithStateNoDelimiter() throws Exception {
        when(mockHttpRequest.getUri()).thenReturn(new URI("/salesforcesales/callback"));
        MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
        params.add("state", "user123");
        when(mockHttpRequest.getQueryParameters()).thenReturn(params);

        String accountId = authenticator.getAuthenticatedAccountId(mockHttpRequest);
        assertEquals("Account ID should be the full state", "user123", accountId);
    }

    @Test
    public void testGetAuthenticatedAccountId_WithRuntimeException() throws Exception {
        when(mockHttpRequest.getUri()).thenReturn(new URI("/salesforcesales/callback"));
        when(mockHttpRequest.getQueryParameters()).thenThrow(new RuntimeException("Test exception"));

        String accountId = authenticator.getAuthenticatedAccountId(mockHttpRequest);
        assertNull("Account ID should be null when RuntimeException occurs", accountId);
    }

    @Test
    public void testGetScheme() {
        assertNull("Scheme should be null", authenticator.getScheme());
    }

    @Test
    public void testGetSupportedProtocols() {
        java.util.Set<String> protocols = authenticator.getSupportedProtocols();
        assertNotNull("Supported protocols should not be null", protocols);
        assertTrue("Supported protocols should be empty", protocols.isEmpty());
    }

    @Test
    public void testSetServiceAuthorization() {
        assertFalse("setServiceAuthorization should return false", authenticator.setServiceAuthorization("test"));
    }

    @Test
    public void testGetAttributeFields() {
        java.util.Map<String, app.krista.model.field.NamedField> fields = authenticator.getAttributeFields();
        assertNotNull("Attribute fields should not be null", fields);
        assertTrue("Attribute fields should be empty", fields.isEmpty());
    }

    @Test
    public void testGetMustAuthenticateResponse_WithProtoRequest() {
        app.krista.extension.authorization.MustAuthenticateException ex =
                new app.krista.extension.authorization.MustAuthenticateException("Test");
        ProtoRequest request = mock(ProtoRequest.class);

        app.krista.extension.request.ProtoResponse response = authenticator.getMustAuthenticateResponse(ex, request);
        assertNull("Response should be null", response);
    }

    @Test
    public void testGetMustAuthenticateResponse_WithoutProtoRequest() {
        app.krista.extension.authorization.MustAuthenticateException ex =
                new app.krista.extension.authorization.MustAuthenticateException("Test");

        RequestAuthenticator.AuthorizationResponse response = authenticator.getMustAuthenticateResponse(ex);
        assertNull("Response should be null", response);
    }

    @Test
    public void testGetMustAuthorizeResponse_WithProtoRequest() {
        java.util.Map<String, Object> meta = new HashMap<>();
        app.krista.extension.authorization.MustAuthorizeException ex =
                new app.krista.extension.authorization.MustAuthorizeException("Test", List.of(
                        new app.krista.model.field.NamedValuedField("userId", "userId", "user1", meta, java.util.Map.of())
                ));
        ProtoRequest request = mock(ProtoRequest.class);

        app.krista.extension.request.ProtoResponse response = authenticator.getMustAuthorizeResponse(ex, request);
        assertNull("Response should be null", response);
    }
}
