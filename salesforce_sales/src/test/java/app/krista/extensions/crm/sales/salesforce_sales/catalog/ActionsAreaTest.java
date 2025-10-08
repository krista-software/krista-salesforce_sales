package app.krista.extensions.crm.sales.salesforce_sales.catalog;

import app.krista.extensions.crm.sales.salesforce_sales.impl.SalesforceManager;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ActionsAreaTest {

    private SalesforceManager manager;
    private ActionsArea area;

    @Before
    public void setUp() {
        manager = mock(SalesforceManager.class);
        area = new ActionsArea(manager);
    }

    @Test
    public void testSendAMail_Success() {
        Boolean res = area.sendAMail("a@b.com", "sub", "body");
        assertTrue(res);
        verify(manager, times(1)).sendEmail("a@b.com", "sub", "body");
    }

    @Test
    public void testSendVerificationMail_InsertsOtpWhenNotPresent() {
        String body = "Hello";
        String otp = area.sendVerificationMail("a@b.com", "sub", body);
        assertNotNull(otp);
        verify(manager, times(1)).sendEmail(eq("a@b.com"), eq("sub"), anyString());
    }

    @Test
    public void testSendVerificationMail_ReplacesOtpPlaceholder() {
        String otp = area.sendVerificationMail("a@b.com", "sub", "Use $otp here");
        assertNotNull(otp);
        verify(manager, times(1)).sendEmail(eq("a@b.com"), eq("sub"), contains(otp));
    }
}

