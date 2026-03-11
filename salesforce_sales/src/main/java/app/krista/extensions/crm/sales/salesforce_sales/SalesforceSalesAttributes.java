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

import app.krista.extension.executor.Invoker;
import app.krista.extension.request.RoutingInfo;
import app.krista.extension.request.protos.http.HttpProtocol;
import app.krista.extension.util.InvokerAttributes;
import app.krista.extensions.crm.sales.salesforce_sales.api.SalesforceCustomApi;
import app.krista.extensions.crm.sales.salesforce_sales.util.Constants;
import com.github.scribejava.apis.SalesforceApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Holds Salesforce configuration attributes used for OAuth and API integration.
 * Provides methods to initialize, access, and update Salesforce connection parameters.
 */
@Service
public final class SalesforceSalesAttributes {

    public static final String CLIENT_ID = "clientId";
    public static final String CLIENT_SECRET = "clientSecret";
    public static final String INSTANCE_URL = "Instance URL";
    public static final String USER_NAME = "username";
    public static final String PASS_WORD = "password";
    public static final String SECURITY_TOKEN = "securityToken";
    public static final String SANDBOX = "sandbox";
    public static final String TIME_ZONE = "timezone";

    private final String routingUrl;
    private String timezone;
    private String clientId;
    private String clientSecret;
    private String instanceUrl;
    private String userName;
    private String password;
    private String securityToken;
    private Boolean isSandbox;
    private OAuth20Service oAuthService;

    /**
     * Constructs the attributes using an {@link Invoker}, extracting routing and attribute information.
     *
     * @param invoker the Krista extension {@link Invoker}
     */
    @Inject
    public SalesforceSalesAttributes(Invoker invoker) {
        this(invoker.getRoutingInfo().getRoutingURL(HttpProtocol.PROTOCOL_NAME, RoutingInfo.Type.APPLIANCE),
                invoker.getAttributes());
    }

    /**
     * Constructs the attributes using a routing URL and a map of attribute values.
     *
     * @param routingUrl the callback routing URL
     * @param attributes the map of attributes (clientId, clientSecret, username, etc.)
     */
    public SalesforceSalesAttributes(String routingUrl, Map<String, Object> attributes) {
        this((String) attributes.get(CLIENT_ID),
                (String) attributes.get(CLIENT_SECRET),
                (String) attributes.get(INSTANCE_URL),
                (String) attributes.get(USER_NAME),
                (String) attributes.get(PASS_WORD),
                (String) attributes.get(SECURITY_TOKEN),
                (Boolean) attributes.get(SANDBOX),
                routingUrl, (String) attributes.get(TIME_ZONE));
    }

    /**
     * Constructs the attributes using individual configuration values.
     *
     * @param clientId      Salesforce client ID
     * @param clientSecret  Salesforce client secret
     * @param userName      Salesforce username
     * @param password      Salesforce password
     * @param securityToken Salesforce security token
     * @param isSandbox     whether to use the sandbox environment
     * @param routingUrl    the callback routing URL
     */
    public SalesforceSalesAttributes(String clientId, String clientSecret, String instanceUrl,
                                     String userName, String password, String securityToken, Boolean isSandbox,
                                     String routingUrl,
                                     String timeZone) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.instanceUrl = validateInstanceUrl(instanceUrl);
        this.userName = userName;
        this.password = password;
        this.securityToken = securityToken;
        this.routingUrl = routingUrl;
        this.isSandbox = isSandbox;
        this.timezone = timeZone;
    }

    /**
     * Creates an instance using an {@link Invoker} and attributes map.
     *
     * @param invoker    the Krista extension {@link Invoker}
     * @param attributes the attributes map
     * @return a new {@link SalesforceSalesExtension} instance
     */
    public static SalesforceSalesAttributes create(Invoker invoker, Map<String, Object> attributes) {
        return new SalesforceSalesAttributes(invoker.getRoutingInfo().getRoutingURL(
                HttpProtocol.PROTOCOL_NAME, RoutingInfo.Type.APPLIANCE), attributes);
    }

    /**
     * @return the Salesforce client ID
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * @return the Salesforce client secret
     */
    public String getClientSecret() {
        return clientSecret;
    }

    /**
     * @return the Salesforce instance URL
     */
    public String getInstanceUrl() {
        return instanceUrl;
    }

    /**
     * @return the Salesforce username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @return the Salesforce password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return the Salesforce security token
     */
    public String getSecurityToken() {
        return securityToken;
    }

    /**
     * @return {@code true} if using sandbox, {@code false} otherwise
     */
    public boolean isSandbox() {
        return isSandbox != null ? isSandbox : false;
    }

    public String getTimezone() {
        return timezone;
    }

    /**
     * Lazily initializes and returns the {@link OAuth20Service} for authentication.
     *
     * <p>This method creates an OAuth service for standard Salesforce authentication
     * using either production or sandbox endpoints based on the {@link #isSandbox()} setting.</p>
     *
     * @return the configured {@link OAuth20Service}
     * @throws IllegalStateException if the OAuth service cannot be created due to invalid configuration
     * @throws IllegalArgumentException if required configuration parameters are missing or invalid
     */
    public synchronized OAuth20Service getOauthService() {
        if (oAuthService == null) {
            try {
                validateOAuthConfiguration();

                oAuthService = new ServiceBuilder(getClientId())
                        .apiSecret(getClientSecret())
                        .callback(getCallbackUrl())
                        .build(isSandbox() ? SalesforceApi.sandbox() : SalesforceApi.instance());

            } catch (Exception e) {
                throw new IllegalStateException(Constants.OAUTH_SERVICE_CREATION_ERROR, e);
            }
        }
        return oAuthService;
    }

    public String getCallbackUrl() {
        String callBackUrl = this.routingUrl.contains("extension.local.eng.krista.app")
                ? this.routingUrl.replace("extension.local.eng.krista.app", "localhost:8765") : this.routingUrl;
        return (callBackUrl.contains("localhost:8765") ? callBackUrl.replace("https", "http") : callBackUrl) + "/rest/salesforcesales/callback";
    }

    /**
     * Lazily initializes and returns the {@link OAuth20Service} for authentication with a custom domain.
     *
     * <p>This method creates an OAuth service for Client Credentials flow authentication
     * using a custom Salesforce instance domain URL.</p>
     *
     * @param domainUrl the custom Salesforce instance domain URL
     * @return the configured {@link OAuth20Service}
     * @throws IllegalStateException if the OAuth service cannot be created due to invalid configuration
     * @throws IllegalArgumentException if the domain URL or other required parameters are invalid
     */
    public synchronized OAuth20Service getOauthService(String domainUrl) {
        if (oAuthService == null) {
            try {
                validateOAuthConfiguration();

                if (domainUrl == null || domainUrl.trim().isEmpty()) {
                    throw new IllegalArgumentException(Constants.DOMAIN_URL_REQUIRED);
                }

                oAuthService = new ServiceBuilder(getClientId())
                        .apiSecret(getClientSecret())
                        .callback(getCallbackUrl())
                        .build(new SalesforceCustomApi(domainUrl));

            } catch (IllegalArgumentException e) {
                // Re-throw validation errors as-is
                throw e;
            } catch (Exception e) {
                throw new IllegalStateException(Constants.OAUTH_SERVICE_CUSTOM_DOMAIN_ERROR + domainUrl +
                        Constants.OAUTH_SERVICE_VERIFY_CONFIG, e);
            }
        }
        return oAuthService;
    }

    /**
     * Updates the current configuration with new attribute values.
     *
     * @param newAttributes the updated attributes map
     */
    public void update(Map<String, Object> newAttributes) {
        clientId = InvokerAttributes.getStringOrNull(newAttributes, CLIENT_ID);
        clientSecret = InvokerAttributes.getStringOrNull(newAttributes, CLIENT_SECRET);
        instanceUrl = validateInstanceUrl(InvokerAttributes.getStringOrNull(newAttributes, INSTANCE_URL));
        userName = InvokerAttributes.getStringOrNull(newAttributes, USER_NAME);
        password = InvokerAttributes.getStringOrNull(newAttributes, PASS_WORD);
        securityToken = InvokerAttributes.getStringOrNull(newAttributes, SECURITY_TOKEN);
        isSandbox = InvokerAttributes.getBooleanOrNull(newAttributes, SANDBOX);
        timezone = InvokerAttributes.getStringOrNull(newAttributes, TIME_ZONE);

    }

    /**
     * Validates the Instance URL format.
     *
     * @param instanceUrl the Instance URL to validate
     * @return the validated Instance URL, or null if input is null/empty
     * @throws IllegalArgumentException if the Instance URL format is invalid
     */
    private String validateInstanceUrl(String instanceUrl) {
        if (instanceUrl == null || instanceUrl.trim().isEmpty()) {
            return instanceUrl; // Allow null or empty values
        }

        try {
            URL url = new URL(instanceUrl.trim());

            // Validate protocol
            String protocol = url.getProtocol();
            if (!"https".equals(protocol) && !"http".equals(protocol)) {
                throw new IllegalArgumentException(Constants.INSTANCE_URL_PROTOCOL_ERROR + protocol);
            }

            // Validate host
            String host = url.getHost();
            if (host == null || host.trim().isEmpty()) {
                throw new IllegalArgumentException(Constants.INSTANCE_URL_HOST_ERROR);
            }

            // Validate that it looks like a Salesforce domain (basic check)
            if (!host.contains("salesforce.com") && !host.contains("force.com") &&
                    !host.contains("my.salesforce.com") && !host.contains("lightning.force.com")) {
                // Allow any domain for flexibility, but log a warning for non-Salesforce domains
                // This is a soft validation - we don't throw an exception
            }

            return instanceUrl.trim();

        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(Constants.INSTANCE_URL_FORMAT_ERROR + instanceUrl + ". " + e.getMessage(), e);
        }
    }

    /**
     * Validates the OAuth configuration to ensure all required parameters are present and valid.
     *
     * @throws IllegalArgumentException if any required OAuth configuration is missing or invalid
     */
    private void validateOAuthConfiguration() {
        if (clientId == null || clientId.trim().isEmpty()) {
            throw new IllegalArgumentException(Constants.CLIENT_ID_REQUIRED);
        }

        if (clientSecret == null || clientSecret.trim().isEmpty()) {
            throw new IllegalArgumentException(Constants.CLIENT_SECRET_REQUIRED);
        }

        // Validate routing URL first (required for callback URL generation)
        if (routingUrl == null || routingUrl.trim().isEmpty()) {
            throw new IllegalArgumentException(Constants.ROUTING_URL_REQUIRED);
        }

        // Validate callback URL
        String callbackUrl = getCallbackUrl();
        if (callbackUrl == null || callbackUrl.trim().isEmpty()) {
            throw new IllegalArgumentException(Constants.CALLBACK_URL_REQUIRED);
        }

        // Basic callback URL format validation
        if (!callbackUrl.startsWith("http://") && !callbackUrl.startsWith("https://")) {
            throw new IllegalArgumentException(Constants.CALLBACK_URL_FORMAT_ERROR + callbackUrl);
        }
    }

    /**
     * Validates the configuration for a specific authentication flow.
     *
     * @param flowType the authentication flow type to validate for
     * @return ValidationResult containing validation status and any error messages
     */
    public ValidationResult validateConfiguration(AuthenticationFlowType flowType) {
        ValidationResult result = new ValidationResult();

        try {
            switch (flowType) {
                case CLIENT_CREDENTIALS:
                    validateClientCredentialsFlow(result);
                    break;
                case AUTHORIZATION_CODE:
                    validateAuthorizationCodeFlow(result);
                    break;
                case PASSWORD_GRANT:
                    validatePasswordGrantFlow(result);
                    break;
                case AUTO_DETECT:
                    validateAutoDetectFlow(result);
                    break;
                default:
                    result.addError("Unknown authentication flow type: " + flowType);
            }
        } catch (Exception e) {
            result.addError("Validation failed: " + e.getMessage());
        }

        return result;
    }

    /**
     * Validates configuration for OAuth 2.0 Client Credentials flow.
     * Requires: Client ID, Client Secret, Instance URL, Routing URL
     */
    private void validateClientCredentialsFlow(ValidationResult result) {
        // Basic OAuth requirements
        if (clientId == null || clientId.trim().isEmpty()) {
            result.addError(Constants.CLIENT_ID_REQUIRED);
        }

        if (clientSecret == null || clientSecret.trim().isEmpty()) {
            result.addError(Constants.CLIENT_SECRET_REQUIRED);
        }

        if (routingUrl == null || routingUrl.trim().isEmpty()) {
            result.addError(Constants.ROUTING_URL_REQUIRED);
        }

        // Instance URL is required for Client Credentials flow
        if (instanceUrl == null || instanceUrl.trim().isEmpty()) {
            result.addError("Instance URL is required for Client Credentials flow");
        } else {
            try {
                validateInstanceUrl(instanceUrl);
            } catch (IllegalArgumentException e) {
                result.addError("Instance URL validation failed: " + e.getMessage());
            }
        }

        // Validate callback URL if routing URL is present
        if (routingUrl != null && !routingUrl.trim().isEmpty()) {
            try {
                String callbackUrl = getCallbackUrl();
                if (!callbackUrl.startsWith("http://") && !callbackUrl.startsWith("https://")) {
                    result.addError(Constants.CALLBACK_URL_FORMAT_ERROR + callbackUrl);
                }
            } catch (Exception e) {
                result.addError("Callback URL generation failed: " + e.getMessage());
            }
        }

        // Username/password should not be required for Client Credentials
        if ((userName != null && !userName.trim().isEmpty()) ||
                (password != null && !password.trim().isEmpty()) ||
                (securityToken != null && !securityToken.trim().isEmpty())) {
            result.addWarning("Username/password credentials are not needed for Client Credentials flow and will be ignored");
        }
    }

    /**
     * Validates configuration for OAuth 2.0 Authorization Code flow.
     * Requires: Client ID, Client Secret, Routing URL (no Instance URL, no username/password)
     */
    private void validateAuthorizationCodeFlow(ValidationResult result) {
        // Basic OAuth requirements
        if (clientId == null || clientId.trim().isEmpty()) {
            result.addError(Constants.CLIENT_ID_REQUIRED);
        }

        if (clientSecret == null || clientSecret.trim().isEmpty()) {
            result.addError(Constants.CLIENT_SECRET_REQUIRED);
        }

        if (routingUrl == null || routingUrl.trim().isEmpty()) {
            result.addError(Constants.ROUTING_URL_REQUIRED);
        }

        // Instance URL should be empty for Authorization Code flow
        if (instanceUrl != null && !instanceUrl.trim().isEmpty()) {
            result.addWarning("Instance URL is not needed for Authorization Code flow and will be ignored");
        }

        // Username/password should not be provided for Authorization Code flow
        if ((userName != null && !userName.trim().isEmpty()) ||
                (password != null && !password.trim().isEmpty()) ||
                (securityToken != null && !securityToken.trim().isEmpty())) {
            result.addWarning("Username/password credentials are not needed for Authorization Code flow and will be ignored");
        }

        // Validate callback URL
        if (routingUrl != null && !routingUrl.trim().isEmpty()) {
            try {
                String callbackUrl = getCallbackUrl();
                if (!callbackUrl.startsWith("http://") && !callbackUrl.startsWith("https://")) {
                    result.addError(Constants.CALLBACK_URL_FORMAT_ERROR + callbackUrl);
                }
            } catch (Exception e) {
                result.addError("Callback URL generation failed: " + e.getMessage());
            }
        }
    }

    /**
     * Validates configuration for OAuth 2.0 Password Grant flow.
     * Requires: Client ID, Client Secret, Username, Password, Security Token, Routing URL
     */
    private void validatePasswordGrantFlow(ValidationResult result) {
        // Basic OAuth requirements
        if (clientId == null || clientId.trim().isEmpty()) {
            result.addError(Constants.CLIENT_ID_REQUIRED);
        }

        if (clientSecret == null || clientSecret.trim().isEmpty()) {
            result.addError(Constants.CLIENT_SECRET_REQUIRED);
        }

        if (routingUrl == null || routingUrl.trim().isEmpty()) {
            result.addError(Constants.ROUTING_URL_REQUIRED);
        }

        // Username/password credentials are required for Password Grant flow
        if (userName == null || userName.trim().isEmpty()) {
            result.addError("Username is required for Password Grant flow");
        }

        if (password == null || password.trim().isEmpty()) {
            result.addError("Password is required for Password Grant flow");
        }

        if (securityToken == null || securityToken.trim().isEmpty()) {
            result.addError("Security Token is required for Password Grant flow");
        }

        // Instance URL should be empty for Password Grant flow (uses standard Salesforce endpoints)
        if (instanceUrl != null && !instanceUrl.trim().isEmpty()) {
            result.addWarning("Instance URL is not needed for Password Grant flow and will be ignored");
        }

        // Validate callback URL
        if (routingUrl != null && !routingUrl.trim().isEmpty()) {
            try {
                String callbackUrl = getCallbackUrl();
                if (!callbackUrl.startsWith("http://") && !callbackUrl.startsWith("https://")) {
                    result.addError(Constants.CALLBACK_URL_FORMAT_ERROR + callbackUrl);
                }
            } catch (Exception e) {
                result.addError("Callback URL generation failed: " + e.getMessage());
            }
        }
    }

    /**
     * Validates configuration for auto-detection of authentication flow.
     * Checks which flows are possible with the current configuration.
     */
    private void validateAutoDetectFlow(ValidationResult result) {
        boolean canUseClientCredentials = true;
        boolean canUseAuthorizationCode = true;
        boolean canUsePasswordGrant = true;

        // Check basic OAuth requirements (needed for all flows)
        if (clientId == null || clientId.trim().isEmpty()) {
            result.addError(Constants.CLIENT_ID_REQUIRED);
            canUseClientCredentials = canUseAuthorizationCode = canUsePasswordGrant = false;
        }

        if (clientSecret == null || clientSecret.trim().isEmpty()) {
            result.addError(Constants.CLIENT_SECRET_REQUIRED);
            canUseClientCredentials = canUseAuthorizationCode = canUsePasswordGrant = false;
        }

        if (routingUrl == null || routingUrl.trim().isEmpty()) {
            result.addError(Constants.ROUTING_URL_REQUIRED);
            canUseClientCredentials = canUseAuthorizationCode = canUsePasswordGrant = false;
        }

        // Check Client Credentials flow requirements
        if (instanceUrl == null || instanceUrl.trim().isEmpty()) {
            canUseClientCredentials = false;
        } else {
            try {
                validateInstanceUrl(instanceUrl);
            } catch (IllegalArgumentException e) {
                canUseClientCredentials = false;
                result.addWarning("Client Credentials flow not available: " + e.getMessage());
            }
        }

        // Check Password Grant flow requirements
        if (userName == null || userName.trim().isEmpty() ||
                password == null || password.trim().isEmpty() ||
                securityToken == null || securityToken.trim().isEmpty()) {
            canUsePasswordGrant = false;
        }

        // Report available flows
        if (canUseClientCredentials) {
            result.addInfo("Client Credentials flow is available (Instance URL provided)");
        }
        if (canUseAuthorizationCode) {
            result.addInfo("Authorization Code flow is available");
        }
        if (canUsePasswordGrant) {
            result.addInfo("Password Grant flow is available (username/password provided)");
        }

        // Determine which flow will be selected
        if (canUseClientCredentials) {
            result.addInfo("Auto-detection will select: Client Credentials flow");
        } else if (canUseAuthorizationCode) {
            result.addInfo("Auto-detection will select: Authorization Code flow");
        } else if (canUsePasswordGrant) {
            result.addInfo("Auto-detection will select: Password Grant flow");
        } else {
            result.addError("No authentication flow is available with current configuration");
        }
    }

    /**
     * Enumeration of supported authentication flow types.
     */
    public enum AuthenticationFlowType {
        CLIENT_CREDENTIALS,
        AUTHORIZATION_CODE,
        PASSWORD_GRANT,
        AUTO_DETECT
    }

    /**
     * Result of configuration validation containing errors, warnings, and info messages.
     */
    public static class ValidationResult {
        private final java.util.List<String> errors = new java.util.ArrayList<>();
        private final java.util.List<String> warnings = new java.util.ArrayList<>();
        private final java.util.List<String> info = new java.util.ArrayList<>();

        public void addError(String error) {
            errors.add(error);
        }

        public void addWarning(String warning) {
            warnings.add(warning);
        }

        public void addInfo(String info) {
            this.info.add(info);
        }

        public boolean isValid() {
            return errors.isEmpty();
        }

        public java.util.List<String> getErrors() {
            return new java.util.ArrayList<>(errors);
        }

        public java.util.List<String> getWarnings() {
            return new java.util.ArrayList<>(warnings);
        }

        public java.util.List<String> getInfo() {
            return new java.util.ArrayList<>(info);
        }

        public boolean hasErrors() {
            return !errors.isEmpty();
        }

        public boolean hasWarnings() {
            return !warnings.isEmpty();
        }

        public boolean hasInfo() {
            return !info.isEmpty();
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("ValidationResult{");
            sb.append("valid=").append(isValid());
            if (!errors.isEmpty()) {
                sb.append(", errors=").append(errors);
            }
            if (!warnings.isEmpty()) {
                sb.append(", warnings=").append(warnings);
            }
            if (!info.isEmpty()) {
                sb.append(", info=").append(info);
            }
            sb.append('}');
            return sb.toString();
        }
    }
}
