package app.krista.extensions.crm.sales.salesforce_sales.util;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class DatePlusTest {

    @Test
    public void testParseDate() {
        String validDateString = "2023-12-07";
        String invalidDateString = "invalid-date";

        Date validDate = DatePlus.parseDate(validDateString);
        Date invalidDate = DatePlus.parseDate(invalidDateString);

        assertNotNull(validDate);
        assertNull(invalidDate);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        assertEquals(validDateString, dateFormat.format(validDate));
    }
}
