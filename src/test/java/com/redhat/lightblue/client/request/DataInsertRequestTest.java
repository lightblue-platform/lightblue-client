package com.redhat.lightblue.client.request;

import com.redhat.lightblue.client.enums.RequestType;
import com.redhat.lightblue.client.projection.Projection;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DataInsertRequestTest {
    private class TestObj {
        public String field1 = "field1Test";
        public String field2 = "field2Test";
        public String toJson() {
            return "{\"field1\":\"" + field1 + "\",\"field2\":\"" + field2 + "\"}";
        }
    }

    private static final String TEST_ENTITY_NAME = "testEntity";
    private static final String TEST_ENTITY_VERSION = "0.0.1";

    private Projection testProjection1 = new Projection () {
        public String toJson() {
            return "{\"field1\":\"name\"}";
        }
    };

    private Projection testProjection2 = new Projection() {
        public String toJson() {
            return "{\"field2\":\"address\"}";
        }
    };

    @Test
    public void testRequestHasDataInsertRequestType() {
        DataInsertRequest request = new DataInsertRequest(TEST_ENTITY_NAME, TEST_ENTITY_VERSION);
        assertEquals(RequestType.DATA_INSERT, request.getRequestType());
    }

    @Test
    public void testRequestWithExpressionAndSingleProjectionFormsProperBody() throws JSONException {
        DataInsertRequest request = new DataInsertRequest(TEST_ENTITY_NAME, TEST_ENTITY_VERSION);
        request.returns(testProjection1);
        TestObj obj = new TestObj();
        request.create(obj);

        String expected = "{\"data\":[" + obj.toJson() + "],\"projection\":[" + testProjection1.toJson() + "]}";

        JSONAssert.assertEquals(expected, request.getBody(), false);
    }

    @Test
    public void testRequestWithMultipleProjectionsPassedAsArgumentsFormsProperBody() throws JSONException {
        DataInsertRequest request = new DataInsertRequest(TEST_ENTITY_NAME, TEST_ENTITY_VERSION);
        request.returns(testProjection1, testProjection2);
        TestObj obj = new TestObj();
        request.create(obj);

        String expected = "{\"data\":[" + obj.toJson() + "],\"projection\":[" + testProjection1.toJson() + "," + testProjection2.toJson() + "]}";

        JSONAssert.assertEquals(expected, request.getBody(), false);
    }

    @Test
    public void testRequestWithMultipleProjectionsPassedAsListFormsProperBody() throws JSONException {
        DataInsertRequest request = new DataInsertRequest(TEST_ENTITY_NAME, TEST_ENTITY_VERSION);
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
        DataInsertRequest request = new DataInsertRequest(TEST_ENTITY_NAME, TEST_ENTITY_VERSION);
        List<Projection> projections = new ArrayList<Projection>();
        projections.add(testProjection1);
        projections.add(testProjection2);

        request.returns(projections);
        TestObj obj1 = new TestObj();
        TestObj obj2 = new TestObj();
        request.create(obj1,obj2);

        String expected = "{\"data\":[" + obj1.toJson() + "," + obj2.toJson() + "],\"projection\":[" + testProjection1.toJson() + "," + testProjection2.toJson() + "]}";

        JSONAssert.assertEquals(expected, request.getBody(), false);
    }
}
