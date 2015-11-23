package com.redhat.lightblue.client.integration.test;

import static com.redhat.lightblue.util.test.AbstractJsonNodeTest.loadJsonNode;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.client.LightblueClient;
import com.redhat.lightblue.client.Projection;
import com.redhat.lightblue.client.integration.test.LightblueExternalResource.LightblueTestMethods;
import com.redhat.lightblue.client.integration.test.pojo.Country;
import com.redhat.lightblue.client.request.data.DataInsertRequest;
import com.redhat.lightblue.client.response.LightblueDataResponse;

public class TestLightblueExternalResource {

    private LightblueClient client;

    @ClassRule
    public static LightblueExternalResource lightblue = new LightblueExternalResource(new LightblueTestMethods() {

        @Override
        public JsonNode[] getMetadataJsonNodes() throws Exception {
            return new JsonNode[]{loadJsonNode("./metadata/country.json")};
        }

    });

    @Before
    public void before() {
        client = lightblue.getLightblueClient();
    }

    @Test
    public void test() throws Exception {
        Country c = new Country();
        c.setName("Poland");
        c.setIso2Code("PL");
        c.setIso3Code("POL");

        DataInsertRequest insert = new DataInsertRequest(Country.objectType, Country.objectVersion);
        insert.create(c);
        insert.returns(Projection.includeFieldRecursively("*"));

        LightblueDataResponse insertResponse = client.data(insert);

        assertEquals(1, insertResponse.parseModifiedCount());
    }

}
