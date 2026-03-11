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

package app.krista.extensions.crm.sales.salesforce_sales.service;

import app.krista.model.base.PhoneNumber;

public interface Lead {
    String getId();

    String getTitle();

    String getCompany();

    String getWebsite();

    String getOwnerId();

    Boolean getIsConverted();

    String getConvertedAccountId();

    String getConvertedContactId();

    String getConvertedOpportunityId();

    String getCleanStatus();

    PhoneNumber getPhone();

    PhoneNumber getMobilePhone();

    String getEmail();

    String getDescription();

    String getStatus();

    String getLeadSource();

    String getIndustry();

    String getRating();

    Double getAnnualRevenue();

    Long getConvertedDate();

    Long getCreatedDate();

    Long getLastModifiedDate();

    String getName();
}
