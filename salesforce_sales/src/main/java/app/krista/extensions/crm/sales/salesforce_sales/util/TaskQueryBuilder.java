package app.krista.extensions.crm.sales.salesforce_sales.util;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.AND;

public final class TaskQueryBuilder {

    private static final String Query_Fields = "Id, OwnerId, Owner.Name, Subject, Priority, ActivityDate, Description, Status, AccountId, WhatId, WhoId, Account.Name, What.Name, What.Type";
    private static final String WHERE = "WHERE";

    private String assignedToName;
    private String priority;
    private String status;
    private String titleEquals;
    private String titleContains;
    private String titleStartsWith;
    private String titleEndsWith;
    private String dueDateEquals;
    private String dueDateLessThan;
    private String dueDateGreaterThan;
    private long size;
    private long page;

    private TaskQueryBuilder() {
    }

    public static TaskQueryBuilder builder() {
        return new TaskQueryBuilder();
    }

    public TaskQueryBuilder assignedToName(String assignedToName) {
        this.assignedToName = assignedToName;
        return this;
    }

    public TaskQueryBuilder priority(String priority) {
        this.priority = priority;
        return this;
    }

    public TaskQueryBuilder status(String status) {
        this.status = status;
        return this;
    }

    public TaskQueryBuilder titleEquals(String titleEquals) {
        this.titleEquals = titleEquals;
        return this;
    }

    public TaskQueryBuilder titleContains(String titleContains) {
        this.titleContains = titleContains;
        return this;
    }

    public TaskQueryBuilder titleStartsWith(String titleStartsWith) {
        this.titleStartsWith = titleStartsWith;
        return this;
    }

    public TaskQueryBuilder titleEndsWith(String titleEndsWith) {
        this.titleEndsWith = titleEndsWith;
        return this;
    }

    public TaskQueryBuilder dueDateEquals(String dueDateEquals) {
        this.dueDateEquals = dueDateEquals;
        return this;
    }

    public TaskQueryBuilder dueDateLessThan(String dueDateLessThan) {
        this.dueDateLessThan = dueDateLessThan;
        return this;
    }

    public TaskQueryBuilder dueDateGreaterThan(String dueDateGreaterThan) {
        this.dueDateGreaterThan = dueDateGreaterThan;
        return this;
    }

    public TaskQueryBuilder size(long size) {
        this.size = size;
        return this;
    }

    public TaskQueryBuilder page(long page) {
        this.page = page;
        return this;
    }

    public String build() {
        return createQuery(assignedToName, priority, status, titleEquals, titleContains, titleStartsWith, titleEndsWith,
                dueDateEquals, dueDateLessThan, dueDateGreaterThan, size, page);
    }


    private String createQuery(String assignedToName, String priority, String status, String titleEquals,
                               String titleContains, String titleStartsWith, String titleEndsWith, String dueDateEquals,
                               String dueDateLessThan, String dueDateGreaterThan, long size, long page) {
        StringBuilder builder = new StringBuilder();
        builder.append("Select " + Query_Fields + " from Task WHERE")
                .append((isAvailable(assignedToName) ? " Owner.Name = '" + assignedToName + "' " + AND : ""))
                .append((isAvailable(priority) ? " Priority = '" + priority + "' " + AND : ""))
                .append((isAvailable(status) ? " Status = '" + status + "' " + AND : ""))
                .append((isAvailable(titleEquals) ? " Subject = '" + titleEquals + "' " + AND : ""))
                .append((isAvailable(titleContains) ? " Subject LIKE '%" + titleContains + "%' " + AND : ""))
                .append((isAvailable(titleStartsWith) ? " Subject LIKE '" + titleStartsWith + "%' " + AND : ""))
                .append((isAvailable(titleEndsWith) ? " Subject LIKE '%" + titleEndsWith + "' " + AND : ""))
                .append((isAvailable(dueDateEquals) ? " ActivityDate = " + dueDateEquals + AND : ""))
                .append((isAvailable(dueDateLessThan) ? " ActivityDate < " + dueDateLessThan + AND : ""))
                .append((isAvailable(dueDateGreaterThan) ? " ActivityDate > " + dueDateGreaterThan + AND : ""));
        if (builder.substring(builder.length() - WHERE.length()).equals(WHERE))
            builder.setLength(builder.lastIndexOf(WHERE));
        if (builder.substring(builder.length() - AND.length()).equals(AND))
            builder.setLength(builder.lastIndexOf(AND));
        builder.append(" ORDER BY LastModifiedDate LIMIT ").append(size).append(" OFFSET ").append(Math.max((page - 1) * size, 0));
        return builder.toString();
    }

    private boolean isAvailable(String s) {
        return s != null && !s.isBlank();
    }

}


