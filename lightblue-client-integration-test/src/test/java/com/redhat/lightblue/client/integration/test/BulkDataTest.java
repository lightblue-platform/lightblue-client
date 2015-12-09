package com.redhat.lightblue.client.integration.test;

import static com.redhat.lightblue.util.test.AbstractJsonNodeTest.loadJsonNode;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.client.Projection;
import com.redhat.lightblue.client.integration.test.pojo.Country;
import com.redhat.lightblue.client.request.DataBulkRequest;
import com.redhat.lightblue.client.request.data.DataInsertRequest;
import com.redhat.lightblue.client.response.DefaultLightblueBulkDataResponse;
import com.redhat.lightblue.client.response.LightblueDataResponse;

public class BulkDataTest extends LightblueClientTestHarness {

    public BulkDataTest() throws Exception {
        super();
    }

    @Before
    public void before() throws UnknownHostException {
        cleanupMongoCollections(Country.objectType);
    }

    @Override
    protected JsonNode[] getMetadataJsonNodes() throws Exception {
        return new JsonNode[] {
                loadJsonNode("metadata/country.json")
        };
    }

    @Test
    public void testBulkData() throws Exception {
        DataBulkRequest request = new DataBulkRequest();

        for (int x = 0; x < 100; x++) {
            DataInsertRequest insert = new DataInsertRequest(Country.objectType, Country.objectVersion);
            insert.create(new Country(String.valueOf(x), "123" + x, "456" + x, null));
            insert.returns(Projection.includeField("name"));

            request.add(insert);
        }

        DefaultLightblueBulkDataResponse bulkResp = getLightblueClient().bulkData(request);

        assertNotNull(bulkResp);

        //Responses should be in the same order.
        int n = 0;
        for (LightblueDataResponse response : bulkResp.getResponses()) {
            assertNotNull(response);
            assertEquals(String.valueOf(n), response.parseProcessed(Country.class).getName());
            n++;
        }
    }

}
