package app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes;

import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Task;

public final class TaskCatalogType {

    private TaskCatalogType() {
    }

    public static Task fromSFTask(app.krista.extensions.crm.sales.salesforce_sales.service.Task task) {
        Task newTask = new Task();
        newTask.id = task.getId();
        newTask.assignedToId = task.getOwnerId();
        newTask.assignedToName = task.getOwner();
        newTask.title = task.getSubject();
        newTask.priority = task.getPriority();
        newTask.dueDate = task.getActivityDate();
        newTask.status = task.getStatus();
        newTask.associatedEntityId = task.getWhatId();
        newTask.associatedEntityName = task.getAssociatedToName();
        newTask.associatedEntityType = task.getAssociatedToType();
        newTask.description = task.getDescription();
        return newTask;
    }
}
