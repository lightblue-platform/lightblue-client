package com.redhat.lightblue.client.expression;

import com.redhat.lightblue.client.enums.ExpressionOperation;

import org.junit.Test;
import org.junit.Assert;

/**
 * Created by vkumar on 10/10/14.
 */
public class FieldExpressionTest {

    @Test
    public void testToJsonConstructedWithStringsAndExpressionOperation() {
        FieldExpression expression = new FieldExpression("field1", ExpressionOperation.EQUALS, "field2");

        String expectedJson = "{\"field\":\"field1\",\"op\":\"=\",\"rfield\":\"field2\"}";

        Assert.assertEquals(expectedJson, expression.toJson());
    }
}
