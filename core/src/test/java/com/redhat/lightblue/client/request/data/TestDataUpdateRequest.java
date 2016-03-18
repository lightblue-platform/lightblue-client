package com.redhat.lightblue.client.request.data;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import com.redhat.lightblue.client.Operation;
import com.redhat.lightblue.client.Projection;
import com.redhat.lightblue.client.Update;
import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;

public class TestDataUpdateRequest extends AbstractLightblueRequestTest {

    private final Projection testProjection1 = Projection.field("name", false, false);

    private DataUpdateRequest request;

	@Before
	public void setUp() throws Exception {
		request = new DataUpdateRequest(entityName, entityVersion);
	}

	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals("update", request.getOperationPathParam());
	}

    @Test
    public void testGetOperation() {
        Assert.assertEquals(Operation.UPDATE, request.getOperation());
    }

    @Test
    public void testGetHttpMethod() {
        Assert.assertEquals(HttpMethod.POST, request.getHttpMethod());
    }

    @Test
    public void testRequestWithRange() throws JSONException {
        request.returns(new Projection[]{testProjection1}, 0, 20);
        Update update = Update.set("field1", "field1Test");
        request.updates(update);

        String expected = "{\"update\":" + update.toJson() + ",\"projection\":" + testProjection1.toJson() + ",\"range\": [0,20]" + "}";
        JSONAssert.assertEquals(expected, request.getBody(), true);
    }

    @Test
    public void testRequestWithRangeNullTo() throws JSONException {
        request.returns(new Projection[]{testProjection1}, 0, null);
        Update update = Update.set("field1", "field1Test");
        request.updates(update);

        String expected = "{\"update\":" + update.toJson() + ",\"projection\":" + testProjection1.toJson() + ",\"range\": [0,null]" + "}";
        JSONAssert.assertEquals(expected, request.getBody(), true);
    }

}
