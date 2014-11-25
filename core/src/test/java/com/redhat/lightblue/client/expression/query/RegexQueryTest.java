package com.redhat.lightblue.client.expression.query;

import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

/**
 * Created by bmiller on 10/10/14.
 */
public class RegexQueryTest {
    @Test
    public void testToJsonCreatesWellFormedJson() throws JSONException {
        RegexQuery expression = new RegexQuery("field1", ".+pattern.*", false, true, false, true);
        String expectedJson = "{\"field\":\"field1\",\"regex\":\".+pattern.*\","
                              + "\"caseInsensitive\":\"false\",\"extended\":\"true\","
                              + "\"multiline\":\"false\",\"dotall\":\"true\"}";

        JSONAssert.assertEquals(expression.toJson(), expectedJson, false);
    }

    @Test
    public void testToStringCallsToJson() throws JSONException {
        RegexQuery expression = new RegexQuery("field1", ".+pattern.*", false, true, false, true);

        JSONAssert.assertEquals(expression.toString(), expression.toJson(), false);
    }
}
