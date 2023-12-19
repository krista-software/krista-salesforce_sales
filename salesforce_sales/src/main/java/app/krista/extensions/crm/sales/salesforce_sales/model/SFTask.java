package app.krista.extensions.crm.sales.salesforce_sales.model;

import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Task;
import app.krista.extensions.crm.sales.salesforce_sales.util.DatePlus;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public final class SFTask {
    @SerializedName("Id")
    public String id;
    @SerializedName("OwnerId")
    public String ownerId;
    @SerializedName("Subject")
    public String subject;
    @SerializedName("Priority")
    public String priority;
    @SerializedName("ActivityDate")
    public String activityDate;
    @SerializedName("Description")
    public String description;
    @SerializedName("Status")
    public String status;
    @SerializedName("WhatId")
    public String whatId;
    @SerializedName("WhoId")
    public String whoId;

    public static class AttributeClass {
        public String Name;
        public String Type;
    }

    public AttributeClass Owner;
    public AttributeClass What;

    public static SFTask from(Map<String, Object> entityAttributeField) {
        SFTask task = new SFTask();
        task.id = (String) entityAttributeField.get("Id");
        task.ownerId = (String) entityAttributeField.get("Assigned To Id");
        task.subject = (String) entityAttributeField.get("Title");
        task.priority = (String) entityAttributeField.get("Priority");
        task.description = (String) entityAttributeField.get("Description");
        task.whatId = (String) entityAttributeField.get("Associated Entity Id");
        task.status = (String) entityAttributeField.get("Status");
        Long activityDate = (Long) entityAttributeField.get("Due Date");
        task.activityDate = activityDate != null && activityDate > 0 ? DatePlus.getYYYYMMDD(activityDate) : null;
        return task;
    }

    public static SFTask from(Task sfTask) {
        SFTask task = new SFTask();
        task.id = sfTask.id;
        task.ownerId = sfTask.assignedToId;
        task.subject = sfTask.title;
        task.priority = sfTask.priority;
        task.activityDate = sfTask.dueDate != null && sfTask.dueDate > 0 ? DatePlus.getYYYYMMDD(sfTask.dueDate) : null;
        task.description = sfTask.description;
        task.whatId = sfTask.associatedEntityId;
        task.status = sfTask.status;
        return task;
    }

}
