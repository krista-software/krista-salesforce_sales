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
import app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes.LeadCatalogType;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Lead;
import app.krista.extensions.crm.sales.salesforce_sales.impl.LeadDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.impl.LeadImpl;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFLead;
import app.krista.extensions.crm.sales.salesforce_sales.util.SoqlQueryHelper;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static app.krista.extensions.crm.sales.salesforce_sales.impl.LeadDaoImpl.QUERY_FIELDS;

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
            String leadId = leadDao.addLead(entityAttributeField);
            return get(leadId);
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
            throw new IllegalArgumentException("Lead ID is required for deletion. Please provide the Salesforce ID of the lead you want to delete.");
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
            return leadDao.getLeads(pageSize, pageIndex).stream()
                    .map(LeadCatalogType::fromSFLead)
                    .collect(Collectors.toList());
        }
        List<SearchCondition> conditions = searchQuery.getSearchConditions();
        List<QueryClause> clauses = searchQuery.getQueryClauses();
        try {
            // Single Id lookup optimization
            for (SearchCondition sc : conditions) {
                if ("Id".equalsIgnoreCase(sc.getFieldName()) && sc.getOperand() != null) {
                    String id = String.valueOf(sc.getOperand()).trim();
                    if (!id.isEmpty()) {
                        Lead lead = get(id);
                        return lead != null ? List.of(lead) : List.of();
                    }
                }
            }
            // Build SOQL using helpers
            String where = SoqlQueryHelper.buildWhereClause("lead", conditions);
            String orderBy = SoqlQueryHelper.buildOrderByClause("lead", clauses);
            String pagination = SoqlQueryHelper.buildPaginationClause(pageSize, pageIndex);

            if (where.isEmpty()) {
                return leadDao.getLeads(pageSize, pageIndex).stream()
                        .map(LeadCatalogType::fromSFLead)
                        .collect(Collectors.toList());
            }

            String query = "SELECT " + QUERY_FIELDS + " FROM Lead WHERE " + where + orderBy + pagination;
            return leadDao.getLeadsByQuery(query).stream()
                    .map(LeadCatalogType::fromSFLead)
                    .collect(Collectors.toList());
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
