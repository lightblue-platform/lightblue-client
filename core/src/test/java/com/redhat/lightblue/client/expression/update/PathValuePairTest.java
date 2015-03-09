package com.redhat.lightblue.client.expression.update;

import org.junit.Assert;
import org.junit.Test;

public class PathValuePairTest {

    @Test
    public void testToJsonConstructedWithStringsAndExpressionOperation() {
        PathValuePair expression = new PathValuePair("path", new ObjectRValue("value"));

        Assert.assertEquals("\"path\":\"value\"", expression.toJson());
    }

    @Test
    public void testToJsonConstructedWithStringsAndExpressionOperation_CreatedWithNullValue() {
        PathValuePair expression = new PathValuePair("path", null);

        Assert.assertEquals("\"path\":\"$null\"", expression.toJson());
    }

    @Test
    public void testToJsonConstructedWithStringsAndExpressionOperation_CreatedWithNullReturningRValue() {
        PathValuePair expression = new PathValuePair("path", new RValue() {

            @Override
            public String toJson() {
                return null;
            }

        });

        Assert.assertEquals("\"path\":\"$null\"", expression.toJson());
    }

    @Test
    public void testToJsonConstructedWithStringsAndExpressionOperation_CreatedWithNullStringReturningRValue() {
        PathValuePair expression = new PathValuePair("path", new RValue() {

            @Override
            public String toJson() {
                return "null";
            }

        });

        Assert.assertEquals("\"path\":\"$null\"", expression.toJson());
    }

}
