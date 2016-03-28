package com.redhat.lightblue.client.integration.test.data;

import static com.redhat.lightblue.util.test.AbstractJsonNodeTest.loadJsonNode;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.client.Projection;
import com.redhat.lightblue.client.integration.test.LightblueClientTestHarness;
import com.redhat.lightblue.client.integration.test.pojo.Country;
import com.redhat.lightblue.client.request.data.DataSaveRequest;

public class TestDataSaveRequest extends LightblueClientTestHarness {

    public TestDataSaveRequest() throws Exception {
        super();
    }

    @Override
    protected JsonNode[] getMetadataJsonNodes() throws Exception {
        return new JsonNode[]{
                loadJsonNode("./metadata/country.json")
        };
    }

    @Test
    public void testWithProjection() throws Exception {
        Country c1 = new Country();
        c1.setName("Poland");
        c1.setIso2Code("PL");
        c1.setIso3Code("POL");

        DataSaveRequest request = new DataSaveRequest("country");
        request.create(c1);
        request.returns(Projection.includeField("name"));
        request.setUpsert(true);
        Country[] savedCountries = getLightblueClient().data(request,Country[].class);

        assertNotNull(savedCountries);
        assertEquals(1, savedCountries.length);
        assertNotNull(savedCountries[0]);
        assertEquals("Poland", savedCountries[0].getName());
        assertNull(savedCountries[0].getIso2Code());
        assertNull(savedCountries[0].getIso3Code());
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

        DataSaveRequest request = new DataSaveRequest("country");
        request.create(c1, c2);
        request.returns(new Projection[]{Projection.includeFieldRecursively("*")}, 0, 1);
        request.setUpsert(true);
        Country[] savedCountries = getLightblueClient().data(request,Country[].class);

        assertNotNull(savedCountries);
        assertEquals(1, savedCountries.length);
    }

}
