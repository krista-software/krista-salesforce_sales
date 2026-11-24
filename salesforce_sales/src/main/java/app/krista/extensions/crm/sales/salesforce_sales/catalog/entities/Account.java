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

package app.krista.extensions.crm.sales.salesforce_sales.catalog.entities;


import app.krista.extension.impl.anno.*;
import app.krista.model.base.PhoneNumber;

import java.util.LinkedHashMap;
import java.util.Map;

@Searchable
@Domain(id = "catEntryDomain_086f33da-aaea-4d0c-816f-3d5f20b4a273",
        name = "Sales",
        ecosystemId = "catEntryEcosystem_8a0e8ce7-6289-492f-9202-52284024280c",
        ecosystemName = "CRM",
        ecosystemVersion = "db320c64-6f6f-4c9e-8daa-41d3f796d6a6")
@Entity(name = "Account", id = "localDomainEntity_f48532cb-46ca-44ed-9eab-462339f9a807", primaryKey = "Id", supportStore = true, description = "Represents a Salesforce account (company or organization). Contains Opportunities where Opportunity.Account Id matches Account.Id. Contains Orders where Order.Account Id matches Account.Id. Contains Quotes where Quote.Account Id matches Account.Id. Contains Assets where Asset.Account Id matches Account.Id. Owner Id references User.Id.")
public class Account {

    @Searchable
    @Field(name = "Id", type = "Text", required = false, description = "Unique Salesforce identifier for the account.")
    public String id;

    @Searchable
    @ToString
    @Field(name = "Name", type = "Text", description = "Name of the account. Referenced by Opportunity.Account Name.")
    public String name;

    @Field(name = "Fax", type = "Text", required = false, description = "Fax number of the account.")
    public String fax;

    @Field(name = "Account Number", type = "Text", required = false, description = "Business account number.")
    public String accountNumber;

    @Field(name = "Website", type = "Text", required = false, description = "URL of the account's website.")
    public String website;

    @Field(name = "Sic", type = "Text", required = false, description = "Standard Industrial Classification code.")
    public String sic;

    @Field(name = "Owner Id", type = "Text", required = false, description = "Salesforce User Id of the account owner. References User.Id.")
    public String ownerId;

    @Field(name = "Created By Id", type = "Text", required = false, description = "Salesforce User Id who created the account. References User.Id.")
    public String createdById;

    @Field(name = "Clean Status", type = "Text", required = false, description = "Data.com clean status of the account.")
    public String cleanStatus;

    @Field(name = "SLA Serial Number", type = "Text", required = false, description = "SLA serial number for the account.")
    public String sLASerialNumber;

    @Field(name = "Created Date", type = "Date", required = false, description = "Date and time when the account was created.")
    public Long createdDate;

    @Field(name = "Type", type = "PickOne", required = false, description = "Type of account (e.g., Customer, Partner, Prospect).")
    public String type;

    @Field(name = "Billing Street", type = "Street", required = false, description = "Billing address street of the account.")
    public String billingStreet;

    @Field(name = "Billing City", type = "City", required = false, description = "Billing address city of the account.")
    public String billingCity;

    @Field(name = "Billing State", type = "State", required = false, description = "Billing address state of the account.")
    public String billingState;

    @Field(name = "Billing Postal Code", type = "PostalCode", required = false, description = "Billing address postal code of the account.")
    public String billingPostalCode;

    @Field(name = "Billing Country", type = "Country", required = false, description = "Billing address country of the account.")
    public String billingCountry;

    @Field(name = "Shipping Street", type = "Street", required = false, description = "Shipping address street of the account.")
    public String shippingStreet;

    @Field(name = "Shipping City", type = "City", required = false, description = "Shipping address city of the account.")
    public String shippingCity;

    @Field(name = "Shipping State", type = "State", required = false, description = "Shipping address state of the account.")
    public String shippingState;

    @Field(name = "Shipping Postal Code", type = "PostalCode", required = false, description = "Shipping address postal code of the account.")
    public String shippingPostalCode;

    @Field(name = "Shipping Country", type = "Country", required = false, description = "Shipping address country of the account.")
    public String shippingCountry;

    @Field(name = "Phone", type = "Phone", required = false, description = "Primary phone number of the account.")
    public PhoneNumber phone;

    @Field(name = "Industry", type = "PickOne", required = false, description = "Industry sector of the account.")
    public String industry;

    @Field(name = "Number Of Employees", type = "Number", required = false, description = "Number of employees at the account.")
    public Double numberOfEmployees;

    @Field(name = "Ownership", type = "PickOne", required = false, description = "Ownership type of the account.")
    public String ownership;

    @Field(name = "CustomerPriority", type = "PickOne", required = false, description = "Customer priority level.")
    public String customerPriority;

    @Field(name = "SLA Expiration Date", type = "Date", required = false, description = "SLA expiration date for the account.")
    public Long sLAExpirationDate;

    @Field(name = " Annual Revenue", type = "Number", required = false, description = "Annual revenue of the account.")
    public Double annualRevenue;

    @Field(name = "Custom Fields", type = "FreeForm", required = false, description = "Dynamic custom fields from Salesforce (fields ending with __c).")
    public Map<String, Object> customFields;

    public static Account fromFields(Map<String, Object> account) {
        Account newAccount = new Account();
        newAccount.accountNumber = (String) account.get("Account Number");
        newAccount.ownership = (String) account.get("Ownership");
        newAccount.createdDate = (Long) account.get("Created Date");
        newAccount.website = (String) account.get("Website");
        newAccount.sLAExpirationDate = (Long) account.get("SLA Expiration Date");
        newAccount.name = (String) account.get("Name");
        newAccount.industry = (String) account.get("Industry");
        newAccount.numberOfEmployees = (Double) account.get("Number Of Employees");
        newAccount.billingCity = (String) account.get("Billing City");
        newAccount.ownerId = (String) account.get("Owner Id");
        newAccount.shippingStreet = (String) account.get("Shipping Street");
        newAccount.phone = (PhoneNumber) account.get("Phone");
        newAccount.sLASerialNumber = (String) account.get("SLA Serial Number");
        newAccount.shippingState = (String) account.get("Shipping State");
        newAccount.createdById = (String) account.get("Created By Id");
        newAccount.billingState = (String) account.get("Billing State");
        newAccount.shippingCity = (String) account.get("Shipping City");
        newAccount.billingCountry = (String) account.get("Billing Country");
        newAccount.sic = (String) account.get("Sic");
        newAccount.shippingCountry = (String) account.get("Shipping Country");
        newAccount.shippingPostalCode = (String) account.get("Shipping Postal Code");
        newAccount.type = (String) account.get("Type");
        newAccount.billingStreet = (String) account.get("Billing Street");
        newAccount.billingPostalCode = (String) account.get("Billing Postal Code");
        newAccount.customerPriority = (String) account.get("CustomerPriority");
        newAccount.cleanStatus = (String) account.get("Clean Status");
        newAccount.id = (String) account.get("Id");
        newAccount.fax = (String) account.get("Fax");
        newAccount.annualRevenue = (Double) account.get(" Annual Revenue");

        // Extract custom fields from the "Custom Fields" FreeForm
        Object customFieldsObj = account.get("Custom Fields");
        if (customFieldsObj instanceof Map) {
            newAccount.customFields = (Map<String, Object>) customFieldsObj;
        }

        return newAccount;
    }

    public Map<String, Object> toFields() {
        Map<String, Object> fieldMap = new LinkedHashMap<>();
        fieldMap.put("Id", id);
        fieldMap.put("Name", name);
        fieldMap.put("Fax", fax);
        fieldMap.put("Account Number", accountNumber);
        fieldMap.put("Website", website);
        fieldMap.put("Sic", sic);
        fieldMap.put("Owner Id", ownerId);
        fieldMap.put("Created By Id", createdById);
        fieldMap.put("Clean Status", cleanStatus);
        fieldMap.put("SLA Serial Number", sLASerialNumber);
        fieldMap.put("Created Date", createdDate);
        fieldMap.put("Type", type);
        fieldMap.put("Billing Street", billingStreet);
        fieldMap.put("Billing City", billingCity);
        fieldMap.put("Billing State", billingState);
        fieldMap.put("Billing Postal Code", billingPostalCode);
        fieldMap.put("Billing Country", billingCountry);
        fieldMap.put("Shipping Street", shippingStreet);
        fieldMap.put("Shipping City", shippingCity);
        fieldMap.put("Shipping State", shippingState);
        fieldMap.put("Shipping Postal Code", shippingPostalCode);
        fieldMap.put("Shipping Country", shippingCountry);
        fieldMap.put("Phone", phone);
        fieldMap.put("Industry", industry);
        fieldMap.put("Number Of Employees", numberOfEmployees);
        fieldMap.put("Ownership", ownership);
        fieldMap.put("CustomerPriority", customerPriority);
        fieldMap.put("SLA Expiration Date", sLAExpirationDate);
        fieldMap.put(" Annual Revenue", annualRevenue);
        fieldMap.put("Custom Fields", customFields);
        return fieldMap;
    }

}
