package com.redhat.lightblue.client.expression.query;

import com.redhat.lightblue.client.Literal;
import com.redhat.lightblue.client.Query;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

/**
 * Created by vkumar on 10/10/14.
 */
public class ArrayQueryTest {

    @Test
    public void testToJsonConstructedWithObjectElementExpression() throws JSONException {
        Query queryExpression = Query.arrayMatch("someArray", Query.withValue("item", Query.BinOp.eq, "1"));
        String expectedJson = "{\"array\":\"someArray\",\"elemMatch\":{\"field\":\"item\",\"op\":\"=\",\"rvalue\":\"1\"}}";

        JSONAssert.assertEquals(expectedJson, queryExpression.toString(), false);
    }

    @Test
    public void testToJsonConstructedWithcontainsvalue1andvalue2Expression() throws JSONException {
        String expectedJson = "{\"array\":\"someArray\",\"contains\":\"$all\",\"values\":[\"value1\",\"value2\",\"value3\"]}";

        String[] array = {"value1", "value2", "value3"};
        Query arrayExpression = Query.arrayContains("someArray", Query.ArrOp.all, Literal.values(array));

        JSONAssert.assertEquals(expectedJson, arrayExpression.toString(), false);
    }
}
