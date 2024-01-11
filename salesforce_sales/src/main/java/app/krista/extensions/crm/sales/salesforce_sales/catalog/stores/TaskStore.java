package app.krista.extensions.crm.sales.salesforce_sales.catalog.stores;

import app.krista.extension.executor.SearchCondition;
import app.krista.extension.executor.SearchQuery;
import app.krista.extension.util.EntityStore;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TaskStore implements EntityStore<Task> {

    private static final String DUE_DATE = "Due Date";
    private static final String TITLE = "Title";
    private final TaskDaoImpl taskDao;
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskStore.class);


    @Inject
    public TaskStore(TaskDaoImpl taskDao) {
        this.taskDao = taskDao;
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
        try {
            List<Task> tasks;
            if (conditions.size() == 1) {
                tasks = taskDao.getTasks(pageSize, pageIndex).stream().map(TaskCatalogType::fromSFTask).collect(Collectors.toList());
            } else {
                String query = createQuery(pageIndex, pageSize, conditions);

                tasks = taskDao.getTasksByQuery(query).stream().map(TaskCatalogType::fromSFTask).collect(Collectors.toList());
            }
            return tasks;
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return List.of();
        }
    }

    private static String createQuery(long pageIndex, int pageSize, List<SearchCondition> conditions) {
        return TaskQueryBuilder.builder()
                .assignedToNameEquals(getStringConditionValue(conditions, "Assigned To Name", "equals"))
                .assignedToNameContains(getStringConditionValue(conditions, "Assigned To Name", "contains"))
                .priority(getStringConditionValue(conditions, "Priority"))
                .status(getStringConditionValue(conditions, "Status"))
                .titleEquals(getStringConditionValue(conditions, TITLE, "equals"))
                .titleContains(getStringConditionValue(conditions, TITLE, "contains"))
                .titleStartsWith(getStringConditionValue(conditions, TITLE, "startswith"))
                .titleEndsWith(getStringConditionValue(conditions, TITLE, "endswith"))
                .dueDateEquals(getLongConditionValue(conditions, DUE_DATE, "equals"))
                .dueDateLessThan(getLongConditionValue(conditions, DUE_DATE, "lt"))
                .dueDateGreaterThan(getLongConditionValue(conditions, DUE_DATE, "gt"))
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

    private static String getLongConditionValue(List<SearchCondition> conditions, String fieldName, String queryOperand) {
        for (SearchCondition con : conditions) {
            if (fieldName.equalsIgnoreCase(con.getFieldName()) && con.getOperator().toString().equalsIgnoreCase(queryOperand)) {
                if (con.getOperand() == null) {
                    return null;
                } else {
                    double timestamp = Double.parseDouble(String.valueOf(con.getOperand()));
                    long epoch = (long) timestamp;
                    Date date = new Date(epoch);
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    return dateFormat.format(date);
                }
            }
        }
        return null;
    }


}
