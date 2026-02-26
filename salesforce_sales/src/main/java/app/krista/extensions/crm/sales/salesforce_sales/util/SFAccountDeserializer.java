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

