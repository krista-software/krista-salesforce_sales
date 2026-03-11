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