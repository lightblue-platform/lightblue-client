package com.redhat.lightblue.client.integration.test.metadata;

import static com.redhat.lightblue.util.test.AbstractJsonNodeTest.loadJsonNode;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.client.integration.test.LightblueClientTestHarness;
import com.redhat.lightblue.client.request.metadata.MetadataCreateSchemaRequest;
import com.redhat.lightblue.client.response.LightblueResponse;

public class TestMetadataCreateSchemaRequest extends LightblueClientTestHarness {

    public TestMetadataCreateSchemaRequest() throws Exception {
        super();
    }

    @Override
    protected JsonNode[] getMetadataJsonNodes() throws Exception {
        return new JsonNode[]{loadJsonNode("./metadata/country.json")};
    }

    @Test
    public void testCreateNewSchema() throws Exception {
        MetadataCreateSchemaRequest request = new MetadataCreateSchemaRequest("country", "0.2.0-SNAPSHOT");
        request.setBodyJson(TestMetadataCreateSchemaRequest.class.getResourceAsStream("/metadata/countrySchema2.json"));

        LightblueResponse response = getLightblueClient().metadata(request);
        assertNotNull(response);
    }

}
