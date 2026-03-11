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

import java.util.Map;

public interface Account {
    String getId();

    String getName();

    String getFax();

    String getAccountNumber();

    String getWebsite();

    String getSic();

    String getOwnerId();

    String getCreatedById();

    String getCleanStatus();

    String getSLASerialNumber();

    Long getCreatedDate();

    String getType();

    String getBillingStreet();

    String getBillingCity();

    String getBillingState();

    String getBillingPostalCode();

    String getBillingCountry();

    String getShippingStreet();

    String getShippingCity();

    String getShippingState();

    String getShippingPostalCode();

    String getShippingCountry();

    PhoneNumber getPhone();

    String getIndustry();

    Double getNumberOfEmployees();

    String getOwnership();

    String getCustomerPriority();

    Long getSLAExpirationDate();

    Double getAnnualRevenue();

    Map<String, Object> getCustomFields();
}
