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

package app.krista.extensions.crm.sales.salesforce_sales.impl.connectores;

import app.krista.extension.authorization.MustAuthorizeException;
import app.krista.extensions.crm.sales.salesforce_sales.SalesforceSalesAttributes;
import app.krista.extensions.crm.sales.salesforce_sales.impl.stores.RefreshTokenStore;
import app.krista.extensions.crm.sales.salesforce_sales.util.Constants;
import app.krista.ksdk.context.AuthorizationContext;
import app.krista.ksdk.context.RequestContext;
import app.krista.model.field.NamedValuedField;
import com.github.scribejava.apis.salesforce.SalesforceToken;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuth2AccessTokenErrorResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public final class SalesforceTokenProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(SalesforceTokenProvider.class);
    private final RefreshTokenStore refreshTokenStore;
    private final RequestContext requestContext;
    private final SalesforceSalesAttributes salesforceSalesAttributes;
    private final String authContextId;
    private final AuthorizationContext authorizationContext;

    /**
     * Constructs a SalesforceTokenProvider with the specified dependencies.
     *
     * @param refreshTokenStore         The store used to persist refresh tokens
     * @param requestContext            The context of the current request
     * @param salesforceSalesAttributes The Salesforce service configuration attributes
     * @param authorizationContext      The authorization context for the current user
     */
    @Inject
    public SalesforceTokenProvider(RefreshTokenStore refreshTokenStore, RequestContext requestContext,
                                   SalesforceSalesAttributes salesforceSalesAttributes, AuthorizationContext authorizationContext) {
        this(refreshTokenStore, requestContext, salesforceSalesAttributes, authorizationContext, null);
    }

    /**
     * Constructs a SalesforceTokenProvider with the specified dependencies and authorization context ID.
     *
     * @param refreshTokenStore         The store used to persist refresh tokens
     * @param requestContext            The context of the current request
     * @param salesforceSalesAttributes The Salesforce service configuration attributes
     * @param authorizationContext      The authorization context for the current user
     * @param authContextId             The authorization context ID, if applicable
     */
    public SalesforceTokenProvider(RefreshTokenStore refreshTokenStore, RequestContext requestContext,
                                   SalesforceSalesAttributes salesforceSalesAttributes,
                                   AuthorizationContext authorizationContext, String authContextId) {
        this.requestContext = requestContext;
        this.refreshTokenStore = refreshTokenStore;
        this.salesforceSalesAttributes = salesforceSalesAttributes;
        this.authorizationContext = authorizationContext;
        this.authContextId = authContextId;
    }

    /**
     * Returns the Salesforce customer sales attributes used by this token provider.
     *
     * @return The Salesforce customer sales attributes
     */
    public SalesforceSalesAttributes getSalesforceSalesAttributes() {
        return salesforceSalesAttributes;
    }

    /**
     * Retrieves a Salesforce token for the current user.
     *
     * @return A valid Salesforce token for the current user
     * @throws MustAuthorizeException If authorization is required to obtain a new token
     */
    public SalesforceToken getSalesforceTokenForUser(boolean invokerAsUser, String accountId) {
        return getSalesforceToken(invokerAsUser, accountId);
    }

    public SalesforceToken getSalesforceTokenForUser() {
        return getSalesforceToken(requestContext.invokeAsUser(), null);
    }

    /**
     * Retrieves a Salesforce token for admin operations.
     *
     * @throws MustAuthorizeException If authorization is required to obtain a new token
     */
    public void getSalesforceTokenForAdmin() {
        getSalesforceToken(false, null);
    }

    /**
     * Retrieves a Salesforce token based on the specified parameters.
     *
     * @param isAdmin   Whether the token is for admin operations
     * @param accountId The account ID to use, or null to use the current authorized account
     * @return A valid Salesforce token
     * @throws IllegalArgumentException If the provided credentials are invalid
     * @throws MustAuthorizeException   If authorization is required to obtain a new token
     */
    private SalesforceToken getSalesforceToken(boolean isAdmin, String accountId) throws IllegalArgumentException {
        if (SalesforceTokenProvider.validateUsingOAuth(isAdmin, salesforceSalesAttributes)) {
            String domain = salesforceSalesAttributes.getInstanceUrl();
            if (domain != null && !domain.isEmpty()) {
                LOGGER.info("Using OAuth 2.0 Client Credentials flow for authentication with custom domain: {}", domain);
                return getTokenUsingClientCredentials(domain);
            } else {
                LOGGER.info("Using OAuth 2.0 Authorization Code flow for authentication (no custom domain provided)");
                accountId = accountId == null ? authorizationContext.getAuthorizedAccount().getAccountId() : accountId;
                String refreshToken = null;
                try {
                    refreshToken = refreshTokenStore.get(getRefreshTokenKey(accountId));
                } catch (IOException cause) {
                    LOGGER.error("Failed to retrieve refresh token from store: {}", cause.getMessage());
                    throw new RuntimeException(cause);
                }
                if (refreshToken == null) {
                    LOGGER.debug("No refresh token found for account: {}, requiring user authorization", accountId);
                    throw createMustAuthorizationException(false, accountId);
                }
                try {
                    LOGGER.debug("Attempting to refresh access token for account: {}", accountId);
                    return getSalesforceTokenForUser(salesforceSalesAttributes.getOauthService().refreshAccessToken(refreshToken));
                } catch (MustAuthorizeException cause) {
                    LOGGER.debug("Refresh token failed, requiring re-authorization for account: {}", accountId);
                    throw createMustAuthorizationException(true, accountId);
                } catch (OAuth2AccessTokenErrorResponse cause) {
                    if (cause.getErrorDescription() != null && cause.getErrorDescription().equals("expired access/refresh token")) {
                        LOGGER.debug("Refresh token expired for account: {}, requiring re-authorization", accountId);
                        throw createMustAuthorizationException(true, accountId);
                    }
                    throw new RuntimeException("OAuth token refresh failed: " + cause.getErrorDescription() +
                            ". This may indicate an issue with your Salesforce connected app configuration or network connectivity. " +
                            "Please verify your OAuth settings and try again.", cause);
                } catch (InterruptedException | ExecutionException | IOException cause) {
                    throw new RuntimeException("OAuth token refresh failed due to a technical error: " + cause.getMessage() +
                            ". Please re-authorize yourself by logging in again. If the problem persists, " +
                            "check your network connection and Salesforce service status.", cause);
                }
            }
        } else {
            LOGGER.info("Using OAuth 2.0 Password Grant flow for authentication with username/password credentials");
            Object username = salesforceSalesAttributes.getUserName();
            Object password = salesforceSalesAttributes.getPassword();
            Object securityToken = salesforceSalesAttributes.getSecurityToken();

            try {
                LOGGER.debug("Attempting password grant authentication for username: {}", username);
                return getSalesforceTokenForUser(
                        salesforceSalesAttributes.getOauthService().getAccessTokenPasswordGrant(String.valueOf(username),
                                password + String.valueOf(securityToken)));
            } catch (Exception cause) {
                LOGGER.error("Password Grant authentication failed for username: {} - {}", username, cause.getMessage());
                String message = this.getErrorMessage(cause);
                throw new IllegalArgumentException(message, cause);
            }
        }
    }

    /**
     * Retrieves a Salesforce token using OAuth 2.0 Client Credentials flow.
     *
     * @param domain The Salesforce instance domain
     * @return A valid Salesforce token
     * @throws IllegalArgumentException If the client credentials are invalid or the request fails
     */
    private SalesforceToken getTokenUsingClientCredentials(String domain) throws IllegalArgumentException {
        try {
            LOGGER.debug("Executing Client Credentials grant request to domain: {}", domain);
            return getSalesforceTokenForUser(salesforceSalesAttributes.getOauthService(domain).getAccessTokenClientCredentialsGrant());
        } catch (Exception cause) {
            LOGGER.error("Client Credentials flow failed for domain: {} - {}", domain, cause.getMessage(), cause);
            String message = this.getErrorMessage(cause);
            throw new IllegalArgumentException(message, cause);
        }
    }

    /**
     * Returns the value of the "error" key from the exception message if it is valid JSON.
     * If parsing fails, returns a generic error message.
     * <p>
     * This method parses Salesforce API error responses and provides user-friendly
     * error messages based on the error type.
     *
     * @param cause The exception to extract the error message from
     * @return The error value, or a generic message if not available
     */
    private String getErrorMessage(Exception cause) {
        String message = cause.getMessage();
        if (message != null) {
            try {
                // Attempt to parse the message as JSON
                JSONObject json = new JSONObject(message);
                if (json.has(Constants.JSON_ERROR_KEY)) {
                    // Return the value of the "error" key directly
                    String errorMessage = json.getString(Constants.JSON_ERROR_KEY);
                    return switch (errorMessage) {
                        // Thrown when the client ID or Client Secret is invalid
                        case Constants.INVALID_CLIENT_ERROR -> {
                            LOGGER.debug("Invalid client credentials detected");
                            yield buildClientCredentialsErrorMessage();
                        }

                        //Thrown when the user credentials (username, password, or security token) are invalid
                        case Constants.INVALID_GRANT_ERROR -> {
                            LOGGER.debug("Invalid grant credentials detected");
                            yield buildPasswordGrantErrorMessage();
                        }

                        //Handles any other authentication errors returned by Salesforce
                        default -> {
                            LOGGER.debug("Other authentication error: {}", errorMessage);
                            yield Constants.AUTHENTICATION_FAILED_PREFIX + errorMessage + ". " +
                                    "Please verify your Salesforce configuration and try again.";
                        }
                    };
                }
            } catch (Exception exception) {
                LOGGER.debug("Failed to parse error message as JSON: {}", exception.getMessage(), exception);
                return buildGenericErrorMessage(message);
            }
        }
        return "An unexpected error occurred during authentication. Please verify your Salesforce configuration and contact support if the issue persists.";
    }

    /**
     * Builds a detailed error message for client credentials authentication failures.
     * Provides specific guidance based on the current configuration.
     */
    private String buildClientCredentialsErrorMessage() {
        StringBuilder errorMsg = new StringBuilder(Constants.INVALID_CLIENT_MESSAGE);

        // Add specific guidance based on configuration
        String instanceUrl = salesforceSalesAttributes.getInstanceUrl();
        if (instanceUrl != null && !instanceUrl.trim().isEmpty()) {
            errorMsg.append(" You are using Client Credentials flow with custom domain: ")
                    .append(instanceUrl)
                    .append(". Verify that:");
        } else {
            errorMsg.append(" You are using Authorization Code flow. Verify that:");
        }

        errorMsg.append("\n1. Your Client ID is correct and active in Salesforce")
                .append("\n2. Your Client Secret matches the connected app configuration")
                .append("\n3. The connected app has the correct OAuth scopes enabled");

        if (instanceUrl != null && !instanceUrl.trim().isEmpty()) {
            errorMsg.append("\n4. The Instance URL (").append(instanceUrl).append(") is correct and accessible");
        }

        return errorMsg.toString();
    }

    /**
     * Builds a detailed error message for password grant authentication failures.
     * Provides specific guidance for username/password authentication.
     */
    private String buildPasswordGrantErrorMessage() {
        StringBuilder errorMsg = new StringBuilder(Constants.INVALID_GRANT_MESSAGE);

        String username = String.valueOf(salesforceSalesAttributes.getUserName());
        errorMsg.append(" You are using Password Grant flow");

        if (username != null && !username.equals("null") && !username.trim().isEmpty()) {
            errorMsg.append(" with username: ").append(username);
        }

        errorMsg.append(". Verify that:")
                .append("\n1. Your username is correct and the user account is active")
                .append("\n2. Your password is current and not expired")
                .append("\n3. Your security token is valid (check your email for the latest token)")
                .append("\n4. The user has API access enabled in their profile")
                .append("\n5. Your organization allows API access")
                .append("\n6. You are connecting to the correct Salesforce environment (production vs sandbox)");

        return errorMsg.toString();
    }

    /**
     * Builds a generic error message when JSON parsing fails.
     * Attempts to extract useful information from the raw error message.
     */
    private String buildGenericErrorMessage(String rawMessage) {
        // Check for common error patterns in non-JSON responses
        if (rawMessage.toLowerCase().contains("unauthorized")) {
            return "Authentication failed: Unauthorized access. Please check your credentials and ensure your user account has the necessary permissions.";
        } else if (rawMessage.toLowerCase().contains("forbidden")) {
            return "Authentication failed: Access forbidden. Your credentials may be correct but lack sufficient permissions for API access.";
        } else if (rawMessage.toLowerCase().contains("timeout")) {
            return "Authentication failed: Request timeout. Please check your network connection and try again.";
        } else if (rawMessage.toLowerCase().contains("connection")) {
            return "Authentication failed: Connection error. Please verify your Instance URL and network connectivity.";
        } else if (rawMessage.toLowerCase().contains("ssl") || rawMessage.toLowerCase().contains("certificate")) {
            return "Authentication failed: SSL/Certificate error. Please verify your Instance URL uses HTTPS and has a valid certificate.";
        }

        // Generic fallback with the original message
        return "Authentication failed with error: " + rawMessage +
                ". Please verify your Salesforce configuration and contact support if the issue persists.";
    }

    /**
     * Generates a unique key for storing and retrieving refresh tokens.
     *
     * @param accountId The account ID associated with the refresh token
     * @return A unique key combining account ID and client credentials
     */
    private String getRefreshTokenKey(String accountId) {
        return accountId + "&" + salesforceSalesAttributes.getClientId() + salesforceSalesAttributes.getClientSecret();
    }

    /**
     * Validates and casts the given OAuth2 access token to a {@link SalesforceToken}.
     * If the provided token is an instance of {@code SalesforceToken}, it is returned.
     * Otherwise, a {@link MustAuthorizeException} is thrown to indicate that re-authorization is required.
     *
     * @param accessToken the OAuth2 access token to validate and cast
     * @return the {@link SalesforceToken} instance
     * @throws MustAuthorizeException if the token is not a {@code SalesforceToken}
     */
    private SalesforceToken getSalesforceTokenForUser(OAuth2AccessToken accessToken) {
        final SalesforceToken salesforceAccessToken;
        if (accessToken instanceof SalesforceToken) {
            salesforceAccessToken = (SalesforceToken) accessToken;
        } else if (accessToken.getAccessToken() != null) {
            salesforceAccessToken = new SalesforceToken(accessToken.getAccessToken(), salesforceSalesAttributes.getInstanceUrl(), accessToken.getRawResponse());
        } else {
            throw new IllegalArgumentException("Salesforce authentication succeeded but returned an invalid token format. " +
                    "This may indicate a configuration issue with your Salesforce connected app or an unexpected response format. " +
                    "Please verify your connected app settings and contact support if the issue persists.");
        }
        return salesforceAccessToken;
    }

    /**
     * Creates a MustAuthorizeException with appropriate details for the authorization flow.
     *
     * @param reAuthentication Whether this is a re-authentication request
     * @param accountId        The account ID requiring authentication
     * @return A MustAuthorizeException with appropriate details
     */
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

    /**
     * Determines whether OAuth-based authentication should be used based on the context and attributes.
     * <p>
     * OAuth is used when in admin context or when username/password credentials are not fully provided.
     *
     * @param isAdmin                   Whether the context is admin
     * @param salesforceSalesAttributes The Salesforce attributes
     * @return True if OAuth should be used, false if password grant is sufficient
     */
    public static boolean validateUsingOAuth(boolean isAdmin, SalesforceSalesAttributes
            salesforceSalesAttributes) {
        boolean useOAuth = isAdmin || (salesforceSalesAttributes.getUserName() == null || salesforceSalesAttributes.getUserName().isBlank() ||
                salesforceSalesAttributes.getPassword() == null || salesforceSalesAttributes.getPassword().isBlank() ||
                salesforceSalesAttributes.getSecurityToken() == null || salesforceSalesAttributes.getSecurityToken().isBlank());

        LOGGER.debug("Authentication flow selection - Admin context: {}, Use OAuth: {}, " +
                        "Has username: {}, Has password: {}, Has security token: {}, Instance URL: {}",
                isAdmin, useOAuth,
                salesforceSalesAttributes.getUserName() != null && !salesforceSalesAttributes.getUserName().isBlank(),
                salesforceSalesAttributes.getPassword() != null && !salesforceSalesAttributes.getPassword().isBlank(),
                salesforceSalesAttributes.getSecurityToken() != null && !salesforceSalesAttributes.getSecurityToken().isBlank(),
                salesforceSalesAttributes.getInstanceUrl() != null && !salesforceSalesAttributes.getInstanceUrl().isEmpty() ?
                        "provided" : "not provided");

        return useOAuth;
    }

}
