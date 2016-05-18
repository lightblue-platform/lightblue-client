package com.redhat.lightblue.client.integration.test.data;

import static com.redhat.lightblue.util.test.AbstractJsonNodeTest.loadJsonNode;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.client.Projection;
import com.redhat.lightblue.client.Query;
import com.redhat.lightblue.client.Sort;
import com.redhat.lightblue.client.integration.test.LightblueClientTestHarness;
import com.redhat.lightblue.client.integration.test.pojo.Country;
import com.redhat.lightblue.client.request.data.DataFindRequest;
import com.redhat.lightblue.client.request.data.DataInsertRequest;

public class TestDataFindRequest extends LightblueClientTestHarness {

    public TestDataFindRequest() throws Exception {
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
        DataFindRequest findRequest = new DataFindRequest("country");
        findRequest.where(Query.withValue("objectType", Query.eq, "country"));
        findRequest.select(new Projection[]{Projection.includeFieldRecursively("*")}, 0, 1);
        findRequest.sort(Sort.asc("name"));
        Country[] countries = getLightblueClient().data(findRequest, Country[].class);

        assertNotNull(countries);
        assertEquals(1, countries.length);
        assertEquals("Poland", countries[0].getName());
    }

    /**
     * Test that deprecated range(Integer, Integer) method works correctly
     */
    @Test
    public void testWithDeprecatedRangeMethod_MiddleResultOnly() throws Exception {
        Country c3 = new Country();
        c3.setName("Mexico");
        c3.setIso2Code("MX");
        c3.setIso3Code("MEX");

        Country c1 = new Country();
        c1.setName("Poland");
        c1.setIso2Code("PL");
        c1.setIso3Code("POL");

        Country c4 = new Country();
        c4.setName("Thailand");
        c4.setIso2Code("TH");
        c4.setIso3Code("THD");

        Country c2 = new Country();
        c2.setName("United States");
        c2.setIso2Code("US");
        c2.setIso3Code("USA");

        DataInsertRequest insertRequest = new DataInsertRequest("country");
        insertRequest.create(c1, c2, c3, c4);
        getLightblueClient().data(insertRequest);

        //Do test
        DataFindRequest findRequest = new DataFindRequest("country");
        findRequest.where(Query.withValue("objectType", Query.eq, "country"));
        findRequest.range(1, 2);
        findRequest.select(new Projection[]{Projection.includeFieldRecursively("*")});
        findRequest.sort(Sort.asc("name"));
        Country[] countries = getLightblueClient().data(findRequest, Country[].class);

        assertNotNull(countries);
        assertEquals(2, countries.length);
        assertEquals("Poland", countries[0].getName());
        assertEquals("Thailand", countries[1].getName());
    }

}
