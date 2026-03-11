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

import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.User;

public final class UserCatalogType {

    private UserCatalogType() {
    }

    public static User fromSFUser(app.krista.extensions.crm.sales.salesforce_sales.service.User user) {
        User newUser = new User();
        newUser.id = user.getId();
        newUser.userName = user.getUserName();
        newUser.lastName = user.getLastName();
        newUser.firstName = user.getFirstName();
        newUser.name = user.getName();
        newUser.companyName = user.getCompanyName();
        newUser.alias = user.getAlias();
        newUser.communityNickname = user.getCommunityNickname();
        newUser.email = user.getEmail();
        newUser.mobileNumber = user.getMobileNumber();
        return newUser;
    }
}
