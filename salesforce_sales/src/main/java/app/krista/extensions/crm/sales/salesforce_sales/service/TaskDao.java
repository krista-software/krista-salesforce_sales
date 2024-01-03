package app.krista.extensions.crm.sales.salesforce_sales.service;

import java.util.List;
import java.util.Map;

public interface TaskDao {

    String addTask(Map<String, Object> entityAttributeField);

    void deleteTask(String key);

    Task getTaskById(String id);

    List<Task> getTasks(long size, long page);

    void updateTask(Task task);

    List<Task> getTasksByQuery(String query);

    List<String> getTaskStatusByQuery();
}
