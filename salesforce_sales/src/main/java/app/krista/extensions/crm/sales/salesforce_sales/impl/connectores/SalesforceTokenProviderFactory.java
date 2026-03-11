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
