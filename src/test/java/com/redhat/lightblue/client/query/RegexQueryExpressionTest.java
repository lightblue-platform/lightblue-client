package com.redhat.lightblue.client.query;

import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

/**
 * Created by bmiller on 10/10/14.
 */
public class RegexQueryExpressionTest {
    @Test
    public void testToJsonCreatesWellFormedJson() throws JSONException {
        RegexQueryExpression expression = new RegexQueryExpression("field1", ".+pattern.*", false, true, false, true);
        String expectedJson = "{\"field\":\"field1\",\"pattern\":\".+pattern.*\","
                              + "\"caseInsensitive\":\"false\",\"extended\":\"true\","
                              + "\"multiline\":\"false\",\"dotall\":\"true\"}";

        JSONAssert.assertEquals(expression.toJson(), expectedJson, false);
    }

    @Test
    public void testToStringCallsToJson() throws JSONException {
        RegexQueryExpression expression = new RegexQueryExpression("field1", ".+pattern.*", false, true, false, true);

        JSONAssert.assertEquals(expression.toString(), expression.toJson(), false);
    }
}
