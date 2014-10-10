package com.redhat.lightblue.client.expression;

import org.junit.Test;
import org.junit.Assert;

/**
 * Created by bmiller on 10/10/14.
 */
public class RegexExpressiontest {
    @Test
    public void testToJsonCreatesWellFormedJson() {
        RegexExpression expression = new RegexExpression("field1", ".+pattern.*", false, true, false, true);
        String expectedJson = "{\"field\":\"field1\",\"pattern\":\".+pattern.*\","
                              + "\"caseInsensitive\":\"false\",\"extended\":\"true\","
                              + "\"multiline\":\"false\",\"dotall\":\"true\"}";

        Assert.assertEquals(expression.toJson(), expectedJson);
    }

    @Test
    public void testToStringCallsToJson() {
        RegexExpression expression = new RegexExpression("field1", ".+pattern.*", false, true, false, true);

        Assert.assertEquals(expression.toString(), expression.toJson());
    }
}
