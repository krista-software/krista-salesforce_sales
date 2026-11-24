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

package app.krista.extensions.crm.sales.salesforce_sales.catalog.entities;


import app.krista.extension.impl.anno.*;
import app.krista.model.base.PhoneNumber;

import java.util.LinkedHashMap;
import java.util.Map;

@Searchable
@Domain(id = "catEntryDomain_086f33da-aaea-4d0c-816f-3d5f20b4a273",
        name = "Sales",
        ecosystemId = "catEntryEcosystem_8a0e8ce7-6289-492f-9202-52284024280c",
        ecosystemName = "CRM",
        ecosystemVersion = "db320c64-6f6f-4c9e-8daa-41d3f796d6a6")
@Entity(name = "User", id = "localDomainEntity_c64a8b45-ea45-4a20-9326-b680a2accac2", primaryKey = "Id", supportStore = true, description = "Represents a Salesforce user. Id is referenced by Owner Id on Account, Lead, Opportunity, Order, Asset, Event. Id is referenced by Task.Assigned To Id. Name is referenced by Opportunity.Owner Name and Task.Assigned To Name.")
public class User {

    @Field(name = "Id", type = "Text", required = false, description = "Unique Salesforce identifier for the user. Referenced by Owner Id on Account, Lead, Opportunity, Order, Asset, Event. Referenced by Task.Assigned To Id.")
    public String id;

    @Searchable
    @ToString
    @Field(name = "User name", type = "Text", description = "Username for Salesforce login.")
    public String userName;

    @Field(name = "Last Name", type = "Text", description = "Last name of the user.")
    public String lastName;

    @Field(name = "First Name", type = "Text", required = false, description = "First name of the user.")
    public String firstName;

    @Searchable
    @ToString
    @Field(name = "Name", type = "Text", required = false, description = "Full display name of the user. Referenced by Opportunity.Owner Name and Task.Assigned To Name.")
    public String name;

    @Field(name = "Company Name", type = "Text", required = false, description = "Company name of the user.")
    public String companyName;

    @Field(name = "Alias", type = "Text", description = "Short alias for the user.")
    public String alias;

    @Field(name = "Community Nickname", type = "Text", required = false, description = "Nickname used in Salesforce communities.")
    public String communityNickname;

    @Searchable
    @ToString
    @Field(name = "Email", type = "Email", description = "Email address of the user.")
    public String email;

    @Field(name = "Mobile Number", type = "Phone", required = false, description = "Mobile phone number of the user.")
    public PhoneNumber mobileNumber;

    public static User fromFields(Map<String, Object> fields) {
        User user = new User();
        user.id = (String) fields.get("Id");
        user.userName = (String) fields.get("User name");
        user.lastName = (String) fields.get("Last Name");
        user.firstName = (String) fields.get("First Name");
        user.name = (String) fields.get("Name");
        user.companyName = (String) fields.get("Company Name");
        user.alias = (String) fields.get("Alias");
        user.communityNickname = (String) fields.get("Community Nickname");
        user.email = (String) fields.get("Email");
        user.mobileNumber = (PhoneNumber) fields.get("Mobile Number");
        return user;
    }

    public Map<String, Object> toFields() {
        Map<String, Object> fieldMap = new LinkedHashMap<>();
        fieldMap.put("Id", id);
        fieldMap.put("User name", userName);
        fieldMap.put("Last Name", lastName);
        fieldMap.put("First Name", firstName);
        fieldMap.put("Name", name);
        fieldMap.put("Company Name", companyName);
        fieldMap.put("Alias", alias);
        fieldMap.put("Community Nickname", communityNickname);
        fieldMap.put("Email", email);
        fieldMap.put("Mobile Number", mobileNumber);
        return fieldMap;
    }

}
