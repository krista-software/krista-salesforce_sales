package app.krista.extensions.crm.sales.salesforce_sales.util;

import java.util.*;

/**
 * Utility class for extracting and converting custom fields from Krista entity input
 * to Salesforce API format.
 * 
 * This class handles:
 * - Extracting custom fields from "Custom Fields" FreeForm object
 * - Converting field names to Salesforce API format (adds __c suffix)
 * - Converting Long/Double timestamps to YYYY-MM-DD date strings
 * - Filtering out standard fields
 * - Backward compatibility with direct custom field entries
 */
public class CustomFieldsExtractor {

    /**
     * Extracts custom fields from the entity attribute field map.
     * 
     * @param entityAttributeField Map containing all field values from Krista UI
     * @param standardFields Set of standard field names that should NOT be treated as custom fields
     * @param entityName Name of the entity (for logging purposes)
     * @return Map of custom fields with Salesforce API names and converted values
     */
    public static Map<String, Object> extractCustomFields(
            Map<String, Object> entityAttributeField,
            Set<String> standardFields,
            String entityName) {
        
        Map<String, Object> customFields = new HashMap<>();

        System.out.println("========================================");
        System.out.println(entityName + ".extractCustomFields() CALLED");
        System.out.println("Input map keys: " + entityAttributeField.keySet());
        System.out.println("========================================");

        // First, check for "Custom Fields" FreeForm object
        Object customFieldsObj = entityAttributeField.get("Custom Fields");
        if (customFieldsObj != null) {
            System.out.println("Found 'Custom Fields' object: " + customFieldsObj.getClass().getName());

            if (customFieldsObj instanceof Map) {
                Map<String, Object> customFieldsMap = (Map<String, Object>) customFieldsObj;
                System.out.println("Custom Fields Map contents: " + customFieldsMap);

                // Process all fields from the Custom Fields FreeForm
                for (Map.Entry<String, Object> entry : customFieldsMap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();

                    if (value != null) {
                        // Convert timestamps to date strings
                        Object convertedValue = convertDateValue(value);
                        
                        // Convert field name to Salesforce API format
                        String salesforceFieldName = convertFieldName(key);
                        
                        System.out.println("  -> Adding custom field from FreeForm: " + key + " -> " + salesforceFieldName + " = " + convertedValue);
                        customFields.put(salesforceFieldName, convertedValue);
                    }
                }
            }
        }

        // Also check for any custom fields passed directly in the root map (backward compatibility)
        System.out.println("Checking for custom fields in root map...");
        for (Map.Entry<String, Object> entry : entityAttributeField.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            // Skip if it's a standard field or the "Custom Fields" FreeForm itself
            if (standardFields.contains(key) || "Custom Fields".equals(key)) {
                continue;
            }

            // If the field ends with __c, it's a custom field
            if (key.endsWith("__c") && value != null) {
                Object convertedValue = convertDateValue(value);
                System.out.println("  -> Adding custom field from root map: " + key + " = " + convertedValue);
                customFields.put(key, convertedValue);
            }
        }

        System.out.println("Extracted custom fields: " + customFields);
        System.out.println("========================================");

        return customFields;
    }

    /**
     * Converts a field name to Salesforce API format.
     *
     * Handles three cases:
     * 1. Already ends with __c (e.g., "PO_Number__c") → return as-is
     * 2. Ends with _c (e.g., "Date_c") → replace _c with __c
     * 3. Otherwise (e.g., "PO Number") → replace spaces with _ and add __c
     *
     * @param fieldName Original field name from Krista UI
     * @return Salesforce API field name
     */
    private static String convertFieldName(String fieldName) {
        // Case 1: Already in correct Salesforce format (ends with __c)
        if (fieldName.endsWith("__c")) {
            return fieldName;
        }

        // Case 2: Ends with _c (user trying to match Salesforce field name but used single underscore)
        // Example: "Date_c" should become "Date__c" (replace _c with __c)
        if (fieldName.endsWith("_c") && !fieldName.endsWith("__c")) {
            return fieldName.substring(0, fieldName.length() - 2) + "__c";
        }

        // Case 3: Regular field name (e.g., "PO Number" or "Custom Field")
        // Replace spaces with underscores and add __c
        return fieldName.replace(" ", "_") + "__c";
    }

    /**
     * Converts date values from Long/Double timestamps to YYYY-MM-DD format.
     * Returns other values unchanged.
     * 
     * @param value Original value
     * @return Converted value (date string if timestamp, otherwise original value)
     */
    private static Object convertDateValue(Object value) {
        if (value instanceof Long timestamp) {
            if (timestamp > 0) {
                String dateString = DatePlus.getYYYYMMDD(timestamp);
                System.out.println("    -> Converted Long timestamp " + timestamp + " to date: " + dateString);
                return dateString;
            }
        } else if (value instanceof Double doubleValue) {
            long timestamp = doubleValue.longValue();
            if (timestamp > 0) {
                String dateString = DatePlus.getYYYYMMDD(timestamp);
                System.out.println("    -> Converted Double timestamp " + doubleValue + " to date: " + dateString);
                return dateString;
            }
        }
        return value;
    }
}

