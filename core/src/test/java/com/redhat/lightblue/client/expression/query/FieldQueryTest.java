package com.redhat.lightblue.client.expression.query;

import com.redhat.lightblue.client.enums.ExpressionOperation;
import com.redhat.lightblue.client.enums.NaryExpressionOperation;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

/**
 * Created by vkumar on 10/10/14.
 */
public class FieldQueryTest {

    @Test
    public void testToJsonConstructedWithStringsAndExpressionOperation() throws JSONException {
        FieldQuery expression = new FieldQuery("field1", ExpressionOperation.EQUALS, "field2");

        String expectedJson = "{\"field\":\"field1\",\"op\":\"=\",\"rfield\":\"field2\"}";

        JSONAssert.assertEquals(expectedJson, expression.toJson(), false);
    }

    @Test
    public void testNaryFieldComparison() throws JSONException {
        FieldQuery expression = new FieldQuery("field1", NaryExpressionOperation.IN, "field2");

        String expectedJson = "{\"field\":\"field1\",\"op\":\"$in\",\"rfield\":\"field2\"}";

        JSONAssert.assertEquals(expectedJson, expression.toJson(), false);
    }
}
