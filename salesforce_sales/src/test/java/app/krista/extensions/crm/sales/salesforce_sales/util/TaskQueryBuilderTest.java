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

package app.krista.extensions.crm.sales.salesforce_sales.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class TaskQueryBuilderTest {

    @Test
    public void testBuild_WithMultipleFilters() {
        String q = TaskQueryBuilder.builder()
                .assignedToNameEquals("John Doe")
                .priority("High")
                .status("In Progress")
                .titleContains("Follow up")
                .size(50)
                .page(1)
                .build();

        assertTrue(q.startsWith("Select "));
        assertTrue(q.contains(" from Task "));
        assertTrue(q.contains("Owner.Name = 'John Doe'"));
        assertTrue(q.contains("Priority = 'High'"));
        assertTrue(q.contains("Status = 'In Progress'"));
        assertTrue(q.contains("Subject LIKE '%Follow up%'"));
        assertTrue(q.endsWith(" LIMIT 50 OFFSET 0"));
    }

    @Test
    public void testBuild_NoFilters_TrimsWhere() {
        String q = TaskQueryBuilder.builder().size(10).page(3).build();
        assertFalse(q.contains("WHERE ORDER"));
        assertTrue(q.endsWith(" LIMIT 10 OFFSET 20"));
    }
}

