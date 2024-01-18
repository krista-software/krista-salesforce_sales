package app.krista.extensions.crm.sales.salesforce_sales.impl.stores;

import app.krista.extension.executor.Invoker;
import app.krista.extension.request.RoutingInfo;
import app.krista.extension.request.protos.http.HttpProtocol;
import app.krista.extensions.crm.sales.salesforce_sales.SalesforceSalesAttributes;
import app.krista.extensions.crm.sales.salesforce_sales.util.Constants;
import app.krista.extensions.util.KeyValueStore;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.util.Map;
import java.util.UUID;

@Service
public final class SalesforceSaleAttributeStore {

    private final KeyValueStore store;
    private final Invoker invoker;

    @Inject
    public SalesforceSaleAttributeStore(KeyValueStore store, Invoker invoker) {
        this.store = store;
        this.invoker = invoker;
    }

    public String save(SalesforceSalesAttributes salesforceSalesAttributes) {
        String authContextId = UUID.randomUUID().toString();
        store.put(authContextId, Constants.GSON.toJson(Map.of(SalesforceSalesAttributes.CLIENT_ID, salesforceSalesAttributes.getClientId(),
                SalesforceSalesAttributes.CLIENT_SECRET, salesforceSalesAttributes.getClientSecret(),
                SalesforceSalesAttributes.SANDBOX, salesforceSalesAttributes.isSandbox())));
        return authContextId;
    }

    public SalesforceSalesAttributes load(String authContextId) {
        Map attributes = Constants.GSON.fromJson(store.get(authContextId).toString(), Map.class);
        return new SalesforceSalesAttributes((String) attributes.get(SalesforceSalesAttributes.CLIENT_ID),
                (String) attributes.get(SalesforceSalesAttributes.CLIENT_SECRET),
                (String) attributes.get(SalesforceSalesAttributes.USER_NAME),
                (String) attributes.get(SalesforceSalesAttributes.PASS_WORD),
                (String) attributes.get(SalesforceSalesAttributes.SECURITY_TOKEN),
                (Boolean) attributes.get(SalesforceSalesAttributes.SANDBOX),
                (String) attributes.get(SalesforceSalesAttributes.TIME_ZONE),
                invoker.getRoutingInfo().getRoutingURL(HttpProtocol.PROTOCOL_NAME, RoutingInfo.Type.APPLIANCE));
    }

    public void remove(String authContextId) {
        store.remove(authContextId);
    }
}
