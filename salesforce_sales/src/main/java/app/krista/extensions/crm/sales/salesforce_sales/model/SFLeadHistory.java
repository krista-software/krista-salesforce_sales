package app.krista.extensions.crm.sales.salesforce_sales.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SFLeadHistory {
    public int totalSize;
    public boolean done;
    public ArrayList<Record> records;

    public Map<String, Object> toSFHistoryMap() {
        Map<String, Object> newRecord = new HashMap<>();
        if (records != null && !records.isEmpty()) {
            newRecord.put(records.get(0).Field, records.get(0).NewValue);
            Long createdDate = records.get(0).CreatedDate.getTime();
            for (int i = 1; i < records.size(); i++) {
                Long newCreatedDate = records.get(i).CreatedDate.getTime();
                if (createdDate > newCreatedDate) {
                    return newRecord;
                } else if (createdDate == newCreatedDate) {
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
        public String LeadId;
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
