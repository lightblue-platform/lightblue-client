package com.redhat.lightblue.client.expression.update;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by vkumar on 10/10/14.
 */
public class SetUpdateTest {

    @Test
    public void testToJsonConstructedWithStringsAndExpressionOperation() {
        RValue rValueExpression = new RValue() {

            @Override
            public String toJson() {
                return "value";
            }
        };
        PathValuePair pathvaluepair = new PathValuePair("path", rValueExpression);
        SetUpdate expression = new SetUpdate(pathvaluepair);

        String expectedJson = "{\"$set\":{\"path\":value}}";

        Assert.assertEquals(expectedJson, expression.toJson());
    }
}
