package app.krista.extensions.crm.sales.salesforce_sales.impl;

import app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes.TaskCatalogType;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFTask;
import app.krista.extensions.crm.sales.salesforce_sales.service.Task;
import app.krista.extensions.crm.sales.salesforce_sales.service.TaskDao;
import app.krista.extensions.crm.sales.salesforce_sales.util.Constants;
import app.krista.extensions.crm.sales.salesforce_sales.util.SalesforceListResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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

@Service
public class TaskDaoImpl implements TaskDao {

    private static final String TASK = "Task";
    private static final String Query_Fields = "Id, OwnerId, Owner.Name, Subject, Priority, ActivityDate, Description, Status, AccountId, WhatId, WhoId, Account.Name, What.Name, What.Type";
    private static final String API_NAME = "ApiName";
    private static final String RECORDS = "records";
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
            throw new IllegalArgumentException("Task Id cant be empty");
        SFTask updatedTask = SFTask.from(TaskCatalogType.fromSFTask(task));
        SFTask oldTask = SFTask.from(TaskCatalogType.fromSFTask(getTaskById(updatedTask.id)));
        Map<String, Object> diff = salesforceManager.getDifference(oldTask, updatedTask);
        salesforceManager.updateRecord(updatedTask.id, GSON.toJson(diff), TASK);
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
        try {
            String jsonResponse = salesforceManager.getRecordByQuery("SELECT ApiName FROM TaskStatus");

            if (jsonResponse == null || jsonResponse.isEmpty()) {
                LOGGER.info("Empty JSON response");
                return new ArrayList<>();
            }

            JsonObject jsonObject = GSON.fromJson(jsonResponse, JsonObject.class);

            if (!jsonObject.has(RECORDS)) {
                LOGGER.info("records array not present in JSON response");
                return new ArrayList<>();
            }

            JsonArray jsonArray = jsonObject.getAsJsonArray(RECORDS);

            if (jsonArray == null || jsonArray.size() == 0) {
                LOGGER.info("records array is empty");
                return new ArrayList<>();
            }

            return getApiNamesList(jsonArray);
        } catch (Exception cause) {
            LOGGER.error(cause.getMessage(), cause);
            return new ArrayList<>();
        }
    }

    private static List<String> getApiNamesList(JsonArray jsonArray) {
        List<String> apiNamesList = new ArrayList<>();
        for (JsonElement jsonElement : jsonArray) {
            JsonObject object = jsonElement.getAsJsonObject();

            if (object.has(API_NAME)) {
                String apiName = object.get(API_NAME).getAsString();
                apiNamesList.add(apiName);
            } else {
                LOGGER.info("ApiName not found in a record");
            }
        }

        return apiNamesList;
    }
}
