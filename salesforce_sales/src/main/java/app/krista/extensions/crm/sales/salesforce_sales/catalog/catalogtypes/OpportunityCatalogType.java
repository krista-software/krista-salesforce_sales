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

import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Opportunity;

public final class OpportunityCatalogType {

    private OpportunityCatalogType() {
    }

    public static Opportunity fromSFOpportunity(app.krista.extensions.crm.sales.salesforce_sales.service.Opportunity opportunity) {
        Opportunity newOpportunity = new Opportunity();
        newOpportunity.name = opportunity.getName();
        newOpportunity.id = opportunity.getId();
        newOpportunity.contactId = opportunity.getContactId();
        newOpportunity.ownerId = opportunity.getOwnerId();
        newOpportunity.ownerName = opportunity.getOwnerName();
        newOpportunity.accountId = opportunity.getAccountId();
        newOpportunity.accountName = opportunity.getAccountName();
        newOpportunity.type = opportunity.getType();
        newOpportunity.leadSource = opportunity.getLeadSource();
        newOpportunity.pricebookId = opportunity.getPricebookId();
        newOpportunity.stageName = opportunity.getStageName();
        newOpportunity.nextStep = opportunity.getNextStep();
        newOpportunity.amount = opportunity.getAmount();
        newOpportunity.acv = opportunity.getAcv();
        newOpportunity.partner = opportunity.getPartner();
        newOpportunity.competition = opportunity.getCompetition();
        newOpportunity.probability = opportunity.getProbability();
        newOpportunity.description = opportunity.getDescription();
        newOpportunity.expectedRevenue = opportunity.getExpectedRevenue();
        newOpportunity.lastModifiedDate = opportunity.getLastModifiedDate();
        newOpportunity.createdDate = opportunity.getCreatedDate();
        newOpportunity.closeDate = opportunity.getCloseDate();
        return newOpportunity;
    }

}
