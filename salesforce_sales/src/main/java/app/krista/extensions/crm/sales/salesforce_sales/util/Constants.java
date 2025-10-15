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
    public static final String INVALID_TASK_ID = "The task ID provided is invalid. Please verify the task ID and try again.";
    public static final String DATA = "Data";
    public static final String SERVICES_DATA_V_48_0_SOBJECTS = "/services/data/v48.0/sobjects/";
    public static final String ENTITY_MAP_IS_NULL_OR_EMPTY = "No entity data was provided. Please ensure you provide the required entity information and try again.";
    public static final String PLEASE_PROVIDE_SUPPORTED_TIME_ZONE = "The time zone you provided is not supported. Please select a valid time zone from the supported list and try again.";
    public static final String CALLBACK_URI = "/rest/salesforcesales/callback";

    // Instance URL Validation Messages
    public static final String INSTANCE_URL_PROTOCOL_ERROR = "The Instance URL must use HTTP or HTTPS protocol. You provided: ";
    public static final String INSTANCE_URL_HOST_ERROR = "The Instance URL is missing a valid host name. Please provide a complete Salesforce instance URL (e.g., https://yourinstance.salesforce.com).";
    public static final String INSTANCE_URL_FORMAT_ERROR = "The Instance URL format is invalid: ";

    // OAuth Configuration Error Messages
    public static final String CLIENT_ID_REQUIRED = "Client ID is required for OAuth authentication. Please provide your Salesforce Connected App Client ID in the extension configuration.";
    public static final String CLIENT_SECRET_REQUIRED = "Client Secret is required for OAuth authentication. Please provide your Salesforce Connected App Client Secret in the extension configuration.";
    public static final String ROUTING_URL_REQUIRED = "Routing URL is required for OAuth authentication. Please configure the routing URL in your Krista settings to enable OAuth callback functionality.";
    public static final String CALLBACK_URL_REQUIRED = "Callback URL could not be generated. Please ensure the routing URL is properly configured in your Krista settings.";
    public static final String CALLBACK_URL_FORMAT_ERROR = "The callback URL must be a valid HTTP or HTTPS URL. Generated URL: ";
    public static final String DOMAIN_URL_REQUIRED = "Domain URL is required when using a custom Salesforce domain. Please provide your custom domain URL (e.g., https://yourdomain.my.salesforce.com).";

    // OAuth Service Creation Error Messages
    public static final String OAUTH_SERVICE_CREATION_ERROR = "Unable to establish OAuth connection with Salesforce. Please verify that your Client ID and Client Secret are correct, and that the callback URL is properly configured in your Salesforce Connected App settings.";
    public static final String OAUTH_SERVICE_CUSTOM_DOMAIN_ERROR = "Unable to establish OAuth connection with your custom Salesforce domain: ";
    public static final String OAUTH_SERVICE_VERIFY_CONFIG = ". Please verify that your Client ID, Client Secret, callback URL, and custom domain URL are all correctly configured.";

    // Domain Validation Messages
    public static final String DOMAIN_NULL_ERROR = "Domain URL cannot be null. Please provide your Salesforce domain URL.";
    public static final String DOMAIN_EMPTY_ERROR = "Domain URL cannot be empty. Please provide your Salesforce domain URL (e.g., https://yourdomain.my.salesforce.com).";
    public static final String DOMAIN_PROTOCOL_ERROR = "Domain URL must start with http:// or https://. Please provide a complete URL including the protocol.";
    public static final String DOMAIN_TOO_SHORT_ERROR = "The domain URL appears to be incomplete. Please provide a complete Salesforce domain URL (e.g., https://yourdomain.my.salesforce.com).";

    // OAuth 2.0 Endpoints
    public static final String OAUTH2_TOKEN_ENDPOINT = "/services/oauth2/token";
    public static final String OAUTH2_AUTHORIZE_ENDPOINT = "/services/oauth2/authorize";

    // Authentication Error Messages
    public static final String INVALID_CLIENT_ERROR = "invalid_client";
    public static final String INVALID_GRANT_ERROR = "invalid_grant";
    public static final String INVALID_CLIENT_MESSAGE = "Authentication failed due to invalid client credentials. Please verify that your Client ID and Client Secret are correct in the extension configuration.";
    public static final String INVALID_GRANT_MESSAGE = "Authentication failed due to invalid credentials. Please verify your Salesforce username, password, and security token. If you recently changed your password, you may need to reset your security token.";
    public static final String AUTHENTICATION_FAILED_PREFIX = "Authentication failed: ";
    public static final String EXPIRED_REFRESH_TOKEN = "expired access/refresh token";

    // JSON Error Keys
    public static final String JSON_ERROR_KEY = "error";

    private Constants() {
    }

}

