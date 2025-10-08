package app.krista.extensions.crm.sales.salesforce_sales.catalog;

import app.krista.extensions.crm.sales.salesforce_sales.impl.OpportunityDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.impl.SOQLDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.impl.SalesPipelineSummaryDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.util.Constants;
import app.krista.ksdk.context.AuthorizationContext;
import app.krista.ksdk.context.RequestContext;
import app.krista.model.base.FreeForm;
import app.krista.extensions.util.EventHandler;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReportingAreaTest {

    private OpportunityDaoImpl opportunityDao;
    private SalesPipelineSummaryDaoImpl salesPipelineSummaryDao;
    private SOQLDaoImpl soqlDao;
    private RequestContext requestContext;
    private AuthorizationContext authorizationContext;
    private EventHandler eventHandler;
    private ReportingArea area;

    @Before
    public void setUp() throws Exception {
        opportunityDao = mock(OpportunityDaoImpl.class);
        salesPipelineSummaryDao = mock(SalesPipelineSummaryDaoImpl.class);
        soqlDao = mock(SOQLDaoImpl.class);
        requestContext = mock(RequestContext.class);
        authorizationContext = mock(AuthorizationContext.class);
        eventHandler = mock(EventHandler.class);
        var ctor = ReportingArea.class.getDeclaredConstructor(OpportunityDaoImpl.class, SalesPipelineSummaryDaoImpl.class, SOQLDaoImpl.class, RequestContext.class, AuthorizationContext.class, EventHandler.class);
        ctor.setAccessible(true);
        area = ctor.newInstance(opportunityDao, salesPipelineSummaryDao, soqlDao, requestContext, authorizationContext, eventHandler);
        // Provide non-null SalesforceSalesAttributes for SFHelper.validateUsingOAuth
        app.krista.extensions.crm.sales.salesforce_sales.SalesforceSalesAttributes attrs = mock(app.krista.extensions.crm.sales.salesforce_sales.SalesforceSalesAttributes.class);
        when(attrs.getUserName()).thenReturn("u");
        when(attrs.getPassword()).thenReturn("p");
        when(attrs.getSecurityToken()).thenReturn("t");
        when(soqlDao.getSalesAttributes()).thenReturn(attrs);

    }

    @Test
    public void testGetOpportunitiesSummary() {
        when(opportunityDao.getOpportunitiesRegionWise()).thenReturn(List.of(Map.of("Region","East","Amount",100)));
        var res = area.getOpportunitiesSummary();
        assertEquals(1, res.size());
        assertEquals(100, res.get(0).get("Amount"));
    }

    @Test
    public void testProcessQueryRequest_ReturnsDaoResult() {
        when(soqlDao.getSalesforceObjectByQuery("SELECT 1")).thenReturn(Map.of("ok", true));
        var res = area.processQueryRequest("SELECT 1");
        assertEquals(true, ((Map<?,?>)res).get("ok"));
    }

    @Test
    public void testProcessQueryRequestAsync_EmitsEventAndReturnsJobId() {
        when(requestContext.invokeAsUser()).thenReturn(false);
        when(soqlDao.getSalesforceObjectByQuery(eq("SELECT Name FROM Account"), eq(false), isNull())).thenReturn("{\"records\":[{\"Name\":\"A\"}],\"totalSize\":1,\"done\":true}");
        String jobId = area.processQueryRequestAsync("SELECT Name FROM Account");
        assertNotNull(jobId);
        verify(eventHandler, timeout(3000)).handleEvent(eq(jobId), any(FreeForm.class));
    }

    @Test
    public void testGetResults_HappyPath() {
        String jobId = UUID.randomUUID().toString();
        FreeForm ff = new FreeForm();
        ff.put(Constants.DATA, "Text", "{\"records\":[{\"Name\":\"A\"}],\"totalSize\":1,\"done\":true}");
        Object res = area.getResults(jobId, ff, jobId);
        assertTrue(res instanceof java.util.Map);
        java.util.Map map = (java.util.Map) res;
        assertTrue(map.containsKey("Responses"));
        java.util.List responses = (java.util.List) map.get("Responses");
        assertEquals(1, responses.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetResults_InvalidJob_Throws() {
        area.getResults("jobA", new FreeForm(), "jobB");
    }

    @Test
    public void testYtdSalesRevenueClosed_WithOwnerId() {
        when(opportunityDao.getYTDClosedSumByOwnerId("U1")).thenReturn(List.of(Map.of("Total",100)));
        var res = area.yTDSalesRevenueClosed("U1");
        assertEquals(1, res.size());
    }

    @Test
    public void testSalesQuotaForTheQuarter() {
        when(opportunityDao.getCurrentQuarterQuota()).thenReturn(List.of(Map.of("Owner Name","A","Quota Amount",500)));
        var res = area.salesQuotaForTheQuarter();
        assertEquals(1, res.size());
        assertEquals(500, res.get(0).get("Quota Amount"));
    }

    @Test
    public void testValueOfOpportunitiesClosedForCurrentQuarter() {
        when(opportunityDao.getQuarterClosedSum("U1")).thenReturn(List.of(Map.of("Owner Id","U1","Total", "100")));
        var res = area.valueOfOpportunitiesClosedForCurrentQuarter("U1");
        assertEquals("U1", res.get(0).get("Owner Id"));
    }

    @Test
    public void testTotalPotentialOpportunityValueForQuarter() {
        when(opportunityDao.getTotalAmountClosedThisQuarter()).thenReturn(123.45);
        Double res = area.totalPotentialOpportunityValueForQuarter();
        assertEquals(Double.valueOf(123.45), res);
    }
}

