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

import app.krista.extension.executor.QueryClause;
import app.krista.extension.executor.SearchCondition;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static app.krista.extensions.crm.sales.salesforce_sales.util.SFHelper.soqlEscape;

/**
 * Shared utility class with static methods for building SOQL query fragments
 * from {@link SearchCondition} and {@link QueryClause} objects, using
 * {@link SoqlFieldMapping} for field name and type resolution.
 */
public final class SoqlQueryHelper {

    /** Matches valid numeric values: integers, decimals, negative numbers */
    private static final Pattern NUMERIC_PATTERN = Pattern.compile("-?\\d+(\\.\\d+)?");

    /** Matches SOQL date literals: yyyy-MM-dd or full ISO-8601 datetime */
    private static final Pattern DATE_PATTERN = Pattern.compile(
            "\\d{4}-\\d{2}-\\d{2}(T\\d{2}:\\d{2}:\\d{2}(\\.\\d+)?(Z|[+-]\\d{4})?)?");

    private SoqlQueryHelper() {
    }

    /**
     * Build a complete SOQL WHERE clause (without timezone conversion for dates).
     * Delegates to {@link #buildWhereClause(String, List, String)} with null timezone.
     */
    public static String buildWhereClause(String entityName, List<SearchCondition> conditions) {
        return buildWhereClause(entityName, conditions, null);
    }

    /**
     * Build a complete SOQL WHERE clause from search conditions using SoqlFieldMapping.
     * <p>
     * Handles all operators: equals, notequals, contains, startswith, endswith, gt, lt, gte, lte.
     * Groups multiple equals on the same EXACT/IDENTITY field into IN clauses.
     * All string values are escaped with {@link SFHelper#soqlEscape(String)}.
     * <p>
     * When a timezone is provided, DATE field operands that are epoch millis are converted
     * to SOQL date format (yyyy-MM-dd) in the given timezone, matching the old query builder behavior.
     *
     * @param entityName the entity name (e.g. "Opportunity", "Task")
     * @param conditions the search conditions from SearchQuery
     * @param timeZone   the timezone for date conversion (e.g. "America/New_York"), or null to pass values as-is
     * @return the WHERE clause (without the "WHERE" keyword), or empty string if no conditions apply
     */
    public static String buildWhereClause(String entityName, List<SearchCondition> conditions, String timeZone) {
        if (conditions == null || conditions.isEmpty()) {
            return "";
        }

        List<String> whereParts = new ArrayList<>();
        // Group equals values by SOQL field for IN clause generation
        Map<String, List<String>> equalsGroups = new LinkedHashMap<>();
        // Group notequals values by SOQL field for NOT IN clause generation
        Map<String, List<String>> notEqualsGroups = new LinkedHashMap<>();

        for (SearchCondition sc : conditions) {
            String fieldName = sc.getFieldName();
            Object operand = sc.getOperand();
            SearchCondition.Operator operator = sc.getOperator();

            if (operand == null) continue;
            String value = String.valueOf(operand).trim();
            if (value.isEmpty()) continue;

            // Skip orderby conditions — they are handled by buildOrderByClause
            if (operator == SearchCondition.Operator.orderby) continue;

            SoqlFieldMapping mapping = SoqlFieldMapping.forEntity(entityName, fieldName);
            if (mapping == null) continue;

            String soqlField = mapping.getSoqlFieldName();
            SoqlFieldMapping.FieldType fieldType = mapping.getFieldType();

            String clause = buildConditionClause(soqlField, fieldType, operator, value, equalsGroups, notEqualsGroups, timeZone);
            if (clause != null) {
                whereParts.add(clause);
            }
        }

        // Build IN clauses for grouped equals conditions
        for (Map.Entry<String, List<String>> entry : equalsGroups.entrySet()) {
            String soqlField = entry.getKey();
            List<String> values = entry.getValue();
            if (values.size() == 1) {
                whereParts.add(soqlField + " = '" + soqlEscape(values.get(0)) + "'");
            } else {
                String inList = values.stream()
                        .map(v -> "'" + soqlEscape(v) + "'")
                        .collect(Collectors.joining(", "));
                whereParts.add(soqlField + " IN (" + inList + ")");
            }
        }

        // Build NOT IN clauses for grouped notequals conditions
        for (Map.Entry<String, List<String>> entry : notEqualsGroups.entrySet()) {
            String soqlField = entry.getKey();
            List<String> values = entry.getValue();
            if (values.size() == 1) {
                whereParts.add(soqlField + " != '" + soqlEscape(values.get(0)) + "'");
            } else {
                String inList = values.stream()
                        .map(v -> "'" + soqlEscape(v) + "'")
                        .collect(Collectors.joining(", "));
                whereParts.add(soqlField + " NOT IN (" + inList + ")");
            }
        }

        return String.join(" AND ", whereParts);
    }

    /**
     * Build a single WHERE condition clause, or add to grouped equals/notequals maps.
     *
     * @return the clause string, or null if the value was grouped for IN/NOT IN
     */
    private static String buildConditionClause(String soqlField, SoqlFieldMapping.FieldType fieldType,
                                                SearchCondition.Operator operator, String value,
                                                Map<String, List<String>> equalsGroups,
                                                Map<String, List<String>> notEqualsGroups,
                                                String timeZone) {
        // Determine effective operator (null defaults to equals for EXACT/IDENTITY/COMPARABLE/DATE, contains for TEXT)
        SearchCondition.Operator effectiveOp = operator;
        if (effectiveOp == null) {
            effectiveOp = (fieldType == SoqlFieldMapping.FieldType.TEXT)
                    ? SearchCondition.Operator.contains
                    : SearchCondition.Operator.equals;
        }

        switch (fieldType) {
            case TEXT:
                return buildTextClause(soqlField, effectiveOp, value);
            case EXACT:
                return buildExactClause(soqlField, effectiveOp, value, equalsGroups, notEqualsGroups);
            case COMPARABLE:
                return buildComparableClause(soqlField, effectiveOp, value);
            case DATE:
                return buildDateClause(soqlField, effectiveOp, value, timeZone);
            case IDENTITY:
                return buildIdentityClause(soqlField, effectiveOp, value, equalsGroups, notEqualsGroups);
            default:
                return null;
        }
    }

    private static String buildTextClause(String soqlField, SearchCondition.Operator op, String value) {
        String escaped = soqlEscape(value);
        switch (op) {
            case contains:
                return soqlField + " LIKE '%" + escaped + "%'";
            case startswith:
                return soqlField + " LIKE '" + escaped + "%'";
            case endswith:
                return soqlField + " LIKE '%" + escaped + "'";
            case equals:
                return soqlField + " = '" + escaped + "'";
            case notequals:
                return soqlField + " != '" + escaped + "'";
            default:
                // Fallback: treat as contains for text fields
                return soqlField + " LIKE '%" + escaped + "%'";
        }
    }

    private static String buildExactClause(String soqlField, SearchCondition.Operator op, String value,
                                            Map<String, List<String>> equalsGroups,
                                            Map<String, List<String>> notEqualsGroups) {
        switch (op) {
            case equals:
                equalsGroups.computeIfAbsent(soqlField, k -> new ArrayList<>()).add(value);
                return null; // Will be emitted as IN clause later
            case notequals:
                notEqualsGroups.computeIfAbsent(soqlField, k -> new ArrayList<>()).add(value);
                return null; // Will be emitted as NOT IN clause later
            case contains:
                // Fallback for contains on exact fields: use equals
                return soqlField + " = '" + soqlEscape(value) + "'";
            default:
                return soqlField + " = '" + soqlEscape(value) + "'";
        }
    }

    private static String buildComparableClause(String soqlField, SearchCondition.Operator op, String value) {
        // Validate numeric format to prevent SOQL injection (values are unquoted)
        if (!NUMERIC_PATTERN.matcher(value).matches()) {
            return null; // skip invalid non-numeric values
        }
        switch (op) {
            case equals:
                return soqlField + " = " + value;
            case notequals:
                return soqlField + " != " + value;
            case gt:
                return soqlField + " > " + value;
            case lt:
                return soqlField + " < " + value;
            case gte:
                return soqlField + " >= " + value;
            case lte:
                return soqlField + " <= " + value;
            default:
                return soqlField + " = " + value;
        }
    }

    private static String buildDateClause(String soqlField, SearchCondition.Operator op, String value, String timeZone) {
        // Convert epoch millis to SOQL date format if the value looks numeric
        // This matches the old behavior in OpportunityQueryBuilder/TaskQueryBuilder
        // which used DatePlus.getDateForGivenTimeZone() for conversion
        String dateValue = convertToSoqlDate(value, timeZone);

        // Validate the final date value format to prevent SOQL injection (values are unquoted)
        if (!DATE_PATTERN.matcher(dateValue).matches() && !NUMERIC_PATTERN.matcher(dateValue).matches()) {
            return null; // skip invalid date values
        }

        switch (op) {
            case equals:
                return soqlField + " = " + dateValue;
            case notequals:
                return soqlField + " != " + dateValue;
            case gt:
                return soqlField + " > " + dateValue;
            case lt:
                return soqlField + " < " + dateValue;
            case gte:
                return soqlField + " >= " + dateValue;
            case lte:
                return soqlField + " <= " + dateValue;
            default:
                return soqlField + " = " + dateValue;
        }
    }

    /**
     * Converts a date value to SOQL date format.
     * If the value is epoch millis (numeric), converts to yyyy-MM-dd using the provided timezone.
     * If the value is already a date string (yyyy-MM-dd or ISO-8601), passes through as-is.
     */
    private static String convertToSoqlDate(String value, String timeZone) {
        // If already in date format, pass through
        if (DATE_PATTERN.matcher(value).matches()) {
            return value;
        }

        // Try to parse as epoch millis (matching old DatePlus.convertDateToSpecificTimeZone behavior)
        try {
            double timestamp = Double.parseDouble(value);
            long epoch = (long) timestamp;
            Date date = new Date(epoch);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            if (timeZone != null && !timeZone.isEmpty()) {
                dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
            }
            return dateFormat.format(date);
        } catch (NumberFormatException e) {
            // Not epoch millis, return as-is
            return value;
        }
    }

    private static String buildIdentityClause(String soqlField, SearchCondition.Operator op, String value,
                                               Map<String, List<String>> equalsGroups,
                                               Map<String, List<String>> notEqualsGroups) {
        switch (op) {
            case equals:
                equalsGroups.computeIfAbsent(soqlField, k -> new ArrayList<>()).add(value);
                return null; // Will be emitted as IN clause later
            case notequals:
                notEqualsGroups.computeIfAbsent(soqlField, k -> new ArrayList<>()).add(value);
                return null; // Will be emitted as NOT IN clause later
            default:
                return soqlField + " = '" + soqlEscape(value) + "'";
        }
    }

    /**
     * Build ORDER BY clause from QueryClauses.
     * Falls back to "ORDER BY LastModifiedDate DESC" (or "ORDER BY CreatedDate DESC"
     * for entities without LastModifiedDate) if no clauses are provided.
     *
     * @param entityName the entity name
     * @param clauses    the query clauses from SearchQuery
     * @return the ORDER BY clause including the "ORDER BY" keyword
     */
    public static String buildOrderByClause(String entityName, List<QueryClause> clauses) {
        List<String> orderParts = new ArrayList<>();

        if (clauses != null) {
            for (QueryClause qc : clauses) {
                if (qc.getOperator() == QueryClause.Operator.orderby) {
                    String orderField = SoqlFieldMapping.orderByField(entityName, qc.getFieldName());
                    if (orderField != null) {
                        String dir = qc.getOperand() != null
                                ? parseDirection(String.valueOf(qc.getOperand()).trim())
                                : "DESC";
                        orderParts.add(orderField + " " + dir);
                    }
                }
            }
        }

        if (!orderParts.isEmpty()) {
            return " ORDER BY " + String.join(", ", orderParts);
        }

        // Default: try LastModifiedDate, fall back to CreatedDate
        return getDefaultOrderBy(entityName);
    }

    /**
     * Build pagination clause (LIMIT and OFFSET).
     *
     * @param pageSize  the number of records per page
     * @param pageIndex the 1-based page index
     * @return the LIMIT/OFFSET clause
     */
    public static String buildPaginationClause(long pageSize, long pageIndex) {
        long offset = Math.max((pageIndex - 1) * pageSize, 0);
        return " LIMIT " + pageSize + " OFFSET " + offset;
    }

    /**
     * Resolve owner/user name conditions to User IDs.
     * <p>
     * When a condition targets a user name field ("Owner Name", "Assigned To Name")
     * with contains/equals operator, search for matching users via UserDao and replace
     * with OwnerId equals/IN condition.
     *
     * @param conditions the original search conditions
     * @param entityName the entity name (used for field mapping resolution)
     * @param userDao    the UserDao for searching users by name
     * @return the resolved conditions with user name conditions replaced by ID conditions
     */
    public static List<SearchCondition> resolveOwnerConditions(
            List<SearchCondition> conditions,
            String entityName,
            app.krista.extensions.crm.sales.salesforce_sales.service.UserDao userDao) {
        if (conditions == null || userDao == null) {
            return conditions;
        }

        List<SearchCondition> resolved = new ArrayList<>();
        for (SearchCondition sc : conditions) {
            String fieldName = sc.getFieldName();
            if (fieldName != null && isUserNameField(fieldName) && sc.getOperand() != null) {
                String nameQuery = String.valueOf(sc.getOperand()).trim();
                if (!nameQuery.isEmpty()) {
                    String targetIdField = resolveUserNameToIdField(fieldName);
                    List<app.krista.extensions.crm.sales.salesforce_sales.service.User> users =
                            searchUsersByName(userDao, nameQuery);
                    if (!users.isEmpty()) {
                        if (users.size() == 1) {
                            resolved.add(new SearchCondition(
                                    SearchCondition.Operator.equals,
                                    users.get(0).getId(),
                                    targetIdField));
                        } else {
                            // Add multiple equals conditions which will be grouped into IN clause
                            for (app.krista.extensions.crm.sales.salesforce_sales.service.User user : users) {
                                resolved.add(new SearchCondition(
                                        SearchCondition.Operator.equals,
                                        user.getId(),
                                        targetIdField));
                            }
                        }
                        continue;
                    }
                }
            }
            resolved.add(sc);
        }
        return resolved;
    }

    // ── Private helpers ────────────────────────────────────────────────────

    private static final Set<String> USER_NAME_FIELDS = Set.of("owner name", "assigned to name");

    private static boolean isUserNameField(String fieldName) {
        return USER_NAME_FIELDS.contains(fieldName.toLowerCase(Locale.ROOT));
    }

    /**
     * Maps user name field names to their corresponding ID field names.
     * "Owner Name" and "Assigned To Name" both resolve to "Owner Id".
     */
    private static String resolveUserNameToIdField(String userNameField) {
        String lower = userNameField.toLowerCase(Locale.ROOT);
        if ("owner name".equals(lower) || "assigned to name".equals(lower)) {
            return "Owner Id";
        }
        return "Owner Id";
    }

    private static List<app.krista.extensions.crm.sales.salesforce_sales.service.User> searchUsersByName(
            app.krista.extensions.crm.sales.salesforce_sales.service.UserDao userDao, String name) {
        String escaped = soqlEscape(name);
        String query = "SELECT Id, Name FROM User WHERE Name LIKE '%" + escaped + "%'";
        try {
            return userDao.getUsersByQuery(query);
        } catch (Exception e) {
            return List.of();
        }
    }

    private static String parseDirection(String value) {
        if (value == null) return "DESC";
        String upper = value.toUpperCase(Locale.ROOT).trim();
        if ("ASC".equals(upper) || "ASCENDING".equals(upper)) return "ASC";
        return "DESC";
    }

    private static final Set<String> ENTITIES_WITHOUT_LAST_MODIFIED = Set.of("account", "order");

    private static String getDefaultOrderBy(String entityName) {
        if (entityName != null && ENTITIES_WITHOUT_LAST_MODIFIED.contains(entityName.toLowerCase(Locale.ROOT))) {
            return " ORDER BY CreatedDate DESC";
        }
        return " ORDER BY LastModifiedDate DESC";
    }
}
