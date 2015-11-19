package com.redhat.lightblue.client.request.data;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import com.redhat.lightblue.client.Query;
import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;

public class TestDataDeleteRequest extends AbstractLightblueRequestTest {

    private final Query testQueryExpression = Query.withValue("test", Query.BinOp.neq, "hack");

    private DataDeleteRequest request;

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
