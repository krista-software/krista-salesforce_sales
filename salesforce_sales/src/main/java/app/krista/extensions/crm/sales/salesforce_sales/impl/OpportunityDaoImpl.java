package app.krista.extensions.crm.sales.salesforce_sales.impl;

import app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes.OpportunityCatalogType;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.LatestPosts;
import app.krista.extensions.crm.sales.salesforce_sales.util.MediaClient;
import app.krista.extensions.crm.sales.salesforce_sales.model.Quota;
import app.krista.extensions.crm.sales.salesforce_sales.model.RegionWiseSummary;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFAggOpportunityOwnerTotal;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFOpportunity;
import app.krista.extensions.crm.sales.salesforce_sales.service.Opportunity;
import app.krista.extensions.crm.sales.salesforce_sales.service.OpportunityDao;
import app.krista.extensions.crm.sales.salesforce_sales.service.PriceBook;
import app.krista.extensions.crm.sales.salesforce_sales.service.User;
import app.krista.extensions.crm.sales.salesforce_sales.util.DatePlus;
import app.krista.extensions.crm.sales.salesforce_sales.util.SalesforceListResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.model.Response;
import com.google.gson.reflect.TypeToken;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.GSON;

@Service
public class OpportunityDaoImpl implements OpportunityDao {

    private static final String OPPORTUNITY = "Opportunity";
    public static final String QUERY_FIELDS = "id, Name, StageName, NextStep, Amount, ACV__c, AccountId, Account.Name, ContactId, Pricebook2Id, type, LeadSource, Partner__c, Competition__c, closeDate, probability, Description, " +
            "ExpectedRevenue, LastModifiedDate, createdDate, ownerid, owner.Name";
    private static final String ELEMENTS = "elements";
    private static final String FEED_ELEMENT_TYPE = "feedElementType";
    private static final String ACTOR = "actor";
    private static final String NAME = "name";
    private static final String BODY = "body";
    private static final String MESSAGE_SEGMENTS = "messageSegments";
    private static final String TYPE = "type";
    private static final String INLINE_IMAGE = "InlineImage";
    private static final String DOWNLOAD_URL = "downloadUrl";
    private static final String ITEMS = "items";
    private static final String CAPABILITIES = "capabilities";
    private static final String FILES = "files";
    private final SalesforceManager salesforceManager;
    private final PriceBookDaoImpl priceBookDao;
    private final UserDaoImpl userDao;

    @Inject
    public OpportunityDaoImpl(SalesforceManager salesforceManager, PriceBookDaoImpl priceBookDao, UserDaoImpl userDao) {
        this.salesforceManager = salesforceManager;
        this.priceBookDao = priceBookDao;
        this.userDao = userDao;
    }

    @Override
    public Opportunity getOpportunityById(String id) {
        return getOpportunitiesByQuery("id = '" + id + "'").stream().findFirst().orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public List<Opportunity> getOpportunities(long size, long page) {
        return getOpportunitiesByQuery("Select " + QUERY_FIELDS + " from Opportunity ORDER BY Name LIMIT " + size + " OFFSET " + Math.max((page - 1) * size, 0));
    }

    @Override
    public List<Opportunity> getOpportunitiesByQuery(String query) {
        if (!query.toLowerCase(Locale.ROOT).startsWith("select")) {
            query = "Select " + QUERY_FIELDS + " from Opportunity where " + query;
        }
        String response = salesforceManager.getRecordByQuery(query);
        SalesforceListResponse<SFOpportunity> oppList =
                GSON.fromJson(response, new TypeToken<SalesforceListResponse<SFOpportunity>>() {
                }.getType());
        if (oppList.records.size() > 0) {
            return oppList.records.stream().map(OpportunityImpl::new).collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("No records found");
        }
    }

    @Override
    public List<Opportunity> getNewOpportunitiesByLastDays(Double days) {
        return getOpportunitiesByQuery("CreatedDate = LAST_N_DAYS:" + new BigDecimal(days));
    }

    //Todo Need to add RegionWiseSummary entity
    @Override
    public List<Map<String, Object>> getOpportunitiesRegionWise() {
        List<Opportunity> opportunitiesByOwnerId = getOpportunitiesByQuery("select ownerId, SUM(AMOUNT) Amount from Opportunity group by ownerId");
        Map<String, RegionWiseSummary> regionRecordMap = new HashMap<>();
        for (Opportunity opportunity : opportunitiesByOwnerId) {
            User user = userDao.getUserById(opportunity.getOwnerId());
            String country = user.getCountry();
            RegionWiseSummary alreadyAdded = regionRecordMap.get(country);
            if (alreadyAdded != null) {
                alreadyAdded.amount += opportunity.getAmount();
            } else {
                RegionWiseSummary record = new RegionWiseSummary();
                record.region = country;
                record.amount = opportunity.getAmount();
                regionRecordMap.put(country, record);
            }
        }
        return regionRecordMap.values().stream().sequential().map(RegionWiseSummary::toMap).collect(Collectors.toList());
    }

    @Override
    public void updateOpportunity(Opportunity opportunity) {
        if (opportunity.getId() == null || opportunity.getId().isEmpty())
            throw new IllegalArgumentException("Opportunity Id cant be empty");
        SFOpportunity updatedOpportunity = SFOpportunity.from(OpportunityCatalogType.fromSFOpportunity(opportunity));
        SFOpportunity oldOpportunity = SFOpportunity.from(OpportunityCatalogType.fromSFOpportunity(getOpportunityById(updatedOpportunity.id)));
        Map<String, Object> diff = salesforceManager.getDifference(oldOpportunity, updatedOpportunity);
        salesforceManager.updateRecord(updatedOpportunity.id, GSON.toJson(diff), OPPORTUNITY);
    }

    @Override
    public void updateOpportunityStatus(String name, String status) {
        getOpportunitiesByQuery("name LIKE '%" + name + "%'").forEach(opportunity -> {
            SFOpportunity updatedOpportunity = SFOpportunity.from(OpportunityCatalogType.fromSFOpportunity(opportunity));
            updatedOpportunity.stageName = status;
            SFOpportunity oldOpportunity = SFOpportunity.from(OpportunityCatalogType.fromSFOpportunity(getOpportunityById(updatedOpportunity.id)));
            Map<String, Object> diff = salesforceManager.getDifference(oldOpportunity, updatedOpportunity);
            salesforceManager.updateRecord(updatedOpportunity.id, GSON.toJson(diff), OPPORTUNITY);
        });
    }

    @Override
    public void addOpportunity(Map<String, Object> entityAttributeField) {
        SFOpportunity opportunity1 = SFOpportunity.from(entityAttributeField);
        if (opportunity1.Pricebook2Id == null) {
            PriceBook priceBook = priceBookDao.getPriceBook();
            if (priceBook.getId() != null)
                opportunity1.Pricebook2Id = priceBook.getId();
        }
        salesforceManager.createRecord(GSON.toJson(opportunity1), OPPORTUNITY);
    }

    @Override
    public Double getTotalAmountClosedThisQuarter() {
        List<SFAggOpportunityOwnerTotal> list = getRevenueByQuery("SELECT SUM(AMOUNT) Total from Opportunity where IsClosed = false AND CloseDate=THIS_QUARTER GROUP BY STAGENAME");
        return list.isEmpty() ? 0.0 : Math.round(list.stream().map(r -> r.Total != null ? r.Total : 0.0).mapToDouble(Double::doubleValue).sum());
    }

    @Override
    public Double getRevenue(boolean isClosed) {
        List<SFAggOpportunityOwnerTotal> list = getRevenueByQuery("SELECT SUM(ExpectedRevenue) Total from Opportunity where IsClosed = " + isClosed + " AND CloseDate=THIS_QUARTER GROUP BY STAGENAME");
        return list.isEmpty() ? 0.0 : Math.round(list.stream().map(r -> r.Total != null ? r.Total : 0.0).mapToDouble(Double::doubleValue).sum());
    }

    @Override
    public List<Object> getExpectedRevenueOfQuarters(List<String> quarters, String query) {
        List<Object> results = new ArrayList<>();
        for (String quarter : quarters) {
            Map<String, Object> map = new HashMap<>();
            String sOQLQuery = query != null ? query : "Select sum(ExpectedRevenue) Total from Opportunity where (closeDate >= qstartdate and closeDate <= qendDate)";
            DatePlus.QuarterDates quarterDates = DatePlus.getQuarterDates(quarter);
            sOQLQuery = sOQLQuery.replace("qstartdate", quarterDates.startDate);
            sOQLQuery = sOQLQuery.replace("qendDate", quarterDates.endDate);
            List<SFAggOpportunityOwnerTotal> list = getRevenueByQuery(sOQLQuery.trim());
            map.put("Quarter Name", quarter);
            if (list.isEmpty()) {
                map.put("Total Revenue", "NA");
            } else {
                Double val = list.stream().map(r -> r.Total).collect(Collectors.toList()).get(0);
                map.put("Total Revenue", val != null ? val : 0);
            }
            results.add(map);
        }
        return results;
    }

    @Override
    public List<Object> getYTDClosedSumByOwnerId(String ownerId) {
        List<SFAggOpportunityOwnerTotal> sfAggOpportunityOwnerTotal = getRevenueByQuery("SELECT ownerid, owner.name, SUM(AMOUNT) Total from Opportunity where IsClosed = true AND CALENDAR_YEAR(CloseDate) = " + Calendar.getInstance().get(Calendar.YEAR) + " GROUP BY ownerid, owner.name Having ownerid = '" + ownerId + "'");
        return sfAggOpportunityOwnerTotal != null ? sfAggOpportunityOwnerTotal.stream().map(SFAggOpportunityOwnerTotal::toMap).collect(Collectors.toList()) : List.of();
    }

    @Override
    public List<Object> getYTDClosedSum() {
        List<SFAggOpportunityOwnerTotal> sfAggOpportunityOwnerTotal = getRevenueByQuery("SELECT ownerid, owner.name, SUM(AMOUNT) Total from Opportunity where IsClosed = true AND CALENDAR_YEAR(CloseDate) = " + Calendar.getInstance().get(Calendar.YEAR) + " GROUP BY ownerid, owner.name ");
        return sfAggOpportunityOwnerTotal != null ? sfAggOpportunityOwnerTotal.stream().map(SFAggOpportunityOwnerTotal::toMap).collect(Collectors.toList()) : List.of();
    }

    @Override
    public List getQuarterClosedSum(String ownerId) {
        String query = "SELECT ownerid, owner.name,SUM(AMOUNT) Total from Opportunity where IsClosed = true AND CloseDate=THIS_QUARTER GROUP BY ownerid, owner.name";
        if (ownerId != null && !ownerId.isBlank()) {
            query += " Having ownerid = '" + ownerId + "'";
        }
        List<SFAggOpportunityOwnerTotal> sfAggOpportunityOwnerTotal = getRevenueByQuery(query);
        return sfAggOpportunityOwnerTotal != null ? sfAggOpportunityOwnerTotal.stream().map(SFAggOpportunityOwnerTotal::toMap).collect(Collectors.toList()) : List.of();
    }

    @Override
    public List<Map<String, Object>> getCurrentQuarterQuota() {
        String res = salesforceManager.getRecordByQuery("SELECT QuotaOwner.Name, QuotaAmount FROM ForecastingQuota where startdate = THIS_QUARTER");
        SalesforceListResponse<Quota> oppList =
                GSON.fromJson(res, new TypeToken<SalesforceListResponse<Quota>>() {
                }.getType());
        return oppList.records.stream().map(Quota::to).collect(Collectors.toList());
    }

    private List<SFAggOpportunityOwnerTotal> getRevenueByQuery(String query) {
        String response = salesforceManager.getRecordByQuery(query);
        SalesforceListResponse<SFAggOpportunityOwnerTotal> oppList =
                GSON.fromJson(response, new TypeToken<SalesforceListResponse<SFAggOpportunityOwnerTotal>>() {
                }.getType());
        return oppList.records;
    }

    @Override
    public List<LatestPosts> getLatestPosts(String opportunityId, int numberOfPosts) throws IOException {
        String response = salesforceManager.getLatestNPostsByOpportunityId(opportunityId, numberOfPosts);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(response);
        JsonNode elementsNode = rootNode.get(ELEMENTS);

        List<LatestPosts> feedPosts = new ArrayList<>();
        for (JsonNode elementNode : elementsNode) {
            LatestPosts post = parseElementToPost(elementNode);
            if (post != null) {
                feedPosts.add(post);
            }
        }
        return feedPosts;
    }

    public void deleteOpportunity(String key) {
        salesforceManager.deleteRecordById(key, OPPORTUNITY);
    }

    private LatestPosts parseElementToPost(JsonNode elementNode) throws IOException {
        String feedElementType = elementNode.get(FEED_ELEMENT_TYPE).asText();
        if (!feedElementType.equalsIgnoreCase("FeedItem")) {
            return null;
        }

        LatestPosts post = new LatestPosts();
        post.actorName = getActorName(elementNode);
        post.postMessage = getPostMessage(elementNode);
        post.files = getFiles(elementNode);
        return post;
    }

    private String getActorName(JsonNode elementNode) {
        JsonNode actorNode = elementNode.get(ACTOR);
        return actorNode != null ? actorNode.get(NAME).asText() : null;
    }

    private String getPostMessage(JsonNode elementNode) {
        JsonNode bodyNode = elementNode.get(BODY);
        if (bodyNode != null && bodyNode.has(MESSAGE_SEGMENTS)) {
            StringBuilder textBuilder = new StringBuilder();
            for (JsonNode segmentNode : bodyNode.get(MESSAGE_SEGMENTS)) {
                if ("Text".equals(segmentNode.get(TYPE).asText())) {
                    textBuilder.append(segmentNode.get("text").asText());
                }
            }
            return textBuilder.toString();
        }
        return null;
    }

    private List<app.krista.model.base.File> getFiles(JsonNode elementNode) throws IOException {
        List<app.krista.model.base.File> files = new ArrayList<>();

        // Add inline images
        JsonNode bodyNode = elementNode.get(BODY);
        if (bodyNode != null && bodyNode.has(MESSAGE_SEGMENTS)) {
            for (JsonNode segmentNode : bodyNode.get(MESSAGE_SEGMENTS)) {
                if (isInlineImage(segmentNode)) {
                    files.add(downloadFile(segmentNode.get("url").asText()));
                }
            }
        }

        // Add attached files
        JsonNode filesNode = elementNode.get(CAPABILITIES).get(FILES);
        if (filesNode != null) {
            for (JsonNode fileNode : filesNode.get(ITEMS)) {
                files.add(downloadFile(fileNode.get(DOWNLOAD_URL).asText()));
            }
        }

        return files;
    }

    private boolean isInlineImage(JsonNode segmentNode) {
        return segmentNode.has(TYPE) && segmentNode.get(TYPE).asText().equals(INLINE_IMAGE);
    }

    private app.krista.model.base.File downloadFile(String downloadUrl) throws IOException {

        Response fileResponse = salesforceManager.getFileResponse(downloadUrl);
        MediaClient mediaClient = MediaClient.getInstance();

        String contentDisposition = fileResponse.getHeader("Content-Disposition");
        Pattern pattern = Pattern.compile("filename=\"(.*)\"");
        Matcher matcher = pattern.matcher(contentDisposition);
        String fileName = matcher.find() ? matcher.group(1) : "unknown";

        File localFile = saveToLocalTempDir(fileResponse.getStream(), fileName);
        String fileExtension = getFileExtension(localFile.getName());
        String mediaId = mediaClient.getMediaID(fileExtension);

        uploadFileToMediaClient(localFile, mediaId);

        return new app.krista.model.base.File(localFile.length(), localFile.getName(),
                fileExtension, mediaId);
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private File saveToLocalTempDir(InputStream inputStream, String filePath) throws IOException {
        File localFile = new File(filePath);
        Files.copy(inputStream, localFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return localFile;
    }

    private void uploadFileToMediaClient(File file, String mediaId) {
        MediaClient mediaClient = MediaClient.getInstance();
        mediaClient.upload(mediaClient.createMediaUrl(mediaId), file);
    }

}
