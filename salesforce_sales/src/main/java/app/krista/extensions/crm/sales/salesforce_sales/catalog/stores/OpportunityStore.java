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

package app.krista.extensions.crm.sales.salesforce_sales.catalog.stores;

import app.krista.extension.executor.QueryClause;
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
import app.krista.extensions.crm.sales.salesforce_sales.util.SoqlQueryHelper;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static app.krista.extensions.crm.sales.salesforce_sales.impl.OpportunityDaoImpl.QUERY_FIELDS;

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
            throw new IllegalArgumentException("Opportunity ID is required for deletion. Please provide the Salesforce ID of the opportunity you want to delete.");
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
            return opportunityDao.getOpportunities(pageSize, pageIndex).stream().map(OpportunityCatalogType::fromSFOpportunity).collect(Collectors.toList());
        }
        List<SearchCondition> conditions = searchQuery.getSearchConditions();
        List<QueryClause> clauses = searchQuery.getQueryClauses();
        try {
            // Single Id lookup optimization
            for (SearchCondition sc : conditions) {
                if ("Id".equalsIgnoreCase(sc.getFieldName()) && sc.getOperand() != null) {
                    String id = String.valueOf(sc.getOperand()).trim();
                    if (!id.isEmpty()) {
                        Opportunity entity = get(id);
                        return entity != null ? List.of(entity) : List.of();
                    }
                }
            }
            String timeZone = salesforceSalesAttributes.getTimezone() != null
                    ? salesforceSalesAttributes.getTimezone() : java.util.TimeZone.getDefault().getID();
            String where = SoqlQueryHelper.buildWhereClause("opportunity", conditions, timeZone);
            String orderBy = SoqlQueryHelper.buildOrderByClause("opportunity", clauses);
            String pagination = SoqlQueryHelper.buildPaginationClause(pageSize, pageIndex);

            if (where.isEmpty()) {
                return opportunityDao.getOpportunities(pageSize, pageIndex).stream().map(OpportunityCatalogType::fromSFOpportunity).collect(Collectors.toList());
            }
            String query = "SELECT " + QUERY_FIELDS + " FROM Opportunity WHERE " + where + orderBy + pagination;
            return opportunityDao.getOpportunitiesByQuery(query).stream().map(OpportunityCatalogType::fromSFOpportunity).collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
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
