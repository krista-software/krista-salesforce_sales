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

package app.krista.extensions.crm.sales.salesforce_sales.impl;

import app.krista.extensions.crm.sales.salesforce_sales.model.SFQuote;
import app.krista.extensions.crm.sales.salesforce_sales.service.Quote;
import app.krista.extensions.crm.sales.salesforce_sales.util.DatePlus;

public class QuoteImpl implements Quote {

    private final SFQuote sfQuote;

    public QuoteImpl(SFQuote sfQuote) {
        this.sfQuote = sfQuote;
    }

    @Override
    public String getId() {
        return sfQuote.id;
    }

    @Override
    public String getName() {
        return sfQuote.name;
    }

    @Override
    public Long getCreatedDate() {
        return sfQuote.createdDate == null ? null : sfQuote.createdDate.getTime();
    }

    @Override
    public String getOpportunityId() {
        return sfQuote.opportunityId;
    }

    @Override
    public String getPricebookId() {
        return sfQuote.pricebookId;
    }

    @Override
    public String getContactId() {
        return sfQuote.contactId;
    }

    @Override
    public Double getShippingHandling() {
        return sfQuote.shippingHandling;
    }

    @Override
    public Double getTax() {
        return sfQuote.tax;
    }

    @Override
    public Long getExpirationDate() {
        return sfQuote.expirationDate == null ? null : DatePlus.fromYYYYMMDD(sfQuote.expirationDate).getTime();
    }

    @Override
    public Double getSubtotal() {
        return sfQuote.subtotal;
    }

    @Override
    public Double getLineItemCount() {
        return sfQuote.lineItemCount;
    }

    @Override
    public String getBillingName() {
        return sfQuote.billingName;
    }

    @Override
    public String getAccountId() {
        return sfQuote.accountId;
    }

    @Override
    public Double getQuoteNumber() {
        return sfQuote.quoteNumber;
    }

    @Override
    public String getStatus() {
        return sfQuote.status;
    }

    @Override
    public String getDescription() {
        return sfQuote.description;
    }

    @Override
    public Double getTotalPrice() {
        return sfQuote.totalPrice;
    }

    @Override
    public Double getDiscount() {
        return sfQuote.discount;
    }

    @Override
    public Double getGrandTotal() {
        return sfQuote.grandTotal;
    }
}
