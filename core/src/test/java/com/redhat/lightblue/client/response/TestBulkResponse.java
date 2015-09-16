/**
 * 
 */
package com.redhat.lightblue.client.response;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.redhat.lightblue.client.Projection;
import com.redhat.lightblue.client.Query;
import com.redhat.lightblue.client.request.AbstractLightblueDataRequest;
import com.redhat.lightblue.client.request.DataBulkRequest;
import com.redhat.lightblue.client.request.data.DataFindRequest;
import com.redhat.lightblue.client.util.JSON;

/**
 * @author bvulaj
 *
 */
public class TestBulkResponse {

    private BulkLightblueResponse bulkResponse;
    private DataBulkRequest bulkRequest;
    private static final String jsonResponse =
            "{\"responses\":[{\"seq\":0,\"response\":{\"status\":\"COMPLETE\",\"modifiedCount\":0,\"matchCount\":1,\"processed\":[{\"identity#\":1,\"entityName\":\"foo\",\"lastUpdateDate\":\"\",\"versionText\":\"1.0.0\",\"_id\":\"\",\"audits#\":5,\"objectType\":\"audit\"}]}},{\"seq\":1,\"response\":{\"status\":\"COMPLETE\",\"modifiedCount\":0,\"matchCount\":1,\"processed\":[{\"identity#\":1,\"entityName\":\"foo\",\"lastUpdateDate\":\"\",\"versionText\":\"1.0.0\",\"_id\":\"\",\"audits#\":5,\"objectType\":\"audit\"}]}}]}";

    @Before
    public void setUp() throws Exception {
        bulkRequest = new DataBulkRequest();

        DataFindRequest dfr = new DataFindRequest("foo", "bar");
        dfr.select(Projection.includeField("*"));
        dfr.where(Query.regex("foo", "*", 0));

        DataFindRequest dfr2 = new DataFindRequest("fooz", "bar");
        dfr2.select(Projection.includeField("*"));
        dfr2.where(Query.regex("fooz", "*", 0));

        bulkRequest.add(dfr);
        bulkRequest.add(dfr2);

        bulkResponse = new BulkLightblueResponse(jsonResponse, bulkRequest);
    }

    @Test
    public void testGetText() {
        assertEquals(jsonResponse, bulkResponse.getText());
    }

    @Test
    public void testGetJson() {
        assertEquals(JSON.toJsonNode(jsonResponse), bulkResponse.getJson());
    }

    @Test
    public void testGetResponse() {
        assertEquals(bulkResponse.getResponses().get(0), bulkResponse.getResponse(bulkRequest.getRequests().get(0)));
    }

}
