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

import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Lead;

public final class LeadCatalogType {

    private LeadCatalogType() {
    }

    public static Lead fromSFLead(app.krista.extensions.crm.sales.salesforce_sales.service.Lead lead) {
        Lead newLead = new Lead();
        newLead.id = lead.getId();
        newLead.title = lead.getTitle();
        newLead.company = lead.getCompany();
        newLead.website = lead.getWebsite();
        newLead.ownerId = lead.getOwnerId();
        newLead.isConverted = lead.getIsConverted();
        newLead.convertedAccountId = lead.getConvertedAccountId();
        newLead.convertedContactId = lead.getConvertedContactId();
        newLead.convertedOpportunityId = lead.getConvertedOpportunityId();
        newLead.cleanStatus = lead.getCleanStatus();
        newLead.phone = lead.getPhone();
        newLead.mobilePhone = lead.getMobilePhone();
        newLead.email = lead.getEmail();
        newLead.description = lead.getDescription();
        newLead.status = lead.getStatus();
        newLead.leadSource = lead.getLeadSource();
        newLead.industry = lead.getIndustry();
        newLead.rating = lead.getRating();
        newLead.annualRevenue = lead.getAnnualRevenue();
        newLead.convertedDate = lead.getConvertedDate();
        newLead.createdDate = lead.getCreatedDate();
        newLead.lastModifiedDate = lead.getLastModifiedDate();
        newLead.name = lead.getName();
        return newLead;
    }
}
