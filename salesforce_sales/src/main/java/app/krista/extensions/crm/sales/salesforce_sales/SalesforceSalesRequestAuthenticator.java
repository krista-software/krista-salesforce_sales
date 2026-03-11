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

import app.krista.extension.authorization.MustAuthenticateException;
import app.krista.extension.authorization.MustAuthorizeException;
import app.krista.extension.authorization.RequestAuthenticator;
import app.krista.extension.request.ProtoRequest;
import app.krista.extension.request.ProtoResponse;
import app.krista.extension.request.protos.http.HttpRequest;
import app.krista.extensions.crm.sales.salesforce_sales.impl.stores.SalesforceSaleAttributeStore;
import app.krista.model.field.NamedField;
import app.krista.model.field.NamedValuedField;
import com.github.scribejava.core.oauth.OAuth20Service;

import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.util.*;

public class SalesforceSalesRequestAuthenticator implements RequestAuthenticator {
    private final SalesforceSalesAttributes salesforceSalesAttributes;
    private final SalesforceSaleAttributeStore attributeStore;

    public SalesforceSalesRequestAuthenticator(SalesforceSalesAttributes salesforceSalesAttributes, SalesforceSaleAttributeStore attributeStore) {
        this.salesforceSalesAttributes = salesforceSalesAttributes;
        this.attributeStore = attributeStore;
    }

    @Override
    public String getScheme() {
        return null;
    }

    @Override
    public Set<String> getSupportedProtocols() {
        return Set.of();
    }

    @Override
    public String getAuthenticatedAccountId(ProtoRequest protoRequest) {
        try {
            HttpRequest httpRequest = (HttpRequest) protoRequest;
            if ("/salesforcesales/callback".equals(httpRequest.getUri().getPath())) {
                httpRequest.bufferBody();
                MultivaluedMap<String, String> queryParameters = httpRequest.getQueryParameters();
                List<String> stateParam = queryParameters == null ? null : queryParameters.get("state");
                if (stateParam == null || stateParam.get(0) == null) {
                    return null;
                }
                String state = String.valueOf(stateParam.get(0));
                String[] parts = state.split("&");
                return parts[0];
            } else {
                return null;
            }
        } catch (RuntimeException | IOException cause) {
            return null;
        }
    }

    @Override
    public boolean setServiceAuthorization(String s) {
        return false;
    }

    @Override
    public Map<String, NamedField> getAttributeFields() {
        return Map.of();
    }

    @Override
    public ProtoResponse getMustAuthenticateResponse(MustAuthenticateException cause, ProtoRequest request) {
        return null;
    }

    @Override
    public AuthorizationResponse getMustAuthenticateResponse(MustAuthenticateException cause) {
        return null;
    }

    @Override
    public ProtoResponse getMustAuthorizeResponse(MustAuthorizeException cause, ProtoRequest request) {
        return null;
    }

    @Override
    public AuthorizationResponse getMustAuthorizeResponse(MustAuthorizeException cause) {
        String userId = (String) cause.getDetails().get(0).getValue();
        Optional<NamedValuedField> authContextIdField = cause.getDetails().stream().filter(f -> Objects.equals(f.getName(), "authContextId")).findFirst();
        SalesforceSalesAttributes effectiveSalesforceSalesAttributes;
        String state;
        if (authContextIdField.isPresent()) {
            String authContextId = (String) authContextIdField.get().getValue();
            effectiveSalesforceSalesAttributes = attributeStore.load(authContextId);
            state = userId + "#" + authContextId;
        } else {
            effectiveSalesforceSalesAttributes = salesforceSalesAttributes;
            state = userId;
        }
        OAuth20Service salesforceSaleService = effectiveSalesforceSalesAttributes.getOauthService();
        String url = salesforceSaleService.getAuthorizationUrl(state);
        return new AuthorizationResponse(url, List.of());
    }

}
