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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public final class DatePlus {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatePlus.class);

    private DatePlus() {
    }

    public static String getISODate(Date date) {
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(date);
    }

    public static String getISODate(Long date) {
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(date);
    }

    public static String getYYYYMMDD(long date) {
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date(date));
    }


    public static Date fromYYYYMMDD(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date parseDate(Object dueDate) {
        if (dueDate != null && dueDate instanceof String) {
            String dateString = (String) dueDate;
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                return dateFormat.parse(dateString);
            } catch (ParseException cause) {
                LOGGER.error(cause.getMessage());
            }
        }
        return null;
    }

    public static String getDateForGivenTimeZoneWithQueryOperand(List<SearchCondition> conditions, String fieldName, String queryOperand, String timeZone) {
        for (SearchCondition con : conditions) {
            if (fieldName.equalsIgnoreCase(con.getFieldName()) && con.getOperator().toString().equalsIgnoreCase(queryOperand)) {
                return convertDateToSpecificTimeZone(timeZone, con);
            }
        }
        return null;
    }

    private static String convertDateToSpecificTimeZone(String timeZone, SearchCondition con) {
        if (con.getOperand() == null) {
            return null;
        } else {
            double timestamp = Double.parseDouble(String.valueOf(con.getOperand()));
            long epoch = (long) timestamp;
            Date date = new Date(epoch);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
            return dateFormat.format(date);
        }
    }

    public static String getDateForGivenTimeZone(List<SearchCondition> conditions, String fieldName, String timeZone) {
        for (SearchCondition con : conditions) {
            if (fieldName.equalsIgnoreCase(con.getFieldName())) {
                return convertDateToSpecificTimeZone(timeZone, con);
            }
        }
        return null;
    }

    public static QuarterDates getQuarterDates(String quarter) {
        Date today = new Date();
        int current_quarter = (int) Math.floor((today.getMonth() / 3));
        int selected_quarter;
        try {
            selected_quarter = Integer.parseInt(quarter.substring(quarter.length() - 1)) - 1;
        } catch (NumberFormatException cause) {
            throw new IllegalArgumentException("Invalid quarter format. Please provide a valid quarter (e.g., 'Q1', 'Q2', 'Q3', or 'Q4').");
        }

        Date startFullQuarter;
        Date endFullQuarter;
        if (current_quarter <= selected_quarter) {
            startFullQuarter = new Date(today.getYear(), selected_quarter * 3, 1);
            endFullQuarter = new Date(today.getYear(), startFullQuarter.getMonth() + 3, 0);
        } else {
            startFullQuarter = new Date(today.getYear() + 1, selected_quarter * 3, 1);
            endFullQuarter = new Date(today.getYear() + 1, startFullQuarter.getMonth() + 3, 0);
        }

        String start = DatePlus.getYYYYMMDD(startFullQuarter.getTime());
        String todayISO = DatePlus.getYYYYMMDD(today.getTime());
        String end = DatePlus.getYYYYMMDD(endFullQuarter.getTime());

        return new QuarterDates(start, todayISO, end);
    }


    public static class QuarterDates {

        public final String startDate;
        public final String endDate;
        public final String current;

        QuarterDates(String startDate, String current, String endDate) {
            this.startDate = startDate;
            this.current = current;
            this.endDate = endDate;
        }
    }
}