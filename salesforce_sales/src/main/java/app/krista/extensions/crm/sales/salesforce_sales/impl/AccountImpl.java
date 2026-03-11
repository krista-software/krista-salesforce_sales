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

import app.krista.extensions.crm.sales.salesforce_sales.model.SFAccount;
import app.krista.extensions.crm.sales.salesforce_sales.service.Account;
import app.krista.extensions.crm.sales.salesforce_sales.util.DatePlus;
import app.krista.model.base.PhoneNumber;

import java.util.Map;

public class AccountImpl implements Account {

    private final SFAccount sfAccount;

    public AccountImpl(SFAccount sfAccount) {
        this.sfAccount = sfAccount;
    }

    @Override
    public String getId() {
        return sfAccount.id;
    }

    @Override
    public String getName() {
        return sfAccount.name;
    }

    @Override
    public String getFax() {
        return sfAccount.fax;
    }

    @Override
    public String getAccountNumber() {
        return sfAccount.accountNumber;
    }

    @Override
    public String getWebsite() {
        return sfAccount.website;
    }

    @Override
    public String getSic() {
        return sfAccount.sic;
    }

    @Override
    public String getOwnerId() {
        return sfAccount.ownerId;
    }

    @Override
    public String getCreatedById() {
        return sfAccount.createdById;
    }

    @Override
    public String getCleanStatus() {
        return sfAccount.cleanStatus;
    }

    @Override
    public String getSLASerialNumber() {
        return sfAccount.sLASerialNumber;
    }

    @Override
    public Long getCreatedDate() {
        return sfAccount.createdDate != null ? sfAccount.createdDate.getTime() : null;
    }

    @Override
    public String getType() {
        return sfAccount.type;
    }

    @Override
    public String getBillingStreet() {
        return sfAccount.billingStreet;
    }

    @Override
    public String getBillingCity() {
        return sfAccount.billingCity;
    }

    @Override
    public String getBillingState() {
        return sfAccount.billingState;
    }

    @Override
    public String getBillingPostalCode() {
        return sfAccount.billingPostalCode;
    }

    @Override
    public String getBillingCountry() {
        return sfAccount.billingCountry;
    }

    @Override
    public String getShippingStreet() {
        return sfAccount.shippingStreet;
    }

    @Override
    public String getShippingCity() {
        return sfAccount.shippingCity;
    }

    @Override
    public String getShippingState() {
        return sfAccount.shippingState;
    }

    @Override
    public String getShippingPostalCode() {
        return sfAccount.shippingPostalCode;
    }

    @Override
    public String getShippingCountry() {
        return sfAccount.shippingCountry;
    }

    @Override
    public PhoneNumber getPhone() {
        return app.krista.extensions.crm.sales.salesforce_sales.model.PhoneNumber.parsePhoneNumber(sfAccount.phone);
    }

    @Override
    public String getIndustry() {
        return sfAccount.industry;
    }

    @Override
    public Double getNumberOfEmployees() {
        return sfAccount.numberOfEmployees;
    }

    @Override
    public String getOwnership() {
        return sfAccount.ownership;
    }

    @Override
    public String getCustomerPriority() {
        return sfAccount.customerPriority;
    }

    @Override
    public Long getSLAExpirationDate() {
        return sfAccount.sLAExpirationDate != null ? DatePlus.fromYYYYMMDD(sfAccount.sLAExpirationDate).getTime() : null;
    }

    @Override
    public Double getAnnualRevenue() {
        return sfAccount.annualRevenue;
    }

    @Override
    public Map<String, Object> getCustomFields() {
        return sfAccount.customFields;
    }
}
