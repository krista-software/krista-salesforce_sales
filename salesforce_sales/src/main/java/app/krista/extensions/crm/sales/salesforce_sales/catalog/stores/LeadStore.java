package app.krista.extensions.crm.sales.salesforce_sales.catalog.stores;

import app.krista.extension.executor.SearchCondition;
import app.krista.extension.executor.SearchQuery;
import app.krista.extension.util.EntityStore;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes.LeadCatalogType;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Lead;
import app.krista.extensions.crm.sales.salesforce_sales.impl.LeadDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.impl.LeadImpl;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFLead;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static app.krista.extensions.crm.sales.salesforce_sales.impl.LeadDaoImpl.QUERY_FIELDS;
import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.AND;

@Service
public class LeadStore implements EntityStore<Lead> {

    private final LeadDaoImpl leadDao;
    private static final Logger LOGGER = LoggerFactory.getLogger(LeadStore.class);


    @Inject
    public LeadStore(LeadDaoImpl leadDao) {
        this.leadDao = leadDao;
    }

    @Override
    public Lead create(Map<String, Object> entityAttributeField) {
        if (entityAttributeField == null || entityAttributeField.isEmpty()) {
            LOGGER.info("Entity attribute field map cannot be null or empty");
        }
        try {
            leadDao.addLead(entityAttributeField);
            return Lead.fromFields(entityAttributeField);
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return new Lead();
        }
    }

    @Override
    public Lead get(String primaryKey) {
        if (primaryKey == null || primaryKey.isEmpty()) {
            LOGGER.info("Primary key cannot be null or empty");
        }
        try {
            return LeadCatalogType.fromSFLead(leadDao.getLeadById(primaryKey));
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return null;
        }
    }

    @Override
    public Lead update(Lead lead) {
        if (lead == null) {
            LOGGER.info("Lead cannot be null");
        }
        try {
            leadDao.updateLead(new LeadImpl(SFLead.from(lead)));
            return lead;
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
        leadDao.deleteLead(key);
    }

    @Override
    public boolean contains(String s) {
        return false;
    }

    @Override
    public List<Lead> search(List<SearchCondition> conditions, long pageIndex, int pageSize) {
        return List.of();
    }

    @Override
    public List<Lead> search(SearchQuery searchQuery, long pageIndex, int pageSize) throws IOException {
        if (searchQuery == null || searchQuery.getSearchConditions() == null || searchQuery.getSearchConditions().isEmpty()) {
            LOGGER.info("Search conditions cannot be null or empty");
        }
        List<SearchCondition> conditions = searchQuery.getSearchConditions();
        try {
            String name = getStringConditionValue(conditions, "Name");
            String ownerId = getStringConditionValue(conditions, "Owner Id");
            String status = getStringConditionValue(conditions, "Status");
            List<Lead> leads;
            if (conditions.size() == 1) {
                leads = leadDao.getLeads(pageSize, pageIndex).stream().map(LeadCatalogType::fromSFLead).collect(Collectors.toList());
            } else {
                String query = createQuery(name, ownerId, status, pageSize, pageIndex);
                leads = leadDao.getLeadsByQuery(query).stream().map(LeadCatalogType::fromSFLead).collect(Collectors.toList());
            }
            return leads;
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

    private String getStringConditionValue(List<SearchCondition> conditions, String fieldName) {
        for (SearchCondition con : conditions) {
            if (fieldName.equalsIgnoreCase(con.getFieldName())) {
                return con.getOperand() != null ? String.valueOf(con.getOperand()).trim() : "";
            }
        }
        return "";
    }

    private String createQuery(String name, String ownerId, String status, long size, long page) {
        StringBuilder builder = new StringBuilder();
        builder.append("Select " + QUERY_FIELDS + " from Lead WHERE").
                append((isAvailable(name) ? " name LIKE '%" + name + "%'" + AND : "")).
                append((isAvailable(ownerId) ? " ownerId = '" + ownerId + "'" + AND : "")).
                append((isAvailable(status) ? " status LIKE '%" + status + "%'" + AND : ""));
        if (builder.substring(builder.length() - "WHERE".length()).equals("WHERE"))
            builder.setLength(builder.lastIndexOf("WHERE"));
        if (builder.substring(builder.length() - AND.length()).equals(AND))
            builder.setLength(builder.lastIndexOf(AND));
        builder.append(" ORDER BY LastModifiedDate LIMIT ").append(size).append(" OFFSET ").append(Math.max((page - 1) * size, 0));
        return builder.toString();
    }

    private static boolean isAvailable(String s) {
        return s != null && !s.isBlank();
    }
}
