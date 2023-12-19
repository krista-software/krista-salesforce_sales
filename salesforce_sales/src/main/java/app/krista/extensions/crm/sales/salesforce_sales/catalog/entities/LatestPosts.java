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
@Entity(name = "Latest Posts", id = "localDomainEntity_bef002d8-59ea-4789-b5fc-6d681333bec9", primaryKey = "Actor Name", options = {})
public class LatestPosts {

    @Field.Text(name = "Actor Name", required = false, attributes = {}, options = {})
    public String actorName;

    @Field(name = "Post Message", type = "RichText", required = false, attributes = {}, options = {})
    public String postMessage;

    @Field.File(name = "Files", multipleFileUpload = true, required = false, attributes = {}, options = {})
    public List<File> files;

}
