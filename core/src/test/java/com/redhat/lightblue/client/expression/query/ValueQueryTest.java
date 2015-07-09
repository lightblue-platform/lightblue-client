package com.redhat.lightblue.client.expression.query;

import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import com.redhat.lightblue.client.enums.ExpressionOperation;
import com.redhat.lightblue.client.enums.NaryExpressionOperation;

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

    @Test
    public void testFieldInArray() throws JSONException {
        ValueQuery expression = new ValueQuery("foo.*.bar.*.uid = id");

        String expectedJson = "{\"field\":\"foo.*.bar.*.uid\",\"op\":\"=\",\"rvalue\":\"id\"}";

        JSONAssert.assertEquals(expectedJson, expression.toJson(), false);
    }

    @Test
    public void testToJsonConstructedWithStringsAndNaryExpressionOperation() throws JSONException {
        ValueQuery expression = new ValueQuery("field1", NaryExpressionOperation.IN, new String[]{"value1", "value2"});

        String expectedJson = "{\"field\":\"field1\",\"op\":\"$in\",\"values\":[\"value1\",\"value2\"]}";

        JSONAssert.assertEquals(expectedJson, expression.toJson(), false);
    }

    @Test
    public void testToJsonConstructedWithNaryIn() throws JSONException {
        ValueQuery expression = new ValueQuery("field1 $in [value1, value2]");
        String expectedJson = "{\"field\":\"field1\",\"op\":\"$in\",\"values\":[\"value1\",\"value2\"]}";

        JSONAssert.assertEquals(expectedJson, expression.toJson(), false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidExpressionSpaceInFieldName() {
        new ValueQuery("fie ld1 = Red Hat Enterprise Linux");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidExpressionUnrecognizedOperator() {
        new ValueQuery("field1 ~= Red Hat Enterprise Linux");
    }

    @Test
    public void testNullValue() throws JSONException {
        ValueQuery expression = new ValueQuery("field1", ExpressionOperation.EQUALS, null);
        String expectedJson = "{\"field\":\"field1\",\"op\":\"=\",\"rvalue\":null}";

        JSONAssert.assertEquals(expectedJson, expression.toJson(), false);
    }

    @Test
    public void testNullValueAmongInValues() throws JSONException {
        ValueQuery expression = new ValueQuery("field1", NaryExpressionOperation.IN, "blah", null);
        String expectedJson = "{\"field\":\"field1\",\"op\":\"$in\",\"values\":[\"blah\",null]}";
        System.out.println(expression.toJson());
        JSONAssert.assertEquals(expectedJson, expression.toJson(), false);
    }
}
