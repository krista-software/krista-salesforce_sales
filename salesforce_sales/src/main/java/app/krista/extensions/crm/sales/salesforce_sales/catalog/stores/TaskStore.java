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
import app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes.TaskCatalogType;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Task;
import app.krista.extensions.crm.sales.salesforce_sales.impl.TaskDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.impl.TaskImpl;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFTask;
import app.krista.extensions.crm.sales.salesforce_sales.util.SoqlQueryHelper;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TaskStore implements EntityStore<Task> {

    private static final String QUERY_FIELDS = "Id, OwnerId, Owner.Name, Subject, Priority, ActivityDate, Description, Status, AccountId, WhatId, WhoId, Account.Name, What.Name, What.Type";
    private final TaskDaoImpl taskDao;
    private final SalesforceSalesAttributes salesforceSalesAttributes;
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskStore.class);


    @Inject
    public TaskStore(TaskDaoImpl taskDao, SalesforceSalesAttributes salesforceSalesAttributes) {
        this.taskDao = taskDao;
        this.salesforceSalesAttributes = salesforceSalesAttributes;
    }

    @Override
    public Task create(Map<String, Object> entityAttributeField) {

        if (entityAttributeField == null || entityAttributeField.isEmpty()) {
            LOGGER.info("Entity attribute field map cannot be null or empty");
        }
        try {
            String taskId = taskDao.addTask(entityAttributeField);
            return get(taskId);
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return new Task();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }


    @Override
    public Task get(String primaryKey) throws IOException {
        if (primaryKey == null || primaryKey.isEmpty()) {
            LOGGER.info("Primary key cannot be null or empty");
        }
        try {
            return TaskCatalogType.fromSFTask(taskDao.getTaskById(primaryKey));
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return null;
        }
    }


    @Override
    public Task update(Task task) throws IOException {
        if (task == null) {
            LOGGER.info("Task cannot be null");
            return null;
        }
        try {
            taskDao.updateTask(new TaskImpl(SFTask.from(task)));
            return get(task.id);
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return null;
        }
    }

    @Override
    public void delete(String key) throws IOException {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Task ID is required for deletion. Please provide the Salesforce ID of the task you want to delete.");
        }
        taskDao.deleteTask(key);

    }

    @Override
    public boolean contains(String s) throws IOException {
        return false;
    }

    @Override
    public List<Task> search(List<SearchCondition> list, long l, int i) throws IOException {
        return null;
    }

    @Override
    public List<Task> search(SearchQuery searchQuery, long pageIndex, int pageSize) throws IOException {
        if (searchQuery == null || searchQuery.getSearchConditions() == null || searchQuery.getSearchConditions().isEmpty()) {
            return taskDao.getTasks(pageSize, pageIndex).stream().map(TaskCatalogType::fromSFTask).collect(Collectors.toList());
        }
        List<SearchCondition> conditions = searchQuery.getSearchConditions();
        List<QueryClause> clauses = searchQuery.getQueryClauses();
        try {
            // Single Id lookup optimization
            for (SearchCondition sc : conditions) {
                if ("Id".equalsIgnoreCase(sc.getFieldName()) && sc.getOperand() != null) {
                    String id = String.valueOf(sc.getOperand()).trim();
                    if (!id.isEmpty()) {
                        Task entity = get(id);
                        return entity != null ? List.of(entity) : List.of();
                    }
                }
            }
            String timeZone = salesforceSalesAttributes.getTimezone() != null
                    ? salesforceSalesAttributes.getTimezone() : java.util.TimeZone.getDefault().getID();
            String where = SoqlQueryHelper.buildWhereClause("task", conditions, timeZone);
            String orderBy = SoqlQueryHelper.buildOrderByClause("task", clauses);
            String pagination = SoqlQueryHelper.buildPaginationClause(pageSize, pageIndex);

            if (where.isEmpty()) {
                return taskDao.getTasks(pageSize, pageIndex).stream().map(TaskCatalogType::fromSFTask).collect(Collectors.toList());
            }
            String query = "SELECT " + QUERY_FIELDS + " FROM Task WHERE " + where + orderBy + pagination;
            return taskDao.getTasksByQuery(query).stream().map(TaskCatalogType::fromSFTask).collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return List.of();
        }
    }

    @Override
    public long count(List<SearchCondition> list) throws IOException {
        return 0;
    }

    @Override
    public List<String> lookup(Map<String, Object> map, List<String> list) throws IOException {
        return null;
    }

}
