package com.redhat.lightblue.client.expression.query;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import com.redhat.lightblue.client.enums.ExpressionOperation;

/**
 * Created by bmiller on 10/10/14.
 */
public class ValueQueryTest {

    @Test
    public void testToJsonConstructedWithStringExpression() throws JSONException {
        ValueQuery expression = new ValueQuery("field1 = value1");
        String expectedJson = "{\"field\":\"field1\",\"op\":\"=\",\"rvalue\":\"value1\"}";

        JSONAssert.assertEquals(expectedJson, expression.toJson(), false);
    }

    @Test
    public void testToJsonConstructedWithStringsAndExpressionOperation() throws JSONException {
        ValueQuery expression = new ValueQuery("field1", ExpressionOperation.EQUALS, "value1");

        String expectedJson = "{\"field\":\"field1\",\"op\":\"=\",\"rvalue\":\"value1\"}";

        JSONAssert.assertEquals(expectedJson, expression.toJson(), false);
    }

    @Test
    public void testToStringCallsToJson() throws JSONException {
        ValueQuery expression = new ValueQuery("field1 = value1");

        JSONAssert.assertEquals(expression.toString(), expression.toJson(), false);
    }

    @Test
    public void testValueWithWhiteSpace() throws JSONException {
        ValueQuery expression = new ValueQuery("field1 = Red Hat Enterprise Linux");

        String expectedJson = "{\"field\":\"field1\",\"op\":\"=\",\"rvalue\":\"Red Hat Enterprise Linux\"}";

        JSONAssert.assertEquals(expectedJson, expression.toJson(), false);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testInvalidExpression() {
        new ValueQuery("fie ld1 = Red Hat Enterprise Linux");
        Assert.fail();
    }

    @Test(expected=IllegalArgumentException.class)
    public void testInvalidOperator() {
        new ValueQuery("field1 ~= Red Hat Enterprise Linux");
        Assert.fail();
    }
}
