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

import app.krista.extensions.crm.sales.salesforce_sales.model.SFOpportunity;
import app.krista.extensions.crm.sales.salesforce_sales.service.Opportunity;
import app.krista.extensions.crm.sales.salesforce_sales.util.DatePlus;

public class OpportunityImpl implements Opportunity {

    private final SFOpportunity sfOpportunity;

    public OpportunityImpl(SFOpportunity sfOpportunity) {
        this.sfOpportunity = sfOpportunity;
    }

    @Override
    public String getName() {
        return sfOpportunity.name;
    }

    @Override
    public String getId() {
        return sfOpportunity.id;
    }

    @Override
    public String getContactId() {
        return sfOpportunity.contactId;
    }

    @Override
    public String getOwnerId() {
        return sfOpportunity.ownerId;
    }

    @Override
    public String getOwnerName() {
        return sfOpportunity.Owner != null ? sfOpportunity.Owner.Name : null;
    }

    @Override
    public String getAccountId() {
        return sfOpportunity.accountId;
    }

    @Override
    public String getAccountName() {
        return sfOpportunity.Account != null ? sfOpportunity.Account.Name : null;
    }

    @Override
    public String getType() {
        return sfOpportunity.type;
    }

    @Override
    public String getLeadSource() {
        return sfOpportunity.leadSource;
    }

    @Override
    public String getPricebookId() {
        return sfOpportunity.Pricebook2Id;
    }

    @Override
    public String getStageName() {
        return sfOpportunity.stageName;
    }

    @Override
    public String getNextStep() {
        return sfOpportunity.nextStep;
    }

    @Override
    public Double getAmount() {
        return sfOpportunity.amount;
    }

    @Override
    public Double getAcv() {
        return sfOpportunity.acv;
    }

    @Override
    public String getPartner() {
        return sfOpportunity.partner;
    }

    @Override
    public String getCompetition() {
        return sfOpportunity.competition;
    }

    @Override
    public Double getProbability() {
        return sfOpportunity.probability;
    }

    @Override
    public String getDescription() {
        return sfOpportunity.description;
    }

    @Override
    public Double getExpectedRevenue() {
        return sfOpportunity.expectedRevenue;
    }

    @Override
    public Long getCreatedDate() {
        return sfOpportunity.createdDate != null ? sfOpportunity.createdDate.getTime() : null;
    }

    @Override
    public Long getLastModifiedDate() {
        return sfOpportunity.lastModifiedDate != null ? sfOpportunity.lastModifiedDate.getTime() : null;
    }

    @Override
    public Long getCloseDate() {
        return sfOpportunity.closeDate != null ? DatePlus.fromYYYYMMDD(sfOpportunity.closeDate).getTime() : null;
    }
}
