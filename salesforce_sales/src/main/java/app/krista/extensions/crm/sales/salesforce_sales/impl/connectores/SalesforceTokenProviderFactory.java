package app.krista.extensions.crm.sales.salesforce_sales.impl.connectores;

import app.krista.extensions.crm.sales.salesforce_sales.SalesforceSalesAttributes;
import app.krista.extensions.crm.sales.salesforce_sales.impl.stores.RefreshTokenStore;
import app.krista.extensions.crm.sales.salesforce_sales.impl.stores.SalesforceSaleAttributeStore;
import app.krista.ksdk.context.AuthorizationContext;
import app.krista.ksdk.context.RequestContext;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;

@Service
public final class SalesforceTokenProviderFactory {

    private final RequestContext requestContext;
    private final RefreshTokenStore refreshTokenStore;
    private final SalesforceTokenProvider defaultsalesforceTokenProvider;
    private final SalesforceSaleAttributeStore attributeStore;
    private final AuthorizationContext authorizationContext;

    @Inject
    public SalesforceTokenProviderFactory(RefreshTokenStore refreshTokenStore, RequestContext requestContext, SalesforceSalesAttributes salesforceSalesAttributes, SalesforceSaleAttributeStore attributeStore, AuthorizationContext authorizationContext) {
        this(refreshTokenStore, requestContext, authorizationContext,
                new SalesforceTokenProvider(refreshTokenStore, requestContext, salesforceSalesAttributes, authorizationContext, null), attributeStore);
    }

    public SalesforceTokenProviderFactory(RefreshTokenStore refreshTokenStore, RequestContext requestContext,
                                          AuthorizationContext authorizationContext, SalesforceTokenProvider salesforceTokenProvider,
                                          SalesforceSaleAttributeStore attributeStore) {
        this.refreshTokenStore = refreshTokenStore;
        this.requestContext = requestContext;
        this.authorizationContext = authorizationContext;
        this.defaultsalesforceTokenProvider = salesforceTokenProvider;
        this.attributeStore = attributeStore;
    }

    public SalesforceTokenProvider create(SalesforceSalesAttributes salesforceSalesAttributes) {
        String authContextId = attributeStore.save(salesforceSalesAttributes);
        return new SalesforceTokenProvider(refreshTokenStore, requestContext, salesforceSalesAttributes, authorizationContext, authContextId);
    }

    public SalesforceTokenProvider create() {
        return defaultsalesforceTokenProvider;
    }
}
