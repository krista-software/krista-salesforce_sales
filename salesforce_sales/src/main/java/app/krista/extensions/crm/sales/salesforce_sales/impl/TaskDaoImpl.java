package app.krista.extensions.crm.sales.salesforce_sales.impl;

import app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes.TaskCatalogType;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFTask;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFTaskStatus;
import app.krista.extensions.crm.sales.salesforce_sales.service.Task;
import app.krista.extensions.crm.sales.salesforce_sales.service.TaskDao;
import app.krista.extensions.crm.sales.salesforce_sales.util.Constants;
import app.krista.extensions.crm.sales.salesforce_sales.util.SalesforceListResponse;
import com.google.gson.reflect.TypeToken;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.GSON;
import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.gsonBuilder;

@Service
public class TaskDaoImpl implements TaskDao {

    private static final String TASK = "Task";
    private static final String Query_Fields = "Id, OwnerId, Owner.Name, Subject, Priority, ActivityDate, Description, Status, AccountId, WhatId, WhoId, Account.Name, What.Name, What.Type";
    private final SalesforceManager salesforceManager;
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskDaoImpl.class);

    @Inject
    public TaskDaoImpl(SalesforceManager salesforceManager) {
        this.salesforceManager = salesforceManager;
    }

    @Override
    public String addTask(Map<String, Object> entityAttributeField) {
        if (entityAttributeField == null || entityAttributeField.isEmpty()) {
            LOGGER.info("Failed create task request. " + Constants.ENTITY_MAP_IS_NULL_OR_EMPTY);
        }
        var response = GSON.fromJson(salesforceManager.createRecord(GSON.toJson(SFTask.from(entityAttributeField)), TASK), Map.class);
        return String.valueOf(response.get("id"));
    }

    @Override
    public void deleteTask(String key) {
        salesforceManager.deleteRecordById(key, TASK);
    }

    @Override
    public Task getTaskById(String id) {
        return getTasksByQuery("id = '" + id + "'").stream().findFirst().orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public void updateTask(Task task) {
        if (task.getId() == null || task.getId().isEmpty())
            throw new IllegalArgumentException("Task ID is required. Please provide the Salesforce ID of the task you want to update.");
        SFTask updatedTask = SFTask.from(TaskCatalogType.fromSFTask(task));
        SFTask oldTask = SFTask.from(TaskCatalogType.fromSFTask(getTaskById(updatedTask.id)));
        Map<String, Object> diff = salesforceManager.getDifference(oldTask, updatedTask);
        salesforceManager.updateRecord(updatedTask.id, gsonBuilder.toJson(diff), TASK);
    }

    @Override
    public List<Task> getTasks(long size, long page) {
        return getTasksByQuery("Select " + Query_Fields + " from Task ORDER BY Owner.Name LIMIT " + size + " OFFSET " + Math.max((page - 1) * size, 0));
    }

    @Override
    public List<Task> getTasksByQuery(String query) {
        if (!query.toLowerCase(Locale.ROOT).startsWith("select")) {
            query = "Select " + Query_Fields + " from Task where " + query;
        }
        String response = salesforceManager.getRecordByQuery(query);
        SalesforceListResponse<SFTask> taskList =
                GSON.fromJson(response, new TypeToken<SalesforceListResponse<SFTask>>() {
                }.getType());
        if (taskList.records.size() > 0) {
            return taskList.records.stream().map(TaskImpl::new).collect(Collectors.toList());
        } else {
            LOGGER.info("No tasks found");
            return new ArrayList<>();
        }
    }

    @Override
    public List<String> getTaskStatusByQuery() {
        String jsonResponse = salesforceManager.getRecordByQuery("SELECT ApiName FROM TaskStatus");

        SalesforceListResponse<SFTaskStatus> taskList =
                GSON.fromJson(jsonResponse, new TypeToken<SalesforceListResponse<SFTaskStatus>>() {
                }.getType());

        return taskList != null ? taskList.records.stream()
                .map(SFTaskStatus::getApiName)
                .collect(Collectors.toList()) : List.of();
    }
}
