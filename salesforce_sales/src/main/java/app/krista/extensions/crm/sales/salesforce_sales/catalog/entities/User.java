package app.krista.extensions.crm.sales.salesforce_sales.catalog.entities;


import app.krista.extension.impl.anno.*;
import app.krista.model.base.PhoneNumber;

import java.util.Map;

@Domain(id = "catEntryDomain_086f33da-aaea-4d0c-816f-3d5f20b4a273",
        name = "Sales",
        ecosystemId = "catEntryEcosystem_8a0e8ce7-6289-492f-9202-52284024280c",
        ecosystemName = "CRM",
        ecosystemVersion = "db320c64-6f6f-4c9e-8daa-41d3f796d6a6")
@Entity(name = "User", id = "localDomainEntity_c64a8b45-ea45-4a20-9326-b680a2accac2", primaryKey = "Id")
public class User {

    @Field(name = "Id", type = "Text", required = false)
    public String id;

    @Searchable
    @ToString
    @Field(name = "User name", type = "Text")
    public String userName;

    @Field(name = "Last Name", type = "Text")
    public String lastName;

    @Field(name = "First Name", type = "Text", required = false)
    public String firstName;

    @Searchable
    @ToString
    @Field(name = "Name", type = "Text", required = false)
    public String name;

    @Field(name = "Company Name", type = "Text", required = false)
    public String companyName;

    @Field(name = "Alias", type = "Text")
    public String alias;

    @Field(name = "Community Nickname", type = "Text", required = false)
    public String communityNickname;

    @Searchable
    @ToString
    @Field(name = "Email", type = "Email")
    public String email;

    @Field(name = "Mobile Number", type = "Phone", required = false)
    public PhoneNumber mobileNumber;

    public static User fromFields(Map<String, Object> fields) {
        User user = new User();
        user.id = (String) fields.get("Id");
        user.userName = (String) fields.get("User name");
        user.lastName = (String) fields.get("Last Name");
        user.firstName = (String) fields.get("First Name");
        user.name = (String) fields.get("Name");
        user.companyName = (String) fields.get("Company Name");
        user.alias = (String) fields.get("Alias");
        user.communityNickname = (String) fields.get("Community Nickname");
        user.email = (String) fields.get("Email");
        user.mobileNumber = (PhoneNumber) fields.get("Mobile Number");
        return user;
    }

}