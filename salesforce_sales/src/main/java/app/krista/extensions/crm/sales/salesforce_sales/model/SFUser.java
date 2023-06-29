package app.krista.extensions.crm.sales.salesforce_sales.model;

import app.krista.model.base.PhoneNumber;

import java.util.Map;

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
        user.CompanyName = (String) entityAttributeField.get("Company Name");
        user.MobilePhone = (String) entityAttributeField.get("Mobile Number");
        user.Email = (String) entityAttributeField.get("Email");
        user.Username = (String) entityAttributeField.get("User name");
        user.Alias = (String) entityAttributeField.get("Alias");
        user.FirstName = (String) entityAttributeField.get("First Name");
        user.Id = (String) entityAttributeField.get("Id");
        user.LastName = (String) entityAttributeField.get("Last Name");
        user.Name = (String) entityAttributeField.get("Name");
        user.CommunityNickname = (String) entityAttributeField.get("Community Nickname");
        user.TimeZoneSidKey = "America/Los_Angeles";
        user.LocaleSidKey = "en_US";
        user.EmailEncodingKey = "ISO-8859-1";
        user.ProfileId = "00e5w000002jlSdAAI";
        user.LanguageLocaleKey = "en_US";
        return user;
    }

}