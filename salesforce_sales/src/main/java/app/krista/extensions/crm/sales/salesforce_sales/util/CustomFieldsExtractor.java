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

    private static final String CUSTOM_FIELDS_KEY = "Custom Fields";
    private static final String CUSTOM_FIELD_SUFFIX = "__c";
    private static final String SINGLE_C_SUFFIX = "_c";

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

        extractFromFreeFormObject(entityAttributeField, customFields);
        extractFromRootMap(entityAttributeField, standardFields, customFields);

        return customFields;
    }

    /**
     * Extracts custom fields from the "Custom Fields" FreeForm object.
     *
     * @param entityAttributeField Map containing all field values from Krista UI
     * @param customFields Map to populate with extracted custom fields
     */
    private static void extractFromFreeFormObject(
            Map<String, Object> entityAttributeField,
            Map<String, Object> customFields) {

        Object customFieldsObj = entityAttributeField.get(CUSTOM_FIELDS_KEY);
        if (!(customFieldsObj instanceof Map)) {
            return;
        }

        Map<String, Object> customFieldsMap = (Map<String, Object>) customFieldsObj;

        for (Map.Entry<String, Object> entry : customFieldsMap.entrySet()) {
            processCustomFieldEntry(entry, customFields);
        }
    }

    /**
     * Processes a single custom field entry from the FreeForm object.
     *
     * @param entry Map entry containing field name and value
     * @param customFields Map to populate with processed custom field
     */
    private static void processCustomFieldEntry(
            Map.Entry<String, Object> entry,
            Map<String, Object> customFields) {

        String key = entry.getKey();
        Object value = entry.getValue();

        if (value == null) {
            return;
        }

        Object convertedValue = convertDateValue(value);
        String salesforceFieldName = convertFieldName(key);

        customFields.put(salesforceFieldName, convertedValue);
    }

    /**
     * Extracts custom fields passed directly in the root map (backward compatibility).
     *
     * @param entityAttributeField Map containing all field values from Krista UI
     * @param standardFields Set of standard field names to exclude
     * @param customFields Map to populate with extracted custom fields
     */
    private static void extractFromRootMap(
            Map<String, Object> entityAttributeField,
            Set<String> standardFields,
            Map<String, Object> customFields) {

        for (Map.Entry<String, Object> entry : entityAttributeField.entrySet()) {
            processRootMapEntry(entry, standardFields, customFields);
        }
    }

    /**
     * Processes a single entry from the root map.
     *
     * @param entry Map entry containing field name and value
     * @param standardFields Set of standard field names to exclude
     * @param customFields Map to populate with processed custom field
     */
    private static void processRootMapEntry(
            Map.Entry<String, Object> entry,
            Set<String> standardFields,
            Map<String, Object> customFields) {

        String key = entry.getKey();
        Object value = entry.getValue();

        if (shouldSkipEntry(key, standardFields, value)) {
            return;
        }

        Object convertedValue = convertDateValue(value);
        customFields.put(key, convertedValue);
    }

    /**
     * Determines if a root map entry should be skipped.
     *
     * @param key Field name
     * @param standardFields Set of standard field names
     * @param value Field value
     * @return true if entry should be skipped, false otherwise
     */
    private static boolean shouldSkipEntry(String key, Set<String> standardFields, Object value) {
        // Skip if it's a standard field or the "Custom Fields" FreeForm itself
        if (standardFields.contains(key) || CUSTOM_FIELDS_KEY.equals(key)) {
            return true;
        }

        // Skip if value is null
        if (value == null) {
            return true;
        }

        // Only process fields that end with __c (custom fields)
        return !key.endsWith(CUSTOM_FIELD_SUFFIX);
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
        if (fieldName.endsWith(CUSTOM_FIELD_SUFFIX)) {
            return fieldName;
        }

        // Case 2: Ends with _c (user trying to match Salesforce field name but used single underscore)
        // Example: "Date_c" should become "Date__c" (replace _c with __c)
        if (fieldName.endsWith(SINGLE_C_SUFFIX)) {
            return fieldName.substring(0, fieldName.length() - 2) + CUSTOM_FIELD_SUFFIX;
        }

        // Case 3: Regular field name (e.g., "PO Number" or "Custom Field")
        // Replace spaces with underscores and add __c
        return fieldName.replace(" ", "_") + CUSTOM_FIELD_SUFFIX;
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
            return convertTimestamp(timestamp);
        }

        if (value instanceof Double doubleValue) {
            return convertTimestamp(doubleValue.longValue());
        }

        return value;
    }

    /**
     * Converts a timestamp to YYYY-MM-DD format if valid.
     *
     * @param timestamp The timestamp value
     * @return Date string if timestamp > 0, otherwise the original timestamp
     */
    private static Object convertTimestamp(long timestamp) {
        if (timestamp > 0) {
            return DatePlus.getYYYYMMDD(timestamp);
        }
        return timestamp;
    }
}
