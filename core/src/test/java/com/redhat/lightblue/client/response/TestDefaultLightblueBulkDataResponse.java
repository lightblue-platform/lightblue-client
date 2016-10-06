/**
 *
 */
package com.redhat.lightblue.client.response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.SortedMap;

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

    private static final String jsonResponse
            = "{\"responses\":["
            + "{\"seq\":0,\"response\":{\"status\":\"COMPLETE\",\"modifiedCount\":0,\"matchCount\":1,\"processed\":[{\"identity#\":1,\"entityName\":\"foo\",\"lastUpdateDate\":\"\",\"versionText\":\"1.0.0\",\"_id\":\"\",\"audits#\":5,\"objectType\":\"audit\"}]}},"
            + "{\"seq\":1,\"response\":{\"status\":\"COMPLETE\",\"modifiedCount\":0,\"matchCount\":1,\"processed\":[{\"identity#\":1,\"entityName\":\"foo\",\"lastUpdateDate\":\"\",\"versionText\":\"1.0.0\",\"_id\":\"\",\"audits#\":5,\"objectType\":\"audit\"}]}}"
            + "]}";

    private static final String jsonResponseWithError
            = "{\"responses\":["
            + "{\"seq\":0,\"response\":{\"status\":\"COMPLETE\",\"modifiedCount\":0,\"matchCount\":1,\"processed\":[{\"identity#\":1,\"entityName\":\"foo\",\"lastUpdateDate\":\"\",\"versionText\":\"1.0.0\",\"_id\":\"\",\"audits#\":5,\"objectType\":\"audit\"}]}},"
            + "{\"seq\":1,\"response\":{\"status\":\"ERROR\",\"modifiedCount\":0,\"matchCount\":0,\"errors\":[{\"context\":\"some context\",\"errorCode\":\"errCode\",\"msg\":\"some msg\",\"status\":\"ERROR\"}]}},"
            + "{\"seq\":2,\"response\":{\"status\":\"COMPLETE\",\"modifiedCount\":0,\"matchCount\":1,\"processed\":[{\"identity#\":1,\"entityName\":\"foo\",\"lastUpdateDate\":\"\",\"versionText\":\"1.0.0\",\"_id\":\"\",\"audits#\":5,\"objectType\":\"audit\"}]}}"
            + "]}";

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
    public void testBulkException_Responses() throws Exception {
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

        try {
            new DefaultLightblueBulkDataResponse(jsonResponseWithError, bulkRequest);
            fail();
        } catch (LightblueBulkResponseException e) {
            //expected

            //getSequencedResponses
            SortedMap<Integer, LightblueDataResponse> seqResponses = e.getBulkResponse().getSequencedResponses();
            assertNotNull(seqResponses);
            assertEquals(3, seqResponses.size());

            assertEquals(seqResponses.get(0), e.getBulkResponse().getResponse(dfr));
            assertEquals(seqResponses.get(1), e.getBulkResponse().getResponse(dfrErrored));
            assertEquals(seqResponses.get(2), e.getBulkResponse().getResponse(dfr2));

            //getResponses
            List<LightblueDataResponse> responses = e.getBulkResponse().getResponses();
            assertNotNull(responses);
            assertEquals(3, responses.size());

            assertEquals(responses.get(0), e.getBulkResponse().getResponse(dfr));
            assertEquals(responses.get(1), e.getBulkResponse().getResponse(dfrErrored));
            assertEquals(responses.get(2), e.getBulkResponse().getResponse(dfr2));
        }
    }

    @Test
    public void testBulkException_SuccessfulResponses() throws Exception {
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

        try {
            new DefaultLightblueBulkDataResponse(jsonResponseWithError, bulkRequest);
            fail();
        } catch (LightblueBulkResponseException e) {
            //expected
            assertEquals(1, e.getLightblueResponseExceptions().size());

            //getSequencedSuccessfulResponses
            SortedMap<Integer, LightblueDataResponse> seqResponses = e.getBulkResponse().getSequencedSuccessfulResponses();
            assertNotNull(seqResponses);
            assertEquals(2, seqResponses.size());

            assertEquals(seqResponses.get(0), e.getBulkResponse().getResponse(dfr));
            assertNull(seqResponses.get(1));
            assertEquals(seqResponses.get(2), e.getBulkResponse().getResponse(dfr2));

            //getSuccessfulResponses
            List<LightblueDataResponse> responses = e.getBulkResponse().getSuccessfulResponses();
            assertNotNull(responses);
            assertEquals(2, responses.size());

            //Responses will be ordered sequentially
            //but as errored responses are not included, the positions could be off.
            assertEquals(responses.get(0), e.getBulkResponse().getResponse(dfr));
            assertFalse(responses.contains(e.getBulkResponse().getResponse(dfrErrored)));
            assertEquals(responses.get(1), e.getBulkResponse().getResponse(dfr2));
        }
    }

    @Test
    public void testBulkException_ErroredResponses() throws Exception {
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

        try {
            new DefaultLightblueBulkDataResponse(jsonResponseWithError, bulkRequest);
            fail();
        } catch (LightblueBulkResponseException e) {
            //expected
            assertEquals(1, e.getLightblueResponseExceptions().size());

            //getSequencedResponsesWithErrors
            SortedMap<Integer, LightblueDataResponse> seqResponses = e.getBulkResponse().getSequencedResponsesWithErrors();
            assertNotNull(seqResponses);
            assertEquals(1, seqResponses.size());

            assertNull(seqResponses.get(0));
            assertEquals(seqResponses.get(1), e.getBulkResponse().getResponse(
                    dfrErrored));
            assertNull(seqResponses.get(2));

            //getResponsesWithErrors
            List<LightblueDataResponse> responses = e.getBulkResponse().getResponsesWithErrors();
            assertNotNull(responses);
            assertEquals(1, responses.size());

            //Responses will be ordered sequentially
            //but as successful responses are not included, the positions could be off.
            assertFalse(responses.contains(e.getBulkResponse().getResponse(dfr)));
            assertEquals(responses.get(0), e.getBulkResponse().getResponse(dfrErrored));
            assertFalse(responses.contains(e.getBulkResponse().getResponse(dfr2)));

            assertEquals("Message should contain all responses", "Errors returned in responses: {\"responses\":[{\"seq\":0,\"response\":{\"status\":\"COMPLETE\",\"modifiedCount\":0,\"matchCount\":1,\"processed\":[{\"identity#\":1,\"entityName\":\"foo\",\"lastUpdateDate\":\"\",\"versionText\":\"1.0.0\",\"_id\":\"\",\"audits#\":5,\"objectType\":\"audit\"}]}},{\"seq\":1,\"response\":{\"status\":\"ERROR\",\"modifiedCount\":0,\"matchCount\":0,\"errors\":[{\"context\":\"some context\",\"errorCode\":\"errCode\",\"msg\":\"some msg\",\"status\":\"ERROR\"}]}},{\"seq\":2,\"response\":{\"status\":\"COMPLETE\",\"modifiedCount\":0,\"matchCount\":1,\"processed\":[{\"identity#\":1,\"entityName\":\"foo\",\"lastUpdateDate\":\"\",\"versionText\":\"1.0.0\",\"_id\":\"\",\"audits#\":5,\"objectType\":\"audit\"}]}}]}", e.getMessage());
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
