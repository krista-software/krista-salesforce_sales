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

import java.util.ArrayList;
import java.util.List;

public final class SFEmail {
    public String emailAddresses, emailSubject, emailBody;

    public SFEmail(String emailAddresses, String emailSubject, String emailBody) {
        this.emailAddresses = emailAddresses;
        this.emailSubject = emailSubject;
        this.emailBody = emailBody;
    }

    public static class Body {
        public List<SFEmail> inputs = new ArrayList<>();
    }
}