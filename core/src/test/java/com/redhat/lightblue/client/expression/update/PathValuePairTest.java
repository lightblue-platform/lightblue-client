package com.redhat.lightblue.client.expression.update;

import org.junit.Assert;
import org.junit.Test;

public class PathValuePairTest {

    @Test
    public void testToJsonConstructedWithStringsAndExpressionOperation() {
        PathValuePair expression = new PathValuePair("path", new ObjectRValue("value"));

        Assert.assertEquals("\"path\":\"value\"", expression.toJson());
    }

}
