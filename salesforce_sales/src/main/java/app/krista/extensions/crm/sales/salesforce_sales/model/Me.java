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

package app.krista.extensions.crm.sales.salesforce_sales.model;

import app.krista.model.base.PhoneNumber;

import java.util.List;

public final class Me {
    public String companyName;
    public String mobileNumber;
    public String email;
    public String username;
    public String displayName;
    public String alias;
    public String firstName;
    public String id;
    public String lastName;
    public String name;
    public String communityNickname;

    public List<PhoneNumber> phoneNumbers;

    public static SFUser to(Me me) {
        SFUser user = new SFUser();
        user.Id = me.id;
        user.Name = me.name;
        user.CompanyName = me.companyName;
        user.Email = me.email;
        user.Username = me.username;
        user.Alias = me.alias;
        user.FirstName = me.firstName;
        user.LastName = me.lastName;
        user.CommunityNickname = me.communityNickname;
        if (me.phoneNumbers != null && !me.phoneNumbers.isEmpty() && me.phoneNumbers.get(0) != null) {
            user.mobilePhone = me.phoneNumbers.get(0);
        }

        return user;
    }

}