package com.redhat.lightblue.client.integration.test.metadata;

import static com.redhat.lightblue.util.test.AbstractJsonNodeTest.loadJsonNode;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.client.integration.test.LightblueClientTestHarness;
import com.redhat.lightblue.client.request.metadata.MetadataRemoveEntityRequest;
import com.redhat.lightblue.client.response.LightblueResponse;

public class TestMetadataRemoveEntityRequest extends LightblueClientTestHarness {

    public TestMetadataRemoveEntityRequest() throws Exception {
        super();
    }

    @Override
    protected JsonNode[] getMetadataJsonNodes() throws Exception {
        return new JsonNode[]{loadJsonNode("./metadata/country.json")};
    }

    @Test
    public void testCreateNewEntityInfo() throws Exception {
        MetadataRemoveEntityRequest request = new MetadataRemoveEntityRequest("country");

        LightblueResponse response = getLightblueClient().metadata(request);
        assertNotNull(response);
    }

}
