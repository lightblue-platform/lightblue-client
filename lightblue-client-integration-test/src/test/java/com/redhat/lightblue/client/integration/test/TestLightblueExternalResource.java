package com.redhat.lightblue.client.integration.test;

import static com.redhat.lightblue.util.test.AbstractJsonNodeTest.loadJsonNode;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.client.Projection;
import com.redhat.lightblue.client.integration.test.LightblueExternalResource.LightblueTestMethods;
import com.redhat.lightblue.client.integration.test.pojo.Country;
import com.redhat.lightblue.client.request.data.DataInsertRequest;
import com.redhat.lightblue.client.response.LightblueDataResponse;
import com.redhat.lightblue.rest.integration.FakeIdentityManager;

public class TestLightblueExternalResource {

    @ClassRule
    public static LightblueExternalResource lightblue = new LightblueExternalResource(new LightblueTestMethods() {

        @Override
        public JsonNode[] getMetadataJsonNodes() throws Exception {
            return new JsonNode[]{
                    loadJsonNode("./metadata/country.json")
            };
        }

    });

    @Before
    public void before() throws IOException, InterruptedException {
        lightblue.getControllerInstance().cleanupMongoCollections("country");

        if (lightblue.getControllerInstance().getIdentityManager() != null) {
            lightblue.changeIdentityManager(null);

            //TODO remove sleep. There is some sort of timing issue with the server restart.
            //DeploymentManager might be the key, but no way to get access to it today
            // https://github.com/undertow-io/undertow/blob/master/examples/src/main/java/io/undertow/examples/servlet/ServletServer.java
            Thread.sleep(5000);
        }
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

        LightblueDataResponse insertResponse = lightblue.getLightblueClient().data(insert);

        assertEquals(1, insertResponse.parseModifiedCount());
    }

    @Test
    public void testWithIdentityManager() throws Exception {
        lightblue.changeIdentityManager(
                new FakeIdentityManager().add("fakeuser", "fakepassword"));

        Country c = new Country();
        c.setName("Poland");
        c.setIso2Code("PL");
        c.setIso3Code("POL");

        DataInsertRequest insert = new DataInsertRequest(Country.objectType, Country.objectVersion);
        insert.create(c);
        insert.returns(Projection.includeFieldRecursively("*"));

        LightblueDataResponse insertResponse = lightblue.getLightblueClient("fakeuser", "fakepassword").data(insert);

        assertEquals(1, insertResponse.parseModifiedCount());
    }

}
