package com.redhat.lightblue.client.request.data;

import com.redhat.lightblue.client.expression.query.Query;
import com.redhat.lightblue.client.request.AbstractLightblueRequestTest;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

public class TestDataDeleteRequest extends AbstractLightblueRequestTest {

    private Query testQueryExpression = new Query() {
        public String toJson() {
            return "{\"field1\":\"test\",\"op\":\"$ne\",\"rValue\":\"hack\"}";
        }
    };

	DataDeleteRequest request = new DataDeleteRequest();

	@Before
	public void setUp() throws Exception {
		request = new DataDeleteRequest(entityName, entityVersion);
	}

	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals(DataDeleteRequest.PATH_PARAM_DELETE, request.getOperationPathParam());
	}

    @Test
    public void testRequestWithExpressionFormsProperBody() throws JSONException {
        request.where(testQueryExpression);

        String expected = "{\"query\":" + testQueryExpression.toJson() + "}";

        JSONAssert.assertEquals(expected, request.getBody(), false);
    }

}
