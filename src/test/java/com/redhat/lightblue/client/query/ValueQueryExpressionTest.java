package com.redhat.lightblue.client.query;

import com.redhat.lightblue.client.enums.ExpressionOperation;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

/**
 * Created by bmiller on 10/10/14.
 */
public class ValueQueryExpressionTest {

    @Test
    public void testToJsonConstructedWithStringExpression() throws JSONException {
        ValueQueryExpression expression = new ValueQueryExpression("field1 = value1");
        String expectedJson = "{\"field\":\"field1\",\"op\":\"=\",\"rvalue\":\"value1\"}";

        JSONAssert.assertEquals(expectedJson, expression.toJson(), false);
    }

    @Test
    public void testToJsonConstructedWithStringsAndExpressionOperation() throws JSONException {
        ValueQueryExpression expression = new ValueQueryExpression("field1", ExpressionOperation.EQUALS, "value1");

        String expectedJson = "{\"field\":\"field1\",\"op\":\"=\",\"rvalue\":\"value1\"}";

        JSONAssert.assertEquals(expectedJson, expression.toJson(), false);
    }

    @Test
    public void testToStringCallsToJson() throws JSONException {
        ValueQueryExpression expression = new ValueQueryExpression("field1 = value1");

        JSONAssert.assertEquals(expression.toString(), expression.toJson(), false);
    }
}
