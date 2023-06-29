package app.krista.extensions.crm.sales.salesforce_sales.impl;

import app.krista.extensions.crm.sales.salesforce_sales.SalesforceSalesAttributes;
import app.krista.extensions.crm.sales.salesforce_sales.impl.connectores.SalesforceTokenProvider;
import app.krista.extensions.crm.sales.salesforce_sales.impl.connectores.SalesforceTokenProviderFactory;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFEmail;
import app.krista.extensions.crm.sales.salesforce_sales.util.Constants;
import com.github.scribejava.apis.salesforce.SalesforceToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Service
public final class SalesforceManager {
    private final SalesforceSalesAttributes salesAttributes;
    private final SalesforceTokenProvider tokenProvider;

    @Inject
    public SalesforceManager(SalesforceTokenProviderFactory providerFactory, SalesforceSalesAttributes salesAttributes) {
        this(providerFactory.create(), salesAttributes);
    }

    public SalesforceManager(SalesforceTokenProvider tokenProvider, SalesforceSalesAttributes salesAttributes) {
        this.tokenProvider = tokenProvider;
        this.salesAttributes = salesAttributes;
    }

    public Map<String, Object> getDifference(Object old, Object newObj) {
        try {
            Map<String, Object> values = new HashMap<>();
            for (Field field : old.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value1 = field.get(old);
                Object value2 = field.get(newObj);
                if (value1 != null && value2 != null && !Objects.equals(value1, value2) && !field.getName().contains("id")) {
                    values.put(field.getName(), value2);
                }
            }
            return values;
        } catch (IllegalAccessException cause) {
            throw new IllegalArgumentException(cause.getMessage());
        }
    }

    public String getRecordById(String id, String sobject) {
        try {
            SalesforceToken salesforceToken = tokenProvider.getSalesforceToken();
            String url = salesforceToken.getInstanceUrl() + "/services/data/v48.0/sobjects/" + sobject + "/" + id;
            final OAuthRequest request = getoAuthRequest(Verb.GET, null, salesforceToken, url);
            return getRequestResponse(salesforceToken, request);
        } catch (IOException cause) {
            throw new IllegalArgumentException("Failed to get record by id");
        }
    }

    public String updateRecord(String id, String body, String sobject) {
        try {
            SalesforceToken salesforceToken = tokenProvider.getSalesforceToken();
            String url = salesforceToken.getInstanceUrl() + "/services/data/v48.0/sobjects/" + sobject + "/" + id + "?_HttpMethod=PATCH";
            final OAuthRequest request = getoAuthRequest(Verb.POST, body, salesforceToken, url);
            return getRequestResponse(salesforceToken, request);
        } catch (IOException cause) {
            throw new IllegalArgumentException("Failed to update record " + cause.getCause());
        }
    }

    public String getRecordByQuery(String query) {
        try {
            SalesforceToken salesforceToken = tokenProvider.getSalesforceToken();
            String url = salesforceToken.getInstanceUrl() + "/services/data/v48.0/query?q=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
            final OAuthRequest request = getoAuthRequest(Verb.GET, null, salesforceToken, url);
            return getRequestResponse(salesforceToken, request);
        } catch (IOException cause) {
            throw new IllegalArgumentException(cause.getMessage(), cause);
        }
    }

    public String getMe() {
        try {
            SalesforceToken salesforceToken = tokenProvider.getSalesforceToken();
            String url = salesforceToken.getInstanceUrl() + "/services/data/v48.0/chatter/users/me";
            final OAuthRequest request = getoAuthRequest(Verb.GET, null, salesforceToken, url);
            return getRequestResponse(salesforceToken, request);
        } catch (IOException cause) {
            throw new IllegalArgumentException("Failed to get user details " + cause.getMessage());
        }
    }

    public void sendEmail(String recepients, String subj, String body) {
        try {
            SalesforceToken salesforceToken = tokenProvider.getSalesforceToken();
            String url = salesforceToken.getInstanceUrl() + "/services/data/v48.0/actions/standard/emailSimple";
            SFEmail.Body emailBody = new SFEmail.Body();
            emailBody.inputs.add(new SFEmail(recepients.trim(), subj.trim(), body.trim()));
            final OAuthRequest request = getoAuthRequest(Verb.POST, Constants.GSON.toJson(emailBody), salesforceToken, url);
            getRequestResponse(salesforceToken, request);
        } catch (IOException cause) {
            throw new IllegalArgumentException("Failed to get user details " + cause.getMessage());
        }
    }

    public String createRecord(String body, String sobject) {
        try {
            SalesforceToken salesforceToken = tokenProvider.getSalesforceToken();
            String url = salesforceToken.getInstanceUrl() + "/services/data/v48.0/sobjects/" + sobject + "/";
            final OAuthRequest request = getoAuthRequest(Verb.POST, body, salesforceToken, url);
            return getRequestResponse(salesforceToken, request);
        } catch (IOException cause) {
            throw new IllegalArgumentException("Failed to create record " + cause.getMessage());
        }
    }

    public String getObjects(String sObject) {
        try {
            SalesforceToken salesforceToken = tokenProvider.getSalesforceToken();
            String url = salesforceToken.getInstanceUrl() + "/services/data/v48.0/sobjects/" + sObject;
            final OAuthRequest request = getoAuthRequest(Verb.GET, null, salesforceToken, url);
            return getRequestResponse(salesforceToken, request);
        } catch (IOException cause) {
            throw new IllegalArgumentException(cause.getMessage());
        }
    }

    public void deleteRecordById(String id, String sobject) {
        try {
            SalesforceToken salesforceToken = tokenProvider.getSalesforceToken();
            String url = salesforceToken.getInstanceUrl() + "/services/data/v48.0/sobjects/" + sobject + "/" + id;
            final OAuthRequest request = getoAuthRequest(Verb.DELETE, null, salesforceToken, url);
            getRequestResponse(salesforceToken, request);
        } catch (IOException cause) {
            throw new IllegalArgumentException("Failed to delete asset " + cause.getMessage());
        }
    }

    private OAuthRequest getoAuthRequest(Verb verb, String body, SalesforceToken salesforceToken, String url) {
        final OAuthRequest request = new OAuthRequest(verb, url);
        if (body != null) {
            request.setPayload(body);
        }
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Authorization", "Bearer " + salesforceToken.getAccessToken());
        return request;
    }

    private String getRequestResponse(SalesforceToken salesforceToken, OAuthRequest request) {
        salesAttributes.getOauthService().signRequest(salesforceToken, request);
        try (com.github.scribejava.core.model.Response response = salesAttributes.getOauthService().execute(request)) {
            if (response.getCode() >= 400) {
                throw new IllegalArgumentException("Bad Request, Please enter correct required data. The error is " + response.getBody());
            }
            return response.getBody();
        } catch (IOException | InterruptedException | ExecutionException cause) {
            throw new IllegalArgumentException("Failed to get record " + cause.getMessage());
        }
    }

    public String getLatestNPostsByOpportunityId(String opportunityId, int numberOfPosts) {
        try {
            SalesforceToken salesforceToken = tokenProvider.getSalesforceToken();
            String url = salesforceToken.getInstanceUrl() + "/services/data/v48.0/chatter/feeds/record/" + opportunityId +
                    "/feed-elements?sortOrder=ASC&sortField=CreatedDate&pageSize=" + numberOfPosts;
            final OAuthRequest request = getoAuthRequest(Verb.GET, null, salesforceToken, url);
            return getRequestResponse(salesforceToken, request);
        } catch (IOException cause) {
            throw new IllegalArgumentException("Failed to get opportunity feed posts. " + cause. getMessage(), cause);
        }
    }
}
