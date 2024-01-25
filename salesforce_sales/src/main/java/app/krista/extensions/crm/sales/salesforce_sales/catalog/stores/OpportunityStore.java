package app.krista.extensions.crm.sales.salesforce_sales.catalog.stores;

import app.krista.extension.executor.SearchCondition;
import app.krista.extension.executor.SearchQuery;
import app.krista.extension.util.EntityStore;
import app.krista.extensions.crm.sales.salesforce_sales.SalesforceSalesAttributes;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes.OpportunityCatalogType;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Opportunity;
import app.krista.extensions.crm.sales.salesforce_sales.impl.OpportunityDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.impl.OpportunityImpl;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFOpportunity;
import app.krista.extensions.crm.sales.salesforce_sales.service.OpportunityDao;
import app.krista.extensions.crm.sales.salesforce_sales.util.OpportunityQueryBuilder;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Service
public class OpportunityStore implements EntityStore<Opportunity> {

    private final OpportunityDao opportunityDao;
    private static final Logger LOGGER = LoggerFactory.getLogger(OpportunityStore.class);
    private final SalesforceSalesAttributes salesforceSalesAttributes;


    @Inject
    public OpportunityStore(OpportunityDaoImpl opportunityDao, SalesforceSalesAttributes salesforceSalesAttributes) {
        this.opportunityDao = opportunityDao;
        this.salesforceSalesAttributes = salesforceSalesAttributes;
    }

    @Override
    public Opportunity create(Map<String, Object> entityAttributeField) {
        if (entityAttributeField == null || entityAttributeField.isEmpty()) {
            LOGGER.info("Entity attribute field map cannot be null or empty");
        }
        try {
            String opportunityId = opportunityDao.addOpportunity(entityAttributeField);
            return get(opportunityId);
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return new Opportunity();
        }
    }

    @Override
    public Opportunity get(String primaryKey) {
        if (primaryKey == null || primaryKey.isEmpty()) {
            LOGGER.info("Primary key cannot be null or empty");
        }
        try {
            return OpportunityCatalogType.fromSFOpportunity(opportunityDao.getOpportunityById(primaryKey));
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return null;
        }
    }

    @Override
    public Opportunity update(Opportunity opportunity) {
        if (opportunity == null) {
            LOGGER.info("Opportunity cannot be null");
        }
        try {
            opportunityDao.updateOpportunity(new OpportunityImpl(SFOpportunity.from(opportunity)));
            return opportunity;
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return null;
        }
    }

    @Override
    public void delete(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Delete identifier cannot be null or empty");
        }
        opportunityDao.deleteOpportunity(key);
    }

    @Override
    public boolean contains(String s) {
        return false;
    }

    @Override
    public List<Opportunity> search(List<SearchCondition> conditions, long pageIndex, int pageSize) {
        return List.of();
    }

    @Override
    public List<Opportunity> search(SearchQuery searchQuery, long pageIndex, int pageSize) throws IOException {
        if (searchQuery == null || searchQuery.getSearchConditions() == null || searchQuery.getSearchConditions().isEmpty()) {
            LOGGER.info("Search conditions cannot be null or empty");
        }
        List<SearchCondition> conditions = searchQuery.getSearchConditions();
        String timeZone = salesforceSalesAttributes.getTimezone() != null ? salesforceSalesAttributes.getTimezone() : TimeZone.getDefault().getID();
        try {
            List<Opportunity> opportunities;
            if (conditions.size() == 1) {
                opportunities = opportunityDao.getOpportunities(pageSize, pageIndex).stream().map(OpportunityCatalogType::fromSFOpportunity).collect(Collectors.toList());
            } else {
                String query = OpportunityQueryBuilder.getQuery(conditions, pageSize, pageIndex, timeZone);
                opportunities = opportunityDao.getOpportunitiesByQuery(query).stream().map(OpportunityCatalogType::fromSFOpportunity).collect(Collectors.toList());
            }
            return opportunities;
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return List.of();
        }
    }

    @Override
    public long count(List<SearchCondition> list) {
        return 0;
    }

    @Override
    public List<String> lookup(Map<String, Object> map, List<String> list) {
        return null;
    }
}
