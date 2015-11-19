package com.redhat.lightblue.client.integration.test.metadata;

import static com.redhat.lightblue.util.test.AbstractJsonNodeTest.loadJsonNode;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.client.integration.test.LightblueClientTestHarness;
import com.redhat.lightblue.client.request.metadata.MetadataUpdateEntityInfoRequest;
import com.redhat.lightblue.client.response.LightblueResponse;

public class TestMetadataUpdateEntityInfoRequest extends LightblueClientTestHarness{

    public TestMetadataUpdateEntityInfoRequest() throws Exception {
        super();
    }

    @Override
    protected JsonNode[] getMetadataJsonNodes() throws Exception {
        return new JsonNode[]{loadJsonNode("./metadata/country.json")};
    }

    @Test
    public void testCreateNewEntityInfo() throws Exception {
        MetadataUpdateEntityInfoRequest request = new MetadataUpdateEntityInfoRequest("country");
        request.setBodyJson(loadJsonNode("./metadata/countryEntityInfo2.json"));

        LightblueResponse response = getLightblueClient().metadata(request);
        assertNotNull(response);
    }

}
