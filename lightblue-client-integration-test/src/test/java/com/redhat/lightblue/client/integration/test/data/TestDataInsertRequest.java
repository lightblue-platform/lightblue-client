package com.redhat.lightblue.client.integration.test.data;

import static com.redhat.lightblue.util.test.AbstractJsonNodeTest.loadJsonNode;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.client.Projection;
import com.redhat.lightblue.client.integration.test.LightblueClientTestHarness;
import com.redhat.lightblue.client.integration.test.pojo.Country;
import com.redhat.lightblue.client.request.data.DataInsertRequest;

public class TestDataInsertRequest extends LightblueClientTestHarness {

    public TestDataInsertRequest() throws Exception {
        super();
    }

    @Override
    protected JsonNode[] getMetadataJsonNodes() throws Exception {
        return new JsonNode[]{
                loadJsonNode("./metadata/country.json")
        };
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

        DataInsertRequest request = new DataInsertRequest("country");
        request.create(c1, c2);
        request.returns(new Projection[]{Projection.includeFieldRecursively("*")}, 0, 0);
        Country[] insertedCountries = getLightblueClient().data(request, Country[].class);

        assertNotNull(insertedCountries);
        assertEquals(1, insertedCountries.length);
    }

}
