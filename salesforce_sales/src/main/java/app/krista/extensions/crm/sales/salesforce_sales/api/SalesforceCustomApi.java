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

package app.krista.extensions.crm.sales.salesforce_sales.api;

import app.krista.extensions.crm.sales.salesforce_sales.util.Constants;
import com.github.scribejava.core.builder.api.DefaultApi20;

/**
 * Custom Salesforce OAuth 2.0 API implementation for Client Credentials flow.
 *
 * <p>This class extends {@link DefaultApi20} to provide OAuth 2.0 endpoints
 * for a specific Salesforce instance domain. It is used when the Instance URL
 * is provided in the configuration, enabling the OAuth 2.0 Client Credentials
 * flow for server-to-server authentication.</p>
 *
 * <p>The Client Credentials flow is suitable for scenarios where:</p>
 * <ul>
 *   <li>No user interaction is required</li>
 *   <li>The application acts on its own behalf</li>
 *   <li>Server-to-server communication is needed</li>
 *   <li>Background services need to access Salesforce APIs</li>
 * </ul>
 *
 * <p>This implementation constructs OAuth 2.0 endpoints by combining the
 * provided domain with standard Salesforce OAuth paths:</p>
 * <ul>
 *   <li>Token endpoint: {@code {domain}/services/oauth2/token}</li>
 *   <li>Authorization endpoint: {@code {domain}/services/oauth2/authorize}</li>
 * </ul>
 *
 * @author Krista Software
 * @version 2.0.15
 * @since 2.0.15
 * @see DefaultApi20
 * @see com.github.scribejava.core.oauth.OAuth20Service
 */
public class SalesforceCustomApi extends DefaultApi20 {

    /**
     * The Salesforce instance domain URL.
     * This should be a complete URL including protocol (e.g., "https://myorg.my.salesforce.com").
     */
    private final String domain;

    /**
     * Constructs a new SalesforceCustomApi with the specified domain.
     *
     * <p>The domain should be a complete Salesforce instance URL including
     * the protocol. For example:</p>
     * <ul>
     *   <li>{@code https://myorg.my.salesforce.com}</li>
     *   <li>{@code https://myorg--sandbox.my.salesforce.com}</li>
     *   <li>{@code https://custom.my.salesforce.com}</li>
     * </ul>
     *
     * @param domain the Salesforce instance domain URL
     * @throws IllegalArgumentException if domain is null, empty, or invalid
     */
    public SalesforceCustomApi(String domain) {
        this.domain = validateDomain(domain);
    }

    /**
     * Validates the domain parameter.
     *
     * @param domain the domain to validate
     * @return the validated domain
     * @throws IllegalArgumentException if domain is null, empty, or invalid
     */
    private String validateDomain(String domain) {
        if (domain == null) {
            throw new IllegalArgumentException(Constants.DOMAIN_NULL_ERROR);
        }

        String trimmedDomain = domain.trim();
        if (trimmedDomain.isEmpty()) {
            throw new IllegalArgumentException(Constants.DOMAIN_EMPTY_ERROR);
        }

        // Basic URL format validation
        if (!trimmedDomain.startsWith("http://") && !trimmedDomain.startsWith("https://")) {
            throw new IllegalArgumentException(Constants.DOMAIN_PROTOCOL_ERROR);
        }

        // Check for basic domain structure
        if (trimmedDomain.length() < 10) { // Minimum length for a valid URL like "http://a.b"
            throw new IllegalArgumentException(Constants.DOMAIN_TOO_SHORT_ERROR);
        }

        return trimmedDomain;
    }

    /**
     * Returns the OAuth 2.0 access token endpoint for this Salesforce instance.
     *
     * <p>This endpoint is used to exchange authorization codes for access tokens
     * in the Authorization Code flow, or to directly obtain access tokens in the
     * Client Credentials flow.</p>
     *
     * <p>The returned URL follows the format: {@code {domain}/services/oauth2/token}</p>
     *
     * @return the complete URL to the OAuth 2.0 token endpoint
     * @throws NullPointerException if the domain was null during construction
     */
    @Override
    public String getAccessTokenEndpoint() {
        return domain + Constants.OAUTH2_TOKEN_ENDPOINT;
    }

    /**
     * Returns the OAuth 2.0 authorization base URL for this Salesforce instance.
     *
     * <p>This endpoint is used to redirect users for authorization in the
     * Authorization Code flow. It is not typically used in the Client Credentials
     * flow, but is required by the {@link DefaultApi20} interface.</p>
     *
     * <p>The returned URL follows the format: {@code {domain}/services/oauth2/authorize}</p>
     *
     * @return the complete URL to the OAuth 2.0 authorization endpoint
     * @throws NullPointerException if the domain was null during construction
     */
    @Override
    protected String getAuthorizationBaseUrl() {
        return domain + Constants.OAUTH2_AUTHORIZE_ENDPOINT;
    }
}
