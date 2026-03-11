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
        task.description = sfTask.description != null ? sfTask.description : "";
        task.whatId = sfTask.associatedEntityId;
        task.status = sfTask.status;
        return task;
    }

}
