package app.krista.extensions.crm.sales.salesforce_sales.api;

import app.krista.extensions.crm.sales.salesforce_sales.SalesforceSalesAttributes;
import app.krista.extensions.crm.sales.salesforce_sales.impl.connectores.SalesforceTokenProvider;
import app.krista.extensions.crm.sales.salesforce_sales.impl.connectores.SalesforceTokenProviderFactory;
import app.krista.extensions.crm.sales.salesforce_sales.impl.stores.RefreshTokenStore;
import app.krista.extensions.crm.sales.salesforce_sales.impl.stores.SalesforceSaleAttributeStore;
import app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser.Notifications;
import app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser.XMlResponseParser;
import app.krista.extensions.util.EventHandler;
import app.krista.ksdk.authentication.AuthorizationListener;
import app.krista.ksdk.context.AuthorizationContext;
import app.krista.model.base.FreeForm;
import com.github.scribejava.core.model.OAuth2AccessToken;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;
import java.util.Objects;

@Path("/")
public final class AuthenticationResource {
    public static final String CODE = "code";
    public static final String STATE = "state";
    private final RefreshTokenStore refreshTokenStore;
    private final EventHandler eventHandler;
    private final SalesforceSaleAttributeStore attributeStore;
    private final SalesforceTokenProviderFactory providerFactory;
    private final AuthorizationContext context;
    private final AuthorizationListener authorizationListener;

    @Inject
    public AuthenticationResource(RefreshTokenStore refreshTokenStore, EventHandler eventHandler, SalesforceSaleAttributeStore attributeStore, SalesforceTokenProviderFactory providerFactory, AuthorizationContext context, AuthorizationListener authorizationListener) {
        this.refreshTokenStore = refreshTokenStore;
        this.eventHandler = eventHandler;
        this.attributeStore = attributeStore;
        this.providerFactory = providerFactory;
        this.context = context;
        this.authorizationListener = authorizationListener;
    }

    @GET
    @Path("/callback")
    public String getCallBack(@QueryParam(CODE) String code, @QueryParam(STATE) String state) {
        Objects.requireNonNull(code);
        String[] parts = state.split("#");
        if (parts[0].isBlank() || parts.length > 2) {
            throw new BadRequestException("Invalid state parameters!");
        }
        String key = parts[0];
        String authContextId = parts.length == 2 ? parts[1] : null;
        try {
            Objects.requireNonNull(code);
            SalesforceTokenProvider tokenProvider;
            if (authContextId == null) {
                tokenProvider = providerFactory.create();
            } else if (authContextId.isBlank()) {
                throw new BadRequestException("Invalid state parameter!");
            } else {
                SalesforceSalesAttributes salesforceSalesAttributes = attributeStore.load(authContextId);
                tokenProvider = providerFactory.create(salesforceSalesAttributes);
            }
            OAuth2AccessToken accessToken = tokenProvider.getSalesforceSalesAttributes().getOauthService().getAccessToken(code);
            refreshTokenStore.put(key + "&" + tokenProvider.getSalesforceSalesAttributes().getClientId() + tokenProvider.getSalesforceSalesAttributes().getClientSecret(), accessToken.getRefreshToken());
            if (context.isAuthenticated()) {
                authorizationListener.authorized();
                return "User authenticated successfully. Please close the dialog.";
            }
            return "User authenticated successfully. Save the changes.";
        } catch (Exception cause) {
            System.err.println("Exception occurred due to :" + cause.getMessage());
            throw new IllegalStateException("Error occurred during authorization", cause.getCause());
        } finally {
            if (authContextId != null) {
                attributeStore.remove(authContextId);
            }
        }
    }

    @POST
    @Path("/opportunityChanged")
    public Response handleAmountChangedEvent(String xml) throws Exception {
        System.out.println("Opportunity Alert is called.." + xml);
        JAXBElement<Notifications> notifications = XMlResponseParser.parseXmlResponse(xml);
        if (notifications != null) {
            String id = notifications.getValue().getNotification().getSObject().Id;
            Double amount = notifications.getValue().getNotification().getSObject().Amount;
            String stageName = notifications.getValue().getNotification().getSObject().StageName;
            String name = notifications.getValue().getNotification().getSObject().Name;
            FreeForm freeForm = new FreeForm();
            freeForm.put("Id", "Text", id);
            freeForm.put("Amount", "Text", amount);
            freeForm.put("StageName", "Text", stageName);
            freeForm.put("Name", "Text", name);
            eventHandler.handleEvent("opportunityChanged", freeForm);
        }

        return Response.ok().build();
    }

}
