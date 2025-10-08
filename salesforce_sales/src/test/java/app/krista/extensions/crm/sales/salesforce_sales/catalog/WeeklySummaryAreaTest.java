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

