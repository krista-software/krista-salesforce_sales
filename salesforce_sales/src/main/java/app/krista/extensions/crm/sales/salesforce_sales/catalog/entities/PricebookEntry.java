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

package app.krista.extensions.crm.sales.salesforce_sales.catalog.entities;


import app.krista.extension.impl.anno.*;

import java.util.LinkedHashMap;
import java.util.Map;

@Domain(id = "catEntryDomain_086f33da-aaea-4d0c-816f-3d5f20b4a273",
        name = "Sales",
        ecosystemId = "catEntryEcosystem_8a0e8ce7-6289-492f-9202-52284024280c",
        ecosystemName = "CRM",
        ecosystemVersion = "db320c64-6f6f-4c9e-8daa-41d3f796d6a6")
@Entity(name = "Pricebook Entry", id = "localDomainEntity_cad67c19-86fd-4220-9768-c190a70f9fa5", primaryKey = "Id", supportStore = true,
        description = "Represents a product's price in a specific pricebook in Salesforce. Product Id references Product.Id. Referenced by OrderItem.Pricebook Entry Id.")
@Searchable
public class PricebookEntry {

    @Field(name = "Id", type = "Text", required = false, description = "Unique Salesforce identifier for the pricebook entry. Referenced by OrderItem.Pricebook Entry Id.")
    public String id;

    @ToString
    @Field(name = "Name", type = "Text", required = false, description = "Name of the pricebook entry.")
    public String name;

    @Field(name = "Pricebook Id", type = "Text", required = true, description = "Id of the pricebook this entry belongs to.")
    public String pricebookId;

    @Field(name = "Product Id", type = "Text", required = true, description = "Id of the associated product. References Product.Id.")
    public String productId;

    @Field(name = "Product Code", type = "Text", required = false, description = "Product code from the associated product.")
    public String productCode;

    @Field(name = "Created Date", type = "Date", required = false, attributes = @Attribute(name = "includeTimeOfDay", value = "true"), description = "Date when the pricebook entry was created.")
    public Long createdDate;

    @Field(name = "Last Modified Date", type = "Date", required = false, attributes = @Attribute(name = "includeTimeOfDay", value = "true"), description = "Date when the pricebook entry was last modified.")
    public Long lastModifiedDate;

    @Field(name = "Unit Price", type = "Currency", required = true, description = "Price of the product in this pricebook.")
    public Double unitPrice;

    public static PricebookEntry fromFields(Map<String, Object> fields) {
        PricebookEntry pricebookEntry = new PricebookEntry();
        pricebookEntry.id = (String) fields.get("Id");
        pricebookEntry.name = (String) fields.get("Name");
        pricebookEntry.pricebookId = (String) fields.get("Pricebook Id");
        pricebookEntry.productId = (String) fields.get("Product Id");
        pricebookEntry.productCode = (String) fields.get("Product Code");
        pricebookEntry.createdDate = (Long) fields.get("Created Date");
        pricebookEntry.lastModifiedDate = (Long) fields.get("Last Modified Date");
        pricebookEntry.unitPrice = (Double) fields.get("Unit Price");
        return pricebookEntry;
    }

    /**
     * Converts this PricebookEntry entity to a Map of field values.
     *
     * @return Map containing pricebook entry field values in declaration order
     */
    public Map<String, Object> toFields() {
        Map<String, Object> fields = new LinkedHashMap<>();
        fields.put("Id", id);
        fields.put("Name", name);
        fields.put("Pricebook Id", pricebookId);
        fields.put("Product Id", productId);
        fields.put("Product Code", productCode);
        fields.put("Created Date", createdDate);
        fields.put("Last Modified Date", lastModifiedDate);
        fields.put("Unit Price", unitPrice);
        return fields;
    }

}
