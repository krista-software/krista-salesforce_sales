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
