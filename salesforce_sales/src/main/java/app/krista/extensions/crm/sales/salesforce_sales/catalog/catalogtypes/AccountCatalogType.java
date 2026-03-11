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

import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Account;

public final class AccountCatalogType {

    private AccountCatalogType() {
    }

    public static Account fromSFAccount(app.krista.extensions.crm.sales.salesforce_sales.service.Account account) {
        Account newAccount = new Account();
        newAccount.id = account.getId();
        newAccount.name = account.getName();
        newAccount.fax = account.getFax();
        newAccount.accountNumber = account.getAccountNumber();
        newAccount.website = account.getWebsite();
        newAccount.sic = account.getSic();
        newAccount.ownerId = account.getOwnerId();
        newAccount.createdById = account.getCreatedById();
        newAccount.cleanStatus = account.getCleanStatus();
        newAccount.sLASerialNumber = account.getSLASerialNumber();
        newAccount.createdDate = account.getCreatedDate();
        newAccount.type = account.getType();
        newAccount.billingStreet = account.getBillingStreet();
        newAccount.billingCity = account.getBillingCity();
        newAccount.billingState = account.getBillingState();
        newAccount.billingPostalCode = account.getBillingPostalCode();
        newAccount.billingCountry = account.getBillingCountry();
        newAccount.shippingStreet = account.getShippingStreet();
        newAccount.shippingCity = account.getShippingCity();
        newAccount.shippingState = account.getShippingState();
        newAccount.shippingPostalCode = account.getShippingPostalCode();
        newAccount.shippingCountry = account.getShippingCountry();
        //ToDo implement phone number
        newAccount.phone = account.getPhone();
        newAccount.industry = account.getIndustry();
        newAccount.numberOfEmployees = account.getNumberOfEmployees();
        newAccount.ownership = account.getOwnership();
        newAccount.customerPriority = account.getCustomerPriority();
        newAccount.sLAExpirationDate = account.getSLAExpirationDate();
        newAccount.annualRevenue = account.getAnnualRevenue();
        newAccount.customFields = account.getCustomFields();
        return newAccount;
    }

}
