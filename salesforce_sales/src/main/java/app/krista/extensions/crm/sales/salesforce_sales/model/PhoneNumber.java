package app.krista.extensions.crm.sales.salesforce_sales.model;

import com.google.gson.annotations.SerializedName;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

@SuppressWarnings("unused")
public final class PhoneNumber {
    @SerializedName("countryCode")
    public Long countryCode;

    @SerializedName("nationalNumber")
    public Long nationalNumber;

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
            } catch (NumberParseException e) {
                System.err.println("NumberParseException was thrown: " + e.toString());
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong");
        }
    }
}
