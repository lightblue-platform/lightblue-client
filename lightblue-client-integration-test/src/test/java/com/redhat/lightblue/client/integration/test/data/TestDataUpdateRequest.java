package com.redhat.lightblue.client.integration.test.data;

import static com.redhat.lightblue.util.test.AbstractJsonNodeTest.loadJsonNode;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.client.Projection;
import com.redhat.lightblue.client.Query;
import com.redhat.lightblue.client.Update;
import com.redhat.lightblue.client.integration.test.LightblueClientTestHarness;
import com.redhat.lightblue.client.integration.test.pojo.Country;
import com.redhat.lightblue.client.request.data.DataInsertRequest;
import com.redhat.lightblue.client.request.data.DataUpdateRequest;

public class TestDataUpdateRequest extends LightblueClientTestHarness {

    public TestDataUpdateRequest() throws Exception {
        super();
    }

    @Override
    protected JsonNode[] getMetadataJsonNodes() throws Exception {
        return new JsonNode[]{
            loadJsonNode("./metadata/country.json")
        };
    }

    @Before
    public void before() throws Exception {
        cleanupMongoCollections("country");
    }

    @Test
    public void testWithRange() throws Exception {
        Country c1 = new Country();
        c1.setName("Poland");
        c1.setIso2Code("PL");
        c1.setIso3Code("POL");

        Country c2 = new Country();
        c2.setName("United States");
        c2.setIso2Code("US");
        c2.setIso3Code("USA");

        DataInsertRequest insertRequest = new DataInsertRequest("country");
        insertRequest.create(c1, c2);
        getLightblueClient().data(insertRequest);

        //Do test
        DataUpdateRequest updateRequest = new DataUpdateRequest("country");
        updateRequest.updates(Update.set("name", "Canada"));
        updateRequest.where(Query.withValue("objectType", Query.eq, "country"));
        updateRequest.returns(new Projection[]{Projection.includeFieldRecursively("*")}, 0, 1);
        Country[] countries = getLightblueClient().data(updateRequest, Country[].class);

        assertNotNull(countries);
        assertEquals(1, countries.length);
        assertEquals("Canada", countries[0].getName());
    }

}
