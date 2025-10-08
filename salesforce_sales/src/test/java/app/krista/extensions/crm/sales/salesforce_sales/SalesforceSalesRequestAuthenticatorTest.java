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
}

