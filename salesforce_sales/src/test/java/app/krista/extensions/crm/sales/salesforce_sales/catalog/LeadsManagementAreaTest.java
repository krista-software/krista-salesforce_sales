package app.krista.extensions.crm.sales.salesforce_sales.catalog;

import app.krista.extensions.crm.sales.salesforce_sales.impl.LeadDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.impl.LeadImpl;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFLead;
import app.krista.extensions.crm.sales.salesforce_sales.util.Constants;
import app.krista.model.base.FreeForm;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class LeadsManagementAreaTest {

    private LeadDaoImpl leadDao;
    private LeadsManagementArea area;

    @Before
    public void setUp() throws Exception {
        leadDao = mock(LeadDaoImpl.class);
        var ctor = LeadsManagementArea.class.getDeclaredConstructor(LeadDaoImpl.class);
        ctor.setAccessible(true);
        area = ctor.newInstance(leadDao);
    }

    @Test
    public void testGetLeads_ByUser() {
        String ownerId = "U1";
        List<app.krista.extensions.crm.sales.salesforce_sales.service.Lead> list = new ArrayList<>();
        SFLead lead = new SFLead();
        lead.id = "L1";
        lead.name = "John Doe";
        list.add(new LeadImpl(lead));
        when(leadDao.getLeadsByOwnerId(ownerId)).thenReturn(list);

        var res = area.getLeads("By User", ownerId, null);
        assertEquals(1, res.size());
        assertEquals("L1", res.get(0).id);
        assertEquals("John Doe", res.get(0).name);
    }

    @Test
    public void testGetLeads_NewLeads() {
        long start = System.currentTimeMillis() - 86400000L;
        List<app.krista.extensions.crm.sales.salesforce_sales.service.Lead> list = new ArrayList<>();
        SFLead lead = new SFLead();
        lead.id = "L2";
        lead.name = "Jane Doe";
        list.add(new LeadImpl(lead));
        when(leadDao.getLeadByDate(start)).thenReturn(list);

        var res = area.getLeads("New Leads", null, start);
        assertEquals(1, res.size());
        assertEquals("L2", res.get(0).id);
        assertEquals("Jane Doe", res.get(0).name);
    }

    @Test
    public void testLeadChangeAlert_HappyPath() {
        FreeForm eventData = new FreeForm();
        eventData.put("Id", Constants.TEXT, "L1");
        eventData.put("Name", Constants.TEXT, "John Doe");
        Map<String, Object> updated = new java.util.HashMap<>();
        updated.put("FirstName", "John");
        updated.put("LastName", "Doe");
        when(leadDao.getLeadsUpdatedFields(anyString())).thenReturn(updated);

        Map<String, Object> res = area.leadChangeAlert("Lead Change Alert", eventData);
        assertEquals("L1", res.get("Lead Id"));
        assertEquals("John Doe", res.get("Lead Name"));
    }

    @Test
    public void testGetLeads_InvalidField_ReturnsEmpty() {
        var res = area.getLeads("Other", null, null);
        assertTrue(res.isEmpty());
    }
}

