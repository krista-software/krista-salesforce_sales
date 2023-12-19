package app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes;

import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.User;

public final class UserCatalogType {

    private UserCatalogType() {
    }

    public static User fromSFUser(app.krista.extensions.crm.sales.salesforce_sales.service.User user) {
        User newUser = new User();
        newUser.id = user.getId();
        newUser.userName = user.getUserName();
        newUser.lastName = user.getLastName();
        newUser.firstName = user.getFirstName();
        newUser.name = user.getName();
        newUser.companyName = user.getCompanyName();
        newUser.alias = user.getAlias();
        newUser.communityNickname = user.getCommunityNickname();
        newUser.email = user.getEmail();
        newUser.mobileNumber = user.getMobileNumber();
        return newUser;
    }
}
