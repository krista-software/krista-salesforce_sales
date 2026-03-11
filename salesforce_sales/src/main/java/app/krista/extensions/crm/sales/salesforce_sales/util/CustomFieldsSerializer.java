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

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Map;

/**
 * Utility class for serializing Salesforce entities with custom fields flattened to root JSON.
 * 
 * This class handles the serialization challenge where:
 * - Standard Gson serialization creates nested objects: {"customFields": {"PO_Number__c": "value"}}
 * - Salesforce requires flattened format: {"PO_Number__c": "value"} at root level
 * 
 * The utility flattens custom fields into the root JSON object before sending to Salesforce.
 */
public class CustomFieldsSerializer {

    private static final Gson GSON = new Gson();

    /**
     * Serializes an entity with custom fields flattened to root JSON.
     * 
     * @param entity The Salesforce entity object (SFAccount, SFOrder, SFOrderItem, etc.)
     * @param customFields Map of custom fields to flatten into root JSON
     * @param entityName Name of the entity (for logging purposes)
     * @return JSON string with custom fields flattened to root level
     */
    public static String serializeWithCustomFields(
            Object entity,
            Map<String, Object> customFields,
            String entityName) {
        
        // First, serialize the entity normally (this will include all standard fields)
        JsonObject entityJson = GSON.toJsonTree(entity).getAsJsonObject();

        // Remove the customFields property if it exists (it shouldn't be serialized as-is)
        entityJson.remove("customFields");

        // Add each custom field directly to the root JSON object
        if (customFields != null && !customFields.isEmpty()) {
            for (Map.Entry<String, Object> entry : customFields.entrySet()) {
                String fieldName = entry.getKey();
                Object fieldValue = entry.getValue();

                // Add the custom field to the root JSON object based on type
                if (fieldValue instanceof String) {
                    entityJson.addProperty(fieldName, (String) fieldValue);
                } else if (fieldValue instanceof Number) {
                    entityJson.addProperty(fieldName, (Number) fieldValue);
                } else if (fieldValue instanceof Boolean) {
                    entityJson.addProperty(fieldName, (Boolean) fieldValue);
                } else if (fieldValue != null) {
                    entityJson.addProperty(fieldName, fieldValue.toString());
                }
            }
        }

        return GSON.toJson(entityJson);
    }
}

