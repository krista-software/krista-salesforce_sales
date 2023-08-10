package app.krista.extensions.crm.sales.salesforce_sales.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ValidatorsTest {
    @Test
    public void testGetDoubleOrZeroWithValidInput() {
        Double input = 5.5;
        Double result = Validators.getDoubleOrZero(input);
        assertEquals(input, result, 0.0);
    }

    @Test
    public void testGetDoubleOrZeroWithNull() {
        Double result = Validators.getDoubleOrZero(null);
        assertEquals(0, result, 0.0);
    }

    @Test
    public void testGetStringOrEmptyWithValidInput(){
        String input = "hello";
        String result = Validators.getStringOrEmpty(input);
        assertEquals(input, result);
    }

    @Test
    public void testGetStringOrEmptyWithNull(){
        String result = Validators.getStringOrEmpty(null);
        assertEquals("", result);
    }
}

