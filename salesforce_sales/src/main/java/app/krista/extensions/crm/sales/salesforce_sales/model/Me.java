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