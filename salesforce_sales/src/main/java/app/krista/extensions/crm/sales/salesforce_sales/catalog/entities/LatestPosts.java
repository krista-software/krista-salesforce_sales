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


import app.krista.extension.impl.anno.Domain;
import app.krista.extension.impl.anno.Entity;
import app.krista.extension.impl.anno.Field;
import app.krista.model.base.File;

import java.util.List;

@Domain(id = "catEntryDomain_086f33da-aaea-4d0c-816f-3d5f20b4a273",
        name = "Sales",
        ecosystemId = "catEntryEcosystem_8a0e8ce7-6289-492f-9202-52284024280c",
        ecosystemName = "CRM",
        ecosystemVersion = "7ead9f04-08d3-4ef8-9243-90c167afee6a")
@Entity(name = "Latest Posts", id = "localDomainEntity_bef002d8-59ea-4789-b5fc-6d681333bec9", primaryKey = "Actor Name", supportStore = false, options = {})
public class LatestPosts {

    @Field.Text(name = "Actor Name", required = false, attributes = {}, options = {})
    public String actorName;

    @Field(name = "Post Message", type = "RichText", required = false, attributes = {}, options = {})
    public String postMessage;

    @Field.File(name = "Files", multipleFileUpload = true, required = false, attributes = {}, options = {})
    public List<File> files;

}
