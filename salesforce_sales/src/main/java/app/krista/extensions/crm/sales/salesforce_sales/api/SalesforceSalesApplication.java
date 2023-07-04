package app.krista.extensions.crm.sales.salesforce_sales.api;

import org.jvnet.hk2.annotations.ContractsProvided;
import org.jvnet.hk2.annotations.Service;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Set;

@Service
@ApplicationPath("salesforcesales")
@ContractsProvided(Application.class)
public class SalesforceSalesApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        return Set.of(AuthenticationResource.class);
    }
}
