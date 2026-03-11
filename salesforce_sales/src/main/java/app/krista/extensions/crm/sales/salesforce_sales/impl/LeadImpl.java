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

import app.krista.extensions.crm.sales.salesforce_sales.model.SFLead;
import app.krista.extensions.crm.sales.salesforce_sales.service.Lead;
import app.krista.extensions.crm.sales.salesforce_sales.util.DatePlus;
import app.krista.model.base.PhoneNumber;

import javax.inject.Inject;

public class LeadImpl implements Lead {

    private final SFLead sfLead;

    @Inject
    public LeadImpl(SFLead sfLead) {
        this.sfLead = sfLead;
    }

    @Override
    public String getId() {
        return sfLead.id;
    }

    @Override
    public String getTitle() {
        return sfLead.title;
    }

    @Override
    public String getCompany() {
        return sfLead.company;
    }

    @Override
    public String getWebsite() {
        return sfLead.website;
    }

    @Override
    public String getOwnerId() {
        return sfLead.ownerId;
    }

    @Override
    public Boolean getIsConverted() {
        return sfLead.isConverted;
    }

    @Override
    public String getConvertedAccountId() {
        return sfLead.convertedAccountId;
    }

    @Override
    public String getConvertedContactId() {
        return sfLead.convertedContactId;
    }

    @Override
    public String getConvertedOpportunityId() {
        return sfLead.convertedOpportunityId;
    }

    @Override
    public String getCleanStatus() {
        return sfLead.cleanStatus;
    }

    @Override
    public PhoneNumber getPhone() {
        return app.krista.extensions.crm.sales.salesforce_sales.model.PhoneNumber.parsePhoneNumber(sfLead.phone);
    }

    @Override
    public PhoneNumber getMobilePhone() {
        return app.krista.extensions.crm.sales.salesforce_sales.model.PhoneNumber.parsePhoneNumber(sfLead.mobilePhone);
    }

    @Override
    public String getEmail() {
        return sfLead.email;
    }

    @Override
    public String getDescription() {
        return sfLead.description;
    }

    @Override
    public String getStatus() {
        return sfLead.status;
    }

    @Override
    public String getLeadSource() {
        return sfLead.leadSource;
    }

    @Override
    public String getIndustry() {
        return sfLead.industry;
    }

    @Override
    public String getRating() {
        return sfLead.rating;
    }

    @Override
    public Double getAnnualRevenue() {
        return sfLead.annualRevenue;
    }

    @Override
    public Long getConvertedDate() {
        return sfLead.convertedDate != null ? DatePlus.fromYYYYMMDD(sfLead.convertedDate).getTime() : null;
    }

    @Override
    public Long getCreatedDate() {
        return sfLead.createdDate != null ? sfLead.createdDate.getTime() : null;
    }

    @Override
    public Long getLastModifiedDate() {
        return sfLead.lastModifiedDate != null ? sfLead.lastModifiedDate.getTime() : null;
    }

    @Override
    public String getName() {
        return sfLead.name;
    }
}
