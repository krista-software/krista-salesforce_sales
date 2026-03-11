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

import app.krista.extensions.crm.sales.salesforce_sales.model.SFOrderItem;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.*;

/**
 * Custom Gson deserializer for SFOrderItem that extracts dynamic custom fields.
 * 
 * This deserializer:
 * 1. Deserializes all standard OrderItem fields normally
 * 2. Identifies any field ending with __c that is NOT hardcoded
 * 3. Adds those fields to the customFields map
 * 
 * This allows retrieving ANY custom field from Salesforce without hardcoding them in the model.
 */
public class SFOrderItemDeserializer implements JsonDeserializer<SFOrderItem> {
    
    /**
     * Set of hardcoded custom fields that are already defined in SFOrderItem model.
     * These should NOT be added to the customFields map.
     * 
     * Note: Currently no hardcoded custom fields for OrderItem.
     * If you add hardcoded custom fields to SFOrderItem in the future, add them here.
     */
    private static final Set<String> HARDCODED_CUSTOM_FIELDS = new HashSet<>(Arrays.asList(
        // Add any hardcoded custom field names here if needed
        // Example: \"Custom_Field__c\"
    ));

    @Override
    public SFOrderItem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) 
            throws JsonParseException {
        
        JsonObject jsonObject = json.getAsJsonObject();
        
        // First, deserialize normally to get all standard fields
        Gson gson = new Gson();
        SFOrderItem orderItem = gson.fromJson(json, SFOrderItem.class);
        
        // Initialize customFields map if null
        if (orderItem.customFields == null) {
            orderItem.customFields = new HashMap<>();
        }
        
        // Iterate through all fields in the JSON response
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String fieldName = entry.getKey();

            // Check if it's a custom field (ends with __c) and NOT hardcoded
            if (fieldName.endsWith("__c") && !HARDCODED_CUSTOM_FIELDS.contains(fieldName)) {
                Object value = convertJsonElementToValue(entry.getValue());
                orderItem.customFields.put(fieldName, value);
            }
        }
        
        return orderItem;
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

