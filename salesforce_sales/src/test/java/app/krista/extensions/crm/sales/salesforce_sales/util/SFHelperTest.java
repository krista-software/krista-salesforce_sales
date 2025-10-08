package app.krista.extensions.crm.sales.salesforce_sales.util;

import app.krista.extensions.crm.sales.salesforce_sales.SalesforceSalesAttributes;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class SFHelperTest {

    @Test
    public void testValidateUsingOAuth_AdminAlwaysTrue() {
        SalesforceSalesAttributes attrs = Mockito.mock(SalesforceSalesAttributes.class);
        assertTrue(SFHelper.validateUsingOAuth(true, attrs));
    }

    @Test
    public void testValidateUsingOAuth_MissingCredsUsesOAuth() {
        SalesforceSalesAttributes attrs = Mockito.mock(SalesforceSalesAttributes.class);
        when(attrs.getUserName()).thenReturn("");
        when(attrs.getPassword()).thenReturn("secret");
        when(attrs.getSecurityToken()).thenReturn("token");
        assertTrue(SFHelper.validateUsingOAuth(false, attrs));
    }

    @Test
    public void testValidateUsingOAuth_AllCredsPresent_NoOAuth() {
        SalesforceSalesAttributes attrs = Mockito.mock(SalesforceSalesAttributes.class);
        when(attrs.getUserName()).thenReturn("user");
        when(attrs.getPassword()).thenReturn("secret");
        when(attrs.getSecurityToken()).thenReturn("token");
        assertFalse(SFHelper.validateUsingOAuth(false, attrs));
    }
}

