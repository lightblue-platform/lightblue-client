package com.redhat.lightblue.client.expression;

import com.redhat.lightblue.client.enums.ExpressionOperation;

import org.junit.Test;
import org.junit.Assert;

/**
 * Created by bmiller on 10/10/14.
 */
public class ValueExpressionTest {

    @Test
    public void testToJsonConstructedWithStringExpression() {
        ValueExpression expression = new ValueExpression("field1 = value1");
        String expectedJson = "{\"field\":\"field1\",\"op\":\"=\",\"rValue\":\"value1\"}";

        Assert.assertEquals(expectedJson, expression.toJson());
    }

    @Test
    public void testToJsonConstructedWithStringsAndExpressionOperation() {
        ValueExpression expression = new ValueExpression("field1", ExpressionOperation.EQUALS, "value1");

        String expectedJson = "{\"field\":\"field1\",\"op\":\"=\",\"rValue\":\"value1\"}";

        Assert.assertEquals(expectedJson, expression.toJson());
    }

    @Test
    public void testToStringCallsToJson() {
        ValueExpression expression = new ValueExpression("field1 = value1");

        Assert.assertEquals(expression.toString(), expression.toJson());
    }
}
