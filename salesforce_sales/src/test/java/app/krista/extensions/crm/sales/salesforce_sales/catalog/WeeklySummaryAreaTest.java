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

import app.krista.extensions.crm.sales.salesforce_sales.impl.OpportunityDaoImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class WeeklySummaryAreaTest {

    private OpportunityDaoImpl opportunityDao;
    private WeeklySummaryArea area;

    @Before
    public void setUp() {
        opportunityDao = mock(OpportunityDaoImpl.class);
        area = new WeeklySummaryArea(opportunityDao);
    }

    @Test
    public void testGetRevenueClosedInCurrentQuarter_Success() {
        when(opportunityDao.getRevenue(true)).thenReturn(12345.0);
        Double res = area.getRevenueClosedInCurrentQuarter();
        assertEquals(Double.valueOf(12345.0), res);
        verify(opportunityDao, times(1)).getRevenue(true);
    }

    @Test
    public void testGetRevenueExpectedToCloseInCurrentQuarter_Success() {
        when(opportunityDao.getRevenue(false)).thenReturn(54321.0);
        Double res = area.getRevenueExpectedToCloseInCurrentQuarter();
        assertEquals(Double.valueOf(54321.0), res);
        verify(opportunityDao, times(1)).getRevenue(false);
    }
}

