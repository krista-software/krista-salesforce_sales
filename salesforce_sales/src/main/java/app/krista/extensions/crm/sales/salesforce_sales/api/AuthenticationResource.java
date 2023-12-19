package app.krista.extensions.crm.sales.salesforce_sales.api;

import app.krista.extensions.crm.sales.salesforce_sales.SalesforceSalesAttributes;
import app.krista.extensions.crm.sales.salesforce_sales.impl.connectores.SalesforceTokenProvider;
import app.krista.extensions.crm.sales.salesforce_sales.impl.connectores.SalesforceTokenProviderFactory;
import app.krista.extensions.crm.sales.salesforce_sales.impl.stores.RefreshTokenStore;
import app.krista.extensions.crm.sales.salesforce_sales.impl.stores.SalesforceSaleAttributeStore;
import app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser.XMlResponseParser;
import app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser.account_notification.AccountNotifications;
import app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser.lead_notification.LeadNotifications;
import app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser.opportunity_notification.OpportunityNotifications;
import app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser.task_notification.TaskNotifications;
import app.krista.extensions.util.EventHandler;
import app.krista.ksdk.authentication.AuthorizationListener;
import app.krista.ksdk.context.AuthorizationContext;
import app.krista.model.base.FreeForm;
import com.github.scribejava.core.model.OAuth2AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static com.github.scribejava.core.model.OAuthConstants.CODE;
import static com.github.scribejava.core.model.OAuthConstants.STATE;

@Path("/")
public final class AuthenticationResource {

    public static final String INVALID_STATE_PARAMETERS = "Invalid state parameters!";
    private final RefreshTokenStore refreshTokenStore;
    private final EventHandler eventHandler;
    private final SalesforceSaleAttributeStore attributeStore;
    private final SalesforceTokenProviderFactory providerFactory;
    private final AuthorizationContext context;
    private final AuthorizationListener authorizationListener;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationResource.class);


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
            throw new BadRequestException(INVALID_STATE_PARAMETERS);
        }
        String key = parts[0];
        String authContextId = parts.length == 2 ? parts[1] : null;
        try {
            Objects.requireNonNull(code);
            SalesforceTokenProvider tokenProvider = getSalesforceTokenProvider(authContextId);
            OAuth2AccessToken accessToken = tokenProvider.getSalesforceSalesAttributes().getOauthService().getAccessToken(code);
            refreshTokenStore.put(key + "&" + tokenProvider.getSalesforceSalesAttributes().getClientId() +
                    tokenProvider.getSalesforceSalesAttributes().getClientSecret(), accessToken.getRefreshToken());
            if (context.isAuthenticated()) {
                authorizationListener.authorized();
                return "User authenticated successfully.";
            }
            return "User authenticated successfully. Save the changes.";
        } catch (IOException | ExecutionException cause) {
            LOGGER.error("Exception occurred due to :" + cause.getMessage(), cause);

            throw new IllegalStateException("Error occurred during authorization", cause.getCause());
        } catch (InterruptedException cause) {
            Thread.currentThread().interrupt();
            LOGGER.error("Exception occurred due to :" + cause.getMessage(), cause);
            throw new IllegalStateException("Error occurred during authorization", cause.getCause());
        } finally {
            if (authContextId != null) {
                attributeStore.remove(authContextId);
            }
        }
    }

    private SalesforceTokenProvider getSalesforceTokenProvider(String authContextId) {
        SalesforceTokenProvider tokenProvider;
        if (authContextId == null) {
            tokenProvider = providerFactory.create();
        } else if (authContextId.isBlank()) {
            throw new BadRequestException(INVALID_STATE_PARAMETERS);
        } else {
            SalesforceSalesAttributes salesforceSalesAttributes = attributeStore.load(authContextId);
            tokenProvider = providerFactory.create(salesforceSalesAttributes);
        }
        return tokenProvider;
    }

    @POST
    @Path("/opportunityChanged")
    public Response handleAmountChangedEvent(String xml) {
        JAXBElement<OpportunityNotifications> notifications = XMlResponseParser.parseOpportunityXmlResponse(xml);
        if (notifications != null) {
            FreeForm notificationSObject = notifications.getValue().getNotification().getSObject()
                    .toOpportunityFreeForm();
            eventHandler.handleEvent("opportunityChanged", notificationSObject);
        }
        return Response.ok().build();
    }

    @POST
    @Path("/accountchanged")
    public Response handleAccountChangedEvent(String xml) {
        JAXBElement<AccountNotifications> accountNotifications = XMlResponseParser.parseAccountXmlResponse(xml);
        if (accountNotifications != null) {
            FreeForm accountNotificationSObject = accountNotifications.getValue().getAccountNotification().getSalesforceXmlAccount()
                    .toAccountFreeForm();

            eventHandler.handleEvent("Account Change Alert", accountNotificationSObject);
        }
        return Response.ok().build();
    }

    @POST
    @Path("/leadchanged")
    public Response handleLeadChangedEvent(String xml) {
        JAXBElement<LeadNotifications> leadNotifications = XMlResponseParser.parseLeadXmlResponse(xml);
        if (leadNotifications != null) {
            FreeForm leadNotificationSObject = leadNotifications.getValue().getLeadNotification().getSalesforceXmlLead()
                    .toLeadFreeForm();
            eventHandler.handleEvent("Lead Change Alert", leadNotificationSObject);
        }
        return Response.ok().build();
    }

    @POST
    @Path("/taskchanged")
    public Response handleTaskChangedEvent(String xml) {
        JAXBElement<TaskNotifications> taskNotifications = XMlResponseParser.parseTaskXmlResponse(xml);
        if (taskNotifications != null) {
            FreeForm taskNotificationSObject = taskNotifications.getValue().getTaskNotification().getSalesforceXmlTask()
                    .toTaskFreeForm();
            eventHandler.handleEvent("Task Change Alert", taskNotificationSObject);
        }
        return Response.ok().build();
    }

}
