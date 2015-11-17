package com.redhat.lightblue.client.request;

import com.redhat.lightblue.client.Sort;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

/**
 * Created by vkumar on 10/10/14.
 */
public class SortConditionTest {

    @Test
    public void testToJsonConstructedSortCondiitionExpression() throws JSONException {

        String expectedJson = "{\"field\":\"$asc\"}";

        Sort condition = Sort.asc("field");
        JSONAssert.assertEquals(expectedJson, condition.toString(), false);
    }
}
