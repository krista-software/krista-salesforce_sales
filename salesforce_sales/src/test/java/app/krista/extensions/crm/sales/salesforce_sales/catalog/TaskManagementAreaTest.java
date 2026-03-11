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

package app.krista.extensions.crm.sales.salesforce_sales.catalog;

import app.krista.extensions.crm.sales.salesforce_sales.impl.TaskDaoImpl;
import app.krista.model.base.FreeForm;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TaskManagementAreaTest {

    private TaskDaoImpl taskDao;
    private TaskManagementArea area;

    @Before
    public void setUp() {
        taskDao = mock(TaskDaoImpl.class);
        area = new TaskManagementArea(taskDao);
    }

    @Test
    public void testGetTaskStatuses_Success() {
        when(taskDao.getTaskStatusByQuery()).thenReturn(List.of("Open", "Closed"));
        var result = area.getTaskStatuses();
        assertEquals(2, result.size());
        assertTrue(result.contains("Open"));
        verify(taskDao, times(1)).getTaskStatusByQuery();
    }

    @Test
    public void testTaskChangeAlert_HappyPath() {
        FreeForm freeForm = new FreeForm();
        freeForm.put("Id", "Text", "T1");
        freeForm.put("Title", "Text", "Call");
        freeForm.put("Priority", "Text", "High");
        freeForm.put("ActivityDate", "Long", 1700000000000L);
        freeForm.put("Description", "Text", "desc");
        freeForm.put("Status", "Text", "Open");
        freeForm.put("OwnerId", "Text", "U1");
        freeForm.put("WhatId", "Text", "O1");

        Map<String, Object> result = area.taskChangeAlert("Task Change Alert", freeForm);
        assertEquals("T1", result.get("Task Id"));
        assertEquals("High", result.get("Task Priority"));
    }

    @Test
    public void testTaskChangeAlert_WrongEventReturnsEmptyMap() {
        FreeForm freeForm = new FreeForm();
        Map<String, Object> result = area.taskChangeAlert("Other", freeForm);
        assertTrue(result.isEmpty());
    }
}

