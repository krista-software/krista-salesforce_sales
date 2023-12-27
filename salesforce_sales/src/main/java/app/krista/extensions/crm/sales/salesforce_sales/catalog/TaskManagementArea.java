package app.krista.extensions.crm.sales.salesforce_sales.catalog;

import app.krista.extension.impl.anno.CatalogRequest;
import app.krista.extension.impl.anno.Domain;
import app.krista.extension.impl.anno.Field;
import app.krista.extensions.crm.sales.salesforce_sales.impl.TaskDaoImpl;
import app.krista.model.base.FreeForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static app.krista.extensions.crm.sales.salesforce_sales.util.SFHelper.upsertTaskDetails;

@Domain(id = "catEntryDomain_086f33da-aaea-4d0c-816f-3d5f20b4a273",
        name = "Sales",
        ecosystemId = "catEntryEcosystem_8a0e8ce7-6289-492f-9202-52284024280c",
        ecosystemName = "CRM",
        ecosystemVersion = "4dc0ebeb-d312-4169-99d7-17ff46cc0a6e")
public class TaskManagementArea {

    private final TaskDaoImpl taskDao;
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskManagementArea.class);

    @Inject
    public TaskManagementArea(TaskDaoImpl taskDao) {
        this.taskDao = taskDao;
    }

    @CatalogRequest(
            id = "localDomainRequest_a3941861-dc65-41a1-b324-224de3929cb9",
            name = "Get Task Status",
            description = "desc",
            area = "Task Management",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field.Desc(name = "Task Status", type = "[ Text ]", required = false)
    public List<String> getTaskStatus() {
        try {
            return taskDao.getTaskStatusByQuery();
        } catch (Exception cause) {
            LOGGER.error("Unable to get TaskStatus: " + cause.getMessage(), cause);
            return new ArrayList<>();
        }
    }

    @CatalogRequest(
            id = "localDomainRequest_13ca2a59-9785-4020-ae99-7c5c7dc2abf7",
            name = "Task Change Alert",
            description = "This request will alert the user whenever you create a lead or update assigned to,status and due date is overdue.",
            area = "Task Management",
            type = CatalogRequest.Type.WAIT_FOR_EVENT)
    @Field.Desc(name = "Task Response", type = "{ Task Id: Text, Task Title: Text, Task Priority: Text, Task Due Date: Date, Task Description: Text, Task Status: Text, Task Assigned To Id: Text, Task Associated Entity Id: Text }", required = false)
    public Map<String, Object> taskChangeAlert(
            @Field(name = "eventName", type = "Text") String eventName,
            @Field(name = "eventData", type = "FreeForm") FreeForm eventData) {
        Map<String, Object> taskMap = new HashMap<>();
        try {
            if (eventName.equals("Task Change Alert")) {
                taskMap.put("Task Id", eventData.get("Id"));
                upsertTaskDetails(eventData, taskMap);
                return taskMap;
            }
        } catch (IllegalArgumentException cause) {
            LOGGER.error("Unable to create alert for task change due to " + cause.getMessage(), cause);
        }
        return taskMap;
    }


}

