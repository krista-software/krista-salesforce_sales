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

import app.krista.extensions.crm.sales.salesforce_sales.model.SFAccount;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.*;

/**
 * Custom Gson deserializer for SFAccount that:
 * 1. Deserializes all standard fields normally
 * 2. Captures dynamic custom fields (ending with __c) into the customFields map
 */
public class SFAccountDeserializer implements JsonDeserializer<SFAccount> {

    // List of hardcoded custom fields that are already mapped in SFAccount
    private static final Set<String> HARDCODED_CUSTOM_FIELDS = new HashSet<>(Arrays.asList(
        "SLASerialNumber__c",
        "SLAExpirationDate__c",
        "CustomerPriority__c"
    ));

    @Override
    public SFAccount deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        
        JsonObject jsonObject = json.getAsJsonObject();
        
        // First, deserialize normally using Gson (this handles all @SerializedName fields)
        Gson gson = new Gson();
        SFAccount account = gson.fromJson(json, SFAccount.class);
        
        // Now extract dynamic custom fields
        Map<String, Object> customFields = new HashMap<>();
        
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String fieldName = entry.getKey();
            
            // Check if this is a custom field (ends with __c) and NOT a hardcoded one
            if (fieldName.endsWith("__c") && !HARDCODED_CUSTOM_FIELDS.contains(fieldName)) {
                JsonElement value = entry.getValue();
                
                // Convert JsonElement to appropriate Java type
                if (value.isJsonNull()) {
                    customFields.put(fieldName, null);
                } else if (value.isJsonPrimitive()) {
                    JsonPrimitive primitive = value.getAsJsonPrimitive();
                    if (primitive.isString()) {
                        customFields.put(fieldName, primitive.getAsString());
                    } else if (primitive.isNumber()) {
                        customFields.put(fieldName, primitive.getAsNumber());
                    } else if (primitive.isBoolean()) {
                        customFields.put(fieldName, primitive.getAsBoolean());
                    }
                } else {
                    // For complex types, store as string
                    customFields.put(fieldName, value.toString());
                }
                
            }
        }

        account.customFields = customFields;

        return account;
    }
}

