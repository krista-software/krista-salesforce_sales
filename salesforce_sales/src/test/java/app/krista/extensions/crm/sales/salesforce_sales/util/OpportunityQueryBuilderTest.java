package app.krista.extensions.crm.sales.salesforce_sales.util;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class OpportunityQueryBuilderTest {

    @Test
    public void testGetQuery_TrimsTrailingWhereAndAddsPagination() {
        String q = OpportunityQueryBuilder.getQuery(new ArrayList<>(), 10, 2, "UTC");
        assertTrue(q.startsWith("Select "));
        assertTrue(q.contains(" from Opportunity "));
        assertFalse("Should not contain dangling WHERE", q.contains("WHERE ORDER"));
        assertTrue(q.endsWith(" LIMIT 10 OFFSET 10"));
    }
}

