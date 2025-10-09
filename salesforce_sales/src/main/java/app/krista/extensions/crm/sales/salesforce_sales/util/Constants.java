package app.krista.extensions.crm.sales.salesforce_sales.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class Constants {
    public static final Gson GSON = new Gson();
    public static final Gson gsonBuilder = new GsonBuilder().serializeNulls().create();

    public static final String CREATED = "created";
    public static final String TEXT = "Text";
    public static final String DOUBLE = "Double";
    public static final String LONG = "Long";
    public static final String AND = " AND ";
    public static final String MEDIA_URI = "http://platform:9991";
    public static final String SFORCE_COM_2005_09_OUTBOUND = "http://soap.sforce.com/2005/09/outbound";
    public static final String ENTERPRISE_SOAP_SFORCE_COM = "urn:sobject.enterprise.soap.sforce.com";
    public static final String NAME = "Name";
    public static final String INVALID_TASK_ID = "Invalid task ID";
    public static final String DATA = "Data";
    public static final String SERVICES_DATA_V_48_0_SOBJECTS = "/services/data/v48.0/sobjects/";
    public static final String ENTITY_MAP_IS_NULL_OR_EMPTY = "Entity map is null or empty. ";
    public static final String PLEASE_PROVIDE_SUPPORTED_TIME_ZONE = "Please provide supported time zone.";
    public static final String CALLBACK_URI = "/rest/salesforcesales/callback";

    // Instance URL Validation Messages
    public static final String INSTANCE_URL_PROTOCOL_ERROR = "Instance URL must use HTTP or HTTPS protocol. Found: ";
    public static final String INSTANCE_URL_HOST_ERROR = "Instance URL must have a valid host";
    public static final String INSTANCE_URL_FORMAT_ERROR = "Invalid Instance URL format: ";

    // OAuth Configuration Error Messages
    public static final String CLIENT_ID_REQUIRED = "Client ID is required for OAuth authentication";
    public static final String CLIENT_SECRET_REQUIRED = "Client Secret is required for OAuth authentication";
    public static final String ROUTING_URL_REQUIRED = "Routing URL is required for OAuth authentication. Please configure the routing URL to generate callback URL.";
    public static final String CALLBACK_URL_REQUIRED = "Callback URL is required for OAuth authentication. Please ensure routing URL is configured.";
    public static final String CALLBACK_URL_FORMAT_ERROR = "Callback URL must be a valid HTTP or HTTPS URL: ";
    public static final String DOMAIN_URL_REQUIRED = "Domain URL cannot be null or empty for custom OAuth service";

    // OAuth Service Creation Error Messages
    public static final String OAUTH_SERVICE_CREATION_ERROR = "Failed to create OAuth service for standard Salesforce authentication. Please verify your Client ID, Client Secret, and callback configuration.";
    public static final String OAUTH_SERVICE_CUSTOM_DOMAIN_ERROR = "Failed to create OAuth service for custom domain: ";
    public static final String OAUTH_SERVICE_VERIFY_CONFIG = ". Please verify your Client ID, Client Secret, callback configuration, and domain URL format.";

    // Domain Validation Messages
    public static final String DOMAIN_NULL_ERROR = "Domain cannot be null";
    public static final String DOMAIN_EMPTY_ERROR = "Domain cannot be empty";
    public static final String DOMAIN_PROTOCOL_ERROR = "Domain must start with http:// or https://";
    public static final String DOMAIN_TOO_SHORT_ERROR = "Domain appears to be too short to be valid";

    // OAuth 2.0 Endpoints
    public static final String OAUTH2_TOKEN_ENDPOINT = "/services/oauth2/token";
    public static final String OAUTH2_AUTHORIZE_ENDPOINT = "/services/oauth2/authorize";

    // Authentication Error Messages
    public static final String INVALID_CLIENT_ERROR = "invalid_client";
    public static final String INVALID_GRANT_ERROR = "invalid_grant";
    public static final String INVALID_CLIENT_MESSAGE = "Authentication failed. Please check your Salesforce client credentials.";
    public static final String INVALID_GRANT_MESSAGE = "Authentication failed. Please check your Salesforce username, password, and security token.";
    public static final String AUTHENTICATION_FAILED_PREFIX = "Authentication failed: ";
    public static final String EXPIRED_REFRESH_TOKEN = "expired access/refresh token";

    // JSON Error Keys
    public static final String JSON_ERROR_KEY = "error";

    private Constants() {
    }

}

