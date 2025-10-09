package app.krista.extensions.crm.sales.salesforce_sales.catalog;

import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Opportunity;
import app.krista.extensions.crm.sales.salesforce_sales.impl.OpportunityDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFOpportunity;
import app.krista.extensions.crm.sales.salesforce_sales.impl.OpportunityImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OpportunityManagementAreaTest {

    private OpportunityDaoImpl opportunityDao;
    private OpportunityManagementArea area;

    @Before
    public void setUp() {
        opportunityDao = mock(OpportunityDaoImpl.class);
        app.krista.extensions.crm.sales.salesforce_sales.impl.UserDaoImpl userDao = mock(app.krista.extensions.crm.sales.salesforce_sales.impl.UserDaoImpl.class);
        try {
            var ctor = OpportunityManagementArea.class.getDeclaredConstructor(OpportunityDaoImpl.class, app.krista.extensions.crm.sales.salesforce_sales.impl.UserDaoImpl.class);
            ctor.setAccessible(true);
            area = ctor.newInstance(opportunityDao, userDao);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetOpportunities_BySoql() {
        String soql = "SELECT Id, Name FROM Opportunity WHERE Amount > 1000";
        SFOpportunity sfOpp = new SFOpportunity();
        sfOpp.id = "OPP1";
        sfOpp.name = "Big Deal";
        java.util.List<app.krista.extensions.crm.sales.salesforce_sales.service.Opportunity> svcList = new java.util.ArrayList<>();
        svcList.add(new OpportunityImpl(sfOpp));
        when(opportunityDao.getOpportunitiesByQuery(soql)).thenReturn(svcList);

        List<Opportunity> result = area.getOpportunities(soql, null);
        assertEquals(1, result.size());
        assertEquals("OPP1", result.get(0).id);
        assertEquals("Big Deal", result.get(0).name);
        verify(opportunityDao, times(1)).getOpportunitiesByQuery(soql);
    }

    @Test
    public void testGetOpportunities_ByLastDays() {
        Double lastDays = 5.0;
        SFOpportunity sfOpp = new SFOpportunity();
        sfOpp.id = "OPP2";
        sfOpp.name = "Recent Deal";
        java.util.List<app.krista.extensions.crm.sales.salesforce_sales.service.Opportunity> svcList = new java.util.ArrayList<>();
        svcList.add(new OpportunityImpl(sfOpp));
        when(opportunityDao.getNewOpportunitiesByLastDays(lastDays)).thenReturn(svcList);

        List<Opportunity> result = area.getOpportunities(null, lastDays);
        assertEquals(1, result.size());
        assertEquals("OPP2", result.get(0).id);
        verify(opportunityDao, times(1)).getNewOpportunitiesByLastDays(lastDays);
    }

    @Test
    public void testGetOpportunities_InvalidInputReturnsEmptyList() {
        List<Opportunity> result = area.getOpportunities(null, null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testUpdateOpportunityStatusByName_Success() {
        String name = "Deal";
        String status = "Closed Won";
        Boolean ok = area.updateOpportunityStatusByName(name, status);
        assertTrue(ok);
        verify(opportunityDao, times(1)).updateOpportunityStatus(name, status);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateOpportunityStatusByName_EmptyNameThrows() {
        area.updateOpportunityStatusByName("", "Closed Won");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateOpportunityStatusByName_EmptyStatusThrows() {
        area.updateOpportunityStatusByName("Deal", "");
    }
}

