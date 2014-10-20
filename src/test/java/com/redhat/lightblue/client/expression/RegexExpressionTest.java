package com.redhat.lightblue.client.expression;

import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

/**
 * Created by bmiller on 10/10/14.
 */
public class RegexExpressionTest {
    @Test
    public void testToJsonCreatesWellFormedJson() throws JSONException {
        RegexExpression expression = new RegexExpression("field1", ".+pattern.*", false, true, false, true);
        String expectedJson = "{\"field\":\"field1\",\"pattern\":\".+pattern.*\","
                              + "\"caseInsensitive\":\"false\",\"extended\":\"true\","
                              + "\"multiline\":\"false\",\"dotall\":\"true\"}";

        JSONAssert.assertEquals(expression.toJson(), expectedJson, false);
    }

    @Test
    public void testToStringCallsToJson() throws JSONException {
        RegexExpression expression = new RegexExpression("field1", ".+pattern.*", false, true, false, true);

        JSONAssert.assertEquals(expression.toString(), expression.toJson(), false);
    }
}
