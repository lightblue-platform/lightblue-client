package com.redhat.lightblue.client.request.data;

import com.redhat.lightblue.client.projection.Projection;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.ArrayList;
import java.util.List;

public class TestDataInsertRequest extends AbstractLightblueRequestTest {

    private class TestObj {
        public String field1 = "field1Test";
        public String field2 = "field2Test";
        public String toJson() {
            return "{\"field1\":\"" + field1 + "\",\"field2\":\"" + field2 + "\"}";
        }
    }

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

	DataInsertRequest request = new DataInsertRequest();
	
	@Before
	public void setUp() throws Exception {
		request = new DataInsertRequest(entityName, entityVersion);
	}

	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals(DataInsertRequest.PATH_PARAM_INSERT, request.getOperationPathParam());
	}

    @Test
    public void testRequestWithExpressionAndSingleProjectionFormsProperBody() throws JSONException {
        request.returns(testProjection1);
        TestObj obj = new TestObj();
        request.create(obj);

        String expected = "{\"data\":[" + obj.toJson() + "],\"projection\":[" + testProjection1.toJson() + "]}";

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
        request.create(obj1,obj2);

        String expected = "{\"data\":[" + obj1.toJson() + "," + obj2.toJson() + "],\"projection\":[" + testProjection1.toJson() + "," + testProjection2.toJson() + "]}";

        JSONAssert.assertEquals(expected, request.getBody(), false);
    }

}
