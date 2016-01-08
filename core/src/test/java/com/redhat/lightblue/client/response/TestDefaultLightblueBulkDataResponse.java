/**
 *
 */
package com.redhat.lightblue.client.response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.junit.Test;

import com.redhat.lightblue.client.Projection;
import com.redhat.lightblue.client.Query;
import com.redhat.lightblue.client.request.DataBulkRequest;
import com.redhat.lightblue.client.request.data.DataFindRequest;
import com.redhat.lightblue.client.util.JSON;

/**
 * @author bvulaj
 *
 */
public class TestDefaultLightblueBulkDataResponse {

    private static final String jsonResponse =
            "{\"responses\":[{\"seq\":0,\"response\":{\"status\":\"COMPLETE\",\"modifiedCount\":0,\"matchCount\":1,\"processed\":[{\"identity#\":1,\"entityName\":\"foo\",\"lastUpdateDate\":\"\",\"versionText\":\"1.0.0\",\"_id\":\"\",\"audits#\":5,\"objectType\":\"audit\"}]}},{\"seq\":1,\"response\":{\"status\":\"COMPLETE\",\"modifiedCount\":0,\"matchCount\":1,\"processed\":[{\"identity#\":1,\"entityName\":\"foo\",\"lastUpdateDate\":\"\",\"versionText\":\"1.0.0\",\"_id\":\"\",\"audits#\":5,\"objectType\":\"audit\"}]}}]}";

    private DefaultLightblueBulkDataResponse simpleSetUp() throws Exception {
        DataBulkRequest bulkRequest = new DataBulkRequest();

        DataFindRequest dfr = new DataFindRequest("foo", "bar");
        dfr.select(Projection.includeField("*"));
        dfr.where(Query.regex("foo", "*", 0));

        DataFindRequest dfr2 = new DataFindRequest("fooz", "bar");
        dfr2.select(Projection.includeField("*"));
        dfr2.where(Query.regex("fooz", "*", 0));

        bulkRequest.add(dfr);
        bulkRequest.add(dfr2);

        return new DefaultLightblueBulkDataResponse(jsonResponse, bulkRequest);
    }

    @Test
    public void testGetText() throws Exception {
        assertEquals(jsonResponse, simpleSetUp().getText());
    }

    @Test
    public void testGetJson() throws Exception {
        assertEquals(JSON.toJsonNode(jsonResponse), simpleSetUp().getJson());
    }

    @Test
    public void testGetResponseByRequest() throws Exception {
        DefaultLightblueBulkDataResponse bulkResponse = simpleSetUp();
        assertEquals(bulkResponse.getResponses().get(0), bulkResponse.getResponse(bulkResponse.getRequests().get(0)));
        assertEquals(bulkResponse.getResponses().get(1), bulkResponse.getResponse(bulkResponse.getRequests().get(1)));
    }

    @Test
    public void testGetResponseBySeq() throws Exception {
        DefaultLightblueBulkDataResponse bulkResponse = simpleSetUp();
        assertEquals(bulkResponse.getResponses().get(0), bulkResponse.getResponse(0));
        assertEquals(bulkResponse.getResponses().get(1), bulkResponse.getResponse(1));
    }

    @Test
    public void testResponseWithError() throws LightblueParseException {
        DataBulkRequest bulkRequest = new DataBulkRequest();

        DataFindRequest dfr = new DataFindRequest("foo", "bar");
        dfr.select(Projection.includeField("*"));
        dfr.where(Query.regex("foo", "*", 0));

        DataFindRequest dfrErrored = new DataFindRequest("fooled", "bar");
        dfrErrored.select(Projection.includeField("*"));
        dfrErrored.where(Query.regex("fooled", "*", 0));

        DataFindRequest dfr2 = new DataFindRequest("fooz", "bar");
        dfr2.select(Projection.includeField("*"));
        dfr2.where(Query.regex("fooz", "*", 0));

        bulkRequest.add(dfr);
        bulkRequest.add(dfrErrored);
        bulkRequest.add(dfr2);

        String jsonResponseWithError =
                "{\"responses\":["
                + "{\"seq\":0,\"response\":{\"status\":\"COMPLETE\",\"modifiedCount\":0,\"matchCount\":1,\"processed\":[{\"identity#\":1,\"entityName\":\"foo\",\"lastUpdateDate\":\"\",\"versionText\":\"1.0.0\",\"_id\":\"\",\"audits#\":5,\"objectType\":\"audit\"}]}},"
                + "{\"seq\":1,\"response\":{\"status\":\"ERROR\",\"modifiedCount\":0,\"matchCount\":0,\"errors\":[{\"context\":\"some context\",\"errorCode\":\"errCode\",\"msg\":\"some msg\",\"status\":\"ERROR\"}]}},"
                + "{\"seq\":2,\"response\":{\"status\":\"COMPLETE\",\"modifiedCount\":0,\"matchCount\":1,\"processed\":[{\"identity#\":1,\"entityName\":\"foo\",\"lastUpdateDate\":\"\",\"versionText\":\"1.0.0\",\"_id\":\"\",\"audits#\":5,\"objectType\":\"audit\"}]}}"
                + "]}";

        try {
            new DefaultLightblueBulkDataResponse(jsonResponseWithError, bulkRequest);
        } catch (LightblueBulkResponseException e) {
            //expected
            LightblueBulkDataResponse bulkResponse = e.getBulkResponse();
            assertNotNull(bulkResponse);
            assertEquals(bulkResponse.getResponses().get(0), bulkResponse.getResponse(dfr));
            assertEquals(bulkResponse.getResponses().get(1), bulkResponse.getResponse(dfrErrored));
            assertEquals(bulkResponse.getResponses().get(2), bulkResponse.getResponse(dfr2));

            Map<Integer, LightblueResponseException> erroredResponses = e.getLightblueResponseExceptions();
            assertNotNull(erroredResponses);
            assertEquals(1, erroredResponses.size());
            assertEquals(bulkResponse.getResponses().get(1), erroredResponses.get(1).getLightblueResponse());
        }
    }

    @Test(expected = LightblueParseException.class)
    public void testConstructor_With_UnexpectedJson() throws Exception {
        new DefaultLightblueBulkDataResponse("{}", new DataBulkRequest());
    }

    @Test(expected = LightblueParseException.class)
    public void testConstructor_With_NonArrayResponses() throws Exception {
        new DefaultLightblueBulkDataResponse("{\"responses\":\"notAnArray\"}", new DataBulkRequest());
    }

    @Test(expected = LightblueParseException.class)
    public void testConstructor_With_NonNumericSeq() throws Exception {
        new DefaultLightblueBulkDataResponse("{\"responses\":[{\"seq\":\"notint\",\"response\":{}}]}", new DataBulkRequest());
    }

}
