package app.krista.extensions.crm.sales.salesforce_sales.impl;

import app.krista.extensions.crm.sales.salesforce_sales.model.SFPriceBook;
import app.krista.extensions.crm.sales.salesforce_sales.service.PriceBook;
import app.krista.extensions.crm.sales.salesforce_sales.service.PriceBookDao;
import app.krista.extensions.crm.sales.salesforce_sales.util.SalesforceListResponse;
import com.google.gson.reflect.TypeToken;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.util.stream.Collectors;

import static app.krista.extensions.crm.sales.salesforce_sales.util.Constants.GSON;

@Service
public class PriceBookDaoImpl implements PriceBookDao {

    public static final String QUERY_FIELDS = " Id,Name ";
    private final SalesforceManager salesforceManager;

    @Inject
    public PriceBookDaoImpl(SalesforceManager salesforceManager) {
        this.salesforceManager = salesforceManager;
    }

    @Override
    public PriceBook getPriceBook() {
        String res = salesforceManager.getRecordByQuery(
                "Select " + QUERY_FIELDS + " from Pricebook2 where Name='Standard'");
        SalesforceListResponse<SFPriceBook> oppList =
                GSON.fromJson(res, new TypeToken<SalesforceListResponse<SFPriceBook>>() {
                }.getType());
        if (oppList.records.size() > 0) {
            return oppList.records.stream().map(PriceBookImpl::new).collect(Collectors.toList()).stream().findFirst().orElseThrow();
        } else {
            return null;
        }
    }
}
