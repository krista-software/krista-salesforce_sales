package app.krista.extensions.crm.sales.salesforce_sales.impl.stores;

import app.krista.extensions.util.KeyValueStore;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.io.IOException;

@Service
public final class RefreshTokenStore {

    private final KeyValueStore keyValueStore;

    @Inject
    public RefreshTokenStore(KeyValueStore keyValueStore) {
        this.keyValueStore = keyValueStore;
    }

    public void put(String key, String refToken) throws IOException {
        keyValueStore.put(key, refToken);
    }

    public String get(String key) throws IOException {
        return keyValueStore.get(key, String.class);
    }

    public void deleteSession(String key) throws IOException {
        keyValueStore.remove(key);
    }
}
