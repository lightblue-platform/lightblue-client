package com.redhat.lightblue.client.request.data;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import com.redhat.lightblue.client.projection.Projection;
import com.redhat.lightblue.client.request.AbstractLightblueDataRequest.Operation;
import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;

public class TestDataSaveRequest extends AbstractLightblueRequestTest {

    private class TestObj {
        public String field1 = "field1Test";
        public String field2 = "field2Test";

        public String toJson() {
            return "{\"field1\":\"" + field1 + "\",\"field2\":\"" + field2 + "\"}";
        }
    }

    private final Projection testProjection1 = new Projection() {
        @Override
        public String toJson() {
            return "{\"field1\":\"name\"}";
        }
    };

    private final Projection testProjection2 = new Projection() {
        @Override
        public String toJson() {
            return "{\"field2\":\"address\"}";
        }
    };

    DataSaveRequest request = new DataSaveRequest();

    @Before
    public void setUp() throws Exception {
        request = new DataSaveRequest(entityName, entityVersion);
    }

    @Test
    public void testGetOperationPathParam() {
        Assert.assertEquals(Operation.SAVE.getPathParam(), request.getOperationPathParam());
    }

    @Test
    public void testRequestWithSingleProjectionFormsProperBody() throws JSONException {
        request.returns(testProjection1);
        TestObj obj = new TestObj();
        request.create(obj);

        String expected = "{\"data\":[" + obj.toJson() + "],\"projection\":[" + testProjection1.toJson() + "]}";

        JSONAssert.assertEquals(expected, request.getBody(), false);
    }

    @Test
    public void testRequestWithUpsert() throws JSONException {
        request.setUpsert(true);
        request.returns(testProjection1);
        TestObj obj = new TestObj();
        request.create(obj);

        String expected = "{\"data\":[" + obj.toJson() + "],\"projection\":[" + testProjection1.toJson() + "],\"upsert\":true}";

        JSONAssert.assertEquals(expected, request.getBody(), false);
    }

    @Test
    public void testRequestWithMultipleProjectionsPassedAsArgumentsFormsProperBody() throws JSONException {
        request.returns(testProjection1, testProjection2);
        TestObj obj = new TestObj();
        request.create(obj);

        String expected = "{\"data\":[" + obj.toJson() + "],\"projection\":[" + testProjection1.toJson() + "," + testProjection2.toJson() + "]}";

        JSONAssert.assertEquals(expected, request.getBody(), false);
    }

    @Test
    public void testRequestWithMultipleProjectionsPassedAsListFormsProperBody() throws JSONException {
        List<Projection> projections = new ArrayList<Projection>();
        projections.add(testProjection1);
        projections.add(testProjection2);

        request.returns(projections);
        TestObj obj = new TestObj();
        request.create(obj);

        String expected = "{\"data\":[" + obj.toJson() + "],\"projection\":[" + testProjection1.toJson() + "," + testProjection2.toJson() + "]}";

        JSONAssert.assertEquals(expected, request.getBody(), false);
    }

    @Test
    public void testRequestWithMultipleCreations() throws JSONException {
        List<Projection> projections = new ArrayList<Projection>();
        projections.add(testProjection1);
        projections.add(testProjection2);

        request.returns(projections);
        TestObj obj1 = new TestObj();
        TestObj obj2 = new TestObj();
        request.create(obj1, obj2);

        String expected = "{\"data\":[" + obj1.toJson() + "," + obj2.toJson() + "],\"projection\":[" + testProjection1.toJson() + "," + testProjection2.toJson() + "]}";

        JSONAssert.assertEquals(expected, request.getBody(), false);
    }
}
