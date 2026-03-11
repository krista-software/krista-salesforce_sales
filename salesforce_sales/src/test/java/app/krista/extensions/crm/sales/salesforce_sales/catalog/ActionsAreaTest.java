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

