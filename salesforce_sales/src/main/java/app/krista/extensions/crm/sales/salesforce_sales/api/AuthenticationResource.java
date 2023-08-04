package app.krista.extensions.crm.sales.salesforce_sales.api;

import app.krista.extensions.crm.sales.salesforce_sales.SalesforceSalesAttributes;
import app.krista.extensions.crm.sales.salesforce_sales.impl.connectores.SalesforceTokenProvider;
import app.krista.extensions.crm.sales.salesforce_sales.impl.connectores.SalesforceTokenProviderFactory;
import app.krista.extensions.crm.sales.salesforce_sales.impl.stores.RefreshTokenStore;
import app.krista.extensions.crm.sales.salesforce_sales.impl.stores.SalesforceSaleAttributeStore;
import app.krista.extensions.crm.sales.salesforce_sales.service.Lead;
import app.krista.extensions.crm.sales.salesforce_sales.util.Constants;
import app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser.Notifications;
import app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser.XMlResponseParser;
import app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser.account_notification.AccountNotifications;
import app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser.account_notification.AccountObject;
import app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser.lead_notification.LeadNotifications;
import app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser.lead_notification.LeadObject;
import app.krista.extensions.crm.sales.salesforce_sales.util.xmlparser.sObject;
import app.krista.extensions.util.EventHandler;
import app.krista.ksdk.authentication.AuthorizationListener;
import app.krista.ksdk.context.AuthorizationContext;
import app.krista.model.base.FreeForm;
import com.github.scribejava.core.model.OAuth2AccessToken;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.*;

@Path("/")
public final class AuthenticationResource {

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
    public String getCallBack(@QueryParam(Constants.CODE) String code, @QueryParam(Constants.STATE) String state) {
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
                return "User authenticated successfully.";
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
        JAXBElement<Notifications> notifications = XMlResponseParser.parseXmlResponse(xml);
        if (notifications != null) {
            sObject notificationSObject = notifications.getValue().getNotification().getSObject();
            String id = notificationSObject.Id;
            Double amount = notificationSObject.Amount;
            String stageName = notificationSObject.StageName;
            String name = notificationSObject.Name;

            FreeForm freeForm = getOppurtunityFreeForm(id, amount, stageName, name);

            eventHandler.handleEvent("opportunityChanged", freeForm);
        }

        return Response.ok().build();
    }

    private static FreeForm getOppurtunityFreeForm(String id, Double amount, String stageName, String name) {
        FreeForm freeForm = new FreeForm();
        freeForm.put("Id", TEXT, id);
        freeForm.put("Amount", TEXT, amount);
        freeForm.put("StageName", TEXT, stageName);
        freeForm.put("Name", TEXT, name);
        return freeForm;
    }

    @POST
    @Path("/accountChanged")
    public Response handleAccountChangedEvent(String xml) throws Exception {
        JAXBElement<AccountNotifications> accountNotifications = XMlResponseParser.parseAccountXmlResponse(xml);
        if (accountNotifications != null) {
            AccountObject accountNotificationSObject = accountNotifications.getValue().accountNotification.accountObject;
            eventHandler.handleEvent("Account Change Alert", AccountObject.toAccountFreeForm(accountNotificationSObject));
        }
        return Response.ok().build();
    }



    @POST
    @Path("/leadChanged")
    public Response handleLeadChangedEvent(String xml) throws Exception {
        JAXBElement<LeadNotifications> leadNotifications = XMlResponseParser.parseLeadXmlResponse(xml);
        if (leadNotifications != null) {
            LeadObject leadNotificationSObject = leadNotifications.getValue().leadNotification.leadObject;
            eventHandler.handleEvent("Lead Change Alert", LeadObject.toLeadFreeForm(leadNotificationSObject));
        }
        return Response.ok().build();
    }



}
