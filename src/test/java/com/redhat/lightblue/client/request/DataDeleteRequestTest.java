package com.redhat.lightblue.client.request;

import com.redhat.lightblue.client.enums.RequestType;
import com.redhat.lightblue.client.query.QueryExpression;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.junit.Assert.assertEquals;

/**
 * Created by jblashka on 10/28/14.
 */
public class DataDeleteRequestTest {
    private static final String TEST_ENTITY_NAME = "testEntity";
    private static final String TEST_ENTITY_VERSION = "0.0.1";

    private QueryExpression testQueryExpression = new QueryExpression() {
        public String toJson() {
            return "{\"field1\":\"test\",\"op\":\"$ne\",\"rValue\":\"hack\"}";
        }
    };

    @Test
    public void testRequestHasDataFindRequestType() {
        DataDeleteRequest request = new DataDeleteRequest(TEST_ENTITY_NAME, TEST_ENTITY_VERSION);
        assertEquals(RequestType.DATA_DELETE, request.getRequestType());
    }

    @Test
    public void testRequestWithExpressionFormsProperBody() throws JSONException {
        DataDeleteRequest request = new DataDeleteRequest(TEST_ENTITY_NAME, TEST_ENTITY_VERSION);
        request.where(testQueryExpression);

        String expected = "{\"query\":" + testQueryExpression.toJson() + "}";

        JSONAssert.assertEquals(expected, request.getBody(), false);
    }
}
