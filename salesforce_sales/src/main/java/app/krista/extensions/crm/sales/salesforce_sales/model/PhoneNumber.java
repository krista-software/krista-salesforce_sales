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

import com.google.gson.annotations.SerializedName;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unused")
public final class PhoneNumber {
    @SerializedName("countryCode")
    public Long countryCode;

    @SerializedName("nationalNumber")
    public Long nationalNumber;
    private static final Logger LOGGER = LoggerFactory.getLogger(PhoneNumber.class);

    public static app.krista.model.base.PhoneNumber parsePhoneNumber(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        String val = value.startsWith("+") ? value : "+" + value;
        try {
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            try {
                Phonenumber.PhoneNumber phoneNumber = phoneUtil.parse(value, "IN");
                return new app.krista.model.base.PhoneNumber(phoneNumber.getCountryCode(), phoneNumber.getNationalNumber());
            } catch (NumberParseException cause) {
                LOGGER.error("NumberParseException was thrown: " + cause.getMessage());
            }
            return null;
        } catch (Exception exception) {
            throw new RuntimeException("Something went wrong");
        }
    }
}
