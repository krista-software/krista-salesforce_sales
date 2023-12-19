package app.krista.extensions.crm.sales.salesforce_sales.impl;


import app.krista.extensions.crm.sales.salesforce_sales.model.SFTask;
import app.krista.extensions.crm.sales.salesforce_sales.service.Task;
import app.krista.extensions.crm.sales.salesforce_sales.util.DatePlus;

public class TaskImpl implements Task {

    private final SFTask sfTask;

    public TaskImpl(SFTask sfTask) {
        this.sfTask = sfTask;
    }

    @Override
    public String getId() {
        return sfTask.id;
    }

    @Override
    public String getOwnerId() {
        return sfTask.ownerId;
    }

    @Override
    public String getSubject() {
        return sfTask.subject;
    }

    @Override
    public String getPriority() {
        return sfTask.priority;
    }

    @Override
    public Long getActivityDate() {
        return sfTask.activityDate != null ? DatePlus.fromYYYYMMDD(sfTask.activityDate).getTime() : null;

    }

    @Override
    public String getDescription() {
        return sfTask.description;
    }

    @Override
    public String getStatus() {
        return sfTask.status;
    }

    @Override
    public String getWhatId() {
        return sfTask.whatId;
    }

    @Override
    public String getOwner() {
        return sfTask.Owner != null ? sfTask.Owner.Name : null;
    }

    @Override
    public String getAssociatedToName() {
        return sfTask.What != null ? sfTask.What.Name : null;
    }

    @Override
    public String getAssociatedToType() {
        return sfTask.What != null ? sfTask.What.Type : null;
    }

}