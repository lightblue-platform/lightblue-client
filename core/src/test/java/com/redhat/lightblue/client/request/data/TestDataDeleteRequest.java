package com.redhat.lightblue.client.request.data;

import com.redhat.lightblue.client.Query;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;
import org.skyscreamer.jsonassert.JSONAssert;

public class TestDataDeleteRequest extends AbstractLightblueRequestTest {

    private Query testQueryExpression = Query.withValue("test", Query.BinOp.neq, "hack");

    DataDeleteRequest request = new DataDeleteRequest();

    @Before
    public void setUp() throws Exception {
        request = new DataDeleteRequest(entityName, entityVersion);
    }

    @Test
    public void testGetOperationPathParam() {
        Assert.assertEquals("delete", request.getOperationPathParam());
    }

    @Test
    public void testRequestWithExpressionFormsProperBody() throws JSONException {
        request.where(testQueryExpression);

        String expected = "{\"query\":" + testQueryExpression.toJson() + "}";

        JSONAssert.assertEquals(expected, request.getBody(), false);
    }

}
