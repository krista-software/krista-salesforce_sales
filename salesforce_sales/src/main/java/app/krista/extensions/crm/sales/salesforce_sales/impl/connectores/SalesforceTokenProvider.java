package app.krista.extensions.crm.sales.salesforce_sales.impl.connectores;

import app.krista.extension.authorization.MustAuthorizeException;
import app.krista.extensions.crm.sales.salesforce_sales.SalesforceSalesAttributes;
import app.krista.extensions.crm.sales.salesforce_sales.impl.stores.RefreshTokenStore;
import app.krista.extensions.crm.sales.salesforce_sales.util.SFHelper;
import app.krista.ksdk.context.AuthorizationContext;
import app.krista.ksdk.context.RequestContext;
import app.krista.model.field.NamedValuedField;
import com.github.scribejava.apis.salesforce.SalesforceToken;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuth2AccessTokenErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class SalesforceTokenProvider {

    private final RefreshTokenStore refreshTokenStore;
    private final RequestContext requestContext;
    private final SalesforceSalesAttributes salesforceSalesAttributes;
    private final String authContextId;
    private final AuthorizationContext authorizationContext;
    private static final Logger LOGGER = LoggerFactory.getLogger(SalesforceTokenProvider.class);


    @Inject
    public SalesforceTokenProvider(RefreshTokenStore refreshTokenStore, RequestContext requestContext,
                                   SalesforceSalesAttributes salesforceSalesAttributes, AuthorizationContext authorizationContext) {
        this(refreshTokenStore, requestContext, salesforceSalesAttributes, authorizationContext, null);
    }

    public SalesforceTokenProvider(RefreshTokenStore refreshTokenStore, RequestContext requestContext, SalesforceSalesAttributes salesforceSalesAttributes, AuthorizationContext authorizationContext, String authContextId) {
        this.requestContext = requestContext;
        this.refreshTokenStore = refreshTokenStore;
        this.salesforceSalesAttributes = salesforceSalesAttributes;
        this.authorizationContext = authorizationContext;
        this.authContextId = authContextId;
    }

    public SalesforceSalesAttributes getSalesforceSalesAttributes() {
        return salesforceSalesAttributes;
    }

    public SalesforceToken getSalesforceTokenForUser() throws IOException {
        return getSalesforceToken(requestContext.invokeAsUser(), null);
    }

    public SalesforceToken getSalesforceTokenForUser(boolean invokeAsUser, String accountId) throws IOException {
        return getSalesforceToken(invokeAsUser, accountId);
    }

    public SalesforceToken getSalesforceTokenForAdmin() throws IOException {
        return getSalesforceToken(false, null);
    }

    private SalesforceToken getSalesforceToken(boolean isAdmin, String accountId) throws IOException {
        if (SFHelper.validateUsingOAuth(isAdmin, salesforceSalesAttributes)) {
            accountId = accountId == null ? authorizationContext.getAuthorizedAccount().getAccountId() : accountId;
            String refreshToken = refreshTokenStore.get(getRefreshTokenKey(accountId));
            if (refreshToken == null) {
                throw createMustAuthorizationException(false, accountId);
            }
            try {
                return getSalesforceTokenForUser(salesforceSalesAttributes.getOauthService().refreshAccessToken(refreshToken));
            } catch (MustAuthorizeException cause) {
                throw createMustAuthorizationException(true, accountId);
            } catch (OAuth2AccessTokenErrorResponse cause) {
                if (cause.getErrorDescription() != null && cause.getErrorDescription().equals("expired access/refresh token")) {
                    throw createMustAuthorizationException(true, accountId);
                }
                throw new RuntimeException("Something went wrong, please try again later. " + cause.getErrorDescription(), cause);
            } catch (Exception cause) {
                throw new RuntimeException("Refresh Token not found. Please re authorize yourself. " + cause.getMessage(), cause);
            }
        } else {
            Object username = salesforceSalesAttributes.getUserName();
            Object password = salesforceSalesAttributes.getPassword();
            Object securityToken = salesforceSalesAttributes.getSecurityToken();

            try {
                return getSalesforceTokenForUser(
                        salesforceSalesAttributes.getOauthService().getAccessTokenPasswordGrant(String.valueOf(username),
                                password + String.valueOf(securityToken)));
            } catch (Exception cause) {
                throw new IllegalArgumentException(cause.getMessage());
            }
        }
    }

    private String getRefreshTokenKey(String accountId) {
        return accountId + "&" + salesforceSalesAttributes.getClientId() + salesforceSalesAttributes.getClientSecret();
    }

    private SalesforceToken getSalesforceTokenForUser(OAuth2AccessToken accessToken) {
        final SalesforceToken salesforceAccessToken;
        if (accessToken instanceof SalesforceToken) {
            salesforceAccessToken = (SalesforceToken) accessToken;
        } else {
            throw new MustAuthorizeException("Salesforce didn't return the correct token. Please Reauthorize");
        }
        return salesforceAccessToken;
    }

    private MustAuthorizeException createMustAuthorizationException(boolean reAuthentication, String accountId) {
        LOGGER.info(reAuthentication ? "Got error for Authentication. So sending for re authentication" : "Got error for Authentication. So sending for authentication");
        accountId = accountId == null ? authorizationContext.getAuthorizedAccount().getAccountId() : accountId;
        List<NamedValuedField> details = new ArrayList<>();
        NamedValuedField userIdField = new NamedValuedField("userId", "Text", accountId, new HashMap<>(), new HashMap<>());
        details.add(userIdField);
        if (authContextId != null) {
            NamedValuedField contextIdField = new NamedValuedField("authContextId", "Text", authContextId, new HashMap<>(), new HashMap<>());
            details.add(contextIdField);
        }
        return new MustAuthorizeException(reAuthentication ? "Refresh Token Expired. Please reauthorize yourself" : "Authorize yourself and click 'Validate Attributes' before saving changes to proceed further", details);
    }

}