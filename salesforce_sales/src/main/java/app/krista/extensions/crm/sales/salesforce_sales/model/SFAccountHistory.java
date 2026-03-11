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

package app.krista.extensions.crm.sales.salesforce_sales.model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SFAccountHistory {
    public int totalSize;
    public boolean done;
    public List<Record> records;

    public Map<String, Object> toMap() {
        Map<String, Object> newRecord = new HashMap<>();
        if (records != null && !records.isEmpty()) {
            newRecord.put(records.get(0).Field, records.get(0).NewValue);
            Long createdDate = records.get(0).CreatedDate.getTime();
            for (int i = 1; i < records.size(); i++) {
                Long newCreatedDate = records.get(i).CreatedDate.getTime();
                if (createdDate > newCreatedDate) {
                    return newRecord;
                } else if (createdDate.equals(newCreatedDate)) {
                    newRecord.put(records.get(i).Field, records.get(i).NewValue);
                }
            }
        }
        return newRecord;
    }

    public class Record {
        public Attributes attributes;
        public String Id;
        public boolean IsDeleted;
        public String AccountId;
        public String CreatedById;
        public Date CreatedDate;
        public String Field;
        public String DataType;
        public String OldValue;
        public String NewValue;

        public class Attributes {
            public String type;
            public String url;
        }
    }
}
