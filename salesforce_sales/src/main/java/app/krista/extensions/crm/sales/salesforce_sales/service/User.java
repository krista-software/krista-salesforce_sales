package app.krista.extensions.crm.sales.salesforce_sales.service;

import app.krista.model.base.PhoneNumber;

public interface User {

    String getId();

    String getUserName();

    String getLastName();

    String getFirstName();

    String getName();

    String getCompanyName();

    String getAlias();

    String getCommunityNickname();

    String getEmail();

    PhoneNumber getMobileNumber();

    String getCountry();
}
