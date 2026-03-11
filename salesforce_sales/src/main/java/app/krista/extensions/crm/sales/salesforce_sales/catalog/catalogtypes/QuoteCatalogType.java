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

package app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes;

import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Quote;

public final class QuoteCatalogType {

    private QuoteCatalogType() {
    }

    public static Quote fromSFQuote(app.krista.extensions.crm.sales.salesforce_sales.service.Quote quote) {
        Quote newQuote = new Quote();
        newQuote.id = quote.getId();
        newQuote.name = quote.getName();
        newQuote.createdDate = quote.getCreatedDate();
        newQuote.opportunityId = quote.getOpportunityId();
        newQuote.pricebookId = quote.getPricebookId();
        newQuote.contactId = quote.getContactId();
        newQuote.shippingHandling = quote.getShippingHandling();
        newQuote.tax = quote.getTax();
        newQuote.expirationDate = quote.getExpirationDate();
        newQuote.subtotal = quote.getSubtotal();
        newQuote.lineItemCount = quote.getLineItemCount();
        newQuote.billingName = quote.getBillingName();
        newQuote.accountId = quote.getAccountId();
        newQuote.quoteNumber = quote.getQuoteNumber();
        newQuote.status = quote.getStatus();
        newQuote.description = quote.getDescription();
        newQuote.totalPrice = quote.getTotalPrice();
        newQuote.discount = quote.getDiscount();
        newQuote.grandTotal = quote.getGrandTotal();
        return newQuote;
    }
}
