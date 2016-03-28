package com.redhat.lightblue.client.request.data;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import com.redhat.lightblue.client.Operation;
import com.redhat.lightblue.client.Projection;
import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;


public class TestDataSaveRequest extends AbstractLightblueRequestTest {

    private class TestObj {
        public String field1 = "field1Test";
        public String field2 = "field2Test";

        public String toJson() {
            return "{\"field1\":\"" + field1 + "\",\"field2\":\"" + field2 + "\"}";
        }
    }

    private final Projection testProjection1 = Projection.field("name", false, false);

    private final Projection testProjection2 = Projection.field("address", false, false);

    private DataSaveRequest request;

    @Before
    public void setUp() throws Exception {
        request = new DataSaveRequest(entityName, entityVersion);
    }

    @Test
    public void testGetOperationPathParam() {
        Assert.assertEquals("save", request.getOperationPathParam());
    }

    @Test
    public void testGetOperation() {
        Assert.assertEquals(Operation.SAVE, request.getOperation());
    }

    @Test
    public void testGetHttpMethod() {
        Assert.assertEquals(HttpMethod.POST, request.getHttpMethod());
    }

    @Test
    public void testRequestWithSingleProjectionFormsProperBody() throws JSONException {
        request.returns(testProjection1);
        TestObj obj = new TestObj();
        request.create(obj);

        String expected = "{\"data\":" + obj.toJson() + ",\"projection\":" + testProjection1.toJson() + "}";

        JSONAssert.assertEquals(expected, request.getBody(), false);
    }

    @Test
    public void testRequestWithUpsert() throws JSONException {
        request.setUpsert(true);
        request.returns(testProjection1);
        TestObj obj = new TestObj();
        request.create(obj);

        String expected = "{\"data\":" + obj.toJson() + ",\"projection\":" + testProjection1.toJson() + ",\"upsert\":true}";

        JSONAssert.assertEquals(expected, request.getBody(), false);
    }

    @Test
    public void testRequestWithMultipleProjectionsPassedAsArgumentsFormsProperBody() throws JSONException {
        request.returns(testProjection1, testProjection2);
        TestObj obj = new TestObj();
        request.create(obj);

        String expected = "{\"data\":" + obj.toJson() + ",\"projection\":[" + testProjection1.toJson() + "," + testProjection2.toJson() + "]}";

        JSONAssert.assertEquals(expected, request.getBody(), false);
    }

    @Test
    public void testRequestWithMultipleProjectionsPassedAsListFormsProperBody() throws JSONException {
        request.returns(testProjection1, testProjection2);
        TestObj obj = new TestObj();
        request.create(obj);

        String expected = "{\"data\":" + obj.toJson() + ",\"projection\":[" + testProjection1.toJson() + "," + testProjection2.toJson() + "]}";

        JSONAssert.assertEquals(expected, request.getBody(), false);
    }

    @Test
    public void testRequestWithMultipleCreations() throws JSONException {
        request.returns(testProjection1, testProjection2);
        TestObj obj1 = new TestObj();
        TestObj obj2 = new TestObj();
        request.create(obj1, obj2);

        String expected = "{\"data\":[" + obj1.toJson() + "," + obj2.toJson() + "],\"projection\":[" + testProjection1.toJson() + "," + testProjection2.toJson() + "]}";

        JSONAssert.assertEquals(expected, request.getBody(), false);
    }

    @Test
    public void testRequestWithRange() throws JSONException {
        request.returns(new Projection[]{testProjection1}, 0, 20);
        TestObj obj = new TestObj();
        request.create(obj);

        String expected = "{\"data\":" + obj.toJson() + ",\"projection\":" + testProjection1.toJson() + ",\"from\": 0, \"maxResults\" : 20" + "}";
        JSONAssert.assertEquals(expected, request.getBody(), true);
    }

    @Test
    public void testRequestWithRangeNullTo() throws JSONException {
        request.returns(new Projection[]{testProjection1}, 0, null);
        TestObj obj = new TestObj();
        request.create(obj);

        String expected = "{\"data\":" + obj.toJson() + ",\"projection\":" + testProjection1.toJson() + ",\"from\": 0" + "}";
        JSONAssert.assertEquals(expected, request.getBody(), true);
    }
}
