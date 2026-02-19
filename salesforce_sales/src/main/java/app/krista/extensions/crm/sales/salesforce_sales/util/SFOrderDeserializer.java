package app.krista.extensions.crm.sales.salesforce_sales.util;

import app.krista.extensions.crm.sales.salesforce_sales.model.SFOrder;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.*;

/**
 * Custom Gson deserializer for SFOrder that extracts dynamic custom fields.
 * 
 * This deserializer:
 * 1. Deserializes all standard Order fields normally
 * 2. Identifies any field ending with __c that is NOT hardcoded
 * 3. Adds those fields to the customFields map
 * 
 * This allows retrieving ANY custom field from Salesforce without hardcoding them in the model.
 */
public class SFOrderDeserializer implements JsonDeserializer<SFOrder> {
    
    /**
     * Set of hardcoded custom fields that are already defined in SFOrder model.
     * These should NOT be added to the customFields map.
     * 
     * Note: Currently no hardcoded custom fields for Order.
     * If you add hardcoded custom fields to SFOrder in the future, add them here.
     */
    private static final Set<String> HARDCODED_CUSTOM_FIELDS = new HashSet<>(Arrays.asList(
        // Add any hardcoded custom field names here if needed
        // Example: "PO_Number__c", "Extraction_Status__c"
    ));

    @Override
    public SFOrder deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) 
            throws JsonParseException {
        
        JsonObject jsonObject = json.getAsJsonObject();
        
        // First, deserialize normally to get all standard fields
        Gson gson = new Gson();
        SFOrder order = gson.fromJson(json, SFOrder.class);
        
        // Initialize customFields map if null
        if (order.customFields == null) {
            order.customFields = new HashMap<>();
        }
        
        System.out.println("========================================");
        System.out.println("SFOrderDeserializer - Extracting custom fields from Salesforce response");
        
        // Iterate through all fields in the JSON response
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String fieldName = entry.getKey();
            
            // Check if it's a custom field (ends with __c) and NOT hardcoded
            if (fieldName.endsWith("__c") && !HARDCODED_CUSTOM_FIELDS.contains(fieldName)) {
                Object value = convertJsonElementToValue(entry.getValue());
                order.customFields.put(fieldName, value);
                System.out.println("  -> Extracted custom field from Salesforce response: " + fieldName + " = " + value);
            }
        }
        
        System.out.println("Total custom fields extracted: " + order.customFields.size());
        System.out.println("========================================");
        
        return order;
    }
    
    /**
     * Converts a JsonElement to an appropriate Java type.
     * 
     * @param element JsonElement to convert
     * @return Java object (String, Number, Boolean, or null)
     */
    private Object convertJsonElementToValue(JsonElement element) {
        if (element == null || element.isJsonNull()) {
            return null;
        }
        
        if (element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();
            
            if (primitive.isString()) {
                return primitive.getAsString();
            } else if (primitive.isNumber()) {
                return primitive.getAsNumber();
            } else if (primitive.isBoolean()) {
                return primitive.getAsBoolean();
            }
        }
        
        // For complex types (objects, arrays), convert to string
        return element.toString();
    }
}

