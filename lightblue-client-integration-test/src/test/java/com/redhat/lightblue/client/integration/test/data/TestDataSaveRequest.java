package com.redhat.lightblue.client.integration.test.data;

import static com.redhat.lightblue.util.test.AbstractJsonNodeTest.loadJsonNode;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.client.Projection;
import com.redhat.lightblue.client.Query;
import com.redhat.lightblue.client.Literal;
import com.redhat.lightblue.client.integration.test.LightblueClientTestHarness;
import com.redhat.lightblue.client.integration.test.pojo.Country;
import com.redhat.lightblue.client.request.data.DataSaveRequest;
import com.redhat.lightblue.client.request.data.DataFindRequest;

import com.redhat.lightblue.client.response.LightblueDataResponse;
import com.redhat.lightblue.client.response.DefaultLightblueDataResponse;

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

    @Before
    public void before() throws Exception {
        cleanupMongoCollections("country");
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
        Country[] savedCountries = getLightblueClient().data(request, Country[].class);

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
        Country[] savedCountries = getLightblueClient().data(request, Country[].class);

        assertNotNull(savedCountries);
        assertEquals(1, savedCountries.length);
    }

    @Test
    public void testDocVer() throws Exception {
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
        getLightblueClient().data(request, Country[].class);

        DataFindRequest f=new DataFindRequest("country");
        f.select(Projection.includeFieldRecursively("*"));
        f.where(Query.withValue("iso2Code",Query.eq,Literal.value("PL")));
        
        LightblueDataResponse r=getLightblueClient().data(f);
        Country c=r.parseProcessed(Country.class);
        assertNotNull(c);
        assertEquals(1,r.getResultMetadata().length);
        String ver=r.getResultMetadata()[0].getDocumentVersion();
        assertNotNull(ver);

        request=new DataSaveRequest("country");
        c.setName("updated");
        request.create(c);
        request.ifCurrent(r.getResultMetadata()[0].getDocumentVersion());
        getLightblueClient().data(request);

        r=getLightblueClient().data(f);
        c=r.parseProcessed(Country.class);
        assertEquals("updated",c.getName());
        assertTrue(!r.getResultMetadata()[0].getDocumentVersion().equals(ver));
    }

    @Test
    public void testDocVerWithFailure() throws Exception {
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
        getLightblueClient().data(request, Country[].class);

        DataFindRequest f=new DataFindRequest("country");
        f.select(Projection.includeFieldRecursively("*"));
        f.where(Query.withValue("iso2Code",Query.eq,Literal.value("PL")));
        
        DefaultLightblueDataResponse r=getLightblueClient().data(f);
        Country c=r.parseProcessed(Country.class);
        assertNotNull(c);
        assertEquals(1,r.getResultMetadata().length);
        assertNotNull(r.getResultMetadata()[0].getDocumentVersion());
        System.out.println("Version:"+r.getResultMetadata()[0].getDocumentVersion());
        String ver=r.getResultMetadata()[0].getDocumentVersion();

        // Someone else updates it
        DataSaveRequest r2=new DataSaveRequest("country");
        c.setName("updated2");
        r2.create(c);
        getLightblueClient().data(r2);

        r=getLightblueClient().data(f);
        c=r.parseProcessed(Country.class);
        System.out.println("Country:"+c.getName());
        System.out.println(r.getResultMetadata()[0]);

        request=new DataSaveRequest("country");
        c.setName("updated");        
        request.create(c);
        request.ifCurrent(ver);
        try {
            r=getLightblueClient().data(request);
            fail();
        } catch (Exception e) {}

        r=getLightblueClient().data(f);
        c=r.parseProcessed(Country.class);
        assertEquals("updated2",c.getName());
        System.out.println("Country:"+c.getName());
        System.out.println(r.getResultMetadata()[0]);
    }

}
