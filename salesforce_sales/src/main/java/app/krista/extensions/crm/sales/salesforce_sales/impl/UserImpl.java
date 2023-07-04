package app.krista.extensions.crm.sales.salesforce_sales.impl;

import app.krista.extensions.crm.sales.salesforce_sales.model.SFUser;
import app.krista.extensions.crm.sales.salesforce_sales.service.User;
import app.krista.model.base.PhoneNumber;

public class UserImpl implements User {

    private final SFUser sfUser;

    public UserImpl(SFUser sfUser) {
        this.sfUser = sfUser;
    }

    @Override
    public String getId() {
        return sfUser.Id;
    }

    @Override
    public String getUserName() {
        return sfUser.Username;
    }

    @Override
    public String getLastName() {
        return sfUser.LastName;
    }

    @Override
    public String getFirstName() {
        return sfUser.FirstName;
    }

    @Override
    public String getName() {
        return sfUser.Name;
    }

    @Override
    public String getCompanyName() {
        return sfUser.CompanyName;
    }

    @Override
    public String getAlias() {
        return sfUser.Alias;
    }

    @Override
    public String getCommunityNickname() {
        return sfUser.CommunityNickname;
    }

    @Override
    public String getEmail() {
        return sfUser.Email;
    }

    @Override
    public PhoneNumber getMobileNumber() {
        return app.krista.extensions.crm.sales.salesforce_sales.model.PhoneNumber.parsePhoneNumber(sfUser.MobilePhone);
    }

    @Override
    public String getCountry() {
        return sfUser.Country;
    }
}
