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

package app.krista.extensions.crm.sales.salesforce_sales.util;

import app.krista.extension.executor.SearchCondition;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static app.krista.extensions.crm.sales.salesforce_sales.impl.OpportunityDaoImpl.QUERY_FIELDS;
import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.AND;
import static app.krista.extensions.crm.sales.salesforce_sales.util.SFHelper.soqlEscape;
import static app.krista.extensions.crm.sales.salesforce_sales.util.DatePlus.getDateForGivenTimeZone;

public final class OpportunityQueryBuilder {

    public static String getQuery(List<SearchCondition> searchConditions, long size, long page, String timeZone) {
        String name = getStringConditionValue(searchConditions, "Name");
        String id = getStringConditionValue(searchConditions, "Id");
        String stageName = getStringConditionValue(searchConditions, "Stage Name");
        String nextStep = getStringConditionValue(searchConditions, "Next Step");
        String ownerId = getStringConditionValue(searchConditions, "Owner Id");
        String ownerName = getStringConditionValue(searchConditions, "Owner Name");
        String accountId = getStringConditionValue(searchConditions, "Account Id");
        String accountName = getStringConditionValue(searchConditions, "Account Name");
        String type = getStringConditionValue(searchConditions, "Type");
        String leadSource = getStringConditionValue(searchConditions, "Lead Source");
        String partner = getStringConditionValue(searchConditions, "Partner");
        String competition = getStringConditionValue(searchConditions, "Competition");
        Double amount = getDoubleConditionValue(searchConditions, "Amount", "equals");
        Double amountLessThan = getDoubleConditionValue(searchConditions, "Amount", "lt");
        Double amountGreaterThan = getDoubleConditionValue(searchConditions, "Amount", "gt");
        Double acv = getDoubleConditionValue(searchConditions, "ACV", "equals");
        Double probability = getDoubleConditionValue(searchConditions, "Probability", "equals");
        Double probabilityGreaterThan = getDoubleConditionValue(searchConditions, "Probability", "gt");
        Double probabilityLessThan = getDoubleConditionValue(searchConditions, "Probability", "lt");
        String createdOnLessThan = getLongConditionValue(searchConditions, "Created Date", "lt");
        String createdOnGreaterThan = getLongConditionValue(searchConditions, "Created Date", "gt");
        String modifiedOnLessThan = getLongConditionValue(searchConditions, "Last Modified Date", "lt");
        String modifiedOnGreaterThan = getLongConditionValue(searchConditions, "Last Modified Date", "gt");
        String closeDate = getDateForGivenTimeZone(searchConditions, "Close Date", timeZone);
        return createQuery(name, id, stageName, nextStep, ownerId, ownerName,
                accountId, accountName, type, leadSource, partner, competition, amount, amountLessThan, amountGreaterThan, acv, probability, probabilityGreaterThan, probabilityLessThan,
                createdOnLessThan, createdOnGreaterThan, modifiedOnLessThan, modifiedOnGreaterThan, closeDate, size, page);
    }

    private static String getStringConditionValue(List<SearchCondition> conditions, String fieldName) {
        for (SearchCondition con : conditions) {
            if (fieldName.equalsIgnoreCase(con.getFieldName())) {
                return con.getOperand() != null ? String.valueOf(con.getOperand()).trim() : "";
            }
        }
        return "";
    }

    private static Double getDoubleConditionValue(List<SearchCondition> conditions, String fieldName, String queryOperand) {
        for (SearchCondition con : conditions) {
            if (fieldName.equalsIgnoreCase(con.getFieldName()) && con.getOperator().toString().equalsIgnoreCase(queryOperand)) {
                return con.getOperand() != null ? (Double) con.getOperand() : null;
            }
        }
        return null;
    }


    private static String getLongConditionValue(List<SearchCondition> conditions, String fieldName, String queryOperand) {
        for (SearchCondition con : conditions) {
            if (fieldName.equalsIgnoreCase(con.getFieldName()) && con.getOperator().toString().equalsIgnoreCase(queryOperand)) {
                if (con.getOperand() == null) {
                    return null;
                } else {
                    String dateString = String.valueOf(con.getOperand()).trim().replace("E", "").replace(".", "");
                    long epoch = Long.parseLong(dateString) * 1000;
                    Date date = new Date(dateString.length() == 9 ? epoch * 10 : epoch);
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000+0000");
                    dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
                    return dateFormat.format(date);
                }
            }
        }
        return null;
    }

    private static String createQuery(String name, String id, String stageName, String nextStep, String ownerId, String ownerName,
                                      String accountId, String accountName, String type, String leadSource, String partner, String competition,
                                      Double amount, Double amountLessThan, Double amountGreaterThan, Double acv, Double probability, Double probabilityGreaterThan, Double probabilityLessThan,
                                      String createdOnLessThan, String createdOnGreaterThan, String modifiedOnLessThan, String modifiedOnGreaterThan, String closeDate, long size, long page) {
        StringBuilder builder = new StringBuilder();
        builder.append("Select " + QUERY_FIELDS + " from Opportunity WHERE")
                .append((isAvailable(name) ? " name LIKE '%" + soqlEscape(name) + "%' " + AND : ""))
                .append((isAvailable(id) ? " Id = '" + soqlEscape(id) + "' " + AND : ""))
                .append((isAvailable(stageName) ? " StageName LIKE '%" + soqlEscape(stageName) + "%' " + AND : ""))
                .append((isAvailable(nextStep) ? " NextStep LIKE '%" + soqlEscape(nextStep) + "%' " + AND : ""))
                .append((isAvailable(ownerId) ? " OwnerId = '" + soqlEscape(ownerId) + "' " + AND : ""))
                .append((isAvailable(ownerName) ? " Owner.Name = '" + soqlEscape(ownerName) + "' " + AND : ""))
                .append((isAvailable(accountId) ? " AccountId = '" + soqlEscape(accountId) + "' " + AND : ""))
                .append((isAvailable(accountName) ? " Account.Name = '" + soqlEscape(accountName) + "' " + AND : ""))
                .append((isAvailable(type) ? " Type = '" + soqlEscape(type) + "' " + AND : ""))
                .append((isAvailable(leadSource) ? " LeadSource = '" + soqlEscape(leadSource) + "' " + AND : ""))
                .append((isAvailable(partner) ? " Partner__c LIKE '%" + soqlEscape(partner) + "%' " + AND : ""))
                .append((isAvailable(competition) ? " Competition__c LIKE '%" + soqlEscape(competition) + "%' " + AND : ""))
                .append((isAvailable(amount) ? " amount = " + amount + AND : ""))
                .append((isAvailable(amountLessThan) ? " amount < " + amountLessThan + AND : ""))
                .append((isAvailable(amountGreaterThan) ? " amount > " + amountGreaterThan + AND : ""))
                .append((isAvailable(acv) ? " ACV__c = " + acv + AND : ""))
                .append((isAvailable(probability) ? " probability = " + probability + AND : ""))
                .append((isAvailable(probabilityLessThan) ? " probability < " + probabilityLessThan + AND : ""))
                .append((isAvailable(probabilityGreaterThan) ? " probability > " + probabilityGreaterThan + AND : ""))
                .append((isAvailable(createdOnLessThan) ? " CreatedDate < " + createdOnLessThan + AND : ""))
                .append((isAvailable(createdOnGreaterThan) ? " CreatedDate > " + createdOnGreaterThan + AND : ""))
                .append((isAvailable(modifiedOnLessThan) ? " LastModifiedDate < " + modifiedOnLessThan + AND : ""))
                .append((isAvailable(modifiedOnGreaterThan) ? " LastModifiedDate > " + modifiedOnGreaterThan + AND : ""))
                .append((isAvailable(closeDate) ? " CloseDate = " + closeDate + AND : ""));
        if (builder.substring(builder.length() - "WHERE".length()).equals("WHERE"))
            builder.setLength(builder.lastIndexOf("WHERE"));
        if (builder.substring(builder.length() - " AND ".length()).equals(" AND "))
            builder.setLength(builder.lastIndexOf(" AND "));
        builder.append(" ORDER BY LastModifiedDate LIMIT ").append(size).append(" OFFSET ").append(Math.max((page - 1) * size, 0));
        return builder.toString();
    }

    private static boolean isAvailable(String s) {
        return s != null && !s.isBlank();
    }

    private static boolean isAvailable(Double s) {
        return s != null;
    }
}
