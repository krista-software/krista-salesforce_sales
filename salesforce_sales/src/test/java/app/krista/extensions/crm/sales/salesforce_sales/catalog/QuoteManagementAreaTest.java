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

import app.krista.extensions.crm.sales.salesforce_sales.impl.PriceBookEntryDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.impl.QuoteDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.impl.QuoteImpl;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFQuote;
import app.krista.model.base.FreeForm;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class QuoteManagementAreaTest {

    private QuoteDaoImpl quoteDao;
    private PriceBookEntryDaoImpl priceBookEntryDao;
    private QuoteManagementArea area;

    @Before
    public void setUp() throws Exception {
        quoteDao = mock(QuoteDaoImpl.class);
        priceBookEntryDao = mock(PriceBookEntryDaoImpl.class);
        var ctor = QuoteManagementArea.class.getDeclaredConstructor(QuoteDaoImpl.class, PriceBookEntryDaoImpl.class);
        ctor.setAccessible(true);
        area = ctor.newInstance(quoteDao, priceBookEntryDao);
    }

    @Test
    public void testGetAllQuoteStatus() {
        when(quoteDao.getQuoteStatuses()).thenReturn(List.of("Draft", "Approved"));
        List<String> res = area.getAllQuoteStatus();
        assertEquals(2, res.size());
        assertTrue(res.contains("Draft"));
    }

    @Test
    public void testUpdateQuoteByQuoteId_HappyPath() {
        String quoteId = "Q1";
        List<Object> fields = new ArrayList<>();
        Map<String, Object> fieldMap = new HashMap<>();
        fieldMap.put("Status", "Approved");
        // The area builds SFQuote.from(fieldMap) and calls update, then getQuoteById
        SFQuote sfQuote = new SFQuote();
        sfQuote.id = quoteId;
        when(quoteDao.getQuoteById(quoteId)).thenReturn(new QuoteImpl(sfQuote));

        var updated = area.updateQuoteByQuoteId(quoteId, fields); // fields are only used via CatalogTypes, we don't need details here
        assertNotNull(updated);
        verify(quoteDao, times(1)).updateQuote(any());
        verify(quoteDao, times(1)).getQuoteById(quoteId);
    }

    @Test
    public void testUpdateQuoteStatusById() {
        String quoteId = "Q2";
        SFQuote sf = new SFQuote();
        sf.id = quoteId;
        when(quoteDao.getQuoteById(quoteId)).thenReturn(new QuoteImpl(sf));
        String res = area.updateQuoteStatusById(quoteId, "Sent");
        assertEquals("Sent", res);
        verify(quoteDao, times(1)).updateQuote(any());
    }

    @Test
    public void testGetTheLatestQuoteFromOpportunity() {
        SFQuote sf = new SFQuote();
        sf.id = "Q3";
        when(quoteDao.getQuoteByOpportunityId("OPP1")).thenReturn(new QuoteImpl(sf));
        var res = area.getTheLatestQuoteFromOpportunity("OPP1");
        assertNotNull(res);
        assertEquals("Q3", res.id);
    }

    @Test
    public void testAddProductToAQuote_CallsDaoAndReturnsItems() {
        when(quoteDao.getQuoteById("Q4")).thenReturn(new QuoteImpl(new SFQuote()));
        when(priceBookEntryDao.getPriceBookEntryById("PB1")).thenReturn(mock(app.krista.extensions.crm.sales.salesforce_sales.service.PriceBookEntry.class));
        when(quoteDao.getQuoteLineItems("Q4")).thenReturn(List.of(Map.of("LineNumber", 1)));
        var res = area.addProductToAQuote("Q4", "PB1", 2.0, 10.0);
        assertEquals(1, res.size());
        verify(quoteDao, times(1)).addProductToQuote(any(), any(), any());
    }

    @Test
    public void testGetQuoteDetails() {
        FreeForm ff = new FreeForm();
        ff.put("QuoteId", "Text", "Q1");
        when(quoteDao.getQuoteMapById("Q1")).thenReturn(ff);
        var res = area.getQuoteDetails("Q1");
        assertEquals("Q1", res.get("QuoteId"));
    }

    @Test
    public void testGetQuoteLineItemByQuoteId() {
        when(quoteDao.getQuoteLineItems("Q5", null)).thenReturn(List.of(Map.of("LineNumber", 1)));
        var res = area.getQuoteLineItemByQuoteId("Q5", null);
        assertEquals(1, res.size());
    }
}

