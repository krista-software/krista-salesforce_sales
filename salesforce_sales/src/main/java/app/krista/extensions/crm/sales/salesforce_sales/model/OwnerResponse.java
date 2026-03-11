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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public final class OwnerResponse {

    @SerializedName("totalSize")
    @Expose
    public Integer totalSize;
    @SerializedName("done")
    @Expose
    public Boolean done;
    @SerializedName("records")
    @Expose
    public List<Record> records = null;


    public class Attributes {

        @SerializedName("type")
        @Expose
        public String type;
        @SerializedName("url")
        @Expose
        public String url;

    }

    public class Record {

        @SerializedName("attributes")
        @Expose
        public Attributes attributes;
        @SerializedName("Name")
        @Expose
        public String name;

        @SerializedName("Address")
        @Expose
        public Address address;
    }

    public class Address {

        @SerializedName("city")
        @Expose
        public String city;
        @SerializedName("country")
        @Expose
        public String country;
        @SerializedName("geocodeAccuracy")
        @Expose
        public Object geocodeAccuracy;
        @SerializedName("latitude")
        @Expose
        public Object latitude;
        @SerializedName("longitude")
        @Expose
        public Object longitude;
        @SerializedName("postalCode")
        @Expose
        public String postalCode;
        @SerializedName("state")
        @Expose
        public String state;
        @SerializedName("street")
        @Expose
        public String street;
    }
}
