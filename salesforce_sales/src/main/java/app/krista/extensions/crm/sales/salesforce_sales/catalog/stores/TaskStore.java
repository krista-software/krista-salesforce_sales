package app.krista.extensions.crm.sales.salesforce_sales.catalog.stores;

import app.krista.extension.executor.SearchCondition;
import app.krista.extension.executor.SearchQuery;
import app.krista.extension.util.EntityStore;
import app.krista.extensions.crm.sales.salesforce_sales.SalesforceSalesAttributes;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes.TaskCatalogType;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Task;
import app.krista.extensions.crm.sales.salesforce_sales.impl.TaskDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.impl.TaskImpl;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFTask;
import app.krista.extensions.crm.sales.salesforce_sales.util.TaskQueryBuilder;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

import static app.krista.extensions.crm.sales.salesforce_sales.util.DatePlus.getDateForGivenTimeZone;

@Service
public class TaskStore implements EntityStore<Task> {

    private static final String DUE_DATE = "Due Date";
    private static final String TITLE = "Title";
    private static final String EQUALS = "equals";
    private static final String CONTAINS = "contains";
    private static final String ASSIGNED_TO_NAME = "Assigned To Name";
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
            throw new IllegalArgumentException("Delete identifier cannot be null or empty");
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
            LOGGER.info("Search conditions cannot be null or empty");
        }
        List<SearchCondition> conditions = searchQuery.getSearchConditions();
        String timeZone = salesforceSalesAttributes.getTimezone() != null ? salesforceSalesAttributes.getTimezone() : TimeZone.getDefault().getID();
        try {
            List<Task> tasks;
            if (conditions.size() == 1) {
                tasks = taskDao.getTasks(pageSize, pageIndex).stream().map(TaskCatalogType::fromSFTask).collect(Collectors.toList());
            } else {
                String query = createQuery(pageIndex, pageSize, conditions, timeZone);

                tasks = taskDao.getTasksByQuery(query).stream().map(TaskCatalogType::fromSFTask).collect(Collectors.toList());
            }
            return tasks;
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return List.of();
        }
    }

    private static String createQuery(long pageIndex, int pageSize, List<SearchCondition> conditions, String timeZone) {
        return TaskQueryBuilder.builder()
                .assignedToNameEquals(getStringConditionValue(conditions, ASSIGNED_TO_NAME, EQUALS))
                .assignedToNameContains(getStringConditionValue(conditions, ASSIGNED_TO_NAME, CONTAINS))
                .priority(getStringConditionValue(conditions, "Priority"))
                .status(getStringConditionValue(conditions, "Status"))
                .titleEquals(getStringConditionValue(conditions, TITLE, EQUALS))
                .titleContains(getStringConditionValue(conditions, TITLE, CONTAINS))
                .titleStartsWith(getStringConditionValue(conditions, TITLE, "startswith"))
                .titleEndsWith(getStringConditionValue(conditions, TITLE, "endswith"))
                .dueDateEquals(getDateForGivenTimeZone(conditions, DUE_DATE, EQUALS, timeZone))
                .dueDateLessThan(getDateForGivenTimeZone(conditions, DUE_DATE, "lt", timeZone))
                .dueDateGreaterThan(getDateForGivenTimeZone(conditions, DUE_DATE, "gt", timeZone))
                .size(pageSize)
                .page(pageIndex)
                .build();
    }

    @Override
    public long count(List<SearchCondition> list) throws IOException {
        return 0;
    }

    @Override
    public List<String> lookup(Map<String, Object> map, List<String> list) throws IOException {
        return null;
    }

    private static String getStringConditionValue(List<SearchCondition> conditions, String fieldName) {
        for (SearchCondition con : conditions) {
            if (fieldName.equalsIgnoreCase(con.getFieldName())) {
                return con.getOperand() != null ? String.valueOf(con.getOperand()).trim() : "";
            }
        }
        return "";
    }

    private static String getStringConditionValue(List<SearchCondition> conditions, String fieldName, String queryOperand) {
        for (SearchCondition con : conditions) {
            if (fieldName.equalsIgnoreCase(con.getFieldName()) && con.getOperator().toString().equalsIgnoreCase(queryOperand)) {
                return con.getOperand() != null ? String.valueOf(con.getOperand()).trim() : "";
            }
        }
        return "";
    }

}
