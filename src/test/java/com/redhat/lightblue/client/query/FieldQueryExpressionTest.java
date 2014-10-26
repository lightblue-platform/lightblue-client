package com.redhat.lightblue.client.query;

import com.redhat.lightblue.client.enums.ExpressionOperation;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

/**
 * Created by vkumar on 10/10/14.
 */
public class FieldQueryExpressionTest {

    @Test
    public void testToJsonConstructedWithStringsAndExpressionOperation() throws JSONException {
        FieldQueryExpression expression = new FieldQueryExpression("field1", ExpressionOperation.EQUALS, "field2");

        String expectedJson = "{\"field\":\"field1\",\"op\":\"=\",\"rfield\":\"field2\"}";

        JSONAssert.assertEquals(expectedJson, expression.toJson(), false);
    }
}
