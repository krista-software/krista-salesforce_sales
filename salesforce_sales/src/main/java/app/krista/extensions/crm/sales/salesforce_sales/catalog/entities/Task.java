package app.krista.extensions.crm.sales.salesforce_sales.catalog.entities;

import app.krista.extension.impl.anno.*;

@Domain(id = "catEntryDomain_086f33da-aaea-4d0c-816f-3d5f20b4a273",
        name = "Sales",
        ecosystemId = "catEntryEcosystem_8a0e8ce7-6289-492f-9202-52284024280c",
        ecosystemName = "CRM",
        ecosystemVersion = "b0024c31-acfe-4171-8fb0-f04514155787")
@Entity(name = "Task", id = "localDomainEntity_2dcaec4e-e63f-413d-8c5b-dced4bac7097", primaryKey = "Id", options = {})
public class Task {

    @Field.Text(name = "Id", required = false, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {})
    public String id;

    @Searchable
    @ToString
    @Field.Text(name = "Title", required = true, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {})
    public String title;

    @Searchable
    @ToString
    @Field.PickOne(name = "Priority", values = {"Low", "Normal", "High"}, required = true, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {})
    public String priority;

    @Searchable
    @ToString
    @Field.Date(name = "Due Date", required = false, includeTimeOfDay = true, showHowManyDaysInViewer = 60, allowPast = true, allowToday = true, allowFuture = true, defaultTimeSpan = 1, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {})
    public Long dueDate;

    @ToString
    @Field(name = "Description", type = "Paragraph", required = false, attributes = {@Attribute(name = "visualWidth", value = "L")}, options = {})
    public String description;

    @Searchable
    @ToString
    @Field.Text(name = "Status", required = true, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {})
    public String status;

    @Field.Text(name = "Task Type", required = false, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {})
    public String taskType;

    @Searchable
    @ToString
    @Field.Text(name = "Assigned To Name", required = false, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {})
    public String assignedToName;

    @Field.Text(name = "Assigned To Id", required = true, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {})
    public String assignedToId;

    @Field.Text(name = "Associated Entity Id", required = false, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {})
    public String associatedEntityId;

    @Field.Text(name = "Associated Entity Type", required = false, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {})
    public String associatedEntityType;

    @Field.Text(name = "Associated Entity Name", required = false, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {})
    public String associatedEntityName;

}