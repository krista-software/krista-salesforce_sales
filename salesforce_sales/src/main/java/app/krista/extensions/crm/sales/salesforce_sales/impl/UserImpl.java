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

import app.krista.extensions.crm.sales.salesforce_sales.model.SFUser;
import app.krista.extensions.crm.sales.salesforce_sales.service.User;
import app.krista.model.base.PhoneNumber;

public class UserImpl implements User {

    private final SFUser sfUser;

    public UserImpl(SFUser sfUser) {
        this.sfUser = sfUser;
    }

    @Override
    public String getId() {
        return sfUser.Id;
    }

    @Override
    public String getUserName() {
        return sfUser.Username;
    }

    @Override
    public String getLastName() {
        return sfUser.LastName;
    }

    @Override
    public String getFirstName() {
        return sfUser.FirstName;
    }

    @Override
    public String getName() {
        return sfUser.Name;
    }

    @Override
    public String getCompanyName() {
        return sfUser.CompanyName;
    }

    @Override
    public String getAlias() {
        return sfUser.Alias;
    }

    @Override
    public String getCommunityNickname() {
        return sfUser.CommunityNickname;
    }

    @Override
    public String getEmail() {
        return sfUser.Email;
    }

    @Override
    public PhoneNumber getMobileNumber() {
        return app.krista.extensions.crm.sales.salesforce_sales.model.PhoneNumber.parsePhoneNumber(sfUser.MobilePhone);
    }

    @Override
    public String getCountry() {
        return sfUser.Country;
    }
}
