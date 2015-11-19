package com.redhat.lightblue.client.integration.test.metadata;

import static com.redhat.lightblue.util.test.AbstractJsonNodeTest.loadJsonNode;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.client.integration.test.LightblueClientTestHarness;
import com.redhat.lightblue.client.request.metadata.MetadataCreateNewEntityRequest;
import com.redhat.lightblue.client.response.LightblueResponse;

public class TestMetadataCreateNewEntityRequest  extends LightblueClientTestHarness {

    public TestMetadataCreateNewEntityRequest() throws Exception {
        super();
    }

    @Override
    protected JsonNode[] getMetadataJsonNodes() throws Exception {
        return new JsonNode[]{};
    }

    @Test
    public void testCreateNewSchema() throws Exception {
        MetadataCreateNewEntityRequest request = new MetadataCreateNewEntityRequest("country", "0.1.0-SNAPSHOT");
        request.setBodyJson(loadJsonNode("./metadata/country.json"));

        LightblueResponse response = getLightblueClient().metadata(request);
        assertNotNull(response);
    }

}
