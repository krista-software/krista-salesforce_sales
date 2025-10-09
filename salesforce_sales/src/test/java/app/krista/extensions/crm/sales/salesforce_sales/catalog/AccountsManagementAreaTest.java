package app.krista.extensions.crm.sales.salesforce_sales.catalog;

import app.krista.extensions.crm.sales.salesforce_sales.impl.AccountDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.util.Constants;
import app.krista.model.base.FreeForm;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AccountsManagementAreaTest {

    private AccountDaoImpl accountDao;
    private AccountsManagementArea area;

    @Before
    public void setUp() throws Exception {
        accountDao = mock(AccountDaoImpl.class);
        var ctor = AccountsManagementArea.class.getDeclaredConstructor(AccountDaoImpl.class);
        ctor.setAccessible(true);
        area = ctor.newInstance(accountDao);
    }

    @Test
    public void testAccountChangeAlert_HappyPath() {
        FreeForm eventData = new FreeForm();
        eventData.put("Id", Constants.TEXT, "A1");
        eventData.put("Name", Constants.TEXT, "Acme");
        eventData.put("Fax", Constants.TEXT, "111-222");

        Map<String, Object> updated = new HashMap<>();
        updated.put("Website", "https://acme.com");
        updated.put("Name", "Acme");
        when(accountDao.getAccountUpdatedFields(anyString())).thenReturn(updated);

        Map<String, Object> res = area.accountChangeAlert("Account Change Alert", eventData);
        assertEquals("A1", res.get("Account Id"));
        assertEquals("Acme", res.get("Account Name"));
        assertEquals("https://acme.com", res.get("Account Website"));
        verify(accountDao, times(1)).getAccountUpdatedFields(contains("AccountId = 'A1'"));
    }

    @Test
    public void testAccountChangeAlert_WrongEvent_ReturnsEmptyMap() {
        FreeForm eventData = new FreeForm();
        Map<String, Object> res = area.accountChangeAlert("Other", eventData);
        assertTrue(res.isEmpty());
        verify(accountDao, never()).getAccountUpdatedFields(anyString());
    }
}

