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

package app.krista.extensions.crm.sales.salesforce_sales.impl.stores;

import app.krista.model.base.FreeForm;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class ContentDaoImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContentDaoImpl.class);

    public FreeForm parseJSONToMap(String jsonResponse, List<String> customFields) {
        FreeForm resultMap = new FreeForm();
        try {
            processJSONResponse(jsonResponse, customFields, resultMap);
        } catch (JSONException exception) {
            LOGGER.info("Exception occurred due to :" + exception.getMessage(), exception);
        }
        return resultMap;
    }

    private static void processJSONResponse(String jsonResponse, List<String> customFields, FreeForm resultMap) {
        if (jsonResponse != null) {
            JSONObject jsonObject = new JSONObject(jsonResponse);

            if (jsonObject.has("records")) {
                JSONArray records = jsonObject.getJSONArray("records");

                if (records.length() > 0) {
                    JSONObject record = records.getJSONObject(0);

                    processRecordFields(customFields, resultMap, record);
                } else {
                    LOGGER.info("No records found in the JSON response.");
                }
            } else {
                LOGGER.info("No 'records' array found in the JSON response.");
            }
        } else {
            LOGGER.info("JSON response is null.");
        }
    }

    private static void processRecordFields(List<String> customFields, FreeForm resultMap, JSONObject record) {
        if (record != null) {
            for (String key : customFields) {
                if (record.has(key)) {
                    Object value = record.get(key);

                    if (value.equals(null)) {
                        resultMap.put(key, "Text", "");
                    } else {
                        resultMap.put(key, "Text", value.toString());
                    }
                }
            }
        } else {
            LOGGER.info("First record is null.");
        }
    }

}