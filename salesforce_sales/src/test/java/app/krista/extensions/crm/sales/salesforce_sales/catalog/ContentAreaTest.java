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
import app.krista.extensions.crm.sales.salesforce_sales.impl.stores.ContentDaoImpl;
import app.krista.model.base.FreeForm;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ContentAreaTest {

    private SalesforceManager salesforceManager;
    private ContentDaoImpl contentDao;
    private ContentArea area;

    @Before
    public void setup() throws Exception {
        salesforceManager = mock(SalesforceManager.class);
        contentDao = mock(ContentDaoImpl.class);
        var ctor = ContentArea.class.getDeclaredConstructor(SalesforceManager.class, ContentDaoImpl.class);
        ctor.setAccessible(true);
        area = ctor.newInstance(salesforceManager, contentDao);
    }

    @Test
    public void testRetrieveCustomFieldsForGivenObject_HappyPath() {
        List<String> fields = Arrays.asList("Custom_Field__c", "Other__c");
        when(salesforceManager.getRecordByQuery(anyString())).thenReturn("{\"records\":[{\"Custom_Field__c\":\"X\",\"Other__c\":\"Y\"}]} ");
        FreeForm ff = new FreeForm();
        ff.put("Custom_Field__c", "Text", "X");
        ff.put("Other__c", "Text", "Y");
        when(contentDao.parseJSONToMap(anyString(), eq(fields))).thenReturn(ff);

        FreeForm res = area.retrieveCustomFieldsForGivenObject("ID1", "Account", fields);
        assertEquals("X", res.get("Custom_Field__c"));
        assertEquals("Y", res.get("Other__c"));
        verify(salesforceManager, times(1)).getRecordByQuery(contains("from Account"));
    }

    @Test
    public void testRetrieveCustomFieldsForGivenObject_NonCustomFieldReturnsEmpty() {
        List<String> fields = Arrays.asList("Name", "Custom__c");
        FreeForm res = area.retrieveCustomFieldsForGivenObject("ID1", "Lead", fields);
        assertTrue(res.isEmpty());
        verify(salesforceManager, never()).getRecordByQuery(anyString());
    }
}

