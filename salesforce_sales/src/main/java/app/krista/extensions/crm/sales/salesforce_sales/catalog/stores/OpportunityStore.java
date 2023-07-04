package app.krista.extensions.crm.sales.salesforce_sales.catalog.stores;

import app.krista.extension.executor.SearchCondition;
import app.krista.extension.executor.SearchQuery;
import app.krista.extension.util.EntityStore;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.CatalogTypes;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Opportunity;
import app.krista.extensions.crm.sales.salesforce_sales.impl.OpportunityDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.impl.OpportunityImpl;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFOpportunity;
import app.krista.extensions.crm.sales.salesforce_sales.service.OpportunityDao;
import app.krista.extensions.crm.sales.salesforce_sales.util.OpportunityQueryBuilder;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OpportunityStore implements EntityStore<Opportunity> {

    private final OpportunityDao opportunityDao;

    @Inject
    public OpportunityStore(OpportunityDaoImpl opportunityDao) {
        this.opportunityDao = opportunityDao;
    }

    @Override
    public Opportunity create(Map<String, Object> entityAttributeField) {
        if (entityAttributeField == null || entityAttributeField.isEmpty()) {
            System.err.println("Entity attribute field map cannot be null or empty");
        }
        try {
            opportunityDao.addOpportunity(entityAttributeField);
            return Opportunity.fromFields(entityAttributeField);
        } catch (IllegalArgumentException cause) {
            System.err.println(cause.getMessage());
            cause.printStackTrace();
            return new Opportunity();
        }
    }

    @Override
    public Opportunity get(String primaryKey) {
        if (primaryKey == null || primaryKey.isEmpty()) {
            System.err.println("Primary key cannot be null or empty");
        }
        try {
            return CatalogTypes.fromSFOpportunity(opportunityDao.getOpportunityById(primaryKey));
        } catch (IllegalArgumentException cause) {
            System.err.println(cause.getMessage());
            cause.printStackTrace();
            return null;
        }
    }

    @Override
    public Opportunity update(Opportunity opportunity) {
        if (opportunity == null) {
            System.err.println("Opportunity cannot be null");
        }
        try {
            opportunityDao.updateOpportunity(new OpportunityImpl(SFOpportunity.from(opportunity)));
            return opportunity;
        } catch (IllegalArgumentException cause) {
            System.err.println(cause.getMessage());
            cause.printStackTrace();
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
            System.err.println("Search conditions cannot be null or empty");
        }
        List<SearchCondition> conditions = searchQuery.getSearchConditions();
        try {
            List<Opportunity> opportunities;
            if (conditions.size() == 1) {
                opportunities = opportunityDao.getOpportunities(pageSize, pageIndex).stream().map(CatalogTypes::fromSFOpportunity).collect(Collectors.toList());
            } else {
                String query = OpportunityQueryBuilder.getQuery(conditions, pageSize, pageIndex);
                opportunities = opportunityDao.getOpportunitiesByQuery(query).stream().map(CatalogTypes::fromSFOpportunity).collect(Collectors.toList());
            }
            return opportunities;
        } catch (IllegalArgumentException cause) {
            System.err.println(cause.getMessage());
            cause.printStackTrace();
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
