/*
 * Salesforce Sales Extension for Krista
 * Copyright (C) 2025 Krista Software
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package app.krista.extensions.crm.sales.salesforce_sales;

import app.krista.extension.authorization.RequestAuthenticator;
import app.krista.extension.executor.Invoker;
import app.krista.extension.impl.anno.*;
import app.krista.extensions.crm.sales.salesforce_sales.impl.connectores.SalesforceTokenProviderFactory;
import app.krista.extensions.crm.sales.salesforce_sales.impl.stores.SalesforceSaleAttributeStore;
import app.krista.extensions.crm.sales.salesforce_sales.util.Validators;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Map;

@Field.Text(value = SalesforceSalesAttributes.CLIENT_ID)
@Field.Text(value = SalesforceSalesAttributes.CLIENT_SECRET, isSecured = true)
@Field.Text(value = SalesforceSalesAttributes.INSTANCE_URL, required = false)
@Field.Text(value = SalesforceSalesAttributes.USER_NAME, required = false)
@Field.Text(value = SalesforceSalesAttributes.PASS_WORD, isSecured = true, required = false)
@Field.Text(value = SalesforceSalesAttributes.SECURITY_TOKEN, isSecured = true, required = false)
@Field.Boolean(value = SalesforceSalesAttributes.SANDBOX, required = false)
@Field.Text(value = SalesforceSalesAttributes.TIME_ZONE, required = false)
@ChangeLog(file = "docs/pages/releaseNotes.md")
@Extension(version = "2.0.18", name = "Salesforce sales", description = "Integrating Salesforce with Krista")
@Java(version = Java.Version.JAVA_21)
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
    public RequestAuthenticator getRequestAuthenticator() {
        return requestAuthenticator;
    }

    @InvokerRequest(InvokerRequest.Type.CUSTOM_TABS)
    public Map<String, String> customTabs() {
        return Map.of("Documentation", "static/docs");
    }

    @InvokerRequest(InvokerRequest.Type.VALIDATE_ATTRIBUTES)
    public void validateConnection(Map<String, Object> connectionAttributes) throws IOException {
        SalesforceSalesAttributes salesAttributes = SalesforceSalesAttributes.create(invoker, connectionAttributes);
        validateSalesAttributes(salesAttributes);
    }

    private void validateSalesAttributes(SalesforceSalesAttributes salesforceSalesAttributes) throws IOException {
        Validators.validateTimeZone(salesforceSalesAttributes.getTimezone());
        if (salesforceSalesAttributes.getUserName() == null || salesforceSalesAttributes.getUserName().isBlank() ||
                salesforceSalesAttributes.getPassword() == null || salesforceSalesAttributes.getPassword().isBlank() ||
                salesforceSalesAttributes.getSecurityToken() == null || salesforceSalesAttributes.getSecurityToken().isBlank()) {
            providerFactory.create(salesforceSalesAttributes).getSalesforceTokenForUser();
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
