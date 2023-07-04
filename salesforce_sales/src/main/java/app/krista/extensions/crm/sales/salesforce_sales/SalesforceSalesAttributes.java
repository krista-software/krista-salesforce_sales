package app.krista.extensions.crm.sales.salesforce_sales;

import app.krista.extension.executor.Invoker;
import app.krista.extension.request.RoutingInfo;
import app.krista.extension.request.protos.http.HttpProtocol;
import app.krista.extension.util.InvokerAttributes;
import com.github.scribejava.apis.SalesforceApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.util.Map;

@Service
public final class SalesforceSalesAttributes {

    public static final String CLIENT_ID = "clientId";
    public static final String CLIENT_SECRET = "clientSecret";
    public static final String USER_NAME = "username";
    public static final String PASSWORD = "password";
    public static final String SECURITY_TOKEN = "securityToken";
    public static final String SANDBOX = "sandbox";
    private final String routingUrl;
    private String clientId;
    private String clientSecret;
    private String userName;
    private String password;
    private String securityToken;
    private Boolean isSandbox;
    private OAuth20Service oAuthService;

    @Inject
    public SalesforceSalesAttributes(Invoker invoker) {
        this(invoker.getRoutingInfo().getRoutingURL(HttpProtocol.PROTOCOL_NAME, RoutingInfo.Type.APPLIANCE),
                invoker.getAttributes());
    }

    public SalesforceSalesAttributes(String routingUrl, Map<String, Object> attributes) {
        this((String) attributes.get(CLIENT_ID),
                (String) attributes.get(CLIENT_SECRET),
                (String) attributes.get(USER_NAME),
                (String) attributes.get(PASSWORD),
                (String) attributes.get(SECURITY_TOKEN),
                (Boolean) attributes.get(SANDBOX),
                routingUrl);
    }

    public SalesforceSalesAttributes(String clientId, String clientSecret, String userName, String password, String securityToken, Boolean isSandbox, String routingUrl) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.userName = userName;
        this.password = password;
        this.securityToken = securityToken;
        this.routingUrl = routingUrl;
        this.isSandbox = isSandbox;
    }

    public static SalesforceSalesAttributes create(Invoker invoker, Map<String, Object> attributes) {
        return new SalesforceSalesAttributes(invoker.getRoutingInfo().getRoutingURL(HttpProtocol.PROTOCOL_NAME, RoutingInfo.Type.APPLIANCE), attributes);
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public boolean isSandbox() {
        return isSandbox != null ? isSandbox : false;
    }

    public String getCallbackUrl() {
        return (routingUrl.contains("extension.local.eng.krista.app") ? routingUrl.replace("extension.local.eng.krista.app", "localhost:8765") : routingUrl) + "/rest/salesforcesales/callback";
    }

    public synchronized OAuth20Service getOauthService() {
        if (oAuthService == null) {
            oAuthService = new ServiceBuilder(getClientId())
                    .apiSecret(getClientSecret())
                    .callback(getCallbackUrl())
                    .build(isSandbox() ? SalesforceApi.sandbox() : SalesforceApi.instance());
        }
        return oAuthService;
    }
    public void update(Map<String, Object> newAttributes) {
        clientId = InvokerAttributes.getStringOrNull(newAttributes, CLIENT_ID);
        clientSecret = InvokerAttributes.getStringOrNull(newAttributes, CLIENT_SECRET);
        userName = InvokerAttributes.getStringOrNull(newAttributes, USER_NAME);
        password = InvokerAttributes.getStringOrNull(newAttributes, PASSWORD);
        securityToken = InvokerAttributes.getStringOrNull(newAttributes, SECURITY_TOKEN);
        isSandbox = InvokerAttributes.getBooleanOrNull(newAttributes, SANDBOX);
    }
}
