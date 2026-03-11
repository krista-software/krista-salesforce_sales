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
