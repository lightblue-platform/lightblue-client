package com.redhat.lightblue.client.expression.query;

import com.redhat.lightblue.client.Query;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

/**
 * Created by vkumar on 10/10/14.
 */
public class FieldQueryTest {

    @Test
    public void testToJsonConstructedWithStringsAndExpressionOperation() throws JSONException {
        Query expression = Query.withField("field1", Query.BinOp.eq, "field2");

        String expectedJson = "{\"field\":\"field1\",\"op\":\"=\",\"rfield\":\"field2\"}";

        JSONAssert.assertEquals(expectedJson, expression.toString(), false);
    }

    @Test
    public void testNaryFieldComparison() throws JSONException {
        Query expression = Query.withFieldValues("field1", Query.NaryOp.in, "field2");

        String expectedJson = "{\"field\":\"field1\",\"op\":\"$in\",\"rfield\":\"field2\"}";

        JSONAssert.assertEquals(expectedJson, expression.toString(), false);
    }
}
