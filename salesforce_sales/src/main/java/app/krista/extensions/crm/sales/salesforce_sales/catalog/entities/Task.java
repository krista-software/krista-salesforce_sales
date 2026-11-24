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

package app.krista.extensions.crm.sales.salesforce_sales.catalog.entities;

import app.krista.extension.impl.anno.*;

import java.util.LinkedHashMap;
import java.util.Map;

@Domain(id = "catEntryDomain_086f33da-aaea-4d0c-816f-3d5f20b4a273",
        name = "Sales",
        ecosystemId = "catEntryEcosystem_8a0e8ce7-6289-492f-9202-52284024280c",
        ecosystemName = "CRM",
        ecosystemVersion = "b0024c31-acfe-4171-8fb0-f04514155787")
@Entity(name = "Task", id = "localDomainEntity_2dcaec4e-e63f-413d-8c5b-dced4bac7097", primaryKey = "Id", supportStore = true, options = {},
        description = "Represents a task or to-do item in Salesforce. Assigned To Id references User.Id. Assigned To Name matches User.Name. Associated Entity Id references the related record (Account.Id, Lead.Id, Opportunity.Id, etc.).")
@Searchable
public class Task {

    @Field.Text(name = "Id", required = false, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {}, description = "Unique Salesforce identifier for the task.")
    public String id;

    @Searchable
    @ToString
    @Field.Text(name = "Title", required = true, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {}, description = "Subject or title of the task.")
    public String title;

    @Searchable
    @ToString
    @Field.PickOne(name = "Priority", values = {"Low", "Normal", "High"}, required = true, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {}, description = "Priority level of the task (High, Normal, Low).")
    public String priority;

    @Searchable
    @ToString
    @Field.Date(name = "Due Date", required = false, includeTimeOfDay = true, showHowManyDaysInViewer = 60, allowPast = true, allowToday = true, allowFuture = true, defaultTimeSpan = 1, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {}, description = "Due date for the task.")
    public Long dueDate;

    @ToString
    @Field(name = "Description", type = "Paragraph", required = false, attributes = {@Attribute(name = "visualWidth", value = "L")}, options = {}, description = "Detailed description of the task.")
    public String description;

    @Searchable
    @ToString
    @Field.Text(name = "Status", required = true, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {}, description = "Current status of the task.")
    public String status;

    @Field.Text(name = "Task Type", required = false, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {}, description = "Type of the task.")
    public String taskType;

    @Searchable
    @ToString
    @Field.Text(name = "Assigned To Name", required = false, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {}, description = "Display name of the assigned user. Matches User.Name.")
    public String assignedToName;

    @Field.Text(name = "Assigned To Id", required = true, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {}, description = "Salesforce User Id of the assigned user. References User.Id.")
    public String assignedToId;

    @Field.Text(name = "Associated Entity Id", required = false, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {}, description = "Id of the related record (Account, Lead, Opportunity, etc.).")
    public String associatedEntityId;

    @Field.Text(name = "Associated Entity Type", required = false, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {}, description = "Type of the related record.")
    public String associatedEntityType;

    @Field.Text(name = "Associated Entity Name", required = false, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {}, description = "Name of the related record.")
    public String associatedEntityName;

    /**
     * Converts this Task entity to a Map of field values.
     *
     * @return Map containing task field values in declaration order
     */
    public Map<String, Object> toFields() {
        Map<String, Object> fields = new LinkedHashMap<>();
        fields.put("Id", id);
        fields.put("Title", title);
        fields.put("Priority", priority);
        fields.put("Due Date", dueDate);
        fields.put("Description", description);
        fields.put("Status", status);
        fields.put("Task Type", taskType);
        fields.put("Assigned To Name", assignedToName);
        fields.put("Assigned To Id", assignedToId);
        fields.put("Associated Entity Id", associatedEntityId);
        fields.put("Associated Entity Type", associatedEntityType);
        fields.put("Associated Entity Name", associatedEntityName);
        return fields;
    }

}
