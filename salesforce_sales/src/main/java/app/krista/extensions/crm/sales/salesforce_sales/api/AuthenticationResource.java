package app.krista.extensions.crm.sales.salesforce_sales.api;

import app.krista.extensions.crm.sales.salesforce_sales.SalesforceSalesAttributes;
import app.krista.extensions.crm.sales.salesforce_sales.impl.connectores.SalesforceTokenProvider;
import app.krista.extensions.crm.sales.salesforce_sales.impl.connectores.SalesforceTokenProviderFactory;
import app.krista.extensions.crm.sales.salesforce_sales.impl.stores.RefreshTokenStore;
import app.krista.extensions.crm.sales.salesforce_sales.impl.stores.SalesforceSaleAttributeStore;
import app.krista.extensions.crm.sales.salesforce_sales.service.Lead;
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

@Path("/")
public final class AuthenticationResource {
    public static final String CODE = "code";
    public static final String STATE = "state";
    public static final String TEXT = "Text";
    public static final String DOUBLE = "Double";
    public static final String LONG = "Long";
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

            FreeForm freeForm = new FreeForm();
            freeForm.put("Id", TEXT, id);
            freeForm.put("Amount", TEXT, amount);
            freeForm.put("StageName", TEXT, stageName);
            freeForm.put("Name", TEXT, name);

            eventHandler.handleEvent("opportunityChanged", freeForm);
        }

        return Response.ok().build();
    }

    @POST
    @Path("/accountChanged")
    public Response handleAccountChangedEvent(String xml) throws Exception {
        JAXBElement<AccountNotifications> accountNotifications = XMlResponseParser.parseAccountXmlResponse(xml);
        if (accountNotifications != null) {
            AccountObject accountNotificationSObject = accountNotifications.getValue().accountNotification.accountObject;
            FreeForm freeForm = generateAccountFreeForm(accountNotificationSObject);
            eventHandler.handleEvent("Account Change Alert", freeForm);
        }
        return Response.ok().build();
    }

    private static FreeForm generateAccountFreeForm(AccountObject accountNotification) {

        FreeForm freeForm = new FreeForm();
        freeForm.put("Id", TEXT, accountNotification.Id);
        freeForm.put("Name", TEXT, accountNotification.Name);
        freeForm.put("Fax", TEXT, accountNotification.Fax);
        freeForm.put("AccountNumber", TEXT, accountNotification.AccountNumber);
        freeForm.put("Website", TEXT, accountNotification.Website);
        freeForm.put("Sic", TEXT, accountNotification.Sic);
        freeForm.put("OwnerId", TEXT, accountNotification.OwnerId);
        freeForm.put("CreatedById", TEXT, accountNotification.CreatedById);
        freeForm.put("CleanStatus", TEXT, accountNotification.CleanStatus);
        freeForm.put("SLASerialNumber__c", TEXT, accountNotification.SLASerialNumber__c);
        freeForm.put("CreatedDate", LONG, accountNotification.CreatedDate);
        freeForm.put("Type", TEXT, accountNotification.Type);
        freeForm.put("BillingStreet", TEXT, accountNotification.BillingStreet);
        freeForm.put("BillingCity", TEXT, accountNotification.BillingCity);
        freeForm.put("BillingState", TEXT, accountNotification.BillingState);
        freeForm.put("BillingPostalCode", TEXT, accountNotification.BillingPostalCode);
        freeForm.put("BillingCountry", TEXT, accountNotification.BillingCountry);
        freeForm.put("ShippingStreet", TEXT, accountNotification.ShippingStreet);
        freeForm.put("ShippingCity", TEXT, accountNotification.ShippingCity);
        freeForm.put("ShippingState", TEXT, accountNotification.ShippingState);
        freeForm.put("ShippingPostalCode", TEXT, accountNotification.ShippingPostalCode);
        freeForm.put("ShippingCountry", TEXT, accountNotification.ShippingCountry);
        freeForm.put("Phone", TEXT, accountNotification.Phone);
        freeForm.put("Industry", TEXT, accountNotification.Industry);
        freeForm.put("NumberOfEmployees", DOUBLE, accountNotification.NumberOfEmployees);
        freeForm.put("Ownership", TEXT, accountNotification.Ownership);
        freeForm.put("CustomerPriority__c", TEXT, accountNotification.CustomerPriority__c);
        freeForm.put("SLAExpirationDate__c", TEXT, accountNotification.SLAExpirationDate__c);
        freeForm.put("AnnualRevenue", DOUBLE, accountNotification.AnnualRevenue);
        return freeForm;
    }

    @POST
    @Path("/leadChanged")
    public Response handleLeadChangedEvent(String xml) throws Exception {
        System.out.println("XML" + xml);
        JAXBElement<LeadNotifications> leadNotifications = XMlResponseParser.parseLeadXmlResponse(xml);
        if (leadNotifications != null) {
            LeadObject leadNotificationSObject = leadNotifications.getValue().leadNotification.leadObject;
            FreeForm freeForm = generateLeadFreeForm(leadNotificationSObject);
            eventHandler.handleEvent("Lead Change Alert", freeForm);
        }
        return Response.ok().build();
    }

    private static FreeForm generateLeadFreeForm(LeadObject leadNotification) {
        FreeForm freeForm = new FreeForm();
        if (leadNotification.Name != null) {
            List<String> name = new ArrayList<>(Arrays.asList(leadNotification.Name.split("\\s+")));
            if (name.size() == 1)
                leadNotification.LastName = leadNotification.Name;
            else {
                leadNotification.FirstName = name.get(0);
                name.remove(0);
                leadNotification.LastName = String.join(" ", name);
            }
        }
        String leadName = leadNotification.FirstName == null ? leadNotification.LastName :
                leadNotification.FirstName + " " + leadNotification.LastName;
        System.out.println("leadName"+leadName);
        freeForm.put("Name", TEXT, leadName);
        freeForm.put("Id", TEXT, leadNotification.Id);
        freeForm.put("Title", TEXT, leadNotification.Title);
        freeForm.put("Company", TEXT, leadNotification.Company);
        freeForm.put("Website", TEXT, leadNotification.Website);
        freeForm.put("OwnerId", TEXT, leadNotification.OwnerId);
        freeForm.put("IsConverted", TEXT, leadNotification.IsConverted);
        freeForm.put("ConvertedAccountId", TEXT, leadNotification.ConvertedAccountId);
        freeForm.put("ConvertedContactId", TEXT, leadNotification.ConvertedContactId);
        freeForm.put("ConvertedOpportunityId", TEXT, leadNotification.ConvertedOpportunityId);
        freeForm.put("CleanStatus", TEXT, leadNotification.CleanStatus);
        freeForm.put("Phone", TEXT, leadNotification.Phone);
        freeForm.put("MobilePhone", TEXT, leadNotification.MobilePhone);
        freeForm.put("Email", TEXT, leadNotification.Email);
        freeForm.put("Description", TEXT, leadNotification.Description);
        freeForm.put("Status", TEXT, leadNotification.Status);
        freeForm.put("LeadSource", TEXT, leadNotification.LeadSource);
        freeForm.put("Industry", TEXT, leadNotification.Industry);
        freeForm.put("Rating", TEXT, leadNotification.Rating);
        freeForm.put("AnnualRevenue", DOUBLE, leadNotification.AnnualRevenue);
        freeForm.put("ConvertedDate", TEXT, leadNotification.ConvertedDate);
        freeForm.put("CreatedDate", LONG, leadNotification.CreatedDate);
        freeForm.put("LastModifiedDate", LONG, leadNotification.LastModifiedDate);
        return freeForm;
    }

}
