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

package app.krista.extensions.crm.sales.salesforce_sales.catalog.stores;

import app.krista.extension.executor.QueryClause;
import app.krista.extension.executor.SearchCondition;
import app.krista.extension.executor.SearchQuery;
import app.krista.extensions.crm.sales.salesforce_sales.SalesforceSalesAttributes;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.*;
import app.krista.extensions.crm.sales.salesforce_sales.impl.*;
import app.krista.extensions.crm.sales.salesforce_sales.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests the search(SearchQuery, ...) method across all stores
 * that were refactored to use SoqlQueryHelper.
 */
@RunWith(MockitoJUnitRunner.class)
public class StoreSearchTest {

    // ===== Account =====
    @Mock private AccountDaoImpl accountDao;
    @InjectMocks private AccountStore accountStore;

    // ===== Asset =====
    @Mock private AssetDaoImpl assetDao;
    @InjectMocks private AssetStore assetStore;

    // ===== Event =====
    @Mock private EventDaoImpl eventDao;
    @InjectMocks private EventStore eventStore;

    // ===== Lead =====
    @Mock private LeadDaoImpl leadDao;
    @InjectMocks private LeadStore leadStore;

    // ===== Opportunity =====
    @Mock private OpportunityDaoImpl opportunityDao;
    @Mock private SalesforceSalesAttributes salesforceSalesAttributes;
    @InjectMocks private OpportunityStore opportunityStore;

    // ===== Quote =====
    @Mock private QuoteDaoImpl quoteDao;
    @InjectMocks private QuoteStore quoteStore;

    // ===== Task =====
    @Mock private TaskDaoImpl taskDao;
    @InjectMocks private TaskStore taskStore;

    // ===== User =====
    @Mock private UserDaoImpl userDaoImpl;
    @InjectMocks private UserStore userStore;

    // ===== PricebookEntry =====
    @Mock private PriceBookEntryDaoImpl priceBookEntryDao;
    @InjectMocks private PricebookEntryStore pricebookEntryStore;

    // ---------- Helper ----------
    private SearchQuery queryWith(SearchCondition... conditions) {
        return new SearchQuery(List.of(conditions), Collections.emptyList());
    }

    private SearchCondition cond(String field, SearchCondition.Operator op, Object val) {
        return new SearchCondition(op, val, field);
    }

    // ===================== AccountStore =====================

    @Test
    public void accountSearch_NullQuery_ReturnsAll() throws IOException {
        SFAccount sf = new SFAccount();
        sf.name = "Test";
        AccountImpl impl = new AccountImpl(sf);
        when(accountDao.getAccounts(anyLong(), anyLong())).thenReturn(List.of(impl));

        List<Account> result = accountStore.search((SearchQuery) null, 1, 20);
        assertFalse(result.isEmpty());
    }

    @Test
    public void accountSearch_ById_ReturnsSingle() throws IOException {
        SFAccount sf = new SFAccount();
        sf.id = "001ABC";
        sf.name = "Test";
        AccountImpl impl = new AccountImpl(sf);
        when(accountDao.getAccountById("001ABC")).thenReturn(impl);

        SearchQuery sq = queryWith(cond("Id", SearchCondition.Operator.equals, "001ABC"));
        List<Account> result = accountStore.search(sq, 1, 20);
        assertEquals(1, result.size());
        assertEquals("001ABC", result.get(0).id);
    }

    @Test
    public void accountSearch_ByName_BuildsQuery() throws IOException {
        when(accountDao.getAccountByQuery(anyString())).thenReturn(Collections.emptyList());

        SearchQuery sq = queryWith(cond("Name", SearchCondition.Operator.contains, "Acme"));
        List<Account> result = accountStore.search(sq, 1, 20);
        assertNotNull(result);
        verify(accountDao).getAccountByQuery(contains("Name LIKE '%Acme%'"));
    }

    @Test
    public void accountSearch_UnmappedField_ReturnsAll() throws IOException {
        SFAccount sf = new SFAccount();
        sf.name = "Fallback";
        when(accountDao.getAccounts(anyLong(), anyLong())).thenReturn(List.of(new AccountImpl(sf)));

        SearchQuery sq = queryWith(cond("unknownField", SearchCondition.Operator.equals, "val"));
        List<Account> result = accountStore.search(sq, 1, 20);
        assertFalse(result.isEmpty());
    }

    // ===================== AssetStore =====================

    @Test
    public void assetSearch_NullQuery_ReturnsAll() throws IOException {
        SFAsset sf = new SFAsset();
        sf.name = "Asset1";
        when(assetDao.getAssets(anyLong(), anyLong())).thenReturn(List.of(new AssetImpl(sf)));

        List<Asset> result = assetStore.search((SearchQuery) null, 1, 20);
        assertFalse(result.isEmpty());
    }

    @Test
    public void assetSearch_ById_ReturnsSingle() throws IOException {
        SFAsset sf = new SFAsset();
        sf.id = "02iAST";
        sf.name = "Server";
        when(assetDao.getAssetById("02iAST")).thenReturn(new AssetImpl(sf));

        SearchQuery sq = queryWith(cond("Id", SearchCondition.Operator.equals, "02iAST"));
        List<Asset> result = assetStore.search(sq, 1, 20);
        assertEquals(1, result.size());
    }

    @Test
    public void assetSearch_ByName_BuildsQuery() throws IOException {
        when(assetDao.getAssetByQuery(anyString())).thenReturn(Collections.emptyList());

        SearchQuery sq = queryWith(cond("Name", SearchCondition.Operator.contains, "Laptop"));
        List<Asset> result = assetStore.search(sq, 1, 20);
        assertNotNull(result);
        verify(assetDao).getAssetByQuery(contains("Name LIKE '%Laptop%'"));
    }

    // ===================== EventStore =====================

    @Test
    public void eventSearch_NullQuery_ReturnsAll() throws IOException {
        SFEvent sf = new SFEvent();
        sf.subject = "Meeting";
        when(eventDao.getEvents(anyLong(), anyLong())).thenReturn(List.of(new EventImpl(sf)));

        List<Event> result = eventStore.search((SearchQuery) null, 1, 20);
        assertFalse(result.isEmpty());
    }

    @Test
    public void eventSearch_ById_ReturnsSingle() throws IOException {
        SFEvent sf = new SFEvent();
        sf.id = "00UEVT";
        sf.subject = "Call";
        when(eventDao.getEventById("00UEVT")).thenReturn(new EventImpl(sf));

        SearchQuery sq = queryWith(cond("Id", SearchCondition.Operator.equals, "00UEVT"));
        List<Event> result = eventStore.search(sq, 1, 20);
        assertEquals(1, result.size());
    }

    @Test
    public void eventSearch_BySubject_BuildsQuery() throws IOException {
        when(eventDao.getEventByQuery(anyString())).thenReturn(Collections.emptyList());

        SearchQuery sq = queryWith(cond("Subject", SearchCondition.Operator.contains, "Demo"));
        List<Event> result = eventStore.search(sq, 1, 20);
        assertNotNull(result);
        verify(eventDao).getEventByQuery(contains("Subject LIKE '%Demo%'"));
    }

    // ===================== LeadStore =====================

    @Test
    public void leadSearch_NullQuery_ReturnsAll() throws IOException {
        SFLead sf = new SFLead();
        sf.name = "Lead1";
        when(leadDao.getLeads(anyLong(), anyLong())).thenReturn(List.of(new LeadImpl(sf)));

        List<Lead> result = leadStore.search((SearchQuery) null, 1, 20);
        assertFalse(result.isEmpty());
    }

    @Test
    public void leadSearch_ById_ReturnsSingle() throws IOException {
        SFLead sf = new SFLead();
        sf.id = "00QLEAD";
        sf.name = "John";
        when(leadDao.getLeadById("00QLEAD")).thenReturn(new LeadImpl(sf));

        SearchQuery sq = queryWith(cond("Id", SearchCondition.Operator.equals, "00QLEAD"));
        List<Lead> result = leadStore.search(sq, 1, 20);
        assertEquals(1, result.size());
    }

    @Test
    public void leadSearch_ByName_BuildsQuery() throws IOException {
        when(leadDao.getLeadsByQuery(anyString())).thenReturn(Collections.emptyList());

        SearchQuery sq = queryWith(cond("Name", SearchCondition.Operator.contains, "Smith"));
        List<Lead> result = leadStore.search(sq, 1, 20);
        assertNotNull(result);
        verify(leadDao).getLeadsByQuery(contains("Name LIKE '%Smith%'"));
    }

    // ===================== OpportunityStore =====================

    @Test
    public void oppSearch_NullQuery_ReturnsAll() throws IOException {
        SFOpportunity sf = new SFOpportunity();
        sf.name = "Deal";
        when(opportunityDao.getOpportunities(anyLong(), anyLong())).thenReturn(List.of(new OpportunityImpl(sf)));

        List<Opportunity> result = opportunityStore.search((SearchQuery) null, 1, 20);
        assertFalse(result.isEmpty());
    }

    @Test
    public void oppSearch_ById_ReturnsSingle() throws IOException {
        SFOpportunity sf = new SFOpportunity();
        sf.id = "006OPP";
        sf.name = "Big Deal";
        when(opportunityDao.getOpportunityById("006OPP")).thenReturn(new OpportunityImpl(sf));

        SearchQuery sq = queryWith(cond("Id", SearchCondition.Operator.equals, "006OPP"));
        List<Opportunity> result = opportunityStore.search(sq, 1, 20);
        assertEquals(1, result.size());
    }

    @Test
    public void oppSearch_ByName_BuildsQuery() throws IOException {
        when(salesforceSalesAttributes.getTimezone()).thenReturn("UTC");
        when(opportunityDao.getOpportunitiesByQuery(anyString())).thenReturn(Collections.emptyList());

        SearchQuery sq = queryWith(cond("Name", SearchCondition.Operator.contains, "Enterprise"));
        List<Opportunity> result = opportunityStore.search(sq, 1, 20);
        assertNotNull(result);
        verify(opportunityDao).getOpportunitiesByQuery(contains("Name LIKE '%Enterprise%'"));
    }

    // ===================== QuoteStore =====================

    @Test
    public void quoteSearch_NullQuery_ReturnsAll() throws IOException {
        SFQuote sf = new SFQuote();
        sf.name = "Q-001";
        when(quoteDao.getQuotes(anyLong(), anyLong())).thenReturn(List.of(new QuoteImpl(sf)));

        List<Quote> result = quoteStore.search((SearchQuery) null, 1, 20);
        assertFalse(result.isEmpty());
    }

    @Test
    public void quoteSearch_ById_ReturnsSingle() throws IOException {
        SFQuote sf = new SFQuote();
        sf.id = "0Q0QUO";
        sf.name = "Q-001";
        when(quoteDao.getQuoteById("0Q0QUO")).thenReturn(new QuoteImpl(sf));

        SearchQuery sq = queryWith(cond("Id", SearchCondition.Operator.equals, "0Q0QUO"));
        List<Quote> result = quoteStore.search(sq, 1, 20);
        assertEquals(1, result.size());
    }

    @Test
    public void quoteSearch_ByName_BuildsQuery() throws IOException {
        when(quoteDao.getQuotesByQuery(anyString())).thenReturn(Collections.emptyList());

        SearchQuery sq = queryWith(cond("Name", SearchCondition.Operator.contains, "Quote"));
        List<Quote> result = quoteStore.search(sq, 1, 20);
        assertNotNull(result);
        verify(quoteDao).getQuotesByQuery(contains("Name LIKE '%Quote%'"));
    }

    // ===================== TaskStore =====================

    @Test
    public void taskSearch_NullQuery_ReturnsAll() throws IOException {
        SFTask sf = new SFTask();
        sf.subject = "Follow up";
        when(taskDao.getTasks(anyLong(), anyLong())).thenReturn(List.of(new TaskImpl(sf)));

        List<Task> result = taskStore.search((SearchQuery) null, 1, 20);
        assertFalse(result.isEmpty());
    }

    @Test
    public void taskSearch_ById_ReturnsSingle() throws IOException {
        SFTask sf = new SFTask();
        sf.id = "00TTASK";
        sf.subject = "Call";
        when(taskDao.getTaskById("00TTASK")).thenReturn(new TaskImpl(sf));

        SearchQuery sq = queryWith(cond("Id", SearchCondition.Operator.equals, "00TTASK"));
        List<Task> result = taskStore.search(sq, 1, 20);
        assertEquals(1, result.size());
    }

    @Test
    public void taskSearch_ByTitle_BuildsQuery() throws IOException {
        when(salesforceSalesAttributes.getTimezone()).thenReturn("UTC");
        when(taskDao.getTasksByQuery(anyString())).thenReturn(Collections.emptyList());

        SearchQuery sq = queryWith(cond("Title", SearchCondition.Operator.contains, "Follow"));
        List<Task> result = taskStore.search(sq, 1, 20);
        assertNotNull(result);
        verify(taskDao).getTasksByQuery(contains("Subject LIKE '%Follow%'"));
    }

    // ===================== UserStore =====================

    @Test
    public void userSearch_NullQuery_ReturnsAll() throws IOException {
        SFUser sf = new SFUser();
        sf.Name = "John";
        when(userDaoImpl.getUsers(anyLong(), anyLong())).thenReturn(List.of(new UserImpl(sf)));

        List<User> result = userStore.search((SearchQuery) null, 1, 20);
        assertFalse(result.isEmpty());
    }

    @Test
    public void userSearch_ById_ReturnsSingle() throws IOException {
        SFUser sf = new SFUser();
        sf.Id = "005USR";
        sf.Name = "John";
        when(userDaoImpl.getUserById("005USR")).thenReturn(new UserImpl(sf));

        SearchQuery sq = queryWith(cond("Id", SearchCondition.Operator.equals, "005USR"));
        List<User> result = userStore.search(sq, 1, 20);
        assertEquals(1, result.size());
    }

    @Test
    public void userSearch_ByName_BuildsQuery() throws IOException {
        when(userDaoImpl.getUsersByQuery(anyString())).thenReturn(Collections.emptyList());

        SearchQuery sq = queryWith(cond("Name", SearchCondition.Operator.contains, "Doe"));
        List<User> result = userStore.search(sq, 1, 20);
        assertNotNull(result);
        verify(userDaoImpl).getUsersByQuery(contains("Name LIKE '%Doe%'"));
    }

    // ===================== PricebookEntryStore =====================

    @Test
    public void pbeSearch_NullQuery_ReturnsAll() throws IOException {
        SFPricebookEntry sf = new SFPricebookEntry();
        sf.name = "Entry";
        when(priceBookEntryDao.getPriceBookEntries(anyLong(), anyLong())).thenReturn(List.of(new PriceBookEntryImpl(sf)));

        List<PricebookEntry> result = pricebookEntryStore.search((SearchQuery) null, 1, 20);
        assertFalse(result.isEmpty());
    }

    @Test
    public void pbeSearch_ById_ReturnsSingle() throws IOException {
        SFPricebookEntry sf = new SFPricebookEntry();
        sf.id = "01uPBE";
        sf.name = "Entry";
        when(priceBookEntryDao.getPriceBookEntryById("01uPBE")).thenReturn(new PriceBookEntryImpl(sf));

        SearchQuery sq = queryWith(cond("Id", SearchCondition.Operator.equals, "01uPBE"));
        List<PricebookEntry> result = pricebookEntryStore.search(sq, 1, 20);
        assertEquals(1, result.size());
    }

    @Test
    public void pbeSearch_ByName_BuildsQuery() throws IOException {
        when(priceBookEntryDao.getPriceBookEntryByQuery(anyString())).thenReturn(Collections.emptyList());

        SearchQuery sq = queryWith(cond("Name", SearchCondition.Operator.contains, "Standard"));
        List<PricebookEntry> result = pricebookEntryStore.search(sq, 1, 20);
        assertNotNull(result);
        verify(priceBookEntryDao).getPriceBookEntryByQuery(contains("Name LIKE '%Standard%'"));
    }

    // ===================== Exception handling =====================

    @Test
    public void accountSearch_DaoThrows_ReturnsEmpty() throws IOException {
        when(accountDao.getAccountByQuery(anyString())).thenThrow(new RuntimeException("DB error"));

        SearchQuery sq = queryWith(cond("Name", SearchCondition.Operator.contains, "Fail"));
        List<Account> result = accountStore.search(sq, 1, 20);
        assertTrue(result.isEmpty());
    }

    @Test
    public void leadSearch_DaoThrows_ReturnsEmpty() throws IOException {
        when(leadDao.getLeadsByQuery(anyString())).thenThrow(new RuntimeException("DB error"));

        SearchQuery sq = queryWith(cond("Name", SearchCondition.Operator.contains, "Fail"));
        List<Lead> result = leadStore.search(sq, 1, 20);
        assertTrue(result.isEmpty());
    }
}
