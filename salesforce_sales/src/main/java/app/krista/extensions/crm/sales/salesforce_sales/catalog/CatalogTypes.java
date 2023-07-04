package app.krista.extensions.crm.sales.salesforce_sales.catalog;

import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.*;
import app.krista.extensions.crm.sales.salesforce_sales.service.PriceBookEntry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CatalogTypes {

    private CatalogTypes() {
    }

    public static Opportunity fromSFOpportunity(app.krista.extensions.crm.sales.salesforce_sales.service.Opportunity opportunity) {
        Opportunity newOpportunity = new Opportunity();
        newOpportunity.name = opportunity.getName();
        newOpportunity.id = opportunity.getId();
        newOpportunity.contactId = opportunity.getContactId();
        newOpportunity.ownerId = opportunity.getOwnerId();
        newOpportunity.ownerName = opportunity.getOwnerName();
        newOpportunity.accountId = opportunity.getAccountId();
        newOpportunity.accountName = opportunity.getAccountName();
        newOpportunity.type = opportunity.getType();
        newOpportunity.leadSource = opportunity.getLeadSource();
        newOpportunity.pricebookId = opportunity.getPricebookId();
        newOpportunity.stageName = opportunity.getStageName();
        newOpportunity.nextStep = opportunity.getNextStep();
        newOpportunity.amount = opportunity.getAmount();
        newOpportunity.acv = opportunity.getAcv();
        newOpportunity.partner = opportunity.getPartner();
        newOpportunity.competition = opportunity.getCompetition();
        newOpportunity.probability = opportunity.getProbability();
        newOpportunity.description = opportunity.getDescription();
        newOpportunity.expectedRevenue = opportunity.getExpectedRevenue();
        newOpportunity.lastModifiedDate = opportunity.getLastModifiedDate();
        newOpportunity.createdDate = opportunity.getCreatedDate();
        newOpportunity.closeDate = opportunity.getCloseDate();
        return newOpportunity;
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

    public static PricebookEntry fromSFPriceBookEntry(PriceBookEntry priceBookEntry) {
        PricebookEntry pricebookEntry = new PricebookEntry();
        pricebookEntry.id = priceBookEntry.getId();
        pricebookEntry.name = priceBookEntry.getName();
        pricebookEntry.pricebookId = priceBookEntry.getPriceBookId();
        pricebookEntry.productId = priceBookEntry.getProductId();
        pricebookEntry.productCode = priceBookEntry.getProductCode();
        pricebookEntry.createdDate = priceBookEntry.getCreatedDate();
        pricebookEntry.lastModifiedDate = priceBookEntry.getLastModifiedDate();
        pricebookEntry.unitPrice = priceBookEntry.getUnitPrice();
        return pricebookEntry;
    }

    public static Lead fromSFLead(app.krista.extensions.crm.sales.salesforce_sales.service.Lead lead) {
        Lead newLead = new Lead();
        newLead.id = lead.getId();
        newLead.title = lead.getTitle();
        newLead.company = lead.getCompany();
        newLead.website = lead.getWebsite();
        newLead.ownerId = lead.getOwnerId();
        newLead.isConverted = lead.getIsConverted();
        newLead.convertedAccountId = lead.getConvertedAccountId();
        newLead.convertedContactId = lead.getConvertedContactId();
        newLead.convertedOpportunityId = lead.getConvertedOpportunityId();
        newLead.cleanStatus = lead.getCleanStatus();
        newLead.phone = lead.getPhone();
        newLead.mobilePhone = lead.getMobilePhone();
        newLead.email = lead.getEmail();
        newLead.description = lead.getDescription();
        newLead.status = lead.getStatus();
        newLead.leadSource = lead.getLeadSource();
        newLead.industry = lead.getIndustry();
        newLead.rating = lead.getRating();
        newLead.annualRevenue = lead.getAnnualRevenue();
        newLead.convertedDate = lead.getConvertedDate();
        newLead.createdDate = lead.getCreatedDate();
        newLead.lastModifiedDate = lead.getLastModifiedDate();
        newLead.name = lead.getName();
        return newLead;
    }

    public static Event fromSFEvent(app.krista.extensions.crm.sales.salesforce_sales.service.Event event) {
        Event newEvent = new Event();
        newEvent.id = event.getId();
        newEvent.leadId = event.getLeadId();
        newEvent.subject = event.getSubject();
        newEvent.ownerId = event.getOwnerId();
        newEvent.accountId = event.getAccountId();
        newEvent.createdDate = event.getCreatedDate();
        newEvent.isPrivate = event.getIsPrivate();
        newEvent.isGroupEvent = event.getIsGroupEvent();
        newEvent.lastModifiedDate = event.getLastModifiedDate();
        newEvent.location = event.getLocation();
        newEvent.startDateTime = event.getStartDateTime();
        newEvent.endDateTime = event.getEndDateTime();
        return newEvent;
    }

    public static Asset fromSFAsset(app.krista.extensions.crm.sales.salesforce_sales.service.Asset asset) {
        Asset newAsset = new Asset();
        newAsset.id = asset.getId();
        newAsset.contactId = asset.getContactId();
        newAsset.accountId = asset.getAccountId();
        newAsset.rootAssetId = asset.getRootAssetId();
        newAsset.isCompetitorProduct = asset.getIsCompetitorProduct();
        newAsset.createdDate = asset.getCreatedDate();
        newAsset.createdById = asset.getCreatedById();
        newAsset.lastModifiedDate = asset.getLastModifiedDate();
        newAsset.isDeleted = asset.getIsDeleted();
        newAsset.name = asset.getName();
        newAsset.ownerId = asset.getOwnerId();
        return newAsset;
    }

    public static Account fromSFAccount(app.krista.extensions.crm.sales.salesforce_sales.service.Account account) {
        Account newAccount = new Account();
        newAccount.id = account.getId();
        newAccount.name = account.getName();
        newAccount.fax = account.getFax();
        newAccount.accountNumber = account.getAccountNumber();
        newAccount.website = account.getWebsite();
        newAccount.sic = account.getSic();
        newAccount.ownerId = account.getOwnerId();
        newAccount.createdById = account.getCreatedById();
        newAccount.cleanStatus = account.getCleanStatus();
        newAccount.sLASerialNumber = account.getSLASerialNumber();
        newAccount.createdDate = account.getCreatedDate();
        newAccount.type = account.getType();
        newAccount.billingStreet = account.getBillingStreet();
        newAccount.billingCity = account.getBillingCity();
        newAccount.billingState = account.getBillingState();
        newAccount.billingPostalCode = account.getBillingPostalCode();
        newAccount.billingCountry = account.getBillingCountry();
        newAccount.shippingStreet = account.getShippingStreet();
        newAccount.shippingCity = account.getShippingCity();
        newAccount.shippingState = account.getShippingState();
        newAccount.shippingPostalCode = account.getShippingPostalCode();
        newAccount.shippingCountry = account.getShippingCountry();
        //ToDo implement phone number
        newAccount.phone = account.getPhone();
        newAccount.industry = account.getIndustry();
        newAccount.numberOfEmployees = account.getNumberOfEmployees();
        newAccount.ownership = account.getOwnership();
        newAccount.customerPriority = account.getCustomerPriority();
        newAccount.sLAExpirationDate = account.getSLAExpirationDate();
        newAccount.annualRevenue = account.getAnnualRevenue();
        return newAccount;
    }

    public static Quote fromSFQuote(app.krista.extensions.crm.sales.salesforce_sales.service.Quote quote) {
        Quote newQuote = new Quote();
        newQuote.id = quote.getId();
        newQuote.name = quote.getName();
        newQuote.createdDate = quote.getCreatedDate();
        newQuote.opportunityId = quote.getOpportunityId();
        newQuote.pricebookId = quote.getPricebookId();
        newQuote.contactId = quote.getContactId();
        newQuote.shippingHandling = quote.getShippingHandling();
        newQuote.tax = quote.getTax();
        newQuote.expirationDate = quote.getExpirationDate();
        newQuote.subtotal = quote.getSubtotal();
        newQuote.lineItemCount = quote.getLineItemCount();
        newQuote.billingName = quote.getBillingName();
        newQuote.accountId = quote.getAccountId();
        newQuote.quoteNumber = quote.getQuoteNumber();
        newQuote.status = quote.getStatus();
        newQuote.description = quote.getDescription();
        newQuote.totalPrice = quote.getTotalPrice();
        newQuote.discount = quote.getDiscount();
        newQuote.grandTotal = quote.getGrandTotal();
        return newQuote;
    }

    public static SalesPipelineSummary fromSFSalesPipelineSummary(app.krista.extensions.crm.sales.salesforce_sales.service.SalesPipelineSummary salesPipelineSummary) {
        SalesPipelineSummary newSalesPipelineSummary = new SalesPipelineSummary();
        newSalesPipelineSummary.status = salesPipelineSummary.getStatus();
        newSalesPipelineSummary.average = salesPipelineSummary.getAverage();
        newSalesPipelineSummary.total = salesPipelineSummary.getTotal();
        return newSalesPipelineSummary;
    }

    public static Map<String, Object> getFieldMapFromList(List quoteFields) {
        Map<String, Object> fieldsMap = new HashMap<>();
        for (Object field : quoteFields) {
            fieldsMap.putAll((Map<? extends String, ?>) field);
        }
        return fieldsMap;
    }
}