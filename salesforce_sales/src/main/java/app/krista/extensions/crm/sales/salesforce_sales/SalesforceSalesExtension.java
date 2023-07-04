package app.krista.extensions.crm.sales.salesforce_sales;

import app.krista.extension.authorization.RequestAuthenticator;
import app.krista.extension.executor.Invoker;
import app.krista.extension.impl.anno.Extension;
import app.krista.extension.impl.anno.Field;
import app.krista.extension.impl.anno.InvokerRequest;
import app.krista.extension.impl.anno.StaticResource;
import app.krista.extensions.crm.sales.salesforce_sales.impl.connectores.SalesforceTokenProviderFactory;
import app.krista.extensions.crm.sales.salesforce_sales.impl.stores.SalesforceSaleAttributeStore;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Map;

@Field.Text(value = SalesforceSalesAttributes.CLIENT_ID)
@Field.Text(value = SalesforceSalesAttributes.CLIENT_SECRET, isSecured = true)
@Field.Text(value = SalesforceSalesAttributes.USER_NAME, required = false)
@Field.Text(value = SalesforceSalesAttributes.PASSWORD, isSecured = true, required = false)
@Field.Text(value = SalesforceSalesAttributes.SECURITY_TOKEN, isSecured = true, required = false)
@Field.Boolean(value = SalesforceSalesAttributes.SANDBOX, required = false)
@Extension(version = "2.0.4-rc1")
@StaticResource(path = "docs", file = "docs")
public final class SalesforceSalesExtension {

    private final SalesforceTokenProviderFactory providerFactory;
    private final Invoker invoker;
    private final SalesforceSalesAttributes salesforceSalesAttributes;
    private final SalesforceSalesRequestAuthenticator requestAuthenticator;

    @Inject
    public SalesforceSalesExtension(SalesforceTokenProviderFactory providerFactory, Invoker invoker, SalesforceSalesAttributes salesforceSalesAttributes, SalesforceSaleAttributeStore attributeStore) {
        this(providerFactory, invoker, salesforceSalesAttributes, new SalesforceSalesRequestAuthenticator(salesforceSalesAttributes, attributeStore));
    }

    public SalesforceSalesExtension(SalesforceTokenProviderFactory providerFactory, Invoker invoker, SalesforceSalesAttributes salesforceSalesAttributes, SalesforceSalesRequestAuthenticator requestAuthenticator) {
        this.providerFactory = providerFactory;
        this.invoker = invoker;
        this.salesforceSalesAttributes = salesforceSalesAttributes;
        this.requestAuthenticator = requestAuthenticator;
    }

    @InvokerRequest(InvokerRequest.Type.AUTHENTICATOR)
    /**
     * This method is used for redirecting the MustAuthenticateException to RequestAuthenticator Interface.
     */
    public RequestAuthenticator getRequestAuthenticator() {
        return requestAuthenticator;
    }

    @InvokerRequest(InvokerRequest.Type.CUSTOM_TABS)
    public Map<String, String> customTabs() {
        return Map.of("Documentation", "static/docs");
    }

    @InvokerRequest(InvokerRequest.Type.VALIDATE_ATTRIBUTES)
    public void validateConnection(Map<String, Object> connectionAttributes) throws IOException {
        SalesforceSalesAttributes salesforceSalesAttributes = SalesforceSalesAttributes.create(invoker, connectionAttributes);
        validateSalesAttributes(salesforceSalesAttributes);
    }

    private void validateSalesAttributes(SalesforceSalesAttributes salesforceSalesAttributes) throws IOException {
        if (salesforceSalesAttributes.getUserName() == null || salesforceSalesAttributes.getUserName().isBlank() ||
                salesforceSalesAttributes.getPassword() == null || salesforceSalesAttributes.getPassword().isBlank() ||
                salesforceSalesAttributes.getSecurityToken() == null || salesforceSalesAttributes.getSecurityToken().isBlank()) {
            providerFactory.create(salesforceSalesAttributes).getSalesforceToken();
        } else {
            providerFactory.create(salesforceSalesAttributes).getSalesforceTokenForAdmin();
        }
    }

    @InvokerRequest(InvokerRequest.Type.TEST_CONNECTION)
    public void testConnection() throws IOException {
        validateSalesAttributes(salesforceSalesAttributes);
    }

    @InvokerRequest(InvokerRequest.Type.INVOKER_UPDATED)
    public void attributesUpdated(Map<String, Object> oldAttributes, Map<String, Object> newAttributes) {
        salesforceSalesAttributes.update(newAttributes);
    }
}
