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

import java.util.Map;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Validators.returnNullOrValue;

public final class SFUser {

    public String Id;
    public String Username;
    public String LastName;
    public String FirstName;
    public String Name;
    public String CompanyName;
    public String Email;
    public String MobilePhone;

    public PhoneNumber mobilePhone;
    public String Alias;
    public String CommunityNickname;
    public String Country;
    public String TimeZoneSidKey;
    public String LocaleSidKey;
    public String EmailEncodingKey;
    public String ProfileId;
    public String LanguageLocaleKey;

    public static SFUser from(Map<String, Object> entityAttributeField) {
        SFUser user = new SFUser();
        user.CompanyName = returnNullOrValue((String) entityAttributeField.get("Company Name"));
        user.MobilePhone = returnNullOrValue((String) entityAttributeField.get("Mobile Number"));
        user.Email = returnNullOrValue((String) entityAttributeField.get("Email"));
        user.Username = returnNullOrValue((String) entityAttributeField.get("User name"));
        user.Alias = returnNullOrValue((String) entityAttributeField.get("Alias"));
        user.FirstName = returnNullOrValue((String) entityAttributeField.get("First Name"));
        user.LastName = returnNullOrValue((String) entityAttributeField.get("Last Name"));
        user.Name = returnNullOrValue((String) entityAttributeField.get("Name"));
        user.CommunityNickname = returnNullOrValue((String) entityAttributeField.get("Community Nickname"));
        user.TimeZoneSidKey = "America/Los_Angeles";
        user.LocaleSidKey = "en_US";
        user.EmailEncodingKey = "ISO-8859-1";
        user.ProfileId = "00e5w000002jlSdAAI";
        user.LanguageLocaleKey = "en_US";
        return user;
    }

}